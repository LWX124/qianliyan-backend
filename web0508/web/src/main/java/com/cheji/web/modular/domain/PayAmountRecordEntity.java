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
 * 红包金额记录表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
@TableName("app_pay_amount_record")
public class PayAmountRecordEntity extends Model<PayAmountRecordEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 事故id
     */
    private Integer accid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 支付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;
    /**
     * 支付时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PayAmountRecordEntity{" +
        ", id=" + id +
        ", accid=" + accid +
        ", userId=" + userId +
        ", payAmount=" + payAmount +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
