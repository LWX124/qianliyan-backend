package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionBailConstant;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.constant.AuctionConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.dto.AuctionCarListDto;
import com.cheji.web.modular.dto.AuctionMyDto;
import com.cheji.web.modular.mapper.*;
import com.cheji.web.util.AssertUtil;
import com.cheji.web.util.PriceUtils;
import com.cheji.web.util.UseDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 拍卖相关
 * </p>
 *
 * @author yang
 */
@Slf4j
@Service
public class AppAuctionService extends ServiceImpl<AppAuctionMapper, AppAuctionEntity> implements IService<AppAuctionEntity> {

    @Autowired
    private AppAuctionImgService appAuctionImgService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AppAuctionBidMapper appAuctionBidMapper;

    @Autowired
    private AppAuctionCollectService appAuctionCollectService;

    @Resource
    private AppAuctionMapper appAuctionMapper;

    @Autowired
    private AppAuctionCounselorService appAuctionCounselorService;

    @Autowired
    private AppAuctionBailLogService bailLogService;

    @Autowired
    private AppAuctionBailRefundLogMapper appAuctionBailRefundLogMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AppAuctionHotService appAuctionHotService;

    @Autowired
    private AppAuctionOrderService appAuctionOrderService;

    @Autowired
    private AppAuctionBailLogMapper appAuctionBailLogMapper;
    @Autowired
    private AppAuctionOnePriceCarLogMapper appAuctionOnePriceCarLogMapper;

    @Autowired
    private AppAuctionBidService appAuctionBidService;

    @Autowired
    private AppAuctionVipLvService appAuctionVipLvService;

    @Autowired
    private AppAuctionVipControlService appAuctionVipControlService;

    @Autowired
    private AppAuctionWarnCarService appAuctionWarnCarService;

    @Autowired
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionHomePageService homePageService;

    @Autowired
    private CityService cityService;

    @Value("${bail}")
    private String bail;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject addAuctionCar(AppAuctionEntity appAuction, String id) throws ParseException {
        JSONObject result = new JSONObject();
        EntityWrapper<AppAuctionEntity> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("plate_no", appAuction.getPlateNo());
        AppAuctionEntity auction = selectOne(wrapper1);
        if (auction != null) {
            result.put("code", 4004);
            result.put("msg", "车辆已存在,请勿重复录入!");
            return result;
        }
        if (StringUtils.isEmpty(id)) {
            result.put("code", 4005);
            result.put("msg", "未知错误,请重新保存!");
            return result;
        }
        appAuction.setUserId(id);
        appAuction.setUpState(AppAuctionConstant.ZERO);
        appAuction.setCreateTime(new Date());

        boolean insert = appAuctionService.insert(appAuction);

        try {
            AssertUtil.isTrue(insert, "录入失败!");
        } catch (Exception e) {
            result.put("code", 4011);
            result.put("msg", "录入失败,请联系管理员!");
            return result;
        }

        Wrapper<AppAuctionUpEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("car_id", appAuction.getId());
        AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper);
        if (appAuctionUpEntity == null) {
            AppAuctionUpEntity appAuctionUpEntity1 = new AppAuctionUpEntity();
            appAuctionUpEntity1.setCreateTime(new Date());
            appAuctionUpEntity1.setCarId(appAuction.getId());
            appAuctionUpEntity1.setBrand(appAuction.getPlateNo());
            appAuctionUpService.insert(appAuctionUpEntity1);
        } else {
            result.put("code", 4002);
            result.put("msg", "该车已存在");
            return result;
        }

        List<AppAuctionImgEntity> imgList = appAuction.getImgList();
        if (imgList != null) {
            List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectByCarId(appAuction.getId());
            if (appAuctionImgEntities != null) {
                appAuctionImgEntities.stream().forEach(img -> appAuctionImgService.deleteById(img.getId()));
            }
            int index = 1;
            for (AppAuctionImgEntity s : imgList) {
                s.setCarId(Long.valueOf(appAuction.getId()));
                s.setIndex(index++);
                s.setCreateTime(new Date());
                String imgUrl = judgmentJpg(s.getUrl());
                s.setUrl(imgUrl);
                appAuctionImgService.insert(s);
            }
        }

