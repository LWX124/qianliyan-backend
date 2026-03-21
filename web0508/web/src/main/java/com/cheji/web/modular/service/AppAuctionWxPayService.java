package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.*;
import com.cheji.web.modular.controller.IndexController;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 微信支付相关
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionWxPayService {

    private Logger log = LoggerFactory.getLogger(AppAuctionWxPayService.class);

    @Resource
    private RedisLock redisLock;

    @Resource
    private WxPayService wxPayService;      //微信api

    @Resource
    private AppAuctionBidMapper appAuctionBidMapper;

    @Resource
    private AppAuctionVipControlMapper appAuctionVipControlMapper;

    @Resource
    private AppAuctionVipLvMapper appAuctionVipLvMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppAuctionMapper appAuctionMapper;

    @Resource
    private AppAuctionPayLogService appAuctionPayLogService;

    @Resource
    private AppAuctionBailRefundLogService bailRefundLogService;

    @Resource
    private AppAuctionAddressService addressService;

    @Resource
    private AppAuctionBailLogService appAuctionBailLogService;

    @Resource
    private AppAuctionOnePriceService appAuctionOnePriceService;

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
    private AppAuctionOnePriceCarLogMapper appAuctionOnePriceCarLogMapper;
    @Resource
    private AppAuctionUpMapper appAuctionUpMapper;
    @Resource
    private AppAuctionVipControlMapper appAuctionVipControlMapperMapper;
    @Resource
    private AppAuctionOrderMapper appAuctionOrderMapper;

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

    /**
     * 创建一口价车辆订单
     * 会员买车直接下订单，非会员买车需要支付保证金
     *
     * @param currentLoginUser
     * @param carId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject createOnePriceOrder(TokenPojo currentLoginUser, Long carId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        String redisKey = RedisConstant.CREATE_ONE_PRICE_CAR_ORDER + carId;
        redisLock.lock(redisKey);
        try {

            AppAuctionEntity appAuction = appAuctionMapper.selectById(carId);
            if (appAuction == null) {
                result.put("code", 401);
                result.put("msg", "车辆id错误");
                return result;
            }

            if (!"1".equals(appAuction.getFixedPrice())) {//是否是一口价,0否,1是
                result.put("code", 401);
                result.put("msg", "不是一口价车辆");
                return result;
            }

            if (appAuction.getCarState() != 7) {
                result.put("code", 403);
                result.put("msg", "车辆状态不对");
                return result;
            }

            //查看当前订单是否被锁定（由于部分用户支付需要时间，如果超过5分钟还没支付完成，就解锁。防止非会员和会员之间同时购买车辆发生冲突）
            String onePriceLock = stringRedisTemplate.opsForValue().get(RedisConstant.ONE_PRICE_CAR_LOCK + appAuction.getId());
            if (StringUtils.isNotBlank(onePriceLock)) {
                result.put("code", 300);
                result.put("msg", "当前车辆被锁定");
                return result;
            }

            UserEntity appUserEntity = userService.selectById(currentLoginUser.getAppUserEntity().getId());

            String authentication = appUserEntity.getAuthentication();
            if (Objects.isNull(authentication)) {
                result.put("code", 202);
                result.put("msg", "请先认证身份!");
                return result;
            }


            //如果是vip客户，那么可以不用付钱，直接创建订单
            AppAuctionVipControlEntity appAuctionVipControlEntity = appAuctionVipControlMapper.selectByUserId(currentLoginUser.getAppUserEntity().getId());
            if (appAuctionVipControlEntity != null) {
                //vip客户判断参拍台次后直接创建订单
                String vipLv = appAuctionVipControlEntity.getVipLv();
                AppAuctionVipLvEntity appAuctionVipLv = appAuctionVipLvMapper.selectByVip(vipLv);
                Integer timeOut = appAuctionVipLv.getTimeOut();//当前vip限制的参拍台次
                //查找已使用免费一口价权限的数量
                Integer onePriceOrderCount = appAuctionOnePriceCarLogMapper.selectCountByUserId(currentLoginUser.getAppUserEntity().getId());
                //查找当前用户拍卖中的车子 出价数量
                Integer countNum = appAuctionBidMapper.countByUserId(currentLoginUser.getAppUserEntity().getId());
                log.info("### 查找已使用免费一口价权限的数量={};查找当前用户拍卖中的车子={}；当前vip限制的参拍台次={};userId={}", onePriceOrderCount, countNum, timeOut, currentLoginUser.getAppUserEntity().getId());
                if (timeOut <= onePriceOrderCount + countNum) {
                    result.put("code", "301");
                    result.put("msg", "当前vip等级只能参拍" + timeOut + "次,已使用一口价权益" + onePriceOrderCount + "次，拍卖台次权益" + countNum + "次，请升级VIP！");
                    return result;
                }

                //用户没有超过限制，为vip用户创建不用支付的支付单
                AppAuctionOnePriceCarLogEntity appAuctionOnePriceCarLogEntity = new AppAuctionOnePriceCarLogEntity();
                appAuctionOnePriceCarLogEntity.setOutTradeNo(COrderNoUtil.getOnePriceUniqueOrder());
                appAuctionOnePriceCarLogEntity.setCarId(appAuction.getId());
                appAuctionOnePriceCarLogEntity.setAmount(0);
                appAuctionOnePriceCarLogEntity.setVipLv(Integer.valueOf(vipLv));
                appAuctionOnePriceCarLogEntity.setCreateTime(new Date());
                appAuctionOnePriceCarLogEntity.setUserId(currentLoginUser.getAppUserEntity().getId());
                appAuctionOnePriceCarLogEntity.setStatus(4);//支付状态：1 初始状态  2 支付成功  3 支付失败 4 不用支付
                appAuctionOnePriceCarLogEntity.setUpdateTime(new Date());
                appAuctionOnePriceCarLogMapper.insert(appAuctionOnePriceCarLogEntity);

                AppAuctionUpEntity appAuctionUpParamer = new AppAuctionUpEntity();
                appAuctionUpParamer.setCarId(appAuction.getId());
                AppAuctionUpEntity appAuctionUpEntity = appAuctionUpMapper.selectOne(appAuctionUpParamer);

                //创建订单信息
                AppAuctionOrderEntity appAuctionOrderEntity = new AppAuctionOrderEntity();
                appAuctionOrderEntity.setUserId(currentLoginUser.getAppUserEntity().getId().longValue());
                appAuctionOrderEntity.setCreateTime(new Date());
                appAuctionOrderEntity.setUpdateTime(new Date());
                appAuctionOrderEntity.setCarId(appAuction.getId());
                appAuctionOrderEntity.setOrderNo(appAuctionOnePriceCarLogEntity.getOutTradeNo());
                appAuctionOrderEntity.setOrderAmount(new BigDecimal(0));
                BigDecimal serviceAmt = appAuction.getPrice().multiply(new BigDecimal(appAuctionUpEntity.getServiceFee())).divide(new BigDecimal(100));
                //根据vip等级计算最终服务费
                if ("3".equals(vipLv)) {
                    serviceAmt = serviceAmt.multiply(new BigDecimal(0.8));
                } else if ("4".equals(vipLv)) {
                    serviceAmt = serviceAmt.multiply(new BigDecimal(0.5));
                }
                appAuctionOrderEntity.setServiceFee(serviceAmt);
                appAuctionOrderEntity.setDesc("vip用户创建的一口价车辆订单！");
                appAuctionOrderEntity.setState(0);//订单状态,0交易中,1交易完成
                appAuctionOrderMapper.insert(appAuctionOrderEntity);

                appAuction.setCarState(8);//拍卖完成
                appAuction.setUpdateTime(new Date());//拍卖完成
                appAuctionMapper.updateById(appAuction);

                //增加一条出价记录
                AppAuctionBidEntity appAuctionBidEntity = new AppAuctionBidEntity();
                appAuctionBidEntity.setBid(appAuction.getPrice());
                appAuctionBidEntity.setUserId(currentLoginUser.getAppUserEntity().getId().longValue());
                appAuctionBidEntity.setCarId(appAuction.getId().longValue());
                appAuctionBidEntity.setUpdateTime(new Date());
                appAuctionBidEntity.setCreateTime(new Date());
                appAuctionBidEntity.setValid(1);
                appAuctionBidMapper.insert(appAuctionBidEntity);

                result.put("code", 201);
                result.put("msg", "当前订单创建成功，vip免支付！");
                return result;
            }


            BigDecimal price = appAuction.getPrice();
            log.info("### 创建一口价车辆订单 金额1={}", price);
            //非vip会员需要支付保证金
            //五万以上  百分之5
            //五万以下  百分之10
//            -1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
            if (price.compareTo(new BigDecimal(50000)) > 0) {
                //大于5万
                price = price.divide(new BigDecimal(20), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                price = price.divide(new BigDecimal(10), 2, BigDecimal.ROUND_HALF_UP);
            }
            log.info("### 创建一口价车辆订单 金额2={}", price);

            result = wxPay("车己汽车-一口价车辆订金", httpServletRequest, price, appAuction.getId(), result, currentLoginUser, null, "onePriceNotify", AppAuctionWxPayConstant.WX_PAY_STATE_ONE_PRICE);
            //锁定5分钟，用户必须5分钟内支付完成，否则可以让别人支付
            stringRedisTemplate.opsForValue().set(RedisConstant.ONE_PRICE_CAR_LOCK + appAuction.getId(), "1", 60 * 5, TimeUnit.SECONDS);
        } finally {
            redisLock.unlock(redisKey);
        }

        return result;
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
        BigDecimal payAmount = appAuctionVipLvEntity.getAmount();
        if (payAmount == null || payAmount.compareTo(new BigDecimal("0.00")) == -1) {
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
            if (userEntity.getIsInner() == 1) {
                //内部工作人员 升级vip 使用内部价格
                BigDecimal targetAmount = appAuctionVipLvEntity.getInnerAmount();//目标会员的金额
                AppAuctionVipLvEntity lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", appAuctionVipControlEntity1.getVipLv()));
                BigDecimal currentAmount = lv.getInnerAmount();//当前用户金额
                payAmount = targetAmount.subtract(currentAmount);
                log.info("### 111 车己汽车-开通会员 内部工作人员开通vip appAuctionVipLvEntity={};", appAuctionVipLvEntity);
            } else {
                //正常用户
                BigDecimal targetAmount = appAuctionVipLvEntity.getAmount();//目标会员的金额
                AppAuctionVipLvEntity lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", appAuctionVipControlEntity1.getVipLv()));
                BigDecimal currentAmount = lv.getAmount();//当前用户金额
                payAmount = targetAmount.subtract(currentAmount);
                log.info("### 111 车己汽车-开通会员 普通用户开通vip appAuctionVipLvEntity={};", appAuctionVipLvEntity);
            }
        } else {
            if (userEntity.getIsInner() == 1) {
                payAmount = appAuctionVipLvEntity.getInnerAmount();
            }
        }
        log.info("### 111 车己汽车-开通会员 当前用户 userEntity={}; payAmount={}", userEntity, payAmount);

        //微信支付
        return wxPay("车己汽车-开通会员", httpServletRequest, payAmount, null, result, currentLoginUser, vipLv, "payLogNotify", AppAuctionWxPayConstant.WX_PAY_STATE_PAYLOG);
    }

    //参入金额单位元
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject wxPay(String payBody, HttpServletRequest httpServletRequest, BigDecimal amount, Long carId, JSONObject result, TokenPojo currentLoginUser, String vipLv, String url, String state) {
        //微信相关
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult;
        String uniqueOrder = "";

        try {
            wxPayUnifiedOrderRequest.setBody(payBody);
            if (AppAuctionWxPayConstant.WX_PAY_STATE_ONE_PRICE.equals(state)) {
                //如果是一口价保证金支付，设置支付过期时间 5分钟
                SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeExpire = sm.format(new Date(new Date().getTime() + 300000));
                wxPayUnifiedOrderRequest.setTimeExpire(timeExpire);
                uniqueOrder = COrderNoUtil.getOnePriceUniqueOrder();
            } else {
                uniqueOrder = COrderNoUtil.getUniqueOrder();
            }
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
            } else if (AppAuctionWxPayConstant.WX_PAY_STATE_ONE_PRICE.equals(state)) {
                appAuctionOnePriceService.insertLog(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), carId);
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

    //一口价订单支付回调通知
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String onePriceNotify(HttpServletRequest request) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
        String orderId = result.getOutTradeNo();        // 获取商户订单号
        String resultCode = result.getResultCode();
        Integer totalFee = result.getTotalFee();
        JSONObject jsonObject = dealOnePriceOrder(orderId, resultCode, totalFee);
        if (jsonObject.getInteger("code") == 402) {
            return WxPayNotifyResponse.fail("一口价订单 未找到对应的订单");
        } else if (jsonObject.getInteger("code") == 403) {
            return WxPayNotifyResponse.success("处理成功,支付失败");
        } else if (jsonObject.getInteger("code") == 200) {
            return WxPayNotifyResponse.success("处理成功,支付成功!");
        } else if (jsonObject.getInteger("code") == 500) {
            return WxPayNotifyResponse.success("处理报错");
        }
        return "";
    }

    //一口价订单 支付回调或者主动查询业务逻辑处理
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject dealOnePriceOrder(String orderId, String resultCode, Integer totalFeeFen) {
        JSONObject result = new JSONObject();

        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
//        String tradeNo = result.getTransactionId();        //微信支付订单号
        String totalFee = BaseWxPayResult.fenToYuan(totalFeeFen);      // 获取订单金额
        log.info(" ###订单金额 totalFee={}", totalFee);

        String redisKey = RedisConstant.ONE_PRICE_ORDER_STATUS + orderId;
        redisLock.lock(redisKey);
        try {
            //判断金额
            AppAuctionOnePriceCarLogEntity onePriceCarLogParamer = new AppAuctionOnePriceCarLogEntity();
            onePriceCarLogParamer.setOutTradeNo(orderId);
            AppAuctionOnePriceCarLogEntity byNo = appAuctionOnePriceCarLogMapper.selectOne(onePriceCarLogParamer);
            if (byNo == null) {
//                log.error(" 一口价订单支付回调通知 #### 一口价订单 未找到对应的订单 result={}", result);
                result.put("code", 402);
                return result;
            }

            if (resultCode.equals("SUCCESS")) {
                if (byNo.getStatus() == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {

                    byNo.setUpdateTime(new Date());
                    byNo.setNotifyTime(new Date());
                    byNo.setStatus(2);
                    appAuctionOnePriceCarLogMapper.updateById(byNo);

                    AppAuctionTransactionLogEntity appAuctionTransactionLog = appAuctionTransactionLogService.selectOne(new EntityWrapper<AppAuctionTransactionLogEntity>().eq("user_id", Long.valueOf(byNo.getUserId())).eq("car_id", byNo.getCarId()));
                    if (appAuctionTransactionLog == null) {
                        AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
                        appAuctionTransactionLogEntity.setUserId(Long.valueOf(byNo.getUserId()));
                        appAuctionTransactionLogEntity.setCreateTime(new Date());
                        appAuctionTransactionLogEntity.setAmount(new BigDecimal(byNo.getAmount()));
                        appAuctionTransactionLogEntity.setDesc("一口价订单保证金");
                        appAuctionTransactionLogEntity.setType(TransactionConstant.TRANSACTION_CONSTANT_RECHARGE);
                        appAuctionTransactionLogEntity.setOrderState(AppAuctionWxPayConstant.ORDERSTATE_DEFAULT);
                        appAuctionTransactionLogEntity.setOrderId(orderId);
                        appAuctionTransactionLogEntity.setPayType(AppAuctionWxPayConstant.WX_PAY);
                        appAuctionTransactionLogEntity.setCarId(byNo.getCarId());
                        appAuctionTransactionLogService.insert(appAuctionTransactionLogEntity);
                    }

                    AppAuctionEntity appAuction = appAuctionMapper.selectById(byNo.getCarId());
                    appAuction.setCarState(8);
                    appAuction.setUpdateTime(new Date());
                    appAuctionMapper.updateById(appAuction);//修改订单状态

                    AppAuctionOrderEntity appAuctionOrderParamer = new AppAuctionOrderEntity();
                    appAuctionOrderParamer.setOrderNo(orderId);
                    AppAuctionOrderEntity appAuctionOrderEntity1 = appAuctionOrderMapper.selectOne(appAuctionOrderParamer);
                    if (appAuctionOrderEntity1 == null) {
                        AppAuctionUpEntity appAuctionUpParamer = new AppAuctionUpEntity();
                        appAuctionUpParamer.setCarId(appAuction.getId());
                        AppAuctionUpEntity appAuctionUp = appAuctionUpMapper.selectOne(appAuctionUpParamer);
//                        AppAuctionVipControlEntity appAuctionVipControlEntity = appAuctionVipControlMapperMapper.selectByUserId(byNo.getUserId());


                        //创建订单信息
                        AppAuctionOrderEntity appAuctionOrderEntity = new AppAuctionOrderEntity();
                        appAuctionOrderEntity.setUserId(byNo.getUserId().longValue());
                        appAuctionOrderEntity.setCreateTime(new Date());
                        appAuctionOrderEntity.setUpdateTime(new Date());
                        appAuctionOrderEntity.setCarId(byNo.getCarId());
                        appAuctionOrderEntity.setOrderNo(orderId);
                        appAuctionOrderEntity.setOrderAmount(new BigDecimal(0));
                        BigDecimal serviceAmt = appAuction.getPrice().multiply(new BigDecimal(appAuctionUp.getServiceFee())).divide(new BigDecimal(100));
//                        //根据vip等级计算最终服务费
//                        if ("3".equals(appAuctionVipControlEntity.getVipLv())) {
//                            serviceAmt = serviceAmt.multiply(new BigDecimal(0.8));
//                        } else if ("4".equals(appAuctionVipControlEntity.getVipLv())) {
//                            serviceAmt = serviceAmt.multiply(new BigDecimal(0.5));
//                        }
                        appAuctionOrderEntity.setServiceFee(serviceAmt);
                        appAuctionOrderEntity.setDesc("非VIP用户创建的一口价车辆订单！");
                        appAuctionOrderEntity.setState(0);//订单状态,0交易中,1交易完成
                        appAuctionOrderMapper.insert(appAuctionOrderEntity);
                    }

                    AppAuctionBidEntity appAuctionBidParamer = new AppAuctionBidEntity();
                    appAuctionBidParamer.setCarId(appAuction.getId().longValue());
                    appAuctionBidParamer.setUserId(appAuction.getId().longValue());
                    appAuctionBidMapper.selectOne(appAuctionBidParamer);
                    //增加一条出价记录
                    AppAuctionBidEntity appAuctionBidEntity = new AppAuctionBidEntity();
                    appAuctionBidEntity.setBid(appAuction.getPrice());
                    appAuctionBidEntity.setUserId(Long.valueOf(byNo.getUserId()));
                    appAuctionBidEntity.setCarId(appAuction.getId().longValue());
                    appAuctionBidEntity.setUpdateTime(new Date());
                    appAuctionBidEntity.setCreateTime(new Date());
                    appAuctionBidEntity.setValid(1);
                    appAuctionBidMapper.insert(appAuctionBidEntity);

                } else {
                    //重复通知 不需要做处理
                }
            } else {
                //支付失败
                log.info("一口价订单支付回调通知 支付失败");
                byNo.setStatus(ConsEnum.WX_PAY_ORDER_FAIL.getCode());
                appAuctionOnePriceCarLogMapper.updateById(byNo);
                result.put("code", 403);
                return result;
            }
            result.put("code", 200);
            return result;
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            e.printStackTrace();
            result.put("code", 500);
            return result;
        } finally {
            log.info("##解回调锁###");
            redisLock.unlock(redisKey);
        }
    }

    //前端查询一口价订单状态
    public JSONObject queryOnePriceOrderStatus(JSONObject result, String outTradeNo) {
        AppAuctionOnePriceCarLogEntity onePriceCarLogParamer = new AppAuctionOnePriceCarLogEntity();
        onePriceCarLogParamer.setOutTradeNo(outTradeNo);
        AppAuctionOnePriceCarLogEntity byNo = appAuctionOnePriceCarLogMapper.selectOne(onePriceCarLogParamer);
        if (byNo == null) {
            result.put("code", 402);
            result.put("msg", "根据订单号没有找到对应订单！");
            return result;
        }

        Integer status = byNo.getStatus();
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
        if (status == ConsEnum.WX_PAY_ORDER_DEFAULT.getCode()) {
            //支付回调通知还没收到结果，需要主动查询
            WxPayOrderQueryResult wxPayOrderQueryResult = null;
            try {
                wxPayOrderQueryResult = this.wxPayService.queryOrder("", outTradeNo);

                String orderId = wxPayOrderQueryResult.getOutTradeNo();        // 获取商户订单号
                String resultCode = wxPayOrderQueryResult.getResultCode();
                Integer totalFee = wxPayOrderQueryResult.getTotalFee();
                JSONObject jsonObject = dealOnePriceOrder(orderId, resultCode, totalFee);
                return jsonObject;
            } catch (WxPayException e) {
                e.printStackTrace();
            }
        }

        result.put("code", 201);
        result.put("msg", "当前订单是vip用户不用支付！");
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
