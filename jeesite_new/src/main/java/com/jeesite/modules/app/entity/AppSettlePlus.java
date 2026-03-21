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
 * 结算plus会员明细Entity
 * @author zcq
 * @version 2019-09-21
 */
@Table(name="app_settle_plus", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="settlement_amount", attrName="settlementAmount", label="结算到用户金额"),
		@Column(name="proprotion", attrName="proprotion", label="结算比例"),
		@Column(name="settle_plus_amount", attrName="settlePlusAmount", label="结算到plus会员金额"),
		@Column(name="plus_id", attrName="plusId", label="plus会员id"),
		@Column(name="indent_id", attrName="indentId", label="订单id"),
		@Column(name="inform", attrName="inform", label="通知"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppSettlePlus extends DataEntity<AppSettlePlus> {

	private static final long serialVersionUID = 1L;
	private BigDecimal settlementAmount;		// 结算到用户金额
	private BigDecimal proprotion;		// 结算比例
	private BigDecimal settlePlusAmount;		// 结算到plus会员金额
	private Long plusId;		// plus会员id
	private Long indentId;		// 订单id
	private String inform;		// 通知
	private Date createTime;		// create_time
	private Date updateTime;		// update_time

	public AppSettlePlus() {
		this(null);
	}

	public AppSettlePlus(String id){
		super(id);
	}

	public BigDecimal getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(BigDecimal settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public BigDecimal getProprotion() {
		return proprotion;
	}

	public void setProprotion(BigDecimal proprotion) {
		this.proprotion = proprotion;
	}

	public BigDecimal getSettlePlusAmount() {
		return settlePlusAmount;
	}

	public void setSettlePlusAmount(BigDecimal settlePlusAmount) {
		this.settlePlusAmount = settlePlusAmount;
	}

	public Long getPlusId() {
		return plusId;
	}

	public void setPlusId(Long plusId) {
		this.plusId = plusId;
	}

	public Long getIndentId() {
		return indentId;
	}

	public void setIndentId(Long indentId) {
		this.indentId = indentId;
	}

	@Length(min=0, max=50, message="通知长度不能超过 50 个字符")
	public String getInform() {
		return inform;
	}

	public void setInform(String inform) {
		this.inform = inform;
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