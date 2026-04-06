package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

	private Subscribe subscribe = new Subscribe();

	/**
	 * 多源小程序配置，key 为 source 标识（如 SSP、SOURCE_B 等）
	 * 配置示例：
	 * wx.sources.SSP.appId=wx...
	 * wx.sources.SSP.secret=...
	 * wx.sources.SSP.templateId=...
	 */
	private Map<String, SourceConfig> sources = new HashMap<>();

	public static class SourceConfig {
		private String appId;
		private String secret;
		private String templateId;

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

		public String getTemplateId() {
			return templateId;
		}

		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}
	}

	public Map<String, SourceConfig> getSources() {
		return sources;
	}

	public void setSources(Map<String, SourceConfig> sources) {
		this.sources = sources;
	}

	/**
	 * 根据 source 获取 appId，未配置则回退到默认 appId
	 */
	public String getAppIdBySource(String source) {
		if (source != null && sources.containsKey(source)) {
			return sources.get(source).getAppId();
		}
		return appId;
	}

	/**
	 * 根据 source 获取 secret，未配置则回退到默认 secret
	 */
	public String getSecretBySource(String source) {
		if (source != null && sources.containsKey(source)) {
			return sources.get(source).getSecret();
		}
		return secret;
	}

	/**
	 * 根据 source 获取 templateId，未配置则回退到默认 templateId
	 */
	public String getTemplateIdBySource(String source) {
		if (source != null && sources.containsKey(source)) {
			String tid = sources.get(source).getTemplateId();
			if (tid != null && !tid.isEmpty()) {
				return tid;
			}
		}
		return subscribe.getTemplateId();
	}

	public static class Subscribe {
		private String templateId;

		public String getTemplateId() {
			return templateId;
		}

		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}
	}

	public Subscribe getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Subscribe subscribe) {
		this.subscribe = subscribe;
	}

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
