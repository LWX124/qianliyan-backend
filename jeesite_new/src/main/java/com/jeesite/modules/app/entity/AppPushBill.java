/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 用户扣费记录表Entity
 * @author zcq
 * @version 2019-10-24
 */
@Table(name="app_push_bill", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="accid", attrName="accid", label="事故id"),
		@Column(name="user_id", attrName="userId", label="b端用户id"),
		@Column(name="deduction", attrName="deduction", label="事故来源"),
		@Column(name="source", attrName="source", label="推送扣费"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="is_claim", attrName="isClaim", label="是否理赔老师"),
		@Column(name="check_time", attrName="checkTime", label="签到时间"),
		@Column(name="check_address", attrName="checkAddress", label="签到地点"),
		@Column(name="voice", attrName="vioce", label="录音"),
		@Column(name="remark", attrName="remark", label="备注"),
		@Column(name="reward", attrName="reward", label="奖励"),
		@Column(name="manager_remark", attrName="managerRemark", label="经理备注"),

}, orderBy="a.id DESC"
)
public class AppPushBill extends DataEntity<AppPushBill> {
	
	private static final long serialVersionUID = 1L;
	private Long accid;		// 事故id
	private Long userId;		// b端用户id
	private Double deduction;		// 推送扣费
	private Integer source;		//事故来源(1,app,2.小程序)
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String video;			//视频
	private String merchantsName;		//商户名字
	private Integer isClaim;
	private Date checkTime;
	private String checkAddress;
	private String voice;
	private String remark;		//备注
	private BigDecimal reward;	//奖励
	private String managerRemark;

	public String getManagerRemark() {
		return managerRemark;
	}

	public void setManagerRemark(String managerRemark) {
		this.managerRemark = managerRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	@JsonFormat(pattern = "HH:mm")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckAddress() {
		return checkAddress;
	}

	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}

	public Integer getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(Integer isClaim) {
		this.isClaim = isClaim;
	}

	public String getMerchantsName() {
		return merchantsName;
	}

	public void setMerchantsName(String merchantsName) {
		this.merchantsName = merchantsName;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public AppPushBill() {
		this(null);
	}

	public AppPushBill(String id){
		super(id);
	}
	
	@NotNull(message="事故id不能为空")
	public Long getAccid() {
		return accid;
	}

	public void setAccid(Long accid) {
		this.accid = accid;
	}
	
	@NotNull(message="b端用户id不能为空")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@NotNull(message="推送扣费不能为空")
	public Double getDeduction() {
		return deduction;
	}

	public void setDeduction(Double deduction) {
		this.deduction = deduction;
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

	@Override
	public String toString() {
		return "AppPushBill{" +
				"accid=" + accid +
				", userId=" + userId +
				", deduction=" + deduction +
				", source=" + source +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", video='" + video + '\'' +
				", merchantsName='" + merchantsName + '\'' +
				'}';
	}
}