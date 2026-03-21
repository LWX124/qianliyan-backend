/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 用户信息表Entity
 * @author zcq
 * @version 2019-10-16
 */
@Table(name="app_b_user", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="avatar", attrName="avatar", label="头像", isQuery=false),
		@Column(name="parent_id", attrName="parentId", label="父id", isQuery=false),
		@Column(name="username", attrName="username", label="账户", comment="账户(和电话一致)", queryType=QueryType.LIKE),
		@Column(name="password", attrName="password", label="密码", isQuery=false),
		@Column(name="phone_number", attrName="phoneNumber", label="电话号码", isQuery=false),
		@Column(name="salt", attrName="salt", label="密码盐", isQuery=false),
		@Column(name="name", attrName="name", label="名字", isQuery=false),
		@Column(name="status", attrName="status", label="状态", comment="状态(1:启用，2:冻结，3:删除，4:注册)", isQuery=false),
		@Column(name="balance", attrName="balance", label="用户余额", isQuery=false),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isQuery=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isQuery=false),
		@Column(name="wx_union_id", attrName="wxUnionId", label="微信unionId", isQuery=false),
		@Column(name="wx_open_id", attrName="wxOpenId", label="微信openId", isQuery=false),
		@Column(name="huanxin_user_name", attrName="huanxinUserName", label="环信账号", isQuery=false),
		@Column(name="huanxin_password", attrName="huanxinPassword", label="环信密码", isQuery=false),
		@Column(name="merchants_name", attrName="merchantsName", label="商户名称", queryType=QueryType.LIKE),
		@Column(name="service_sorce", attrName="serviceSorce", label="服务分", isQuery=false),
		@Column(name="effciency_score", attrName="effciencyScore", label="效率分", isQuery=false),
		@Column(name="score", attrName="score", label="总分", isQuery=false),
		@Column(name="lat", attrName="lat", label="纬度", isQuery=false),
		@Column(name="lng", attrName="lng", label="经度", isQuery=false),
		@Column(name="address", attrName="address", label="地址", queryType=QueryType.LIKE),
		@Column(name="type", attrName="type", label="商户类型", comment="商户类型(1,4S店2,修理厂3,专修)"),
		@Column(name="business_start", attrName="businessStart", label="商户营业开始时间", isQuery=false),
		@Column(name="business_end", attrName="businessEnd", label="商户营业结束时间", isQuery=false),
		@Column(name="announcement", attrName="announcement", label="公告", isUpdate=false, isQuery=false),
		@Column(name="state", attrName="state", label="商户提交信息的状态0，未审核，1，已审核，2，未通过"),
		@Column(name="reason", attrName="reason", label="不通过原因", isQuery=false),
		@Column(name="merchants_phone", attrName="merchantsPhone", label="商户电话", isUpdate=false, isQuery=false),
		@Column(name="lable_id", attrName="lableId", label="标签", isQuery=false),
		@Column(name="merchants_level", attrName="merchantsLevel", label="商户等级"),
		@Column(name="details_lable", attrName="detailsLable", label="商户详情页面标签"),
		@Column(name="province", attrName="province", label="省"),
		@Column(name="city", attrName="city", label="市", queryType=QueryType.LIKE),
		@Column(name="county", attrName="county", label="县"),
		@Column(name="addgeo", attrName="addgeo", label="是否添加到geo"),
		@Column(name="redis", attrName="redis", label="redis保存数据"),
		@Column(name="rescue_redis", attrName="rescueRedis", label="redis保存救援数据"),
		@Column(name="brand_id", attrName="brandId", label="品牌id冗余数据"),
		@Column(name="yearcheck_redis", attrName="yearcheckRedis", label="年检商户redis"),
		@Column(name="is_company", attrName="isCompany", label="是否公司员工"),
		@Column(name="substitute_redis", attrName="substituteRedis", label="redis代驾数据"),
		@Column(name="begin_time", attrName="beginTime", label="合同开始时间", isQuery=false),
		@Column(name="end_time", attrName="endTime", label="合同结束时间", isQuery=false),
		@Column(name="responsible_person", attrName="responsiblePerson", label="负责人员", isQuery=false),
		@Column(name="position", attrName="position", label="职位", isQuery=false),
		@Column(name="up_id", attrName="upId", label="上架关联店铺", isQuery=false),
	},
		// 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
		joinTable = {
				@JoinTable(type=Type.LEFT_JOIN, entity=AppCarBrand.class, attrName="this", alias="c",
						on="c.id = a.brand_id",
						columns={
								@Column(name="name", attrName="brandName", label="品牌名称"),
						}),
		}, orderBy="a.id DESC"
)
public class AppBUser extends DataEntity<AppBUser> {
	
