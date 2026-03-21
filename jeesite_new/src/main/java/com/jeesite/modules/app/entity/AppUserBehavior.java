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
 * 用户视频统计个数Entity
 * @author zcq
 * @version 2019-12-04
 */
@Table(name="app_user_behavior", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="user_id", attrName="userId", label="C端用户id或者微信openid"),
		@Column(name="user_source", attrName="userSource", label="1,C端用户。2,小程序用户"),
		@Column(name="slight", attrName="slight", label="轻微"),
		@Column(name="on_ground", attrName="onGround", label="地上拍"),
		@Column(name="near_car", attrName="nearCar", label="车边拍"),
		@Column(name="repeat", attrName="repeat", label="重复"),
		@Column(name="pass_number", attrName="passNumber", label="通过个数"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppUserBehavior extends DataEntity<AppUserBehavior> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// C端用户id或者微信openid
	private String userSource;		// 1,C端用户。2,小程序用户
	private Integer slight;		// 轻微
	private Integer onGround;		// 地上拍
	private Integer nearCar;		// 车边拍
	private Integer repeat;		// 重复
	private Integer passNumber;		// 通过个数
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String userName;		//用户名称
	private String userCome;		//用户来源
	private Integer balck;		//黑名单

	public Integer getBalck() {
		return balck;
	}

	public void setBalck(Integer balck) {
		this.balck = balck;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCome() {
		return userCome;
	}

	public void setUserCome(String userCome) {
		this.userCome = userCome;
	}

	public AppUserBehavior() {
		this(null);
	}

	public AppUserBehavior(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="C端用户id或者微信openid长度不能超过 255 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=1, message="1,C端用户。2,小程序用户长度不能超过 1 个字符")
	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}
	
	public Integer getSlight() {
		return slight;
	}

	public void setSlight(Integer slight) {
		this.slight = slight;
	}
	
	public Integer getOnGround() {
		return onGround;
	}

	public void setOnGround(Integer onGround) {
		this.onGround = onGround;
	}
	
	public Integer getNearCar() {
		return nearCar;
	}

	public void setNearCar(Integer nearCar) {
		this.nearCar = nearCar;
	}
	
	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}
	
	public Integer getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(Integer passNumber) {
		this.passNumber = passNumber;
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


	@Override
	public String toString() {
		return "AppUserBehavior{" +
				"userId='" + userId + '\'' +
				", userSource='" + userSource + '\'' +
				", slight=" + slight +
				", onGround=" + onGround +
				", nearCar=" + nearCar +
				", repeat=" + repeat +
				", passNumber=" + passNumber +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", userName='" + userName + '\'' +
				", userCome='" + userCome + '\'' +
				", balck='" + balck + '\'' +
				'}';
	}
}