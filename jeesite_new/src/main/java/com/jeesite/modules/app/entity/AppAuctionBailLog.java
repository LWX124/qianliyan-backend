/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * 支付保证金记录Entity
 *
 * @author y
 * @version 2023-03-19
 */
@Table(name = "app_auction_bail_log", alias = "a", columns = {
        @Column(name = "out_trade_no", attrName = "outTradeNo", label = "支付订单号", isPK = true),
        @Column(name = "amount", attrName = "amount", label = "支付金额.单位", comment = "支付金额.单位:分,实际金额除100"),
        @Column(name = "status", attrName = "status", label = "支付状态", comment = "支付状态：1 初始状态  2 支付成功  3 支付失败", isUpdate = false),
        @Column(name = "user_id", attrName = "userId", label = "user_id"),
        @Column(name = "prepay_id", attrName = "prepayId", label = "预支付id"),
        @Column(name = "notify_time", attrName = "notifyTime", label = "支付通知生成时间"),
        @Column(name = "create_time", attrName = "createTime", label = "订单生成时间"),
        @Column(name = "type", attrName = "type", label = "支付类型，1 微信 2支付宝"),
        @Column(name = "car_id", attrName = "carId", label = "支付保证金车辆的id"),
}, joinTable = {
        @JoinTable(type = Type.LEFT_JOIN, entity = AppAuction.class, attrName = "appAuction", alias = "o",
                on = "o.id = a.car_id", columns = {
                @Column(name = "brand", label = "型号", isQuery = true, queryType = QueryType.LIKE),
                @Column(name = "plate_no", label = "车牌", isQuery = true, queryType = QueryType.LIKE),
                @Column(name = "price", label = "起拍价", isQuery = true, queryType = QueryType.EQ),
                @Column(name = "car_state", label = "状态", isQuery = true, queryType = QueryType.EQ),
                @Column(name = "up_state", label = "上架状态", isQuery = true, queryType = QueryType.EQ),
                @Column(name = "fixed_price", label = "是否一口价", isQuery = true, queryType = QueryType.EQ),
                @Column(name = "id", label = "id", isPK = true, isQuery = true, queryType = QueryType.LIKE),
        }),
        @JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "appUser", alias = "i",
                on = "i.id = a.user_id", columns = {
                @Column(name = "username", label = "账户", isQuery = true, queryType = QueryType.LIKE)
        }),
}, orderBy = "a.create_time DESC"
)
public class AppAuctionBailLog extends DataEntity<AppAuctionBailLog> {

    private static final long serialVersionUID = 1L;
    private String outTradeNo;        // 支付订单号
    private Double amount;        // 支付金额.单位:分,实际金额除100
    private Long userId;        // user_id
    private String prepayId;        // 预支付id
    private Date notifyTime;        // 支付通知生成时间
    private Date createTime;        // 订单生成时间
    private Integer type;        // 支付类型，1 微信 2支付宝
    @JsonSerialize(using = ToStringSerializer.class)//解决long类型到前端精度丢失
    private Long carId;        // 支付保证金车辆的id

    private AppAuction appAuction;

    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppAuction getAppAuction() {
        return appAuction;
    }

    public void setAppAuction(AppAuction appAuction) {
        this.appAuction = appAuction;
    }

    public AppAuctionBailLog() {
        this(null);
    }

    public AppAuctionBailLog(String id) {
        super(id);
    }

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

    @NotNull(message = "user_id不能为空")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NotBlank(message = "预支付id不能为空")
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
    @NotNull(message = "订单生成时间不能为空")
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

}