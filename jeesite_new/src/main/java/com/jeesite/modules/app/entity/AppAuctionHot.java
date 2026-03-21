/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 热点表Entity
 * @author y
 * @version 2023-01-04
 */
@Table(name="app_auction_hot", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="hits", attrName="hits", label="点击量"),
		@Column(name="car_id", attrName="carId", label="关联拍卖车id"),
		@Column(name="is_enabled", attrName="isEnabled", label="0有效,1失效"),
		@Column(name="city", attrName="city", label="车辆所在地"),
	}, orderBy="a.id DESC"
)
public class AppAuctionHot extends DataEntity<AppAuctionHot> {
	
	private static final long serialVersionUID = 1L;
	private Long hits;		// 点击量
	private Long carId;		// 关联拍卖车id
	private Integer isEnabled;		// 0有效,1失效
	private String city;		// 车辆所在地
	
	public AppAuctionHot() {
		this(null);
	}

	public AppAuctionHot(String id){
		super(id);
	}
	
	@NotNull(message="点击量不能为空")
	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}
	
	@NotNull(message="关联拍卖车id不能为空")
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long upId) {
		this.carId = carId;
	}
	
	@NotBlank(message="0有效,1失效不能为空")
	@Length(min=0, max=8, message="0有效,1失效长度不能超过 8 个字符")
	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@Length(min=0, max=255, message="车辆所在地长度不能超过 255 个字符")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}