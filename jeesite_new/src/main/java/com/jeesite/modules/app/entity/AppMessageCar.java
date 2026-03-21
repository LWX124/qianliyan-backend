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
 * app_message_carEntity
 * @author zcq
 * @version 2021-09-13
 */
@Table(name="app_message_car", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="license_plate", attrName="licensePlate", label="车牌"),
		@Column(name="customer_name", attrName="customerName", label="客户姓名", queryType=QueryType.LIKE),
		@Column(name="phone", attrName="phone", label="客户电话"),
		@Column(name="local_insurance", attrName="localInsurance", label="我的保险"),
		@Column(name="other_insurance", attrName="otherInsurance", label="他车保险"),
		@Column(name="help_address", attrName="helpAddress", label="施救地址"),
		@Column(name="help_lng", attrName="helpLng", label="施救经度"),
		@Column(name="help_lat", attrName="helpLat", label="施救维度"),
		@Column(name="accident_responsibility", attrName="accidentResponsibility", label="事故责任"),
		@Column(name="maintenance_mode", attrName="maintenanceMode", label="维修方式 1.保险，2。自费，3.酒驾"),
		@Column(name="vehicle_loss", attrName="vehicleLoss", label="车辆损失 1.一般，2.严重，3.全损"),
		@Column(name="customer_intention", attrName="customerIntention", label="客户意向 ，1.维修，2.卖车，3.报废"),
		@Column(name="leave_message", attrName="leaveMessage", label="留言"),
		@Column(name="vehicle_results", attrName="vehicleResults", label="车辆结果 1.跟踪", comment="车辆结果 1.跟踪：2.失败：3.到店：4.成功"),
		@Column(name="main_phone", attrName="mainPhone", label="主车电话"),
		@Column(name="main_name", attrName="mainName", label="主车客户姓名", queryType=QueryType.LIKE),
		@Column(name="main_insurance", attrName="mainInsurance", label="主车保险"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="voice", attrName="voice", label="录音"),
		@Column(name="brand_id", attrName="brandId", label="品牌id"),
		@Column(name="acc_conditions", attrName="accConditions", label="事故情况"),
		@Column(name="casualties", attrName="casualties", label="人员伤亡"),
		@Column(name="channels_ins", attrName="channelsIns", label="保险渠道"),
		@Column(name="usually_maintain", attrName="usuallyMaintain", label="常用维修点"),
		@Column(name="buy_car", attrName="buyCar", label="客户购车"),
		@Column(name="financial_loss", attrName="financialLoss", label="财务损失"),
		@Column(name="fix_intention", attrName="fixIntention", label="维修意向"),
		@Column(name="save_costs", attrName="saveCosts", label="施救费用"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="type", attrName="type", label="信息来源 1 pb  2 sos"),
		@Column(name="mess_id", attrName="messId", label="信息id"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppMessageCar extends DataEntity<AppMessageCar> {
	
	private static final long serialVersionUID = 1L;
	private String licensePlate;		// 车牌
	private String customerName;		// 客户姓名
	private String phone;		// 客户电话
	private String localInsurance;		// 我的保险
	private String otherInsurance;		// 他车保险
	private String helpAddress;		// 施救地址
	private Double helpLng;		// 施救经度
	private Double helpLat;		// 施救维度
	private String accidentResponsibility;		// 事故责任
	private Integer maintenanceMode;		// 维修方式 1.保险，2。自费，3.酒驾
	private Integer vehicleLoss;		// 车辆损失 1.一般，2.严重，3.全损
	private Integer customerIntention;		// 客户意向 ，1.维修，2.卖车，3.报废
	private String leaveMessage;		// 留言
	private Integer vehicleResults;		// 车辆结果 1.跟踪：2.失败：3.到店：4.成功
	private String mainPhone;		// 主车电话
	private String mainName;		// 主车客户姓名
	private String mainInsurance;		// 主车保险
	private String userBId;		// 商户id
	private String voice;		// 录音
	private String brandId;		// 品牌id
	private String accConditions;		// 事故情况
	private String casualties;		// 人员伤亡
	private String channelsIns;		// 保险渠道
	private String usuallyMaintain;		// 常用维修点
	private String buyCar;		// 客户购车
	private String financialLoss;		// 财务损失
	private String fixIntention;		// 维修意向
	private String saveCosts;		// 施救费用
	private Date createTime;		// create_time
	private Integer type;		// 信息来源 1 pb  2 sos
	private String messId;		// 信息id
	private Date updateTime;		// update_time
	
	public AppMessageCar() {
		this(null);
	}

	public AppMessageCar(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="车牌长度不能超过 255 个字符")
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	@Length(min=0, max=255, message="客户姓名长度不能超过 255 个字符")
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Length(min=0, max=255, message="客户电话长度不能超过 255 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="我的保险长度不能超过 255 个字符")
	public String getLocalInsurance() {
		return localInsurance;
	}

	public void setLocalInsurance(String localInsurance) {
		this.localInsurance = localInsurance;
	}
	
	@Length(min=0, max=255, message="他车保险长度不能超过 255 个字符")
	public String getOtherInsurance() {
		return otherInsurance;
	}

	public void setOtherInsurance(String otherInsurance) {
		this.otherInsurance = otherInsurance;
	}
	
	@Length(min=0, max=255, message="施救地址长度不能超过 255 个字符")
	public String getHelpAddress() {
		return helpAddress;
	}

	public void setHelpAddress(String helpAddress) {
		this.helpAddress = helpAddress;
	}
	
	public Double getHelpLng() {
		return helpLng;
	}

	public void setHelpLng(Double helpLng) {
		this.helpLng = helpLng;
	}
	
	public Double getHelpLat() {
		return helpLat;
	}

	public void setHelpLat(Double helpLat) {
		this.helpLat = helpLat;
	}
	
	@Length(min=0, max=255, message="事故责任长度不能超过 255 个字符")
	public String getAccidentResponsibility() {
		return accidentResponsibility;
	}

	public void setAccidentResponsibility(String accidentResponsibility) {
		this.accidentResponsibility = accidentResponsibility;
	}
	
	public Integer getMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(Integer maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}
	
	public Integer getVehicleLoss() {
		return vehicleLoss;
	}

	public void setVehicleLoss(Integer vehicleLoss) {
		this.vehicleLoss = vehicleLoss;
	}
	
	public Integer getCustomerIntention() {
		return customerIntention;
	}

	public void setCustomerIntention(Integer customerIntention) {
		this.customerIntention = customerIntention;
	}
	
	@Length(min=0, max=255, message="留言长度不能超过 255 个字符")
	public String getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}
	
	public Integer getVehicleResults() {
		return vehicleResults;
	}

	public void setVehicleResults(Integer vehicleResults) {
		this.vehicleResults = vehicleResults;
	}
	
	@Length(min=0, max=255, message="主车电话长度不能超过 255 个字符")
	public String getMainPhone() {
		return mainPhone;
	}

	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}
	
	@Length(min=0, max=255, message="主车客户姓名长度不能超过 255 个字符")
	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}
	
	@Length(min=0, max=255, message="主车保险长度不能超过 255 个字符")
	public String getMainInsurance() {
		return mainInsurance;
	}

	public void setMainInsurance(String mainInsurance) {
		this.mainInsurance = mainInsurance;
	}
	
	public String getUserBId() {
		return userBId;
	}

	public void setUserBId(String userBId) {
		this.userBId = userBId;
	}
	
	@Length(min=0, max=255, message="录音长度不能超过 255 个字符")
	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
	
	@Length(min=0, max=20, message="品牌id长度不能超过 20 个字符")
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	@Length(min=0, max=255, message="事故情况长度不能超过 255 个字符")
	public String getAccConditions() {
		return accConditions;
	}

	public void setAccConditions(String accConditions) {
		this.accConditions = accConditions;
	}
	
	@Length(min=0, max=255, message="人员伤亡长度不能超过 255 个字符")
	public String getCasualties() {
		return casualties;
	}

	public void setCasualties(String casualties) {
		this.casualties = casualties;
	}
	
	@Length(min=0, max=255, message="保险渠道长度不能超过 255 个字符")
	public String getChannelsIns() {
		return channelsIns;
	}

	public void setChannelsIns(String channelsIns) {
		this.channelsIns = channelsIns;
	}
	
	@Length(min=0, max=255, message="常用维修点长度不能超过 255 个字符")
	public String getUsuallyMaintain() {
		return usuallyMaintain;
	}

	public void setUsuallyMaintain(String usuallyMaintain) {
		this.usuallyMaintain = usuallyMaintain;
	}
	
	@Length(min=0, max=255, message="客户购车长度不能超过 255 个字符")
	public String getBuyCar() {
		return buyCar;
	}

	public void setBuyCar(String buyCar) {
		this.buyCar = buyCar;
	}
	
	@Length(min=0, max=255, message="财务损失长度不能超过 255 个字符")
	public String getFinancialLoss() {
		return financialLoss;
	}

	public void setFinancialLoss(String financialLoss) {
		this.financialLoss = financialLoss;
	}
	
	@Length(min=0, max=255, message="维修意向长度不能超过 255 个字符")
	public String getFixIntention() {
		return fixIntention;
	}

	public void setFixIntention(String fixIntention) {
		this.fixIntention = fixIntention;
	}
	
	@Length(min=0, max=255, message="施救费用长度不能超过 255 个字符")
	public String getSaveCosts() {
		return saveCosts;
	}

	public void setSaveCosts(String saveCosts) {
		this.saveCosts = saveCosts;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=100, message="信息id长度不能超过 100 个字符")
	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}