package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.modular.domain.AppRescueIndentEntity;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.cheji.web.modular.domain.BUserEntity;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.service.AppRescueIndentService;
import com.cheji.web.modular.service.AppUserBMessageService;
import com.cheji.web.modular.service.AppWxService;
import com.cheji.web.modular.service.BUserService;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import com.cheji.web.util.MapNavUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 救援表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-02-21
 */
@RestController
@RequestMapping("/appRescueIndent")
public class AppRescueIndentController extends BaseController {

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppWxService appWxService;

    @Resource
    private BUserService bUserService;

//    @Resource
//    private DefaultMQProducer mqProducer;


    @Value("${host}")
    private String host;

    @ApiOperation(value = "保存救援订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPosition", value = "救援位置", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "licensePlate", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "rescueName", value = "救援联系人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phoneNumber", value = "手机号码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "emergencyNumber", value = "紧急号码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "救援类型(1.搭电。2.拖车。3.换胎。4.送油)", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rescueScene", value = "救援场景 1事故，2故障，3地面，4地库，5困境，6其他，7有备胎，8送胎", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "救援经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "救援纬度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "inlng", value = "目的地经度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "inlat", value = "目的地纬度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "destination", value = "目的地", required = false, dataType = "String")
    })
    @RequestMapping(value = "/saveTakeElectricity", method = RequestMethod.POST)
    public JSONObject saveTakeElectricity(@RequestBody AppRescueIndentEntity appRescueIndent, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        appRescueIndent.setUserId(userid);
        if (StringUtils.isEmpty(appRescueIndent.getCurrentPosition())) {
            result.put("code", 420);
            result.put("msg", "救援位置为空");
            return result;
        }
        if (appRescueIndent.getLng() == null) {
            result.put("code", 420);
            result.put("msg", "经度为空");
            return result;
        }
        if (appRescueIndent.getLat() == null) {
            result.put("code", 420);
            result.put("msg", "纬度为空");
            return result;
        }
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "ER" + orderNo;
        appRescueIndent.setRescueNumber(orderNo);
        appRescueIndent.setPayState(0);
        appRescueIndent.setCreateTime(new Date());
        appRescueIndent.setUpdateTime(new Date());
        //计算价格
        JSONObject in = appRescueIndentService.getPrice(appRescueIndent.getType(), appRescueIndent.getLng(), appRescueIndent.getLat(),
                appRescueIndent.getInlng(), appRescueIndent.getInlat(), appRescueIndent.getCurrentPosition(), appRescueIndent.getDestination());
        if (StringUtils.isNotEmpty(in.getString("msg"))) {
            return in;
        }
        BigDecimal price = in.getBigDecimal("price");
        BigDecimal distance = in.getBigDecimal("distance");
        Integer userBid = in.getInteger("userBId");
        String address = in.getString("address");
        appRescueIndent.setUserBId(userBid);
        appRescueIndent.setMerchantsPosition(address);
        appRescueIndent.setPrice(price);
        appRescueIndent.setDistance(distance);
        appRescueIndentService.insert(appRescueIndent);

        //支付
        JSONObject json = new JSONObject();
//        json.put("data",appRescueIndent);
//        return json;
        json.put("id", appRescueIndent.getId().toString());
        json.put("type", 6);
        json.put("number", appRescueIndent.getRescueNumber());

        return appWxService.createCusOrder(json, userid, "车己汽车-开通PLUS会员", result, request, currentLoginUser, host);
    }


    @ApiOperation(value = "计算保存救援价格")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "救援类型(1.搭电。2.拖车。3.换胎。)", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "currentPosition", value = "救援地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "救援经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "救援纬度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "inlng", value = "目的地经度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "inlat", value = "目的地纬度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "destination", value = "目的地", required = false, dataType = "String")
    })
    @RequestMapping(value = "/calculateRescuePrice", method = RequestMethod.POST)
    public JSONObject calculateRescuePrice(@RequestBody JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        // 救援， 搭电，换胎
        //拖车两个经纬度，拖车两个地点，算出两个地点参数得距离然后再来算价格
        //根据经纬度拿到最近的一个商户，拿到距离之后算出价格
        //搭电，救援
        Integer type = jsonObject.getInteger("type");
        if (type != 1 && type != 2 && type != 3) {
            result.put("code", 405);
            result.put("msg", "失败，请检查type");
        }
        String currentPosition = jsonObject.getString("currentPosition");
        if (StringUtils.isEmpty(currentPosition)) {
            result.put("code", 405);
            result.put("msg", "失败，请检查currentPosition");
        }
        BigDecimal lng = jsonObject.getBigDecimal("lng");
        BigDecimal lat = jsonObject.getBigDecimal("lat");
        BigDecimal inlng = jsonObject.getBigDecimal("inlng");
        BigDecimal inlat = jsonObject.getBigDecimal("inlat");
        String destination = jsonObject.getString("destination");
        if (type == 2 && StringUtils.isEmpty(destination)) {
            result.put("code", 405);
            result.put("msg", "失败，请检查destination");
        }

        JSONObject in = appRescueIndentService.getPrice(type, lng, lat, inlng, inlat, currentPosition, destination);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    @ApiOperation(value = "救援详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "rescueNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/rescueDetails", method = RequestMethod.GET)
    public JSONObject rescueDetails(String rescueNumber) {
        JSONObject result = new JSONObject();
        //根据订单编号查询救援详情
        EntityWrapper<AppRescueIndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("rescue_number", rescueNumber);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(wrapper);
        if (appRescueIndentEntity == null) {
            result.put("code", 408);
            result.put("msg", "检查订单编号");
            return result;
        }
        JSONObject jsonObject = new JSONObject();
        //价格，，服务类型
        Integer type = appRescueIndentEntity.getType();
        String url = appRescueIndentService.findImg(type);
        jsonObject.put("url", url);
        jsonObject.put("price", appRescueIndentEntity.getPrice());
        if (appRescueIndentEntity.getType() == 1) {
            jsonObject.put("type", "搭电");
        } else if (appRescueIndentEntity.getType() == 2) {
            jsonObject.put("type", "拖车");
        } else if (appRescueIndentEntity.getType() == 3) {
            jsonObject.put("type", "换胎");
        }
        //订单编号，车牌号，救援位置，金额，状态，支付时间，距离
        jsonObject.put("rescueNumber", rescueNumber);//订单编号
        jsonObject.put("licensePlate", appRescueIndentEntity.getLicensePlate());//车牌
        jsonObject.put("currentPosition", appRescueIndentEntity.getCurrentPosition());//救援地点
        jsonObject.put("payState", appRescueIndentEntity.getPayState());//
        Date createTime = appRescueIndentEntity.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(createTime);
        jsonObject.put("createTime", dateString);
        jsonObject.put("distance", appRescueIndentEntity.getDistance());
        jsonObject.put("indentState", appRescueIndentEntity.getState());
        jsonObject.put("destination", appRescueIndentEntity.getDestination());
        jsonObject.put("rescueName", appRescueIndentEntity.getRescueName());
        jsonObject.put("phoneNumber", appRescueIndentEntity.getPhoneNumber());
        jsonObject.put("emergencyNumber", appRescueIndentEntity.getEmergencyNumber());
        jsonObject.put("rescueScene", appRescueIndentEntity.getRescueScene());
        jsonObject.put("remark", appRescueIndentEntity.getRemark());
        //商户名称，电话，地址，
        EntityWrapper<BUserEntity> bUserWrapper = new EntityWrapper<>();
        bUserWrapper.eq("id", appRescueIndentEntity.getUserBId());
        BUserEntity bUserEntity = bUserService.selectOne(bUserWrapper);
        if (bUserEntity == null) {
            result.put("code", 408);
            result.put("msg", "商户id有误");
            return result;
        }
        jsonObject.put("merchantsName", bUserEntity.getMerchantsName());
        jsonObject.put("merchantsPhone", bUserEntity.getPhoneNumber());
        jsonObject.put("merchantsAddress", bUserEntity.getAddress());
        //技师经纬度地址
        //查询救援技师
//        EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
//        bMessageWrapper.eq("user_b_id",bUserEntity.getId())
//                .eq("wrok_type",1);
//        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(bMessageWrapper);
//        jsonObject.put("technicianLng",appUserBMessageEntity.getLng());
//        jsonObject.put("technicianLat",appUserBMessageEntity.getLat());
//        jsonObject.put("technicianAddress",appUserBMessageEntity.getWorkPlace());
//        //商户经纬度
//        jsonObject.put("merchantsLng",bUserEntity.getLng());
//        jsonObject.put("merchantsLat",bUserEntity.getLat());

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", jsonObject);
        return result;
    }


//    @ApiOperation(value = "救援结算")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "rescueNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/rescueSettlement", method = RequestMethod.GET)
//    public JSONObject rescueSettlement(String rescueNumber) {
//        //根据订单编号查询到订单价格，从订单价格抽20%
//        JSONObject result = new JSONObject();
//        //救援方获得80%
//        EntityWrapper<AppRescueIndentEntity> wrapper = new EntityWrapper<>();
//        wrapper.eq("rescue_number", rescueNumber);
//        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(wrapper);
//        if (appRescueIndentEntity == null) {
//            result.put("code", 402);
//            result.put("msg", "检查订单编号");
//            return result;
//        }
//        if (appRescueIndentEntity.getPayState() == 0) {
//            result.put("code", 402);
//            result.put("msg", "该订单还未支付");
//            return result;
//        } else if (appRescueIndentEntity.getPayState() == 2) {
//            result.put("code", 402);
//            result.put("msg", "该订单已支付，请勿重新支付");
//            return result;
//        }
//        //查询到订单数据
//        //给商户加钱
//        //行锁
//
//        try {
//            appRescueIndentService.addMerchantsAmount(appRescueIndentEntity);
//            //修改订单状态
//            appRescueIndentEntity.setState(3);
//            appRescueIndentService.updateById(appRescueIndentEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //救援
//        Message sendMsg = new Message("all", "jgts_jy", appRescueIndentEntity.getRescueNumber().getBytes());
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.put("code", 200);
//        result.put("msg", "救援结算成功");
//        return result;
//    }


    @ApiOperation(value = "等待救援技师" +
            "407:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "救援订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/waitRescueTechnician", method = RequestMethod.GET)
    public JSONObject waitRescueTechnician(HttpServletRequest request, String orderNumber) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 407);
            result.put("msg", "订单编号为空");
            return result;
        }

        EntityWrapper<AppRescueIndentEntity> appRescueIndentWrapper = new EntityWrapper<>();
        appRescueIndentWrapper.eq("rescue_number", orderNumber);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(appRescueIndentWrapper);
        if (appRescueIndentEntity == null) {
            result.put("code", 407);
            result.put("msg", "订单编号有误");
            return result;
        }
        //技师id
        JSONObject in = appRescueIndentService.findRescueTechnician(appRescueIndentEntity);

        if (in == null) {
            result.put("code", 407);
            result.put("msg", "检查参数");
        } else {
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", in);
        }
        return result;

    }


    //取消救援订单
