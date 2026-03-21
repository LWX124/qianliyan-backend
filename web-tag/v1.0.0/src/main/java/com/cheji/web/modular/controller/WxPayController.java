package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.AppWxpayOrderMapper;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.PayResponPojo;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.IpUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Api("微信支付")
@RestController
@RequestMapping("/wx/pay")
public class WxPayController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(WxPayController.class);


    @Resource
    private WxPayService wxPayService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppWxpayOrderMapper appWxpayOrderMapper;

    @Resource
    private AppWxService appWxService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppUserPlusService appUserPlusService;

    @Resource
    private RedisLock redisLock;

    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppUserBMessageService appUserBMessageService;


    private final String body = "车己汽车-开通PLUS会员";

    @Value("${host}")
    private String host;


    @ApiOperation(value = "统一下单，并组装所需支付参数" +
            "401：参数错误")
    @PostMapping("/createOrder")
    public JSONObject createOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) throws WxPayException {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        String price = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_MEMBER_PRICE);
        if (StringUtils.isEmpty(price)) {
            price = "39900";
        }
        Integer amount = Integer.parseInt(price);
        if (amount == null || amount <= 0) {
            result.put("code", 401);
            result.put("msg", "金额不能小于0");
            return result;
        }

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult = null;
        try {
            wxPayUnifiedOrderRequest.setBody(body);
            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
            wxPayUnifiedOrderRequest.setTotalFee(amount);
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "cServer/wx/pay/notify");
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            logger.info("#### createOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            logger.info("#### createOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            appWxService.insert(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), ConsEnum.PLUS_MEMBER.getCode(), 0, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PayResponPojo payResponPojo = new PayResponPojo();
        payResponPojo.setWxPayAppOrderResult(wxPayAppOrderResult);
        payResponPojo.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());

        result.put("code", 200);
        result.put("data", payResponPojo);
        return result;
    }

    @ApiOperation(value = "统一下单，自定义支付" +
            "401：参数错误" +
            "406：优惠卷有误" +
            "type: 4：洗车,5:美容,6：救援,7:喷漆,8:年检,9:代驾,10:商户支付之后看消息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "messageType", value = "信息类型，只有商户支付才需要", required = false, dataType = "int"),
    })
    @PostMapping("/createCusOrder")
    public JSONObject createCusOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) throws WxPayException {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();

        return appWxService.createCusOrder(in, userid, body, result, httpServletRequest, currentLoginUser, host);
    }

    @ApiOperation(value = "统一下单，信息费充值" +
            "401：参数错误" +
            "530：用户未登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "amount", value = "充值金额（最低1元）", required = true, dataType = "int"),
    })
    @PostMapping("/createInfoOrder")
    public JSONObject createInfoOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        Integer amount = in.getInteger("amount");
        if (amount == null || amount < 1) {
            result.put("code", 401);
            result.put("msg", "金额错误");
            return result;
        }
        amount = amount * 100;
