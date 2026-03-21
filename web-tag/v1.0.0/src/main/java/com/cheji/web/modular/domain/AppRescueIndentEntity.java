package com.cheji.web.modular.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 救援表
 * </p>
 *
 * @author Ashes
 * @since 2020-02-21
 */
@TableName("app_rescue_indent")
public class AppRescueIndentEntity extends Model<AppRescueIndentEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 救援编号
     */
    @TableField("rescue_number")
    private String rescueNumber;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 救援位置
     */
    @TableField("current_position")
    private String currentPosition;

    /**
     * 商户位置
     */
    @TableField("merchants_position")
    private String merchantsPosition;

    /**
     * 车牌号
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 救援联系人
     */
    @TableField("rescue_name")
    private String rescueName;

    /**
     * 手机号码
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 紧急号码
     */
    @TableField("emergency_number")
    private String emergencyNumber;

    /**
     * 救援类型(1.搭电)
     */
    private Integer type;

    /**
     * 支付价格
     */
    private BigDecimal price;


    private BigDecimal distance;

    @TableField("rescue_scene")
    private Integer rescueScene;


    @TableField("pay_state")
    private Integer payState;



    @TableField("merchants_pay_number")
    private String merchantsPayNumber;

    private BigDecimal lng;

    private BigDecimal lat;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("inlng")
    private BigDecimal inlng;

    @TableField("inlat")
    private BigDecimal inlat;

    @TableField("destination")
    private String destination;

    public BigDecimal getInlng() {
        return inlng;
    }

    public void setInlng(BigDecimal inlng) {
        this.inlng = inlng;
    }

    public BigDecimal getInlat() {
        return inlat;
    }

    public void setInlat(BigDecimal inlat) {
        this.inlat = inlat;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public String getMerchantsPayNumber() {
        return merchantsPayNumber;
    }

    public void setMerchantsPayNumber(String merchantsPayNumber) {
        this.merchantsPayNumber = merchantsPayNumber;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }


    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getRescueNumber() {
        return rescueNumber;
    }

    public void setRescueNumber(String rescueNumber) {
        this.rescueNumber = rescueNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getMerchantsPosition() {
        return merchantsPosition;
    }

    public void setMerchantsPosition(String merchantsPosition) {
        this.merchantsPosition = merchantsPosition;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getRescueName() {
        return rescueName;
    }

    public void setRescueName(String rescueName) {
        this.rescueName = rescueName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Integer getRescueScene() {
        return rescueScene;
    }

    public void setRescueScene(Integer rescueScene) {
        this.rescueScene = rescueScene;
    }

    @Override
    public String toString() {
        return "AppRescueIndentEntity{" +
                "id=" + id +
                ", rescueNumber='" + rescueNumber + '\'' +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", currentPosition='" + currentPosition + '\'' +
                ", merchantsPosition='" + merchantsPosition + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", rescueName='" + rescueName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emergencyNumber='" + emergencyNumber + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", distance=" + distance +
                ", rescueScene=" + rescueScene +
                ", payState=" + payState +
                ", merchantsPayNumber='" + merchantsPayNumber + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", state=" + state +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", inlng=" + inlng +
                ", inlat=" + inlat +
                ", destination='" + destination + '\'' +
                '}';
    }
}
