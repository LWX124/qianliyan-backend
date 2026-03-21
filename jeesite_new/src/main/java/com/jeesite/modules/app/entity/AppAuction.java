/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 拍卖业务表Entity
 * @author y
 * @version 2023-03-10
 */
@Table(name="app_auction", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="plate_no", attrName="plateNo", label="车牌号"),
		@Column(name="name", attrName="name", label="所有人名字", queryType=QueryType.LIKE),
		@Column(name="brand", attrName="brand", label="品牌型号"),
		@Column(name="displacement", attrName="displacement", label="排量"),
		@Column(name="transmission", attrName="transmission", label="传动类型,1,自动,0,手动"),
		@Column(name="fuel", attrName="fuel", label="燃油类型,0,汽油,1,柴油,2电,3混动"),
		@Column(name="product_date", attrName="productDate", label="出厂日期"),
		@Column(name="register_date", attrName="registerDate", label="注册日期/初登日期"),
		@Column(name="issue_date", attrName="issueDate", label="发证日期"),
		@Column(name="annual_check", attrName="annualCheck", label="年检期限"),
		@Column(name="compulsory_insurance_vilidity", attrName="compulsoryInsuranceVilidity", label="交强险有效期"),
		@Column(name="registered_residence", attrName="registeredResidence", label="户籍地"),
		@Column(name="parking_place", attrName="parkingPlace", label="停放地"),
		@Column(name="accident_type", attrName="accidentType", label="车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.无记录"),
		@Column(name="sunroof", attrName="sunroof", label="是否有天窗"),
		@Column(name="frame_no", attrName="frameNo", label="车架号"),
		@Column(name="use_nature", attrName="useNature", label="使用性质,1运营,0非运营"),
		@Column(name="frame_no_damaged_condition", attrName="frameNoDamagedCondition", label="车架号是否受损,0否,1是"),
		@Column(name="engine", attrName="engine", label="发动机"),
		@Column(name="mileage", attrName="mileage", label="行驶里程"),
		@Column(name="key", attrName="key", label="钥匙"),
		@Column(name="purchase_tax", attrName="purchaseTax", label="购置税"),
		@Column(name="plate_number", attrName="plateNumber", label="车牌数"),
		@Column(name="registration_certificate", attrName="registrationCertificate", label="登记证书"),
		@Column(name="create_time", attrName="createTime", label="create_time"),
		@Column(name="update_time", attrName="updateTime", label="update_time"),
		@Column(name="overhaul", attrName="overhaul", label="是否拆检,0否,1是"),
		@Column(name="announcements", attrName="announcements", label="注意事项"),
		@Column(name="other_field", attrName="otherField", label="扩展字段"),
		@Column(name="color", attrName="color", label="车身颜色"),
		@Column(name="mortgage", attrName="mortgage", label="抵押按揭"),
		@Column(name="second", attrName="second", label="二次事故"),
		@Column(name="change", attrName="change", label="是否改装"),
		@Column(name="transfer", attrName="transfer", label="过户次数"),
		@Column(name="owner", attrName="owner", label="所有人,个人,公司"),
		@Column(name="duty", attrName="duty", label="事故责任"),
		@Column(name="duty_book", attrName="dutyBook", label="责任书"),
		@Column(name="driving_license", attrName="drivingLicense", label="行驶证"),
		@Column(name="licence", attrName="licence", label="驾照"),
		@Column(name="insurance", attrName="insurance", label="保险公司"),
		@Column(name="repair", attrName="repair", label="维修预估"),
		@Column(name="user_id", attrName="userId", label="登录人id"),
		@Column(name="price", attrName="price", label="起拍价"),
		@Column(name="fixed_price", attrName="fixedPrice", label="是否是一口价,0否,1是"),
		@Column(name="insured_amount", attrName="insuredAmount", label="保险金额"),
		@Column(name="up_state", attrName="upState", label="上架状态,0.未上架,1,已上架"),
		@Column(name="phone", attrName="phone", label="车辆所有人电话"),
		@Column(name="auction_type", attrName="auctionType", label="拍卖类型,保险询价,客户拍卖,车己拍卖,车商拍卖"),
		@Column(name="damage_reason", attrName="damageReason", label="车损原因"),
		@Column(name="car_state", attrName="carState", label="0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成"),
		@Column(name="ins_remark", attrName="insRemark", label="保险车备注"),
		@Column(name="source_type", attrName="sourceType", label="二手车——信息来源默认是事故车，2.二手车"),
		@Column(name="car_bond_amt", attrName="carBondAmt", label="车辆保证金（元）"),
		@Column(name="car_service_amt", attrName="carServiceAmt", label="车辆服务费（元）"),
		@Column(name="luxury_car_price", attrName="luxuryCarPrice", label="二手车精品车价格"),
		@Column(name="first_amount", attrName="firstAmount", label="二手车首付价格"),
	}, orderBy="a.id DESC"
)
public class AppAuction extends DataEntity<AppAuction> {
	
