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
 * 商户合同图片表Entity
 * @author zcq
 * @version 2020-08-10
 */
@Table(name="app_b_user_contract", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="url", attrName="url", label="合同图片地址"),
		@Column(name="index", attrName="index", label="排序"),
		@Column(name="user_b_id", attrName="userBId", label="排序"),
		@Column(name="state", attrName="state", label="状态"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppBUserContract extends DataEntity<AppBUserContract> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 合同图片地址
	private Integer index;		// 排序
	private Integer userBId;		// 排序
	private Long state;		// 状态
	private Date createTime;		// create_time
	private Date updateTime;		// update_time

	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}

	public AppBUserContract() {
		this(null);
	}

	public AppBUserContract(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="合同图片地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
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