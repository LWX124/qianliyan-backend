/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
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
 * 支付记录 vip充值Entity
 * @author dh
 * @version 2023-04-05
 */
@Table(name="app_auction_pay_log", alias="a", columns={
		@Column(name="out_trade_no", attrName="outTradeNo", label="支付订单号", isPK=true),
		@Column(name="amount", attrName="amount", label="支付金额.单位", comment="支付金额.单位:分,实际金额除100"),
		@Column(name="status", attrName="status", label="支付状态", comment="支付状态：1 初始状态  2 支付成功  3 支付失败", isUpdate=false),
		@Column(name="user_id", attrName="userId", label="user_id"),
		@Column(name="prepay_id", attrName="prepayId", label="预支付id"),
		@Column(name="notify_time", attrName="notifyTime", label="支付通知生成时间"),
		@Column(name="create_time", attrName="createTime", label="订单生成时间"),
		@Column(name="type", attrName="type", label="支付类型，1 微信 2支付宝"),
		@Column(name="vip_lv", attrName="vipLv", label="开通的等级"),
		@Column(name="back_trade_no", attrName="backTradeNo", label="原路退款退款单号"),
		@Column(name="refund_id", attrName="refundId", label="原路退款id"),
		@Column(name="err_code", attrName="errCode", label="错误码"),
		@Column(name="err_code_desc", attrName="errCodeDesc", label="错误码翻译"),
		@Column(name="return_msg", attrName="returnMsg", label="返回信息"),
		@Column(name="back_status", attrName="backStatus", label="2", comment="2：成功 3：失败"),
	},
		joinTable = {
				@JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName="appUser", alias = "t2",
						on = "t2.id = a.user_id", columns={
						@Column(name="username", label="账号（手机）", isQuery=true,queryType=QueryType.LIKE),
						@Column(name="name", label="姓名", isQuery=true,queryType=QueryType.LIKE),
						@Column(name="vip_lv", label="vip等级", isQuery=true,queryType=QueryType.EQ),
						@Column(name="id", label="id",isPK = true, isQuery=true,queryType=QueryType.LIKE),
				}),
				@JoinTable(type = Type.LEFT_JOIN, entity = AppAuctionMessageIdentify.class, attrName="appAuctionMessageIdentify", alias = "t3",
						on = "t3.user_id = a.user_id", columns={
						@Column(name="id_name", label="身份证姓名", isQuery=true,queryType=QueryType.EQ),
						@Column(name="id_number", label="身份证号", isQuery=true,queryType=QueryType.EQ),
				}),
		}, orderBy = "a.create_time DESC"
)
public class AppAuctionPayLog extends DataEntity<AppAuctionPayLog> {

	private static final long serialVersionUID = 1L;
	private String outTradeNo;		// 支付订单号
	private Double amount;		// 支付金额.单位:分,实际金额除100
	private Integer userId;		// user_id
	private String prepayId;		// 预支付id
	private Date notifyTime;		// 支付通知生成时间
	private Date createTime;		// 订单生成时间
	private Integer type;		// 支付类型，1 微信 2支付宝
	private Integer vipLv;		// 开通的等级
	private String backTradeNo;		// 原路退款退款单号
	private String refundId;		// 原路退款id
	private String errCode;		// 错误码
	private String errCodeDesc;		// 错误码翻译
	private String returnMsg;		// 返回信息
	private Integer backStatus;		// 退款状态2：成功 3：失败
	private AppUser appUser;
	//实名认证
	private AppAuctionMessageIdentify appAuctionMessageIdentify;

	public AppAuctionMessageIdentify getAppAuctionMessageIdentify() {
		return appAuctionMessageIdentify;
	}

	public void setAppAuctionMessageIdentify(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		this.appAuctionMessageIdentify = appAuctionMessageIdentify;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public AppAuctionPayLog() {
		this(null);
	}

	public AppAuctionPayLog(String id){
		super(id);
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@NotNull(message="user_id不能为空")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getVipLv() {
		return vipLv;
	}

	public void setVipLv(Integer vipLv) {
		this.vipLv = vipLv;
	}

	@Length(min=0, max=100, message="原路退款退款单号长度不能超过 100 个字符")
	public String getBackTradeNo() {
		return backTradeNo;
	}

	public void setBackTradeNo(String backTradeNo) {
		this.backTradeNo = backTradeNo;
	}

	@Length(min=0, max=64, message="原路退款id长度不能超过 64 个字符")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	@Length(min=0, max=255, message="错误码长度不能超过 255 个字符")
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@Length(min=0, max=255, message="错误码翻译长度不能超过 255 个字符")
	public String getErrCodeDesc() {
		return errCodeDesc;
	}

	public void setErrCodeDesc(String errCodeDesc) {
		this.errCodeDesc = errCodeDesc;
	}

	@Length(min=0, max=255, message="返回信息长度不能超过 255 个字符")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

}