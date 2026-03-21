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
 * 派单记录图片表Entity
 * @author zcq
 * @version 2020-10-15
 */
@Table(name="app_send_url", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="type", attrName="type", label="类型，1.事故图片，2.签到图片"),
		@Column(name="url", attrName="url", label="图片地址"),
		@Column(name="index", attrName="index", label="排序"),
		@Column(name="send_id", attrName="sendId", label="派单id"),
		@Column(name="source", attrName="source", label="来源  1，pushbill，2.派单sos"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
	}, orderBy="a.id DESC"
)
public class AppSendUrl extends DataEntity<AppSendUrl> {
	
	private static final long serialVersionUID = 1L;
	private Integer type;		// 类型，1.事故图片，2.签到图片
	private String url;		// 图片地址
	private Integer index;		// 排序
	private String sendId;		// 派单id
	private Integer source;
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Length(min=0, max=255, message="图片地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
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