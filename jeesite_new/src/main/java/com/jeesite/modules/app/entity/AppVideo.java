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

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 视频信息表Entity
 * @author zcq
 * @version 2020-02-27
 */
@Table(name="app_video", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="url", attrName="url", label="视频地址", isQuery=false),
		@Column(name="user_id", attrName="userId", label="关联user表", isQuery=false),
		@Column(name="source", attrName="source", label="来源，1.事故表，2.小程序表", isQuery=false),
		@Column(name="count", attrName="count", label="点赞数", isQuery=false),
		@Column(name="share", attrName="share", label="分享次数", isQuery=false),
		@Column(name="state", attrName="state", label="审核状态"),
		@Column(name="reson", attrName="reson", label="未通过原因", isQuery=false),
		@Column(name="app_show_falg", attrName="appShowFalg", label="是否展示到app", isQuery=false),
		@Column(name="accident_id", attrName="accidentId", label="关联事故表",isQuery=false),
		@Column(name="creat_time", attrName="creatTime", label="上传时间", isQuery=false),
		@Column(name="app_view_counts", attrName="appViewCounts", label="视频播放量", isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time",isQuery=false),
		@Column(name="address", attrName="address", label="地址", isQuery=false),
		@Column(name="introduce", attrName="introduce", label="视频介绍", isQuery=false),
		@Column(name="is_pay", attrName="isPay", label="是否发钱", comment="是否发钱（0.没有，1.发了）"),
		@Column(name="thumbnail_flag", attrName="thumbnailFlag", label="缩略图；1", comment="缩略图；1:已处理   2:未处理", isQuery=false),
		@Column(name="thumbnail_url", attrName="thumbnailUrl", label="缩略图地址", isQuery=false),
},
		joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="name", attrName="name", label="名称"),
						@Column(name ="avatar",attrName ="avatar",label = "头像"),
				}),
}, orderBy="a.id DESC"
)
public class AppVideo extends DataEntity<AppVideo> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 视频地址
	private String userId;		// 关联user表
	private Integer source;		// 来源，1.事故表，2.小程序表
	private Integer count;		// 点赞数
	private Long share;		// 分享次数
	private Integer state;		// 审核状态
	private String reson;		// 未通过原因
	private Integer appShowFalg;		// 是否展示到app
	private Long accidentId;		// 关联事故表
	private Date creatTime;		// 上传时间
	private Long appViewCounts;		// 视频播放量
	private Date updateTime;		// update_time
	private String address;		// 地址
	private String introduce;		// 视频介绍
	private Integer isPay;		// 是否发钱（0.没有，1.发了）
	private Integer thumbnailFlag;		// 缩略图；1:已处理   2:未处理
	private String thumbnailUrl;		// 缩略图地址
	private String name;
	private String avatar;			//头像

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public AppVideo() {
		this(null);
	}

	public AppVideo(String id){
		super(id);
	}
	
	@NotBlank(message="视频地址不能为空")
	@Length(min=0, max=255, message="视频地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Long getShare() {
		return share;
	}

	public void setShare(Long share) {
		this.share = share;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@Length(min=0, max=100, message="未通过原因长度不能超过 100 个字符")
	public String getReson() {
		return reson;
	}

	public void setReson(String reson) {
		this.reson = reson;
	}
	
	public Integer getAppShowFalg() {
		return appShowFalg;
	}

	public void setAppShowFalg(Integer appShowFalg) {
		this.appShowFalg = appShowFalg;
	}
	
	public Long getAccidentId() {
		return accidentId;
	}

	public void setAccidentId(Long accidentId) {
		this.accidentId = accidentId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	public Long getAppViewCounts() {
		return appViewCounts;
	}

	public void setAppViewCounts(Long appViewCounts) {
		this.appViewCounts = appViewCounts;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Length(min=0, max=255, message="地址长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=255, message="视频介绍长度不能超过 255 个字符")
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public Integer getIsPay() {
		return isPay;
	}

	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}
	
	public Integer getThumbnailFlag() {
		return thumbnailFlag;
	}

	public void setThumbnailFlag(Integer thumbnailFlag) {
		this.thumbnailFlag = thumbnailFlag;
	}
	
	@Length(min=0, max=255, message="缩略图地址长度不能超过 255 个字符")
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public String toString() {
		return "AppVideo{" +
				"url='" + url + '\'' +
				", userId='" + userId + '\'' +
				", source=" + source +
				", count=" + count +
				", share=" + share +
				", state=" + state +
				", reson='" + reson + '\'' +
				", appShowFalg=" + appShowFalg +
				", accidentId=" + accidentId +
				", creatTime=" + creatTime +
				", appViewCounts=" + appViewCounts +
				", updateTime=" + updateTime +
				", address='" + address + '\'' +
				", introduce='" + introduce + '\'' +
				", isPay=" + isPay +
				", thumbnailFlag=" + thumbnailFlag +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				", name='" + name + '\'' +
				", avatar='" + avatar + '\'' +
				'}';
	}
}