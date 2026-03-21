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
 * 图片上传商户图片表Entity
 * @author zcq
 * @version 2021-03-08
 */
@Table(name="app_photo_mer", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="url", attrName="url", label="图片地址"),
		@Column(name="index", attrName="index", label="排序"),
		@Column(name="up_id", attrName="upId", label="图片上传id"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppPhotoMer extends DataEntity<AppPhotoMer> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 图片地址
	private Integer index;		// 排序
	private Integer upId;		// 图片上传id
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppPhotoMer() {
		this(null);
	}

	public AppPhotoMer(String url, Integer index, Integer upId, Date createTime, Date updateTime) {
		this.url = url;
		this.index = index;
		this.upId = upId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public AppPhotoMer(String id){
		super(id);
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
	
	public Integer getUpId() {
		return upId;
	}

	public void setUpId(Integer upId) {
		this.upId = upId;
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
		return "AppPhotoMer{" +
				"url='" + url + '\'' +
				", index=" + index +
				", upId=" + upId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}