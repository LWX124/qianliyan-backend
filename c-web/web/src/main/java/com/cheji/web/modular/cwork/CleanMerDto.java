package com.cheji.web.modular.cwork;

import com.cheji.web.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.web.modular.domain.CleanPriceDetailEntity;

import java.math.BigDecimal;
import java.util.List;

public class CleanMerDto {
    private String carName;
    private Integer userBId;
    private String merchantsName;
    private BigDecimal score;
    private BigDecimal lng;
    private BigDecimal lat;
    private String address;
    private Integer type;
    private String url;
    private BigDecimal orginalPrice;
    private BigDecimal preferentialPrice;
    private Integer indentCount;       //总订单
    private Integer saveAllCount;       //已售总数
    private Integer carType;
    private String huanxinUserName;     //环信账号
    private String phoneNumber;         //电话号码
    private BigDecimal settlementRatio;     //到手价格比例
    private String prompt;                  //提示
    private String startTime;
    private String endTime;
    private List<CleanPriceDetailEntity> priceDetailEntityList;
    private List<AppBeautyPriceDetailEntity> beautyPriceDetailList;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public List<AppBeautyPriceDetailEntity> getBeautyPriceDetailList() {
        return beautyPriceDetailList;
    }

    public void setBeautyPriceDetailList(List<AppBeautyPriceDetailEntity> beautyPriceDetailList) {
        this.beautyPriceDetailList = beautyPriceDetailList;
    }

    public BigDecimal getSettlementRatio() {
        return settlementRatio;
    }

    public void setSettlementRatio(BigDecimal settlementRatio) {
        this.settlementRatio = settlementRatio;
    }

    public List<CleanPriceDetailEntity> getPriceDetailEntityList() {
        return priceDetailEntityList;
    }

    public void setPriceDetailEntityList(List<CleanPriceDetailEntity> priceDetailEntityList) {
        this.priceDetailEntityList = priceDetailEntityList;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public Integer getIndentCount() {
        return indentCount;
    }

    public void setIndentCount(Integer indentCount) {
        this.indentCount = indentCount;
    }

    public Integer getSaveAllCount() {
        return saveAllCount;
    }

    public void setSaveAllCount(Integer saveAllCount) {
        this.saveAllCount = saveAllCount;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getOrginalPrice() {
        return orginalPrice;
    }

    public void setOrginalPrice(BigDecimal orginalPrice) {
        this.orginalPrice = orginalPrice;
    }

    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    @Override
    public String toString() {
        return "CleanMerDto{" +
                "carName='" + carName + '\'' +
                ", userBId=" + userBId +
                ", merchantsName='" + merchantsName + '\'' +
                ", score=" + score +
                ", lng=" + lng +
                ", lat=" + lat +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", orginalPrice=" + orginalPrice +
                ", preferentialPrice=" + preferentialPrice +
                ", indentCount=" + indentCount +
                ", saveAllCount=" + saveAllCount +
                ", carType=" + carType +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", settlementRatio=" + settlementRatio +
                ", prompt='" + prompt + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", priceDetailEntityList=" + priceDetailEntityList +
                ", beautyPriceDetailList=" + beautyPriceDetailList +
                '}';
    }
}
