package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 支付宝营销红包支付明细表
 * </p>
 *
 * @author kosan
 * @since 2018-08-01
 */
@TableName("biz_alipay_pay_record")
public class BizAlipayPayRecord extends Model<BizAlipayPayRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 支付主表id
     */
    @TableField("bill_id")
    private Integer billId;
    /**
     * 接口返回数据
     */
    @TableField("response_info")
    private String responseInfo;
    @TableField(exist = false)
    private String subCode;
    @TableField(exist = false)
    private String outBizNo;
    @TableField("prize_amount")
    private String prizeAmount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 支付宝账户
     */
    @TableField("alipay_account")
    private String alipayAccount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
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

    public String getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(String prizeAmount) {
        this.prizeAmount = prizeAmount;
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

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    @Override
    public String toString() {
        return "BizAlipayPayRecord{" +
                "id=" + id +
                ", billId=" + billId +
                ", responseInfo='" + responseInfo + '\'' +
                ", subCode='" + subCode + '\'' +
                ", outBizNo='" + outBizNo + '\'' +
                ", prizeAmount='" + prizeAmount + '\'' +
                ", createTime=" + createTime +
                ", alipayAccount='" + alipayAccount + '\'' +
                '}';
    }
}
