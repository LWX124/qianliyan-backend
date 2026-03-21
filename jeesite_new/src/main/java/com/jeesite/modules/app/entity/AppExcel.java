package com.jeesite.modules.app.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jeesite.common.entity.DataEntity;

public class AppExcel extends DataEntity<AppExcel> implements java.io.Serializable{
    @Excel(name = "商户名称",orderNum = "0")
    private String merchantsName;

    @Excel(name = "商户电话",orderNum = "1")
    private String merchantsPhone;

    @Excel(name = "商户余额",orderNum = "2")
    private String merchantsBalance;

    @Excel(name = "用户名称",orderNum = "3")
    private String indentName;

    @Excel(name = "用户电话",orderNum = "4")
    private String indentPhone;

    @Excel(name = "推修理赔人员",orderNum = "5")
    private String username;

    @Excel(name = "维修方案",orderNum = "6")
    private String plan;

    @Excel(name = "维修店名称",orderNum = "7")
    private String sendUnit;

    @Excel(name = "车牌",orderNum = "8")
    private String licensePlate;

    @Excel(name = "品牌名称",orderNum = "9")
    private String brand;

    @Excel(name = "施救费",orderNum = "10")
    private String rescueThemFee;

    @Excel(name = "责任划分",orderNum = "11")
    private String responsibility;

    @Excel(name = "保险公司",orderNum = "12")
    private String insuranceCompany;

    @Excel(name = "订单号",orderNum = "13")
    private String orderNumber;

    @Excel(name = "订单状态",orderNum = "14")
    private String indentState;

    @Excel(name = "进车时间",orderNum = "15")
    private String createTime;

    @Excel(name = "预估维修金额",orderNum = "16")
    private String fixloss;

    @Excel(name = "实际维修金额",orderNum = "17")
    private String settleAccounts;

    @Excel(name = "结算给推送人员费用",orderNum = "18")
    private String fixlossUser;

    @Excel(name = "结算给推送人员比例",orderNum = "19")
    private String commissionRate;

    @Excel(name = "送修经理",orderNum = "20")
    private String sendPeople;

    @Excel(name = "4s店结算给公司金额",orderNum = "21")
    private String settleFoursCompany;

    @Excel(name = "4s店结算给公司比例",orderNum = "22")
    private String settleFoursCompanyRate;

    @Excel(name = "信息来源",orderNum = "23")
    private String messageSource;

    @Excel(name = "成交时间",orderNum = "24")
    private String dealTime;

    @Excel(name = "备注",orderNum = "25")
    private String remake;

    @Excel(name = "发放时间",orderNum = "26")
    private String  outOfTime;

    @Excel(name = "发放人员",orderNum = "27")
    private String  outOfPersonnel;


    @Excel(name = "结算方式",orderNum = "28")
    private String  sendBack;

    @Excel(name = "是否到账",orderNum = "29")
    private String  account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getOutOfTime() {
        return outOfTime;
    }

    public void setOutOfTime(String outOfTime) {
        this.outOfTime = outOfTime;
    }

    public String getOutOfPersonnel() {
        return outOfPersonnel;
    }

    public void setOutOfPersonnel(String outOfPersonnel) {
        this.outOfPersonnel = outOfPersonnel;
    }

    public String getSendBack() {
        return sendBack;
    }

    public void setSendBack(String sendBack) {
        this.sendBack = sendBack;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(String messageSource) {
        this.messageSource = messageSource;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

    public String getMerchantsBalance() {
        return merchantsBalance;
    }

    public void setMerchantsBalance(String merchantsBalance) {
        this.merchantsBalance = merchantsBalance;
    }

    public String getIndentName() {
        return indentName;
    }

    public void setIndentName(String indentName) {
        this.indentName = indentName;
    }

    public String getIndentPhone() {
        return indentPhone;
    }

    public void setIndentPhone(String indentPhone) {
        this.indentPhone = indentPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRescueThemFee() {
        return rescueThemFee;
    }

    public void setRescueThemFee(String rescueThemFee) {
        this.rescueThemFee = rescueThemFee;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIndentState() {
        return indentState;
    }

    public void setIndentState(String indentState) {
        this.indentState = indentState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFixloss() {
        return fixloss;
    }

    public void setFixloss(String fixloss) {
        this.fixloss = fixloss;
    }

    public String getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(String settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public String getFixlossUser() {
        return fixlossUser;
    }

    public void setFixlossUser(String fixlossUser) {
        this.fixlossUser = fixlossUser;
    }

    public String getSettleFoursCompany() {
        return settleFoursCompany;
    }

    public void setSettleFoursCompany(String settleFoursCompany) {
        this.settleFoursCompany = settleFoursCompany;
    }

    public String getSettleFoursCompanyRate() {
        return settleFoursCompanyRate;
    }

    public void setSettleFoursCompanyRate(String settleFoursCompanyRate) {
        this.settleFoursCompanyRate = settleFoursCompanyRate;
    }

    @Override
    public String toString() {
        return "AppExcel{" +
                "merchantsName='" + merchantsName + '\'' +
                ", merchantsPhone='" + merchantsPhone + '\'' +
                ", merchantsBalance='" + merchantsBalance + '\'' +
                ", indentName='" + indentName + '\'' +
                ", indentPhone='" + indentPhone + '\'' +
                ", username='" + username + '\'' +
                ", plan='" + plan + '\'' +
                ", sendUnit='" + sendUnit + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", brand='" + brand + '\'' +
                ", rescueThemFee='" + rescueThemFee + '\'' +
                ", responsibility='" + responsibility + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", indentState='" + indentState + '\'' +
                ", createTime='" + createTime + '\'' +
                ", fixloss='" + fixloss + '\'' +
                ", settleAccounts='" + settleAccounts + '\'' +
                ", fixlossUser='" + fixlossUser + '\'' +
                ", sendPeople='" + sendPeople + '\'' +
                ", settleFoursCompany='" + settleFoursCompany + '\'' +
                ", settleFoursCompanyRate='" + settleFoursCompanyRate + '\'' +
                ", messageSource='" + messageSource + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", remake='" + remake + '\'' +
                '}';
    }
}

