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
 * 年检订单表Entity
 * @author zcq
 * @version 2020-04-03
 */
@Table(name="app_year_check_indent", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="year_check_number", attrName="yearCheckNumber", label="年检订单编号"),
		@Column(name="license_plate", attrName="licensePlate", label="车牌"),
		@Column(name="username", attrName="username", label="用户姓名"),
		@Column(name="phone", attrName="phone", label="电话号码"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="纬度"),
		@Column(name="pick_address", attrName="pickAddress", label="接车地点"),
		@Column(name="price", attrName="price", label="价格"),
		@Column(name="user_id", attrName="userId", label="c端用户id"),
		@Column(name="user_b_id", attrName="userBId", label="b端用户id"),
		@Column(name="state", attrName="state", label="订单状态 1.开始，2.进行中，3完成, 4.取消"),
		@Column(name="pay_state", attrName="payState", label="支付状态 0初始状态  1已支付   2支付失败  3退款"),
		@Column(name="confirm_car", attrName="confirmCar", label="是否取车"),
		@Column(name="year_check_type", attrName="yearCheckType", label="年检类型", comment="年检类型（1.免检代办，2.上线年检）"),
		@Column(name="pay_number", attrName="payNumber", label="支付订单编号"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="back_state", attrName="backState", label="back_state"),
}, orderBy="a.id DESC"
)
public class AppYearCheckIndent extends DataEntity<AppYearCheckIndent> {
	
	private static final long serialVersionUID = 1L;
	private String yearCheckNumber;		// 年检订单编号
	private String licensePlate;		// 车牌
	private String username;		// 用户姓名
	private String phone;		// 电话号码
	private Double lng;		// 经度
	private Double lat;		// 纬度
	private String pickAddress;		// 接车地点
	private Double price;		// 价格
	private Long userId;		// c端用户id
	private Integer userBId;		// b端用户id
	private Integer state;		// 订单状态 1.开始，2.进行中，3完成, 4.取消
	private Integer payState;		// 支付状态 0初始状态  1已支付   2支付失败  3退款
	private Integer confirmCar;		// 是否取车
	private Integer yearCheckType;		// 年检类型（1.免检代办，2.上线年检）
	private String payNumber;		// 支付订单编号
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private Integer backState;		//退款状态

	public Integer getBackState() {
		return backState;
	}

	public void setBackState(Integer backState) {
		this.backState = backState;
	}

	public AppYearCheckIndent() {
		this(null);
	}

	public AppYearCheckIndent(String id){
		super(id);
	}
	
	@Length(min=0, max=100, message="年检订单编号长度不能超过 100 个字符")
	public String getYearCheckNumber() {
		return yearCheckNumber;
	}

	public void setYearCheckNumber(String yearCheckNumber) {
		this.yearCheckNumber = yearCheckNumber;
	}
	
	@Length(min=0, max=20, message="车牌长度不能超过 20 个字符")
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	@Length(min=0, max=20, message="用户姓名长度不能超过 20 个字符")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=11, message="电话号码长度不能超过 11 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
	
	@Length(min=0, max=200, message="接车地点长度不能超过 200 个字符")
	public String getPickAddress() {
		return pickAddress;
	}

	public void setPickAddress(String pickAddress) {
		this.pickAddress = pickAddress;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}
	
	public Integer getConfirmCar() {
		return confirmCar;
	}

	public void setConfirmCar(Integer confirmCar) {
		this.confirmCar = confirmCar;
	}
	
	public Integer getYearCheckType() {
		return yearCheckType;
	}

	public void setYearCheckType(Integer yearCheckType) {
		this.yearCheckType = yearCheckType;
	}
	
	@Length(min=0, max=255, message="支付订单编号长度不能超过 255 个字符")
	public String getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
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