/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 视频点赞表Entity
 * @author zcq
 * @version 2019-09-17
 */
@Table(name="app_video_thumbs", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="video_id", attrName="videoId", label="video_id"),
		@Column(name="user_id", attrName="userId", label="user_id"),
		@Column(name="status", attrName="status", label="点赞状态，0取消，1点赞"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
	}, orderBy="a.id DESC"
)
public class AppVideoThumbs extends DataEntity<AppVideoThumbs> {
	
	private static final long serialVersionUID = 1L;
	private String videoId;		// video_id
	private String userId;		// user_id
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public AppVideoThumbs(String videoId, String userId, String  status) {
		this.videoId = videoId;
		this.userId = userId;
		this.status = status;
	}

	public AppVideoThumbs(String id){
		super(id);
	}
	
	@NotBlank(message="video_id不能为空")
	@Length(min=0, max=32, message="video_id长度不能超过 32 个字符")
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	@NotBlank(message="user_id不能为空")
	@Length(min=0, max=32, message="user_id长度不能超过 32 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="create_time不能为空")
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