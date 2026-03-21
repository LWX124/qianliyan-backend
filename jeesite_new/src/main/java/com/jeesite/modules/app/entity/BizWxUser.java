/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
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
 * 微信用户信息表Entity
 * @author zcq
 * @version 2019-09-24
 */
@Table(name="biz_wx_user", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="openid", attrName="openid", label="微信用户id"),
		@Column(name="type", attrName="type", label="用户来源0", comment="用户来源0：公众号，1：小程序"),
		@Column(name="alipay_account", attrName="alipayAccount", label="支付宝账户"),
		@Column(name="extensionaccount", attrName="extensionaccount", label="推广员工账号"),
		@Column(name="phone", attrName="phone", label="手机号"),
		@Column(name="blacklist", attrName="blacklist", label="是否黑名单", comment="是否黑名单（0 否  1是）"),
		@Column(name="headimg", attrName="headimg", label="微信头像链接"),
		@Column(name="wxname", attrName="wxname", label="微信昵称 兼容颜文字"),
		@Column(name="bindaccount", attrName="bindaccount", label="绑定的4S门店登陆账号"),
		@Column(name="createtime", attrName="createtime", label="上报时间"),
		@Column(name="version", attrName="version", label="保留字段"),
	}, orderBy="a.id DESC"
)
public class BizWxUser extends DataEntity<BizWxUser> {
	
	private static final long serialVersionUID = 1L;
	private String openid;		// 微信用户id
	private Integer type;		// 用户来源0：公众号，1：小程序
	private String alipayAccount;		// 支付宝账户
	private String extensionaccount;		// 推广员工账号
	private String phone;		// 手机号
	private Integer blacklist;		// 是否黑名单（0 否  1是）
	private String headimg;		// 微信头像链接
	private String wxname;		// 微信昵称 兼容颜文字
	private String bindaccount;		// 绑定的4S门店登陆账号
	private Date createtime;		// 上报时间
	private Long version;		// 保留字段
	
	public BizWxUser() {
		this(null);
	}

	public BizWxUser(String id){
		super(id);
	}
	
	@NotBlank(message="微信用户id不能为空")
	@Length(min=0, max=100, message="微信用户id长度不能超过 100 个字符")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=100, message="支付宝账户长度不能超过 100 个字符")
	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	
	@Length(min=0, max=50, message="推广员工账号长度不能超过 50 个字符")
	public String getExtensionaccount() {
		return extensionaccount;
	}

	public void setExtensionaccount(String extensionaccount) {
		this.extensionaccount = extensionaccount;
	}
	
	@Length(min=0, max=50, message="手机号长度不能超过 50 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Integer getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Integer blacklist) {
		this.blacklist = blacklist;
	}
	
	@Length(min=0, max=255, message="微信头像链接长度不能超过 255 个字符")
	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}
	
	@Length(min=0, max=255, message="微信昵称 兼容颜文字长度不能超过 255 个字符")
	public String getWxname() {
		return wxname;
	}

	public void setWxname(String wxname) {
		this.wxname = wxname;
	}
	
	@Length(min=0, max=100, message="绑定的4S门店登陆账号长度不能超过 100 个字符")
	public String getBindaccount() {
		return bindaccount;
	}

	public void setBindaccount(String bindaccount) {
		this.bindaccount = bindaccount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
}