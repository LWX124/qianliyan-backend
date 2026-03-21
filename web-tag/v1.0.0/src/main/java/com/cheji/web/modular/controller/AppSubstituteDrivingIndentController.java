package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代驾订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-06-08
 */
@RestController
@RequestMapping("/appSubstituteDrivingIndent")
public class AppSubstituteDrivingIndentController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AppSubstituteDrivingIndentController.class);


    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private LableService lableService;

    @Resource
    private DefaultMQProducer mqProducer;

    @Resource
    private AppSubstituteDrivingImgService appSubstituteDrivingImgService;

    @Resource
    private AppUserCouponService appUserCouponService;


    //保存代驾订单
    @ApiOperation(value = "保存代驾送车订单" +
            "409未结算" +
            "407参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "代驾类型 1.送车代驾，2.日常代驾 ，3.包时代驾", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "startPoint", value = "起点地点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startAddress", value = "起点地址(详情)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startLng", value = "起点经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "startLat", value = "起点纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "startName", value = "送车人名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startPhone", value = "送车人电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startRemark", value = "送车备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endPoint", value = "接车地点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endAddress", value = "接车地址(详情)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endLng", value = "接车经度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "endLat", value = "接车纬度", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "endName", value = "接车人名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endPhone", value = "接车人电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endRemark", value = "接车备注", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/saveSubstituteDrivingIndentIndent", method = RequestMethod.POST)
    public JSONObject saveSubstituteDrivingIndentIndent(@RequestBody AppSubstituteDrivingIndentEntity in, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();

        // 查询到有没有未支付的订单
//        List<Integer> appSubstituteDrivingIndentEntities = appSubstituteDrivingIndentService.selectNoPayList(userid);
//        if (!appSubstituteDrivingIndentEntities.isEmpty()) {
//            result.put("code", 409);
//            result.put("msg", "用户有进行中的订单");
//            return result;
//        }

        if (in.getType() == null) {
            result.put("code", 407);
            result.put("msg", "检查type");
            return result;
        }

        if (in.getType() != 1 && in.getType() != 2 && in.getType() != 3) {
            result.put("code", 407);
            result.put("msg", "检查type");
            return result;
        }
        if (in.getType() == 1) {
            if (in.getStartPoint() == null || in.getStartLat() == null || in.getStartAddress() == null
                    || in.getStartLng() == null || in.getStartName() == null || in.getStartPhone() == null) {
                result.put("code", 407);
                result.put("msg", "检查送车资料");
                return result;
            }
            if (in.getEndPoint() == null || in.getEndLat() == null || in.getEndLng() == null
                    || in.getEndName() == null || in.getEndPhone() == null || in.getEndAddress() == null) {
                result.put("code", 407);
                result.put("msg", "检查送车资料");
                return result;
            }
        } else {
            if (in.getStartPoint() == null || in.getStartLat() == null || in.getStartAddress() == null || in.getStartLng() == null) {
                result.put("code", 407);
                result.put("msg", "检查送车资料");
                return result;
            }
            if (in.getEndPoint() == null || in.getEndLat() == null || in.getEndLng() == null || in.getEndAddress() == null) {
                result.put("code", 407);
                result.put("msg", "检查送车资料");
                return result;
            }
        }
        //订单编号
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "SC" + orderNo;
        in.setSubstituteDrivingNumber(orderNo);
        in.setPayState(0);
        in.setIndentState(0);
        in.setCreateTime(new Date());
        in.setUpdateTime(new Date());


        //查询到b端商户
        JSONObject getprice = appSubstituteDrivingIndentService.getprice(in.getType(), in.getStartLng(), in.getStartLat(), in.getEndLng(), in.getEndLat());
        if (StringUtils.isNotEmpty(getprice.getString("msg"))) {
            return getprice;
        }

        //预估价格
        in.setEstimatePrice(getprice.getBigDecimal("price"));
        //实际价格
        in.setActualPrice(getprice.getBigDecimal("price"));
        in.setDistance(getprice.getBigDecimal("distance"));
        //保存数据

//        JSONObject merchants = appRescueIndentService.getMerchants(in.getStartLng(), in.getStartLat(), null, RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO);
//        Integer userBId = merchants.getInteger("userBId");
//        if (userBId == null) {
//            result.put("code", 407);
//            result.put("msg", "未查询到附近商户");
//            return result;
//        }
//        in.setUserBId(userBId);
        in.setUserId(userid);

        appSubstituteDrivingIndentService.insert(in);

        //转日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(in.getCreateTime());
        in.setTime(format);

        if (in.getEndTime() != null) {
            String format1 = sdf.format(in.getEndTime());
            in.setOvertime(format1);
        }

//        Message sendMsg = new Message("all", "jgts_SC", in.getSubstituteDrivingNumber().getBytes());
//
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        AppUserCouponEntity couponByType = appUserCouponService.findCouponByType(userid, 2);
        JSONObject js = new JSONObject();
        js.put("id", in.getId());
        js.put("money", in.getActualPrice());
        js.put("orderNumber", in.getSubstituteDrivingNumber());
        js.put("couponList", couponByType);


        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", js);
        return result;
    }


    @ApiOperation(value = "计算代驾等价格" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "代驾类型(1.送车代驾。2.酒后代驾)", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "startLng", value = "救援经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "startLat", value = "救援纬度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "endLng", value = "目的地经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "endLat", value = "目的地纬度", required = true, dataType = "BigDecimal"),
    })
    @RequestMapping(value = "/getSubDrivePrice", method = RequestMethod.POST)
    public JSONObject getSubDrivePrice(@RequestBody JSONObject jsonObject) {
        JSONObject result = new JSONObject();

        Integer type = jsonObject.getInteger("type");
        if (type != 1 && type != 2) {
            result.put("code", 405);
            result.put("msg", "失败，请检查type");
        }

        BigDecimal startLng = jsonObject.getBigDecimal("startLng");
        BigDecimal startLat = jsonObject.getBigDecimal("startLat");
        BigDecimal endLng = jsonObject.getBigDecimal("endLng");
        BigDecimal endLat = jsonObject.getBigDecimal("endLat");
        if (startLng == null || startLat == null || endLng == null || endLat == null) {
            result.put("code", 405);
            result.put("msg", "失败，请检查经纬度");
        }
        JSONObject in = appSubstituteDrivingIndentService.getprice(type, startLng, startLat, endLng, endLat);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    //代驾订单详情
    @ApiOperation(value = "查询代驾订单详情" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/subDirvingDetails", method = RequestMethod.GET)
    public JSONObject subDirvingDetails(String orderNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }
        //查询到代驾订单
        EntityWrapper<AppSubstituteDrivingIndentEntity> drivingIndentEntityWrapper = new EntityWrapper<>();
        drivingIndentEntityWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(drivingIndentEntityWrapper);
        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }

        //添加详情
        JSONObject in = new JSONObject();
        Integer indentState = appSubstitute.getIndentState();
        String ste;
        if (indentState == 1) {
            ste = "新订单";
        } else if (indentState == 2 || indentState == 3 || indentState == 4 || indentState == 5) {
            ste = "服务中";
        } else if (indentState == 6) {
            ste = "待评价";
        } else if (indentState == 8 || indentState == 9) {
            ste = "已取消";
        } else {
            ste = "已完成";
        }
        LableEntity lableEntity = lableService.selectById(19);
        in.put("url", lableEntity.getUrl());
        in.put("indentState", indentState);
        in.put("state", ste);
        in.put("orderNumber", appSubstitute.getSubstituteDrivingNumber());
        in.put("type", appSubstitute.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pickCarTime = appSubstitute.getPickCarTime();
        // String format = sdf.format(appSubstitute.getPickCarTime());
        in.put("pickCarTime", pickCarTime);

        in.put("startAddress", appSubstitute.getStartAddress());

        // String format1 = sdf.format(appSubstitute.getSendTime());
        in.put("sendCarTime", appSubstitute.getSendTime());
        in.put("endAddress", appSubstitute.getEndAddress());

        //计算耗时
        Integer waitTime = appSubstituteDrivingIndentService.getTime(appSubstitute);
        if (waitTime == null) {
            waitTime = 0;
            in.put("workTime", waitTime);
        }
        in.put("ditance", appSubstitute.getDistance());
        in.put("actualPrice", appSubstitute.getActualPrice());
        //查询代驾状态
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(appSubstitute.getUserBId());
        in.put("bussinesState", appUserBMessageEntity.getBusinessType());

        //查询图片
        EntityWrapper<AppSubstituteDrivingImgEntity> appSubstituteImgWrapper = new EntityWrapper<>();
        appSubstituteImgWrapper.eq("subsitute_driving_id", appSubstitute.getId())
                .eq("type", 1);
        List<AppSubstituteDrivingImgEntity> appSubstituteDrivingImgEntities = appSubstituteDrivingImgService.selectList(appSubstituteImgWrapper);
        in.put("pickCarImg", appSubstituteDrivingImgEntities);

        EntityWrapper<AppSubstituteDrivingImgEntity> appSubstituteImgWrapper2 = new EntityWrapper<>();
        appSubstituteImgWrapper2.eq("subsitute_driving_id", appSubstitute.getId())
                .eq("type", 2);
        List<AppSubstituteDrivingImgEntity> appSubstituteDrivingImgEntities2 = appSubstituteDrivingImgService.selectList(appSubstituteImgWrapper2);
        in.put("sendCarImg", appSubstituteDrivingImgEntities2);

        in.put("userBId", appUserBMessageEntity.getUserBId());


        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    //取消订单
    @ApiOperation(value = "取消订单" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reason", value = "取消原因", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/cancelSubstituteIndent", method = RequestMethod.GET)
    public JSONObject cancelSubstituteIndent(String orderNumber, String reason, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "订单编号为空");
            return result;
        }
        //查询到代驾订单
        EntityWrapper<AppSubstituteDrivingIndentEntity> drivingIndentEntityWrapper = new EntityWrapper<>();
        drivingIndentEntityWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(drivingIndentEntityWrapper);
        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }

        //判断操作
        if (!appSubstitute.getUserId().equals(userid)) {
            result.put("code", 405);
            result.put("msg", "请操作自己订单");
            return result;
        }

        if (appSubstitute.getIndentState() > 3) {
            result.put("code", 405);
            result.put("msg", "目前订单状态不支持取消");
            return result;
        }

        appSubstitute.setIndentState(8);
        appSubstitute.setCancelReason(reason);
        appSubstitute.setCancelResource("C");
        appSubstituteDrivingIndentService.updateById(appSubstitute);

        Message sendMsg = new Message("all", "jgts_SC", appSubstitute.getSubstituteDrivingNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("code", 200);
        result.put("msg", "订单已取消");
        return result;


    }

    @ApiOperation(value = "等待代驾师傅" +
            "407:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "substituteDrivingNumber", value = "代驾订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findSubTechnician", method = RequestMethod.GET)
    public JSONObject findSubTechnician(HttpServletRequest request, String substituteDrivingNumber) {
        JSONObject result = new JSONObject();
        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }

        if (StringUtils.isEmpty(substituteDrivingNumber)) {
            result.put("code", 407);
            result.put("msg", "订单编号为空");
            return result;
        }

        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteIndentWrapper = new EntityWrapper<>();
        appSubstituteIndentWrapper.eq("substitute_driving_number", substituteDrivingNumber);
        AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent = appSubstituteDrivingIndentService.selectOne(appSubstituteIndentWrapper);
        if (appSubstituteDrivingIndent == null) {
            result.put("code", 407);
            result.put("msg", "订单编号有误");
            return result;
        }
        JSONObject in = appSubstituteDrivingIndentService.findMessage(appSubstituteDrivingIndent);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    @ApiOperation(value = "确认代驾订单" +
            "407:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "substituteDrivingNumber", value = "代驾订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/confirmSubDriIndent", method = RequestMethod.GET)
    public JSONObject confirmSubDriIndent(HttpServletRequest request, String substituteDrivingNumber) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        //修改状态为6，给商户加钱
        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteIndentWrapper = new EntityWrapper<>();
        appSubstituteIndentWrapper.eq("substitute_driving_number", substituteDrivingNumber);
        AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent = appSubstituteDrivingIndentService.selectOne(appSubstituteIndentWrapper);
        if (appSubstituteDrivingIndent == null) {
            result.put("code", 407);
            result.put("msg", "订单编号有误");
            return result;
        }
        appSubstituteDrivingIndent.setIndentState(6);
        appSubstituteDrivingIndentService.updateById(appSubstituteDrivingIndent);

        //查询是否使用了优惠卷
        EntityWrapper<AppUserCouponEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_number", appSubstituteDrivingIndent.getSubstituteDrivingNumber());
        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectOne(entityWrapper);
        int type;
        if (appUserCouponEntity == null) {
            type = 0;
        } else {
            type = 1;
        }

        //商户加钱
        appSubstituteDrivingIndentService.substituAddMerchantsAmount(appSubstituteDrivingIndent, type);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


}