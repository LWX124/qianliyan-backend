/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

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
 * app上报事故信息表Entity
 * @author zcq
 * @version 2019-08-29
 */
@Table(name="app_accident_record", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="user_id", attrName="userId", label="上报用户id", isInsert=false, isUpdate=false),
		@Column(name="video", attrName="video", label="视频url", isInsert=false, isUpdate=false),
		@Column(name="lng", attrName="lng", label="经度", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="lat", attrName="lat", label="纬度", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="checkid", attrName="checkid", label="审核人id", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="statuse", attrName="statuse", label="状态", comment="状态(0：未审核  1：审核通过  2：审核失败）", isInsert=false),
		@Column(name="reason", attrName="reason", label="审核原因", isInsert=false, isQuery=false),
		@Column(name="address", attrName="address", label="上报地址名称", isInsert=false, isQuery=false),
		@Column(name="realness", attrName="realness", label="事故真实性", comment="事故真实性(-1,待处理.0,真现场.1,假现场.2,已撤离.3,非事故)", isInsert=false),
		@Column(name="type", attrName="type", label="视频类型", comment="视频类型(1,派单视频，2.用户上传)", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="introduce", attrName="introduce", label="视频介绍", isInsert=false, isQuery=false),
		@Column(name="georedis", attrName="georedis", label="geo中value"),
		@Column(name="delgeo", attrName="delgeo", label="是否从GEO中移除", isInsert=false, isQuery=false),
		@Column(name="isaddvideo", attrName="isaddvideo", label="是否添加到视频表了", isInsert=false, isQuery=false),
		@Column(name="video_id", attrName="videoId", label="视频关联外键表", isInsert=false, isQuery=false),
		@Column(name="create_time", attrName="createTime", label="上报时间", isInsert=false, isUpdate=false),
		@Column(name="check_time", attrName="checkTime", label="审核时间"),
		@Column(name="real_address", attrName="realAddress", label="上报地址", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="thumbnail_flag", attrName="thumbnailFlag", label="缩略图；1", comment="缩略图；1:已处理   2:未处理", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="thumbnail_url", attrName="thumbnailUrl", label="缩略图地址", isInsert=false, isUpdate=false, isQuery=false),
		@Column(name="img_url", attrName="imgUrl", label="图片地址", isInsert=false),
		@Column(name="update_time", attrName="updateTime", label="修改时间", isInsert=false),
		@Column(name="is_order", attrName="isOrder", label="是否接单", isInsert=false),
		@Column(name="loss_vehicle",attrName = "lossVehicle",label="损失车辆"),
		@Column(name="loss_level",attrName = "lossLevel",label="损失等级"),
		@Column(name="risk_factor",attrName = "riskFactor",label="风险系数"),
},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="name", attrName="name", label="名称"),
				})
}, orderBy="a.id DESC"
)
public class AppAccidentRecord extends DataEntity<AppAccidentRecord> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 上报用户id
	private String video;		// 视频url
	private Double lng;		// 经度
	private Double lat;		// 纬度
	private String checkid;		// 审核人id
	private Integer statuse;
	private String reason;		// 审核原因
	private String address;		// 上报地址名称
	private Integer realness;		// 事故真实性(-1,待处理.0,真现场.1,假现场.2,已撤离.3,非事故)
	private Integer type;		// 视频类型(1,派单视频，2.用户上传)
	private String introduce;	//介绍
	private Integer isaddvideo; //是否添加过到app表中,1.已经添加，0.未添加
	private String georedis;		//redis value
	private String delgeo;			//是否从GEo中移除
	private Long videoId;		//视频关联外键
	private Date createTime;		// 上报时间
	private Date checkTime;		// 审核时间
	private String realAddress;		// 上报地址
	private Integer thumbnailFlag;		// 缩略图；1:已处理   2:未处理
	private String thumbnailUrl;		// 缩略图地址
	private String name;
	private Integer balck;				//是否黑名单
	private String phone;				//电话
	private String pushMerchants;		//推送的商户
	private String redEnvelopes;		//今日红包个数
	private String envelopeAmount;	//今日红包金额
	private String todayAccident;		//今日事故
	private String todayWxup;			//小程序上传
	private String todayAppup;			//今日app上传
	private String passAccident;		//今日通过事故个数
	private String plusEnevlopeAmount;	//plus会员提成金额
	private String plusEnevlopeCount;		//plus会员提成个数
	private Integer slight;		// 轻微
	private Integer onGround;		// 地上拍
	private Integer nearCar;		// 车边拍
	private Integer repeat;		// 重复
	private Integer passNumber;		// 通过个数
	private String useropenId;			//app用户id或者微信openid
	private Integer blackNumber;			//黑名单人数
	private Integer greenNumber;
	private Integer audit;		//今日审核通过个数
	private Integer day;		//白天下单个数
	private Integer night;		//晚上下单个数
	private String imgUrl;

	private String amount;
	private Integer isOrder;
	private Date updateTime;

	private String lossVehicle;		//损失车辆
	private String lossLevel;		//损失等级
	private String riskFactor;		//风险系数
	private String checkImg;		//签到图片
	private String carImg;			//车辆照片
	private String isVoice;		//	是否录音


	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getNight() {
		return night;
	}

	public void setNight(Integer night) {
		this.night = night;
	}

	public String getCheckImg() {
		return checkImg;
	}

	public void setCheckImg(String checkImg) {
		this.checkImg = checkImg;
	}

	public String getCarImg() {
		return carImg;
	}

	public void setCarImg(String carImg) {
		this.carImg = carImg;
	}

	public String getIsVoice() {
		return isVoice;
	}

	public void setIsVoice(String isVoice) {
		this.isVoice = isVoice;
	}

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

	public Integer getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Integer isOrder) {
		this.isOrder = isOrder;
	}

	@JsonFormat(pattern = "HH:mm")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getGreenNumber() {
		return greenNumber;
	}

	public void setGreenNumber(Integer greenNumber) {
		this.greenNumber = greenNumber;
	}

	public Integer getBlackNumber() {
		return blackNumber;
	}

	public void setBlackNumber(Integer blackNumber) {
		this.blackNumber = blackNumber;
	}

	public String getUseropenId() {
		return useropenId;
	}

	public void setUseropenId(String useropenId) {
		this.useropenId = useropenId;
	}

	public Integer getSlight() {
		return slight;
	}

	public void setSlight(Integer slight) {
		this.slight = slight;
	}

	public Integer getOnGround() {
		return onGround;
	}

	public void setOnGround(Integer onGround) {
		this.onGround = onGround;
	}

	public Integer getNearCar() {
		return nearCar;
	}

	public void setNearCar(Integer nearCar) {
		this.nearCar = nearCar;
	}

	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Integer getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(Integer passNumber) {
		this.passNumber = passNumber;
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

	public String getPlusEnevlopeAmount() {
		return plusEnevlopeAmount;
	}

	public void setPlusEnevlopeAmount(String plusEnevlopeAmount) {
		this.plusEnevlopeAmount = plusEnevlopeAmount;
	}

	public String getPlusEnevlopeCount() {
		return plusEnevlopeCount;
	}

	public void setPlusEnevlopeCount(String plusEnevlopeCount) {
		this.plusEnevlopeCount = plusEnevlopeCount;
	}

	public String getEnvelopeAmount() {
		return envelopeAmount;
	}

	public void setEnvelopeAmount(String envelopeAmount) {
		this.envelopeAmount = envelopeAmount;
	}

	public String getTodayAccident() {
		return todayAccident;
	}

	public void setTodayAccident(String todayAccident) {
		this.todayAccident = todayAccident;
	}

	public String getTodayWxup() {
		return todayWxup;
	}

	public void setTodayWxup(String todayWxup) {
		this.todayWxup = todayWxup;
	}

	public String getTodayAppup() {
		return todayAppup;
	}

	public void setTodayAppup(String todayAppup) {
		this.todayAppup = todayAppup;
	}

	public String getPassAccident() {
		return passAccident;
	}

	public void setPassAccident(String passAccident) {
		this.passAccident = passAccident;
	}

	public String getRedEnvelopes() {
		return redEnvelopes;
	}

	public void setRedEnvelopes(String redEnvelopes) {
		this.redEnvelopes = redEnvelopes;
	}

	public String getPushMerchants() {
		return pushMerchants;
	}

	public void setPushMerchants(String pushMerchants) {
		this.pushMerchants = pushMerchants;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getBalck() {
		return balck;
	}

	public void setBalck(Integer balck) {
		this.balck = balck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsaddvideo() {
		return isaddvideo;
	}

	public void setIsaddvideo(Integer isaddvideo) {
		this.isaddvideo = isaddvideo;
	}

	public AppAccidentRecord() {
		this(null);
	}

	public AppAccidentRecord(String id){
		super(id);
	}
	
	@Length(min=0, max=100, message="上报用户id长度不能超过 100 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=255, message="视频url长度不能超过 255 个字符")
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
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
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "MM-dd HH:mm")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "MM-dd HH:mm")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	

	@Length(min=0, max=100, message="上报地址长度不能超过 100 个字符")
	public String getRealAddress() {
		return realAddress;
	}

	public void setRealAddress(String realAddress) {
		this.realAddress = realAddress;
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

	public Integer getStatuse() {
		return statuse;
	}

	public void setStatuse(Integer statuse) {
		this.statuse = statuse;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}


	@Override
	public String toString() {
		return "AppAccidentRecord{" +
				"userId='" + userId + '\'' +
				", video='" + video + '\'' +
				", lng=" + lng +
				", lat=" + lat +
				", checkid='" + checkid + '\'' +
				", statuse=" + statuse +
				", reason='" + reason + '\'' +
				", address='" + address + '\'' +
				", realness=" + realness +
				", type=" + type +
				", introduce='" + introduce + '\'' +
				", isaddvideo=" + isaddvideo +
				", georedis='" + georedis + '\'' +
				", delgeo='" + delgeo + '\'' +
				", videoId=" + videoId +
				", createTime=" + createTime +
				", checkTime=" + checkTime +
				", realAddress='" + realAddress + '\'' +
				", thumbnailFlag=" + thumbnailFlag +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				", name='" + name + '\'' +
				", balck=" + balck +
				", phone='" + phone + '\'' +
				", pushMerchants='" + pushMerchants + '\'' +
				", redEnvelopes='" + redEnvelopes + '\'' +
				", envelopeAmount='" + envelopeAmount + '\'' +
				", todayAccident='" + todayAccident + '\'' +
				", todayWxup='" + todayWxup + '\'' +
				", todayAppup='" + todayAppup + '\'' +
				", passAccident='" + passAccident + '\'' +
				", plusEnevlopeAmount='" + plusEnevlopeAmount + '\'' +
				", plusEnevlopeCount='" + plusEnevlopeCount + '\'' +
				", slight=" + slight +
				", onGround=" + onGround +
				", nearCar=" + nearCar +
				", repeat=" + repeat +
				", passNumber=" + passNumber +
				", useropenId='" + useropenId + '\'' +
				", blackNumber='" + blackNumber + '\'' +
				'}';
	}

}