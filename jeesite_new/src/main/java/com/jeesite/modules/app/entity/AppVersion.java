/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * app_versionEntity
 * @author dh
 * @version 2019-11-18
 */
@Table(name="app_version", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="version_num_min", attrName="versionNumMin", label="最小版本号"),
		@Column(name="version_num_max", attrName="versionNumMax", label="最大版本号"),
		@Column(name="version_name", attrName="versionName", label="版本名字", queryType=QueryType.LIKE),
		@Column(name="info", attrName="info", label="更新内容"),
		@Column(name="href", attrName="href", label="下载地址"),
		@Column(name="type", attrName="type", label="类型"),
	}, orderBy="a.id DESC"
)
public class AppVersion extends DataEntity<AppVersion> {
	
	private static final long serialVersionUID = 1L;
	private Integer versionNumMin;		// 最小版本号
	private Integer versionNumMax;		// 最大版本号
	private String versionName;		// 版本名字
	private String info;		// 更新内容
	private String href;		// 下载地址
	private Integer type;		// 类型
	
	public AppVersion() {
		this(null);
	}

	public AppVersion(String id){
		super(id);
	}
	
	public Integer getVersionNumMin() {
		return versionNumMin;
	}

	public void setVersionNumMin(Integer versionNumMin) {
		this.versionNumMin = versionNumMin;
	}
	
	public Integer getVersionNumMax() {
		return versionNumMax;
	}

	public void setVersionNumMax(Integer versionNumMax) {
		this.versionNumMax = versionNumMax;
	}
	
	@Length(min=0, max=30, message="版本名字长度不能超过 30 个字符")
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	@Length(min=0, max=255, message="更新内容长度不能超过 255 个字符")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Length(min=0, max=255, message="下载地址长度不能超过 255 个字符")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}