package com.cheji.b.modular.dto;

import java.math.BigDecimal;

public class CenterDetailsDto {
    private Integer todayIndent;        //今日订单
    private BigDecimal allEarnings;     //总收益
    private Integer allIndentCount;     //总单数

    private Integer newIntentCount;     //新订单
    private Integer inServiceCount;     //服务中
    private Integer obligationCount;    //待付款
    private Integer toEvaluateCount;    //待评价
    private Integer cancelledCount;     //已取消


    @Override
    public String toString() {
        return "CenterDetailsDto{" +
                "todayIndent=" + todayIndent +
                ", allEarnings=" + allEarnings +
                ", allIndentCount=" + allIndentCount +
                ", newIntentCount=" + newIntentCount +
                ", inServiceCount=" + inServiceCount +
                ", obligationCount=" + obligationCount +
                ", toEvaluateCount=" + toEvaluateCount +
                ", cancelledCount=" + cancelledCount +
                '}';
    }

    public Integer getTodayIndent() {
        return todayIndent;
    }

    public void setTodayIndent(Integer todayIndent) {
        this.todayIndent = todayIndent;
    }

    public BigDecimal getAllEarnings() {
        return allEarnings;
    }

    public void setAllEarnings(BigDecimal allEarnings) {
        this.allEarnings = allEarnings;
    }

    public Integer getAllIndentCount() {
        return allIndentCount;
    }

    public void setAllIndentCount(Integer allIndentCount) {
        this.allIndentCount = allIndentCount;
    }

    public Integer getNewIntentCount() {
        return newIntentCount;
    }

    public void setNewIntentCount(Integer newIntentCount) {
        this.newIntentCount = newIntentCount;
    }

    public Integer getInServiceCount() {
        return inServiceCount;
    }

    public void setInServiceCount(Integer inServiceCount) {
        this.inServiceCount = inServiceCount;
    }

    public Integer getObligationCount() {
        return obligationCount;
    }

    public void setObligationCount(Integer obligationCount) {
        this.obligationCount = obligationCount;
    }

    public Integer getToEvaluateCount() {
        return toEvaluateCount;
    }

    public void setToEvaluateCount(Integer toEvaluateCount) {
        this.toEvaluateCount = toEvaluateCount;
    }

    public Integer getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(Integer cancelledCount) {
        this.cancelledCount = cancelledCount;
    }
}
