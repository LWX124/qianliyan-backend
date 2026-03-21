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
 * web派单记录表Entity
 * @author zcq
 * @version 2020-09-24
 */
@Table(name="app_send_out_sheet", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="adress", attrName="adress", label="地址"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="纬度"),
		@Column(name="tc_time", attrName="tcTime", label="轨迹开始时间"),
		@Column(name="check_time", attrName="checkTime", label="签到时间"),
		@Column(name="remark", attrName="remark", label="备注"),
		@Column(name="back_text", attrName="backText", label="反馈文字"),
		@Column(name="falcon_trajectory", attrName="falconTrajectory", label="轨迹id"),
		@Column(name="tid", attrName="tid", label="设备id"),
		@Column(name="back_voice", attrName="backVoice", label="反馈音频"),
		@Column(name="voice", attrName="voice", label="音频"),
		@Column(name="state", attrName="state", label="状态"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
		@Column(name="infromation_costs", attrName="infromationCosts", label="信息费用"),
		@Column(name="access_information", attrName="accessInformation", label="信息渠道"),
		@Column(name="clinch_deal", attrName="clinchDeal", label="是否成交"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="pay_amount", attrName="payAmount", label="支付金额"),
},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppBUser.class, attrName="this", alias="b",
				on="b.id = a.user_b_id",
				columns={
						@Column(name="merchants_name", attrName="name", label="名称"),
				})
	}, orderBy="a.id DESC"
)
public class AppSendOutSheet extends DataEntity<AppSendOutSheet> {
	
	private static final long serialVersionUID = 1L;
	private Integer userBId;		// 商户id
	private String adress;		// 地址
	private BigDecimal lng;		// 经度
	private BigDecimal lat;		// 纬度
	private Date tcTime;
	private Date checkTime;		// 签到时间
	private String remark;		// 备注
	private String backVoice;		// 反馈音频
	private String backText;		//反馈文字
	private String falconTrajectory;	//轨迹id
	private String tid;					//设备id
	private String voice;		// 音频
	private Integer state;
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间
	private String name;		//名称
	private BigDecimal infromationCosts;		//信息费用
	private String accessInformation;		//信息渠道
	private Integer clinchDeal;				//是否成交
	private Integer userId;			//用户id
	private BigDecimal payAmount;

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getInfromationCosts() {
		return infromationCosts;
	}

	public void setInfromationCosts(BigDecimal infromationCosts) {
		this.infromationCosts = infromationCosts;
	}

	public String getAccessInformation() {
		return accessInformation;
	}

	public void setAccessInformation(String accessInformation) {
		this.accessInformation = accessInformation;
	}

	public Integer getClinchDeal() {
		return clinchDeal;
	}

	public void setClinchDeal(Integer clinchDeal) {
		this.clinchDeal = clinchDeal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTcTime() {
		return tcTime;
	}

	public void setTcTime(Date tcTime) {
		this.tcTime = tcTime;
	}

	public String getBackText() {
		return backText;
	}

	public void setBackText(String backText) {
		this.backText = backText;
	}

	public String getFalconTrajectory() {
		return falconTrajectory;
	}

	public void setFalconTrajectory(String falconTrajectory) {
		this.falconTrajectory = falconTrajectory;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public AppSendOutSheet() {
		this(null);
	}

	public AppSendOutSheet(String id){
		super(id);
	}

	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}

	@Length(min=0, max=255, message="地址长度不能超过 255 个字符")
	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Length(min=0, max=255, message="备注长度不能超过 255 个字符")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Length(min=0, max=255, message="反馈音频长度不能超过 255 个字符")
	public String getBackVoice() {
		return backVoice;
	}

	public void setBackVoice(String backVoice) {
		this.backVoice = backVoice;
	}

	@Length(min=0, max=255, message="音频长度不能超过 255 个字符")
	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
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