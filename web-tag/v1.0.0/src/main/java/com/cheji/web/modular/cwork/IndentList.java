package com.cheji.web.modular.cwork;

import java.math.BigDecimal;
import java.util.List;

//订单列表打工类
public class IndentList {
    private String indentid;      //订单id
    private String orderNumber; //订单编号
    private String carId;       //车牌
    private String brand;       //品牌
    private String imgUrl;    //订单图片
    private String merchantsName;   //维修单位
    private String sendUnit;    //送修单位
    private String phone;    //电话
    private String dayTime; //天数
    private String insuranceCompany;  //保险公司
    private String name;        //名字
    private String finalMeg;
    private String MonthTime;
    private BigDecimal money;
    private BigDecimal fixMoney;
    private Integer day;
    private List<String> imgList;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getFixMoney() {
        return fixMoney;
    }

    public void setFixMoney(BigDecimal fixMoney) {
        this.fixMoney = fixMoney;
    }

    public String getFinalMeg() {
        return finalMeg;
    }

    public void setFinalMeg(String finalMeg) {
        this.finalMeg = finalMeg;
    }

    public String getMonthTime() {
        return MonthTime;
    }

    public void setMonthTime(String monthTime) {
        MonthTime = monthTime;
    }

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIndentid() {
        return indentid;
    }

    public void setIndentid(String indentid) {
        this.indentid = indentid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    @Override
    public String toString() {
        return "IndentList{" +
                "indentid='" + indentid + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", carId='" + carId + '\'' +
                ", brand='" + brand + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", sendUnit='" + sendUnit + '\'' +
                ", phone='" + phone + '\'' +
                ", dayTime='" + dayTime + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", name='" + name + '\'' +
                ", finalMeg='" + finalMeg + '\'' +
                ", MonthTime='" + MonthTime + '\'' +
                ", money=" + money +
                ", fixMoney=" + fixMoney +
                ", imgList=" + imgList +
                '}';
    }
}
