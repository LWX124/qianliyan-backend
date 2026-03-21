/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * car_inforEntity
 * @author y
 * @version 2022-10-09
 */
@Table(name="car_infor", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="plate_no", attrName="plateNo", label="plate_no"),
		@Column(name="brand", attrName="brand", label="brand"),
		@Column(name="phone", attrName="phone", label="phone"),
	}, orderBy="a.id DESC"
)
public class CarInfor extends DataEntity<CarInfor> {
	
	private static final long serialVersionUID = 1L;
	private String plateNo;		// plate_no
	private String brand;		// brand
	private String phone;		// phone
	
	public CarInfor() {
		this(null);
	}

	public CarInfor(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="plate_no长度不能超过 255 个字符")
	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	
	@Length(min=0, max=255, message="brand长度不能超过 255 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=255, message="phone长度不能超过 255 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}