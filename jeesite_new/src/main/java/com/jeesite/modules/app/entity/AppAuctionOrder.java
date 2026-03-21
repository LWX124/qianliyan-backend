/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 拍卖订单表Entity
 * @author y
 * @version 2023-03-12
 */
@Table(name="app_auction_order", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="user_id", attrName="userId", label="买家id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="car_id", attrName="carId", label="拍卖车辆id"),
		@Column(name="order_no", attrName="orderNo", label="订单号"),
		@Column(name="order_amount", attrName="orderAmount", label="订单出价"),
		@Column(name="service_fee", attrName="serviceFee", label="手续费"),
		@Column(name="desc", attrName="desc", label="订单描述"),
		@Column(name="state", attrName="state", label="订单状态,0交易中,1交易完成"),
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
public class AppAuctionOrder extends DataEntity<AppAuctionOrder> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 买家id
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	@JsonSerialize(using = ToStringSerializer.class)//解决long类型到前端精度丢失
	private Long carId;		// 拍卖车辆id
	private String orderNo;		// 订单号
	private Double orderAmount;		// 订单出价
	private Double serviceFee;		// 服务费
	private String desc;		// 订单描述
	private Integer state;		// 订单状态,0交易中,1交易完成

	private AppUser appUser;
	private AppAuctionMessageIdentify appAuctionMessageIdentify;

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public AppAuctionMessageIdentify getAppAuctionMessageIdentify() {
		return appAuctionMessageIdentify;
	}

	public void setAppAuctionMessageIdentify(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		this.appAuctionMessageIdentify = appAuctionMessageIdentify;
	}

	public AppAuctionOrder() {
		this(null);
	}

	public AppAuctionOrder(String id){
		super(id);
	}
	
	@NotNull(message="买家id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	@NotNull(message="拍卖车辆id不能为空")
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}
	
	@Length(min=0, max=255, message="订单号长度不能超过 255 个字符")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	@Length(min=0, max=255, message="订单描述长度不能超过 255 个字符")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}