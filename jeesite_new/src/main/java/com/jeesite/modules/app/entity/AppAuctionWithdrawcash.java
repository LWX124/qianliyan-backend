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
 * 退vip提现申请Entity
 * @author dh
 * @version 2023-04-05
 */
@Table(name="app_auction_withdrawcash", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="state", attrName="state", label="提现状态,0,提现中,1,成功,2,失败,3,已申请提现"),
		@Column(name="user_bank_id", attrName="userBankId", label="用户银行卡id"),
		@Column(name="amount", attrName="amount", label="提现金额,单位分"),
		@Column(name="create_time", attrName="createTime", label="发起提现申请时间"),
		@Column(name="outcash_time", attrName="outcashTime", label="outcash_time"),
		@Column(name="allow", attrName="allow", label="是否允许提现,1是,2否"),
		@Column(name="des", attrName="des", label="描述"),
		@Column(name="vip_lv", attrName="vipLv", label="vip等级"),
		@Column(name="pay_order_no_all", attrName="payOrderNoAll", label="所有支付订单的id，用,分割"),
	}, orderBy="a.id DESC"
)
public class AppAuctionWithdrawcash extends DataEntity<AppAuctionWithdrawcash> {
	
	private static final long serialVersionUID = 1L;
	private Integer userId;		// 用户id
	private Integer state;		// 提现状态,0,提现中,1,成功,2,失败,3,用户取消
	private String userBankId;		// 用户银行卡id
	private Double amount;		// 提现金额,单位分
	private Date createTime;		// 发起提现申请时间
	private Date outcashTime;		// outcash_time
	private Integer allow;		// 是否允许提现,1是,2否
	private String des;		// 描述
	private Integer vipLv;		// vip等级
	private String payOrderNoAll;		// 所有支付订单的id，用,分割
	
	public AppAuctionWithdrawcash() {
		this(null);
	}

	public AppAuctionWithdrawcash(String id){
		super(id);
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@Length(min=0, max=64, message="用户银行卡id长度不能超过 64 个字符")
	public String getUserBankId() {
		return userBankId;
	}

	public void setUserBankId(String userBankId) {
		this.userBankId = userBankId;
	}
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOutcashTime() {
		return outcashTime;
	}

	public void setOutcashTime(Date outcashTime) {
		this.outcashTime = outcashTime;
	}
	
	public Integer getAllow() {
		return allow;
	}

	public void setAllow(Integer allow) {
		this.allow = allow;
	}
	
	@Length(min=0, max=255, message="描述长度不能超过 255 个字符")
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	
	public Integer getVipLv() {
		return vipLv;
	}

	public void setVipLv(Integer vipLv) {
		this.vipLv = vipLv;
	}
	
	@Length(min=0, max=1000, message="所有支付订单的id，用,分割长度不能超过 1000 个字符")
	public String getPayOrderNoAll() {
		return payOrderNoAll;
	}

	public void setPayOrderNoAll(String payOrderNoAll) {
		this.payOrderNoAll = payOrderNoAll;
	}

	@Override
	public String toString() {
		return "AppAuctionWithdrawcash{" +
				"userId=" + userId +
				", state=" + state +
				", userBankId='" + userBankId + '\'' +
				", amount=" + amount +
				", createTime=" + createTime +
				", outcashTime=" + outcashTime +
				", allow=" + allow +
				", des='" + des + '\'' +
				", vipLv=" + vipLv +
				", payOrderNoAll='" + payOrderNoAll + '\'' +
				'}';
	}
}