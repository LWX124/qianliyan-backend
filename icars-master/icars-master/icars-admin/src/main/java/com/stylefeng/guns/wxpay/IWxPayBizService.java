package com.stylefeng.guns.wxpay;

import java.math.BigDecimal;
import java.util.Map;

public interface IWxPayBizService {
    /**
     * 企业付款到微信用户零钱
     * @param openid 微信用户对应小程序的openid
     * @param amount 付款金额
     * @return
     * @throws Exception
     */
    Map<String, String> mmpayMktTransfers(String openid, String amount, String partnerTradeNo) throws Exception;

    Map<String, String> wxPayRedBag(String accId,String openid, String amount, String partnerTradeNo) throws Exception;

    Map<String, String> gettransferinfo(String partnerTradeNo) throws Exception;

    boolean autoTrigger(String openid, Integer accid, String amount);

    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付、小程序支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception;

}
