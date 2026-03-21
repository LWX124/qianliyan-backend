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
 * 支付宝营销红包支付主表
 * </p>
 *
 * @author kosan
 * @since 2018-08-01
 */
@TableName("biz_alipay_bill")
public class BizAlipayBill extends Model<BizAlipayBill> {

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
    private String responseInfo;
    @TableField(exist = false)
    private String subCode;
    @TableField(exist = false)
    private String outBizNo;
    @TableField(exist = false)
    private BigDecimal prizeAmount;
    @TableField(exist = false)
    private String alipayAccount;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public BigDecimal getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(BigDecimal prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    @Override
    public String toString() {
        return "BizAlipayBill{" +
                "id=" + id +
                ", accid=" + accid +
                ", status=" + status +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", responseInfo='" + responseInfo + '\'' +
                ", subCode='" + subCode + '\'' +
                ", outBizNo='" + outBizNo + '\'' +
                ", prizeAmount=" + prizeAmount +
                ", alipayAccount='" + alipayAccount + '\'' +
                '}';
    }
}