//        if (!profileActive.equals("pro")) {
//            amount = 1;
//        }
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult = null;
        try {
            wxPayUnifiedOrderRequest.setBody("车己信息费");
            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
            wxPayUnifiedOrderRequest.setTotalFee(amount);
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "cServer/wx/pay/notify");
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            logger.info("#### createInfoOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            logger.info("#### createInfoOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            appWxService.insert(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), ConsEnum.INFO_INVEST.getCode(), 0, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PayResponPojo payResponPojo = new PayResponPojo();
        payResponPojo.setWxPayAppOrderResult(wxPayAppOrderResult);
        payResponPojo.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        result.put("code", 200);
        result.put("data", payResponPojo);
        return result;
    }


    //返回优惠卷信息
    @ApiOperation(value = "返回优惠卷信息" +
            "407,408：订单编号为空/有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单号", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/couponInformation", method = RequestMethod.GET)
    public JSONObject couponInformation(HttpServletRequest httpServletRequest, String orderNumber) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        //判断订单类型
        //查询下面的优惠卷列表
        //返回id，金额
        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 407);
            result.put("msg", "订单编号为空");
            return result;
        }
        JSONObject object = new JSONObject();
        String substring = orderNumber.substring(0, 2);
        AppUserBMessageEntity appUserBMessage = new AppUserBMessageEntity();
        if (substring.equals("PQ")) {
            //喷漆，查询到喷漆订单
            AppSprayPaintIndentEntity appSprayPaintIndent = appSprayPaintIndentService.selectByNumber(orderNumber);
            //然后查询到喷漆优惠卷列表
            //查询技师
            Integer userBId = appSprayPaintIndent.getTechnicianId();
            appUserBMessage = appUserBMessageService.selectById(userBId);

            AppUserCouponEntity couponList = appUserCouponService.findCouponByType(userid, 1);
            object.put("id", appSprayPaintIndent.getId());
            object.put("money", appSprayPaintIndent.getPrice());
            if (appSprayPaintIndent.getPrice().intValue() < 600) {
                object.put("couponList", new AppUserCouponEntity());
            } else {
                object.put("couponList", couponList);
            }
        } else if (substring.equals("NJ")) {
            String price;
            if (orderNumber.equals("NJ1")) {
                //免检代办
                price = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_IN_ONLINE);
            } else {
                //线上年检
                price = stringRedisTemplate.opsForValue().get(RedisConstant.YEAR_CHECK_ON_OFFLINE);
            }
            //查询到年检订单，和年检优惠卷列表
            // AppYearCheckIndentEntity yearCheckIndent = appYearCheckIndentService.selectByYearNumber(orderNumber);
            //查询到年检技师
            // appUserBMessage = appUserBMessageService.selectByBIdWorkType(yearCheckIndent.getUserBId(),2);
            //查询到喷漆优惠卷列表
            AppUserCouponEntity couponByType = appUserCouponService.findCouponByType(userid, 3);
            object.put("id", "");
            object.put("money", price);
            double v = Double.parseDouble(price);
            if (v < 20) {
                object.put("couponList", null);
            } else {
                if (couponByType == null) {
                    object.put("couponList", null);
                } else {
                    object.put("couponList", couponByType);
                }
            }
        } else if (substring.equals("SC")) {
            //查询到代驾订单，代驾优惠列表
            AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent = appSubstituteDrivingIndentService.selectBySubNumber(orderNumber);
            //查询代驾技师
            appUserBMessage = appUserBMessageService.selectById(appSubstituteDrivingIndent.getUserBId());
            AppUserCouponEntity couponByType = appUserCouponService.findCouponByType(userid, 2);
            object.put("id", appSubstituteDrivingIndent.getId());
            object.put("money", appSubstituteDrivingIndent.getActualPrice());
            if (appSubstituteDrivingIndent.getActualPrice().intValue() < 20) {
                object.put("couponList", new AppUserCouponEntity());
            } else {
                object.put("couponList", couponByType);
            }
        } else {
            result.put("code", 408);
            result.put("msg", "订单编号有误");
            return result;
        }

        object.put("name", appUserBMessage.getName());
        object.put("headImg", appUserBMessage.getHeadImg());
        object.put("score", appUserBMessage.getScore());
        object.put("technologyYear", appUserBMessage.getTechnologyYear());
        object.put("driverYear", appUserBMessage.getDriverYear());

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", object);
        return result;

    }

    /**
     * 获得唯一订单号
     */
    public static String getUniqueOrder() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format2 = format.format(new Date());
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            //有可能是负数
            hashCodeV = -hashCodeV;
        }
        return "pk" + format2 + String.format("%012d", hashCodeV);
    }


    @ResponseBody
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "application/json")
    public String wxpayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String orderId = result.getOutTradeNo();
        String tradeNo = result.getTransactionId();
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());

        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + orderId;
        redisLock.lock(redisKey);
        try {
            //判断金额
            AppWxpayOrderEntity tradeNoParamer = new AppWxpayOrderEntity();
            tradeNoParamer.setOutTradeNo(orderId);
            AppWxpayOrderEntity appWxpayOrderEntity = appWxpayOrderMapper.selectOne(tradeNoParamer);

            if (appWxpayOrderEntity == null) {
                logger.error(" 微信支付回调 #### 商户订单号 未找到对应的订单 result={}", result);
                return WxPayNotifyResponse.fail("商户订单号 未找到对应的订单");
            }

            if (result.getResultCode().equals("SUCCESS")) {
                if ((appWxpayOrderEntity.getAmount().divide(new BigDecimal(100))).compareTo(new BigDecimal(totalFee)) != 0) {
                    logger.error(" 微信支付回调 #### 金额不对 result={}；appWxpayOrderEntity.getAmount()={}；totalFee={}", result, appWxpayOrderEntity.getAmount(), totalFee);
                    return WxPayNotifyResponse.fail("金额不对");
                }
                if (appWxpayOrderEntity.getStatus() == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {
                    if (appWxpayOrderEntity.getType() == ConsEnum.PLUS_MEMBER.getCode()) {
                        //开通plus会员
                        appUserPlusService.insetPlus(appWxpayOrderEntity.getOutTradeNo(), appWxpayOrderEntity.getUserId());
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.CLEAN_CAR_PAY.getCode()) {
                        //洗车
                        appWxService.clearCarNotify(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.BEAUTY_CAR_PAY.getCode()) {
                        //美容
                        appWxService.beautyUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.RESCUE_CAR_PAY.getCode()) {
                        //救援
                        appWxService.rescueUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.SPRAY_CAR_PAY.getCode()) {
                        //喷漆
                        appWxService.sprayUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.CHECK_CAR_PAY.getCode()) {
                        //年检
                        appWxService.checkUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.SUBSITUDE_CAR_PAY.getCode()) {
                        //代驾
                        appWxService.subsitudeUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.MESSAGE_CAR_PAY.getCode()) {
                        //信息
                        appWxService.messagePayUpdate(orderId);
                    } else if (appWxpayOrderEntity.getType() == ConsEnum.INFO_INVEST.getCode()) {
                        //给用户余额加钱
                        appWxService.addMoney(appWxpayOrderEntity);
                    }

                } else {
                    //重复通知 不需要做处理
                    logger.info("### 微信支付回调  重复通知  不需要做处理## result={}", result);
                }
            } else {
                //支付失败
                appWxService.updateOrderStatus(appWxpayOrderEntity.getOutTradeNo(), ConsEnum.WX_PAY_ORDER_FAIL.getCode());
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        } finally {
            redisLock.unlock(redisKey);
        }
    }


    @ApiOperation(value = "统一下单，查询订单状态" +
            "401：参数错误" +
            "530：用户未登录" +
            "402：根据预支付id没有找到对应订单" +
            "403：支付失败" +
            "200：支付成功" +
            "201：用户支付中，稍后重新查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "outTradeNo", value = "订单号", required = true, dataType = "string"),
    })
    @PostMapping("/queryOrderStatus")
    public JSONObject queryOrderStatus(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        String outTradeNo = in.getString("outTradeNo");
        if (StringUtils.isEmpty(outTradeNo)) {
            result.put("code", 401);
            result.put("msg", "入参错误");
            return result;
        }

        //加个redis锁 防止回调通知和主动查询同时为用户开通plus会员
        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + outTradeNo;
        redisLock.lock(redisKey);
        try {
            AppWxpayOrderEntity paramerPrepayId = new AppWxpayOrderEntity();
            paramerPrepayId.setOutTradeNo(outTradeNo);
            AppWxpayOrderEntity appWxpayOrderEntity = appWxpayOrderMapper.selectOne(paramerPrepayId);
            logger.info("###  支付查询 appWxpayOrderEntity={}", appWxpayOrderEntity);
            if (appWxpayOrderEntity == null) {
                result.put("code", 402);
                result.put("msg", "根据订单号没有找到对应订单！");
                return result;
            }

            Integer status = appWxpayOrderEntity.getStatus();
            if (status == ConsEnum.WX_PAY_ORDER_FAIL.getCode()) {
                result.put("code", 403);
                result.put("msg", "支付失败！");
                return result;
            }

            if (status == ConsEnum.WX_PAY_ORDER_SUCCESS.getCode()) {
                result.put("code", 200);
                result.put("msg", "支付成功！");
                return result;
            }

            //支付回调通知还没收到结果，需要主动查询
            WxPayOrderQueryResult wxPayOrderQueryResult = null;
            try {
                wxPayOrderQueryResult = this.wxPayService.queryOrder("", appWxpayOrderEntity.getOutTradeNo());
            } catch (WxPayException e) {
                e.printStackTrace();
            }
            logger.info("###  支付查询 wxPayOrderQueryResult={}", wxPayOrderQueryResult);
            logger.info("wxPayOrderQueryResult.getTradeState()={}", wxPayOrderQueryResult.getTradeState());
            logger.info("wxPayOrderQueryResult   true or  false={}", wxPayOrderQueryResult.getTradeState().equals("SUCCESS"));

            if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
                result.put("code", 200);
                result.put("msg", "支付成功！");

                if (appWxpayOrderEntity.getType() == ConsEnum.PLUS_MEMBER.getCode()) {
                    //开通plus会员
                    appUserPlusService.insetPlus(appWxpayOrderEntity.getOutTradeNo(), currentLoginUser.getAppUserEntity().getId());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.CLEAN_CAR_PAY.getCode()) {
                    //洗车
                    appWxService.clearCarNotify(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.BEAUTY_CAR_PAY.getCode()) {
                    //美容
                    appWxService.beautyUpdate(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.RESCUE_CAR_PAY.getCode()) {
                    //救援
                    appWxService.rescueUpdate(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.SPRAY_CAR_PAY.getCode()) {
                    //喷漆
                    appWxService.sprayUpdate(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.CHECK_CAR_PAY.getCode()) {
                    //年检
                    appWxService.checkUpdate(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.SUBSITUDE_CAR_PAY.getCode()) {
                    //代驾
                    appWxService.subsitudeUpdate(appWxpayOrderEntity.getOutTradeNo());
                } else if (appWxpayOrderEntity.getType() == ConsEnum.MESSAGE_CAR_PAY.getCode()) {
                    //信息
                    appWxService.messagePayUpdate(appWxpayOrderEntity.getOutTradeNo());
                }else if (appWxpayOrderEntity.getType() == ConsEnum.INFO_INVEST.getCode()) {
                    //给用户余额加钱
                    appWxService.addMoney(appWxpayOrderEntity);
                }

                return result;
            }

            if (wxPayOrderQueryResult.getTradeState().equals("USERPAYING")) {
                result.put("code", 201);
                result.put("msg", "用户支付中！");
                return result;
            }
            appWxService.updateOrderStatus(appWxpayOrderEntity.getOutTradeNo(), ConsEnum.WX_PAY_ORDER_FAIL.getCode());
            result.put("code", 403);
            result.put("msg", "支付失败！");
        } finally {
            logger.info("##解锁###");
            redisLock.unlock(redisKey);
        }
        return result;
    }

}
