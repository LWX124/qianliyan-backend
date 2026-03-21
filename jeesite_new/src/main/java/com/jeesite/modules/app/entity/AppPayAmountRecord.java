/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 红包金额记录表Entity
 * @author zcq
 * @version 2019-08-30
 */
@Table(name="app_pay_amount_record", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="accid", attrName="accid", label="事故id"),
		@Column(name="user_id", attrName="userId", label="c用户id"),
		@Column(name="pay_amount", attrName="payAmount", label="支付金额"),
		@Column(name="type", attrName="type", label="金额来源"),
		@Column(name="create_time", attrName="createTime", label="支付时间", isUpdate=false, isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isUpdate=false, isQuery=false),
	},
		// 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
		joinTable = {
				@JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "this", alias = "b",
						on = "b.id = a.user_id",
						columns = {
								@Column(name = "name", attrName = "name", label = "名称"),
						})
		}, orderBy="a.id DESC"
)
public class AppPayAmountRecord extends DataEntity<AppPayAmountRecord> {
	
	private static final long serialVersionUID = 1L;
	private Long accid;		// 事故id
	private Long userId;		// 用户id
	private Double payAmount;		// 支付金额
	private String type;			//金额来源 (1.红包2.plus会员反钱)
	private Date createTime;		// 支付时间
	private Date updateTime;		// update_time
	private String name;			//用户名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppPayAmountRecord() {
		this(null);
	}

	public AppPayAmountRecord(String id){
		super(id);
	}
	
	public Long getAccid() {
		return accid;
	}

	public void setAccid(Long accid) {
		this.accid = accid;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AppPayAmountRecord{" +
				"accid=" + accid +
				", userId=" + userId +
				", payAmount=" + payAmount +
				", type='" + type + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", name='" + name + '\'' +
				'}';
	}
}