/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.math.BigDecimal;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 事故上报信息表Entity
 * @author zcq
 * @version 2019-09-24
 */
@Table(name="biz_accident", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="openid", attrName="openid", label="上报人id"),
		@Column(name="video", attrName="video", label="视频url"),
		@Column(name="lng", attrName="lng", label="经度"),
		@Column(name="lat", attrName="lat", label="纬度"),
		@Column(name="checkid", attrName="checkid", label="审核人id"),
		@Column(name="status", attrName="status", label="状态", comment="状态(1：未审核  2：审核通过  3：审核失败）"),
		@Column(name="reason", attrName="reason", label="审核原因"),
		@Column(name="address", attrName="address", label="上报地址名称"),
		@Column(name="realness", attrName="realness", label="事故真实性", comment="事故真实性(0：真实 1：不真实)"),
		@Column(name="isaddvideo", attrName="isaddvideo", label="是否添加到评论表"),
		@Column(name="video_id", attrName="videoId", label="关联视频id"),
		@Column(name="georedis", attrName="georedis", label="geo中value值"),
		@Column(name="delgeo", attrName="delgeo", label="是否从geo中删除"),
		@Column(name="createtime", attrName="createtime", label="上报时间"),
		@Column(name="checktime", attrName="checktime", label="审核时间"),
		@Column(name="version", attrName="version", label="保留字段"),
		@Column(name="carcount", attrName="carcount", label="车总数"),
		@Column(name="realaddress", attrName="realaddress", label="上报地址"),
		@Column(name="realimg", attrName="realimg", label="现场的照片"),
		@Column(name="accimg", attrName="accimg", label="事故照片"),
		@Column(name="lat2", attrName="lat2", label="事故真实纬度"),
		@Column(name="lng2", attrName="lng2", label="事故真实经度"),
		@Column(name="topflag", attrName="topflag", label="是否置顶"),
		@Column(name="realarrtime", attrName="realarrtime", label="到达时间"),
		@Column(name="thumbnail_flag", attrName="thumbnailFlag", label="缩略图；1", comment="缩略图；1:已处理   2:未处理"),
		@Column(name="thumbnail_url", attrName="thumbnailUrl", label="缩略图地址"),
		@Column(name="update_time", attrName="updateTime", label="修改时间"),
		@Column(name="is_order", attrName="isOrder", label="是否接单"),
		@Column(name="pay_red", attrName="payRed", label="是否是红包"),
		@Column(name="loss_vehicle",attrName = "lossVehicle",label="损失车辆"),
		@Column(name="loss_level",attrName = "lossLevel",label="损失等级"),
		@Column(name="risk_factor",attrName = "riskFactor",label="风险系数"),
}, orderBy="a.id DESC"
)
public class BizAccident extends DataEntity<BizAccident> {
	
	private static final long serialVersionUID = 1L;
	private String openid;		// 上报人id
	private String video;		// 视频url
	private Double lng;		// 经度
	private Double lat;		// 纬度
	private String checkid;		// 审核人id
	private String reason;		// 审核原因
	private String address;		// 上报地址名称
	private Integer realness;		// 事故真实性(0：真实 1：不真实)
	private Integer isaddvideo;		// 是否添加到评论表
	private Long videoId;		// 关联视频id
	private String georedis;	//geovalue值
	private String delgeo;		//是否从geo中移除
	private Date createtime;		// 上报时间
	private Date checktime;		// 审核时间
	private Long version;		// 保留字段
	private Integer carcount;		// 车总数
	private String realaddress;		// 上报地址
	private String realimg;		// 现场的照片
	private String accimg;		// 事故照片
	private Long lat2;		// 事故真实纬度
	private Long lng2;		// 事故真实经度
	private Integer topflag;		// 是否置顶
	private String realarrtime;		// 到达时间
	private Integer thumbnailFlag;		// 缩略图；1:已处理   2:未处理
	private String thumbnailUrl;		// 缩略图地址
	private Date updateTime;			//修改时间
	private Integer isOrder;			//是否接单
	private Integer payRed;		//是红包

	private String lossVehicle;		//损失车辆
	private String lossLevel;		//损失等级
	private String riskFactor;		//风险系数


	public String getLossVehicle() {
		return lossVehicle;
	}

	public void setLossVehicle(String lossVehicle) {
		this.lossVehicle = lossVehicle;
	}

	public String getLossLevel() {
		return lossLevel;
	}

