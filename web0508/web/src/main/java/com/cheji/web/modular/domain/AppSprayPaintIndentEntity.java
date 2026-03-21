package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 喷漆订单表
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_spray_paint_indent")
public class AppSprayPaintIndentEntity extends Model<AppSprayPaintIndentEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 喷漆订单号
     */
    @TableField("spray_paint_number")
    private String sprayPaintNumber;

    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 车辆品牌类型
     */
    @TableField("brand_type")
    private String brandType;

    /**
     * 备注
     */
    private String remark;

    private BigDecimal lng;

    private BigDecimal lat;

    @TableField("pick_address")
    private String pickAddress;  //接车地址

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


    private String username;
    /**
     * 有无保险
     */
    private Integer insurance;

    /**
     * 电话
     */
    private String phone;


    @TableField("user_id")
    private Integer userId;

    @TableField("user_b_id")
    private Integer userBId;

    @TableField("technician_id")
    private Integer technicianId;

    @TableField("is_evaluation")
    private Integer isEvaluation;

    /**
     * 支付订单编号
     */
    @TableField("pay_number")
    private String payNumber;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("is_offer")
    private Integer isOffer;

    @TableField("pick_car_time")
    private Date pickCarTime;

    @TableField("send_car_time")
    private Date sendCarTime;

    @TableField("cancel_reason")
    private String cancelReason;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getPickCarTime() {
        return pickCarTime;
    }

    public void setPickCarTime(Date pickCarTime) {
        this.pickCarTime = pickCarTime;
    }

    public Date getSendCarTime() {
        return sendCarTime;
    }

    public void setSendCarTime(Date sendCarTime) {
        this.sendCarTime = sendCarTime;
    }

    public Integer getIsOffer() {
        return isOffer;
    }

    public void setIsOffer(Integer isOffer) {
        this.isOffer = isOffer;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public Integer getIsEvaluation() {
        return isEvaluation;
    }

    public void setIsEvaluation(Integer isEvaluation) {
        this.isEvaluation = isEvaluation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSprayPaintNumber() {
        return sprayPaintNumber;
    }

    public void setSprayPaintNumber(String sprayPaintNumber) {
        this.sprayPaintNumber = sprayPaintNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getInsurance() {
        return insurance;
    }

    public void setInsurance(Integer insurance) {
        this.insurance = insurance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
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

    @Override
    public String toString() {
        return "AppSprayPaintIndentEntity{" +
                "id=" + id +
                ", sprayPaintNumber='" + sprayPaintNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", brandType='" + brandType + '\'' +
                ", remark='" + remark + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", pickAddress='" + pickAddress + '\'' +
                ", price=" + price +
                ", state=" + state +
                ", payState=" + payState +
                ", username='" + username + '\'' +
                ", insurance=" + insurance +
                ", phone='" + phone + '\'' +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", technicianId=" + technicianId +
                ", isEvaluation=" + isEvaluation +
                ", payNumber='" + payNumber + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isOffer=" + isOffer +
                ", pickCarTime=" + pickCarTime +
                ", sendCarTime=" + sendCarTime +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }
}