	private static final long serialVersionUID = 1L;
	private String avatar;		// 头像
	private Long parentId;		// 父id
	private String username;		// 账户(和电话一致)
	private String password;		// 密码
	private String phoneNumber;		// 电话号码
	private String salt;		// 密码盐
	private String name;		// 名字
	private BigDecimal balance;		// 用户余额
	private Date creatTime;		// creat_time
	private Date updateTime;		// update_time
	private String wxUnionId;		// 微信unionId
	private String wxOpenId;		// 微信openId
	private String huanxinUserName;		// 环信账号
	private String huanxinPassword;		// 环信密码
	private String merchantsName;		// 商户名称
	private Integer serviceSorce;		// 服务分
	private Integer effciencyScore;		// 效率分
	private Double score;		// 总分
	private Double lat;		// 纬度
	private Double lng;		// 经度
	private String address;		// 地址
	private Integer type;		// 商户类型(1,4S店2,修理厂3,专修)
	private String businessStart;		// 商户营业开始时间
	private String businessEnd;		// 商户营业结束时间
	private String announcement;		// 公告
	private Integer state;		// 商户提交信息的状态0，未审核，1，已审核，3，未通过
	private String reason;		// 不通过原因
	private String merchantsPhone;		// 商户电话
	private String lableId;		// 标签
	private String merchantsLevel;		// 商户等级
	private String detailsLable;			//商户详情页面标签
	private String province;		// 省
	private String city;		// 市
	private String county;		// 县
	private String addgeo;		//是否添加到geo(0,没有，1.已添加)
	private String rescueRedis;
	private String redis;		//geo中reids数据
	private String brandId;	//品牌id
	private String brandName;		//品牌名称
	//is_company
	private Integer isCompany;
	private String yearcheckRedis;
	private String substituteRedis;
	private Date beginTime;
	private Date endTime;

	//商户上架，下架，总数
	private Integer shelvesCount;
	private Integer outCount;
	private Integer allCount;

	private List<AppMerchantsInfoBanner> imgList;

	public List<AppMerchantsInfoBanner> getImgList() {
		return imgList;
	}

	public void setImgList(List<AppMerchantsInfoBanner> imgList) {
		this.imgList = imgList;
	}

	//合作保险
	private String insurance;
	private String upId;

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	private String responsiblePerson;
	private String position;

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public Integer getShelvesCount() {
		return shelvesCount;
	}

	public void setShelvesCount(Integer shelvesCount) {
		this.shelvesCount = shelvesCount;
	}

	public Integer getOutCount() {
		return outCount;
	}

	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSubstituteRedis() {
		return substituteRedis;
	}

	public void setSubstituteRedis(String substituteRedis) {
		this.substituteRedis = substituteRedis;
	}

	public String getYearcheckRedis() {
		return yearcheckRedis;
	}

	public void setYearcheckRedis(String yearcheckRedis) {
		this.yearcheckRedis = yearcheckRedis;
	}

	public String getRescueRedis() {
		return rescueRedis;
	}

	public void setRescueRedis(String rescueRedis) {
		this.rescueRedis = rescueRedis;
	}

	public Integer getIsCompany() {
		return isCompany;
	}

	public void setIsCompany(Integer isCompany) {
		this.isCompany = isCompany;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}

	public String getDetailsLable() {
		return detailsLable;
	}

	public void setDetailsLable(String detailsLable) {
		this.detailsLable = detailsLable;
	}

	public String getAddgeo() {
		return addgeo;
	}

	public void setAddgeo(String addgeo) {
		this.addgeo = addgeo;
	}

	public AppBUser() {
		this(null);
	}

