package com.stylefeng.guns.modular.system.constant;

import java.io.Serializable;
import java.math.BigDecimal;

public class Location implements Cloneable,Serializable
{
    private static final long serialVersionUID = 4629380703368671224L;

    /**
     * 坐标点员工账号
     */
    public String account;
    /**
     * 坐标点人员sessionkey
     */
    public String thirdSessionKey;
    /**
     * 纬度
     */
    public BigDecimal lat;
    /**
     * 经度
     */
    public BigDecimal lng;
    /**
     * 距离
     */
    public Double distance;

    //==========4s部门专用===============
    /**
     * 部门全称
     */
    private String fullName;
    /**
     * 备注 4s服务汽车品牌
     */
    private String tips;
    /**
     * 部门id
     */
    private Integer id;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getThirdSessionKey() {
        return thirdSessionKey;
    }

    public void setThirdSessionKey(String thirdSessionKey) {
        this.thirdSessionKey = thirdSessionKey;
    }

    public Location() {
    }

    public Location(String account, String thirdSessionKey, BigDecimal lat, BigDecimal lng) {
        this.account = account;
        this.thirdSessionKey = thirdSessionKey;
        this.lat = lat;
        this.lng = lng;
    }

    public Location(BigDecimal lat, BigDecimal lng, String fullName, String tips, Integer id) {
        this.lat = lat;
        this.lng = lng;
        this.fullName = fullName;
        this.tips = tips;
        this.id = id;
    }

    @Override
    public Location clone() throws CloneNotSupportedException {
        Location o = null;
        try {
            o = (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}