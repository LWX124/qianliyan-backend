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
 * 激光推送表Entity
 * @author zcq
 * @version 2019-11-13
 */
@Table(name="app_jg_push", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="source", attrName="source", label="来源", comment="来源（C端，B端）"),
		@Column(name="ispush", attrName="ispush", label="是否推送", comment="是否推送（0，没有，1，有）"),
		@Column(name="type", attrName="type", label="操作类型", comment="操作类型()"),
		@Column(name="user_id", attrName="userId", label="C端用户id"),
		@Column(name="user_b_id", attrName="userBId", label="B端用户id"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
	}, orderBy="a.id DESC"
)
public class AppJgPush extends DataEntity<AppJgPush> {
	
	private static final long serialVersionUID = 1L;
	private String source;		// 来源（C端，B端）
	private String ispush;		// 是否推送（0，没有，1，有）
	private String type;		// 操作类型()
	private String userId;		// C端用户id
	private String userBId;		// B端用户id
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间

	public AppJgPush(String source, String type, String userId, String userBId) {
		this.source = source;
		this.type = type;
		this.userId = userId;
		this.userBId = userBId;
	}

	public AppJgPush() {
		this(null);
	}

	public AppJgPush(String id){
		super(id);
	}
	
	@Length(min=0, max=10, message="来源长度不能超过 10 个字符")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Length(min=0, max=1, message="是否推送长度不能超过 1 个字符")
	public String getIspush() {
		return ispush;
	}

	public void setIspush(String ispush) {
		this.ispush = ispush;
	}
	
	@Length(min=0, max=20, message="操作类型长度不能超过 20 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=20, message="C端用户id长度不能超过 20 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=20, message="B端用户id长度不能超过 20 个字符")
	public String getUserBId() {
		return userBId;
	}

	public void setUserBId(String userBId) {
		this.userBId = userBId;
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

	@Override
	public String toString() {
		return "AppJgPush{" +
				"source='" + source + '\'' +
				", ispush='" + ispush + '\'' +
				", type='" + type + '\'' +
				", userId='" + userId + '\'' +
				", userBId='" + userBId + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}