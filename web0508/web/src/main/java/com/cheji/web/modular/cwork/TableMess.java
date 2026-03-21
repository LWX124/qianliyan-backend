package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class TableMess {
    private String id;
    private String name;
    private String leave;
    private String brandName;
    private String brandId;
    private Long carMessageId;
    private String licensePlate;
    private String phone;
    private String customerName;
    private Integer isPush;
    private BigDecimal merLng;
    private BigDecimal merLat;
    private String merAddress;
    private String brandUrl;


    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    public BigDecimal getMerLng() {
        return merLng;
    }

    public void setMerLng(BigDecimal merLng) {
        this.merLng = merLng;
    }

    public BigDecimal getMerLat() {
        return merLat;
    }

    public void setMerLat(BigDecimal merLat) {
        this.merLat = merLat;
    }

    public String getMerAddress() {
        return merAddress;
    }

    public void setMerAddress(String merAddress) {
        this.merAddress = merAddress;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public Long getCarMessageId() {
        return carMessageId;
    }

    public void setCarMessageId(Long carMessageId) {
        this.carMessageId = carMessageId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    @Override
    public String toString() {
        return "TableMess{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", leave='" + leave + '\'' +
                '}';
    }
}
