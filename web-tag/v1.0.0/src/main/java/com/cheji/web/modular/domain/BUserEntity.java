package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@TableName("app_b_user")
public class BUserEntity extends Model<BUserEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 账户(和电话一致)
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;
    /**
     * 状态(1:启用，2:冻结，3:删除，4:注册)
     */
    private Integer status;
    /**
     * 用户余额
     */
    private BigDecimal balance;
    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;
    /**
     * 微信unionId
     */
    @TableField("wx_union_id")
    private String wxUnionId;
    /**
     * 微信openId
     */
    @TableField("wx_open_id")
    private String wxOpenId;
    /**
     * 环信账号
     */
    @TableField("huanxin_user_name")
    private String huanxinUserName;
    /**
     * 环信密码
     */
    @TableField("huanxin_password")
    private String huanxinPassword;
    /**
     * 商户名称
     */
    @TableField("merchants_name")
    private String merchantsName;
    /**
     * 服务分
     */
    @TableField("service_sorce")
    private Integer serviceSorce;
    /**
     * 效率分
     */
    @TableField("effciency_score")
    private Integer effciencyScore;
    /**
     * 总分
     */
    private BigDecimal score;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 经度
     */
    private BigDecimal lng;
    /**
     * 地址
     */
    private String address;
    /**
     * 商户类型(1,4S店2,修理厂3,专修)
     */
    private Integer type;
    /**
     * 商户营业开始时间
     */
    @TableField("business_start")
    private String businessStart;
    /**
     * 商户营业结束时间
     */
    @TableField("business_end")
    private String businessEnd;
    /**
     * 公告
     */
    private String announcement;
    /**
     * 商户提交信息的状态0，未审核，1，已审核，3，未通过
     */
    private Integer state;
    /**
     * 不通过原因
     */
    private String reason;
    /**
     * 商户电话
     */
    @TableField("merchants_phone")
    private String merchantsPhone;
    /**
     * 标签
     */
    @TableField("lable_id")
    private String lableId;
    /**
     * 商户等级
     */
    @TableField("merchants_level")
    private String merchantsLevel;

    @TableField("details_lable")
    private String detailsLable;

    @TableField("up_id")
    private String upId;

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;

    @TableField("unread_message")
    private Integer unreadMessage;

    @TableField("real_location")
    private String realLocation;

    @TableField("save_time")
    private Date saveTime;

    @TableField("brand_id")
    private String brandId;


    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getRealLocation() {
        return realLocation;
    }

    public void setRealLocation(String realLocation) {
        this.realLocation = realLocation;
    }

    public Integer getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public String getDetailsLable() {
        return detailsLable;
    }

    public void setDetailsLable(String detailsLable) {
        this.detailsLable = detailsLable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BUserEntity{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", parentId=" + parentId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
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
                '}';
    }
}
