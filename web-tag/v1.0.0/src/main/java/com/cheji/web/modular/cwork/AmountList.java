package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class AmountList {
    private String id;          //操作id
    private String operationName;   //操作名称
    private String amount;          //金额
    private String type;            //类型，1.提现记录，2.红包，3.会员提成记录，4.订单结算plus会员，5，订单结算
    private String operationMonth;  //月份时间
    private BigDecimal income;        //收入
    private BigDecimal spend;           //花费
    private String finalMeg;        //该月最后一条数据


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        return "AmountList{" +
                "id='" + id + '\'' +
                ", operationName='" + operationName + '\'' +
                ", amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", operationMonth='" + operationMonth + '\'' +
                ", income=" + income +
                ", spend=" + spend +
                ", finalMeg='" + finalMeg + '\'' +
                '}';
    }
}
