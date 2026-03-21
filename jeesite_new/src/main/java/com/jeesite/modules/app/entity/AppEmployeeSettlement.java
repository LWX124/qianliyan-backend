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
 * 理赔老师结算表Entity
 * @author zcq
 * @version 2020-07-02
 */
@Table(name="app_employee_settlement", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="indent_id", attrName="indentId", label="订单id"),
		@Column(name="username", attrName="username", label="理赔老师名称"),
		@Column(name="amount", attrName="amount", label="结算金额"),
		@Column(name="indent_amount", attrName="indentAmount", label="订单总金额"),
		@Column(name="proportion", attrName="proportion", label="结算比例"),
		@Column(name="is_settlement", attrName="isSettlement", label="是否结算"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppEmployeeSettlement extends DataEntity<AppEmployeeSettlement> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private Long indentId;		// 订单id
	private String username;		// 理赔老师名称
	private Double amount;		// 结算金额
	private Double indentAmount;		// 订单总金额
	private Double proportion;		// 结算比例
	private Integer isSettlement;		// 是否结算
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppEmployeeSettlement() {
		this(null);
	}

	public AppEmployeeSettlement(String id){
		super(id);
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getIndentId() {
		return indentId;
	}

	public void setIndentId(Long indentId) {
		this.indentId = indentId;
	}
	
	@Length(min=0, max=255, message="理赔老师名称长度不能超过 255 个字符")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getIndentAmount() {
		return indentAmount;
	}

	public void setIndentAmount(Double indentAmount) {
		this.indentAmount = indentAmount;
	}
	
	public Double getProportion() {
		return proportion;
	}

	public void setProportion(Double proportion) {
		this.proportion = proportion;
	}
	
	public Integer getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Integer isSettlement) {
		this.isSettlement = isSettlement;
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