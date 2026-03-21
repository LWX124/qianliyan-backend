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
 * 订单抽成记录表Entity
 * @author zcq
 * @version 2019-10-21
 */
@Table(name="app_our_indent_amount", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="indent_id", attrName="indentId", label="订单id"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="user_id", attrName="userId", label="C用户id"),
		@Column(name="rebates", attrName="rebates", label="比例"),
		@Column(name="fixloss_user", attrName="fixlossUser", label="结算给用户金额"),
		@Column(name="amount", attrName="amount", label="金额"),
		@Column(name="order_number", attrName="orderNumber", label="订单号"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppOurIndentAmount extends DataEntity<AppOurIndentAmount> {
	
	private static final long serialVersionUID = 1L;
	private Long indentId;		// 订单id
	private Long userBId;		// 商户id
	private Long userId;		// C用户id
	private BigDecimal rebates;		// 比例
	private BigDecimal fixlossUser;		// 结算给用户金额
	private BigDecimal amount;		// 金额
	private String orderNumber;		// 订单号
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppOurIndentAmount() {
		this(null);
	}

	public AppOurIndentAmount(String id){
		super(id);
	}
	
	public Long getIndentId() {
		return indentId;
	}

	public void setIndentId(Long indentId) {
		this.indentId = indentId;
	}
	
	public Long getUserBId() {
		return userBId;
	}

	public void setUserBId(Long userBId) {
		this.userBId = userBId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public BigDecimal getRebates() {
		return rebates;
	}

	public void setRebates(BigDecimal rebates) {
		this.rebates = rebates;
	}
	
	public BigDecimal getFixlossUser() {
		return fixlossUser;
	}

	public void setFixlossUser(BigDecimal fixlossUser) {
		this.fixlossUser = fixlossUser;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Length(min=0, max=100, message="订单号长度不能超过 100 个字符")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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