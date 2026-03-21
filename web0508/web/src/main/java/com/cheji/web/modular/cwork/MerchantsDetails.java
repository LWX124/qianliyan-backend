package com.cheji.web.modular.cwork;

import com.cheji.web.modular.domain.*;

import java.math.BigDecimal;
import java.util.List;

//商户详情页面的打工类
public class MerchantsDetails {
    private String merchantsCode;           //商户code
    private String merchantsName;           //商户名称
    private Integer serviceSorce;           //服务分
    private Integer effciencyScore;         //效率分
    private BigDecimal score;               //总分
    private BigDecimal lat;                 //纬度
    private BigDecimal lng;                 //经度
    private String address;                 //地址
    private Integer type;                   //类型
    private List<String> brand;                   //品牌
    private String brandUrl;                //品牌图片地址
    private List<String > lableEntities;             //标签
    private List<MerchantsLableEntity> merchantsLableEntities;   //对应服务项目和返点数
    private List<MerchantsInfoBannerEntity> merchantsInfoBannerEntities; //商户详情banner图
    private List<MerchantsServicerEntity> merchantsServicerEntities;     //服务顾问
    private List<InsuranceMerchantsEntity> insuranceMerchantEntities;      //合作保险
    private String isCanComment;        //是否可以评论
    private String huanxinUserName;     //环信id
    private String merchantsPhone;      //商户电话
    private List<AppBusinessConfirmEntity> businessConfirmList; //列表详情
    private String confirmText;


    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public List<AppBusinessConfirmEntity> getBusinessConfirmList() {
        return businessConfirmList;
    }

    public void setBusinessConfirmList(List<AppBusinessConfirmEntity> businessConfirmList) {
        this.businessConfirmList = businessConfirmList;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getIsCanComment() {
        return isCanComment;
    }

    public void setIsCanComment(String isCanComment) {
        this.isCanComment = isCanComment;
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

    public Integer getServiceSorce() {
        return serviceSorce;
    }

    public void setServiceSorce(Integer serviceSorce) {
        this.serviceSorce = serviceSorce;
    }

    public Integer getEffciencyScore() {
        return effciencyScore;
    }

    public void setEffciencyScore(Integer effciencyScore) {
        this.effciencyScore = effciencyScore;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
        this.brand = brand;
    }

    public List<String > getLableEntities() {
        return lableEntities;
    }

    public void setLableEntities(List<String> lableEntities) {
        this.lableEntities = lableEntities;
    }

    public List<MerchantsLableEntity> getMerchantsLableEntities() {
        return merchantsLableEntities;
    }

    public void setMerchantsLableEntities(List<MerchantsLableEntity> merchantsLableEntities) {
        this.merchantsLableEntities = merchantsLableEntities;
    }

    public List<MerchantsInfoBannerEntity> getMerchantsInfoBannerEntities() {
        return merchantsInfoBannerEntities;
    }

    public void setMerchantsInfoBannerEntities(List<MerchantsInfoBannerEntity> merchantsInfoBannerEntities) {
        this.merchantsInfoBannerEntities = merchantsInfoBannerEntities;
    }

    public List<MerchantsServicerEntity> getMerchantsServicerEntities() {
        return merchantsServicerEntities;
    }

    public void setMerchantsServicerEntities(List<MerchantsServicerEntity> merchantsServicerEntities) {
        this.merchantsServicerEntities = merchantsServicerEntities;
    }

    public List<InsuranceMerchantsEntity> getInsuranceMerchantEntities() {
        return insuranceMerchantEntities;
    }

    public void setInsuranceMerchantEntities(List<InsuranceMerchantsEntity> insuranceMerchantEntities) {
        this.insuranceMerchantEntities = insuranceMerchantEntities;
    }

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    @Override
    public String toString() {
        return "MerchantsDetails{" +
                "merchantsCode='" + merchantsCode + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", serviceSorce=" + serviceSorce +
                ", effciencyScore=" + effciencyScore +
                ", score=" + score +
                ", lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", brand=" + brand +
                ", brandUrl='" + brandUrl + '\'' +
                ", lableEntities=" + lableEntities +
                ", merchantsLableEntities=" + merchantsLableEntities +
                ", merchantsInfoBannerEntities=" + merchantsInfoBannerEntities +
                ", merchantsServicerEntities=" + merchantsServicerEntities +
                ", insuranceMerchantEntities=" + insuranceMerchantEntities +
                ", isCanComment='" + isCanComment + '\'' +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", merchantsPhone='" + merchantsPhone + '\'' +
                ", businessConfirmList=" + businessConfirmList +
                ", confirmText='" + confirmText + '\'' +
                '}';
    }
}
