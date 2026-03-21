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
 * 理赔老师表Entity
 * @author zcq
 * @version 2021-05-17
 */
@Table(name="app_claim_teacher", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="关联用户表"),
		@Column(name="level", attrName="level", label="级别"),
		@Column(name="state", attrName="state", label="状态 1,正常"),
		@Column(name="type", attrName="type", label="1.理赔老师，2.4s店，3.修理厂"),
		@Column(name="on_lion", attrName="onLion", label="在线， 1在，2不在"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
		@Column(name="current_position", attrName="currentPosition", label="当前位置"),
		@Column(name="address", attrName="address", label="地址"),
		@Column(name="is_add", attrName="isAdd", label="是否添加"),
		@Column(name="brand", attrName="brand", label="品牌"),
}, orderBy="a.id DESC"
)
public class AppClaimTeacher extends DataEntity<AppClaimTeacher> {
	
	private static final long serialVersionUID = 1L;
	private Integer userId;		// 关联用户表
	private String level;		// 级别
	private Integer state;		// 状态 1,正常
	private Integer type;		// 1.理赔老师，2.4s店，3.修理厂
	private Integer onLion;		// 在线， 1在，2不在
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间
	private String currentPosition;		// 当前位置
	private String address;		// 地址
	private Integer isAdd;		// 是否添加
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public AppClaimTeacher() {
		this(null);
	}

	public AppClaimTeacher(String id){
		super(id);
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=255, message="级别长度不能超过 255 个字符")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getOnLion() {
		return onLion;
	}

	public void setOnLion(Integer onLion) {
		this.onLion = onLion;
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
	
	@Length(min=0, max=255, message="当前位置长度不能超过 255 个字符")
	public String getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	@Length(min=0, max=255, message="地址长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}
	
}