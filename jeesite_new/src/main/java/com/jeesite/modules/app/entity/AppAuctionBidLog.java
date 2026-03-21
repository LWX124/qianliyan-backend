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
 * 出价记录Entity
 * @author y
 * @version 2023-01-05
 */
@Table(name="app_auction_bid_log", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="出价用户id"),
		@Column(name="car_id", attrName="carId", label="出价车辆id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="bid", attrName="bid", label="出的价格"),
		@Column(name="valid", attrName="valid", label="是否有效,0有效,1过时无效"),
	}, joinTable = {
		@JoinTable(type = Type.LEFT_JOIN, entity = AppAuction.class, attrName = "appAuction", alias = "o",
				on = "o.id = a.car_id", columns = {
				@Column(name = "brand", label = "型号", isQuery = true, queryType = QueryType.LIKE),
				@Column(name = "plate_no", label = "车牌", isQuery = true, queryType = QueryType.LIKE),
				@Column(name = "price", label = "起拍价", isQuery = true, queryType = QueryType.EQ),
				@Column(name = "car_state", label = "状态", isQuery = true, queryType = QueryType.EQ),
				@Column(name = "up_state", label = "上架状态", isQuery = true, queryType = QueryType.EQ),
				@Column(name = "fixed_price", label = "是否一口价", isQuery = true, queryType = QueryType.EQ),
				@Column(name = "id", label = "id", isPK = true, isQuery = true, queryType = QueryType.LIKE),
		}),
		@JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "appUser", alias = "i",
				on = "i.id = a.user_id", columns = {
				@Column(name = "username", label = "账户", isQuery = true, queryType = QueryType.LIKE)
		}),
}, orderBy = "a.create_time DESC"
)
public class AppAuctionBidLog extends DataEntity<AppAuctionBidLog> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 出价用户id
	@JsonSerialize(using = ToStringSerializer.class)//解决long类型到前端精度丢失
	private Long carId;		// 出价车辆id
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private Integer bid;		// 出的价格
	private Integer valid;		// 是否有效,0有效,1过时无效

	private AppAuction appAuction;

	private AppUser appUser;

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public AppAuction getAppAuction() {
		return appAuction;
	}

	public void setAppAuction(AppAuction appAuction) {
		this.appAuction = appAuction;
	}

	public AppAuctionBidLog() {
		this(null);
	}

	public AppAuctionBidLog(String id){
		super(id);
	}
	
	@NotNull(message="出价用户id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@NotNull(message="出价车辆id不能为空")
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
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
	
	@NotNull(message="出的价格不能为空")
	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}
	
	@Length(min=0, max=10, message="是否有效,0有效,1过时无效长度不能超过 10 个字符")
	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
}