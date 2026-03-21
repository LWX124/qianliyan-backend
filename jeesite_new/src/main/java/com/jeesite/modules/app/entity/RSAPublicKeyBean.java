package com.jeesite.modules.app.entity;

import java.io.Serializable;

public class RSAPublicKeyBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**商户号*/
    private String mch_id;
    /**随机字符串*/
    private String nonce_str;
    /**签名*/
    private String sign;
    /**签名类型*/
    private String sign_type;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
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

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}