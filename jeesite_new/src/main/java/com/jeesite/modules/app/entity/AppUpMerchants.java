/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.math.BigDecimal;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 通过图片上架的4s店表Entity
 * @author zcq
 * @version 2021-03-08
 */
@Table(name="app_up_merchants", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="address", attrName="address", label="地址"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="维度"),
		@Column(name="name", attrName="name", label="名字", queryType=QueryType.LIKE),
		@Column(name="brand", attrName="brand", label="品牌"),
		@Column(name="score", attrName="score", label="分数"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="adcode", attrName="adcode", label="区域代码"),
		@Column(name="citycode", attrName="citycode", label="城市代码"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="huanxin_username", attrName="huanxinUsername", label="huanxin_username"),
		@Column(name="huanxin_password", attrName="huanxinPassword", label="huanxin_password"),
	}, orderBy="a.id DESC"
)
public class AppUpMerchants extends DataEntity<AppUpMerchants> {

	private static final long serialVersionUID = 1L;
	private String address;		// 地址
	private BigDecimal lng;		// 经度
	private BigDecimal lat;		// 维度
	private String name;		// 名字
	private String brand;		// 品牌
	private Integer score;		// 分数
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String huanxinUsername;		//环信名称
	private String huanxinPassword;		//环信密码
	private String type;  //类型1，up，2.user_b
	private String phone;
	private Integer adcode;
	private Integer citycode;
	private Integer indentNumber;

	private BigDecimal money;
	private String cityname;
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public Integer getAdcode() {
		return adcode;
	}

	public void setAdcode(Integer adcode) {
		this.adcode = adcode;
	}

	public Integer getCitycode() {
		return citycode;
	}

	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Integer getIndentNumber() {
		return indentNumber;
	}

	public void setIndentNumber(Integer indentNumber) {
		this.indentNumber = indentNumber;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getHuanxinUsername() {
		return huanxinUsername;
	}

	public void setHuanxinUsername(String huanxinUsername) {
		this.huanxinUsername = huanxinUsername;
	}

	public String getHuanxinPassword() {
		return huanxinPassword;
	}

	public void setHuanxinPassword(String huanxinPassword) {
		this.huanxinPassword = huanxinPassword;
	}

	public AppUpMerchants() {
		this(null);
	}

	public AppUpMerchants(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="地址长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}
	
	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	
	@Length(min=0, max=255, message="名字长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="品牌长度不能超过 255 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
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
		return "{\"id\":\" "+ id +"\",\"lng\":\""+lng+"\",\"lat\":\""+lat+"\",\"name\":\""+name
				+"\",\"brand\":\""+brand+"\",\"adcode\":\""+adcode+"\",\"citycode\":\""+citycode+"\"}";
	}
}