//    @ApiOperation(value = "取消救援订单" +
//            "407：订单编号为空或者有误" +
//            "402：请操作自己的订单" +
//            "401：订单未支付或者已完成" +
//            "403：下单时间大于三十分钟")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
//    public JSONObject cancelOrder(String orderNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//
//        if (StringUtils.isEmpty(orderNumber)) {
//            result.put("code", 407);
//            result.put("msg", "订单编号为空");
//            return result;
//        }
//        EntityWrapper<AppRescueIndentEntity> appRescueIndentWrapper = new EntityWrapper<>();
//        appRescueIndentWrapper.eq("rescue_number", orderNumber);
//        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(appRescueIndentWrapper);
//        if (appRescueIndentEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "订单编号有误");
//            return result;
//        }
//
//        if (!appRescueIndentEntity.getUserId().toString().equals(String.valueOf(currentLoginUser.getAppUserEntity().getId()))) {
//            result.put("code", 402);
//            result.put("msg", "请操作自己的订单！");
//            return result;
//        }
//
//        if (appRescueIndentEntity.getPayState() != 1) {
//            result.put("code", 401);
//            result.put("msg", "订单未支付！");
//            return result;
//        }
//
//        if (appRescueIndentEntity.getState() != 1) {
//            result.put("code", 401);
//            result.put("msg", "已完成订单不能取消！");
//            return result;
//        }
//
//        //判断下单时间是否超过三十分钟
//
//        Calendar dateOne = Calendar.getInstance();
//        Calendar dateTwo = Calendar.getInstance();
//        dateOne.setTime(new Date());//设置为当前系统时间
//        dateTwo.setTime(appRescueIndentEntity.getCreateTime());//获取数据库中的时间
//        long timeOne = dateOne.getTimeInMillis();
//        long timeTwo = dateTwo.getTimeInMillis();
//        long minute = (timeOne - timeTwo) / (1000 * 60);//转化minute
//        //判断下单时间是否大于30分钟
//        if (minute > 30) {
//        //大于30分钟，不能退款
//            result.put("code", 403);
//            result.put("msg", "下单时间大于三十分钟请联系客服");
//            return result;
//        }
//
//        try {
//            appRescueIndentService.cancelOrder(orderNumber, appRescueIndentEntity);
//        } catch (CusException e) {
//            result.put("code", e.getCode());
//            result.put("msg", e.getMessage());
//            return result;
//        }
//
//        result.put("code", 200);
//        result.put("msg", "取消订单成功");
//        return result;
//    }


}
