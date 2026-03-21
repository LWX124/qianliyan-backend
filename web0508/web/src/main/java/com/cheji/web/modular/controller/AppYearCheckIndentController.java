package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUserCouponEntity;
import com.cheji.web.modular.domain.AppYearCheckIndentEntity;
import com.cheji.web.modular.domain.BUserEntity;
import com.cheji.web.modular.excep.CusException;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 年检订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/appYearCheckIndent")
public class AppYearCheckIndentController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AppYearCheckIndentController.class);


    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private BUserService bUserService;

    @Value("${host}")
    private String host;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppRescueIndentService appRescueIndentService;

//    @Resource
//    private DefaultMQProducer mqProducer;


    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private AppWxService appWxService;


//    @ApiOperation(value = "保存年检订单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "licensePlate", value = "车牌号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话号码", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
//            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
//            @ApiImplicitParam(paramType = "query", name = "pickAddress", value = "接车地址", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名称", required = true, dataType = "Integer"),
//            @ApiImplicitParam(paramType = "query", name = "yearCheckType", value = "年检类型（1.免检代办，2.上线年检）", required = true, dataType = "Integer")
//            //@ApiImplicitParam(paramType = "query", name = "url", value = "图片地址", required = true, dataType = "String[]")
//    })
//    @RequestMapping(value = "/saveYearCheckIndent", method = RequestMethod.POST)
//    public JSONObject saveYearCheckIndent(@RequestBody AppYearCheckIndentEntity checkIndent, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        Integer userid = currentLoginUser.getAppUserEntity().getId();
//        if (StringUtils.isEmpty(checkIndent.getUsername()) || StringUtils.isEmpty(checkIndent.getLicensePlate()) || StringUtils.isEmpty(checkIndent.getPhone()) || checkIndent.getYearCheckType() == null) {
//            result.put("code", 405);
//            result.put("msg", "参数不能为空");
//            return result;
//        }
//
//        List<Integer> noPayIndent = appYearCheckIndentService.findNoPayIndent(userid);
//
////        if (checkIndent.getUrl().length == 0 || checkIndent.getUrl() == null) {
////            result.put("code", 405);
////            result.put("msg", "图片不能为空");
////            return result;
////        }
//        String orderNo = GenerateDigitalUtil.getOrderNo();
//        checkIndent.setYearCheckNumber("NJ" + orderNo);
//        checkIndent.setUserId(userid);
//        checkIndent.setCreateTime(new Date());
//        checkIndent.setUpdateTime(new Date());
//        String price = "";
//        if (checkIndent.getYearCheckType() == 1) {
//            //免检代办
//            price = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_IN_ONLINE);
//        } else if (checkIndent.getYearCheckType() == 2) {
//            //线上年检
//            price = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_ON_OFFLINE);
//            if (StringUtils.isEmpty(checkIndent.getPickAddress())) {
//
//                result.put("code", 407);
//                result.put("msg", "检查地址");
//                return result;
//            }
//        } else {
//            result.put("code", 406);
//            result.put("msg", "检查type类型");
//            return result;
//        }
//        if (StringUtils.isEmpty(price)) {
//            result.put("code", 407);
//            result.put("msg", "价格查询为空");
//            return result;
//        } else {
//            checkIndent.setPrice(new BigDecimal(price));
//        }
//
//        //接车地址为空
//        if (StringUtils.isEmpty(checkIndent.getLng().toString()) || StringUtils.isEmpty(checkIndent.getLat().toString())) {
//            //默认商户  todo
//            checkIndent.setUserBId(1);
//        } else {
//            //查询到b端就近的商户
//            JSONObject in = appRescueIndentService.getMerchants(checkIndent.getLng(), checkIndent.getLat(), "目的地", RedisConstant.MERCHANTS_YEAR_CHECK_GEO);
//            if (in == null) {
//                result.put("code", 407);
//                result.put("msg", "查询年检商户有误");
//                return result;
//            }
//            String userBId = in.getString("userBId");
//            if (StringUtils.isEmpty(userBId)) {
//                result.put("code", 407);
//                result.put("msg", "查询年检商户有误");
//                return result;
//            }
//            checkIndent.setUserBId(Integer.valueOf(userBId));
//        }
//        appYearCheckIndentService.insert(checkIndent);
//        result.put("code", 200);
//        result.put("msg", "成功");
//        result.put("data", checkIndent.getYearCheckNumber());
//
//        //mq
//        Message sendMsg = new Message("all", "jgts_NJ", checkIndent.getYearCheckNumber().getBytes());
//
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String[] url = checkIndent.getUrl();
//        for (int i = 0; i < url.length; i++) {
//            AppYearCheckImgEntity appYearCheckImgEntity = new AppYearCheckImgEntity();
//            String s = url[i];
//            appYearCheckImgEntity.setAppYearCheckId(checkIndent.getId());
//            appYearCheckImgEntity.setUpdateTime(new Date());
//            appYearCheckImgEntity.setCreateTime(new Date());
//            appYearCheckImgEntity.setIndex(i + 1);
//            appYearCheckImgEntity.setUrl(s);
//            appYearCheckImgService.insert(appYearCheckImgEntity);
//        }
//
//        AppUserCouponEntity couponByType = appUserCouponService.findCouponByType(userid,3);
//
//        JSONObject js = new JSONObject();
//        js.put("id", checkIndent.getId().toString());
//        js.put("type", 8);
//        js.put("number", checkIndent.getYearCheckNumber());
//        if (couponByType==null){
//            js.put("couponId",null);
//        }else {
//            js.put("couponId",couponByType.getId());
//        }
//
//        return appWxService.createCusOrder(js, userid, "车己年检", result, request, currentLoginUser, host);
//    }


    @ApiOperation(value = "查询年检订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/yearCheckDetails", method = RequestMethod.GET)
    public JSONObject yearCheckDetails(String indentNumber) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppYearCheckIndentEntity> checkIndentWarpper = new EntityWrapper<>();
        checkIndentWarpper.eq("year_check_number", indentNumber);
        AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectOne(checkIndentWarpper);
        if (appYearCheckIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }
