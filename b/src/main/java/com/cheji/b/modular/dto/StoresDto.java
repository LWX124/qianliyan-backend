package com.cheji.b.modular.dto;


import com.cheji.b.modular.domain.*;

import java.math.BigDecimal;
import java.util.List;

//门店信息
public class StoresDto {
    private String id;
    private List<MerchantsInfoBannerEntity> masterImg;       //门店图片
    private String merchantsName;   //商户名称
    private String merchantsType;   //商户分类
    private String phoneNumber;     //电话号码
    private String state;            //营业状态
    private BigDecimal lng;
    private BigDecimal lat;
    private String businessStart;   //开门时间
    private String businessEnd;     //关门时间
    private String address;           //门店地址
    private String announcement;      //公告
    private List<CarBrandEntity> carBrandList;  //品牌列表
    private List<LableDetailsReviewTreeEntity> lableList;   //门店服务
    private List<BizInsuranceCompanyEntity> insuranceList;   //合作保险
    private List<MerchantsServicerEntity> servicerList;     //服务顾问
    private String onlineCustomers;     //在线客户

    public List<MerchantsInfoBannerEntity> getMasterImg() {
        return masterImg;
    }

    public void setMasterImg(List<MerchantsInfoBannerEntity> masterImg) {
        this.masterImg = masterImg;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public String getMerchantsType() {
        return merchantsType;
    }

    public void setMerchantsType(String merchantsType) {
        this.merchantsType = merchantsType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBusinessStart() {
        return businessStart;
    }

    public void setBusinessStart(String businessStart) {
        this.businessStart = businessStart;
    }

    public String getBusinessEnd() {
        return businessEnd;
    }

    public void setBusinessEnd(String businessEnd) {
        this.businessEnd = businessEnd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public List<CarBrandEntity> getCarBrandList() {
        return carBrandList;
    }

    public void setCarBrandList(List<CarBrandEntity> carBrandList) {
        this.carBrandList = carBrandList;
    }

    public List<LableDetailsReviewTreeEntity> getLableList() {
        return lableList;
    }

    public void setLableList(List<LableDetailsReviewTreeEntity> lableList) {
        this.lableList = lableList;
    }

    public List<BizInsuranceCompanyEntity> getInsuranceList() {
        return insuranceList;
    }

    public void setInsuranceList(List<BizInsuranceCompanyEntity> insuranceList) {
        this.insuranceList = insuranceList;
    }

    public List<MerchantsServicerEntity> getServicerList() {
        return servicerList;
    }

    public void setServicerList(List<MerchantsServicerEntity> servicerList) {
        this.servicerList = servicerList;
    }

    public String getOnlineCustomers() {
        return onlineCustomers;
    }

    public void setOnlineCustomers(String onlineCustomers) {
        this.onlineCustomers = onlineCustomers;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "StoresDto{" +
                "id='" + id + '\'' +
                ", masterImg=" + masterImg +
                ", merchantsName='" + merchantsName + '\'' +
                ", merchantsType='" + merchantsType + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", state='" + state + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", businessStart='" + businessStart + '\'' +
                ", businessEnd='" + businessEnd + '\'' +
                ", address='" + address + '\'' +
                ", announcement='" + announcement + '\'' +
                ", carBrandList=" + carBrandList +
                ", lableList=" + lableList +
                ", insuranceList=" + insuranceList +
                ", servicerList=" + servicerList +
                ", onlineCustomers='" + onlineCustomers + '\'' +
                '}';
    }
}
