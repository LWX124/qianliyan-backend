package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppAuctionCollectMapper;
import com.cheji.web.modular.mapper.AppAuctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionImgService appAuctionImgService;

    @Autowired
    private AppAuctionBidService appAuctionBidService;

    @Autowired
    private AppAuctionOrderService orderService;

    @Resource
    private AppAuctionCollectMapper collectMapper;
    @Resource
    private AppAuctionMapper appAuctionMapper;

    public JSONObject collectList(Integer id, Page page) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppAuctionCollectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id);
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
     * @param page 竞买车
     * @param id   userid
     * @return
     */
    public JSONObject bidList(Page page, Long id) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppAuctionBidEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id).eq("valid", "0");
//        List<AppAuctionBidEntity> bids = appAuctionBidService.selectPage(page,wrapper).getRecords();
        List<AppAuctionBidEntity> bids = appAuctionBidService.selectList(wrapper);
        if (bids.size() > 0) {
            List<JSONObject> data = bids.stream().collect(
                    Collectors.groupingBy(AppAuctionBidEntity::getCarId, Collectors.collectingAndThen(
                            Collectors.reducing((t1, t2) -> t1.getBid().compareTo(t2.getBid()) > 0 ? t1 : t2),
                            Optional::get
                    ))
            ).keySet().stream().map(carId -> {
                AppAuctionEntity auction = (AppAuctionEntity) appAuctionService.detail(Long.valueOf(carId)).get("data");
                return auc2collDto(auction, id);
            }).collect(Collectors.toList());

            List<JSONObject> collect = data.stream().limit(20).collect(Collectors.toList());
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", collect);
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
            appAuctionOrderEntities = orderService.selectPage(page, new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", id).eq("state", "1")).getRecords();
        } else {
            appAuctionOrderEntities = orderService.selectPage(page, new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", id).eq("state", "0")).getRecords();
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
        jsonObject.put("register_date", appAuction.getRegisterDate());
        jsonObject.put("insuredAmount", appAuction.getInsuredAmount());
        jsonObject.put("accidentType", appAuction.getAccidentType());
        jsonObject.put("parkingPlace", appAuction.getParkingPlace());
        jsonObject.put("fixedPrice", appAuction.getFixedPrice());
        jsonObject.put("price", appAuction.getPrice());
        jsonObject.put("insurance", appAuction.getInsurance());
        jsonObject.put("registerDate", appAuction.getRegisterDate());
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
            jsonObject.put("beginTime", appAuctionUpEntity.getBeginTime());
            jsonObject.put("endTime", appAuctionUpEntity.getEndTime());
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
        result.put("beginTime", upAuction.getBeginTime());
        result.put("endTime", upAuction.getEndTime());
        if (auction.getBrand().length() > 13) {
            result.put("brand", auction.getBrand().substring(0, 13));
        } else {
            result.put("brand", auction.getBrand());
        }
        result.put("insurance", auction.getInsurance());
        result.put("accidentType", auction.getAccidentType());
        result.put("parkingPlace", auction.getParkingPlace());
        result.put("carState", auction.getCarState());
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
        result.put("price", auction.getPrice());
        result.put("registerDate", auction.getRegisterDate());
        result.put("insuredAmount", auction.getInsuredAmount());
        return result;
    }


}
