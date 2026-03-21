package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("app_b_user")
public class AppUserEntity extends Model<AppUserEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String avatar;
    private String parentId;
    private String username;
    private String password;
    private String phoneNumber;
    private String salt;
    private String name;
    private Integer status; //状态(1:启用，2:冻结，3:删除，4:注册)
    private Date creatTime;
    private Date updateTime;
    private String wxUnionId;
    private String wxOpenId;
    private String huanxinUserName;
    private String huanxinPassword;
    private BigDecimal balance;
    private String merchantsName;
    private Integer serviceSorce;
    private Integer effciencyScore;
    private BigDecimal score;
    private BigDecimal lat;
    private BigDecimal lng;
    private String address;
    private Integer type;
    private String businessStart;
    private String businessEnd;
    private String announcement;
    private Integer state;
    private String reason;
    private String merchantsPhone;
    private String lableId;
    private String merchantsLevel;
    private String province;
    private String city;
    private String county;
    private String brandId;
    private String addgeo;
    private String redis;
    private String isCompany;
    private String rescueRedis;
    private String yearcheckRedis;
    private String substituteRedis;
    private Integer unreadMessage;
    private String realLocation;
    private Date saveTime;
    private Integer isLocation;
    private String cId;
    private String upId;


    @Override
    public String toString() {
        return "AppUserEntity{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", parentId='" + parentId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", wxUnionId='" + wxUnionId + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", huanxinPassword='" + huanxinPassword + '\'' +
                ", balance=" + balance +
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
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", brandId='" + brandId + '\'' +
                ", addgeo='" + addgeo + '\'' +
                ", redis='" + redis + '\'' +
                ", isCompany='" + isCompany + '\'' +
                ", rescueRedis='" + rescueRedis + '\'' +
                ", yearcheckRedis='" + yearcheckRedis + '\'' +
                ", substituteRedis='" + substituteRedis + '\'' +
                ", unreadMessage=" + unreadMessage +
                ", realLocation='" + realLocation + '\'' +
                ", saveTime=" + saveTime +
                '}';
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Integer getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Integer isLocation) {
        this.isLocation = isLocation;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

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

    public String getBusinessStart() {
        return businessStart;
    }

    public void setBusinessStart(String businessStart) {
        this.businessStart = businessStart;
    }

    public String getBusinessEnd() {
        return businessEnd;
    }

    public void setBusinessEnd(String businessEnd) {
        this.businessEnd = businessEnd;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

    public String getLableId() {
        return lableId;
    }

    public void setLableId(String lableId) {
        this.lableId = lableId;
    }

    public String getMerchantsLevel() {
        return merchantsLevel;
    }

    public void setMerchantsLevel(String merchantsLevel) {
        this.merchantsLevel = merchantsLevel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getAddgeo() {
        return addgeo;
    }

    public void setAddgeo(String addgeo) {
        this.addgeo = addgeo;
    }

    public String getRedis() {
        return redis;
    }

    public void setRedis(String redis) {
        this.redis = redis;
    }

    public String getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(String isCompany) {
        this.isCompany = isCompany;
    }

    public String getRescueRedis() {
        return rescueRedis;
    }

    public void setRescueRedis(String rescueRedis) {
        this.rescueRedis = rescueRedis;
    }

    public String getYearcheckRedis() {
        return yearcheckRedis;
    }

    public void setYearcheckRedis(String yearcheckRedis) {
        this.yearcheckRedis = yearcheckRedis;
    }

    public String getSubstituteRedis() {
        return substituteRedis;
    }

    public void setSubstituteRedis(String substituteRedis) {
        this.substituteRedis = substituteRedis;
    }

    public Integer getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public String getRealLocation() {
        return realLocation;
    }

    public void setRealLocation(String realLocation) {
        this.realLocation = realLocation;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }
}