        result.put("code", 200);
        result.put("msg", "保存成功");
        result.put("data", appAuction.getId());
        return result;
    }

    private String judgmentJpg(String url) {
        if (url.contains("image-process")) {
            return url;
        } else {
            return url + "?x-image-process=style/style-c2b2";
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject updateAuctionCar(AppAuctionEntity auctionEntity) throws ParseException {
        JSONObject result = new JSONObject();
        Long id1 = auctionEntity.getId();
        AppAuctionEntity auction1 = appAuctionService.selectById(id1);
        if (auction1.getCarState() >= AppAuctionConstant.THREE) {
            result.put("code", 501);
            result.put("msg", "当前状态不能修改");
            return result;
        }

        //修改不能修改状态
        auctionEntity.setUpdateTime(new Date());
        appAuctionMapper.updateById(auctionEntity);

        Wrapper<AppAuctionUpEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("car_id", auctionEntity.getId());
        AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper);

//        appAuctionUpEntity.setCarState(auctionEntity.getCarState());
        appAuctionUpEntity.setUpdateTime(new Date());
        appAuctionUpEntity.setBrand(auctionEntity.getPlateNo());
        appAuctionUpService.updateById(appAuctionUpEntity);
        //查询到已经上传的图片
        EntityWrapper<AppAuctionImgEntity> tWrapper = new EntityWrapper<>();
        tWrapper.eq("car_id", auctionEntity.getId());
        List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectList(tWrapper);
        List<AppAuctionImgEntity> imgList = auctionEntity.getImgList();
        if (appAuctionImgEntities.size() > 0) {
            //如果原来有就删除所有图片
            appAuctionImgEntities.stream().forEach(img -> appAuctionImgService.deleteById(img.getId()));
        }
        int index = 1;
        if (Objects.nonNull(imgList) && imgList.size() > 0) {
            for (AppAuctionImgEntity s : imgList) {
                s.setCarId(Long.valueOf(auctionEntity.getId()));
                s.setIndex(index++);
                s.setUpdateTime(new Date());
                String imgUrl = judgmentJpg(s.getUrl());
                s.setUrl(imgUrl);
                appAuctionImgService.insert(s);
            }
        }
        result.put("code", 200);
        result.put("msg", "修改成功");
        return result;
    }

    @Resource
    private AppAuctionHotMapper appAuctionHotMapper;

    /**
     * @param city 城市
     * @param id   用户id
     * @return
     */
//    @Cacheable(cacheNames = AppAuctionRedisConstans.KEY_RECOMMEND_HOT, key = "'All'")
    public JSONObject auctionHot(String city, Long id) {
        JSONObject result = new JSONObject();
        List<AppAuctionHotEntity> hot = appAuctionHotService.findHot(city);
        List<Long> collect1 = hot.stream().map(h -> h.getCarId()).collect(Collectors.toList());
        if (collect1.size() > 0) {
            List<AppAuctionEntity> appAuctionEntities = appAuctionMapper.selectBatchIds(collect1);
//            log.info("### 热门推荐数据###  city={};hot={}; collect1={}; appAuctionEntities={} ", city, hot, collect1, appAuctionEntities);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", auction2Dto(new JSONArray(), appAuctionEntities, id));
            return result;
        }
        result.put("code", 200);
        result.put("msg", "无数据");
        result.put("data", new JSONArray());
        return result;
    }

    /**
     * 我的卖车列表统计数字，新订单数量，拍卖中数量，已拍卖数量，已过户数量
     */
    public JSONObject countMyAuction(Integer userId) {
        JSONObject result = new JSONObject();

        EntityWrapper<AppAuctionEntity> newOrderWrapper = new EntityWrapper<>();
        newOrderWrapper.eq("user_id", String.valueOf(userId));
        newOrderWrapper.in("car_state", "0,1,2,3");
        int newOrder = selectCount(newOrderWrapper);

        EntityWrapper<AppAuctionEntity> auctionWrapper = new EntityWrapper<>();
        auctionWrapper.eq("user_id", String.valueOf(userId));
        auctionWrapper.in("car_state", "7");
        int auction = selectCount(auctionWrapper);

        EntityWrapper<AppAuctionEntity> endAuctionWrapper = new EntityWrapper<>();
        endAuctionWrapper.eq("user_id", String.valueOf(userId));
        endAuctionWrapper.in("car_state", "8,9,10,11");
        int endAuction = selectCount(endAuctionWrapper);

        EntityWrapper<AppAuctionEntity> transferWrapper = new EntityWrapper<>();
        transferWrapper.eq("user_id", String.valueOf(userId));
        transferWrapper.in("car_state", "12");
        int transfer = selectCount(transferWrapper);

        result.put("newOrder", newOrder);
        result.put("auction", auction);
        result.put("endAuction", endAuction);
        result.put("transfer", transfer);

        JSONObject object = new JSONObject();

        object.put("code", 200);
        object.put("msg", "查询成功");
        object.put("data", result);

        return object;
    }

    /**
     * 我的拍卖订单列表
     *
     * @param userId
     * @param page
     * @return
     */
    public JSONObject getMyAuctionList(Integer userId, Page page, Integer state) {
        String sqlState = "";

        switch (state) {
            case 1:
                sqlState = "0,1,2,3";
                break;
            case 2:
                sqlState = "7";
                break;
            case 3:
                sqlState = "8,9,10,11";
                break;
            case 4:
                sqlState = "12";
                break;
        }

        JSONObject result = new JSONObject();

        EntityWrapper<AppAuctionEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        if (StringUtils.isNotBlank(sqlState)) {
            wrapper.in("car_state", sqlState).orderBy("create_time", false);
        }
        ArrayList<String> sortList = new ArrayList<>();
        sortList.add("create_time");
        wrapper.orderDesc(sortList);
        List<AppAuctionEntity> records = selectPage(page, wrapper).getRecords();
        List<JSONObject> collect = records.stream().map(a -> a2Dto(a)).collect(Collectors.toList());

        result.put("code", 200);
        result.put("data", collect);
        return result;
    }

    public JSONObject getAuctionList(AuctionCarListDto dto, Long userId) {
        JSONObject result = new JSONObject();
        if (dto.getPriceMax() != null) {
            dto.setPriceMax(dto.getPriceMax().multiply(new BigDecimal("10000")));
        }
        if (dto.getPriceMin() != null) {
            dto.setPriceMin(dto.getPriceMin().multiply(new BigDecimal("10000")));
        }
        List<AppAuctionEntity> appAuctionEntities = appAuctionMapper.selectByCondition(dto);

        dto.setAuctionState(1); //查询拍卖中数量
        Integer auctionIngCount = appAuctionMapper.countByCondition(dto);

        dto.setAuctionState(2);// 查询待开始数量
        Integer auctionWaitCount = appAuctionMapper.countByCondition(dto);


        JSONArray array = auction2Dto(new JSONArray(), appAuctionEntities, userId);
        result.put("auctionIngCount", auctionIngCount);
        result.put("auctionWaitCount", auctionWaitCount);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", array);
        return result;
    }

    public JSONObject getCity() {
        JSONObject result = new JSONObject();
        List<CityEntity> cityEntities1 = cityService.selectList(null);
        JSONObject jsonObject = null;
        JSONArray objects = null;
        JSONArray array = new JSONArray();
        String c = null;
        for (CityEntity cityEntity : cityEntities1) {
            String cityName = cityEntity.getCityName();
            if (cityEntity.getCitycode() == null) {
                if (jsonObject != null) {
                    jsonObject.put("capital", c);
                    jsonObject.put("cityName", objects);
                    array.add(jsonObject);
                }
                jsonObject = new JSONObject();
                c = cityName;
                objects = new JSONArray();

            }
            if (cityName.endsWith("市")) {
                if (cityName.equals("上海市") || cityName.equals("重庆市") || cityName.equals("北京市") || cityName.equals("天津市"))
                    continue;
                objects.add(cityEntity.getCityName());
            }
        }
        JSONObject mun = new JSONObject();
        JSONArray array1 = new JSONArray();
        array1.add("上海市");
        array1.add("重庆市");
        array1.add("北京市");
        array1.add("天津市");
        mun.put("capital", "直辖市");
        mun.put("cityName", array1);
        array.add(mun);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", array);
        return result;
    }

    /**
     * 5资金(保证金)
     */
    public JSONObject myAuction(JSONObject result, Integer userId) {
        //返头像和电话
        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", userId).eq("state", AppAuctionConstant.ONE));
        AuctionMyDto vipCon = new AuctionMyDto();
        UserEntity userEntity = userService.selectById(userId);

        vipCon.setIsInner(userEntity.getIsInner());

        //竞买车
        Integer validCount = appAuctionBidService.getValidCount(userId);
        vipCon.setBidCar(validCount);

        //提醒车
        int i2 = appAuctionWarnCarService.selectCount(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", userId));
        vipCon.setWarnCar(Integer.valueOf(i2));

        //关注车
        EntityWrapper<AppAuctionCollectEntity> wra = new EntityWrapper<>();
        wra.eq("user_id", userId);
        int i3 = appAuctionCollectService.selectCount(wra);
        vipCon.setCollectCar(Integer.valueOf(i3));

        //待过户
        int i4 = appAuctionOrderService.selectCount(new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", userId).eq("state", "0"));
        vipCon.setWaitTransfer(Integer.valueOf(i4));

        //已过户
        int i5 = appAuctionOrderService.selectCount(new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", userId).eq("state", "1"));
        vipCon.setTransfered(Integer.valueOf(i5));

        //查询用户已支付并且未退款的单车保证金 金额
        //单位分
        Integer singleAmt = appAuctionBailLogMapper.sumByUserId(userId);
        if (singleAmt == null) {
            singleAmt = 0;
        }

        // 一口价支付 订金
        Integer onePriceAmt = appAuctionOnePriceCarLogMapper.sumByUserId(userId);
        if (onePriceAmt == null) {
            onePriceAmt = 0;
        }
        singleAmt += onePriceAmt;

        //设置单车保证金
        vipCon.setPenalty(new BigDecimal(singleAmt).divide(new BigDecimal(100)));

        if (appAuctionVipControl != null) {
            String vipLv = appAuctionVipControl.getVipLv();
            AppAuctionVipLvEntity lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", vipLv));
            if (lv != null) {
                vipCon.setSignLv(lv.getLvName());
            }
            vipCon.setAuthentication(userEntity.getAuthentication());
            vipCon.setAmount(appAuctionVipControl.getAmount().divide(new BigDecimal("100.00")));
            vipCon.setHead(userEntity.getAvatar());
            if (userEntity.getIsInner() == 1) {
                vipCon.setUsername(userEntity.getName()+"(内部员工)");
            }else{
                vipCon.setUsername(userEntity.getName());
            }


            List<AppAuctionOrderEntity> orders = appAuctionOrderService.selectList(new EntityWrapper<AppAuctionOrderEntity>().eq("user_id", userId));
            BigDecimal sum = new BigDecimal("0.00");
            if (orders.size() > 0) {
                for (AppAuctionOrderEntity order : orders) {
                    if (Objects.nonNull(order.getServiceFee())) {
                        sum.add(order.getServiceFee());
                    }
                }
            }
            vipCon.setServiceFee(sum);
            BigDecimal subtract = vipCon.getAmount();
            if (subtract.compareTo(new BigDecimal("0.00")) < 0) {
                vipCon.setCapital(new BigDecimal("0.00"));
            } else {
                vipCon.setCapital(vipCon.getAmount());
            }

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", vipCon);
            return result;
        } else {
            vipCon.setHead(userEntity.getAvatar());
            vipCon.setUsername(userEntity.getName());
            vipCon.setAuthentication(userEntity.getAuthentication());
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", vipCon);
            return result;
        }
    }

    //
    public JSONObject detail2(Long carId, Long userId) {
        JSONObject result = new JSONObject();
        AppAuctionEntity appAuctionEntity = null;

        if (carId != null && carId > 0) {

            appAuctionEntity = selectById(carId);
            try {
                AssertUtil.isNotNull(appAuctionEntity, "无车辆信息");
            } catch (Exception e) {
                result.put("code", 411);
                result.put("msg", "无车辆信息");
                return result;
            }

            //出价人数
            int bidNum = appAuctionBidService.selectCount(new EntityWrapper<AppAuctionBidEntity>().eq("car_id", carId).setSqlSelect("count(distinct user_id)"));
            appAuctionEntity.setBidNum(bidNum);

            //提醒车人数
            int carEntities = appAuctionWarnCarService.selectCount(new EntityWrapper<AppAuctionWarnCarEntity>().eq("car_id", carId));
            appAuctionEntity.setWarnNum(carEntities);

            //浏览次数
            AppAuctionHotEntity appAuctionHotEntity = appAuctionHotService.selectOne(new EntityWrapper<AppAuctionHotEntity>().eq("car_id", carId));
            if (Objects.nonNull(appAuctionHotEntity)) {
                appAuctionEntity.setBrowseNum(appAuctionHotEntity.getHits());
            } else {
                appAuctionEntity.setBrowseNum(0L);
            }

            appAuctionEntity.setPhone(null);

            //加价幅度
            appAuctionEntity.setAddPrice(new BigDecimal("1000.00"));

            EntityWrapper<AppAuctionUpEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("car_id", appAuctionEntity.getId());
            AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper);

            if (userId != null && appAuctionEntity.getCarState() == 7) { //记录登录用户，出价之后需要发送长连接告知用户最新价格
                //计算redis过期时间
                long endTimes = appAuctionUpEntity.getEndTime().getTime() - new Date().getTime();
                if (endTimes < 0) {
                    log.info("已到下架日期的车辆，不用再记录访问人Id!");
                } else {
                    redisTemplate.opsForSet().add(AuctionConstant.CAR_DETAIL_INFO_SET + carId, String.valueOf(userId));
                    redisTemplate.expire(AuctionConstant.CAR_DETAIL_INFO_SET, endTimes, TimeUnit.MILLISECONDS);
                }
            }

//            appAuctionEntity.setSelfState(setAsSelfState(appAuctionEntity));

            if (appAuctionEntity.getFrameNo() != null && StringUtils.isNotBlank(appAuctionEntity.getFrameNo()) && appAuctionEntity.getFrameNo().length() > 4) {
                appAuctionEntity.setFrameNo(appAuctionEntity.getFrameNo().substring(0, 5) + "***");
            }

            if (appAuctionEntity.getEngine() != null && StringUtils.isNotBlank(appAuctionEntity.getEngine()) && appAuctionEntity.getEngine().length() > 3) {
                appAuctionEntity.setEngine(appAuctionEntity.getEngine().substring(0, 3) + "***");
            }

            if (appAuctionEntity.getRegisteredResidence() != null && appAuctionEntity.getRegisteredResidence().length() > 2) {
                appAuctionEntity.setRegisteredResidence(appAuctionEntity.getRegisteredResidence().substring(0, 3) + "***");
            }

            // 1代表有  0代表没有这个资料
            if (StringUtils.isNotEmpty(appAuctionEntity.getPurchaseTax())) {
                appAuctionEntity.setPurchaseTax(AppAuctionConstant.ONESTR);
            } else {
                appAuctionEntity.setPurchaseTax(AppAuctionConstant.ZEROSTR);
            }

//            if (StringUtils.isNotEmpty(appAuctionEntity.getRegistrationCertificate())) {
                appAuctionEntity.setRegistrationCertificate(AppAuctionConstant.ONESTR);
//            } else {
//                appAuctionEntity.setRegistrationCertificate(AppAuctionConstant.ZEROSTR);
//            }

            if (StringUtils.isNotEmpty(appAuctionEntity.getMortgage())) {
                appAuctionEntity.setMortgage(AppAuctionConstant.ONESTR);
            } else {
                appAuctionEntity.setMortgage(AppAuctionConstant.ZEROSTR);
            }

            if (StringUtils.isNotEmpty(appAuctionEntity.getDutyBook())) {
                appAuctionEntity.setDutyBook(AppAuctionConstant.ONESTR);
            } else {
                appAuctionEntity.setDutyBook(AppAuctionConstant.ZEROSTR);
            }

            if (StringUtils.isNotEmpty(appAuctionEntity.getDrivingLicense())) {
                appAuctionEntity.setDrivingLicense(AppAuctionConstant.ONESTR);
            } else {
                appAuctionEntity.setDrivingLicense(AppAuctionConstant.ZEROSTR);
            }

            if (StringUtils.isNotEmpty(appAuctionEntity.getLicence())) {
                appAuctionEntity.setLicence(AppAuctionConstant.ONESTR);
            } else {
                appAuctionEntity.setLicence(AppAuctionConstant.ZEROSTR);
            }
            AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", userId).eq("state", AppAuctionConstant.ONE));

            if (Objects.nonNull(appAuctionVipControl)) {
                appAuctionEntity.setCarCount(appAuctionVipControl.getCarCount());
            }

            if (Objects.isNull(appAuctionEntity.getIsWarnCar())) {
                AppAuctionWarnCarEntity appAuctionWarnCarEntity = appAuctionWarnCarService.selectOne(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", userId).eq("car_id", carId));
                if (Objects.isNull(appAuctionWarnCarEntity)) {
                    appAuctionEntity.setIsWarnCar(AppAuctionConstant.ZEROSTR);
                } else {
                    appAuctionEntity.setIsWarnCar(AppAuctionConstant.ONESTR);
                }
            }
            if (userId != null) {
                EntityWrapper<AppAuctionCollectEntity> wrapper2 = new EntityWrapper<>();
                wrapper2.eq("user_id", userId).eq("car_id", carId);
                AppAuctionCollectEntity appAuctionCollectEntity = appAuctionCollectService.selectOne(wrapper2);
                if (appAuctionCollectEntity != null) {
                    appAuctionEntity.setCollect(AppAuctionConstant.ONESTR);
                }
            }


            //判断当前登录用户是否已交保证金
            if (Objects.nonNull(userId)) {
                AppAuctionBailLogEntity appAuctionBailLogEntity = bailLogService.selectOne(new EntityWrapper<AppAuctionBailLogEntity>().eq("user_id", userId).eq("car_id", carId).eq("status", AppAuctionBailConstant.AUCTION_BAIL_SUCCESS));
                if (Objects.nonNull(appAuctionBailLogEntity)) {
                    //虽然支付了  但是需要检查用户是否已经退款单车保证金  如果车辆进入流拍后的拍卖  支付的单车保证需要验证是否已退款
                    String outTradeNo = appAuctionBailLogEntity.getOutTradeNo();
                    AppAuctionBailRefundLogEntity appAuctionBailRefundLogEntity = appAuctionBailRefundLogMapper.selectByOutTradeNo(outTradeNo);
                    if (appAuctionBailRefundLogEntity != null && appAuctionBailRefundLogEntity.getState() != 0) {
                        appAuctionEntity.setIsPayBail(AppAuctionBailConstant.IS_NOT_PAY_BAIL);
                    } else {
                        appAuctionEntity.setIsPayBail(AppAuctionBailConstant.IS_PAY_BAIL);
                    }
                } else {
                    appAuctionEntity.setIsPayBail(AppAuctionBailConstant.IS_NOT_PAY_BAIL);
                }
            }

            //如果是一口价车辆  计算订金
            if ("1".equals(appAuctionEntity.getFixedPrice())) {
                appAuctionEntity.setBail(PriceUtils.onePriceDJ(appAuctionEntity.getPrice()));
            } else {
                //拍卖车辆
                if (Objects.nonNull(appAuctionEntity.getPrice())) {
                    appAuctionEntity.setBail(appAuctionEntity.getPrice().divide(new BigDecimal(100)).multiply(new BigDecimal(bail)));
                }
            }

            if (appAuctionUpEntity != null) {
                if (appAuctionUpEntity.getBeginTime() != null) {
                    appAuctionEntity.setBeginTime(appAuctionUpEntity.getBeginTime().getTime());
                    appAuctionEntity.setEndTime(appAuctionUpEntity.getEndTime().getTime());
                }
                appAuctionEntity.setServiceFee(appAuctionUpEntity.getServiceFee());
                appAuctionEntity.setOtherFee(appAuctionUpEntity.getOtherFee());
            }

            //买卖车辆为同一用户返回1
            AppAuctionOrderEntity auctionOrder = appAuctionOrderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId));
            if (Objects.nonNull(userId) && Objects.nonNull(auctionOrder)) {
                if (userId == auctionOrder.getUserId()) {
                    appAuctionEntity.setSelfState(AppAuctionConstant.ONESTR);
                } else {
                    appAuctionEntity.setSelfState(AppAuctionConstant.ZEROSTR);
                }
            } else {
                appAuctionEntity.setSelfState(AppAuctionConstant.ZEROSTR);
            }

//            EntityWrapper<AppAuctionBidEntity> wrapper2 = new EntityWrapper<>();
//            wrapper2.eq("car_id", carId);
//            List<AppAuctionBidEntity> appAuctionBidEntities = appAuctionBidService.selectList(wrapper2);
//            if (appAuctionBidEntities.size() > 0) {
//                List<AppAuctionBidEntity> collect = appAuctionBidEntities.stream().sorted(Comparator.comparing(a -> a.getBid(), Comparator.reverseOrder())).collect(Collectors.toList());
//                BigDecimal bid = collect.get(0).getBid();
//                appAuctionEntity.setTopPrice(bid);
//            }

            String bidCarKey = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, carId);
            //获取最高价
            String mustPrice = (String) redisTemplate.opsForValue().get(bidCarKey);
            if (mustPrice == null) {
                appAuctionEntity.setTopPrice(appAuctionEntity.getPrice());
            } else {
                appAuctionEntity.setTopPrice(new BigDecimal(mustPrice));
            }


            AppAuctionCounselorEntity appAuctionCounselorEntity = appAuctionCounselorService.queryCounselor(new JSONObject(), carId);
            if (Objects.nonNull(appAuctionCounselorEntity)) {
                if (Objects.isNull(appAuctionCounselorEntity.getHead())) {
                    appAuctionCounselorEntity.setHead(AppAuctionConstant.counselorHead);
                }
                appAuctionCounselorEntity.setPhone("18181999916");
                appAuctionCounselorEntity.setName("车己客服");
                appAuctionEntity.setCounselor(appAuctionCounselorEntity);
            } else {
                AppAuctionCounselorEntity appAuctionCounselorEntity1 = appAuctionCounselorService.selectOne(new EntityWrapper<AppAuctionCounselorEntity>().eq("name", "cheji001"));
                if (appAuctionCounselorEntity1 == null) {
                    appAuctionCounselorEntity1 = new AppAuctionCounselorEntity();
                    appAuctionCounselorEntity1.setHead(AppAuctionConstant.counselorHead);
                } else {
                    if (Objects.isNull(appAuctionCounselorEntity1.getHead())) {
                        appAuctionCounselorEntity1.setHead(AppAuctionConstant.counselorHead);
                    }
                }
                appAuctionCounselorEntity1.setPhone("18181999916");
                appAuctionCounselorEntity1.setName("车己客服");
                appAuctionEntity.setCounselor(appAuctionCounselorEntity1);
            }

        }

        if (appAuctionEntity != null) {
            AppAuctionOrderEntity auctionOrder = appAuctionOrderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId));
            if (Objects.isNull(auctionOrder)) {
                //查图片
                Wrapper<AppAuctionImgEntity> wrapper2 = new EntityWrapper<>();
                wrapper2.eq("car_id", carId).eq("state", AppAuctionConstant.ONE);
                List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectList(wrapper2);
                appAuctionEntity.setImgList(appAuctionImgEntities);
            } else {
                List<AppAuctionImgEntity> imgList = appAuctionImgService.selectList(new EntityWrapper<AppAuctionImgEntity>().eq("car_id", carId));
                appAuctionEntity.setImgList(imgList);
            }

            Wrapper<AppAuctionHotEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("car_id", carId);
            AppAuctionHotEntity appAuctionHotEntity = appAuctionHotService.selectOne(wrapper);
            if (appAuctionHotEntity != null) {
                appAuctionHotEntity.setHits(appAuctionHotEntity.getHits() + 1);
                appAuctionHotService.updateById(appAuctionHotEntity);
            } else {
                AppAuctionHotEntity appAuctionHotEntity1 = new AppAuctionHotEntity();
                appAuctionHotEntity1.setHits(1L);
                appAuctionHotEntity1.setCarId(carId);
                appAuctionHotEntity1.setIsEnabled(AppAuctionConstant.ZERO);
                appAuctionHotEntity1.setCity(appAuctionEntity.getParkingPlace());
                appAuctionHotService.insert(appAuctionHotEntity1);
            }
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", appAuctionEntity);
        } else {
            result.put("code", 4002);
            result.put("msg", "查询失败");
        }

        return result;
    }

    public JSONObject detail(Long carId) {
        JSONObject result = new JSONObject();
        AppAuctionEntity appAuctionEntity = null;
        if (carId != null && carId > 0) {
            appAuctionEntity = selectById(carId);
            AssertUtil.isNotNull(appAuctionEntity, "无车辆信息");
            EntityWrapper<AppAuctionUpEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("car_id", appAuctionEntity.getId());
            AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(wrapper);
            if (appAuctionUpEntity != null && appAuctionUpEntity.getExplain() != null) {
                appAuctionEntity.setExplain(appAuctionUpEntity.getExplain());
            }
            if (appAuctionUpEntity != null && appAuctionUpEntity.getBeginTime() != null) {
                appAuctionEntity.setBeginTime(appAuctionUpEntity.getBeginTime().getTime());
                appAuctionEntity.setEndTime(appAuctionUpEntity.getEndTime().getTime());
            }

//            appAuctionEntity.setSelfState(setAsSelfState(appAuctionEntity));
            EntityWrapper<AppAuctionBidEntity> wrapper2 = new EntityWrapper<>();
            wrapper2.eq("car_id", appAuctionEntity.getId());
            List<AppAuctionBidEntity> appAuctionBidEntities = appAuctionBidService.selectList(wrapper2);
            if (appAuctionBidEntities.size() > 0) {
                List<AppAuctionBidEntity> collect = appAuctionBidEntities.stream().sorted(Comparator.comparing(a -> a.getBid(), Comparator.reverseOrder())).collect(Collectors.toList());
                BigDecimal bid = collect.get(0).getBid();
                appAuctionEntity.setTopPrice(bid);
            }
        }

        if (appAuctionEntity != null) {
            //查图片
            Wrapper<AppAuctionImgEntity> wrapper2 = new EntityWrapper<>();
            wrapper2.eq("car_id", carId);
            List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectList(wrapper2);
            appAuctionEntity.setImgList(appAuctionImgEntities);

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", appAuctionEntity);
        } else {
            result.put("code", 4002);
            result.put("msg", "查询失败");
        }

        return result;
    }

    public JSONObject collectHandle(JSONObject result, Integer id, Long carId) {
        if (Objects.isNull(carId)) {
            result.put("code", 433);
            result.put("msg", "请选择车辆!");
            return result;
        }
        Wrapper<AppAuctionCollectEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", Long.valueOf(id)).eq("car_id", carId);
        AppAuctionCollectEntity appAuctionCollectEntity = appAuctionCollectService.selectOne(wrapper);
        if (appAuctionCollectEntity != null) {
            appAuctionCollectService.deleteById(appAuctionCollectEntity.getId());
        } else {
            AppAuctionCollectEntity collectEntity = new AppAuctionCollectEntity();
            collectEntity.setUserId(Long.valueOf(id));
            collectEntity.setCarId(carId);
            collectEntity.setCreateTime(new Date());
            collectEntity.setIsEnabled(AppAuctionConstant.CAR_IS_ENABLED);
            appAuctionCollectService.insert(collectEntity);
        }
        result.put("code", 200);
        result.put("msg", "操作成功!");
        return result;
    }

    public JSONArray auction2Dto(JSONArray objects, List<AppAuctionEntity> appAuctionEntities, Long userId) {
        if (appAuctionEntities.size() < 1) {
            return objects;
        }
        appAuctionEntities.stream().forEach(appAuction -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", appAuction.getId().toString());
            if (appAuction.getBrand().length() > 13) {
                jsonObject.put("brand", appAuction.getBrand().substring(0, 13));
            } else {
                jsonObject.put("brand", appAuction.getBrand());
            }

            jsonObject.put("register_date", UseDateUtils.backStringDate(appAuction.getRegisterDate()));
            jsonObject.put("insuredAmount", appAuction.getInsuredAmount());
            jsonObject.put("accidentType", appAuction.getAccidentType());
            jsonObject.put("parkingPlace", appAuction.getParkingPlace());
            jsonObject.put("fixedPrice", appAuction.getFixedPrice());

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

            jsonObject.put("insurance", appAuction.getInsurance());
            jsonObject.put("carState", appAuction.getCarState());
            if (Objects.nonNull(userId)) {
                jsonObject.put("collect", appAuctionCollectService.isCollect(userId, appAuction.getId()));
            } else {
                jsonObject.put("collect", null);
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
            List<AppAuctionImgEntity> carImgList = appAuctionImgService.selectByCarId(appAuction.getId());
            if (carImgList.size() > 0) {
                for (AppAuctionImgEntity appAuctionImgEntity : carImgList) {
                    if (AppAuctionConstant.ISDEFAULTADDRESS.equals(appAuctionImgEntity.getState())) {
                        jsonObject.put("img", appAuctionImgEntity);
                        break;
                    }
                }
            }
            objects.add(jsonObject);
        });

        return objects;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject setTransfer(JSONObject obj) {
        JSONObject result = new JSONObject();
        //id为车辆id
        String carId = (String) obj.get("carId");
        Integer userId = (Integer) obj.get("userId");
        AppAuctionOrderEntity auctionOrder = appAuctionOrderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId).eq("user_id", userId).eq("state", AppAuctionConstant.ZEROSTR));
        if (Objects.nonNull(auctionOrder) && !userId.toString().equals(auctionOrder.getUserId().toString())) {
            result.put("code", 552);
            result.put("msg", "请提交已购车辆资料!");
            return result;
        }
        Long id = Long.valueOf(carId);
        List<AppAuctionImgEntity> list = (List<AppAuctionImgEntity>) obj.get("imgList");
        String s = JSON.toJSONString(list);
        List<AppAuctionImgEntity> imgList = JSON.parseArray(s, AppAuctionImgEntity.class);

        AppAuctionEntity auction = selectById(id);
