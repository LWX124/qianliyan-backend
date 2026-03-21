/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * app_wx_bankEntity
 * @author dh
 * @version 2019-10-14
 */
@Table(name="app_wx_bank", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="bank_name", attrName="bankName", label="银行名字", queryType=QueryType.LIKE),
		@Column(name="bank_code", attrName="bankCode", label="银行id"),
		@Column(name="icon_url", attrName="iconUrl", label="icon_url"),
	}, orderBy="a.id DESC"
)
public class AppWxBank extends DataEntity<AppWxBank> {
	
	private static final long serialVersionUID = 1L;
	private String bankName;		// 银行名字
	private String bankCode;		// 银行id
	private String iconUrl;		// icon_url
	
	public AppWxBank() {
		this(null);
	}

	public AppWxBank(String id){
		super(id);
	}
	
	@Length(min=0, max=20, message="银行名字长度不能超过 20 个字符")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Length(min=0, max=10, message="银行id长度不能超过 10 个字符")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Length(min=0, max=255, message="icon_url长度不能超过 255 个字符")
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}