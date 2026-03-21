package com.jeesite.modules.app.entity;

import java.math.BigDecimal;

public class RescueMerchants {
    private String id;
    private String merchantsName;
    private String address;
    private BigDecimal lng;
    private BigDecimal lat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "RescueMerchants{" +
                "id='" + id + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", address='" + address + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
