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
 * 商户业务确认表
 * </p>
 *
 * @author Ashes
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_business_confirm")
public class AppBusinessConfirmEntity extends Model<AppBusinessConfirmEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 业务确认
     */
    @TableField("business_confirm")
    private String businessConfirm;

    /**
     * 事故责任
     */
    @TableField("accident_reponsibility")
    private String accidentReponsibility;

    /**
     * 已联客户
     */
    @TableField("customers_have")
    private BigDecimal customersHave;

    /**
     * 未联客户
     */
    @TableField("not_united_customer")
    private BigDecimal notUnitedCustomer;

    /**
     * 包车到店
     */
    @TableField("charter_shop")
    private BigDecimal charterShop;

    @TableField("create_time")
    private Date createTime;

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

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getBusinessConfirm() {
        return businessConfirm;
    }

    public void setBusinessConfirm(String businessConfirm) {
        this.businessConfirm = businessConfirm;
    }

    public String getAccidentReponsibility() {
        return accidentReponsibility;
    }

    public void setAccidentReponsibility(String accidentReponsibility) {
        this.accidentReponsibility = accidentReponsibility;
    }

    public BigDecimal getCustomersHave() {
        return customersHave;
    }

    public void setCustomersHave(BigDecimal customersHave) {
        this.customersHave = customersHave;
    }

    public BigDecimal getNotUnitedCustomer() {
        return notUnitedCustomer;
    }

    public void setNotUnitedCustomer(BigDecimal notUnitedCustomer) {
        this.notUnitedCustomer = notUnitedCustomer;
    }

    public BigDecimal getCharterShop() {
        return charterShop;
    }

    public void setCharterShop(BigDecimal charterShop) {
        this.charterShop = charterShop;
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
}
