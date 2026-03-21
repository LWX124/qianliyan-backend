/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.math.BigDecimal;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 推广金额记录表Entity
 * @author zcq
 * @version 2021-04-08
 */
@Table(name="app_promote_record", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="p_id", attrName="pid", label="父类id"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="money", attrName="money", label="金额"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppPromoteRecord extends DataEntity<AppPromoteRecord> {
	
	private static final long serialVersionUID = 1L;
	private Integer pid;		// 父类id
	private Integer userId;		// 用户id
	private BigDecimal money;		// 金额
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppPromoteRecord() {
		this(null);
	}

	public AppPromoteRecord(String id){
		super(id);
	}
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
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