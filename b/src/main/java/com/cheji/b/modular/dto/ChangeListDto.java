package com.cheji.b.modular.dto;

import java.math.BigDecimal;

public class ChangeListDto {
    private String id;                      //提现或者充值
    private String operationName;           //操作名称
    private String amount;                  //金额
    private Integer type;                   //类型，1提现。2充值，3，视频扣款
    private String operationMonth;                    //月份时间
    private BigDecimal income;                  //收入
    private BigDecimal spend;                   //花费
    private String finalMeg;                    //该月最后一条数据
    private String createTime;                  //创建时间
    private String orderNumber;                 //订单编号


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOperationMonth() {
        return operationMonth;
    }

    public void setOperationMonth(String operationMonth) {
        this.operationMonth = operationMonth;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getSpend() {
        return spend;
    }

    public void setSpend(BigDecimal spend) {
        this.spend = spend;
    }

    public String getFinalMeg() {
        return finalMeg;
    }

    public void setFinalMeg(String finalMeg) {
        this.finalMeg = finalMeg;
    }

    @Override
    public String toString() {
        return "ChangeListDto{" +
                "id='" + id + '\'' +
                ", operationName='" + operationName + '\'' +
                ", amount='" + amount + '\'' +
                ", type=" + type +
                ", operationMonth='" + operationMonth + '\'' +
                ", income=" + income +
                ", spend=" + spend +
                ", finalMeg='" + finalMeg + '\'' +
                ", createTime='" + createTime + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}

