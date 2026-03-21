/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 库存回滚辅助表Entity
 * @author dh
 * @version 2019-12-31
 */
@Table(name="app_order_roll_back", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="create_time", attrName="createTime", label="创建时间 "),
		@Column(name="order_id", attrName="orderId", label="订单id "),
		@Column(name="type", attrName="type", label="类型"),
		@Column(name="pay_flag", attrName="payFlag", label="付款标识"),
		@Column(name="ops_flag", attrName="opsFlag", label="是否处理"),
	}, orderBy="a.id DESC"
)
public class AppOrderRollBack extends DataEntity<AppOrderRollBack> {

	private static final long serialVersionUID = 1L;
	private Date createTime;		// 创建时间
	private String orderId;		// 订单id
	private Integer type;		// 类型
	private Integer payFlag;		// 付款标识
	private Integer opsFlag;		// 是否处理

	public AppOrderRollBack() {
		this(null);
	}

	public AppOrderRollBack(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Length(min=0, max=20, message="订单id长度不能超过 20 个字符")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}

	public Integer getOpsFlag() {
		return opsFlag;
	}

	public void setOpsFlag(Integer opsFlag) {
		this.opsFlag = opsFlag;
	}

}
