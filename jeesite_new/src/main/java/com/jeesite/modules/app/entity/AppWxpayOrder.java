/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * app_wxpay_orderEntity
 *
 * @author dh
 * @version 2019-12-16
 */
@Table(name = "app_wxpay_order", alias = "a", columns = {
        @Column(name = "out_trade_no", attrName = "outTradeNo", label = "支付订单号", isPK = true),
        @Column(name = "amount", attrName = "amount", label = "支付金额"),
        @Column(name = "status", attrName = "status", label = "支付状态", isUpdate = false),
        @Column(name = "user_id", attrName = "userId", label = "user_id"),
        @Column(name = "prepay_id", attrName = "prepayId", label = "预支付id"),
        @Column(name = "notify_time", attrName = "notifyTime", label = "支付通知生成时间"),
        @Column(name = "create_time", attrName = "createTime", label = "订单生成时间"),
        @Column(name = "type", attrName = "type", label = "类型1", comment = "类型1:开通plus会员"),
        @Column(name = "business_status", attrName = "businessStatus", label = "是否处理业务逻辑"),
}, orderBy = "a.out_trade_no DESC"
)
public class AppWxpayOrder extends DataEntity<AppWxpayOrder> {

    private static final long serialVersionUID = 1L;
    private String outTradeNo;        // 支付订单号
    private Double amount;        // 支付金额
    private Long userId;        // user_id
    private String prepayId;        // 预支付id
    private Date notifyTime;        // 支付通知生成时间
    private Date createTime;        // 订单生成时间
    private Integer type;        // 类型1:开通plus会员
    private Integer businessStatus;        // 是否处理业务逻辑

    public AppWxpayOrder() {
        this(null);
    }

    public AppWxpayOrder(String id) {
        super(id);
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @NotNull(message = "支付金额不能为空")
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

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

}
