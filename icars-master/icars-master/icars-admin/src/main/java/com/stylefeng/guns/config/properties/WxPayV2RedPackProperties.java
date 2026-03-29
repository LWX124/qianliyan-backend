package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信公众号现金红包配置
 */
@Component
@ConfigurationProperties(prefix = "wx.pay.redpack")
public class WxPayV2RedPackProperties {
    /** 公众号AppID */
    private String gzhAppId;
    /** 商户号 */
    private String mchId;
    /** 商户API密钥 */
    private String mchKey;
    /** 商户证书路径（apiclient_cert.p12） */
    private String certPath;
    /** 商户名称 */
    private String sendName;
    /** 服务器IP */
    private String clientIp;

    public String getGzhAppId() { return gzhAppId; }
    public void setGzhAppId(String gzhAppId) { this.gzhAppId = gzhAppId; }
    public String getMchId() { return mchId; }
    public void setMchId(String mchId) { this.mchId = mchId; }
    public String getMchKey() { return mchKey; }
    public void setMchKey(String mchKey) { this.mchKey = mchKey; }
    public String getCertPath() { return certPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public String getSendName() { return sendName; }
    public void setSendName(String sendName) { this.sendName = sendName; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
}