//        EntityWrapper<AppAuctionUpEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("car_id", id);
        if (AppAuctionConstant.EIGHT == auction.getCarState() || AppAuctionConstant.ELEVEN == auction.getCarState()) {
            //查询图片,如果有就删了从新加
            EntityWrapper<AppAuctionImgEntity> tWrapper = new EntityWrapper<>();
            tWrapper.eq("car_id", id).and("(state = '3' or state = '4' or state = '5')");
            List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectList(tWrapper);
            if (appAuctionImgEntities.size() < 1 && imgList.size() > 0) {       //没有查询到以前的，是第一次上传
                //查询出来的，appAuctionImgEntities 图片集合    imgList上传的图片集合
                for (AppAuctionImgEntity appAuctionImgEntity : imgList) {
                    if (appAuctionImgEntity.getUrl() != null && appAuctionImgEntity.getState() != null) {
                        appAuctionImgEntity.setCarId(id);
                        appAuctionImgEntity.setCreateTime(new Date());
                        String imgUrl = judgmentJpg(appAuctionImgEntity.getUrl());
                        appAuctionImgEntity.setUrl(imgUrl);
                        appAuctionImgService.insert(appAuctionImgEntity);
                    }
                }
                auction.setCarState(AppAuctionConstant.TEN);
                updateById(auction);
                result.put("code", 200);
                result.put("msg", "提交成功");
            } else if (Objects.nonNull(appAuctionImgEntities) && appAuctionImgEntities.size() > 0 && Objects.nonNull(imgList) && imgList.size() > 0) {  //查询出来有图片，上传的也有图片
                List<Long> collect = appAuctionImgEntities.stream().map(a -> a.getId()).collect(Collectors.toList());
                appAuctionImgService.deleteBatchIds(collect);   //删掉曾经的图片
                for (AppAuctionImgEntity appAuctionImgEntity : imgList) {
                    appAuctionImgEntity.setCarId(id);
                    appAuctionImgEntity.setUpdateTime(new Date());
                    String imgUrl = judgmentJpg(appAuctionImgEntity.getUrl());
                    appAuctionImgEntity.setUrl(imgUrl);
                    appAuctionImgService.insert(appAuctionImgEntity);
                }
                auction.setCarState(AppAuctionConstant.TEN);
                updateById(auction);
                result.put("code", 200);
                result.put("msg", "提交成功");
            } else {
                result.put("code", 551);
                result.put("msg", "资料不完全!");
            }
        } else {
            result.put("code", 550);
            result.put("msg", "状态错误,请联系管理员");
        }
        return result;
    }

    public JSONObject search(String address, String brand, Integer current, Integer size, Long id) {
        JSONObject result = new JSONObject();
        List<AppAuctionEntity> search = appAuctionMapper.search(address, brand, current, size);
        if (search.size() > 0) {
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", auction2Dto(new JSONArray(), search, id));
            return result;
        } else {
            result.put("code", 200);
            result.put("msg", "暂无数据!");
            result.put("data", null);
            return result;
        }
    }

    public JSONObject getCarCount(Integer userId) {
        //获取上架的车辆总数
        AuctionCarListDto dto = new AuctionCarListDto();
        dto.setAuctionState(1); //查询拍卖中数量
        Integer auctionIngCount = appAuctionMapper.countByCondition(dto);
        dto.setAuctionState(2);// 查询待开始数量
        Integer auctionWaitCount = appAuctionMapper.countByCondition(dto);
        Integer count = auctionIngCount + auctionWaitCount;


        JSONObject result = new JSONObject();
        JSONObject images = new JSONObject();

        EntityWrapper<AppAuctionHomePageEntity> wrapper = new EntityWrapper<>();
        List<AppAuctionHomePageEntity> appAuctionHomePageEntities = homePageService.selectList(wrapper);

        int vip = 0;
        if (Objects.nonNull(userId)) {
            UserEntity userEntity = userService.selectById(userId);
            if (Objects.nonNull(userEntity.getVipLv())) {
                vip = Integer.valueOf(userEntity.getVipLv());
            }
        }

        Integer hotCount = appAuctionHotMapper.findHotCount(null);

        images.put("auctionIngCount", auctionIngCount);
        images.put("count", count);
        images.put("hotcCount", hotCount > 40 ? 40 : hotCount);
        images.put("isVip", vip);
        images.put("video", null);
        images.put("images", null);
        appAuctionHomePageEntities.stream().forEach(a -> {
            if (AppAuctionConstant.ONE == a.getType() && Objects.nonNull(a.getUrl())) {
                images.put("video", a.getUrl());
            } else if (AppAuctionConstant.TWO == a.getType() && Objects.nonNull(a.getUrl())) {
                images.put("images", a.getUrl());
            }
        });

        result.put("code", 200);
        result.put("msg", "查询成功!");
        result.put("data", images);
        result.put("phone", "18181999916");
        return result;
    }

    private JSONObject a2Dto(AppAuctionEntity a) {
        JSONObject object = new JSONObject();
        object.put("id", a.getId());
        object.put("plateNo", a.getPlateNo());
        object.put("accidentType", a.getAccidentType());
        if (Objects.nonNull(a.getBrand()) && a.getBrand().length() > 13) {
            object.put("brand", a.getBrand().substring(0, 13));
        } else {
            object.put("brand", a.getBrand());
        }
        object.put("insurance", a.getInsurance());
        object.put("fixedPrice", a.getFixedPrice());
        object.put("price", a.getPrice());
        object.put("parkingPlace", a.getParkingPlace());
        object.put("name", a.getName());
        object.put("phone", a.getPhone());
        object.put("img", null);
        object.put("carState", a.getCarState());

        AppAuctionUpEntity appAuctionUpEntity = appAuctionUpService.selectOne(new EntityWrapper<AppAuctionUpEntity>().eq("car_id", a.getId()));
        if (appAuctionUpEntity != null) {
            object.put("beginTime", appAuctionUpEntity.getBeginTime());
            object.put("endTime", appAuctionUpEntity.getEndTime());
        } else {
            object.put("beginTime", null);
            object.put("endTime", null);
        }


        List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectByCarId(a.getId());
        if (appAuctionImgEntities.size() > 0) {
            for (AppAuctionImgEntity appAuctionImgEntity : appAuctionImgEntities) {
                if (AppAuctionConstant.CAR_IMG_ONE.equals(appAuctionImgEntity.getState())) {
                    object.put("img", appAuctionImgEntity.getUrl());
                    break;
                }
            }
        }

        return object;
    }

    public JSONObject getTransfer(Long carId, Integer userId) {
        EntityWrapper<AppAuctionImgEntity> tWrapper = new EntityWrapper<>();
        tWrapper.eq("car_id", carId).and("(state = '3' or state = '4' or state = '5')");
        List<AppAuctionImgEntity> appAuctionImgEntities = appAuctionImgService.selectList(tWrapper);
        AppAuctionOrderEntity auctionOrder = appAuctionOrderService.selectOne(new EntityWrapper<AppAuctionOrderEntity>().eq("car_id", carId).eq("user_id", userId));
        JSONObject result = new JSONObject();
        AppAuctionUpEntity carUp = appAuctionUpService.selectOne(new EntityWrapper<AppAuctionUpEntity>().eq("car_id", carId));
        AppAuctionEntity appAuction = appAuctionService.selectOne(new EntityWrapper<AppAuctionEntity>().eq("id", carId));
        if (Objects.nonNull(auctionOrder) && appAuctionImgEntities.size() > 0) {
            if (appAuction.getCarState() == AppAuctionConstant.ELEVEN) {
                result.put("err", carUp.getExplain());
            }
            result.put("code", 200);
            result.put("msg", "success!");
            result.put("data", appAuctionImgEntities);
            return result;
        }
        result.put("code", 200);
        result.put("msg", "暂无数据!");
        result.put("data", new ArrayList<String>());
        return result;
    }

    public JSONObject auctionRandom(Long id) {
        JSONObject result = new JSONObject();
        List<AppAuctionHotEntity> hot = appAuctionHotService.findHot(null);
        //随机减少前几个
        Random r = new Random();
        Integer ran = r.nextInt(10);
        //随机去掉ran个后取前10个
        List<Long> collect1 = hot.stream().skip(Long.valueOf(ran)).limit(10).map(h -> h.getCarId()).collect(Collectors.toList());
        if (collect1.size() > 0) {
            List<AppAuctionEntity> appAuctionEntities = appAuctionMapper.selectBatchIds(collect1);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", auction2Dto(new JSONArray(), appAuctionEntities, id));
            return result;
        }
        result.put("code", 200);
        result.put("msg", "无数据");
        result.put("data", null);
        return result;
    }
}
