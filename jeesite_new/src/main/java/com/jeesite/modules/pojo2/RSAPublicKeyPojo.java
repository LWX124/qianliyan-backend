package com.jeesite.modules.pojo2;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class RSAPublicKeyPojo {

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    @XStreamAlias("sign")
    private String sign;

    @XStreamAlias("sign_type")
    private String signType;

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
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
