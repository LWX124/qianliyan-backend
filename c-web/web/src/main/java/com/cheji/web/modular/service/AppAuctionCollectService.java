package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.constant.AuctionConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppAuctionBidMapper;
import com.cheji.web.modular.mapper.AppAuctionCollectMapper;
import com.cheji.web.modular.mapper.AppAuctionMapper;
import com.cheji.web.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cheji.web.util.UseDateUtils.backStringDate;

/**
 * <p>
 * 收藏
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionCollectService extends ServiceImpl<AppAuctionCollectMapper, AppAuctionCollectEntity> implements IService<AppAuctionCollectEntity> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionImgService appAuctionImgService;

    @Autowired
    private AppAuctionBidService appAuctionBidService;

    @Autowired
    private AppAuctionOrderService orderService;

    @Autowired
    private AppAuctionBidMapper appAuctionBidMapper;

    @Resource
    private AppAuctionCollectMapper collectMapper;
    @Resource
    private AppAuctionMapper appAuctionMapper;

    public JSONObject collectList(Integer id, Page page) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppAuctionCollectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.orderBy("create_time", false);
        List<AppAuctionCollectEntity> appAuctionCollectEntities = selectPage(page, wrapper).getRecords();
        List<JSONObject> data = appAuctionCollectEntities.stream().map(a -> {
//            AppAuctionEntity auction = (AppAuctionEntity) appAuctionService.detail2(a.getCarId(),a.getUserId()).get("data");
            AppAuctionEntity auction = appAuctionMapper.selectById(a.getCarId());
            return auc2collDto(auction, Long.valueOf(id));
        }).collect(Collectors.toList());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }

    public Integer isCollect(Long id, Long carId) {
        AppAuctionCollectEntity collect = collectMapper.isCollect(id, carId);
        if (Objects.isNull(collect)) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * @param page   竞买车
     * @param userId userid
     * @return
     */
    public JSONObject bidList(Page page, Long userId) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppAuctionBidEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId).eq("valid", "0");
        wrapper.orderBy("create_time", false);

        int start = (page.getCurrent() - 1) * 10;

        List<AppAuctionEntity> bids = appAuctionBidMapper.selectListByUser(userId, start);

