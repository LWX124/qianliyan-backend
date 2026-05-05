package com.stylefeng.guns.wxpay.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.stylefeng.guns.config.properties.WxPayProperties;
import com.stylefeng.guns.config.properties.WxPayV2RedPackProperties;
import com.stylefeng.guns.mail.IMailService;
import com.stylefeng.guns.modular.system.constant.BizAlipayBillStatus;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.*;
import com.stylefeng.guns.wxpay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;

@Service
public class WxPayBizService implements IWxPayBizService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private WxPayV2RedPackProperties redPackProperties;

    @Autowired
    IBizWxpayBillService bizWxpayBillService;

    @Autowired
    IBizWxPayRecordService bizWxPayRecordService;

    @Autowired
    IBizNotifyMailService bizNotifyMailService;

    @Autowired
    @Qualifier("textMailService")
    private IMailService mailService;

    @Resource
    private IBizWxUserService bizWxUserService;

    @Resource
    private IBizWxUserGzhService bizWxUserGzhService;

    /**
     * 企业付款到微信用户零钱
     *
     * @param openid 微信用户对应小程序的openid
     * @param amount 付款金额
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Map<String, String> mmpayMktTransfers(String openid, String amount, String partnerTradeNo) throws Exception {
        MyConfig myConfig = new MyConfig(wxPayProperties.getAppid(), wxPayProperties.getMchid(), wxPayProperties.getKey(), wxPayProperties.getCertPath());
        WXPay wxpay = new WXPay(myConfig);
        Map<String, String> data = new HashMap<String, String>();
        data.put("partner_trade_no", partnerTradeNo);
        data.put("openid", openid);
        data.put("check_name", "NO_CHECK");
        data.put("amount", amount);
        data.put("desc", "车己信息提供奖励红包");
        data.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress().toString());
        log.info("微信企业付款到零钱api，请求：" + JSON.toJSONString(data));
        Map<String, String> resp = wxpay.qyPay(data);
        log.info("微信企业付款到零钱api，返回：" + JSON.toJSONString(resp));
        return resp;
    }

    /**
     * 付款到红包
     *
     * @param openid
     * @param amount
     * @param partnerTradeNo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Map<String, String> wxPayRedBag(String accId, String openid, String amount, String partnerTradeNo) throws Exception {
        //查询到公众号openid
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openid, null);
        //根据小程序的unionid查询到公众号的openid
        BizWxUserGzh wxUserGzh = bizWxUserGzhService.selectOne(new EntityWrapper<BizWxUserGzh>().eq("unionid", bizWxUser.getUnionId()));
        String gzhOpenid = wxUserGzh.getOpenid();
        MyConfig myConfig = new MyConfig(redPackProperties.getGzhAppId(), redPackProperties.getMchId(), redPackProperties.getMchKey(), redPackProperties.getCertPath());
        WXPay wxpay = new WXPay(myConfig);
        Map<String, String> data = new HashMap<String, String>();
        data.put("act_name","提报事故领红包活动");
        data.put("client_ip", redPackProperties.getClientIp());
        data.put("mch_billno", accId);
        data.put("mch_id", redPackProperties.getMchId());
        data.put("nonce_str", partnerTradeNo);
        data.put("re_openid", gzhOpenid);
        data.put("remark","参与多多，红包多多");
        data.put("send_name", redPackProperties.getSendName());
        data.put("total_amount",amount);
        data.put("total_num","1");
        data.put("wishing","感谢您参加提报事故，为城市做一份贡献");
        data.put("wxappid", redPackProperties.getGzhAppId());
        String sign = WXPayUtil.generateSignature(data, redPackProperties.getMchKey(), WXPayConstants.SignType.MD5);
        data.put("sign", sign);
        log.info("付款到红包，请求：" + JSON.toJSONString(data));
        Map<String, String> resp = wxpay.qyWxRedBagPay(data);
        log.info("付款到红包，返回：" + JSON.toJSONString(resp));
        return resp;
    }


    @Override
    @Transactional
    public Map<String, String> gettransferinfo(String partnerTradeNo) throws Exception {
        MyConfig myConfig = new MyConfig(wxPayProperties.getAppid(), wxPayProperties.getMchid(), wxPayProperties.getKey(), wxPayProperties.getCertPath());
        WXPay wxpay = new WXPay(myConfig);
        Map<String, String> data = new HashMap<String, String>();
        data.put("partner_trade_no", partnerTradeNo);
        log.info("微信企业付款到零钱查询api，请求：" + JSON.toJSONString(data));
        Map<String, String> resp = wxpay.qyPayQuery(data);
        log.info("微信企业付款到零钱查询api，返回：" + JSON.toJSONString(resp));
        return resp;
    }

    @Override
    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        MyConfig myConfig = new MyConfig(wxPayProperties.getAppid(), wxPayProperties.getMchid(), wxPayProperties.getKey(), wxPayProperties.getCertPath());
        WXPay wxpay = new WXPay(myConfig);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "爱车士充值中心-预存款充值");
        data.put("out_trade_no", reqData.get("out_trade_no"));
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", reqData.get("total_fee"));//单位为分
        data.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress().toString());
        data.put("notify_url", wxPayProperties.getNotifyUrl());

        if (reqData.containsKey("payType")) {
            if (reqData.get("payType").equals("APP")) {
                data.put("trade_type", "APP");  // 此处指定为公众号支付

            } else {
                data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
                data.put("openid", reqData.get("openid"));
            }
        } else {
            data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
            data.put("openid", reqData.get("openid"));
        }


        log.info("微信支付统一下单api，请求：" + JSON.toJSONString(data));
        Map<String, String> resp = wxpay.unifiedOrder(data);
        log.info("微信支付统一下单api，返回：" + JSON.toJSONString(resp));
        return resp;
    }

    @Override
    @Transactional
    public boolean autoTrigger(String openid, Integer accid, String amount) {
        BizWxpayBill bizWxpayBill = bizWxpayBillService.selectOneByAccid(accid);
        if (bizWxpayBill != null && bizWxpayBill.getStatus() == 0) {
            log.info("支付微信红包->事故id:" + accid + ",已支付，无法重复支付");
            return false;
        }
        try {
            String partnerTradeNo = WXPayUtil.generateNonceStr();
//            Map<String, String> resp = this.mmpayMktTransfers(openid, amount, partnerTradeNo); 该api已经过期
            Map<String, String> resp = this.wxPayRedBag(accid.toString(),openid, amount, partnerTradeNo);

            if (WXPayConstants.SUCCESS.equals(resp.get("return_code"))) {
                if (WXPayConstants.SUCCESS.equals(resp.get("result_code"))) {
                    doSuccess(bizWxpayBill, resp, accid, amount, openid);
                    return true;
                } else {
                    //业务返回码失败，调用查询接口再次确认该交易是否成功
                    Map<String, String> var1 = gettransferinfo(partnerTradeNo);
                    if (WXPayConstants.SUCCESS.equals(var1.get("return_code"))) {
                        if (WXPayConstants.SUCCESS.equals(var1.get("result_code"))) {
                            if (WXPayConstants.SUCCESS.equals(var1.get("status"))) {
                                doSuccess(bizWxpayBill, var1, accid, amount, openid);
                                return true;
                            } else {
                                doFail(bizWxpayBill, resp, accid, amount, openid, partnerTradeNo);
                                return false;
                            }
                        } else {
                            doFail(bizWxpayBill, resp, accid, amount, openid, partnerTradeNo);
                            return false;
                        }
                    } else {
                        doFail(bizWxpayBill, resp, accid, amount, openid, partnerTradeNo);
                        return false;
                    }
                }
            } else {
                doFail(bizWxpayBill, resp, accid, amount, openid, partnerTradeNo);
                return false;
            }
        } catch (Exception e) {
            log.error("支付微信红包->事故id:" + accid + ",发生异常", e);
        }
        return false;
    }

    private void copyBizWxPayRecord(BizWxPayRecord record, Map<String, String> resp) throws Exception {
        record.setReturnCode(resp.get("return_code"));
        record.setReturnMsg(resp.get("return_msg"));
        record.setMchAppid(resp.get("mch_appid"));
        record.setMchid(resp.get("mchid"));
        record.setDeviceInfo(resp.get("device_info"));
        record.setNonceStr(resp.get("nonce_str"));
        record.setResultCode(resp.get("result_code"));
        record.setErrCode(resp.get("err_code"));
        record.setErrCodeDes(resp.get("err_code_des") == null ? "" : resp.get("err_code_des"));
        record.setPartnerTradeNo(resp.get("partner_trade_no"));
        record.setPaymentNo(resp.get("payment_no"));
        record.setPaymentTime(new Date());
    }

    private void doSuccess(BizWxpayBill bizWxpayBill, Map<String, String> resp, Integer accid, String amount, String openid) throws Exception {
        if (bizWxpayBill != null) {
            bizWxpayBill.setPayTime(new Date());
            bizWxpayBill.setStatus(BizAlipayBillStatus.SUCCESS.getCode());
            bizWxpayBillService.updateById(bizWxpayBill);
        } else {
            bizWxpayBill = new BizWxpayBill();
            bizWxpayBill.setAccid(accid);
            bizWxpayBill.setCreateTime(new Date());
            bizWxpayBill.setPayTime(new Date());
            bizWxpayBill.setStatus(BizAlipayBillStatus.SUCCESS.getCode());
            bizWxpayBillService.add(bizWxpayBill);
        }
        BizWxPayRecord record = bizWxPayRecordService.selectOne(new EntityWrapper<BizWxPayRecord>().eq("bill_id", bizWxpayBill.getId()));
        if (record == null) {
            record = new BizWxPayRecord();
            record.setBillId(bizWxpayBill.getId());
            record.setCreateTime(new Date());
        }
        record.setAmount(new BigDecimal(amount).divide(new BigDecimal(100)));
        record.setOpenid(openid);
        copyBizWxPayRecord(record, resp);
        bizWxPayRecordService.insertOrUpdate(record);
    }

    private void doFail(BizWxpayBill bizWxpayBill, Map<String, String> resp, Integer accid, String amount, String openid, String partnerTradeNo) throws Exception {
        if (bizWxpayBill != null) {
            bizWxpayBill.setPayTime(new Date());
            bizWxpayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
            bizWxpayBillService.updateById(bizWxpayBill);
        } else {
            bizWxpayBill = new BizWxpayBill();
            bizWxpayBill.setAccid(accid);
            bizWxpayBill.setCreateTime(new Date());
            bizWxpayBill.setPayTime(new Date());
            bizWxpayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
            bizWxpayBillService.add(bizWxpayBill);
        }
        BizWxPayRecord record = bizWxPayRecordService.selectOne(new EntityWrapper<BizWxPayRecord>().eq("bill_id", bizWxpayBill.getId()));
        if (record == null) {
            record = new BizWxPayRecord();
            record.setBillId(bizWxpayBill.getId());
            record.setCreateTime(new Date());
        }
        record.setAmount(new BigDecimal(amount).divide(new BigDecimal(100)));
        record.setOpenid(openid);
        copyBizWxPayRecord(record, resp);
        record.setPartnerTradeNo(partnerTradeNo);
        bizWxPayRecordService.insertOrUpdate(record);

        //接口请求失败，发送邮件告知异常信息
//        List<BizNotifyMail> bizNotifyMails = bizNotifyMailService.selectList(new EntityWrapper<BizNotifyMail>().eq("mail_type", 0));
//        if (bizNotifyMails != null && bizNotifyMails.size() > 0) {
//            List<String> mails = new ArrayList<>();
//            for (BizNotifyMail var : bizNotifyMails) {
//                if (StringUtils.isNotEmpty(var.getEmail())) {
//                    mails.add(var.getEmail());
//                }
//            }
//            String[] mailArr = new String[mails.size()];
//            mails.toArray(mailArr);
//            if (mailArr.length > 0) {
//                try {
//                    mailService.sendTextMail(mailArr, "微信红包发放异常通知", JSON.toJSONString(resp));
//                } catch (MessagingException e) {
//                    log.error("发送邮件异常", e);
//                }
//            }
//        }
    }
}
