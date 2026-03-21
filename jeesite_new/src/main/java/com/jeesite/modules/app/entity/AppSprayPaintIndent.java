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
 * 喷漆订单表Entity
 * @author zcq
 * @version 2020-03-24
 */
@Table(name="app_spray_paint_indent", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="spray_paint_number", attrName="sprayPaintNumber", label="喷漆订单号"),
		@Column(name="license_plate", attrName="licensePlate", label="车牌"),
		@Column(name="brand_type", attrName="brandType", label="车辆品牌类型"),
		@Column(name="remark", attrName="remark", label="备注"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="纬度"),
		@Column(name="pick_address", attrName="pickAddress", label="接车地址"),
		@Column(name="price", attrName="price", label="价格"),
		@Column(name="is_offer", attrName="isOffer", label="是否报价"),
		@Column(name="state", attrName="state", label="订单状态 1.开始，2.进行中，3完成, 4.取消"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="is_evaluation", attrName="isEvaluation", label="是否评论"),
		@Column(name="pay_state", attrName="payState", label="支付状态 0初始状态  1已支付   2支付失败  3退款"),
		@Column(name="insurance", attrName="insurance", label="有无保险"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="pay_number", attrName="payNumber", label="支付订单编号"),
		@Column(name="technician_id", attrName="technicianId", label="技师id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppSprayPaintIndent extends DataEntity<AppSprayPaintIndent> {
	
	private static final long serialVersionUID = 1L;
	private String sprayPaintNumber;		// 喷漆订单号
	private String licensePlate;		// 车牌
	private String brandType;		// 车辆品牌类型
	private String remark;		// 备注
	private Double lng;		// 经度
	private Double lat;		// 纬度
	private String pickAddress;		// 接车地址
	private Double price;		// 价格
	private Integer isOffer;		// 是否报价
	private Integer state;		// 订单状态 1.开始，2.进行中，3完成, 4.取消
	private Long userId;		// 用户id
	private Long userBId;		// 商户id
	private Long isEvaluation;		// 是否评论
	private Integer payState;		// 支付状态 0初始状态  1已支付   2支付失败  3退款
	private Integer insurance;		// 有无保险
	private String phone;		// 电话
	private String payNumber;		// 支付订单编号
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private Integer technicianId;


	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public AppSprayPaintIndent() {
		this(null);
	}

	public AppSprayPaintIndent(String id){
		super(id);
	}
	
	@Length(min=0, max=200, message="喷漆订单号长度不能超过 200 个字符")
	public String getSprayPaintNumber() {
		return sprayPaintNumber;
	}

	public void setSprayPaintNumber(String sprayPaintNumber) {
		this.sprayPaintNumber = sprayPaintNumber;
	}
	
	@Length(min=0, max=20, message="车牌长度不能超过 20 个字符")
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	@Length(min=0, max=50, message="车辆品牌类型长度不能超过 50 个字符")
	public String getBrandType() {
		return brandType;
	}

	public void setBrandType(String brandType) {
		this.brandType = brandType;
	}
	
	@Length(min=0, max=255, message="备注长度不能超过 255 个字符")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	@Length(min=0, max=200, message="接车地址长度不能超过 200 个字符")
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
	
	public Integer getIsOffer() {
		return isOffer;
	}

	public void setIsOffer(Integer isOffer) {
		this.isOffer = isOffer;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserBId() {
		return userBId;
	}

	public void setUserBId(Long userBId) {
		this.userBId = userBId;
	}
	
	public Long getIsEvaluation() {
		return isEvaluation;
	}

	public void setIsEvaluation(Long isEvaluation) {
		this.isEvaluation = isEvaluation;
	}
	
	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}
	
	public Integer getInsurance() {
		return insurance;
	}

	public void setInsurance(Integer insurance) {
		this.insurance = insurance;
	}
	
	@Length(min=0, max=12, message="电话长度不能超过 12 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=50, message="支付订单编号长度不能超过 50 个字符")
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