package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.dao.*;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.HuaweiSmsService;
import com.jeesite.modules.util2.PasswordUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/web")
public class PrivateController extends BaseController {

    @Resource
    private HuaweiSmsService huaweiSmsService;

    @Resource
    private AppBUserDao appBUserDao;

    @Resource
    private AppRescueIndentDao appRescueIndentDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppBUserController appBUserController;

    @Resource
    private AppLableDetailsReviewTreeController appLableDetailsReviewTreeController;

    private final static Logger logger = LoggerFactory.getLogger(PrivateController.class);

    @RequestMapping("page")
    public String list() {
        return "modules/web/private";
    }

    @RequestMapping("downApp")
    public String downApp() {
        return "modules/web/downApp";
    }

    @ResponseBody
    @RequestMapping(value = "getCode", method = RequestMethod.POST)
    public JSONObject getCode(String phone) {
        JSONObject result = new JSONObject();
        final String regex = "^1[0-9]{10}$";
        if (!phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        //进行短信过滤
        String s = stringRedisTemplate.opsForValue().get(AppConstants.PRIVATE_USER_LIST);
        if (s == null) {
            result.put("code", 401);
            result.put("msg", "没有白名单");
            return result;
        }

        if (!s.contains(phone)) {
            result.put("code", 401);
            result.put("msg", "当前号码不在白名单内");
            return result;
        }

        String s1 = stringRedisTemplate.opsForValue().get(AppConstants.PRIVATE_USER_CODE_TIME + phone);
        if (StringUtils.isNotEmpty(s1)) {
            result.put("code", 401);
            result.put("msg", "1分钟之内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        huaweiSmsService.sendSmsByTemplate("4", phone, randomCode);

        stringRedisTemplate.opsForValue().set(AppConstants.PRIVATE_USER_CODE + phone, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(AppConstants.PRIVATE_USER_CODE_TIME + phone, randomCode, 60, TimeUnit.SECONDS);

        result.put("code", 200);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JSONObject login(String phone, String code) {
        JSONObject result = new JSONObject();
        final String regex = "^1[0-9]{10}$";
        if (!phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        //进行短信过滤
        String s = stringRedisTemplate.opsForValue().get(AppConstants.PRIVATE_USER_LIST);
        if (s == null) {
            result.put("code", 401);
            result.put("msg", "没有白名单");
            return result;
        }

        if (!s.contains(phone)) {
            result.put("code", 401);
            result.put("msg", "当前号码不在白名单内");
            return result;
        }

        String s1 = stringRedisTemplate.opsForValue().get(AppConstants.PRIVATE_USER_CODE + phone);
        if (StringUtils.isEmpty(s1) || !s1.equals(code)) {
            result.put("code", 401);
            result.put("msg", "验证码错误");
            return result;
        }

        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        stringRedisTemplate.opsForValue().set(AppConstants.PRIVATE_USER_AUTH + thirdSessionKey, phone, 60 * 60 * 3, TimeUnit.SECONDS);
        stringRedisTemplate.delete(AppConstants.PRIVATE_USER_CODE + phone);
        result.put("code", 200);
        result.put("msg", thirdSessionKey);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "ops", method = RequestMethod.POST)
    public JSONObject ops(String phone, String auth, String type) {
        JSONObject result = new JSONObject();

        final String regex = "^1[0-9]{10}$";
        if (!phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        String ops_phone = stringRedisTemplate.opsForValue().get(AppConstants.PRIVATE_USER_AUTH + auth);
        if (StringUtils.isEmpty(ops_phone)) {
            result.put("code", 401);
            result.put("msg", "权限已失效");
            return result;
        }

        logger.error("### 账号ops_phone={} 正在通过 用户={}的登录权限", ops_phone, phone);

        //根据电话号码获取id
        AppBUser appBUser = appBUserDao.findOne(phone);
        if (appBUser == null) {
            result.put("code", 401);
            result.put("msg", "商户不存在，请仔细检查号码");
            return result;
        }

        switch (type) {
            case "1":
                //登录权限
                String s = appBUserController.passAccount(appBUser.getId());
                result.put("msg", s);
                break;
            case "2":
                // 收事故
                String s1 = appBUserController.addgeo(appBUser.getId());
                result.put("msg", s1);
                break;
            case "3":
                List<AppLableDetailsReviewTree> list = appBUserDao.findByUserId(appBUser.getId());
                for (AppLableDetailsReviewTree appLableDetailsReviewTree : list) {
                    appLableDetailsReviewTreeController.pass(appLableDetailsReviewTree.getLableCode());
                }

                // 上架
                String s2 = appBUserController.updateState(appBUser.getId());
                result.put("msg", s2);
                break;
        }

        result.put("code", 200);
        return result;
    }

    @Resource
    private WxPayService wxPayService;

    @Resource
    private RedisLock redisLock;

    @Resource
    private AppWxpayBackDao appWxpayBackDao;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppUserPlusDao appUserPlusDao;

    @Resource
    private AppCleanIndetDao appCleanIndetDao;

    /**
     * 退款结果通知
     */
    @RequestMapping(value = "backMoneyNotify")
    @ResponseBody
    public String backMoneyNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlResult);

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String outRefundNo = result.getReqInfo().getOutRefundNo();
        logger.info("### 收到退款通知#### outRefundNo={}", outRefundNo);

        String redisKey = RedisKeyUtils.REDIS_LOCK_REFUND_NOTIFY + outRefundNo;
        redisLock.lock(redisKey);
        try {
            AppWxpayBack tradeNoParamer = new AppWxpayBack();
            tradeNoParamer.setOutRefundNo(outRefundNo);
            AppWxpayBack appWxpayBack = appWxpayBackDao.getByEntity(tradeNoParamer);

            if (appWxpayBack == null) {
                logger.error(" 微信退款回调 #### 商户订单号 未找到对应的订单 result={}", result);
                return WxPayNotifyResponse.fail("商户订单号 未找到对应的订单");
            }
            if (appWxpayBack.getBackStatus() != null && appWxpayBack.getBackStatus().equals("SUCCESS")) {
                return WxPayNotifyResponse.success("");
            }

            WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();
            logger.info("### 退款结果 ### reqInfo={}", reqInfo.toString());
            appWxpayBack.setOutRefundNo(reqInfo.getOutRefundNo());
            appWxpayBack.setRefundId(reqInfo.getRefundId());
            appWxpayBack.setBackStatus(reqInfo.getRefundStatus());
            appWxpayBack.setTotalFee(reqInfo.getTotalFee());
            appWxpayBack.setRefundFee(reqInfo.getRefundFee());
            appWxpayBack.setSuccessTime(reqInfo.getSuccessTime());
            appWxpayBack.setRefundRecvAccout(reqInfo.getRefundRecvAccout());
            appWxpayBack.setRefundAccount(reqInfo.getRefundAccount());
            appWxpayBackDao.update(appWxpayBack);

            AppWxpayOrder paramer = new AppWxpayOrder();
            paramer.setOutTradeNo(appWxpayBack.getOutTradeNo());
            AppWxpayOrder appWxpayOrder = appWxpayOrderDao.getByEntity(paramer);

            if (reqInfo.getRefundStatus().equals("SUCCESS")) {
                logger.info("### appWxpayOrder={}", appWxpayOrder);
                //-----------------退款成功---------------------------------------------------------
                //处理plus会员逻辑 设置plus会员过期时间为当前
                if (appWxpayOrder.getType() == 1 && appWxpayOrder.getUserId() != null) {
                    AppUserPlus appUserPlusParamer = new AppUserPlus();
                    appUserPlusParamer.setUserId(appWxpayOrder.getUserId());
                    AppUserPlus appUserPlus = appUserPlusDao.getByEntity(appUserPlusParamer);
                    appUserPlus.setInvalidTimeEnd(new Date());
                    appUserPlusDao.update(appUserPlus);
                }

                //b端信息费充值、商户充值
                if (appWxpayOrder.getType() == 2 && appWxpayOrder.getUserId() != null) {
                    AppBUser appBUser = appBUserDao.selectFotUpdate(new Long(appWxpayOrder.getUserId()).intValue());
                    appBUser.setBalance(appBUser.getBalance().subtract(new BigDecimal(appWxpayOrder.getAmount())));
                    appBUserDao.update(appBUser);
                }
                //救援
                if (appWxpayOrder.getType() == 6) {
                    AppRescueIndent appRescueIndent = appRescueIndentDao.selectByOutTradeNo(appWxpayOrder.getOutTradeNo());
                    appRescueIndent.setBackState(3);//3=退款成功
                    appRescueIndent.setState(4);//4=取消
                    appRescueIndentDao.update(appRescueIndent);
                }

                //洗车
                if (appWxpayOrder.getType() == 4 && appWxpayOrder.getUserId() != null) {
                    AppCleanIndet appCleanIndet = appCleanIndetDao.selectByOutTradeNo(appWxpayOrder.getOutTradeNo());
                    if (appCleanIndet != null) {
                        if (appCleanIndet.getPayState().equals("1")) {
                            appCleanIndet.setPayState("3");
                            appCleanIndetDao.update(appCleanIndet);
                        } else {
                            logger.error("### 洗车订单 退款  订单状态不是已支付  appCleanIndet={}", appCleanIndet);
                        }
                    } else {
                        logger.error("## 洗车订单退款 根据支付单号未找到对应订单   outTradeNo={}", appWxpayOrder.getOutTradeNo());
                    }
                }
            } else {
                //退款失败
                logger.error("### 退款失败  结果  reqInfo={}", reqInfo);

                if (appWxpayOrder.getType() == 6) {
                    AppRescueIndent appRescueIndent = appRescueIndentDao.selectByOutTradeNo(appWxpayOrder.getOutTradeNo());
                    appRescueIndent.setBackState(4);//4=退款成功
                    appRescueIndentDao.update(appRescueIndent);
                }
            }
            //表示收到通知
            return WxPayNotifyResponse.success("");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        } finally {
            redisLock.unlock(redisKey);
        }
    }
}
