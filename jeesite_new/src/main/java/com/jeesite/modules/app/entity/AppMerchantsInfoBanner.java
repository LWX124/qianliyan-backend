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
 * 商户详情banner图Entity
 * @author zcq
 * @version 2019-07-30
 */
@Table(name="app_merchants_info_banner", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="url", attrName="url", label="图片地址"),
		@Column(name="index", attrName="index", label="从小到大排序"),
		@Column(name="user_b_id", attrName="userBId", label="商户id"),
		@Column(name="creat_time", attrName="creatTime", label="creat_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppMerchantsInfoBanner extends DataEntity<AppMerchantsInfoBanner> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 图片地址
	private Long index;		// 从小到大排序
	private Integer userBId;		// 商户id
	private Date creatTime;		// creat_time
	private Date updateTime;		// update_time
	
	public AppMerchantsInfoBanner() {
		this(null);
	}

	public AppMerchantsInfoBanner(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="图片地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Integer getUserBId() {
		return userBId;
	}

	public void setUserBId(Integer userBId) {
		this.userBId = userBId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
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
		return "AppMerchantsInfoBanner{" +
				"url='" + url + '\'' +
				", index=" + index +
				", userBId=" + userBId +
				", creatTime=" + creatTime +
				", updateTime=" + updateTime +
				'}';
	}
}