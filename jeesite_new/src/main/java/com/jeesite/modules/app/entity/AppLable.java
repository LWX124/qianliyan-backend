/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 标签表Entity
 * @author zcq
 * @version 2019-08-19
 */
@Table(name="app_lable", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="label", attrName="label", label="标签", isQuery=false),
		@Column(name="type", attrName="type", label="标签类型", comment="标签类型(1.商户列表标签2.商户页面标签)"),
		@Column(name="url", attrName="url", label="路径", comment="路径"),
		@Column(name="enable", attrName="enable", label="是否开通", comment="是否开通"),
		@Column(name="index", attrName="index", label="索引", comment="索引"),
	}, orderBy="a.id DESC"
)
public class AppLable extends DataEntity<AppLable> {
	
	private static final long serialVersionUID = 1L;
	private String label;		// 标签
	private Long type;		// 标签类型(1.商户列表标签2.商户页面标签)
	private String url;

	private Integer enable;

	private Integer index;

	public AppLable() {
		this(null);
	}

	public AppLable(String id){
		super(id);
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@NotBlank(message="标签不能为空")
	@Length(min=0, max=100, message="标签长度不能超过 100 个字符")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@NotNull(message="标签类型不能为空")
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AppLable{" +
				"label='" + label + '\'' +
				", type=" + type +
				", url='" + url + '\'' +
				", enable=" + enable +
				", index=" + index +
				'}';
	}
}