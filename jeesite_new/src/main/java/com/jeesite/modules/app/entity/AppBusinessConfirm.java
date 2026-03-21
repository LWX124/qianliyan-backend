/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 商户业务确认表Entity
 * @author zcq
 * @version 2020-07-09
 */
@Table(name="app_business_confirm", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="business_confirm", attrName="businessConfirm", label="业务确认"),
		@Column(name="accident_reponsibility", attrName="accidentReponsibility", label="事故责任"),
		@Column(name="customers_have", attrName="customersHave", label="已联客户"),
		@Column(name="not_united_customer", attrName="notUnitedCustomer", label="未联客户"),
		@Column(name="charter_shop", attrName="charterShop", label="包车到店"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppBusinessConfirm extends DataEntity<AppBusinessConfirm> {
	
	private static final long serialVersionUID = 1L;
	private Integer userBId;		// 商户id
	private String businessConfirm;		// 业务确认
	private String accidentReponsibility;		// 事故责任
	private BigDecimal customersHave;		// 已联客户
	private BigDecimal notUnitedCustomer;		// 未联客户
	private BigDecimal charterShop;		// 包车到店
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppBusinessConfirm() {
		this(null);
	}

	public AppBusinessConfirm(String id){
		super(id);
	}
	
	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}
	
	@Length(min=0, max=255, message="业务确认长度不能超过 255 个字符")
	public String getBusinessConfirm() {
		return businessConfirm;
	}

	public void setBusinessConfirm(String businessConfirm) {
		this.businessConfirm = businessConfirm;
	}
	
	@Length(min=0, max=255, message="事故责任长度不能超过 255 个字符")
	public String getAccidentReponsibility() {
		return accidentReponsibility;
	}

	public void setAccidentReponsibility(String accidentReponsibility) {
		this.accidentReponsibility = accidentReponsibility;
	}
	
	public BigDecimal getCustomersHave() {
		return customersHave;
	}

	public void setCustomersHave(BigDecimal customersHave) {
		this.customersHave = customersHave;
	}
	
	public BigDecimal getNotUnitedCustomer() {
		return notUnitedCustomer;
	}

	public void setNotUnitedCustomer(BigDecimal notUnitedCustomer) {
		this.notUnitedCustomer = notUnitedCustomer;
	}
	
	public BigDecimal getCharterShop() {
		return charterShop;
	}

	public void setCharterShop(BigDecimal charterShop) {
		this.charterShop = charterShop;
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