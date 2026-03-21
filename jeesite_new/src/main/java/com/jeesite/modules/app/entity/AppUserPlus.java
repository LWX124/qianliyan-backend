/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * plus会员Entity
 * @author dh
 * @version 2019-09-10
 */
@Table(name="app_user_plus", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="user_id", attrName="userId", label="user_id"),
		@Column(name="invalid_time_start", attrName="invalidTimeStart", label="有效期开始时间"),
		@Column(name="invalid_time_end", attrName="invalidTimeEnd", label="有效期结束时间"),
		@Column(name="invite_code", attrName="inviteCode", label="邀请码"),
	}, orderBy="a.id DESC"
)
public class AppUserPlus extends DataEntity<AppUserPlus> {
	
	private static final long serialVersionUID = 1L;
	private Date createTime;		// create_time
	private Long userId;		// user_id
	private Date invalidTimeStart;		// 有效期开始时间
	private Date invalidTimeEnd;		// 有效期结束时间
	private String inviteCode;		// 邀请码

	public AppUserPlus() {
		this(null);
	}

	public AppUserPlus(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInvalidTimeStart() {
		return invalidTimeStart;
	}

	public void setInvalidTimeStart(Date invalidTimeStart) {
		this.invalidTimeStart = invalidTimeStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getInvalidTimeEnd() {
		return invalidTimeEnd;
	}

	public void setInvalidTimeEnd(Date invalidTimeEnd) {
		this.invalidTimeEnd = invalidTimeEnd;
	}
	
	@Length(min=0, max=255, message="邀请码长度不能超过 255 个字符")
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
}