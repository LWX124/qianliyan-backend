package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.*;

import java.math.BigDecimal;
import java.util.List;

//服务页面
public class ServicDisplayDto {
    private String id;
    private List<MerchantsInfoBannerEntity> bannerList; //图片展示
    private String merchantsName;               //商户名称
    private BigDecimal score;                   //打分
    private String brand;                       //品牌
    private String  type;                        //店铺类型
    private List<LableDetailsReviewTreeEntity> lableList;  //服务项目
    private List<MerchantsServicerEntity> servicerList;//服务顾问


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MerchantsInfoBannerEntity> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<MerchantsInfoBannerEntity> bannerList) {
        this.bannerList = bannerList;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String  getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LableDetailsReviewTreeEntity> getLableList() {
        return lableList;
    }

    public void setLableList(List<LableDetailsReviewTreeEntity> lableList) {
        this.lableList = lableList;
    }

    public List<MerchantsServicerEntity> getServicerList() {
        return servicerList;
    }

    public void setServicerList(List<MerchantsServicerEntity> servicerList) {
        this.servicerList = servicerList;
    }

    @Override
    public String toString() {
        return "ServicDisplayDto{" +
                "id='" + id + '\'' +
                ", bannerList=" + bannerList +
                ", merchantsName='" + merchantsName + '\'' +
                ", score=" + score +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", lableList=" + lableList +
                ", servicerList=" + servicerList +
                '}';
    }
}
