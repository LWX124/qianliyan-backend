package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppOrderRollBackMapper;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.AppWxUnifiedorderRecordMapper;
import com.cheji.web.modular.mapper.AppWxpayOrderMapper;
import com.cheji.web.pojo.PayResponPojo;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import com.cheji.web.util.IpUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AppWxService {

    private Logger logger = LoggerFactory.getLogger(AppWxService.class);

    @Resource
    private AppWxUnifiedorderRecordMapper appWxUnifiedorderRecordMapper;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private AppOrderRollBackMapper appOrderRollBackMapper;

    @Resource
    private AppWxpayOrderMapper appWxpayOrderMapper;

    @Resource
    private DefaultMQProducer mqProducer;

    @Resource
    private WxPayService wxPayService;

    @Resource
    private AppWxService appWxService;

    @Resource
    private CleanPriceDetailService cleanPriceDetailService;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppUserCouponService appUserCouponService;

    @Resource
    private AppCouponTypeService appCouponTypeService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private UserService userService;

    @Resource
    private PushBillService pushBillService;

    @Resource
    private AppSendOutSheetService appSendOutSheetService;


    @Transactional(rollbackFor = Exception.class)
    public void insert(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, int userId, int type, int userid, JSONObject in, String orderNo) {

        AppWxpayOrderEntity appWxpayOrderEntity = new AppWxpayOrderEntity();
        appWxpayOrderEntity.setCreateTime(new Date());
        appWxpayOrderEntity.setAmount(new BigDecimal(wxPayUnifiedOrderRequest.getTotalFee()));
        appWxpayOrderEntity.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        appWxpayOrderEntity.setStatus(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode());//初始状态
        appWxpayOrderEntity.setUserId(userId);
        appWxpayOrderEntity.setPrepayId(wxPayAppOrderResult.getPrepayId());
        logger.error("服务类型 type={}", type);
        appWxpayOrderEntity.setType(type);
        appWxpayOrderEntity.setBusinessStatus(ConsEnum.WX_PAY_ORDER_BUSINESS_STATUS_NOT.getCode());

        appWxpayOrderMapper.insert(appWxpayOrderEntity);


        if (type == 4) {
            cleanIndetService.addCleanIndent(in, orderNo, wxPayUnifiedOrderRequest.getOutTradeNo(), userId);
        } else if (type == 5) {
            cleanIndetService.addBeautyIndent(in, orderNo, wxPayUnifiedOrderRequest.getOutTradeNo(), userId);
        } else if (type == 6) {
            cleanIndetService.addRescueIndent(orderNo, wxPayUnifiedOrderRequest.getOutTradeNo());
        } else if (type == 7) {
            cleanIndetService.addSprayPaintIndent(orderNo, wxPayUnifiedOrderRequest.getOutTradeNo());
        } else if (type == 8) {
            cleanIndetService.addYearCheckIndent(orderNo, wxPayUnifiedOrderRequest.getOutTradeNo());
        } else if (type == 9) {
            cleanIndetService.addSusituteDriIndetn(orderNo, wxPayUnifiedOrderRequest.getOutTradeNo());
        } else if (type == 10) {
            cleanIndetService.addPbSosIndent(orderNo, wxPayUnifiedOrderRequest.getOutTradeNo());
        }

    }

    /**
     * 修改订单状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(String tradeOutNo, int orderStatus) {
        logger.info("###  修改支付订单状态##  tradeOutNo={};orderStatus={}", tradeOutNo, orderStatus);

        AppWxpayOrderEntity appWxpayOrderEntity = new AppWxpayOrderEntity();
        appWxpayOrderEntity.setOutTradeNo(tradeOutNo);
        appWxpayOrderEntity.setStatus(orderStatus);

        appWxpayOrderMapper.updateStatus(appWxpayOrderEntity);
    }

    /**
     * 洗车通知业务
     *
     * @param outTradeNo 业务订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void clearCarNotify(String outTradeNo) {
        //洗车
        //修改订单状态
        EntityWrapper<CleanIndetEntity> entityEntityWrapper = new EntityWrapper<>();
        entityEntityWrapper.eq("merchants_pay_number", outTradeNo);
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(entityEntityWrapper);
        logger.error("###  查询修改洗车订单数据 ###  cleanIndetEntity={}", cleanIndetEntity);
        cleanIndetEntity.setPayState("1");
        cleanIndetEntity.setIndentState("2");
        cleanIndetService.updateById(cleanIndetEntity);
        this.updateOrderStatus(outTradeNo, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        if (cleanIndetEntity.getContractFlag() == 1) {//合约标识 1：是  2：否，  如果是合约订单，退款之后需要把次数加上
            //收到支付通知之后  修改商品库存回滚辅助表的支付状态
            AppOrderRollBack appOrderRollBackParamer = new AppOrderRollBack();
            appOrderRollBackParamer.setType(1);
            appOrderRollBackParamer.setOrderId(String.valueOf(cleanIndetEntity.getId()));
            appOrderRollBackParamer.setPayFlag(1);
            appOrderRollBackParamer.setOpsFlag(1);
            AppOrderRollBack appOrderRollBack = appOrderRollBackMapper.selectOne(appOrderRollBackParamer);
            if (appOrderRollBack == null) {
                logger.error("###  支付回调 查不到订单支付回滚数据  appOrderRollBackParamer={}", appOrderRollBackParamer);
            } else {
                appOrderRollBack.setPayFlag(2);//修改付款状态
                appOrderRollBackMapper.updateById(appOrderRollBack);
            }
        }

        //mq
        Message sendMsg = new Message("all", "jgts_xc", cleanIndetEntity.getCleanIndentNumber().getBytes());
        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void beautyUpdate(String outTradeNo) {
        EntityWrapper<CleanIndetEntity> entityEntityWrapper = new EntityWrapper<>();
        entityEntityWrapper.eq("merchants_pay_number", outTradeNo);
        CleanIndetEntity cleanIndetEntity = cleanIndetService.selectOne(entityEntityWrapper);
        logger.error("###  查询修改订单数据 ###  cleanIndetEntity={}", cleanIndetEntity);
        cleanIndetEntity.setPayState("1");
        cleanIndetEntity.setIndentState("2");
        cleanIndetService.updateById(cleanIndetEntity);
        this.updateOrderStatus(outTradeNo, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        //mq
        Message sendMsg = new Message("all", "jgts_mr", cleanIndetEntity.getCleanIndentNumber().getBytes());
        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void rescueUpdate(String outTradeNo) {
        EntityWrapper<AppRescueIndentEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("merchants_pay_number", outTradeNo);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(wrapper);
        logger.error("###  查询修改救援订单数据 ###  rescueEntity={}", appRescueIndentEntity);
        appRescueIndentEntity.setPayState(1);
        appRescueIndentEntity.setState(1);
        appRescueIndentService.updateById(appRescueIndentEntity);
        this.updateOrderStatus(outTradeNo, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        //mq
        Message sendMsg = new Message("all", "jgts_jy", appRescueIndentEntity.getRescueNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void checkUpdate(String orderId) {
        EntityWrapper<AppYearCheckIndentEntity> checkWarpper = new EntityWrapper<>();
        checkWarpper.eq("pay_number", orderId);
        AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectOne(checkWarpper);
        logger.error("###  查询修改年检订单数据 ###  appYearCheckIndentEntity={}", appYearCheckIndentEntity);
        if (appYearCheckIndentEntity.getYearCheckType() == 1) {
            //免检代办
            appYearCheckIndentEntity.setState(2);
        } else {
            appYearCheckIndentEntity.setState(1);
        }
        appYearCheckIndentEntity.setPayState(1);

        EntityWrapper<AppUserCouponEntity> couponWrapper = new EntityWrapper<>();
        couponWrapper.eq("order_number", appYearCheckIndentEntity.getYearCheckNumber());
        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectOne(couponWrapper);
        if (appUserCouponEntity != null) {
            Integer couponId = appUserCouponEntity.getCouponId();
            AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(couponId);
            BigDecimal money = appCouponTypeEntity.getMoney();
            appYearCheckIndentEntity.setPrice(appYearCheckIndentEntity.getPrice().subtract(money));
            appUserCouponEntity.setIsUse(1);
            appUserCouponService.updateById(appUserCouponEntity);
        }


        appYearCheckIndentService.updateById(appYearCheckIndentEntity);
        this.updateOrderStatus(orderId, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
//
//        //结算
//        appYearCheckIndentService.merchantsAddBanlance(appYearCheckIndentEntity);

        //mq
        Message sendMsg = new Message("all", "jgts_NJ", appYearCheckIndentEntity.getYearCheckNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void sprayUpdate(String orderId) {
        EntityWrapper<AppSprayPaintIndentEntity> appSprayPaintIndentWrapper = new EntityWrapper<>();
        appSprayPaintIndentWrapper.eq("pay_number", orderId);
        AppSprayPaintIndentEntity paintIndentEntity = appSprayPaintIndentService.selectOne(appSprayPaintIndentWrapper);
        logger.error("###  查询修改喷漆订单数据 ###  paintIndentEntity={}", paintIndentEntity);
        paintIndentEntity.setPayState(1);
        paintIndentEntity.setState(8);
        paintIndentEntity.setSendCarTime(new Date());

        //查询是否有优惠卷使用
        EntityWrapper<AppUserCouponEntity> couponWrapper = new EntityWrapper<>();
        couponWrapper.eq("order_number", paintIndentEntity.getSprayPaintNumber());
        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectOne(couponWrapper);
        if (appUserCouponEntity != null) {
            Integer couponId = appUserCouponEntity.getCouponId();
            AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(couponId);
            BigDecimal money = appCouponTypeEntity.getMoney();
            paintIndentEntity.setPrice(paintIndentEntity.getPrice().subtract(money));
            appUserCouponEntity.setIsUse(1);
            appUserCouponService.updateById(appUserCouponEntity);
        }

        appSprayPaintIndentService.updateById(paintIndentEntity);
        this.updateOrderStatus(orderId, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        //查询订单数据
        //商户加钱
        appSprayPaintIndentService.SprayPaintAddMerchantsAmount(paintIndentEntity);

        //mq
        Message sendMsg = new Message("all", "jgts_PQ", paintIndentEntity.getSprayPaintNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void messagePayUpdate(String orderId) {
        //根据支付编号查询到订单
        //两边都查询判断
        EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("mess_pay_number", orderId);
        PushBillEntity pushBillEntity = pushBillService.selectOne(wrapper);
        if (pushBillEntity == null) {
            //为空就是sos中的操作
            EntityWrapper<AppSendOutSheetEntity> appSendWrapper = new EntityWrapper<>();
            appSendWrapper.eq("mess_pay_number", orderId);
            AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectOne(appSendWrapper);
            //修改支付状态
            logger.error("###  修改支付状态 ###  appSendOutSheetEntity={}", appSendOutSheetEntity);
            appSendOutSheetEntity.setPayState(1);
            appSendOutSheetService.updateById(appSendOutSheetEntity);
        } else {
            logger.error("###  修改支付状态 ###  pushBillEntity={}", pushBillEntity);
            pushBillEntity.setPayState(1);
            pushBillService.updateById(pushBillEntity);
        }
        this.updateOrderStatus(orderId, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void subsitudeUpdate(String orderId) {
        EntityWrapper<AppSubstituteDrivingIndentEntity> drivingIndentWrapper = new EntityWrapper<>();
        drivingIndentWrapper.eq("pay_number", orderId);
        AppSubstituteDrivingIndentEntity appSubstituteDrivingIndent = appSubstituteDrivingIndentService.selectOne(drivingIndentWrapper);
        logger.error("###  修改代驾订单数据 ###  appSubstituteDrivingIndent={}", appSubstituteDrivingIndent);
        appSubstituteDrivingIndent.setPayState(1);
        appSubstituteDrivingIndent.setIndentState(1);


        EntityWrapper<AppUserCouponEntity> couponWrapper = new EntityWrapper<>();
        couponWrapper.eq("order_number", appSubstituteDrivingIndent.getSubstituteDrivingNumber());
        AppUserCouponEntity appUserCouponEntity = appUserCouponService.selectOne(couponWrapper);
//        int type = 0;
        if (appUserCouponEntity != null) {
            Integer couponId = appUserCouponEntity.getCouponId();
            AppCouponTypeEntity appCouponTypeEntity = appCouponTypeService.selectById(couponId);
            BigDecimal money = appCouponTypeEntity.getMoney();
            appSubstituteDrivingIndent.setActualPrice(appSubstituteDrivingIndent.getActualPrice().subtract(money));
            appUserCouponEntity.setIsUse(1);
            appUserCouponService.updateById(appUserCouponEntity);
//            type =1 ;
        }

        JSONObject itchiecid = appRescueIndentService.getMerchants(appSubstituteDrivingIndent.getStartLng(), appSubstituteDrivingIndent.getStartLat(), null, RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO);
        if (itchiecid != null) {
            Integer thicId = itchiecid.getInteger("userBId");
            if (thicId != null) {
                appSubstituteDrivingIndent.setUserBId(thicId);
            }
        }

        appSubstituteDrivingIndent.setUpdateTime(new Date());
        appSubstituteDrivingIndentService.updateById(appSubstituteDrivingIndent);
        this.updateOrderStatus(orderId, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        //商户加钱
//        appSubstituteDrivingIndentService.substituAddMerchantsAmount(appSubstituteDrivingIndent,type);


        //mq
        Message sendMsg = new Message("all", "jgts_SC", appSubstituteDrivingIndent.getSubstituteDrivingNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


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

    @Transactional(rollbackFor = Exception.class)
    public JSONObject createCusOrder(JSONObject in, Integer userid, String body, JSONObject result, HttpServletRequest httpServletRequest, TokenPojo currentLoginUser, String host) {
        Integer type = in.getInteger("type");

        Integer amount = 0;
        //Integer type = in.getInteger("type");

        String bodyContent = body;
        String orderNo = "";
        if (type == 4) {
            bodyContent = "车己洗车";
            orderNo = GenerateDigitalUtil.getOrderNo();
            orderNo = "xc" + orderNo;
            //判断操作是否为零并且减一
            String id = in.getString("id");
            logger.error("服务id   id={}", id);
            logger.error("type  type={}", type);
            CleanPriceDetailEntity cleanPriceDetailEntity = cleanPriceDetailService.selectById(id);
            logger.error("查询服务详情 cleanPriceDetailEntity={}", cleanPriceDetailEntity);
            amount = cleanPriceDetailEntity.getPreferentialPrice().multiply(new BigDecimal(100)).intValue();
            logger.error("金额数字 amount={}", amount);
            if (cleanPriceDetailEntity.getContractProject() != null) {
                if (cleanPriceDetailEntity.getContractProject() == 1) {
                    //是合约服务
                    if (cleanPriceDetailEntity.getResidueDegree() < 1) {
                        result.put("code", 300);
                        result.put("msg", "该服务已售完");
                        return result;
                    }
                }
            }
        } else if (type == 5) {
            bodyContent = "车己美容";
            amount = cleanIndetService.findMoney(type, in, 0);
            orderNo = GenerateDigitalUtil.getOrderNo();
            orderNo = "mr" + orderNo;
        } else if (type == 6) {
            bodyContent = "车己救援";
            orderNo = in.getString("number");
            //查询价格
            amount = cleanIndetService.findMoney(type, in, 0);
            // amount = 1;
        } else if (type == 7) {
            bodyContent = "车己喷漆";
            String id = in.getString("id");
            AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectById(id);
            if (appSprayPaintIndentEntity == null) {
                result.put("code", 406);
                result.put("msg", "订单不存在");
                return result;
            }
            orderNo = appSprayPaintIndentEntity.getSprayPaintNumber();
            amount = cleanIndetService.findMoney(type, in, 0);
            if (amount == null) {
                result.put("code", 406);
                result.put("msg", "未查询到优惠卷");
                return result;
            }
        } else if (type == 8) {
            bodyContent = "车己年检";
            String id = in.getString("id");
            AppYearCheckIndentEntity appYearCheckIndentEntity = appYearCheckIndentService.selectById(id);
            if (appYearCheckIndentEntity == null) {
                result.put("code", 406);
                result.put("msg", "订单不存在");
                return result;
            }
            orderNo = appYearCheckIndentEntity.getYearCheckNumber();
            amount = cleanIndetService.findMoney(type, in, 0);
            if (amount == null) {
                result.put("code", 406);
                result.put("msg", "未查询到优惠卷");
                return result;
            }
        } else if (type == 9) {
            bodyContent = "车己代驾";
            String id = in.getString("id");
            //查询价格
            AppSubstituteDrivingIndentEntity appSubstituteDrivingIndentEntity = appSubstituteDrivingIndentService.selectById(id);
            if (appSubstituteDrivingIndentEntity == null) {
                result.put("code", 406);
                result.put("msg", "订单不存在");
                return result;
            }
            orderNo = appSubstituteDrivingIndentEntity.getSubstituteDrivingNumber();
            amount = cleanIndetService.findMoney(type, in, 0);
            if (amount == null) {
                result.put("code", 406);
                result.put("msg", "未查询到优惠卷");
                return result;
            }
        } else if (type == 10) {
            bodyContent = "车己信息";
            //信息id
            String id = in.getString("id");
            //messagetype 信息来源 //1 pb  2 sos
            Integer messageType = in.getInteger("messageType");
            if (StringUtils.isEmpty(id)) {
                result.put("code", 406);
                result.put("msg", "信息id为空");
                return result;
            }
            if (messageType == null) {
                result.put("code", 406);
                result.put("msg", "messageType为空");
                return result;
            }
            if (messageType == 1) {
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
                amount = cleanIndetService.findMoney(type, in, messageType);
            } else {
                AppSendOutSheetEntity appSendOutSheetEntity = appSendOutSheetService.selectById(id);
                if (appSendOutSheetEntity == null) {
                    result.put("code", 406);
                    result.put("msg", "订单不存在");
                    return result;
                }
                String sosNumber = GenerateDigitalUtil.getOrderNo();
                orderNo = "sos" + sosNumber;
                appSendOutSheetEntity.setSosNumber(orderNo);
                appSendOutSheetService.updateById(appSendOutSheetEntity);
                amount = cleanIndetService.findMoney(type, in, messageType);
            }
            if (amount == 0 || amount == null) {
                amount = 1000;
            }

        }

        //4：洗车
        PayResponPojo payResponPojo = new PayResponPojo();

        if (amount == null || amount <= 0) {
            //先根据用户id查询到是否有没用使用完的订单
            List<CleanIndetEntity> cleanIndetfree = cleanIndetService.findisOnFreeIndent(userid);
            if (cleanIndetfree != null) {
                result.put("code", 300);
                result.put("msg", "上一个免费订单还没有完成哦");
                return result;
            }
            //免费洗直接下订单 不走支付流程
            cleanIndetService.addCleanIndent(in, orderNo, "免费洗", userid);
            PayResponPojo pojo = new PayResponPojo();
            pojo.setCleanIndentNumber(orderNo);
            pojo.setIsFreeWash("1");
            result.put("code", 200);
            result.put("data", pojo);
            return result;
        }
        payResponPojo.setIsFreeWash("0");

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        WxPayAppOrderResult wxPayAppOrderResult = null;
        try {
            wxPayUnifiedOrderRequest.setBody(bodyContent);
            wxPayUnifiedOrderRequest.setOutTradeNo(getUniqueOrder());
            logger.error("金额数字 amount={}", amount);
            wxPayUnifiedOrderRequest.setTotalFee(amount);
            wxPayUnifiedOrderRequest.setNotifyUrl(host + "cServer/wx/pay/notify");
            wxPayUnifiedOrderRequest.setTradeType("APP");
            wxPayUnifiedOrderRequest.setSpbillCreateIp(IpUtil.getIpAddr(httpServletRequest));
            logger.info("#### createOrder### 入参 wxPayUnifiedOrderRequest={}", wxPayUnifiedOrderRequest);
            wxPayAppOrderResult = this.wxPayService.createOrder(wxPayUnifiedOrderRequest);
            logger.info("#### createOrder### 出参 wxPayAppOrderResult={}", wxPayAppOrderResult);
            appWxService.insert(wxPayUnifiedOrderRequest, wxPayAppOrderResult, currentLoginUser.getAppUserEntity().getId(), type, userid, in, orderNo);
            payResponPojo.setCleanIndentNumber(orderNo);
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

    /**
     * 充值成功后 给用户余额加钱和修改订单状态
     */
    @Transactional
    public void addMoney(AppWxpayOrderEntity appWxpayOrderEntity) {
        UserEntity userEntity = userService.selectById(appWxpayOrderEntity.getUserId());

        userEntity.setBalance(userEntity.getBalance().add(appWxpayOrderEntity.getAmount().divide(new BigDecimal(100))));

        userService.updateById(userEntity);

        appWxpayOrderEntity.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        appWxpayOrderMapper.updateStatus(appWxpayOrderEntity);

        AppUserAccountRecordEntity appUserAccountRecordEntity = new AppUserAccountRecordEntity();
        appUserAccountRecordEntity.setCreateTime(new Date());
        appUserAccountRecordEntity.setAddFlag(1);
        appUserAccountRecordEntity.setMomey(appWxpayOrderEntity.getAmount().divide(new BigDecimal(100)));
        appUserAccountRecordEntity.setSource(1);
        appUserAccountRecordEntity.setType(21);  //c端充值
        appUserAccountRecordEntity.setUserId(appWxpayOrderEntity.getUserId());
        appUserAccountRecordEntity.setBusinessId(appWxpayOrderEntity.getOutTradeNo());
        logger.info("###  用户充值 ### appUserAccountRecordEntity={}", appUserAccountRecordEntity);
        appUserAccountRecordMapper.insert(appUserAccountRecordEntity);

    }
}
