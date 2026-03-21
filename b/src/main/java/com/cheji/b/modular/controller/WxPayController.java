package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.constant.ConsEnum;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppWxpayOrderEntity;
import com.cheji.b.modular.domain.PushBillEntity;
import com.cheji.b.modular.lock.RedisLock;
import com.cheji.b.modular.mapper.AppWxpayOrderMapper;
import com.cheji.b.modular.service.AppWxService;
import com.cheji.b.modular.service.PushBillService;
import com.cheji.b.pojo.PayResponPojo;
import com.cheji.b.pojo.TokenPojo;
import com.cheji.b.utils.GenerateDigitalUtil;
import com.cheji.b.utils.IpUtil;
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
    private RedisLock redisLock;

    @Resource
    private AppWxService appWxService;

    @Resource
    private PushBillService pushBillService;

    @Value("${host}")
    private String host;

    private final String body = "车己-b端充值";


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
        Integer type = in.getInteger("type");

        Integer amount = 0;
        //Integer type = in.getInteger("type");

        String bodyContent = "车己信息";
        String orderNo = "";
        String id = in.getString("id");
        //messagetype 信息来源 //1 pb  2 sos
        Integer messageType = in.getInteger("messageType");
        if (StringUtils.isEmpty(id)) {
            result.put("code", 406);
            result.put("msg", "信息id为空");
            return result;
        }
        //pb
        //查询到具体的信息状态
        //根据id和type
        PushBillEntity pushBillEntity = pushBillService.selectById(id);
        if (pushBillEntity == null) {
            result.put("code", 406);
            result.put("msg", "订单不存在");
            return result;
        }
        //创建编号
        String pbNumber = GenerateDigitalUtil.getOrderNo();
        orderNo = "pb" + pbNumber;
        pushBillEntity.setPbNumber(orderNo);
        pushBillService.updateById(pushBillEntity);
        amount = pushBillEntity.getDeduction().multiply(new BigDecimal("100")).intValue();
        if (amount == 0 || amount == null) {
            amount = 1000;
        }
        PayResponPojo payResponPojo = new PayResponPojo();

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult = null;
        try {
            wxPayUnifiedOrderRequest.setBody(bodyContent);
            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
            logger.error("金额数字 amount={}", amount);
            wxPayUnifiedOrderRequest.setTotalFee(amount);
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "bServer/wx/pay/notify");
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            logger.info("#### createOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            logger.info("#### createOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            appWxService.insertCuser(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), type, userid, in, orderNo);
        } catch (Exception e) {
            logger.error("错误信息 e={}", e);
            e.printStackTrace();
        }

        payResponPojo.setWxPayAppOrderResult(wxPayAppOrderResult);
        payResponPojo.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());

        result.put("code", 200);
        result.put("data", payResponPojo);
        return result;

    }


    @ApiOperation(value = "统一下单，信息费充值" +
            "401：参数错误" +
            "530：用户未登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "amount", value = "充值金额（最低1元）", required = true, dataType = "int"),
    })
    @PostMapping("/createInfoOrder")
    public JSONObject createOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
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
            wxPayUnifiedOrderRequest.setBody(body);
            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
            wxPayUnifiedOrderRequest.setTotalFee(amount);
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "bServer/wx/pay/notify");
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            logger.info("#### createInfoOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            logger.info("#### createInfoOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            appWxService.insert(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), ConsEnum.INFO_INVEST.getCode());
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
        return "info" + format2 + String.format("%012d", hashCodeV);
    }


    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "application/json")
    public String wxpayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String orderId = result.getOutTradeNo();
        String tradeNo = result.getTransactionId();
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());

        String redisKey = RedisConstant.MONEY_ADD_LOCK + orderId;
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
                if (appWxpayOrderEntity.getAmount().divide(new BigDecimal(100)).compareTo(new BigDecimal(totalFee)) != 0) {
                    logger.error(" 微信支付回调 #### 金额不对 result={}；appWxpayOrderEntity.getAmount()={}；totalFee={}", result, appWxpayOrderEntity.getAmount(), totalFee);
                    return WxPayNotifyResponse.fail("金额不对");
                }
                if (appWxpayOrderEntity.getStatus() == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {
                    //开通plus会员
//                    appUserPlusService.insetPlus(appWxpayOrderEntity.getOutTradeNo(), appWxpayOrderEntity.getUserId());
                    //信息费钱包加钱
                    switch (appWxpayOrderEntity.getType()) {
                        case 2:
                            //信息费钱包加钱
                            break;
                        case 3:
                            //b端商户充值
                            break;

                    }
                    //给用户余额加钱
                    appWxService.addMoney(appWxpayOrderEntity);
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
            @ApiImplicitParam(paramType = "query", name = "outTradeNo", value = "订单id", required = true, dataType = "string"),
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
        String redisKey = RedisConstant.MONEY_ADD_LOCK + outTradeNo;
        redisLock.lock(redisKey);
        try {
            AppWxpayOrderEntity paramerPrepayId = new AppWxpayOrderEntity();
            paramerPrepayId.setOutTradeNo(outTradeNo);
            AppWxpayOrderEntity appWxpayOrderEntity = appWxpayOrderMapper.selectOne(paramerPrepayId);

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

            if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
                //给用户加钱
                appWxService.addMoney(appWxpayOrderEntity);
                result.put("code", 200);
                result.put("msg", "支付成功！");
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
            return result;
        } finally {
            logger.info("##解锁###");
            redisLock.unlock(redisKey);
        }
    }


//    @ApiOperation(value = "统一下单，商户充值" +
//            "401：参数错误" +
//            "530：用户未登录")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "amount", value = "充值金额（最低1元，单位分）", required = true, dataType = "int"),
//    })
//    @PostMapping("/createPartnerOrder")
//    public JSONObject createPartnerOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
//        JSONObject result = new JSONObject();
//        Integer amount = in.getInteger("amount");
//        if (amount == null || amount < 100) {
//            result.put("code", 401);
//            result.put("msg", "金额错误");
//            return result;
//        }
//
//        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//
//        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
//        WxPayAppOrderResult wxPayAppOrderResult = null;
//        try {
//            wxPayUnifiedOrderRequest.setBody(body);
//            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
//            wxPayUnifiedOrderRequest.setTotalFee(amount);
//            wxPayUnifiedOrderRequest.setNotifyUrl("http://175.6.112.42/b/wx/pay/notify");
//            wxPayUnifiedOrderRequest.setTradeType("APP");
//            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
//            logger.info("#### createPartnerOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
//            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
//            logger.info("#### createPartnerOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
//            appWxService.insert(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), ConsEnum.PARTNER_INVEST.getCode());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.put("code", 200);
//        result.put("data", wxPayAppOrderResult);
//        return result;
//    }

}
