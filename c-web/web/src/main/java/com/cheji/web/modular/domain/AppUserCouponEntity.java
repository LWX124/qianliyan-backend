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
 * 用户优惠卷表
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_user_coupon")
public class AppUserCouponEntity extends Model<AppUserCouponEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 优惠卷id
     */
    @TableField("coupon_id")
    private Integer couponId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 有效期
     */
    @TableField("validity_time")
    private Date validityTime;

    /**
     * 是否使用（1已使用，2 未使用）
     */
    @TableField("is_use")
    private Integer isUse;

    @TableField("order_number")
    private String orderNumber;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private String toDayTime;


    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private BigDecimal money;


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

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
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

    public String getToDayTime() {
        return toDayTime;
    }

    public void setToDayTime(String toDayTime) {
        this.toDayTime = toDayTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "AppUserCouponEntity{" +
                "id=" + id +
                ", couponId=" + couponId +
                ", userId=" + userId +
                ", validityTime=" + validityTime +
                ", isUse=" + isUse +
                ", orderNumber='" + orderNumber + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", toDayTime='" + toDayTime + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
