/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 认证信息Entity
 * @author dh
 * @version 2023-04-22
 */
@Table(name="app_auction_message_identify", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="id_name", attrName="idName", label="身份证姓名", queryType=QueryType.LIKE),
		@Column(name="id_number", attrName="idNumber", label="身份证号"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="type", attrName="type", label="认证类型,1个人,2企业"),
		@Column(name="business_name", attrName="businessName", label="企业名称", queryType=QueryType.LIKE),
		@Column(name="business_number", attrName="businessNumber", label="企业信用代码"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="phone", attrName="phone", label="认证手机号"),
	}, orderBy="a.id DESC"
)
public class AppAuctionMessageIdentify extends DataEntity<AppAuctionMessageIdentify> {
	
	private static final long serialVersionUID = 1L;
	private String idName;		// 身份证姓名
	private String idNumber;		// 身份证号
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String type;		// 认证类型,1个人,2企业
	private String businessName;		// 企业名称
	private String businessNumber;		// 企业信用代码
	private Long userId;		// 用户id
	private String phone;		// 认证手机号
	
	public AppAuctionMessageIdentify() {
		this(null);
	}

	public AppAuctionMessageIdentify(String id){
		super(id);
	}
	
	@Length(min=0, max=20, message="身份证姓名长度不能超过 20 个字符")
	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}
	
	@Length(min=0, max=255, message="身份证号长度不能超过 255 个字符")
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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
	
	@Length(min=0, max=255, message="认证类型,1个人,2企业长度不能超过 255 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="企业名称长度不能超过 255 个字符")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	@Length(min=0, max=255, message="企业信用代码长度不能超过 255 个字符")
	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}
	
	@NotNull(message="用户id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=11, message="认证手机号长度不能超过 11 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}