/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 加盟信息表Entity
 * @author zcq
 * @version 2019-08-01
 */
@Table(name="app_league", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="shop_name", attrName="shopName", label="店铺名字", queryType=QueryType.LIKE),
		@Column(name="brand", attrName="brand", label="品牌", isQuery=false),
		@Column(name="address", attrName="address", label="店铺详细地址"),
		@Column(name="name", attrName="name", label="加盟人姓名", queryType=QueryType.LIKE),
		@Column(name="phone_number", attrName="phoneNumber", label="加盟人电话"),
		@Column(name="city_name", attrName="cityName", label="城市名字", queryType=QueryType.LIKE),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isUpdate=false, isQuery=false),
	}, orderBy="a.id DESC"
)
public class AppLeague extends DataEntity<AppLeague> {
	
	private static final long serialVersionUID = 1L;
	private String shopName;		// 店铺名字
	private String brand;		// 品牌
	private String address;		// 店铺详细地址
	private String name;		// 加盟人姓名
	private String phoneNumber;		// 加盟人电话
	private String cityName;		// 城市名字
	private Date creatTime;		// creat_time
	private Date updateTime;

	// update_time
	private Integer indentId;

	private Integer value;

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public AppLeague() {
		this(null);
	}

	public AppLeague(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="店铺名字长度不能超过 255 个字符")
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	@Length(min=0, max=255, message="品牌长度不能超过 255 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=255, message="店铺详细地址长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@NotBlank(message="加盟人姓名不能为空")
	@Length(min=0, max=20, message="加盟人姓名长度不能超过 20 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotBlank(message="加盟人电话不能为空")
	@Length(min=0, max=11, message="加盟人电话长度不能超过 11 个字符")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=20, message="城市名字长度不能超过 20 个字符")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="creat_time不能为空")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="update_time不能为空")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	@Override
	public String toString() {
		return "AppLeague{" +
				"shopName='" + shopName + '\'' +
				", brand='" + brand + '\'' +
				", address='" + address + '\'' +
				", name='" + name + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", cityName='" + cityName + '\'' +
				", creatTime=" + creatTime +
				", updateTime=" + updateTime +
				", indentId=" + indentId +
				", value=" + value +
				'}';
	}
}