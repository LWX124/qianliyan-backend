package com.jeesite.modules.pojo2;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxPayToBankPojo {

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
    /**
     * 收款方银行卡号
     */
    @XStreamAlias("enc_bank_no")
    private String encBankNo;
    /**
     * 收款方银行卡号
     */
    @XStreamAlias("enc_true_name")
    private String encTrueName;

    /**
     * 收款方开户行
     */
    @XStreamAlias("bank_code")
    private String bankCode;

    /**
     * 付款金额
     */
    @XStreamAlias("amount")
    private Integer amount;

    @Override
    public String toString() {
        return "WxPayToBankPojo{" +
                "mchId='" + mchId + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                ", encBankNo='" + encBankNo + '\'' +
                ", encTrueName='" + encTrueName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", amount='" + amount + '\'' +
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

    public String getEncBankNo() {
        return encBankNo;
    }

    public void setEncBankNo(String encBankNo) {
        this.encBankNo = encBankNo;
    }

    public String getEncTrueName() {
        return encTrueName;
    }

    public void setEncTrueName(String encTrueName) {
        this.encTrueName = encTrueName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
