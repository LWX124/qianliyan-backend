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
 * 品牌和商户关联表Entity
 * @author dh
 * @version 2019-10-19
 */
@Table(name="app_merchants_brand", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="brand_id", attrName="brandId", label="汽车品牌id"),
		@Column(name="state", attrName="state", label="品牌启用状态"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
	}, orderBy="a.id DESC"
)
public class AppMerchantsBrand extends DataEntity<AppMerchantsBrand> {
	
	private static final long serialVersionUID = 1L;
	private Long userBId;		// 商户id
	private String brandId;		// 汽车品牌id
	private Integer state;		// 品牌启用状态
	private Date createTime;		// 创建时间
	
	public AppMerchantsBrand() {
		this(null);
	}

	public AppMerchantsBrand(String id){
		super(id);
	}
	
	public Long getUserBId() {
		return userBId;
	}

	public void setUserBId(Long userBId) {
		this.userBId = userBId;
	}
	
	@Length(min=0, max=20, message="汽车品牌id长度不能超过 20 个字符")
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}