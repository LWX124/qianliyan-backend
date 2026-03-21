package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.alibaba.rocketmq.client.producer.SendStatus;
//import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.mapper.AppRescueIndentMapper;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.BUserMapper;
import com.cheji.web.util.MapNavUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 救援表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-02-21
 */
@Service
public class AppRescueIndentService extends ServiceImpl<AppRescueIndentMapper, AppRescueIndentEntity> implements IService<AppRescueIndentEntity> {

    private Logger logger = LoggerFactory.getLogger(AppRescueIndentService.class);


    @Resource
    private AppRescueIndentMapper appRescueIndentMapper;

    @Resource
    private BUserMapper bUserMapper;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private BUserService bUserService;

//    @Resource
//    private DefaultMQProducer mqProducer;

    private static final String key = "a11e3020a4b82ce9390044286910f02f";

    public JSONObject getPrice(Integer type, BigDecimal lng, BigDecimal lat, BigDecimal inlng, BigDecimal inlat, String currentPosition, String destination) {
        JSONObject in = getMerchants(lng, lat, currentPosition, RedisConstant.MERCHANTS_RESCUE_GEO);
        if (type == 1 || type == 3) {
            MapNavUtil mapNavUtil = new MapNavUtil(in.getBigDecimal("lng2")+","+in.getBigDecimal("lat2"), lng+","+lat, key,1,"JSON");
            Long results = mapNavUtil.getResults();
            BigDecimal distance2 = new BigDecimal(results);
            distance2 = distance2.divide(new BigDecimal("1000"),2,BigDecimal.ROUND_HALF_UP);
            //根据距离计算价格 搭电换胎，起步60包含五公里，6块一公里
          //  int flag = distance2.compareTo(new BigDecimal(5));
            in.put("distance", distance2);
//            if (flag > 0) {
//                // 大于
//                //大于五公里的部分
//                BigDecimal subtract = distance2.subtract(new BigDecimal(5));
//                BigDecimal multiply = subtract.multiply(new BigDecimal(6));
//                //算出来的价格四舍五入
//                BigDecimal price = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);// 0.24  +60
//                price = new BigDecimal(60);
//                in.put("price", price);
//            } else {
//                //小于或者等于
                BigDecimal price = new BigDecimal("66");
                in.put("price", price);

        } else if (type == 2) {
            //根据两个经纬度来计算出距离
            //添加数据
            MapNavUtil mapNavUtil = new MapNavUtil(lng+","+lat, inlng+","+inlat, key,1,"JSON");
            Long results = mapNavUtil.getResults();
            BigDecimal distance3 = new BigDecimal(results);
            distance3 = distance3.divide(new BigDecimal("1000"),2,BigDecimal.ROUND_HALF_UP);

            in.put("currentPosition", currentPosition);
            in.put("destination", destination);
            in.put("distance", distance3);
            //根据距离计算价格
            //拖车 起步180，包含五公里，8块一公里
            int falg = distance3.compareTo(new BigDecimal("15"));
            if (falg > 0) {
                BigDecimal subtract = distance3.subtract(new BigDecimal("15"));//超出15公里的部分
                BigDecimal multiply = subtract.multiply(new BigDecimal("8"));
                //算出来的价格四舍五入
                BigDecimal price = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);// 0.24
                price = price.add(new BigDecimal("180"));
                in.put("price", price);
            } else {
                BigDecimal price = new BigDecimal("180");
                in.put("price", price);
            }
        } else {
            in.put("msg", "请检查type数据");
            return in;
        }
        return in;
    }

    public Personal findIndentCount(String userId) {
        return appRescueIndentMapper.findIndentCount(userId);
    }

    public String findImg(Integer type) {
        return appRescueIndentMapper.findImg(type);
    }

    public JSONObject getMerchants(BigDecimal lng,BigDecimal lat,String currentPosition,String redisKey){
        JSONObject in = new JSONObject();
        //从geo中拿到最近得一个数据然后计算距离
        Circle circle = new Circle(lng.doubleValue(), lat.doubleValue(), 50.0 * 10000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = stringRedisTemplate.opsForGeo().radius(redisKey, circle, args);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        RescueMerchantsEntity tableMess = new RescueMerchantsEntity();
        BigDecimal lng2 = new BigDecimal(0);
        BigDecimal lat2 = new BigDecimal(0);;
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
            RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
            String name = conten.getName();
            JSONObject jsonObject = JSONObject.parseObject(name);
            String merchantsName = jsonObject.getString("merchantsName");
            lng2 = jsonObject.getBigDecimal("lng");
            String address = jsonObject.getString("address");
            lat2 = jsonObject.getBigDecimal("lat");
            String id1 = jsonObject.getString("id");
            tableMess.setId(id1);
            tableMess.setMerchantsName(merchantsName);
            tableMess.setAddress(address);
            tableMess.setLng(lng2);
            tableMess.setLat(lat2);
            break;
        }
        in.put("currentPosition", currentPosition);
        in.put("address", tableMess.getAddress());
        in.put("userBId",tableMess.getId());
        in.put("lng2",lng2);
        in.put("lat2",lat2);
        return in;
    }

    //给商户加钱
    @Transactional(rollbackFor = Exception.class)
    public void addMerchantsAmount(AppRescueIndentEntity appRescueIndentEntity) {
        BUserEntity bUserEntity = bUserMapper.updateBalance(appRescueIndentEntity.getUserBId().toString());
        //给商户加钱
        BigDecimal price = appRescueIndentEntity.getPrice();
        //商户加钱80%
        BigDecimal multiply = price.multiply(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP));
        bUserEntity.setBalance(bUserEntity.getBalance().add(multiply));

        AppUserAccountRecordEntity appUserAccount = new AppUserAccountRecordEntity();
        appUserAccount.setMomey(multiply);
        appUserAccount.setUserId(appRescueIndentEntity.getUserBId());
        appUserAccount.setType(15);
        appUserAccount.setCreateTime(new Date());
        appUserAccount.setAddFlag(1);
        appUserAccount.setSource(1);
        appUserAccount.setBusinessId(appRescueIndentEntity.getRescueNumber());
        appUserAccountRecordMapper.insert(appUserAccount);

        AppUserAccountRecordEntity chejiBalance = new AppUserAccountRecordEntity();
        chejiBalance.setMomey(price.subtract(multiply));
        chejiBalance.setUserId(appRescueIndentEntity.getUserBId());
        chejiBalance.setType(-1);
        chejiBalance.setCreateTime(new Date());
        chejiBalance.setAddFlag(1);
        chejiBalance.setSource(1);
        chejiBalance.setBusinessId(appRescueIndentEntity.getRescueNumber());
        appUserAccountRecordMapper.insert(chejiBalance);

        bUserMapper.updateById(bUserEntity);


    }

    public JSONObject findRescueTechnician(AppRescueIndentEntity appRescueIndentEntity) {
        JSONObject json = new JSONObject();
        Integer userBId = appRescueIndentEntity.getUserBId();
        //查询到救援技师
        EntityWrapper<AppUserBMessageEntity> appUserBMessageWrapper = new EntityWrapper<>();
        appUserBMessageWrapper.eq("user_b_id",userBId)
                .eq("wrok_type",1);

        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(appUserBMessageWrapper);
        if (appUserBMessageEntity==null){
            return null;
        }
        appUserBMessageEntity.setOrderNumber(appUserBMessageEntity.getOrderNumber()+1);
        appUserBMessageService.updateById(appUserBMessageEntity);
        json.put("id",appRescueIndentEntity.getId());
        json.put("state",appRescueIndentEntity.getState());
        //技师id
        json.put("technicianId",appUserBMessageEntity.getId());
        json.put("technicianPhone",appUserBMessageEntity.getPhone());
        //技师经纬度
        json.put("technicianLat",appUserBMessageEntity.getLat());
        json.put("technicianLng",appUserBMessageEntity.getLng());
        json.put("technicianAddress",appUserBMessageEntity.getWorkPlace());
        //商户经纬度
        BUserEntity bUserEntity = bUserService.selectById(userBId);
        json.put("merchantsLat",bUserEntity.getLat());
        json.put("merchantsLng",bUserEntity.getLng());
        json.put("merchantsAddress",bUserEntity.getAddress());
        json.put("merchantsId",bUserEntity.getId());

        json.put("headImg",appUserBMessageEntity.getHeadImg());

        json.put("name",appUserBMessageEntity.getName());

        json.put("serialNumber",appUserBMessageEntity.getSerialNumber());

        json.put("technologyYear",appUserBMessageEntity.getTechnologyYear());

        json.put("driverYear",appUserBMessageEntity.getDriverYear());

        json.put("score",appUserBMessageEntity.getScore());

        json.put("merchantsPhone",bUserEntity.getMerchantsPhone());

        return json;
    }


//    @Transactional(rollbackFor = Exception.class)
//    public void cancelOrder(String orderNumber, AppRescueIndentEntity appRescueIndentEntity) throws CusException {
//        logger.info("取消救援开始发送消息：" + orderNumber);
//        Message sendMsg = new Message("all", "rescueCancelOrder", orderNumber.getBytes());
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        if (sendResult == null) {
//            throw new CusException(405, "取消失败，请联系客服。");
//        }
//        logger.info("消息发送响应信息：" + sendResult.toString());
//        //修改订单状态
//        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
//            appRescueIndentEntity.setState(4);
//            updateById(appRescueIndentEntity);
//        } else {
//            logger.error("消息发送失败 响应信息：{};cleanIndetEntity={}", sendResult.toString(), appRescueIndentEntity);
//        }
//    }
}
