package com.cheji.b.modular.domain;

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
 * @since 2020-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_spray_paint_indent")
public class AppSprayPaintIndentEntity extends Model<AppSprayPaintIndentEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 接车地址
     */
    @TableField("pick_address")
    private String pickAddress;

    //是否报价
    @TableField("is_offer")
    private Integer isOffer;


    private BigDecimal price;

    /**
     * 订单状态 1.开始，2.进行中，3完成, 4.取消
     */
    private Integer state;

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
     * 支付状态 0初始状态  1已支付   2支付失败  3退款
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 有无保险
     */
    private Integer insurance;

    /**
     * 电话
     */
    private String phone;

    /**
     * 支付订单编号
     */
    @TableField("pay_number")
    private String payNumber;

    @TableField("pick_car_time")
    private Date pickCarTime;

    @TableField("send_car_time")
    private Date sendCarTime;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getIsOffer() {
        return isOffer;
    }

    public void setIsOffer(Integer isOffer) {
        this.isOffer = isOffer;
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
                ", isOffer=" + isOffer +
                ", price=" + price +
                ", state=" + state +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", payState=" + payState +
                ", insurance=" + insurance +
                ", phone='" + phone + '\'' +
                ", payNumber='" + payNumber + '\'' +
                ", pickCarTime=" + pickCarTime +
                ", sendCarTime=" + sendCarTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
