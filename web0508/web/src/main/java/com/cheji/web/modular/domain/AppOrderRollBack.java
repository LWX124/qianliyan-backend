/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存回滚辅助表Entity
 * @author dh
 * @version 2019-12-31
 */
@TableName("app_order_roll_back")
public class AppOrderRollBack extends Model<AppOrderRollBack> {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private static final long serialVersionUID = 1L;
	private Date createTime;		// 创建时间
	private String orderId;		// 订单id
	private Integer type;		// 类型
	private Integer payFlag;		// 付款标识
	private Integer opsFlag;		// 是否处理

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

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
