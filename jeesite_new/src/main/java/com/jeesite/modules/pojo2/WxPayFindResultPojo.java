package com.jeesite.modules.pojo2;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxPayFindResultPojo {

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("partner_trade_no")
    private String partnerTradeNo;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    /**
     * 签名 必须
     */
    @XStreamAlias("sign")
    private String sign;

    @Override
    public String toString() {
        return "WxPayFindResultPojo{" +
                "mchId='" + mchId + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
