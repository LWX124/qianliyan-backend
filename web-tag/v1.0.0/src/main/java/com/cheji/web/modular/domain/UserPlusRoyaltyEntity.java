package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户开通plus会员提成表
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@TableName("app_user_plus_royalty")
public class UserPlusRoyaltyEntity extends Model<UserPlusRoyaltyEntity> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 拿提成的用户id
     */
    @TableField("royalty_user_id")
    private Integer royaltyUserId;
    /**
     * 开通plus会员的id
     */
    @TableField("plus_user_id")
    private Integer plusUserId;
    @TableField("create_time")
    private Date createTime;
    /**
     * 提成金额
     */
    private BigDecimal amount;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRoyaltyUserId() {
        return royaltyUserId;
    }

    public void setRoyaltyUserId(Integer royaltyUserId) {
        this.royaltyUserId = royaltyUserId;
    }

    public Integer getPlusUserId() {
        return plusUserId;
    }

    public void setPlusUserId(Integer plusUserId) {
        this.plusUserId = plusUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserPlusRoyaltyEntity{" +
        ", id=" + id +
        ", royaltyUserId=" + royaltyUserId +
        ", plusUserId=" + plusUserId +
        ", createTime=" + createTime +
        ", amount=" + amount +
        "}";
    }
}
