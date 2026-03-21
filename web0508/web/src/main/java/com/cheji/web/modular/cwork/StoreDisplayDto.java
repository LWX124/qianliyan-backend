package com.cheji.web.modular.cwork;

import com.cheji.web.modular.domain.MerchantsInfoBannerEntity;

import java.math.BigDecimal;
import java.util.List;

public class StoreDisplayDto {
    private Integer userBId;        //商户id
    private String startTime;
    private String endTime;
    private String address;
    private String merchantsName;   //商户名称
    private String type;            //类型
    private BigDecimal score;       //总评分
    private Integer allIndent;      //总订单
    private BigDecimal lng;
    private BigDecimal lat;
    private String huanxinUserName;
    private List<MerchantsInfoBannerEntity> infoBannerList;


    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

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

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getAllIndent() {
        return allIndent;
    }

    public void setAllIndent(Integer allIndent) {
        this.allIndent = allIndent;
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

    public List<MerchantsInfoBannerEntity> getInfoBannerList() {
        return infoBannerList;
    }

    public void setInfoBannerList(List<MerchantsInfoBannerEntity> infoBannerList) {
        this.infoBannerList = infoBannerList;
    }


    @Override
    public String toString() {
        return "StoreDisplayDto{" +
                "userBId=" + userBId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", address='" + address + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", type='" + type + '\'' +
                ", score=" + score +
                ", allIndent=" + allIndent +
                ", lng=" + lng +
                ", lat=" + lat +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", infoBannerList=" + infoBannerList +
                '}';
    }
}
