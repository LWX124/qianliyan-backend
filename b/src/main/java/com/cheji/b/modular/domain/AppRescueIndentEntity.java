package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 救援表
 * </p>
 *
 * @author Ashes
 * @since 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_rescue_indent")
public class AppRescueIndentEntity extends Model<AppRescueIndentEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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
     * 距离
     */
    private BigDecimal distance;

    /**
     * 救援类型(1.搭电。2.拖车。3.换胎)
     */
    private Integer type;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 目的地纬度
     */
    private BigDecimal inlat;

    /**
     * 目的地经度
     */
    private BigDecimal inlng;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 救援场景（1事故，2故障，3地面，4地库，5困境，6其他，7有备胎，8送胎，）
     */
    @TableField("rescue_scene")
    private Integer rescueScene;

    /**
     * 支付价格
     */
    private BigDecimal price;

    /**
     * 订单状态 1.开始，2.进行中，3完成, 4.取消 
     */
    private Integer state;

    /**
     * 支付状态 0初始状态  1已支付   2支付失败  3退款
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 支付订单编号
     */
    @TableField("merchants_pay_number")
    private String merchantsPayNumber;

    /**
     * 备注
     */
    private String remark;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 退款状态 ：1 初始状态  2：申请退款  3：退款成功  4：退款失败
     */
    @TableField("back_state")
    private Integer backState;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public BigDecimal getInlat() {
        return inlat;
    }

    public void setInlat(BigDecimal inlat) {
        this.inlat = inlat;
    }

    public BigDecimal getInlng() {
        return inlng;
    }

    public void setInlng(BigDecimal inlng) {
        this.inlng = inlng;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getRescueScene() {
        return rescueScene;
    }

    public void setRescueScene(Integer rescueScene) {
        this.rescueScene = rescueScene;
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

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getMerchantsPayNumber() {
        return merchantsPayNumber;
    }

    public void setMerchantsPayNumber(String merchantsPayNumber) {
        this.merchantsPayNumber = merchantsPayNumber;
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

    public Integer getBackState() {
        return backState;
    }

    public void setBackState(Integer backState) {
        this.backState = backState;
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
                ", distance=" + distance +
                ", type=" + type +
                ", lat=" + lat +
                ", lng=" + lng +
                ", inlat=" + inlat +
                ", inlng=" + inlng +
                ", destination='" + destination + '\'' +
                ", rescueScene=" + rescueScene +
                ", price=" + price +
                ", state=" + state +
                ", payState=" + payState +
                ", merchantsPayNumber='" + merchantsPayNumber + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", backState=" + backState +
                '}';
    }
}
