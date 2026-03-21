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
 * 用户服务信息表Entity
 * @author zcq
 * @version 2020-06-22
 */
@Table(name="app_user_b_message", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="serial_number", attrName="serialNumber", label="编号"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="name", attrName="name", label="名称", queryType=QueryType.LIKE),
		@Column(name="phone", attrName="phone", label="技师电话"),
		@Column(name="head_img", attrName="headImg", label="头像"),
		@Column(name="work_place", attrName="workPlace", label="工作地址"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="纬度"),
		@Column(name="trailer_plate", attrName="trailerPlate", label="拖车车牌"),
		@Column(name="price", attrName="price", label="价格/面"),
		@Column(name="score", attrName="score", label="分数"),
		@Column(name="order_number", attrName="orderNumber", label="订单个数"),
		@Column(name="introduction", attrName="introduction", label="简介"),
		@Column(name="level", attrName="level", label="工作等级"),
		@Column(name="wrok_type", attrName="wrokType", label="工作内容", comment="工作内容（1.救援，2.年检，3。喷漆，4.代驾）"),
		@Column(name="driver_year", attrName="driverYear", label="驾龄"),
		@Column(name="business_type", attrName="businessType", label="营业状态 1.开始 2未营业"),
		@Column(name="technology_year", attrName="technologyYear", label="技术年龄"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
	}, orderBy="a.id DESC"
)
public class AppUserBMessage extends DataEntity<AppUserBMessage> {
	
	private static final long serialVersionUID = 1L;
	private String serialNumber;		// 编号
	private Integer userBId;		// 商户id
	private String name;		// 名称
	private String phone;		// 技师电话
	private String headImg;		// 头像
	private String workPlace;		// 工作地址
	private Double lng;		// 经度
	private Double lat;		// 纬度
	private String trailerPlate;		// 拖车车牌
	private Double price;		// 价格/面
	private Double score;		// 分数
	private Integer orderNumber;		// 订单个数
	private String introduction;		// 简介
	private Integer level;		// 工作等级
	private Integer wrokType;		// 工作内容（1.救援，2.年检，3。喷漆，4.代驾）
	private String driverYear;		// 驾龄
	private Long businessType;		// 营业状态 1.开始 2未营业
	private String technologyYear;		// 技术年龄
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间
	
	public AppUserBMessage() {
		this(null);
	}

	public AppUserBMessage(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="编号长度不能超过 255 个字符")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}
	
	@Length(min=0, max=255, message="名称长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=20, message="技师电话长度不能超过 20 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="头像长度不能超过 255 个字符")
	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	@Length(min=0, max=255, message="工作地址长度不能超过 255 个字符")
	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	@Length(min=0, max=50, message="拖车车牌长度不能超过 50 个字符")
	public String getTrailerPlate() {
		return trailerPlate;
	}

	public void setTrailerPlate(String trailerPlate) {
		this.trailerPlate = trailerPlate;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@Length(min=0, max=255, message="简介长度不能超过 255 个字符")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getWrokType() {
		return wrokType;
	}

	public void setWrokType(Integer wrokType) {
		this.wrokType = wrokType;
	}
	
	@Length(min=0, max=255, message="驾龄长度不能超过 255 个字符")
	public String getDriverYear() {
		return driverYear;
	}

	public void setDriverYear(String driverYear) {
		this.driverYear = driverYear;
	}
	
	public Long getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Long businessType) {
		this.businessType = businessType;
	}
	
	@Length(min=0, max=255, message="技术年龄长度不能超过 255 个字符")
	public String getTechnologyYear() {
		return technologyYear;
	}

	public void setTechnologyYear(String technologyYear) {
		this.technologyYear = technologyYear;
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
	
}