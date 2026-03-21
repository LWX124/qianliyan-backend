package com.cheji.web.modular.cwork;

import com.cheji.web.modular.domain.LableEntity;

import java.math.BigDecimal;
import java.util.List;

//订单和商户打工类
public class IndentAndMerchants {
    private String name;        //用户名称
    private String  levelName; //用户等级
    private Integer counts;      //团队数量
    private String  phoneNumber; //电话号码
    private String MerchantsName; //商户名称
    private String imgurl;          //商户图片
    private String address;     //地址
    private String lableid;    //标签字段
    private List<LableEntity> lableEntities; //标签
    private BigDecimal  lat;    //纬度
    private BigDecimal  lng;    //经度
    private List<String> sendUnit;
    private List<String> employeeName;

    public List<String> getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(List<String> sendUnit) {
        this.sendUnit = sendUnit;
    }

    public List<String> getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(List<String> employeeName) {
        this.employeeName = employeeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMerchantsName() {
        return MerchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        MerchantsName = merchantsName;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLableid() {
        return lableid;
    }

    public void setLableid(String lableid) {
        this.lableid = lableid;
    }

    public List<LableEntity> getLableEntities() {
        return lableEntities;
    }

    public void setLableEntities(List<LableEntity> lableEntities) {
        this.lableEntities = lableEntities;
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


    @Override
    public String toString() {
        return "IndentAndMerchants{" +
                "name='" + name + '\'' +
                ", levelName='" + levelName + '\'' +
                ", counts=" + counts +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", MerchantsName='" + MerchantsName + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", address='" + address + '\'' +
                ", lableid='" + lableid + '\'' +
                ", lableEntities=" + lableEntities +
                ", lat=" + lat +
                ", lng=" + lng +
                ", sendUnit=" + sendUnit +
                ", employeeName=" + employeeName +
                '}';
    }
}
