package com.cheji.web.modular.cwork;

import java.math.BigDecimal;
import java.util.List;

//零钱账单打工类
public class ChangeBill {
    private String thisMonth;   //年月数据
    private BigDecimal income;      //收入
    private BigDecimal spend;       //支出
    private List<ChangeList> changeLists;   //列表信息

    public String getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
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

    public List<ChangeList> getChangeLists() {
        return changeLists;
    }

    public void setChangeLists(List<ChangeList> changeLists) {
        this.changeLists = changeLists;
    }

    @Override
    public String toString() {
        return "ChangeBill{" +
                "thisMonth='" + thisMonth + '\'' +
                ", income=" + income +
                ", spend=" + spend +
                ", changeLists=" + changeLists +
                '}';
    }
}
