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
 * 退款Entity
 * @author dh
 * @version 2019-12-16
 */
@Table(name="app_wxpay_back", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="out_refund_no", attrName="outRefundNo", label="商户退款单号"),
		@Column(name="out_trade_no", attrName="outTradeNo", label="商户订单号"),
		@Column(name="back_status", attrName="backStatus", label="1 默认  2 成功  3 失败"),
		@Column(name="refund_id", attrName="refundId", label="微信退款单号"),
		@Column(name="refund_fee", attrName="refundFee", label="退款金额"),
		@Column(name="total_fee", attrName="totalFee", label="订单金额"),
		@Column(name="success_time", attrName="successTime", label="退款成功时间"),
		@Column(name="refund_recv_accout", attrName="refundRecvAccout", label="退款入账账户"),
		@Column(name="type", attrName="type", label="业务类型  1：洗车"),
		@Column(name="business_id", attrName="businessId", label="业务id"),
		@Column(name="refund_account", attrName="refundAccount", label="退款资金来源 REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户，REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款"),
	}, orderBy="a.id DESC"
)
public class AppWxpayBack extends DataEntity<AppWxpayBack> {

	private static final long serialVersionUID = 1L;
	private String outRefundNo;		// 商户退款单号
	private String outTradeNo;		// 商户订单号
	private String backStatus;		// 1 默认  2 成功  3 失败
	private String refundId;		// 微信退款单号
	private Integer refundFee;		// 退款金额
	private Integer totalFee;		// 订单金额
	private Integer type;		// 业务类型
	private String businessId;		// 业务id
	private String successTime;		// 退款成功时间
	private String refundRecvAccout;		// 退款入账账户
	private String refundAccount;		// 退款资金来源 REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户，REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public AppWxpayBack() {
		this(null);
	}

	public AppWxpayBack(String id){
		super(id);
	}

	@Length(min=0, max=255, message="商户退款单号长度不能超过 255 个字符")
	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	@Length(min=0, max=255, message="商户订单号长度不能超过 255 个字符")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(String backStatus) {
		this.backStatus = backStatus;
	}

	@Length(min=0, max=255, message="微信退款单号长度不能超过 255 个字符")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}

	@Length(min=0, max=255, message="退款入账账户长度不能超过 255 个字符")
	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}

	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}

	@Length(min=0, max=255, message="退款资金来源 REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户，REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款长度不能超过 255 个字符")
	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

}
