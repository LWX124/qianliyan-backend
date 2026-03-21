/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

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
 * 商户清洗价格明细表Entity
 * @author zcq
 * @version 2019-12-11
 */
@Table(name="app_clean_price_detail", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="clean_type", attrName="cleanType", label="清洗类型5种"),
		@Column(name="car_type", attrName="carType", label="车型8种"),
		@Column(name="original_price", attrName="originalPrice", label="原价"),
		@Column(name="preferential_price", attrName="preferentialPrice", label="优惠价"),
		@Column(name="thrie_price", attrName="thriePrice", label="到手价"),
		@Column(name="contract_project", attrName="contractProject", label="是否合约项目"),
		@Column(name="residue_degree", attrName="residueDegree", label="剩余次数"),
		@Column(name="state", attrName="state", label="审核是否通过"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppBUser.class, attrName="this", alias="b",
				on="b.id = a.user_b_id",
				columns={
						@Column(name="merchants_name", attrName="merchantsName", label="商户名称"),
				})
}, orderBy="a.id DESC"
)
public class AppCleanPriceDetail extends DataEntity<AppCleanPriceDetail> {

	private static final long serialVersionUID = 1L;
	private Integer userBId;		// 商户id
	private Integer cleanType;		// 清洗类型5种
	private Integer carType;		// 车型8种
	private BigDecimal originalPrice;		// 原价
	private BigDecimal preferentialPrice;		// 优惠价
	private BigDecimal thriePrice;		// 到手价
	private Integer contractProject;		//是否合约项目
	private Integer residueDegree;			//剩余次数
	private Integer state;		// 审核是否通过
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间
	private String merchantsName;	//商户名称

	public String getMerchantsName() {
		return merchantsName;
	}

	public void setMerchantsName(String merchantsName) {
		this.merchantsName = merchantsName;
	}

	public Integer getContractProject() {
		return contractProject;
	}

	public void setContractProject(Integer contractProject) {
		this.contractProject = contractProject;
	}

	public Integer getResidueDegree() {
		return residueDegree;
	}

	public void setResidueDegree(Integer residueDegree) {
		this.residueDegree = residueDegree;
	}

	public AppCleanPriceDetail() {
		this(null);
	}

	public AppCleanPriceDetail(String id){
		super(id);
	}

	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}

	public Integer getCleanType() {
		return cleanType;
	}

	public void setCleanType(Integer cleanType) {
		this.cleanType = cleanType;
	}

	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getPreferentialPrice() {
		return preferentialPrice;
	}

	public void setPreferentialPrice(BigDecimal preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}

	public BigDecimal getThriePrice() {
		return thriePrice;
	}

	public void setThriePrice(BigDecimal thriePrice) {
		this.thriePrice = thriePrice;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
