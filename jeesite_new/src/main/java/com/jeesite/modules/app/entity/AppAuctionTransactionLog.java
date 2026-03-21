/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 交易记录Entity
 *
 * @author y
 * @version 2023-03-27
 */
@Table(name = "app_auction_transaction_log", alias = "a",
        columns = {
        @Column(name = "id", attrName = "id", label = "主键id", isPK = true),
                @Column(name = "user_id", attrName = "userId", label = "用户id"),
                @Column(name = "amount", attrName = "amount", label = "发生金额,单位分"),
                @Column(name = "type", attrName = "type", label = "交易类型,1减,2加"),
                @Column(name = "desc", attrName = "desc", label = "描述"),
                @Column(name = "create_time", attrName = "createTime", label = "create_time"),
                @Column(name = "order_id", attrName = "orderId", label = "订单id"),
                @Column(name = "order_state", attrName = "orderState", label = "订单状态,1VIP充值,2拍卖扣除,3提现中,4提现失败,5成功"),
                @Column(name = "pay_type", attrName = "payType", label = "支付方式,1.微信,2.支付宝,3.银行卡"),
                @Column(name = "car_id", attrName = "carId", label = "车辆ID如果是单车保证金，需要记录车辆ID"),
        }, orderBy = "a.id DESC")
public class AppAuctionTransactionLog extends DataEntity<AppAuctionTransactionLog> {

    private static final long serialVersionUID = 1L;
    private Long userId;        // 用户id
    private Double amount;        // 发生金额,单位分
    private Integer type;        // 交易类型,1减,2加
    private String desc;        // 描述
    private Date createTime;        // create_time
    private String orderId;        // 订单id
    private Integer orderState;        // 订单状态,1VIP充值,2拍卖扣除,3提现中,4提现失败,5成功
    private Integer payType;        // 支付方式,1.微信,2.支付宝,3.银行卡

    private Long carId;        // 如果是单车保证金  需要记录车辆ID

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public AppAuctionTransactionLog() {
        this(null);
    }

    public AppAuctionTransactionLog(String id) {
        super(id);
    }

    @NotNull(message = "用户id不能为空")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Length(min = 0, max = 255, message = "描述长度不能超过 255 个字符")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Length(min = 0, max = 255, message = "订单id长度不能超过 255 个字符")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "AppAuctionTransactionLog{" + "userId=" + userId + ", amount=" + amount + ", type=" + type + ", desc='" + desc + '\'' + ", createTime=" + createTime + ", orderId='" + orderId + '\'' + ", orderState=" + orderState + ", payType=" + payType + ", carId=" + carId + '}';
    }
}