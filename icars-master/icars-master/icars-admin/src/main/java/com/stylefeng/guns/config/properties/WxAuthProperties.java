package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = WxAuthProperties.PREFIX)
public class WxAuthProperties {
	public static final String PREFIX = "wx";
	private String appId;
	private String secret;
	private String grantType;
	private String sessionHost;

	//文件存储路径
	private String videoLocalPath;
	//文件网络访问路径
	private String videoHost;


	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getSessionHost() {
		return sessionHost;
	}

	public void setSessionHost(String sessionHost) {
		this.sessionHost = sessionHost;
	}

	public String getVideoLocalPath() {
		return videoLocalPath;
	}

	public void setVideoLocalPath(String videoLocalPath) {
		this.videoLocalPath = videoLocalPath;
	}

	public String getVideoHost() {
		return videoHost;
	}

	public void setVideoHost(String videoHost) {
		this.videoHost = videoHost;
	}
}