	public AppBUser(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="头像长度不能超过 255 个字符")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Length(min=0, max=11, message="账户长度不能超过 11 个字符")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=255, message="密码长度不能超过 255 个字符")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=20, message="电话号码长度不能超过 20 个字符")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=45, message="密码盐长度不能超过 45 个字符")
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Length(min=0, max=20, message="名字长度不能超过 20 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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
	
	@Length(min=0, max=50, message="微信unionId长度不能超过 50 个字符")
	public String getWxUnionId() {
		return wxUnionId;
	}

	public void setWxUnionId(String wxUnionId) {
		this.wxUnionId = wxUnionId;
	}
	
	@Length(min=0, max=50, message="微信openId长度不能超过 50 个字符")
	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}
	
	@Length(min=0, max=50, message="环信账号长度不能超过 50 个字符")
	public String getHuanxinUserName() {
		return huanxinUserName;
	}

	public void setHuanxinUserName(String huanxinUserName) {
		this.huanxinUserName = huanxinUserName;
	}
	
	@Length(min=0, max=50, message="环信密码长度不能超过 50 个字符")
	public String getHuanxinPassword() {
		return huanxinPassword;
	}

	public void setHuanxinPassword(String huanxinPassword) {
		this.huanxinPassword = huanxinPassword;
	}
	
	@Length(min=0, max=20, message="商户名称长度不能超过 20 个字符")
	public String getMerchantsName() {
		return merchantsName;
	}

	public void setMerchantsName(String merchantsName) {
		this.merchantsName = merchantsName;
	}
	
	public Integer getServiceSorce() {
		return serviceSorce;
	}

	public void setServiceSorce(Integer serviceSorce) {
		this.serviceSorce = serviceSorce;
	}
	
	public Integer getEffciencyScore() {
		return effciencyScore;
	}

	public void setEffciencyScore(Integer effciencyScore) {
		this.effciencyScore = effciencyScore;
	}
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
	
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	@Length(min=0, max=50, message="地址长度不能超过 50 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=10, message="商户营业开始时间长度不能超过 10 个字符")
	public String getBusinessStart() {
		return businessStart;
	}

	public void setBusinessStart(String businessStart) {
		this.businessStart = businessStart;
	}
	
	@Length(min=0, max=10, message="商户营业结束时间长度不能超过 10 个字符")
	public String getBusinessEnd() {
		return businessEnd;
	}

	public void setBusinessEnd(String businessEnd) {
		this.businessEnd = businessEnd;
	}
	
	@Length(min=0, max=255, message="公告长度不能超过 255 个字符")
	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@Length(min=0, max=255, message="不通过原因长度不能超过 255 个字符")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=255, message="商户电话长度不能超过 255 个字符")
	public String getMerchantsPhone() {
		return merchantsPhone;
	}

	public void setMerchantsPhone(String merchantsPhone) {
		this.merchantsPhone = merchantsPhone;
	}
	
	@Length(min=0, max=20, message="标签长度不能超过 20 个字符")
	public String getLableId() {
		return lableId;
	}

	public void setLableId(String lableId) {
		this.lableId = lableId;
	}
	
	@Length(min=0, max=11, message="商户等级长度不能超过 11 个字符")
	public String getMerchantsLevel() {
		return merchantsLevel;
	}

	public void setMerchantsLevel(String merchantsLevel) {
		this.merchantsLevel = merchantsLevel;
	}
	
	@Length(min=0, max=20, message="省长度不能超过 20 个字符")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=20, message="市长度不能超过 20 个字符")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="县长度不能超过 20 个字符")
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String toString() {
		return "AppBUser{" +
				"avatar='" + avatar + '\'' +
				", parentId=" + parentId +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", salt='" + salt + '\'' +
				", name='" + name + '\'' +
				", balance=" + balance +
				", creatTime=" + creatTime +
				", updateTime=" + updateTime +
				", wxUnionId='" + wxUnionId + '\'' +
				", wxOpenId='" + wxOpenId + '\'' +
				", huanxinUserName='" + huanxinUserName + '\'' +
				", huanxinPassword='" + huanxinPassword + '\'' +
				", merchantsName='" + merchantsName + '\'' +
				", serviceSorce=" + serviceSorce +
				", effciencyScore=" + effciencyScore +
				", score=" + score +
				", lat=" + lat +
				", lng=" + lng +
				", address='" + address + '\'' +
				", type=" + type +
				", businessStart='" + businessStart + '\'' +
				", businessEnd='" + businessEnd + '\'' +
				", announcement='" + announcement + '\'' +
				", state=" + state +
				", reason='" + reason + '\'' +
				", merchantsPhone='" + merchantsPhone + '\'' +
				", lableId='" + lableId + '\'' +
				", merchantsLevel='" + merchantsLevel + '\'' +
				", detailsLable='" + detailsLable + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", county='" + county + '\'' +
				", addgeo='" + addgeo + '\'' +
				", rescueRedis='" + rescueRedis + '\'' +
				", redis='" + redis + '\'' +
				", brandId='" + brandId + '\'' +
				", brandName='" + brandName + '\'' +
				", isCompany=" + isCompany +
				", yearcheckRedis='" + yearcheckRedis + '\'' +
				", substituteRedis='" + substituteRedis + '\'' +
				", beginTime=" + beginTime +
				", endTime=" + endTime +
				", shelvesCount=" + shelvesCount +
				", outCount=" + outCount +
				", allCount=" + allCount +
				", insurance='" + insurance + '\'' +
				'}';
	}
}