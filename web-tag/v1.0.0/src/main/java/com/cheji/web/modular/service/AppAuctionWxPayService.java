package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.*;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.AppWxCashOutRecordEntityMapper;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.AssertUtil;
import com.cheji.web.util.COrderNoUtil;
import com.cheji.web.util.IpUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 微信支付相关
 * </p>
 *
 * @author yang
 */
@Service
@Slf4j
public class AppAuctionWxPayService {

    @Resource
    private RedisLock redisLock;

    @Resource
    private WxPayService wxPayService;      //微信api

    @Resource
    private AppAuctionPayLogService appAuctionPayLogService;

    @Resource
    private AppAuctionBailRefundLogService bailRefundLogService;

    @Resource
    private AppAuctionAddressService addressService;

    @Resource
    private AppAuctionBailLogService appAuctionBailLogService;

    @Resource
    private AppAuctionVipLvService appAuctionVipLvService;

    @Resource
    private AppAuctionVipControlService appAuctionVipControlService;

    @Resource
    private AppAuctionTransactionLogService appAuctionTransactionLogService;

    @Resource
    private AppAuctionWithdrawCashService appAuctionWithdrawCashService;

    @Resource
    private AppAuctionService appAuctionService;

    @Resource
    private AppAuctionMessageIdentifyService messageIdentifyService;

    @Resource
    private UserService userService;

    @Value("${bail}")
    private String bail;       //保证金比例

    @Value("${host}")
    private String host;

    private final String body = "车己汽车-开通拍卖会员";

