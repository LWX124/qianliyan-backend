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
 * 代驾订单表Entity
 * @author zcq
 * @version 2020-06-23
 */
@Table(name="app_substitute_driving_indent", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="substitute_driving_number", attrName="substituteDrivingNumber", label="代驾订单编号"),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="user_b_id", attrName="userBId", label="技师id"),
		@Column(name="type", attrName="type", label="代驾类型", comment="代驾类型（1.送车代驾，2.日常代驾 ，3.包时代驾）"),
		@Column(name="start_point", attrName="startPoint", label="起点地点"),
		@Column(name="start_address", attrName="startAddress", label="起点地址"),
		@Column(name="start_lng", attrName="startLng", label="起点经度"),
		@Column(name="start_lat", attrName="startLat", label="起点纬度"),
		@Column(name="start_name", attrName="startName", label="送车人", queryType=QueryType.LIKE),
		@Column(name="start_phone", attrName="startPhone", label="送车人电话"),
		@Column(name="start_remark", attrName="startRemark", label="送车备注"),
		@Column(name="end_point", attrName="endPoint", label="终点地点"),
		@Column(name="end_address", attrName="endAddress", label="结束地址"),
		@Column(name="end_lng", attrName="endLng", label="终点经度"),
		@Column(name="end_lat", attrName="endLat", label="终点纬度"),
		@Column(name="end_name", attrName="endName", label="接车名字", queryType=QueryType.LIKE),
		@Column(name="end_phone", attrName="endPhone", label="接车人电话"),
		@Column(name="end_remark", attrName="endRemark", label="接车备注"),
		@Column(name="pick_car_time", attrName="pickCarTime", label="接车时间"),
		@Column(name="send_time", attrName="sendTime", label="送达时间"),
		@Column(name="distance", attrName="distance", label="距离"),
		@Column(name="estimate_price", attrName="estimatePrice", label="预估价格"),
		@Column(name="actual_price", attrName="actualPrice", label="实际价格"),
		@Column(name="mileage_price", attrName="mileagePrice", label="里程价格"),
		@Column(name="tolls_price", attrName="tollsPrice", label="过路费"),
		@Column(name="time_price", attrName="timePrice", label="时长费"),
		@Column(name="cancel_resource", attrName="cancelResource", label="取消来源"),
		@Column(name="cancel_reason", attrName="cancelReason", label="取消原因"),
		@Column(name="indent_state", attrName="indentState", label="订单状态 1.开始，2.进行中，3完成, 4.取消"),
		@Column(name="package_type", attrName="packageType", label="包时代驾套餐类型"),
		@Column(name="begin_time", attrName="beginTime", label="开始时间"),
		@Column(name="end_time", attrName="endTime", label="结束时间"),
		@Column(name="wait_time", attrName="waitTime", label="等待时长"),
		@Column(name="sid", attrName="sid", label="sid为终端所属service唯一编号"),
		@Column(name="tid", attrName="tid", label="tid为终端唯一编号"),
		@Column(name="trid", attrName="trid", label="trid为轨迹唯一编号"),
		@Column(name="pay_state", attrName="payState", label="支付状态 0初始状态  1已支付   2支付失败  3退款"),
		@Column(name="pay_number", attrName="payNumber", label="支付订单编号"),
		@Column(name="create_time", attrName="createTime", label="订单创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
	}, orderBy="a.id DESC"
)
public class AppSubstituteDrivingIndent extends DataEntity<AppSubstituteDrivingIndent> {
	
	private static final long serialVersionUID = 1L;
	private String substituteDrivingNumber;		// 代驾订单编号
	private Long userId;		// 用户id
	private Integer userBId;		// 技师id
	private Integer type;		// 代驾类型（1.送车代驾，2.日常代驾 ，3.包时代驾）
	private String startPoint;		// 起点地点
	private String startAddress;		// 起点地址
	private Double startLng;		// 起点经度
	private Double startLat;		// 起点纬度
	private String startName;		// 送车人
	private String startPhone;		// 送车人电话
	private String startRemark;		// 送车备注
	private String endPoint;		// 终点地点
	private String endAddress;		// 结束地址
	private Double endLng;		// 终点经度
	private Double endLat;		// 终点纬度
	private String endName;		// 接车名字
	private String endPhone;		// 接车人电话
	private String endRemark;		// 接车备注
	private Date pickCarTime;		// 接车时间
	private Date sendTime;		// 送达时间
	private Double distance;		// 距离
	private Double estimatePrice;		// 预估价格
	private Double actualPrice;		// 实际价格
	private Double mileagePrice;		// 里程价格
	private Double tollsPrice;		// 过路费
	private Double timePrice;		// 时长费
	private String cancelResource;		// 取消来源
	private String cancelReason;		// 取消原因
	private Integer indentState;		// 订单状态 1.开始，2.进行中，3完成, 4.取消
	private Integer packageType;		// 包时代驾套餐类型
	private Date beginTime;		// 开始时间
	private Date endTime;		// 结束时间
	private Long waitTime;		// 等待时长
	private String sid;		// sid为终端所属service唯一编号
	private String tid;		// tid为终端唯一编号
	private String trid;		// trid为轨迹唯一编号
	private Integer payState;		// 支付状态 0初始状态  1已支付   2支付失败  3退款
	private String payNumber;		// 支付订单编号
	private Date createTime;		// 订单创建时间
	private Date updateTime;		// 修改时间
	
