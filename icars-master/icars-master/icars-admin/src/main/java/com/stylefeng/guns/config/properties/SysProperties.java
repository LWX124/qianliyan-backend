package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.profiles")
public class SysProperties {
	public final String PREFIX = "";

	private String active;
	private String wsUri;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getWsUri() {
		return wsUri;
	}

	public void setWsUri(String wsUri) {
		this.wsUri = wsUri;
	}
}