//        EntityWrapper<AppYearCheckImgEntity> imgEntityWarpper = new EntityWrapper<>();
//        imgEntityWarpper.eq("app_year_check_id", appYearCheckIndentEntity.getId());
//        List<AppYearCheckImgEntity> appYearCheckImgEntities = appYearCheckImgService.selectList(imgEntityWarpper);
        Integer userBId = appYearCheckIndentEntity.getUserBId();
        if (userBId == null) {
            result.put("code", 402);
            result.put("msg", "商户id有误");
            return result;
        }
        BUserEntity bUserEntity = bUserService.selectById(userBId);
        if (bUserEntity == null) {
            result.put("code", 402);
            result.put("msg", "商户id有误");
            return result;
        }
//        //查询到年检师傅电话
//        EntityWrapper<AppUserBMessageEntity> bMessageWrapper = new EntityWrapper<>();
//        bMessageWrapper.eq("user_b_id",userBId)
//                .eq("wrok_type",2);
//        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(bMessageWrapper);
//        String phone = appUserBMessageEntity.getPhone();

        JSONObject in = new JSONObject();
        in.put("id", appYearCheckIndentEntity.getId());
        in.put("licensePlate", appYearCheckIndentEntity.getLicensePlate());
        in.put("username", appYearCheckIndentEntity.getUsername());
        in.put("phone", appYearCheckIndentEntity.getPhone());
        in.put("pickAddress", appYearCheckIndentEntity.getPickAddress());
        in.put("lng", appYearCheckIndentEntity.getLng());
        in.put("lat", appYearCheckIndentEntity.getLat());
        in.put("payState", appYearCheckIndentEntity.getPayState());
        in.put("state", appYearCheckIndentEntity.getState());
        in.put("price", appYearCheckIndentEntity.getPrice());
        in.put("yearCheckType", appYearCheckIndentEntity.getYearCheckType());
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }

