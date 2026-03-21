package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.util.Date;

@TableName("app_wxpay_order")
public class AppWxpayOrderEntity {

    private String outTradeNo;

    private BigDecimal amount;
    private Integer status; //支付状态：1 初始状态  2 支付成功  3 支付失败
    private Integer userId;
    private Integer type; //1:开通plus会员
    private String prepayId;
    private Date notifyTime;
    private Date createTime;
    private Integer businessStatus;   //是否处理业务逻辑  1：未处理  2：已处理  3：不需要处理

    @Override
    public String toString() {
        return "AppWxpayOrderEntity{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", userId=" + userId +
                ", type=" + type +
                ", prepayId='" + prepayId + '\'' +
                ", notifyTime=" + notifyTime +
                ", createTime=" + createTime +
                ", businessStatus=" + businessStatus +
                '}';
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
