/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 保险和商户关联表Entity
 * @author zcq
 * @version 2020-08-11
 */
@Table(name="app_insurance_merchants", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="insurance_id", attrName="insuranceId", label="保险公司id"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="state", attrName="state", label="启用状态", comment="启用状态(1.启用，2.不启用)"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppInsuranceMerchants extends DataEntity<AppInsuranceMerchants> {
	
	private static final long serialVersionUID = 1L;
	private Integer insuranceId;		// 保险公司id
	private Integer userBId;		// 商户id
	private Integer state;		// 启用状态(1.启用，2.不启用)
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppInsuranceMerchants() {
		this(null);
	}

	public AppInsuranceMerchants(String id){
		super(id);
	}
	
	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}
	
	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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