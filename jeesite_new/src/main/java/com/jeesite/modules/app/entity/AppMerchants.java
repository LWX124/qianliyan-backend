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

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 商户信息表Entity
 * @author zcq
 * @version 2019-08-05
 */
@Table(name="app_merchants", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="name", attrName="name", label="商户名字", queryType=QueryType.LIKE),
		@Column(name="service_score", attrName="serviceScore", label="服务态度分", isQuery=false),
		@Column(name="efficiency_score", attrName="efficiencyScore", label="效率分", isQuery=false),
		@Column(name="score", attrName="score", label="综合分数", isInsert=false),
		@Column(name="rebates", attrName="rebates", label="返点数", isQuery=false),
		@Column(name="lat", attrName="lat", label="纬度", isInsert=false, isQuery=false),
		@Column(name="lng", attrName="lng", label="经度", isInsert=false, isQuery=false),
		@Column(name="address", attrName="address", label="地址信息", isQuery=false),
		@Column(name="phone_number", attrName="phoneNumber", label="商户电话"),
		@Column(name="state", attrName="state", label="状态", comment="状态(0:未审核,1:已通过,2:未通过)", isInsert=false),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="reason", attrName="reason", label="不通过原因", isInsert=false, isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isUpdate=false, isQuery=false),
	}, // 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
		/*joinTable={
				@JoinTable(type=Type.LEFT_JOIN, entity=AppMerchantsInfoBanner.class, attrName="this", alias="b",
						on="b.merchants_id = a.id",
						columns={
								@Column(name="url", attrName="url", label="图片地址"),
								@Column(name ="index",attrName = "index",label = "排序")
						}),
		},*/ orderBy="a.id DESC"
)
public class AppMerchants extends DataEntity<AppMerchants> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 商户名字
	private Integer serviceScore;		// 服务态度分
	private Integer efficiencyScore;		// 效率分
	private Integer score;		// 综合分数
	private Double rebates;		// 返点数
	private Long lat;		// 纬度
	private Long lng;		// 经度
	private String address;		// 地址信息
	private String phoneNumber;		// 商户电话
	private String state;		// 状态(0:未审核,1:已通过,2:未通过)
	private Date creatTime;		// creat_time
	private String reason;		// 不通过原因
	private Date updateTime;		// update_time
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public AppMerchants() {
		this(null);
	}

	public AppMerchants(String id){
		super(id);
	}
	
	@Length(min=0, max=100, message="商户名字长度不能超过 100 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}
	
	public Integer getEfficiencyScore() {
		return efficiencyScore;
	}

	public void setEfficiencyScore(Integer efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	

	public Double getRebates() {
		return rebates;
	}

	public void setRebates(Double rebates) {
		this.rebates = rebates;
	}
	
	public Long getLat() {
		return lat;
	}

	public void setLat(Long lat) {
		this.lat = lat;
	}
	
	public Long getLng() {
		return lng;
	}

	public void setLng(Long lng) {
		this.lng = lng;
	}
	
	@NotBlank(message="地址信息不能为空")
	@Length(min=0, max=255, message="地址信息长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@NotBlank(message="商户电话不能为空")
	@Length(min=0, max=20, message="商户电话长度不能超过 20 个字符")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=20, message="状态长度不能超过 20 个字符")
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
	
	@Length(min=0, max=100, message="不通过原因长度不能超过 100 个字符")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
		return "AppMerchants{" +
				"name='" + name + '\'' +
				", serviceScore=" + serviceScore +
				", efficiencyScore=" + efficiencyScore +
				", score=" + score +
				", rebates=" + rebates +
				", lat=" + lat +
				", lng=" + lng +
				", address='" + address + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", state='" + state + '\'' +
				", creatTime=" + creatTime +
				", reason='" + reason + '\'' +
				", updateTime=" + updateTime +
				", url='" + url + '\'' +
				'}';
	}
}