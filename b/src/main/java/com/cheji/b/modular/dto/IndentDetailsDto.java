package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.ImgEntity;

import java.math.BigDecimal;
import java.util.List;

//订单详情
public class IndentDetailsDto {
    private Integer indentId;           //订单id
    private String username;            //客户姓名
    private String phoneNumber;         //电话号码
    private String licensePlate;        //车牌
    private List<ImgEntity> imgList;    //理赔资料
    private String brand;               //品牌
    private String plan;                //维修方案
    private Integer state;              //状态
    private BigDecimal fixloss;         //预估金额
    private BigDecimal settleAccounts;  //维修金额
    private BigDecimal commissionRate;  //佣金比例
    private BigDecimal fixlossUser;     //结算金额
    private String meansPayments;       //结算方式
    private String orderNumber;         //订单编号
    private String remake;               //订单备注
    private BigDecimal rescueThemFee;    //施救费
    private String responsibility;       //责任划分
    private String insuranceCompany;     //保险公司
    private String sendUnit;             //送修单位
    private String sendPeople;
    private String createTime;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public BigDecimal getRescueThemFee() {
        return rescueThemFee;
    }

    public void setRescueThemFee(BigDecimal rescueThemFee) {
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

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public Integer getIndentId() {
        return indentId;
    }

    public void setIndentId(Integer indentId) {
        this.indentId = indentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public List<ImgEntity> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgEntity> imgList) {
        this.imgList = imgList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getFixloss() {
        return fixloss;
    }

    public void setFixloss(BigDecimal fixloss) {
        this.fixloss = fixloss;
    }

    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getFixlossUser() {
        return fixlossUser;
    }

    public void setFixlossUser(BigDecimal fixlossUser) {
        this.fixlossUser = fixlossUser;
    }

    public String getMeansPayments() {
        return meansPayments;
    }

    public void setMeansPayments(String meansPayments) {
        this.meansPayments = meansPayments;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    @Override
    public String toString() {
        return "IndentDetailsDto{" +
                "indentId=" + indentId +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", imgList=" + imgList +
                ", brand='" + brand + '\'' +
                ", plan='" + plan + '\'' +
                ", state=" + state +
                ", fixloss=" + fixloss +
                ", settleAccounts=" + settleAccounts +
                ", commissionRate=" + commissionRate +
                ", fixlossUser=" + fixlossUser +
                ", meansPayments='" + meansPayments + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", remake='" + remake + '\'' +
                ", rescueThemFee=" + rescueThemFee +
                ", responsibility='" + responsibility + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", sendUnit='" + sendUnit + '\'' +
                ", sendPeople='" + sendPeople + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
