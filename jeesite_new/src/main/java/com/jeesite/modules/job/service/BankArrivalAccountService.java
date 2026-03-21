package com.jeesite.modules.job.service;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jeesite.common.codec.Md5Utils;
import com.jeesite.modules.app.dao.*;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.constant2.ConsEnum;
import com.jeesite.modules.pojo2.WxPayFindResultPojo;
import com.jeesite.modules.util2.HttpUtil;
import com.jeesite.modules.util2.WeChatUtils;
import com.jeesite.modules.util2.XmlTool;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业付款到银行卡
 */
@Component
public class BankArrivalAccountService {

    private static Logger logger = LoggerFactory.getLogger(BankArrivalAccountService.class);

    @Resource
    private AppWxBankInterfaceDao appWxBankInterfaceDao;

    @Resource
    private AppWxCashOutResultDao appWxCashOutResultDao;

    @Resource
    private AppWxCashOutRecordDao appWxCashOutRecordDao;

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private AppBUserDao appBUserDao;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    @Value("${wx.mchId}")
    private String mchId;

    @Value("${publicKey}")
    private String publicKey;

    @Value("${apiclientCert}")
    private String apiclientCert;

    @Autowired
    private WxPayService wxPayService;


    @Transactional(rollbackFor = Exception.class)
    public void payToBank(AppWxCashOutRecord appWxCashOutRecord) {
        EntPayBankRequest request = new EntPayBankRequest();
        request.setPartnerTradeNo(appWxCashOutRecord.getPartnerTradeNo());
        request.setEncBankNo(appWxCashOutRecord.getBankNumber());
        request.setEncTrueName(appWxCashOutRecord.getUserBankName());
        request.setBankCode(appWxCashOutRecord.getBankCode());
        request.setAmount(appWxCashOutRecord.getSendAmount() * 100);//单位分
        request.setDescription("车己提现");
        EntPayBankResult entPayBankResult = null;
        try {
            entPayBankResult = wxPayService.getEntPayService().payBank(request);
        } catch (WxPayException e) {
            // logger.error("##调用结果 e ###",e);
            //回滚账户余额
            if (entPayBankResult==null){
                Map rollBackMap = new HashMap();
                rollBackMap.put("user_id", appWxCashOutRecord.getUserId().intValue());
                rollBackMap.put("amount", appWxCashOutRecord.getAmount());
                rollBackMap.put("cashOutId", appWxCashOutRecord.getId());
                rollBackMap.put("source", appWxCashOutRecord.getSources());
                rollBackMoney(rollBackMap);
                logger.error("### 调用接口失败### 回滚账户余额### rollBackMap={};修改订单状态为 已处理  appWxCashOutRecord={}", rollBackMap, appWxCashOutRecord);
                appWxCashOutRecord.setStatus(String.valueOf(ConsEnum.CASH_OUT_STATUS_FAIL.getCode()));//提交失败
                appWxCashOutRecord.setResult(e.getResultCode() + "," + e.getErrCode() + "," + e.getResultCode() + "," + e.getReturnMsg());
                appWxCashOutRecord.setResultInfo(e.getErrCodeDes() + ",账户余额已退回,请稍后重新提现。");
                if (appWxCashOutRecord.getResultInfo().contains("出款账户余额不足")) {//公司微信商户号上面余额不足
                    appWxCashOutRecord.setResultInfo("银行受理失败，账户余额已退回,请稍后重新提现。");
                }
                appWxCashOutRecordDao.updateContent(appWxCashOutRecord);
                e.printStackTrace();
            }
        } finally {
            write(entPayBankResult, request.toXML(), appWxCashOutRecord);//记录进数据库
        }
    }