	private static final long serialVersionUID = 1L;
	private String plateNo;		// 车牌号
	private String name;		// 所有人名字
	private String brand;		// 品牌型号
	private String displacement;		// 排量
	private String transmission;		// 传动类型,1,自动,0,手动
	private String fuel;		// 燃油类型,0,汽油,1,柴油,2电,3混动
	private Date productDate;		// 出厂日期
	private Date registerDate;		// 注册日期/初登日期
	private Date issueDate;		// 发证日期
	private Date annualCheck;		// 年检期限
	private Date compulsoryInsuranceVilidity;		// 交强险有效期
	private String registeredResidence;		// 户籍地
	private String parkingPlace;		// 停放地
	private String accidentType;		// 车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.无记录
	private String sunroof;		// 是否有天窗
	private String frameNo;		// 车架号
	private String useNature;		// 使用性质,1运营,0非运营
	private String frameNoDamagedCondition;		// 车架号是否受损,0否,1是
	private String engine;		// 发动机
	private String mileage;		// 行驶里程
	private String key;		// 钥匙
	private String purchaseTax;		// 购置税
	private String plateNumber;		// 车牌数
	private String registrationCertificate;		// 登记证书
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String overhaul;		// 是否拆检,0否,1是
	private String announcements;		// 注意事项
	private String otherField;		// 扩展字段
	private String color;		// 车身颜色
	private String mortgage;		// 抵押按揭
	private String second;		// 二次事故
	private String change;		// 是否改装
	private String transfer;		// 过户次数
	private String owner;		// 所有人,个人,公司
	private String insRemark;		// 保险备注
	private String duty;		// 事故责任
	private String dutyBook;		// 责任书
	private String drivingLicense;		// 行驶证
	private String licence;		// 驾照
	private String insurance;		// 保险公司
	private String repair;		// 维修预估
	private String userId;		// 登录人id
	private Double price;		// 起拍价
	private String fixedPrice;		// 是否是一口价,0否,1是
	private Double insuredAmount;		// 保险金额
	private Integer upState;		// 上架状态,0.未上架,1,已上架
	private String phone;		// 车辆所有人电话
	private String auctionType;		// 拍卖类型,保险询价,客户拍卖,车己拍卖,车商拍卖
	private String damageReason;		// 车损原因
	private Integer carState;		// 0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成
	private Integer sourceType;		// 二手车——信息来源默认是事故车，2.二手车
	private BigDecimal carBondAmt;		// 车辆保证金（元）
	private BigDecimal carServiceAmt;		// 车辆服务费（元）

	private BigDecimal luxuryCarPrice;		//精品车价格

	private BigDecimal firstAmount;			//首付金额

	public BigDecimal getCarServiceAmt() {
		return carServiceAmt;
	}

	public void setCarServiceAmt(BigDecimal carServiceAmt) {
		this.carServiceAmt = carServiceAmt;
	}

	public BigDecimal getCarBondAmt() {
		return carBondAmt;
	}

