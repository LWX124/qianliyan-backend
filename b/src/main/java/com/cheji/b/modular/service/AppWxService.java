package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.ConsEnum;
import com.cheji.b.modular.domain.AppUserAccountRecordEntity;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.AppWxpayOrderEntity;
import com.cheji.b.modular.domain.PushBillEntity;
import com.cheji.b.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.b.modular.mapper.AppUserMapper;
import com.cheji.b.modular.mapper.AppWxpayOrderMapper;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class AppWxService {
    private Logger logger = LoggerFactory.getLogger(AppWxService.class);

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Resource
    private AppWxpayOrderMapper appWxpayOrderMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private PushBillService pushBillService;

    @Transactional(rollbackFor = Exception.class)
    public void insertCuser(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, int userId, int type, int userid, JSONObject in, String orderNo) {

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

        EntityWrapper<PushBillEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("pb_number", orderNo);
        PushBillEntity pushBillEntity = pushBillService.selectOne(wrapper);
        pushBillEntity.setMessPayPumber(wxPayUnifiedOrderRequest.getOutTradeNo());
        pushBillService.updateById(pushBillEntity);
        logger.error("###  保存pb支付编号 ##  pushBillEntity={}", pushBillEntity);
    }


    @Async
    public void insert(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, int userId, int orderType) {
//        AppWxUnifiedorderRecord appWxUnifiedorderRecord = new AppWxUnifiedorderRecord();
//
//        //设置请求入参
//        appWxUnifiedorderRecord.setAppid(wxPayUnifiedOrderRequest.getAppid());
//        appWxUnifiedorderRecord.setMchId(wxPayUnifiedOrderRequest.getMchId());
//        appWxUnifiedorderRecord.setNonceStr(wxPayUnifiedOrderRequest.getNonceStr());
//        appWxUnifiedorderRecord.setSign(wxPayUnifiedOrderRequest.getSign());
//        appWxUnifiedorderRecord.setBody(wxPayUnifiedOrderRequest.getBody());
//        appWxUnifiedorderRecord.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
//        appWxUnifiedorderRecord.setTotalFee(wxPayUnifiedOrderRequest.getTotalFee());
//        appWxUnifiedorderRecord.setSpbillCreateIp(wxPayUnifiedOrderRequest.getSpbillCreateIp());
//        appWxUnifiedorderRecord.setNotifyUrl(wxPayUnifiedOrderRequest.getNotifyUrl());
//        appWxUnifiedorderRecord.setTradeType(wxPayUnifiedOrderRequest.getTradeType());
//
//        appWxUnifiedorderRecord.setSource(type);
//        appWxUnifiedorderRecord.setUserId(userId);
//
//        //设置返回值
////        appWxUnifiedorderRecord.setReturnCode(wxPayAppOrderResult.);
////        appWxUnifiedorderRecord.setReturnMsg(jsonObject.getString("return_msg"));
//        appWxUnifiedorderRecord.setWxNonceStr(wxPayAppOrderResult.getNonceStr());
//        appWxUnifiedorderRecord.setWxSign(wxPayAppOrderResult.getSign());
////        appWxUnifiedorderRecord.setResultCode(wxPayAppOrderResult);
////        appWxUnifiedorderRecord.setErrCode(wxPayAppOrderResult.gete);
////        appWxUnifiedorderRecord.setErrCodeDes(wxPayAppOrderResult.get);
////        appWxUnifiedorderRecord.setWxTradeType("APP");
//        appWxUnifiedorderRecord.setPrepayId(wxPayAppOrderResult.getPrepayId());

//        appWxUnifiedorderRecordMapper.insert(appWxUnifiedorderRecord);


        AppWxpayOrderEntity appWxpayOrderEntity = new AppWxpayOrderEntity();
        appWxpayOrderEntity.setCreateTime(new Date());
        appWxpayOrderEntity.setAmount(new BigDecimal(wxPayUnifiedOrderRequest.getTotalFee()));
        appWxpayOrderEntity.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        appWxpayOrderEntity.setStatus(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode());//初始状态
        appWxpayOrderEntity.setUserId(userId);
        appWxpayOrderEntity.setPrepayId(wxPayAppOrderResult.getPrepayId());
        appWxpayOrderEntity.setType(orderType);
        appWxpayOrderEntity.setBusinessStatus(ConsEnum.WX_PAY_ORDER_BUSINESS_STATUS_DEFAULT.getCode());
        logger.info("###  用户充值 ### appWxpayOrderEntity={}", appWxpayOrderEntity);
        appWxpayOrderMapper.insert(appWxpayOrderEntity);
    }

    /**
     * 修改订单状态
     */
    @Async
    public void updateOrderStatus(String tradeOutNo, int orderStatus) {
        AppWxpayOrderEntity appWxpayOrderEntity = new AppWxpayOrderEntity();
        appWxpayOrderEntity.setOutTradeNo(tradeOutNo);

        AppWxpayOrderEntity appWxpayOrderEntity1 = appWxpayOrderMapper.selectOne(appWxpayOrderEntity);
        appWxpayOrderEntity1.setStatus(orderStatus);
        appWxpayOrderMapper.updateStatus(appWxpayOrderEntity1);
    }


    /**
     * 充值成功后 给用户余额加钱和修改订单状态
     */
    @Transactional
    public void addMoney(AppWxpayOrderEntity appWxpayOrderEntity) {
        //修改余额
        AppUserEntity appUserEntity = appUserMapper.selectUser(appWxpayOrderEntity.getUserId());
        appUserEntity.setBalance(appUserEntity.getBalance().add(appWxpayOrderEntity.getAmount().divide(new BigDecimal(100))));
        appUserMapper.updateById(appUserEntity);

        //修改订单状态
        appWxpayOrderEntity.setStatus(ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
        appWxpayOrderMapper.updateStatus(appWxpayOrderEntity);

        AppUserAccountRecordEntity appUserAccountRecordEntity = new AppUserAccountRecordEntity();
        appUserAccountRecordEntity.setCreateTime(new Date());
        appUserAccountRecordEntity.setAddFlag(1);
        appUserAccountRecordEntity.setMomey(appWxpayOrderEntity.getAmount().divide(new BigDecimal(100)));
        appUserAccountRecordEntity.setSource(2);
        appUserAccountRecordEntity.setType(5);
        appUserAccountRecordEntity.setUserId(appWxpayOrderEntity.getUserId());
        appUserAccountRecordEntity.setBusinessId(appWxpayOrderEntity.getOutTradeNo());
        logger.info("###  用户充值 ### appUserAccountRecordEntity={}", appUserAccountRecordEntity);
        appUserAccountRecordMapper.insert(appUserAccountRecordEntity);
    }
}
