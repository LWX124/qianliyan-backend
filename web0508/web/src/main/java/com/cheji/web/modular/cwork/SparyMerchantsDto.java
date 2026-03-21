package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class SparyMerchantsDto {
    private Integer userBId;                //商户id
    private String merchantsName;           //商户名称
    private String address;                 //地址
    private BigDecimal score;               //总分
    private Integer sparyIndent;            //喷漆订单
    private BigDecimal lng;                 //经度
    private BigDecimal lat;                 //纬度
    private String imgUrl;                  //门店主图


    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getSparyIndent() {
        return sparyIndent;
    }

    public void setSparyIndent(Integer sparyIndent) {
        this.sparyIndent = sparyIndent;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "SparyMerchants{" +
                "userBId=" + userBId +
                ", merchantsName='" + merchantsName + '\'' +
                ", address='" + address + '\'' +
                ", score=" + score +
                ", sparyIndent=" + sparyIndent +
                ", lng=" + lng +
                ", lat=" + lat +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
