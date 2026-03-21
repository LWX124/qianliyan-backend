/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日数据Entity
 * @author zcq
 * @version 2019-12-02
 */
@Table(name="app_every_mesg", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="red_envelopes", attrName="redEnvelopes", label="今日红包个数"),
		@Column(name="envelope_amount", attrName="envelopeAmount", label="今日红包金额"),
		@Column(name="today_accident", attrName="todayAccident", label="今日事故"),
		@Column(name="today_wxup", attrName="todayWxup", label="小程序上传个数"),
		@Column(name="today_appup", attrName="todayAppup", label="今日app上传"),
		@Column(name="pass_accident", attrName="passAccident", label="今日通过事故个数"),
		@Column(name="plus_enevlope_amount", attrName="plusEnevlopeAmount", label="plus会员提成金额"),
		@Column(name="plus_enevlope_count", attrName="plusEnevlopeCount", label="plus会员提成个数"),
		@Column(name = "today_time", attrName = "todayTime", label = "当天时间"),
		@Column(name = "create_time", attrName = "createTime", label = "create_time"),
	}, orderBy="a.id DESC"
)
public class AppEveryMesg extends DataEntity<AppEveryMesg> {
	
	private static final long serialVersionUID = 1L;
	private String redEnvelopes;		// 今日红包个数
	private BigDecimal envelopeAmount;		// 今日红包金额
	private String todayAccident;		// 今日事故
	private String todayWxup;		// 小程序上传个数
	private String todayAppup;		// 今日app上传
	private String passAccident;		// 今日通过事故个数
	private BigDecimal plusEnevlopeAmount;		// plus会员提成金额
	private String plusEnevlopeCount;		// plus会员提成个数
	private String todayTime;				//当天时间
	private Date createTime;

	public String getTodayTime() {
		return todayTime;
	}

	public void setTodayTime(String todayTime) {
		this.todayTime = todayTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public AppEveryMesg() {
		this(null);
	}

	public AppEveryMesg(String id){
		super(id);
	}
	
	@Length(min=0, max=20, message="今日红包个数长度不能超过 20 个字符")
	public String getRedEnvelopes() {
		return redEnvelopes;
	}

	public void setRedEnvelopes(String redEnvelopes) {
		this.redEnvelopes = redEnvelopes;
	}
	
	public BigDecimal getEnvelopeAmount() {
		return envelopeAmount;
	}

	public void setEnvelopeAmount(BigDecimal envelopeAmount) {
		this.envelopeAmount = envelopeAmount;
	}
	
	@Length(min=0, max=20, message="今日事故长度不能超过 20 个字符")
	public String getTodayAccident() {
		return todayAccident;
	}

	public void setTodayAccident(String todayAccident) {
		this.todayAccident = todayAccident;
	}
	
	@Length(min=0, max=20, message="小程序上传个数长度不能超过 20 个字符")
	public String getTodayWxup() {
		return todayWxup;
	}

	public void setTodayWxup(String todayWxup) {
		this.todayWxup = todayWxup;
	}
	
	@Length(min=0, max=20, message="今日app上传长度不能超过 20 个字符")
	public String getTodayAppup() {
		return todayAppup;
	}

	public void setTodayAppup(String todayAppup) {
		this.todayAppup = todayAppup;
	}
	
	@Length(min=0, max=20, message="今日通过事故个数长度不能超过 20 个字符")
	public String getPassAccident() {
		return passAccident;
	}

	public void setPassAccident(String passAccident) {
		this.passAccident = passAccident;
	}
	
	public BigDecimal getPlusEnevlopeAmount() {
		return plusEnevlopeAmount;
	}

	public void setPlusEnevlopeAmount(BigDecimal plusEnevlopeAmount) {
		this.plusEnevlopeAmount = plusEnevlopeAmount;
	}
	
	@Length(min=0, max=20, message="plus会员提成个数长度不能超过 20 个字符")
	public String getPlusEnevlopeCount() {
		return plusEnevlopeCount;
	}

	public void setPlusEnevlopeCount(String plusEnevlopeCount) {
		this.plusEnevlopeCount = plusEnevlopeCount;
	}


	@Override
	public String toString() {
		return "AppEveryMesg{" +
				"redEnvelopes='" + redEnvelopes + '\'' +
				", envelopeAmount=" + envelopeAmount +
				", todayAccident='" + todayAccident + '\'' +
				", todayWxup='" + todayWxup + '\'' +
				", todayAppup='" + todayAppup + '\'' +
				", passAccident='" + passAccident + '\'' +
				", plusEnevlopeAmount=" + plusEnevlopeAmount +
				", plusEnevlopeCount='" + plusEnevlopeCount + '\'' +
				", createTime=" + createTime +
				'}';
	}
}