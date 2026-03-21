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
 * 订阅表Entity
 * @author y
 * @version 2022-12-09
 */
@Table(name="app_auction_subscription", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="rss", attrName="rss", label="订阅车型"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
	}, orderBy="a.id DESC"
)
public class AppAuctionSubscription extends DataEntity<AppAuctionSubscription> {
	
	private static final long serialVersionUID = 1L;
	private String rss;		// 订阅车型
	private Long userId;		// 用户id
	private Date createTime;		// create_time
	
	public AppAuctionSubscription() {
		this(null);
	}

	public AppAuctionSubscription(String id){
		super(id);
	}
	
	@Length(min=0, max=20, message="订阅车型长度不能超过 20 个字符")
	public String getRss() {
		return rss;
	}

	public void setRss(String rss) {
		this.rss = rss;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}