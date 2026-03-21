/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 商户和标签关系表Entity
 * @author zcq
 * @version 2019-08-20
 */
@Table(name="app_merchants_lable", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="merchants_id", attrName="merchantsId", label="关联商户表", isQuery=false),
		@Column(name="lable_id", attrName="lableId", label="关联列表标签表", isUpdate=false, isQuery=false),
		@Column(name="rebates", attrName="rebates", label="返点数", isQuery=false),
		@Column(name="state", attrName="state", label="标签启用状态", comment="标签启用状态(1.启用,0.停用)"),
		@Column(name="create_time", attrName="createTime", label="create_time", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isUpdate=false, isQuery=false),
	}, orderBy="a.id DESC"
)
public class AppMerchantsLable extends DataEntity<AppMerchantsLable> {
	
	private static final long serialVersionUID = 1L;
	private Long merchantsId;		// 关联商户表
	private Long lableId;		// 关联列表标签表
	private Double rebates;		// 返点数
	private Integer state;		// 标签启用状态(1.启用,0.停用)
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppMerchantsLable() {
		this(null);
	}

	public AppMerchantsLable(String id){
		super(id);
	}
	
	@NotNull(message="关联商户表不能为空")
	public Long getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(Long merchantsId) {
		this.merchantsId = merchantsId;
	}
	
	@NotNull(message="关联列表标签表不能为空")
	public Long getLableId() {
		return lableId;
	}

	public void setLableId(Long lableId) {
		this.lableId = lableId;
	}
	
	@NotNull(message="返点数不能为空")
	public Double getRebates() {
		return rebates;
	}

	public void setRebates(Double rebates) {
		this.rebates = rebates;
	}
	
	@NotNull(message="标签启用状态不能为空")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}