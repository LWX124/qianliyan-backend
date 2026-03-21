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
 * 年检订单表
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_year_check_indent")
public class AppYearCheckIndentEntity extends Model<AppYearCheckIndentEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 年检订单编号
     */
    @TableField("year_check_number")
    private String yearCheckNumber;

    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 接车地点
     */
    @TableField("pick_address")
    private String pickAddress;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * c端用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * b端用户id
     */
    @TableField("user_b_id")
    private Integer userBId;

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
     * 年检类型（1.免检代办，2.上线年检）
     */
    @TableField("year_check_type")
    private Integer yearCheckType;

    @TableField("confirm_car")
    private Integer confirmCar;

    /**
     * 支付订单编号
     */
    @TableField("pay_number")
    private String payNumber;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getConfirmCar() {
        return confirmCar;
    }

    public void setConfirmCar(Integer confirmCar) {
        this.confirmCar = confirmCar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYearCheckNumber() {
        return yearCheckNumber;
    }

    public void setYearCheckNumber(String yearCheckNumber) {
        this.yearCheckNumber = yearCheckNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Integer getYearCheckType() {
        return yearCheckType;
    }

    public void setYearCheckType(Integer yearCheckType) {
        this.yearCheckType = yearCheckType;
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
        return "AppYearCheckIndentEntity{" +
                "id=" + id +
                ", yearCheckNumber='" + yearCheckNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", pickAddress='" + pickAddress + '\'' +
                ", price=" + price +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", state=" + state +
                ", payState=" + payState +
                ", yearCheckType=" + yearCheckType +
                ", confirmCar=" + confirmCar +
                ", payNumber='" + payNumber + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