	public void setLossLevel(String lossLevel) {
		this.lossLevel = lossLevel;
	}

	public String getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(String riskFactor) {
		this.riskFactor = riskFactor;
	}

	public Integer getPayRed() {
		return payRed;
	}

	public void setPayRed(Integer payRed) {
		this.payRed = payRed;
	}

	@JsonFormat(pattern = "HH:mm")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}

	public String getGeoredis() {
		return georedis;
	}

	public void setGeoredis(String georedis) {
		this.georedis = georedis;
	}

	public String getDelgeo() {
		return delgeo;
	}

	public void setDelgeo(String delgeo) {
		this.delgeo = delgeo;
	}

	public BizAccident() {
		this(null);
	}

	public BizAccident(String id){
		super(id);
	}
	
	@NotBlank(message="上报人id不能为空")
	@Length(min=0, max=100, message="上报人id长度不能超过 100 个字符")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@NotBlank(message="视频url不能为空")
	@Length(min=0, max=100, message="视频url长度不能超过 100 个字符")
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	@NotNull(message="经度不能为空")
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	@NotNull(message="纬度不能为空")
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	@Length(min=0, max=45, message="审核人id长度不能超过 45 个字符")
	public String getCheckid() {
		return checkid;
	}

	public void setCheckid(String checkid) {
		this.checkid = checkid;
	}
	
	@Length(min=0, max=255, message="审核原因长度不能超过 255 个字符")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=250, message="上报地址名称长度不能超过 250 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getRealness() {
		return realness;
	}

	public void setRealness(Integer realness) {
		this.realness = realness;
	}
	
	public Integer getIsaddvideo() {
		return isaddvideo;
	}

	public void setIsaddvideo(Integer isaddvideo) {
		this.isaddvideo = isaddvideo;
	}
	
	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	
	@JsonFormat(pattern = "MM-dd HH:mm")
	@NotNull(message="上报时间不能为空")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@JsonFormat(pattern = "MM-dd HH:mm")
	public Date getChecktime() {
		return checktime;
	}

	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public Integer getCarcount() {
		return carcount;
	}

	public void setCarcount(Integer carcount) {
		this.carcount = carcount;
	}
	
	@Length(min=0, max=100, message="上报地址长度不能超过 100 个字符")
	public String getRealaddress() {
		return realaddress;
	}

	public void setRealaddress(String realaddress) {
		this.realaddress = realaddress;
	}
	
	@Length(min=0, max=255, message="现场的照片长度不能超过 255 个字符")
	public String getRealimg() {
		return realimg;
	}

	public void setRealimg(String realimg) {
		this.realimg = realimg;
	}
	
	@Length(min=0, max=255, message="事故照片长度不能超过 255 个字符")
	public String getAccimg() {
		return accimg;
	}

	public void setAccimg(String accimg) {
		this.accimg = accimg;
	}
	
	public Long getLat2() {
		return lat2;
	}

	public void setLat2(Long lat2) {
		this.lat2 = lat2;
	}
	
	public Long getLng2() {
		return lng2;
	}

	public void setLng2(Long lng2) {
		this.lng2 = lng2;
	}
	
	public Integer getTopflag() {
		return topflag;
	}

	public void setTopflag(Integer topflag) {
		this.topflag = topflag;
	}
	
	@Length(min=0, max=5, message="到达时间长度不能超过 5 个字符")
	public String getRealarrtime() {
		return realarrtime;
	}

	public void setRealarrtime(String realarrtime) {
		this.realarrtime = realarrtime;
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
		return "BizAccident{" +
				"openid='" + openid + '\'' +
				", video='" + video + '\'' +
				", lng=" + lng +
				", lat=" + lat +
				", checkid='" + checkid + '\'' +
				", reason='" + reason + '\'' +
				", address='" + address + '\'' +
				", realness=" + realness +
				", isaddvideo=" + isaddvideo +
				", videoId=" + videoId +
				", georedis='" + georedis + '\'' +
				", delgeo='" + delgeo + '\'' +
				", createtime=" + createtime +
				", checktime=" + checktime +
				", version=" + version +
				", carcount=" + carcount +
				", realaddress='" + realaddress + '\'' +
				", realimg='" + realimg + '\'' +
				", accimg='" + accimg + '\'' +
				", lat2=" + lat2 +
				", lng2=" + lng2 +
				", topflag=" + topflag +
				", realarrtime='" + realarrtime + '\'' +
				", thumbnailFlag=" + thumbnailFlag +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				'}';
	}
}