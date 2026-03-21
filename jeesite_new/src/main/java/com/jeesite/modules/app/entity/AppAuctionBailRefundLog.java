/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
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
 * 单车保证金退款记录Entity
 * @author y
 * @version 2023-03-19
 */
@Table(name="app_auction_bail_refund_log", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="out_trade_no", attrName="outTradeNo", label="支付订单号"),
		@Column(name="out_refund_no", attrName="outRefundNo", label="退款订单号"),
		@Column(name="amount", attrName="amount", label="支付金额.单位", comment="支付金额.单位:分,实际金额除100"),
		@Column(name="state", attrName="state", label="支付状态", comment="支付状态：0初始状态 1退款成功 2退款关闭 3退款处理中 4退款异常"),
		@Column(name="user_id", attrName="userId", label="user_id"),
		@Column(name="prepay_id", attrName="prepayId", label="预支付id"),
		@Column(name="notify_time", attrName="notifyTime", label="支付通知生成时间"),
		@Column(name="create_time", attrName="createTime", label="订单生成时间"),
		@Column(name="car_id", attrName="carId", label="支付保证金车辆的id"),
		@Column(name="explain", attrName="explain", label="退款失败原因"),
		@Column(name="refund_id", attrName="refundId", label="微信退款单号"),
		@Column(name="back_status", attrName="backStatus", label="退款状态"),
	}, orderBy="a.id DESC"
)
public class AppAuctionBailRefundLog extends DataEntity<AppAuctionBailRefundLog> {
	
	private static final long serialVersionUID = 1L;
	private String outTradeNo;		// 支付订单号
	private String outRefundNo;		// 退款订单号
	private Double amount;		// 支付金额.单位:分,实际金额除100
	private Long userId;		// user_id
	private int state;		// 支付状态：0初始状态 1退款成功 2退款关闭 3退款处理中 4退款异常
	private String prepayId;		// 预支付id
	private Date notifyTime;		// 支付通知生成时间
	private Date createTime;		// 订单生成时间
	private Long carId;		// 支付保证金车辆的id
	private String explain;		// 退款失败原因
	private String refundId;		// 微信退款单号
	private String backStatus;		// 退款状态

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(String backStatus) {
		this.backStatus = backStatus;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public AppAuctionBailRefundLog() {
		this(null);
	}

	public AppAuctionBailRefundLog(String id){
		super(id);
	}
	
	@NotBlank(message="支付订单号不能为空")
	@Length(min=0, max=32, message="支付订单号长度不能超过 32 个字符")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	@NotBlank(message="退款订单号不能为空")
	@Length(min=0, max=32, message="退款订单号长度不能超过 32 个字符")
	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@NotNull(message="user_id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@NotBlank(message="预支付id不能为空")
	@Length(min=0, max=64, message="预支付id长度不能超过 64 个字符")
	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="订单生成时间不能为空")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}
	
	@Length(min=0, max=255, message="退款失败原因长度不能超过 255 个字符")
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	@Override
	public String toString() {
		return "AppAuctionBailRefundLog{" +
				"outTradeNo='" + outTradeNo + '\'' +
				", outRefundNo='" + outRefundNo + '\'' +
				", amount=" + amount +
				", userId=" + userId +
				", state=" + state +
				", prepayId='" + prepayId + '\'' +
				", notifyTime=" + notifyTime +
				", createTime=" + createTime +
				", carId=" + carId +
				", explain='" + explain + '\'' +
				", refundId='" + refundId + '\'' +
				", backStatus='" + backStatus + '\'' +
				'}';
	}
}