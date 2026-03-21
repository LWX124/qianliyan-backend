package com.cheji.b.modular.dto;

import java.math.BigDecimal;

//进入收入列表
public class EarningListDto {
    private String indentId;        //订单id
    private String orderNumber;     //订单编号
    private String licensePlate;    //车牌
    private String brand;           //品牌
    private String merchantsName;   //商户名称
    private BigDecimal settleAccounts;  //结算金额
    private String url;             //图片


    public String getIndentId() {
        return indentId;
    }

    public void setIndentId(String indentId) {
        this.indentId = indentId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "EarningListDto{" +
                "indentId='" + indentId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", brand='" + brand + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", settleAccounts=" + settleAccounts +
                ", url='" + url + '\'' +
                '}';
    }
}