//    @ApiOperation(value = "年检结算")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/yearCheckSettlement", method = RequestMethod.GET)
//    public JSONObject yearCheckSettlement(String indentNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        EntityWrapper<AppYearCheckIndentEntity> checkIndentWarpper = new EntityWrapper<>();
//        checkIndentWarpper.eq("year_check_number", indentNumber);
//        AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectOne(checkIndentWarpper);
//        if (appYearCheckIndentEntity == null) {
//            result.put("code", 402);
//            result.put("msg", "检查订单编号");
//            return result;
//        }
//
//        return appYearCheckIndentService.merchantsAddBanlance(appYearCheckIndentEntity);
//    }


    @ApiOperation(value = "年检价格")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "1,免检代办，2.线上年检", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/yearCheckPrice", method = RequestMethod.GET)
    public JSONObject yearCheckSettlement(Integer type, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String s = "";
        if (type == 1) {
            //免检代办
            s = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_IN_ONLINE);
        } else if (type == 2) {
            //线上年检
            s = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_ON_OFFLINE);
        } else {
            result.put("code", 406);
            result.put("msg", "检查type");
            return result;
        }
        if (StringUtils.isEmpty(s)) {
            result.put("code", 407);
            result.put("msg", "价格查询为空");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", s);
        return result;
    }

    @ApiOperation(value = "等待年检技师" +
            "407:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "喷漆订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/waitYearIndentTechnician", method = RequestMethod.GET)
    public JSONObject waitYearIndentTechnician(HttpServletRequest request, String orderNumber) {
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

        EntityWrapper<AppYearCheckIndentEntity> checkIndentWrapper = new EntityWrapper<>();
        checkIndentWrapper.eq("year_check_number", orderNumber);
        AppYearCheckIndentEntity appYearCheckIndent = appYearCheckIndentService.selectOne(checkIndentWrapper);
        if (appYearCheckIndent == null) {
            result.put("code", 407);
            result.put("msg", "订单编号有误");
            return result;
        }

        //技师id
        JSONObject in = appYearCheckIndentService.finfYearIndentMessage(appYearCheckIndent);

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


//    //确认完成 修改state为3    Confirm the settlement
//    @ApiOperation(value = "结算年检订单" +
//            "407:参数有误")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "喷漆订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/confirmSettlement", method = RequestMethod.GET)
//    public JSONObject confirmSettlement(HttpServletRequest request, String orderNumber) {
//        JSONObject result = new JSONObject();
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
//
//        EntityWrapper<AppYearCheckIndentEntity> checkIndentWrapper = new EntityWrapper<>();
//        checkIndentWrapper.eq("year_check_number", orderNumber);
//        AppYearCheckIndentEntity appYearCheckIndent = appYearCheckIndentService.selectOne(checkIndentWrapper);
//        if (appYearCheckIndent == null) {
//            result.put("code", 407);
//            result.put("msg", "订单编号有误");
//            return result;
//        }
//
//        //结算
//        appYearCheckIndentService.merchantsAddBanlance(appYearCheckIndent);
//
//        result.put("code", 200);
//        result.put("msg", "成功");
//        return result;
//    }


//    @ApiOperation(value = "取消年检订单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/cancelYearCheckOrder", method = RequestMethod.GET)
//    public JSONObject cancelYearCheckOrder(String orderNumber, HttpServletRequest request) {
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
//
//        EntityWrapper<AppYearCheckIndentEntity> checkIndentWrapper = new EntityWrapper<>();
//        checkIndentWrapper.eq("year_check_number", orderNumber);
//        AppYearCheckIndentEntity appYearCheckIndent = appYearCheckIndentService.selectOne(checkIndentWrapper);
//        if (appYearCheckIndent == null) {
//            result.put("code", 407);
//            result.put("msg", "订单编号有误");
//            return result;
//        }
//        if (!appYearCheckIndent.getUserId().toString().equals(String.valueOf(currentLoginUser.getAppUserEntity().getId()))) {
//            result.put("code", 402);
//            result.put("msg", "请操作自己的订单！");
//            return result;
//        }
//
//        if (null == appYearCheckIndent.getPayState() || appYearCheckIndent.getPayState() != 1) {
//            result.put("code", 401);
//            result.put("msg", "订单未支付！");
//            return result;
//        }
//
//        if (appYearCheckIndent.getState() != 2) {
//            result.put("code", 401);
//            result.put("msg", "已完成订单不能取消！");
//            return result;
//        }
//
//        //取消年检订单,两个小时以内可以取消
//        Calendar dateOne = Calendar.getInstance();
//        Calendar dateTwo = Calendar.getInstance();
//        dateOne.setTime(new Date());//设置为当前系统时间
//        dateTwo.setTime(appYearCheckIndent.getCreateTime());//获取数据库中的时间
//        long timeOne = dateOne.getTimeInMillis();
//        long timeTwo = dateTwo.getTimeInMillis();
//        long minute = (timeOne - timeTwo) / (1000 * 60);//转化minute
//        //判断下单时间是否大于120分钟
//        if (minute > 120) {
//            //大于120分钟，不能退款
//            result.put("code", 403);
//            result.put("msg", "下单时间大于两小时请联系客服");
//            return result;
//        }
//        try {
//            appYearCheckIndentService.cancelYearCheckOrder(orderNumber, appYearCheckIndent);
//        } catch (CusException e) {
//            result.put("code", e.getCode());
//            result.put("msg", e.getMessage());
//            return result;
//        }
//
//        result.put("code", 200);
//        result.put("msg", "取消订单成功");
//        return result;
//
//        //点击去现场后不能取消
//    }


}