	public AppSubstituteDrivingIndent() {
		this(null);
	}

	public AppSubstituteDrivingIndent(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="代驾订单编号长度不能超过 255 个字符")
	public String getSubstituteDrivingNumber() {
		return substituteDrivingNumber;
	}

	public void setSubstituteDrivingNumber(String substituteDrivingNumber) {
		this.substituteDrivingNumber = substituteDrivingNumber;
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
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="起点地点长度不能超过 255 个字符")
	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}
	
	@Length(min=0, max=255, message="起点地址长度不能超过 255 个字符")
	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	
	public Double getStartLng() {
		return startLng;
	}

	public void setStartLng(Double startLng) {
		this.startLng = startLng;
	}
	
	public Double getStartLat() {
		return startLat;
	}

	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}
	
	@Length(min=0, max=255, message="送车人长度不能超过 255 个字符")
	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}
	
	@Length(min=0, max=255, message="送车人电话长度不能超过 255 个字符")
	public String getStartPhone() {
		return startPhone;
	}

	public void setStartPhone(String startPhone) {
		this.startPhone = startPhone;
	}
	
	@Length(min=0, max=255, message="送车备注长度不能超过 255 个字符")
	public String getStartRemark() {
		return startRemark;
	}

	public void setStartRemark(String startRemark) {
		this.startRemark = startRemark;
	}
	
	@Length(min=0, max=255, message="终点地点长度不能超过 255 个字符")
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Length(min=0, max=255, message="结束地址长度不能超过 255 个字符")
	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	
	public Double getEndLng() {
		return endLng;
	}

	public void setEndLng(Double endLng) {
		this.endLng = endLng;
	}
	
	public Double getEndLat() {
		return endLat;
	}

	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}
	
	@Length(min=0, max=255, message="接车名字长度不能超过 255 个字符")
	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}
	
	@Length(min=0, max=255, message="接车人电话长度不能超过 255 个字符")
	public String getEndPhone() {
		return endPhone;
	}

	public void setEndPhone(String endPhone) {
		this.endPhone = endPhone;
	}
	
	@Length(min=0, max=255, message="接车备注长度不能超过 255 个字符")
	public String getEndRemark() {
		return endRemark;
	}

	public void setEndRemark(String endRemark) {
		this.endRemark = endRemark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPickCarTime() {
		return pickCarTime;
	}

	public void setPickCarTime(Date pickCarTime) {
		this.pickCarTime = pickCarTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	public Double getEstimatePrice() {
		return estimatePrice;
	}

	public void setEstimatePrice(Double estimatePrice) {
		this.estimatePrice = estimatePrice;
	}
	
	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}
	
	public Double getMileagePrice() {
		return mileagePrice;
	}

	public void setMileagePrice(Double mileagePrice) {
		this.mileagePrice = mileagePrice;
	}
	
	public Double getTollsPrice() {
		return tollsPrice;
	}

	public void setTollsPrice(Double tollsPrice) {
		this.tollsPrice = tollsPrice;
	}
	
	public Double getTimePrice() {
		return timePrice;
	}

	public void setTimePrice(Double timePrice) {
		this.timePrice = timePrice;
	}
	
	@Length(min=0, max=10, message="取消来源长度不能超过 10 个字符")
	public String getCancelResource() {
		return cancelResource;
	}

	public void setCancelResource(String cancelResource) {
		this.cancelResource = cancelResource;
	}
	
	@Length(min=0, max=100, message="取消原因长度不能超过 100 个字符")
	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public Integer getIndentState() {
		return indentState;
	}

	public void setIndentState(Integer indentState) {
		this.indentState = indentState;
	}
	
	public Integer getPackageType() {
		return packageType;
	}

	public void setPackageType(Integer packageType) {
		this.packageType = packageType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}
	
	@Length(min=0, max=255, message="sid为终端所属service唯一编号长度不能超过 255 个字符")
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
	@Length(min=0, max=255, message="tid为终端唯一编号长度不能超过 255 个字符")
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	
	@Length(min=0, max=255, message="trid为轨迹唯一编号长度不能超过 255 个字符")
	public String getTrid() {
		return trid;
	}

	public void setTrid(String trid) {
		this.trid = trid;
	}
	
	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
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