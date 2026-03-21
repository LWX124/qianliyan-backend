/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 洗车订单表Entity
 * @author dh
 * @version 2019-12-17
 */
@Table(name="app_clean_indet", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="clean_indent_number", attrName="cleanIndentNumber", label="洗车订单编号"),
		@Column(name="user_b_id", attrName="userBId", label="b端商户id"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="car_type", attrName="carType", label="车型"),
		@Column(name="clean_type", attrName="cleanType", label="清洗类型"),
		@Column(name="original_price", attrName="originalPrice", label="原价"),
		@Column(name="amount", attrName="amount", label="支付金额，也就是优惠价格"),
		@Column(name="merchants_pay_number", attrName="merchantsPayNumber", label="支付订单编号"),
		@Column(name="pay_state", attrName="payState", label="支付状态"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
		@Column(name="bussiness_id", attrName="bussinessId", label="业务id"),
		@Column(name="indent_state", attrName="indentState", label="业务id"),
	}, orderBy="a.id DESC"
)
public class AppCleanIndet extends DataEntity<AppCleanIndet> {

	private static final long serialVersionUID = 1L;
	private String cleanIndentNumber;		// 洗车订单编号
	private String userBId;		// b端商户id
	private String userId;		// 用户id
	private Integer carType;		// 车型
	private Integer cleanType;		// 清洗类型
	private Double originalPrice;		// 原价
	private Double amount;		// 支付金额，也就是优惠价格
	private String merchantsPayNumber;		// 支付订单编号
	private String payState;		// 支付状态
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间
	private String bussinessId;		// 修改时间
	private String indentState;

	public String getIndentState() {
		return indentState;
	}

	public void setIndentState(String indentState) {
		this.indentState = indentState;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public AppCleanIndet() {
		this(null);
	}

	public AppCleanIndet(String id){
		super(id);
	}

	@Length(min=0, max=50, message="洗车订单编号长度不能超过 50 个字符")
	public String getCleanIndentNumber() {
		return cleanIndentNumber;
	}

	public void setCleanIndentNumber(String cleanIndentNumber) {
		this.cleanIndentNumber = cleanIndentNumber;
	}

	@Length(min=0, max=20, message="b端商户id长度不能超过 20 个字符")
	public String getUserBId() {
		return userBId;
	}

	public void setUserBId(String userBId) {
		this.userBId = userBId;
	}

	@Length(min=0, max=20, message="用户id长度不能超过 20 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	public Integer getCleanType() {
		return cleanType;
	}

	public void setCleanType(Integer cleanType) {
		this.cleanType = cleanType;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Length(min=0, max=50, message="支付订单编号长度不能超过 50 个字符")
	public String getMerchantsPayNumber() {
		return merchantsPayNumber;
	}

	public void setMerchantsPayNumber(String merchantsPayNumber) {
		this.merchantsPayNumber = merchantsPayNumber;
	}

	@Length(min=0, max=10, message="支付状态长度不能超过 10 个字符")
	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
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
