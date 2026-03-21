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
 * app_car_brandEntity
 * @author zcq
 * @version 2019-10-15
 */
@Table(name="app_car_brand", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="uniacid", attrName="uniacid", label="uniacid"),
		@Column(name="parent_id", attrName="parentId", label="parent_id"),
		@Column(name="initials", attrName="initials", label="首字母"),
		@Column(name="name", attrName="name", label="name", queryType=QueryType.LIKE),
		@Column(name="pic_url", attrName="picUrl", label="pic_url"),
		@Column(name="status", attrName="status", label="1", comment="1:启用 2：禁用", isUpdate=false),
		@Column(name="sort", attrName="sort", label="sort"),
		@Column(name="is_hot", attrName="isHot", label="is_hot"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
	}, orderBy="a.id DESC"
)
public class AppCarBrand extends DataEntity<AppCarBrand> {
	
	private static final long serialVersionUID = 1L;
	private Long uniacid;		// uniacid
	private Long parentId;		// parent_id
	private String initials;		// 首字母
	private String name;		// name
	private String picUrl;		// pic_url
	private Long sort;		// sort
	private Integer isHot;		// is_hot
	private Long createTime;		// create_time
	
	public AppCarBrand() {
		this(null);
	}

	public AppCarBrand(String id){
		super(id);
	}
	
	public Long getUniacid() {
		return uniacid;
	}

	public void setUniacid(Long uniacid) {
		this.uniacid = uniacid;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Length(min=0, max=5, message="首字母长度不能超过 5 个字符")
	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	@Length(min=0, max=255, message="name长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
	
	public Integer getIsHot() {
		return isHot;
	}

	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}
	
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
}