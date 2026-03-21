/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 事故车送修现场业务表Entity
 * @author y
 * @version 2022-07-18
 */
@Table(name="${_prefix}sys_send_repair", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="time", attrName="time", label="时间"),
		@Column(name="phone", attrName="phone", label="信息手机"),
		@Column(name="area", attrName="area", label="事故区域"),
		@Column(name="severity", attrName="severity", label="严重程度"),
		@Column(name="details", attrName="details", label="事故详情"),
		@Column(name="redpacket", attrName="redpacket", label="红包金额"),
		@Column(name="servicer", attrName="servicer", label="业务老师"),
		@Column(name="business_car", attrName="businessCar", label="业务人车"),
		@Column(name="peer", attrName="peer", label="有无同行"),
		@Column(name="busnumber", attrName="busnumber", label="车牌号码"),
		@Column(name="brand", attrName="brand", label="车型品牌"),
		@Column(name="cusname", attrName="cusname", label="客户姓名"),
		@Column(name="cusphone", attrName="cusphone", label="电话号码"),
		@Column(name="insure", attrName="insure", label="保险公司"),
		@Column(name="duty", attrName="duty", label="事故责任"),
		@Column(name="losses", attrName="losses", label="预估损失"),
		@Column(name="push4s", attrName="push4s", label="推修4s"),
		@Column(name="condition", attrName="condition", label="成交情况"),
		@Column(name="remark", attrName="remark", label="备注"),
		@Column(name="user_id", attrName="userId", label="操作人id"),
	}, orderBy="a.id DESC"
)
public class AppSendRepair extends DataEntity<AppSendRepair> {
	
	private static final long serialVersionUID = 1L;
	private Date time;		// 时间
	private String phone;		// 信息手机
	private String area;		// 事故区域
	private String severity;		// 严重程度
	private String details;		// 事故详情
	private String redpacket;		// 红包金额
	private String servicer;		// 业务老师
	private String businessCar;		// 业务人车
	private String peer;		// 有无同行
	private String busnumber;		// 车牌号码
	private String brand;		// 车型品牌
	private String cusname;		// 客户姓名
	private String cusphone;		// 电话号码
	private String insure;		// 保险公司
	private String duty;		// 事故责任
	private String losses;		// 预估损失
	private String push4s;		// 推修4s
	private String condition;		// 成交情况
	private String remark;		// 备注
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AppSendRepair() {
		this(null);
	}

	public AppSendRepair(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	@Length(min=0, max=20, message="信息手机长度不能超过 20 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=100, message="事故区域长度不能超过 100 个字符")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@Length(min=0, max=64, message="严重程度长度不能超过 64 个字符")
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	@Length(min=0, max=255, message="事故详情长度不能超过 255 个字符")
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	@Length(min=0, max=10, message="红包金额长度不能超过 10 个字符")
	public String getRedpacket() {
		return redpacket;
	}

	public void setRedpacket(String redpacket) {
		this.redpacket = redpacket;
	}
	
	@Length(min=0, max=20, message="业务老师长度不能超过 20 个字符")
	public String getServicer() {
		return servicer;
	}

	public void setServicer(String servicer) {
		this.servicer = servicer;
	}
	
	@Length(min=0, max=20, message="业务人车长度不能超过 20 个字符")
	public String getBusinessCar() {
		return businessCar;
	}

	public void setBusinessCar(String businessCar) {
		this.businessCar = businessCar;
	}
	
	@Length(min=0, max=10, message="有无同行长度不能超过 10 个字符")
	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}
	
	@Length(min=0, max=10, message="车牌号码长度不能超过 10 个字符")
	public String getBusnumber() {
		return busnumber;
	}

	public void setBusnumber(String busnumber) {
		this.busnumber = busnumber;
	}
	
	@Length(min=0, max=10, message="车型品牌长度不能超过 10 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=10, message="客户姓名长度不能超过 10 个字符")
	public String getCusname() {
		return cusname;
	}

	public void setCusname(String cusname) {
		this.cusname = cusname;
	}
	
	@Length(min=0, max=11, message="电话号码长度不能超过 11 个字符")
	public String getCusphone() {
		return cusphone;
	}

	public void setCusphone(String cusphone) {
		this.cusphone = cusphone;
	}
	
	@Length(min=0, max=20, message="保险公司长度不能超过 20 个字符")
	public String getInsure() {
		return insure;
	}

	public void setInsure(String insure) {
		this.insure = insure;
	}
	
	@Length(min=0, max=111, message="事故责任长度不能超过 111 个字符")
	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	@Length(min=0, max=20, message="预估损失长度不能超过 20 个字符")
	public String getLosses() {
		return losses;
	}

	public void setLosses(String losses) {
		this.losses = losses;
	}
	
	@Length(min=0, max=20, message="推修4s长度不能超过 20 个字符")
	public String getPush4s() {
		return push4s;
	}

	public void setPush4s(String push4s) {
		this.push4s = push4s;
	}
	
	@Length(min=0, max=20, message="成交情况长度不能超过 20 个字符")
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Length(min=0, max=255, message="备注长度不能超过 255 个字符")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}