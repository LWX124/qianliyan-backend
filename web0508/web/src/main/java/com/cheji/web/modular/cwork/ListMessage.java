package com.cheji.web.modular.cwork;

import java.math.BigDecimal;
import java.util.List;

//商户列表的打工类
public class ListMessage {
    private String merchantsCode;
    private String merchantsName;
    private BigDecimal score;
    private BigDecimal rebates;
    private BigDecimal lat;
    private BigDecimal lng;
    private String address;
    private String brand;
    private List lable;
    private List<String> brandUrl;
    private String url;
    private Integer type;
    private String toShop;
    private String brandName;
    private String insurance;

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getToShop() {
        return toShop;
    }

    public void setToShop(String toShop) {
        this.toShop = toShop;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMerchantsCode() {
        return merchantsCode;
    }

    public void setMerchantsCode(String merchantsCode) {
        this.merchantsCode = merchantsCode;
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

    public BigDecimal getRebates() {
        return rebates;
    }

    public void setRebates(BigDecimal rebates) {
        this.rebates = rebates;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List getLable() {
        return lable;
    }

    public void setLable(List lable) {
        this.lable = lable;
    }

    public List<String> getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(List<String> brandUrl) {
        this.brandUrl = brandUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ListMessage{" +
                "merchantsCode='" + merchantsCode + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", score=" + score +
                ", rebates=" + rebates +
                ", lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                ", brand='" + brand + '\'' +
                ", lable=" + lable +
                ", brandUrl=" + brandUrl +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", toShop='" + toShop + '\'' +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
