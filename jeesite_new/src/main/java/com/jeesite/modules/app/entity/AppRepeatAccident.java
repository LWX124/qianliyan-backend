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
 * 重复事故记录表Entity
 * @author zcq
 * @version 2019-11-29
 */
@Table(name="app_repeat_accident", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="accident_id", attrName="accidentId", label="上传事故id"),
		@Column(name="accident_source", attrName="accidentSource", label="事故来源", comment="事故来源(1,APP，2,小程序)"),
		@Column(name="repeat_id", attrName="repeatId", label="重复事故id"),
		@Column(name="repeat_srouce", attrName="repeatSrouce", label="重复事故来源", comment="重复事故来源(1,App，2.小程序)"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="state", attrName="state", label="状态", comment="状态(1.是重复数据，2.不是重复数据)"),
	}, orderBy="a.id DESC"
)
public class AppRepeatAccident extends DataEntity<AppRepeatAccident> {
	
	private static final long serialVersionUID = 1L;
	private String accidentId;		// 上传事故id
	private Integer accidentSource;		// 事故来源(1,APP，2,小程序)
	private String repeatId;		// 重复事故id
	private Integer repeatSrouce;		// 重复事故来源(1,App，2.小程序)
	private Date createTime;		// create_time
	private String state;		// 状态(1.是重复数据，2.不是重复数据)
	
	public AppRepeatAccident() {
		this(null);
	}

	public AppRepeatAccident(String id){
		super(id);
	}
	
	@Length(min=0, max=20, message="上传事故id长度不能超过 20 个字符")
	public String getAccidentId() {
		return accidentId;
	}

	public void setAccidentId(String accidentId) {
		this.accidentId = accidentId;
	}
	
	public Integer getAccidentSource() {
		return accidentSource;
	}

	public void setAccidentSource(Integer accidentSource) {
		this.accidentSource = accidentSource;
	}
	
	@Length(min=0, max=11, message="重复事故id长度不能超过 11 个字符")
	public String getRepeatId() {
		return repeatId;
	}

	public void setRepeatId(String repeatId) {
		this.repeatId = repeatId;
	}
	
	public Integer getRepeatSrouce() {
		return repeatSrouce;
	}

	public void setRepeatSrouce(Integer repeatSrouce) {
		this.repeatSrouce = repeatSrouce;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=20, message="状态长度不能超过 20 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}