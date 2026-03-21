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
 * 优惠劵类型表
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_coupon_type")
public class AppCouponTypeEntity extends Model<AppCouponTypeEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单类型(1,喷漆，2.代驾，3.年检)
     */
    private Integer type;

    /**
     * 使用时间
     */
    @TableField("use_period")
    private Integer usePeriod;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUsePeriod() {
        return usePeriod;
    }

    public void setUsePeriod(Integer usePeriod) {
        this.usePeriod = usePeriod;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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
        return "AppCouponTypeEntity{" +
                "id=" + id +
                ", type=" + type +
                ", usePeriod=" + usePeriod +
                ", money=" + money +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
