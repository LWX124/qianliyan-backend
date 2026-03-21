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
 * 用户开通plus会员提成Entity
 * @author dh
 * @version 2019-09-10
 */
@Table(name="app_user_plus_royalty", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="royalty_user_id", attrName="royaltyUserId", label="拿提成的用户id"),
		@Column(name="plus_user_id", attrName="plusUserId", label="开通plus会员的id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="amount", attrName="amount", label="提成金额"),
	}, orderBy="a.id DESC"
)
public class AppUserPlusRoyalty extends DataEntity<AppUserPlusRoyalty> {
	
	private static final long serialVersionUID = 1L;
	private Long royaltyUserId;		// 拿提成的用户id
	private Long plusUserId;		// 开通plus会员的id
	private Date createTime;		// create_time
	private Integer amount;		// 提成金额
	
	public AppUserPlusRoyalty() {
		this(null);
	}

	public AppUserPlusRoyalty(String id){
		super(id);
	}
	
	public Long getRoyaltyUserId() {
		return royaltyUserId;
	}

	public void setRoyaltyUserId(Long royaltyUserId) {
		this.royaltyUserId = royaltyUserId;
	}
	
	public Long getPlusUserId() {
		return plusUserId;
	}

	public void setPlusUserId(Long plusUserId) {
		this.plusUserId = plusUserId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}