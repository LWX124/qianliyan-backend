package com.stylefeng.guns.modular.system.vo.wxmenuvo;

import java.util.Arrays;

public class ComplexMenu extends BasicButton {

	private String type;

	private String url;

	private String appid;

	private String pagepath;
	
	private BasicButton[] sub_button;
 
	public BasicButton[] getSub_button() {
		return sub_button;
	}
 
	public void setSub_button(BasicButton[] sub_button) {
		this.sub_button = sub_button;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppId() {
		return appid;
	}

	public void setAppId(String appId) {
		this.appid = appId;
	}

	public String getPagePath() {
		return pagepath;
	}

	public void setPagePath(String pagePath) {
		this.pagepath = pagePath;
	}

	@Override
	public String toString() {
		return "ComplexMenu{" +
				"type='" + type + '\'' +
				", url='" + url + '\'' +
				", appid='" + appid + '\'' +
				", pagePath='" + pagepath + '\'' +
				", sub_button=" + Arrays.toString(sub_button) +
				'}';
	}
}