    /**
     * 记录进数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void write(EntPayBankResult entPayBankResult, String reqXml, AppWxCashOutRecord appWxCashOutRecord) {
        if (entPayBankResult != null) {
            AppWxBankInterface appWxBankInterface = new AppWxBankInterface();
            appWxBankInterface.setReturnCode(entPayBankResult.getReturnCode());
            appWxBankInterface.setReturnMsg(entPayBankResult.getReturnMsg());
            appWxBankInterface.setResultCode(entPayBankResult.getResultCode());
            appWxBankInterface.setErrCode(entPayBankResult.getErrCode());
            appWxBankInterface.setErrCodeDes(entPayBankResult.getErrCodeDes());
            appWxBankInterface.setMchId(entPayBankResult.getMchId());
            appWxBankInterface.setPartnerTradeNo(entPayBankResult.getPartnerTradeNo());
            appWxBankInterface.setAmount(entPayBankResult.getAmount() + "");
            appWxBankInterface.setNonceStr(entPayBankResult.getNonceStr());
            appWxBankInterface.setSign(entPayBankResult.getSign());
            appWxBankInterface.setPaymentNo(entPayBankResult.getPaymentNo());
            appWxBankInterface.setCmmsAmt(entPayBankResult.getCmmsAmount() + "");
            appWxBankInterface.setCreateTime(new Date());
            appWxBankInterface.setParamerXml(reqXml);
            appWxBankInterface.setOpsFlag(1);
            appWxBankInterfaceDao.insert(appWxBankInterface);

            appWxCashOutRecord.setStatus(String.valueOf(ConsEnum.CASH_OUT_STATUS_SUCCESS.getCode()));//提交成功
            appWxCashOutRecordDao.updateStatusById(appWxCashOutRecord);
            logger.error("### 调用接口成功###;修改订单状态为 已处理  appWxCashOutRecord={}", appWxCashOutRecord);
        }
    }

    public void findOrderResult(Map map) throws DocumentException {
        WxPayFindResultPojo wxPayFindResultPojo = new WxPayFindResultPojo();
        wxPayFindResultPojo.setMchId(mchId);
        wxPayFindResultPojo.setNonceStr(WeChatUtils.getRandomStr(32));
        wxPayFindResultPojo.setPartnerTradeNo(String.valueOf(map.get("partner_trade_no")));

        StringBuilder sb = new StringBuilder();
        sb.append("mch_id=");
        sb.append(wxPayFindResultPojo.getMchId());
        sb.append("&nonce_str=");
        sb.append(wxPayFindResultPojo.getNonceStr());
        sb.append("&partner_trade_no=");
        sb.append(wxPayFindResultPojo.getPartnerTradeNo());
        sb.append("&key=");
        sb.append("jdfjdfd7365uyilz8765rtd6d6c6g7g3");
        String sign = Md5Utils.md5(sb.toString()).toUpperCase();
        wxPayFindResultPojo.setSign(sign);
        String reqXml = WeChatUtils.sendDataToXml(wxPayFindResultPojo).replaceAll("&quot;", "\"");
        logger.error("##企业付款到银行卡 调用接口成功###; 查询提现结果 reqXml={}", reqXml);
        String result = HttpUtil.requestWithHttpsWithP12("https://api.mch.weixin.qq.com/mmpaysptrans/query_bank", reqXml, mchId, apiclientCert);
        logger.error("##企业付款到银行卡 调用接口成功###; 查询提现结果 result={}", result);
        JSONObject jsonObject = XmlTool.documentToJSONObject(result);
        logger.error("##查询结果json ###; json jsonObject={}", jsonObject);
        writeOrderResult(reqXml, jsonObject, map);
    }

    @Transactional
    public void writeOrderResult(String reqXml, JSONObject jsonObject, Map map) {
        String return_code = jsonObject.getString("return_code");
        String return_msg = jsonObject.getString("return_msg");
        String result_code = jsonObject.getString("result_code");
        String err_code = jsonObject.getString("err_code");
        String err_code_des = jsonObject.getString("err_code_des");
        String mch_id = jsonObject.getString("mch_id");
        String partner_trade_no = jsonObject.getString("partner_trade_no");
        String payment_no = jsonObject.getString("payment_no");
        String bank_no_md5 = jsonObject.getString("bank_no_md5");
        String true_name_md5 = jsonObject.getString("true_name_md5");
        String status = jsonObject.getString("status");
        String create_time = jsonObject.getString("create_time");
        String pay_succ_time = jsonObject.getString("pay_succ_time");
        String reason = jsonObject.getString("reason");
//        Integer amount = jsonObject.getInteger("amount");
        Integer sendAmount = jsonObject.getInteger("send_amount");
        Integer cmms_amt = jsonObject.getInteger("cmms_amt");

        AppWxCashOutResult appWxCashOutResult = new AppWxCashOutResult();
        appWxCashOutResult.setReturnCode(return_code);
        appWxCashOutResult.setReturnMsg(return_msg);
        appWxCashOutResult.setResultCode(result_code);
        appWxCashOutResult.setErrCode(err_code);
        appWxCashOutResult.setErrCodeDes(err_code_des);
        appWxCashOutResult.setMchId(mch_id);
        appWxCashOutResult.setPartnerTradeNo(partner_trade_no);
        appWxCashOutResult.setPaymentNo(payment_no);
        appWxCashOutResult.setBankNoMd5(bank_no_md5);
        appWxCashOutResult.setTrueNameMd5(true_name_md5);
        appWxCashOutResult.setStatus(status);
        appWxCashOutResult.setCreateTime(create_time);
        appWxCashOutResult.setCreTime(new Date());
        appWxCashOutResult.setPaySuccTime(pay_succ_time);
        appWxCashOutResult.setReason(reason);
        if (sendAmount != null) {
            appWxCashOutResult.setAmount(Long.valueOf(sendAmount));
        }
        if (cmms_amt != null) {
            appWxCashOutResult.setCmmsAmt(Long.valueOf(cmms_amt));
        }
        appWxCashOutResult.setReqXml(reqXml);

        appWxCashOutResultDao.insert(appWxCashOutResult);


        String cashOutId = String.valueOf(map.get("id"));
        AppWxCashOutRecord appWxCashOutRecord = appWxCashOutRecordDao.get(new AppWxCashOutRecord(cashOutId));
        if (result_code.equals("FAIL")) {//result_code==FAIL 处理err_code
            //查询失败  记录result_code和err_code_des
            appWxCashOutRecord.setResultInfo("FAIL");
            appWxCashOutRecord.setResult(err_code_des);
            if (err_code.equals("ORDERNOTEXIST")) {
                logger.error("###订单不存在###  jsonObject={}", jsonObject);
                appWxCashOutRecord.setResultInfo("订单不存在！已返还提现金额到账户余额，请稍后重新尝试。");
                appWxCashOutRecord.setResult(err_code_des);
                //回滚用户余额 c端
                Map rollBackMap = new HashMap();
                rollBackMap.put("user_id", appWxCashOutRecord.getUserId().intValue());
                rollBackMap.put("amount", appWxCashOutRecord.getAmount());
                rollBackMap.put("cashOutId", appWxCashOutRecord.getId());
                rollBackMap.put("source", appWxCashOutRecord.getSources());
                rollBackMoney(rollBackMap);
            }
        } else {//result_code==SUCCESS 处理status
            appWxCashOutRecord.setResult(status);
            switch (status) {
                case "PROCESSING"://处理中，如有明确失败，则返回额外失败原因；否则没有错误原因
                    //处理中  不做任何处理
                    logger.error("###当前订单任在处理中### partner_trade_no={}", partner_trade_no);
                    appWxCashOutRecord.setResultInfo("处理中");
                    break;
                case "SUCCESS"://付款成功
                    logger.error("###当前订单付款成功 ### partner_trade_no={}", partner_trade_no);
                    appWxCashOutRecord.setResultInfo("提现成功");
                    appWxCashOutRecord.setSuccessTime(pay_succ_time);
                    break;
                case "FAILED"://付款失败,需要替换付款单号重新发起付款
                    logger.error("###付款失败 ### status=FAILED; partner_trade_no ={}", partner_trade_no);
                    appWxCashOutRecord.setResultInfo("FAILED 付款失败,账户余额已退回,请稍后重新提现。");
                    map.put("cashOutId", appWxCashOutRecord.getId());
                    map.put("source", appWxCashOutRecord.getSources());
                    map.put("amount", appWxCashOutRecord.getAmount());
                    logger.error("###付款失败 ### 回滚用户余额={}", map);
                    rollBackMoney(map);//回滚用户余额
                    break;
                case "BANK_FAIL"://银行退票，订单状态由付款成功流转至退票,退票时付款金额和手续费会自动退还
                    logger.error("###银行退票 ### status=BANK_FAIL; partner_trade_no ={}", partner_trade_no);
                    appWxCashOutRecord.setResultInfo("BANK_FAIL 付款失败,账户余额已退回,请稍后重新提现。");
                    map.put("cashOutId", appWxCashOutRecord.getId());
                    map.put("source", appWxCashOutRecord.getSources());
                    map.put("amount", appWxCashOutRecord.getAmount());
                    logger.error("###银行退票 ### 回滚用户余额={}", map);
                    rollBackMoney(map);//回滚用户余额
                    break;
            }
        }

        appWxCashOutRecordDao.updateResult(appWxCashOutRecord);
    }

    /**
     * 回滚账户余额
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void rollBackMoney(Map map) {
        logger.error("##用户回滚账户余额### map={}", map);
        Object user_id = map.get("user_id");
        Object amount = map.get("amount");
        Object source = map.get("source");
        if (Integer.parseInt(String.valueOf(source)) == 1) {
            AppUser appUser = appUserDao.selectFotUpdate((Integer) user_id);
            BigDecimal newBlance = appUser.getBalance().add(new BigDecimal(String.valueOf(amount)));
            appUser.setBalance(newBlance);
            appUserDao.update(appUser);
        } else {
            AppBUser appBUser = appBUserDao.selectFotUpdate((Integer) user_id);
            BigDecimal newBlance = appBUser.getBalance().add(new BigDecimal(String.valueOf(amount)));
            appBUser.setBalance(newBlance);
            appBUserDao.update(appBUser);
        }

        AppUserAccountRecord appUserAccountRecord = new AppUserAccountRecord();
        appUserAccountRecord.setCreateTime(new Date());
        appUserAccountRecord.setAddFlag(1);
        appUserAccountRecord.setMomey(new BigDecimal(String.valueOf(amount)));
        appUserAccountRecord.setSource(Integer.parseInt(String.valueOf(source)));
        appUserAccountRecord.setBusinessId(String.valueOf(map.get("cashOutId")));
        appUserAccountRecord.setType(2);
        appUserAccountRecord.setUserId(Long.valueOf(String.valueOf(user_id)));

        appUserAccountRecordDao.insertCus(appUserAccountRecord);
    }

    /**
     * 处理失败订单 比如AMOUNT_LIMIT 超额  NOTENOUGH 账号余额不足
     *
     * @param appWxCashOutRecord
     * @param parentPartnerTradeNo
     */
//    public void opsFailOrder(AppWxCashOutRecord appWxCashOutRecord, String parentPartnerTradeNo) {
//
//
//        EntPayBankRequest request = new EntPayBankRequest();
//        request.setPartnerTradeNo(appWxCashOutRecord.getPartnerTradeNo());
//        request.setEncBankNo(appWxCashOutRecord.getBankNumber());
//        request.setEncTrueName(appWxCashOutRecord.getUserBankName());
//        request.setBankCode(appWxCashOutRecord.getBankCode());
//        request.setAmount(appWxCashOutRecord.getSendAmount() * 100);//单位分
//        request.setDescription("车己提现");
//        EntPayBankResult entPayBankResult = null;
//        try {
//            entPayBankResult = wxService.getEntPayService().payBank(request);
//        } catch (WxPayException e) {
//            e.printStackTrace();
//        }
//        if(entPayBankResult.getErrCode()){
//            appWxBankInterfaceDao.updateStatusByTradeNo(parentPartnerTradeNo);//修改失败订单的处理状态
//        }
//        write(entPayBankResult, request.toXML(), appWxCashOutRecord, parentPartnerTradeNo);//记录进数据库
//    }
}
