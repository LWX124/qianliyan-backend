package com.jeesite.modules.app.entity;

import java.io.Serializable;

public class TransferBank implements Serializable {
    private static final long serialVersionUID = 1L;
    /**商户号*/
    private String mch_id;
    /**商户订单号*/
    private String partner_trade_no;
    /**随机字符串*/
    private String nonce_str;
    /**签名*/
    private String sign;
    /**收款方银行卡号*/
    private String enc_bank_no;
    /**收款方用户名*/
    private String enc_true_name;
    /**收款方开户行代号 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4*/
    private String bank_code;
    /**付款金额 单位分*/
    private int amount;
    /**付款说明，即备注（非必填）*/
    private String desc;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getEnc_bank_no() {
        return enc_bank_no;
    }

    public void setEnc_bank_no(String enc_bank_no) {
        this.enc_bank_no = enc_bank_no;
    }

    public String getEnc_true_name() {
        return enc_true_name;
    }

    public void setEnc_true_name(String enc_true_name) {
        this.enc_true_name = enc_true_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}