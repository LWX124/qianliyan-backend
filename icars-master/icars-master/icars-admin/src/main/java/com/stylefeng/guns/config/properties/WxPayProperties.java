package com.stylefeng.guns.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = WxPayProperties.PREFIX)
public class WxPayProperties {
	public static final String PREFIX = "spring.wxpay";
	/**
	 * 商户平台绑定的appid
	 */
	private String appid;
	/**
	 * 商户号
	 */
	private String mchid;
	/**
	 * 商户秘钥
	 */
	private String key;
	/**
	 * 证书路径
	 */
	private String certPath;

	/**
	 * 支付回调通知地址
	 */
	@Value("${spring.wxpay.notify-url}")
	private String notifyUrl;


	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}