    //支付单车保证金
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject createBailOrder(JSONObject result, Long carId, HttpServletRequest httpServletRequest, TokenPojo currentLoginUser) {
        AppAuctionEntity auction = appAuctionService.selectById(carId);
        if (Objects.isNull(auction) || Objects.isNull(auction.getPrice()) || auction.getPrice().compareTo(new BigDecimal("0.00")) == -1) {
            result.put("code", 540);
            result.put("msg", "车辆信息错误!");
            return result;
        }

        BigDecimal payAmount = auction.getPrice().divide(new BigDecimal(100)).multiply(new BigDecimal(bail));

        return wxPay("车己汽车-单车保证金", httpServletRequest, payAmount, carId, result, currentLoginUser, null, "payLongNotify", AppAuctionWxPayConstant.WX_PAY_STATE_BAILLOG);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject createVipOrder(JSONObject result, String vipLv, HttpServletRequest httpServletRequest, TokenPojo currentLoginUser) {
        Integer id = currentLoginUser.getAppUserEntity().getId();
        UserEntity userEntity = userService.selectById(id);
        if (userEntity == null) {
            result.put("code", 540);
            result.put("msg", "用户信息错误!");
            return result;
        }
        //vipLv开通的vip等级
        if (!StringUtils.isNumeric(vipLv) || Integer.valueOf(vipLv) < AppAuctionConstant.ONE || Integer.valueOf(vipLv) > AppAuctionConstant.FOUR) {
            result.put("code", 411);
            result.put("msg", "vip参数错误");
            return result;
        }


        EntityWrapper<AppAuctionVipLvEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("lv", vipLv);
        AppAuctionVipLvEntity appAuctionVipLvEntity = appAuctionVipLvService.selectOne(wrapper);
        BigDecimal amount1 = appAuctionVipLvEntity.getAmount();
        if (amount1 == null || amount1.compareTo(new BigDecimal("0.00")) == -1) {
            result.put("code", 401);
            result.put("msg", "金额不能小于0");
            return result;
        }

        EntityWrapper<AppAuctionVipControlEntity> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("user_id", id).eq("state", AppAuctionConstant.ONE);
        AppAuctionVipControlEntity appAuctionVipControlEntity1 = appAuctionVipControlService.selectOne(wrapper1);
        if (Objects.nonNull(appAuctionVipControlEntity1) && Integer.valueOf(vipLv) <= Integer.valueOf(appAuctionVipControlEntity1.getVipLv())) {
            result.put("code", 411);
            result.put("msg", "vip等级参数错误!");
            return result;
        } else if (Objects.nonNull(appAuctionVipControlEntity1) && AppAuctionConstant.FOUR.toString().equals(appAuctionVipControlEntity1.getVipLv())) {
            result.put("code", 412);
            result.put("msg", "已是最高等级vip,无法升级!");
            return result;
        } else if (Objects.nonNull(appAuctionVipControlEntity1) && Integer.valueOf(vipLv) > Integer.valueOf(appAuctionVipControlEntity1.getVipLv())) {
            BigDecimal amount2 = appAuctionVipLvEntity.getAmount();//目标会员的金额
            AppAuctionVipLvEntity lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", appAuctionVipControlEntity1.getVipLv()));
            BigDecimal amount3 = lv.getAmount();//当前用户金额
            amount1 = amount2.subtract(amount3);
        }

        //微信支付
        return wxPay("车己汽车-开通会员", httpServletRequest, amount1, null, result, currentLoginUser, vipLv, "payLogNotify", AppAuctionWxPayConstant.WX_PAY_STATE_PAYLOG);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject wxPay(String payBody, HttpServletRequest httpServletRequest, BigDecimal amount, Long carId, JSONObject result, TokenPojo currentLoginUser, String vipLv, String url, String state) {
        //微信相关
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult;
        String uniqueOrder = COrderNoUtil.getUniqueOrder();
        try {
            wxPayUnifiedOrderRequest.setBody(payBody);
            wxPayUnifiedOrderRequest.setOutTradeNo(uniqueOrder);
            wxPayUnifiedOrderRequest.setTotalFee(amount.multiply(new BigDecimal("100.00")).intValue());
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "cServer/auction/wxPay/" + url);
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            log.info("#### createOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            log.info("#### createOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            if (AppAuctionWxPayConstant.WX_PAY_STATE_PAYLOG.equals(state)) {
                appAuctionPayLogService.insertLog(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), vipLv);
            } else if (AppAuctionWxPayConstant.WX_PAY_STATE_BAILLOG.equals(state)) {
                appAuctionBailLogService.insertLog(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), carId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 203);
            result.put("msg", "支付异常");
            return result;
        }
        JSONObject object = new JSONObject();
        object.put("wxPayAppOrderResult", wxPayAppOrderResult);
        object.put("outTradeNo", uniqueOrder);
        result.put("code", 200);
        result.put("data", object);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String wxpayNotify(HttpServletRequest request) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String orderId = result.getOutTradeNo();        // 获取商户订单号
        String tradeNo = result.getTransactionId();        //微信支付订单号
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());      // 获取订单金额
        log.info(" ###订单金额 totalFee={}", totalFee);

        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + orderId;
        redisLock.lock(redisKey);
        try {
            //判断金额
            AppAuctionPayLogEntity appAuctionPayLogEntity = appAuctionPayLogService.findByNo(orderId);
            if (appAuctionPayLogEntity == null) {
                log.error(" 微信支付回调 #### 商户订单号 未找到对应的订单 result={}", result);
                return WxPayNotifyResponse.fail("商户订单号 未找到对应的订单");
            }

            if (result.getResultCode().equals("SUCCESS")) {
                if ((appAuctionPayLogEntity.getAmount().divide(new BigDecimal(100))).compareTo(new BigDecimal(totalFee)) != 0) {
                    log.error(" 微信支付回调 #### 金额不对 result={}；appWxpayOrderEntity.getAmount()={}；totalFee={}", result, appAuctionPayLogEntity.getAmount(), totalFee);
                    return WxPayNotifyResponse.fail("金额不对");
                }

                if (appAuctionPayLogEntity.getStatus() == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {
                    Integer userId = appAuctionPayLogEntity.getUserId();
                    AppAuctionTransactionLogEntity transationLog = appAuctionTransactionLogService.selectOne(new EntityWrapper<AppAuctionTransactionLogEntity>().eq("order_id", appAuctionPayLogEntity.getOutTradeNo()));
                    if (Objects.isNull(transationLog)) {
                        //记录交易
                        AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
                        appAuctionTransactionLogEntity.setUserId(Long.valueOf(userId));
                        appAuctionTransactionLogEntity.setCreateTime(new Date());
                        appAuctionTransactionLogEntity.setAmount(appAuctionPayLogEntity.getAmount());
                        appAuctionTransactionLogEntity.setDesc("VIP签约保证金");
                        appAuctionTransactionLogEntity.setType(TransactionConstant.TRANSACTION_CONSTANT_RECHARGE);
                        appAuctionTransactionLogEntity.setOrderState(AppAuctionConstant.ONE);
                        appAuctionTransactionLogEntity.setOrderId(orderId);
                        appAuctionTransactionLogEntity.setPayType(AppAuctionConstant.ONE);
                        appAuctionTransactionLogService.insert(appAuctionTransactionLogEntity);
                    }

                    //开通会员
                    String vip = userService.findVip(Long.valueOf(userId));
                    AppAuctionVipControlEntity vipCon = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", userId).eq("state", AppAuctionConstant.ONE));
                    //查询需要充值的lv相关数据
                    EntityWrapper<AppAuctionVipLvEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("lv", appAuctionPayLogEntity.getVipLv());
                    AppAuctionVipLvEntity appAuctionVipLvEntity = appAuctionVipLvService.selectOne(wrapper);
                    if (vip == null && vipCon == null) {
                        log.info("回调开通plus会员");
                        //验证金额
                        BigDecimal amount1 = appAuctionVipLvEntity.getAmount();
                        if ((appAuctionPayLogEntity.getAmount().divide(new BigDecimal(100))).compareTo(amount1) != 0) {
                            return WxPayNotifyResponse.fail("金额不对");
                        }
                        AppAuctionVipControlEntity appAuctionVipControl = newVipCon2DTO(appAuctionPayLogEntity);
                        if (appAuctionVipControl != null) {
                            appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
                            log.info("设置回调开通会员成功!充值金额:{}", appAuctionPayLogEntity.getAmount());
                            boolean b = appAuctionPayLogService.update(appAuctionPayLogEntity, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", appAuctionPayLogEntity.getOutTradeNo()));
                            if (!b) {
                                log.error("修改支付记录失败!");
                                throw new RuntimeException("修改支付记录失败");
                            }
                            userService.openVip(userId, appAuctionPayLogEntity.getVipLv());
                            appAuctionVipControlService.insert(appAuctionVipControl);
                        } else {
                            throw new RuntimeException("生成会员失败");
                        }
                    } else if (vip != null && vip.equals(vipCon.getVipLv()) && Integer.valueOf(appAuctionPayLogEntity.getVipLv()) > Integer.valueOf(vip)) {
                        //升级vip
                        log.info("回调升级vip会员");
                        //验证金额
                        BigDecimal amount1 = appAuctionVipLvEntity.getAmount();

                        if (amount1.subtract(vipCon.getAmount().divide(new BigDecimal(100))).compareTo(new BigDecimal(totalFee)) != 0) {
                            log.info("回调升级vip金额不对");
                            return WxPayNotifyResponse.fail("金额不对");
                        }
                        appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
                        log.info("设置回调升级会员成功!充值金额:", appAuctionPayLogEntity.getAmount());
                        boolean b = appAuctionPayLogService.update(appAuctionPayLogEntity, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", appAuctionPayLogEntity.getOutTradeNo()));
                        AssertUtil.isTrue(b, "修改支付记录失败");

                        userService.openVip(userId, appAuctionPayLogEntity.getVipLv());
                        AssertUtil.isNotNull(vipCon, "未查询到vip信息");

                        vipCon.setVipLv(appAuctionPayLogEntity.getVipLv());
                        BigDecimal totalAmount = vipCon.getAmount().add(new BigDecimal(totalFee).multiply(new BigDecimal("100.00")));
                        vipCon.setAmount(totalAmount);

                        Integer timeOut = appAuctionVipLvEntity.getTimeOut();        //订单的vip循环台次
                        Integer lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", vip)).getTimeOut();//当前vip等级的循环台次
                        if ((timeOut - lv) > AppAuctionVipSet.VIP_COMMON_ZERO) {
                            vipCon.setCarCount(vipCon.getCarCount() + (timeOut - lv));
                        } else {
                            new RuntimeException("台次数据异常");
                        }
                        vipCon.setUpdateTime(new Date());
                        String vipLv = appAuctionPayLogEntity.getVipLv();
                        if (vipLv.equals(AppAuctionConstant.TWO.toString())) {
                            vipCon.setOffer(AppAuctionVipSet.VIP_TWO_OFFER);
                        } else if (vipLv.equals(AppAuctionConstant.THREE.toString())) {
                            vipCon.setOffer(AppAuctionVipSet.VIP_THREE_OFFER);
                        } else if (vipLv.equals(AppAuctionConstant.FOUR.toString())) {
                            vipCon.setOffer(AppAuctionVipSet.VIP_FOUR_OFFER);
                        }
                        appAuctionVipControlService.updateById(vipCon);
                        log.info("回调升级vip成功,总金额:", vipCon.getAmount());
                    } else {
                        log.info("vip等级异常!");
                    }

                } else {
                    //重复通知 不需要做处理
                    log.info("### 微信支付回调  重复通知  不需要做处理## result={}", result);
                }
            } else {
                //支付失败
                log.info("支付失败");
                appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_FAIL.getCode());
                appAuctionPayLogService.update(appAuctionPayLogEntity, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", appAuctionPayLogEntity.getOutTradeNo()));

            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        } finally {
            log.info("##解回调锁###");
            redisLock.unlock(redisKey);
        }
    }

    private AppAuctionVipControlEntity newVipCon2DTO(AppAuctionPayLogEntity wxPayLog) {
        if (wxPayLog == null) {
            return null;
        }
        AppAuctionVipControlEntity appAuctionVipControlEntity = new AppAuctionVipControlEntity();
        if (wxPayLog.getVipLv() != null && StringUtils.isNumeric(wxPayLog.getVipLv())) {
            appAuctionVipControlEntity.setVipLv(wxPayLog.getVipLv());
            appAuctionVipControlEntity.setAmount(wxPayLog.getAmount());
            appAuctionVipControlEntity.setState(AppAuctionVipSet.VIP_STATE_USE);
            appAuctionVipControlEntity.setUserId(Long.valueOf(wxPayLog.getUserId()));
            appAuctionVipControlEntity.setFreezeCount(AppAuctionVipSet.VIP_COMMON_ZERO);
            appAuctionVipControlEntity.setCreateTime(new Date());
            //设置随机vipid
            Random random = new Random();
            String vipid = "cj" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000);
            appAuctionVipControlEntity.setVipId(vipid);

            if (wxPayLog.getVipLv().equals(AppAuctionConstant.ONE.toString())) {
                appAuctionVipControlEntity.setCarCount(AppAuctionVipSet.VIP_ONE_CARCOUNT);
                appAuctionVipControlEntity.setOffer(AppAuctionVipSet.VIP_ONE_OFFER);
            } else if (wxPayLog.getVipLv().equals(AppAuctionConstant.TWO.toString())) {
                appAuctionVipControlEntity.setCarCount(AppAuctionVipSet.VIP_TWO_CARCOUNT);
                appAuctionVipControlEntity.setOffer(AppAuctionVipSet.VIP_TWO_OFFER);
            } else if (wxPayLog.getVipLv().equals(AppAuctionConstant.THREE.toString())) {
                appAuctionVipControlEntity.setCarCount(AppAuctionVipSet.VIP_THREE_CARCOUNT);
                appAuctionVipControlEntity.setOffer(AppAuctionVipSet.VIP_THREE_OFFER);
            } else if (wxPayLog.getVipLv().equals(AppAuctionConstant.FOUR.toString())) {
                appAuctionVipControlEntity.setCarCount(AppAuctionVipSet.VIP_FOUR_CARCOUNT);
                appAuctionVipControlEntity.setOffer(AppAuctionVipSet.VIP_FOUR_OFFER);
            }
            return appAuctionVipControlEntity;
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject queryOrderStatus(JSONObject result, String outTradeNo) {
        if (StringUtils.isEmpty(outTradeNo)) {
            result.put("code", 401);
            result.put("msg", "入参错误");
            return result;
        }

        //加个redis锁 防止回调通知和主动查询同时为用户开通plus会员
        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + outTradeNo;
        redisLock.lock(redisKey);
        try {
            AppAuctionPayLogEntity appAuctionPayLogEntity = appAuctionPayLogService.selectOne(new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", outTradeNo));
            log.info("###  支付查询 appWxpayOrderEntity={}", appAuctionPayLogEntity);
            if (appAuctionPayLogEntity == null) {
                result.put("code", 402);
                result.put("msg", "根据订单号没有找到对应订单！");
                return result;
            }

            Integer status = appAuctionPayLogEntity.getStatus();
            if (status == ConsEnum.WX_PAY_ORDER_FAIL.getCode()) {
                log.info("支付失败,返回");
                result.put("code", 403);
                result.put("msg", "支付失败！");
                return result;
            }

            if (status == ConsEnum.WX_PAY_ORDER_SUCCESS.getCode()) {
                log.info("支付成功,返回");
                result.put("code", 200);
                result.put("msg", "支付成功！");
                return result;
            }

            //支付回调通知还没收到结果，需要主动查询
            WxPayOrderQueryResult wxPayOrderQueryResult = null;
            try {
                wxPayOrderQueryResult = this.wxPayService.queryOrder("", appAuctionPayLogEntity.getOutTradeNo());
            } catch (WxPayException e) {
                e.printStackTrace();
            }
            log.info("###  支付查询 wxPayOrderQueryResult={}", wxPayOrderQueryResult);
            log.info("wxPayOrderQueryResult.getTradeState()={}", wxPayOrderQueryResult.getTradeState());
            log.info("wxPayOrderQueryResult   true or  false={}", wxPayOrderQueryResult.getTradeState().equals("SUCCESS"));

            if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
                Integer userId = appAuctionPayLogEntity.getUserId();
                String vip = userService.findVip(Long.valueOf(userId));
                AppAuctionVipControlEntity vipCon = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", userId).eq("state", AppAuctionConstant.ONE));
                log.info("## 当前用户vip等级为：id={};vip={};vipCon={}", userId, vip, vipCon);
                AppAuctionPayLogEntity byNo = appAuctionPayLogService.findByNo(outTradeNo);
                if (byNo.getStatus().equals(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode())) {
                    AppAuctionTransactionLogEntity transationLog = appAuctionTransactionLogService.selectOne(new EntityWrapper<AppAuctionTransactionLogEntity>().eq("order_id", byNo.getOutTradeNo()));
                    if (Objects.isNull(transationLog)) {
                        AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
                        appAuctionTransactionLogEntity.setUserId(Long.valueOf(userId));
                        appAuctionTransactionLogEntity.setCreateTime(new Date());
                        appAuctionTransactionLogEntity.setAmount(appAuctionPayLogEntity.getAmount());
                        appAuctionTransactionLogEntity.setDesc("签约保证金");
                        appAuctionTransactionLogEntity.setType(TransactionConstant.TRANSACTION_CONSTANT_RECHARGE);
                        appAuctionTransactionLogEntity.setOrderState(AppAuctionWxPayConstant.ORDERSTATE_DEFAULT);
                        appAuctionTransactionLogEntity.setOrderId(outTradeNo);
                        appAuctionTransactionLogEntity.setPayType(AppAuctionWxPayConstant.WX_PAY);
                        appAuctionTransactionLogService.insert(appAuctionTransactionLogEntity);
                    }

                    EntityWrapper<AppAuctionVipLvEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("lv", byNo.getVipLv());
                    AppAuctionVipLvEntity appAuctionVipLvEntity = appAuctionVipLvService.selectOne(wrapper);

                    if ((vip == null || "0".equals(vip)) && vipCon == null) {
                        log.info("查询开通auction会员");
                        //修改支付状态为支付成功
                        appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
                        appAuctionPayLogService.update(appAuctionPayLogEntity, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", outTradeNo));
                        //设置app_user表中vip等级
                        userService.openVip(userId, appAuctionPayLogEntity.getVipLv());

                        AppAuctionVipControlEntity appAuctionVipControl = newVipCon2DTO(appAuctionPayLogEntity);
                        if (appAuctionVipControl != null) {
                            appAuctionVipControlService.insert(appAuctionVipControl);
                        } else {
                            throw new RuntimeException("生成会员错误!");
                        }

                        result.put("code", 200);
                        result.put("msg", "支付成功,成功开通会员!");
                        return result;
                    } else if (vip != null && vip.equals(vipCon.getVipLv()) && Integer.valueOf(byNo.getVipLv()) > Integer.valueOf(vip)) {
                        log.info("查询升级vip会员");

                        byNo.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
                        boolean b = appAuctionPayLogService.update(byNo, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", byNo.getOutTradeNo()));
                        if (!b) {
                            log.info("修改支付记录失败!");
                            throw new RuntimeException("修改支付记录失败");
                        }
                        userService.openVip(userId, byNo.getVipLv());
                        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", byNo.getUserId()).eq("state", AppAuctionConstant.ONE));
                        //修改vip参数
                        if (appAuctionVipControl != null) {
                            appAuctionVipControl.setVipLv(byNo.getVipLv());
                            appAuctionVipControl.setAmount(appAuctionVipControl.getAmount().add(byNo.getAmount()));
                            appAuctionVipControl.setCarCount(appAuctionVipControl.getCarCount() + Integer.valueOf(appAuctionVipLvEntity.getTimeOut()));
                            appAuctionVipControl.setUpdateTime(new Date());
                            appAuctionVipControlService.updateById(appAuctionVipControl);
                            log.info("查询升级vip成功!金额：", appAuctionVipControl.getAmount());
                        } else {
                            log.info("未查询到vipControl信息");
                            throw new RuntimeException("未查询到vipControl信息");
                        }
                        result.put("code", 200);
                        result.put("msg", "升级vip成功!");
                        return result;
                    } else {
                        result.put("code", 200);
                        result.put("msg", "已支付,请勿重复开通会员!");
                        return result;
                    }
                }


            }

            if (wxPayOrderQueryResult.getTradeState().equals("USERPAYING")) {
                result.put("code", 201);
                result.put("msg", "用户支付中！");
                return result;
            }
            //支付失败
            appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_FAIL.getCode());
            appAuctionPayLogService.update(appAuctionPayLogEntity, new EntityWrapper<AppAuctionPayLogEntity>().eq("out_trade_no", outTradeNo));
            log.info("查询支付失败!");
            result.put("code", 403);
            result.put("msg", "支付失败！");
        } finally {
            log.info("##解查询锁###");
            redisLock.unlock(redisKey);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject queryBailOrderStatus(JSONObject result, String outTradeNo) {
        if (StringUtils.isEmpty(outTradeNo)) {
            result.put("code", 401);
            result.put("msg", "入参错误");
            return result;
        }

        //加个redis锁 防止回调通知和主动查询同时
        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + outTradeNo;
        redisLock.lock(redisKey);
        try {
            AppAuctionBailLogEntity appAuctionBailLogEntity = appAuctionBailLogService.selectOne(new EntityWrapper<AppAuctionBailLogEntity>().eq("out_trade_no", outTradeNo));
            log.info("###  支付查询 appWxpayOrderEntity={}", appAuctionBailLogEntity);
            if (appAuctionBailLogEntity == null) {
                result.put("code", 402);
                result.put("msg", "根据订单号没有找到对应订单！");
                return result;
            }

            Integer status = appAuctionBailLogEntity.getStatus();
            if (status == ConsEnum.WX_PAY_ORDER_FAIL.getCode()) {
                log.info("支付失败,返回");
                result.put("code", 403);
                result.put("msg", "支付失败！");
                return result;
            }

            if (status == ConsEnum.WX_PAY_ORDER_SUCCESS.getCode()) {
                log.info("支付成功,返回");
                result.put("code", 200);
                result.put("msg", "支付成功！");
                return result;
            }

            //支付回调通知还没收到结果，需要主动查询
            WxPayOrderQueryResult wxPayOrderQueryResult = null;
            try {
                wxPayOrderQueryResult = this.wxPayService.queryOrder("", appAuctionBailLogEntity.getOutTradeNo());
            } catch (WxPayException e) {
                e.printStackTrace();
            }
            log.info("###  支付查询 wxPayOrderQueryResult={}", wxPayOrderQueryResult);
            log.info("wxPayOrderQueryResult.getTradeState()={}", wxPayOrderQueryResult.getTradeState());
            log.info("wxPayOrderQueryResult   true or  false={}", wxPayOrderQueryResult.getTradeState().equals("SUCCESS"));

            if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
                //单车拍卖保证金支付查询
                AppAuctionBailLogEntity byNo = appAuctionBailLogService.findByNo(outTradeNo);
                if (byNo.getStatus().equals(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode())) {
                    return payNotify2DTO(result, byNo, outTradeNo);
                } else {
                    result.put("code", 200);
                    result.put("msg", "已支付,请勿重复支付!");
                    return result;
                }
            }
            if (wxPayOrderQueryResult.getTradeState().equals("USERPAYING")) {
                result.put("code", 201);
                result.put("msg", "用户支付中！");
                return result;
            }
            //支付失败
            appAuctionBailLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_FAIL.getCode());
            appAuctionBailLogService.update(appAuctionBailLogEntity, new EntityWrapper<AppAuctionBailLogEntity>().eq("out_trade_no", outTradeNo));
            log.info("查询支付失败!");
            result.put("code", 403);
            result.put("msg", "支付失败！");
        } finally {
            log.info("##解查询锁###");
            redisLock.unlock(redisKey);
        }
        return result;
    }

    public JSONObject getPayAmount(JSONObject result, String vipLv, Integer userId) {
        EntityWrapper<AppAuctionVipLvEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("lv", vipLv);
        AppAuctionVipLvEntity appAuctionVipLvEntity = appAuctionVipLvService.selectOne(wrapper);
        BigDecimal amount1 = appAuctionVipLvEntity.getAmount();

        EntityWrapper<AppAuctionVipControlEntity> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("user_id", userId).eq("state", AppAuctionConstant.ONE);
        AppAuctionVipControlEntity appAuctionVipControlEntity1 = appAuctionVipControlService.selectOne(wrapper1);

        AppAuctionWithdrawCashEntity appAuctionWithdrawCashEntity = appAuctionWithdrawCashService.selectOne(new EntityWrapper<AppAuctionWithdrawCashEntity>().eq("user_id", userId).eq("state", AppAuctionConstant.ZERO));
        if (Objects.nonNull(appAuctionWithdrawCashEntity)) {
            result.put("code", 422);
            result.put("msg", "提现中,请勿开通vip!");
            return result;
        }

        //升级会员
        if (Objects.nonNull(appAuctionVipControlEntity1) && Integer.valueOf(vipLv) > Integer.valueOf(appAuctionVipControlEntity1.getVipLv())) {
            BigDecimal amount2 = appAuctionVipLvEntity.getAmount();//目标会员的金额
            BigDecimal amount3 = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", appAuctionVipControlEntity1.getVipLv())).getAmount();
            amount1 = amount2.subtract(amount3);
            result.put("code", 200);
            result.put("msg", "查询成功！");
            result.put("data", amount1);
            return result;
        }

        if (Objects.isNull(appAuctionVipControlEntity1)) {
            result.put("code", 200);
            result.put("msg", "查询成功！");
            result.put("data", amount1);
            return result;
        }

        result.put("code", 202);
        result.put("msg", "查询错误！");
        return result;
    }

    public JSONObject getInfo(Long carId, Integer id) {
        JSONObject result = new JSONObject();
        AppAuctionEntity auction = appAuctionService.selectById(carId);
        AppAuctionMessageIdentifyEntity identify = messageIdentifyService.selectOne(new EntityWrapper<AppAuctionMessageIdentifyEntity>().eq("user_id", id));
        AppAuctionAddressEntity address = addressService.selectOne(new EntityWrapper<AppAuctionAddressEntity>().eq("user_id", id).eq("is_default", AppAuctionConstant.IS_DEFAULT_ADDRESS));
        JSONObject object = new JSONObject();

        object.put("identify", identify);
        object.put("address", address);

        if (Objects.nonNull(auction) && Objects.nonNull(auction.getPrice())) {
            object.put("bail", auction.getPrice().divide(new BigDecimal(100)).multiply(new BigDecimal(bail)));

            result.put("code", 200);
            result.put("msg", "查询成功！");
            result.put("data", object);
        } else {
            result.put("code", 202);
            result.put("msg", "查询错误！");
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String wxpayBaiNotify(HttpServletRequest request) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String orderId = result.getOutTradeNo();        // 获取商户订单号
        String tradeNo = result.getTransactionId();        //微信支付订单号
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());      // 获取订单金额
        log.info(" ###订单金额 totalFee={}", totalFee);

        String redisKey = RedisConstant.PLUS_MEMBER_LOCK + orderId;
        redisLock.lock(redisKey);
        try {
            //判断金额
            AppAuctionBailLogEntity byNo = appAuctionBailLogService.findByNo(orderId);
            if (byNo == null) {
                log.error(" 微信支付回调 #### 商户订单号 未找到对应的订单 result={}", result);
                return WxPayNotifyResponse.fail("商户订单号 未找到对应的订单");
            }

            if (result.getResultCode().equals("SUCCESS")) {
                if ((byNo.getAmount().divide(new BigDecimal(100))).compareTo(new BigDecimal(totalFee)) != 0) {
                    log.error(" 微信支付回调 #### 金额不对 result={}；appWxpayOrderEntity.getAmount()={}；totalFee={}", result, byNo.getAmount(), totalFee);
                    return WxPayNotifyResponse.fail("金额不对");
                }

                if (byNo.getStatus() == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {
                    JSONObject obj = new JSONObject();
                    payNotify2DTO(obj, byNo, orderId);

                } else {
                    //重复通知 不需要做处理
                    log.info("### 微信支付回调  重复通知  不需要做处理## result={}", result);
                }
            } else {
                //支付失败
                log.info("支付失败");
                byNo.setStatus(ConsEnum.WX_PAY_ORDER_FAIL.getCode());
                appAuctionBailLogService.update(byNo, new EntityWrapper<AppAuctionBailLogEntity>().eq("out_trade_no", byNo.getOutTradeNo()));

            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        } finally {
            log.info("##解回调锁###");
            redisLock.unlock(redisKey);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void refundLog2DTO(String outTradeNo, Long carId, Integer userId, String prepayId, BigDecimal amount) {
        AppAuctionBailRefundLogEntity appAuctionBailRefundLogEntity = new AppAuctionBailRefundLogEntity();
        appAuctionBailRefundLogEntity.setCarId(carId);
        appAuctionBailRefundLogEntity.setUserId(userId);
        appAuctionBailRefundLogEntity.setAmount(amount);
        appAuctionBailRefundLogEntity.setOutRefundNo(COrderNoUtil.getRefundOrder());
        appAuctionBailRefundLogEntity.setOutTradeNo(outTradeNo);
        appAuctionBailRefundLogEntity.setPrepayId(prepayId);
        appAuctionBailRefundLogEntity.setState(AppAuctionBailConstant.REFUND_BAIL_INITIAL);
        appAuctionBailRefundLogEntity.setCreateTime(new Date());
        bailRefundLogService.insert(appAuctionBailRefundLogEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject payNotify2DTO(JSONObject result, AppAuctionBailLogEntity byNo, String outTradeNo) {
        AppAuctionTransactionLogEntity transationLog = appAuctionTransactionLogService.selectOne(new EntityWrapper<AppAuctionTransactionLogEntity>().eq("order_id", byNo.getOutTradeNo()));
        if (Objects.isNull(transationLog)) {
            AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
            appAuctionTransactionLogEntity.setUserId(Long.valueOf(byNo.getUserId()));
            appAuctionTransactionLogEntity.setCreateTime(new Date());
            appAuctionTransactionLogEntity.setAmount(byNo.getAmount());
            appAuctionTransactionLogEntity.setDesc("单车保证金充值（自动退款）");
            appAuctionTransactionLogEntity.setType(TransactionConstant.TRANSACTION_CONSTANT_RECHARGE);
            appAuctionTransactionLogEntity.setOrderState(AppAuctionWxPayConstant.ORDERSTATE_DEFAULT);
            appAuctionTransactionLogEntity.setOrderId(outTradeNo);
            appAuctionTransactionLogEntity.setPayType(AppAuctionWxPayConstant.WX_PAY);
            appAuctionTransactionLogEntity.setCarId(byNo.getCarId());
            appAuctionTransactionLogService.insert(appAuctionTransactionLogEntity);
        }
        //录入预退款单
        AppAuctionBailRefundLogEntity bailRefund = bailRefundLogService.selectOne(new EntityWrapper<AppAuctionBailRefundLogEntity>().eq("out_trade_no", byNo.getOutTradeNo()));
        if (Objects.isNull(bailRefund)) {
            refundLog2DTO(byNo.getOutTradeNo(), byNo.getCarId(), byNo.getUserId(), byNo.getPrepayId(), byNo.getAmount());
        }

        byNo.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        boolean b = appAuctionBailLogService.update(byNo, new EntityWrapper<AppAuctionBailLogEntity>().eq("out_trade_no", byNo.getOutTradeNo()));
        if (!b) {
            log.error("##修改支付记录失败!");
            throw new RuntimeException("修改支付记录失败");
        }
        log.info("##查询录入成功!");
        result.put("code", 200);
        result.put("msg", "缴费成功!");
        return result;
    }
}
