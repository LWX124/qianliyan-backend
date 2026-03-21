/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 提醒车Entity
 * @author y
 * @version 2023-02-15
 */
@Table(name="app_auction_warn_car", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="car_id", attrName="carId", label="车辆id车辆id"),
		@Column(name="is_enabled", attrName="isEnabled", label="使用状态"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppAuctionWarnCar extends DataEntity<AppAuctionWarnCar> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private Long carId;		// 车辆id车辆id
	private Integer isEnabled;		// 使用状态
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppAuctionWarnCar() {
		this(null);
	}

	public AppAuctionWarnCar(String id){
		super(id);
	}
	
	@NotNull(message="用户id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	@NotNull(message="车辆id车辆id不能为空")
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}