//        List<AppAuctionBidEntity> bids = appAuctionBidService.selectPage(page,wrapper).getRecords();
//        List<AppAuctionBidEntity> bids = appAuctionBidService.selectPage(page,wrapper).getRecords();
        if (bids.size() > 0) {
            List<JSONObject> data = bids.stream().map(x -> {
                return auc2collDto(x, userId);
            }).collect(Collectors.toList());

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", data);
            return result;
        } else {
            result.put("code", 200);
            result.put("msg", "暂无数据");
            result.put("data", null);
            return result;
        }
    }

    //待过户
    public JSONObject waitTransfer(Page page, Integer id) {
        return comQuery(page, id, AppAuctionConstant.ZERO);
    }

    //已过户
    public JSONObject transfered(Page page, Integer id) {
        return comQuery(page, id, AppAuctionConstant.TWELVE);
    }

    public JSONObject comQuery(Page page, Integer id, Integer transfer_state) {
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        List<AppAuctionOrderEntity> appAuctionOrderEntities;
        if (AppAuctionConstant.TWELVE == transfer_state) {
            appAuctionOrderEntities = orderService.selectPage(page, new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", id).eq("state", "1").orderBy("create_time", false)).getRecords();
        } else {
            appAuctionOrderEntities = orderService.selectPage(page, new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", id).eq("state", "0").orderBy("create_time", false)).getRecords();
        }
        for (AppAuctionOrderEntity appAuctionOrderEntity : appAuctionOrderEntities) {
            EntityWrapper<AppAuctionEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("id", appAuctionOrderEntity.getCarId());
            AppAuctionEntity auction = appAuctionService.selectOne(wrapper);
            JSONObject object = auc2collDto(auction, Long.valueOf(id));
            array.add(object);
        }

        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", array);
        return result;
    }


    //详情结果转DTO
    public JSONObject auc2collDto(AppAuctionEntity appAuction, Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", appAuction.getId());
        if (appAuction.getBrand().length() > 13) {
            jsonObject.put("brand", appAuction.getBrand().substring(0, 13));
        } else {
            jsonObject.put("brand", appAuction.getBrand());
        }
        jsonObject.put("register_date", backStringDate(appAuction.getRegisterDate()));
        jsonObject.put("insuredAmount", appAuction.getInsuredAmount());
        jsonObject.put("accidentType", appAuction.getAccidentType());
        jsonObject.put("parkingPlace", appAuction.getParkingPlace());
        jsonObject.put("fixedPrice", appAuction.getFixedPrice());
        jsonObject.put("luxuryCarPrice", appAuction.getLuxuryCarPrice());
        jsonObject.put("phoneNumber",appAuction.getPhone());

        String bidCarKey = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, appAuction.getId());
        //从redis获取最高出价
        Object mustPrice = redisTemplate.opsForValue().get(bidCarKey);
        if (mustPrice == null) {
            //缓存中没有就去数据库拿
            mustPrice = appAuctionBidMapper.selectMustPrice(appAuction.getId());
            if (mustPrice == null) {
                mustPrice = appAuction.getPrice();
            }
        }
        jsonObject.put("price", mustPrice);


        jsonObject.put("sourceType", appAuction.getSourceType());
        String label = "";

        if (appAuction.getRegisterDate() != null) {
            label += DateUtils.formateDate(appAuction.getRegisterDate().getTime(), "yyyy") + "年 ";
        }

        if (appAuction.getMileage() != null) {
            BigDecimal decimal = new BigDecimal(appAuction.getMileage()).divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal(appAuction.getMileage()).compareTo(new BigDecimal("1000")) < 0) {
                //小于1000公里不转换
                label += appAuction.getMileage() + "公里";
            }else {
                label += decimal + "万公里";
            }
        }

        if (appAuction.getFirstAmount() != null) {
            label +=" 首付"+ appAuction.getFirstAmount() + "万";
        }

        jsonObject.put("label", label);



        jsonObject.put("insurance", appAuction.getInsurance());
        jsonObject.put("registerDate", backStringDate(appAuction.getRegisterDate()));
        jsonObject.put("carState", appAuction.getCarState());
        jsonObject.put("collect", isCollect(id, appAuction.getId()));
        Long carId = appAuction.getId();
        AppAuctionOrderEntity orderEntity = orderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId));
        if (Objects.nonNull(orderEntity) && Objects.nonNull(orderEntity.getUserId())) {
            jsonObject.put("userId", orderEntity.getUserId());//购买人id
        }
        EntityWrapper<AppAuctionUpEntity> wrapper2 = new EntityWrapper<>();
        wrapper2.eq("car_id", appAuction.getId());
        AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper2);
        if (appAuctionUpEntity != null) {
            jsonObject.put("beginTime", appAuctionUpEntity.getBeginTime().getTime());
            jsonObject.put("endTime", appAuctionUpEntity.getEndTime().getTime());
        } else {
            jsonObject.put("beginTime", null);
            jsonObject.put("endTime", null);
        }
        if (appAuctionImgService.selectByCarId(appAuction.getId()).size() > 0) {
            List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectByCarId(appAuction.getId());
            for (AppAuctionImgEntity appAuctionImgEntity : appAuctionImgEntities) {
                if (AppAuctionConstant.ONE.toString().equals(appAuctionImgEntity.getState())) {
                    jsonObject.put("img", appAuctionImgEntity);
                    break;
                    //如果是二手车，照片取状态为10的
                } else if (appAuction.getSourceType() == 2 && AppAuctionConstant.TEN.toString().equals(appAuctionImgEntity.getState())) {
                    jsonObject.put("img", appAuctionImgEntity);
                    break;
                }
            }
        }
        return jsonObject;
    }

    public JSONObject auc2collDto2(AppAuctionEntity auction, Long id) {
        AppAuctionUpEntity upAuction = appAuctionUpService.selectOne(new EntityWrapper<AppAuctionUpEntity>().eq("car_id", auction.getId()));
        JSONObject result = new JSONObject();
        if (Objects.isNull(upAuction)) {
            return result;
        }
        result.put("id", auction.getId());
        result.put("beginTime", upAuction.getBeginTime().getTime());
        result.put("endTime", upAuction.getEndTime().getTime());
        if (auction.getBrand().length() > 13) {
            result.put("brand", auction.getBrand().substring(0, 13));
        } else {
            result.put("brand", auction.getBrand());
        }
        result.put("insurance", auction.getInsurance());
        result.put("accidentType", auction.getAccidentType());
        result.put("parkingPlace", auction.getParkingPlace());
        result.put("carState", auction.getCarState());
        result.put("fixedPrice", auction.getFixedPrice());
        result.put("collect", isCollect(id, auction.getId()));
        Long carId = auction.getId();
        AppAuctionOrderEntity orderEntity = orderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId));
        if (Objects.nonNull(orderEntity) && Objects.nonNull(orderEntity.getUserId())) {
            result.put("userId", orderEntity.getUserId());//购买人id
        }
        List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectByCarId(auction.getId());
        if (appAuctionImgEntities != null && appAuctionImgEntities.size() > 0) {
            for (AppAuctionImgEntity appAuctionImgEntity : appAuctionImgEntities) {
                if ("1".equals(appAuctionImgEntity.getState())) {
                    result.put("img", appAuctionImgEntity);
                }
            }
        } else {
            result.put("img", null);
        }
//        result.put("price", auction.getPrice());

        String bidCarKey = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, auction.getId());
        //从redis获取最高出价
        Object mustPrice = redisTemplate.opsForValue().get(bidCarKey);
        if (mustPrice == null) {
            //缓存中没有就去数据库拿
            mustPrice = appAuctionBidMapper.selectMustPrice(auction.getId());
            if (mustPrice == null) {
                mustPrice = auction.getPrice();
            }
        }
        result.put("price", mustPrice);

        result.put("registerDate", DateUtils.getFormatDate(auction.getRegisterDate()));
        result.put("insuredAmount", auction.getInsuredAmount());
        return result;
    }


}
