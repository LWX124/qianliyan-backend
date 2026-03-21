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
 * 竞拍账号联系表Entity
 * @author y
 * @version 2022-12-12
 */
@Table(name="app_auction_vip_control", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="vip_lv", attrName="vipLv", label="vip等级"),
		@Column(name="amount", attrName="amount", label="签约保证金"),
		@Column(name="car_count", attrName="carCount", label="剩余参拍台次"),
		@Column(name="freeze_count", attrName="freezeCount", label="冻结台次"),
		@Column(name="offer", attrName="offer", label="每月报价台次"),
		@Column(name="state", attrName="state", label="状态,1"),
		@Column(name="creat_time", attrName="creatTime", label="开通时间"),
	}, orderBy="a.id DESC"
)
public class AppAuctionVipControl extends DataEntity<AppAuctionVipControl> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private String vipLv;		// vip等级
	private Integer amount;		// 签约保证金
	private Integer carCount;		// 剩余参拍台次
	private Integer freezeCount;		// 冻结台次
	private Integer offer;		// 每月报价台次,1

	public Integer getOffer() {
		return offer;
	}

	public void setOffer(Integer offer) {
		this.offer = offer;
	}

	private String state;		// 状态,1
	private Date creatTime;		// 开通时间
	
	public AppAuctionVipControl() {
		this(null);
	}

	public AppAuctionVipControl(String id){
		super(id);
	}
	
	@NotNull(message="用户id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@NotBlank(message="vip等级不能为空")
	@Length(min=0, max=8, message="vip等级长度不能超过 8 个字符")
	public String getVipLv() {
		return vipLv;
	}

	public void setVipLv(String vipLv) {
		this.vipLv = vipLv;
	}
	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public Integer getCarCount() {
		return carCount;
	}

	public void setCarCount(Integer carCount) {
		this.carCount = carCount;
	}
	
	public Integer getFreezeCount() {
		return freezeCount;
	}

	public void setFreezeCount(Integer freezeCount) {
		this.freezeCount = freezeCount;
	}
	
	@Length(min=0, max=255, message="状态,1长度不能超过 255 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
}