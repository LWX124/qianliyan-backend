package com.cheji.web.modular.cwork;

import java.util.List;

//账单打工主类
public class BillList {
    private String thisMouth;       //当前月份
    private String pay;             //支出总数
    private String income;          //收入
    private List<BillListDetail> billListDetails;   //账单列表

    public String getThisMouth() {
        return thisMouth;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setThisMouth(String thisMouth) {
        this.thisMouth = thisMouth;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public List<BillListDetail> getBillListDetails() {
        return billListDetails;
    }

    public void setBillListDetails(List<BillListDetail> billListDetails) {
        this.billListDetails = billListDetails;
    }

    @Override
    public String toString() {
        return "BillList{" +
                "thisMouth='" + thisMouth + '\'' +
                ", pay='" + pay + '\'' +
                ", income='" + income + '\'' +
                ", billListDetails=" + billListDetails +
                '}';
    }
}
