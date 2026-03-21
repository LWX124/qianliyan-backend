package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.ImgEntity;
import com.cheji.b.modular.domain.IndentEntity;
import com.cheji.b.modular.domain.LableEntity;

import java.math.BigDecimal;
import java.util.List;

//订单详情页面打工类
public class IndentDetails {
    private String indentId;        //订单id
    private String state;           //订单状态
    private String name;            //客户名称
    private String phoneNumber;    //电话号码
    private String carId;           //车牌号
    private String brand;           //车辆品牌
    private List<ImgEntity> imgEntityList;  //理赔资料图片
    private String plan;            //维修方案
    private List<LableEntity> lableEntityList;  //维修金额方案
    private BigDecimal fixloss;      //定损金额，预估金额
    private BigDecimal commissionRate;  //佣金比例
    private BigDecimal settleAccounts;  //维修金额
    private BigDecimal fixlossUser;    //结算到用户金额
    private String  meansPayments;  //结款方式
    private String remake;          //备注
    private IndentEntity indentEntity;  //订单信息
    private BigDecimal rescueThemFee;    //施救费
    private String responsibility;       //责任划分
    private String insuranceCompany;     //保险公司
    private String sendUnit;             //送修单位
    private String sendPeople;           //送修经理
    private BigDecimal settleFoursCompanyRate;  //4s店结算给公司比例

    public BigDecimal getSettleFoursCompanyRate() {
        return settleFoursCompanyRate;
    }

    public void setSettleFoursCompanyRate(BigDecimal settleFoursCompanyRate) {
        this.settleFoursCompanyRate = settleFoursCompanyRate;
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

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public BigDecimal getFixlossUser() {
        return fixlossUser;
    }

    public void setFixlossUser(BigDecimal fixlossUser) {
        this.fixlossUser = fixlossUser;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getIndentId() {
        return indentId;
    }

    public void setIndentId(String indentId) {
        this.indentId = indentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ImgEntity> getImgEntityList() {
        return imgEntityList;
    }

    public void setImgEntityList(List<ImgEntity> imgEntityList) {
        this.imgEntityList = imgEntityList;
    }

    public List<LableEntity> getLableEntityList() {
        return lableEntityList;
    }

    public void setLableEntityList(List<LableEntity> lableEntityList) {
        this.lableEntityList = lableEntityList;
    }

    public BigDecimal getFixloss() {
        return fixloss;
    }

    public void setFixloss(BigDecimal fixloss) {
        this.fixloss = fixloss;
    }

    public String getMeansPayments() {
        return meansPayments;
    }

    public void setMeansPayments(String meansPayments) {
        this.meansPayments = meansPayments;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public IndentEntity getIndentEntity() {
        return indentEntity;
    }

    public void setIndentEntity(IndentEntity indentEntity) {
        this.indentEntity = indentEntity;
    }

    @Override
    public String toString() {
        return "IndentDetails{" +
                "indentId='" + indentId + '\'' +
                ", state='" + state + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", carId='" + carId + '\'' +
                ", brand='" + brand + '\'' +
                ", imgEntityList=" + imgEntityList +
                ", plan='" + plan + '\'' +
                ", lableEntityList=" + lableEntityList +
                ", fixloss=" + fixloss +
                ", commissionRate=" + commissionRate +
                ", settleAccounts=" + settleAccounts +
                ", fixlossUser=" + fixlossUser +
                ", meansPayments='" + meansPayments + '\'' +
                ", remake='" + remake + '\'' +
                ", indentEntity=" + indentEntity +
                ", rescueThemFee=" + rescueThemFee +
                ", responsibility='" + responsibility + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", sendUnit='" + sendUnit + '\'' +
                ", sendPeople='" + sendPeople + '\'' +
                '}';
    }
}
