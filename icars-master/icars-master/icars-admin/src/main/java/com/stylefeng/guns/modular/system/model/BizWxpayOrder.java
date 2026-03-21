package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
@TableName("biz_wxpay_order")
public class BizWxpayOrder extends Model<BizWxpayOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 支付状态：0 初始状态  1 支付成功  2 支付失败
     */
    private Integer status;
    private String openid;
    /**
     * 预支付id
     */
    @TableField("prepay_id")
    private String prepayId;
    /**
     * 支付通知生成时间
     */
    @TableField("notify_time")
    private Date notifyTime;
    /**
     * 订单生成时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 支付人手机号
     */
    @TableField(exist = false)
    private String phone;


    /**
     * 支付人账号
     */

    private String account;



    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }




    @Override
    protected Serializable pkVal() {
        return this.outTradeNo;
    }

    @Override
    public String toString() {
        return "BizWxpayOrder{" +
        "outTradeNo=" + outTradeNo +
        ", amount=" + amount +
        ", status=" + status +
        ", openid=" + openid +
        ", prepayId=" + prepayId +
        ", notifyTime=" + notifyTime +
        ", createTime=" + createTime +
        ", account=" + account +
        "}";
    }



}
