package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx.pay.v3")
public class WxPayV3Properties {
    /** 小程序AppID */
    private String appId;
    /** 新商户号 */
    private String mchId;
    /** APIv3密钥（32位） */
    private String apiV3Key;
    /** 商户API私钥路径（apiclient_key.pem） */
    private String privateKeyPath;
    /** 商户API证书路径（apiclient_cert.pem） */
    private String certPath;
    /** 商户证书序列号 */
    private String certSerialNo;
    /** 微信支付公钥路径 */
    private String publicKeyPath;
    /** 微信支付公钥ID */
    private String publicKeyId;
    /** 转账场景ID */
    private String transferSceneId;

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getMchId() { return mchId; }
    public void setMchId(String mchId) { this.mchId = mchId; }
    public String getApiV3Key() { return apiV3Key; }
    public void setApiV3Key(String apiV3Key) { this.apiV3Key = apiV3Key; }
    public String getPrivateKeyPath() { return privateKeyPath; }
    public void setPrivateKeyPath(String privateKeyPath) { this.privateKeyPath = privateKeyPath; }
    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public String getCertSerialNo() { return certSerialNo; }
    public void setCertSerialNo(String certSerialNo) { this.certSerialNo = certSerialNo; }
    public String getPublicKeyPath() { return publicKeyPath; }
    public void setPublicKeyPath(String publicKeyPath) { this.publicKeyPath = publicKeyPath; }
    public String getPublicKeyId() { return publicKeyId; }
    public void setPublicKeyId(String publicKeyId) { this.publicKeyId = publicKeyId; }
    public String getTransferSceneId() { return transferSceneId; }
    public void setTransferSceneId(String transferSceneId) { this.transferSceneId = transferSceneId; }
}