	public void setCarBondAmt(BigDecimal carBondAmt) {
		this.carBondAmt = carBondAmt;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public AppAuction() {
		this(null);
	}

	public AppAuction(String id){
		super(id);
	}

	public String getInsRemark() {
		return insRemark;
	}

	public void setInsRemark(String insRemark) {
		this.insRemark = insRemark;
	}

	@Length(min=0, max=20, message="车牌号长度不能超过 20 个字符")
	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	
	@Length(min=0, max=24, message="所有人名字长度不能超过 24 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="品牌型号长度不能超过 255 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=20, message="排量长度不能超过 20 个字符")
	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	
	@Length(min=0, max=20, message="传动类型,1,自动,0,手动长度不能超过 20 个字符")
	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	
	@Length(min=0, max=20, message="燃油类型,0,汽油,1,柴油,2电,3混动长度不能超过 20 个字符")
	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAnnualCheck() {
		return annualCheck;
	}

	public void setAnnualCheck(Date annualCheck) {
		this.annualCheck = annualCheck;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCompulsoryInsuranceVilidity() {
		return compulsoryInsuranceVilidity;
	}

	public void setCompulsoryInsuranceVilidity(Date compulsoryInsuranceVilidity) {
		this.compulsoryInsuranceVilidity = compulsoryInsuranceVilidity;
	}
	
	@Length(min=0, max=255, message="户籍地长度不能超过 255 个字符")
	public String getRegisteredResidence() {
		return registeredResidence;
	}

	public void setRegisteredResidence(String registeredResidence) {
		this.registeredResidence = registeredResidence;
	}
	
	@Length(min=0, max=255, message="停放地长度不能超过 255 个字符")
	public String getParkingPlace() {
		return parkingPlace;
	}

	public void setParkingPlace(String parkingPlace) {
		this.parkingPlace = parkingPlace;
	}
	
	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	
	@Length(min=0, max=10, message="是否有天窗长度不能超过 10 个字符")
	public String getSunroof() {
		return sunroof;
	}

	public void setSunroof(String sunroof) {
		this.sunroof = sunroof;
	}
	
	@Length(min=0, max=32, message="车架号长度不能超过 32 个字符")
	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	
	@Length(min=0, max=20, message="使用性质,1运营,0非运营长度不能超过 20 个字符")
	public String getUseNature() {
		return useNature;
	}

	public void setUseNature(String useNature) {
		this.useNature = useNature;
	}
	
	@Length(min=0, max=10, message="车架号是否受损,0否,1是长度不能超过 10 个字符")
	public String getFrameNoDamagedCondition() {
		return frameNoDamagedCondition;
	}

	public void setFrameNoDamagedCondition(String frameNoDamagedCondition) {
		this.frameNoDamagedCondition = frameNoDamagedCondition;
	}
	
	@Length(min=0, max=24, message="发动机长度不能超过 24 个字符")
	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}
	
	@Length(min=0, max=24, message="行驶里程长度不能超过 24 个字符")
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	@Length(min=0, max=10, message="钥匙长度不能超过 10 个字符")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Length(min=0, max=255, message="购置税长度不能超过 255 个字符")
	public String getPurchaseTax() {
		return purchaseTax;
	}

	public void setPurchaseTax(String purchaseTax) {
		this.purchaseTax = purchaseTax;
	}
	
	@Length(min=0, max=8, message="车牌数长度不能超过 8 个字符")
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	@Length(min=0, max=255, message="登记证书长度不能超过 255 个字符")
	public String getRegistrationCertificate() {
		return registrationCertificate;
	}

	public void setRegistrationCertificate(String registrationCertificate) {
		this.registrationCertificate = registrationCertificate;
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
	
	@Length(min=0, max=12, message="是否拆检,0否,1是长度不能超过 12 个字符")
	public String getOverhaul() {
		return overhaul;
	}

	public void setOverhaul(String overhaul) {
		this.overhaul = overhaul;
	}
	
	public String getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(String announcements) {
		this.announcements = announcements;
	}
	
	@Length(min=0, max=255, message="扩展字段长度不能超过 255 个字符")
	public String getOtherField() {
		return otherField;
	}

	public void setOtherField(String otherField) {
		this.otherField = otherField;
	}
	
	@Length(min=0, max=24, message="车身颜色长度不能超过 24 个字符")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Length(min=0, max=255, message="抵押按揭长度不能超过 255 个字符")
	public String getMortgage() {
		return mortgage;
	}

	public void setMortgage(String mortgage) {
		this.mortgage = mortgage;
	}
	
	@Length(min=0, max=24, message="二次事故长度不能超过 24 个字符")
	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}
	
	@Length(min=0, max=24, message="是否改装长度不能超过 24 个字符")
	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}
	
	@Length(min=0, max=24, message="过户次数长度不能超过 24 个字符")
	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}
	
	@Length(min=0, max=24, message="所有人,个人,公司长度不能超过 24 个字符")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Length(min=0, max=24, message="事故责任长度不能超过 24 个字符")
	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	@Length(min=0, max=255, message="责任书长度不能超过 255 个字符")
	public String getDutyBook() {
		return dutyBook;
	}

	public void setDutyBook(String dutyBook) {
		this.dutyBook = dutyBook;
	}
	
	@Length(min=0, max=255, message="行驶证长度不能超过 255 个字符")
	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	@Length(min=0, max=255, message="驾照长度不能超过 255 个字符")
	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}
	
	@Length(min=0, max=255, message="保险公司长度不能超过 255 个字符")
	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	
	@Length(min=0, max=24, message="维修预估长度不能超过 24 个字符")
	public String getRepair() {
		return repair;
	}

	public void setRepair(String repair) {
		this.repair = repair;
	}
	
	@Length(min=0, max=24, message="登录人id长度不能超过 24 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Length(min=0, max=12, message="是否是一口价,0否,1是长度不能超过 12 个字符")
	public String getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(String fixedPrice) {
		this.fixedPrice = fixedPrice;
	}
	
	public Double getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(Double insuredAmount) {
		this.insuredAmount = insuredAmount;
	}
	
	public Integer getUpState() {
		return upState;
	}

	public void setUpState(Integer upState) {
		this.upState = upState;
	}
	
	@Length(min=0, max=11, message="车辆所有人电话长度不能超过 11 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="拍卖类型,保险询价,客户拍卖,车己拍卖,车商拍卖长度不能超过 255 个字符")
	public String getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(String auctionType) {
		this.auctionType = auctionType;
	}
	
	@Length(min=0, max=255, message="车损原因长度不能超过 255 个字符")
	public String getDamageReason() {
		return damageReason;
	}

	public void setDamageReason(String damageReason) {
		this.damageReason = damageReason;
	}
	
	public Integer getCarState() {
		return carState;
	}

	public void setCarState(Integer carState) {
		this.carState = carState;
	}

	public BigDecimal getLuxuryCarPrice() {
		return luxuryCarPrice;
	}

	public void setLuxuryCarPrice(BigDecimal luxuryCarPrice) {
		this.luxuryCarPrice = luxuryCarPrice;
	}

	public BigDecimal getFirstAmount() {
		return firstAmount;
	}

	public void setFirstAmount(BigDecimal firstAmount) {
		this.firstAmount = firstAmount;
	}
}