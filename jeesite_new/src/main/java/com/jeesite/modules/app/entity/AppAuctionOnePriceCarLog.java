/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 一口价车辆支付记录Entity
 *
 * @author dh
 * @version 2023-04-16
 */
@Table(name = "app_auction_one_price_car_log", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "out_trade_no", attrName = "outTradeNo", label = "支付订单号"),
        @Column(name = "amount", attrName = "amount", label = "支付金额.单位", comment = "支付金额.单位:分,实际金额除100"),
        @Column(name = "status", attrName = "status", label = "支付状态", comment = "支付状态：1 初始状态  2 支付成功  3 支付失败", isUpdate = false),
        @Column(name = "user_id", attrName = "userId", label = "user_id"),
        @Column(name = "prepay_id", attrName = "prepayId", label = "预支付id"),
        @Column(name = "notify_time", attrName = "notifyTime", label = "支付通知生成时间"),
        @Column(name = "create_time", attrName = "createTime", label = "订单生成时间"),
        @Column(name = "type", attrName = "type", label = "支付类型，1 微信 2支付宝"),
        @Column(name = "car_id", attrName = "carId", label = "支付保证金车辆的id"),
        @Column(name = "vip_lv", attrName = "vipLv", label = "当前用户支付时候的vip等级"),
        @Column(name = "update_time", attrName = "updateTime", label = "修改时间"),
        @Column(name = "refund_status", attrName = "refundStatus", label = "1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败"),
        @Column(name = "apply_refund_time", attrName = "applyRefundTime", label = "申请退款时间"),
        @Column(name = "refund_success_time", attrName = "refundSuccessTime", label = "退款成功时间"),
        @Column(name = "refund_fail_reason", attrName = "refundFailReason", label = "退款失败原因"),
        @Column(name = "refund_id", attrName = "refundId", label = "退款单号"),
        @Column(name = "refund_result_code", attrName = "refundResultCode", label = "退款业务结果"),
        @Column(name = "refund_err_code", attrName = "refundErrCode", label = "退款错误码"),
        @Column(name = "refund_err_code_des", attrName = "refundErrCodeDes", label = "退款错误码"),
}, orderBy = "a.id DESC"
)
public class AppAuctionOnePriceCarLog extends DataEntity<AppAuctionOnePriceCarLog> {

    private static final long serialVersionUID = 1L;
    private String outTradeNo;        // 支付订单号
    private Double amount;        // 支付金额.单位:分,实际金额除100
    private Long userId;        // user_id
    private String prepayId;        // 预支付id
    private Date notifyTime;        // 支付通知生成时间
    private Date createTime;        // 订单生成时间
    private Date updateTime;
    private Integer type;        // 支付类型，1 微信 2支付宝
    private Long carId;        // 支付保证金车辆的id
    private Integer vipLv;        // 当前用户支付时候的vip等级

    private Integer refundStatus;        // 1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败
    private Date applyRefundTime;        // '申请退款时间'
    private Date refundSuccessTime;        // '退款成功时间'
    private String refundFailReason;        // ''退款失败原因''
    private String refundId;        // '退款单号'
    private String refundResultCode;        // '退款业务结果'

    private String errCode; //退款错误码
    private String errCodeDes; //退款错误码描述

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getRefundResultCode() {
        return refundResultCode;
    }

    public void setRefundResultCode(String refundResultCode) {
        this.refundResultCode = refundResultCode;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getApplyRefundTime() {
        return applyRefundTime;
    }

    public void setApplyRefundTime(Date applyRefundTime) {
        this.applyRefundTime = applyRefundTime;
    }

    public Date getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(Date refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getRefundFailReason() {
        return refundFailReason;
    }

    public void setRefundFailReason(String refundFailReason) {
        this.refundFailReason = refundFailReason;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public AppAuctionOnePriceCarLog() {
        this(null);
    }

    public AppAuctionOnePriceCarLog(String id) {
        super(id);
    }

    @Length(min = 0, max = 32, message = "支付订单号长度不能超过 32 个字符")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Length(min = 0, max = 64, message = "预支付id长度不能超过 64 个字符")
    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Integer getVipLv() {
        return vipLv;
    }

    public void setVipLv(Integer vipLv) {
        this.vipLv = vipLv;
    }

    @Override
    public String toString() {
        return "AppAuctionOnePriceCarLog{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", amount=" + amount +
                ", userId=" + userId +
                ", prepayId='" + prepayId + '\'' +
                ", notifyTime=" + notifyTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", carId=" + carId +
                ", vipLv=" + vipLv +
                ", refundStatus=" + refundStatus +
                ", applyRefundTime=" + applyRefundTime +
                ", refundSuccessTime=" + refundSuccessTime +
                ", refundFailReason='" + refundFailReason + '\'' +
                ", refundId='" + refundId + '\'' +
                ", refundResultCode='" + refundResultCode + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errCodeDes='" + errCodeDes + '\'' +
                '}';
    }
}