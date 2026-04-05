/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 用户信息表Entity
 * @author zcq
 * @version 2019-08-08
 */
@Table(name="app_user", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="parent_id", attrName="parentId", label="父id", isInsert=false, isQuery=false),
		@Column(name="avatar", attrName="avatar", label="头像", isQuery=false),
		@Column(name="username", attrName="username", label="账户", comment="账户(和电话一致)"),
		@Column(name="password", attrName="password", label="密码", isQuery=false),
		@Column(name="phone_number", attrName="phoneNumber", label="电话号码"),
		@Column(name="salt", attrName="salt", label="密码盐", isInsert=false, isQuery=false),
		@Column(name="name", attrName="name", label="名字", queryType=QueryType.LIKE),
		@Column(name="status", attrName="status", label="状态", comment="状态(1:启用，2:冻结，3:删除，4:注册)", isQuery=false),
		@Column(name="balance", attrName="balance", label="余额", isInsert=false, isQuery=false),
		@Column(name="balck", attrName="balck", label="是否黑名单", isInsert=false, isQuery=false),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isInsert=false, isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isQuery=false),
		@Column(name="wx_open_id", attrName="wxOpenId", label="微信openId", isInsert=false, isQuery=false),
		@Column(name="vip_lv", attrName="vipLv", label="vip等级", isInsert=false, isQuery=false),
		@Column(name="is_inner", attrName="isInner", label="是否内部人员，1是 2否", isInsert=false),
		@Column(name="source", attrName="source", label="小程序来源标识"),
	}, orderBy="a.id DESC"
)
public class AppUser extends DataEntity<AppUser> {
	
	private static final long serialVersionUID = 1L;
	private String avatar;		// 头像
	private Integer parentId;	//父id
	private String username;		// 账户(和电话一致)
	private String password;		// 密码
	private String phoneNumber;		// 电话号码
	private String salt;		// 密码盐
	private String name;		// 名字
	private String status;	//状态
	private BigDecimal balance;	//余额
	private Integer balck;			//是否黑名单（0 否  1是）
	private Date creatTime;		// creat_time
	private Date updateTime;		// update_time
	private String wxOpenId;
	private String vipLv;//vip等级
	private Integer isInner;//是否内部人员，1是 2否
	private String source;		// 小程序来源标识

	public String getVipLv() {
		return vipLv;
	}

	public void setVipLv(String vipLv) {
		this.vipLv = vipLv;
	}

	public Integer getIsInner() {
		return isInner;
	}

	public void setIsInner(Integer isInner) {
		this.isInner = isInner;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getBalck() {
		return balck;
	}

	public void setBalck(Integer balck) {
		this.balck = balck;
	}

	public AppUser() {
		this(null);
	}

	public AppUser(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="头像长度不能超过 255 个字符")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Length(min=0, max=11, message="账户长度不能超过 11 个字符")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=100, message="密码长度不能超过 45 个字符")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=20, message="电话号码长度不能超过 20 个字符")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=45, message="密码盐长度不能超过 45 个字符")
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Length(min=0, max=20, message="名字长度不能超过 20 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}
}