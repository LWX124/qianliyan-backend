package com.stylefeng.guns.modular.system.model;

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
 * 微信企业付款到零钱红包支付主表
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
@TableName("biz_wxpay_bill")
public class BizWxpayBill extends Model<BizWxpayBill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 事故id
     */
    private Integer accid;
    /**
     * 支付状态 0：支付成功  1：支付失败
     */
    private Integer status;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String openid;

    @TableField(exist = false)
    private BigDecimal amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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
        return "BizWxpayBill{" +
                "id=" + id +
                ", accid=" + accid +
                ", status=" + status +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", openid='" + openid + '\'' +
                ", amount=" + amount +
                '}';
    }
}
