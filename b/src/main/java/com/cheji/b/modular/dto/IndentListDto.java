package com.cheji.b.modular.dto;

//订单列表
public class IndentListDto {
    private String indentId;
    private String orderNumber;
    private String licensePlate;
    private String brand;
    private String referees;
    private String url;
    private String state;
    private String createTime;
    private String finalMeg;
    private String MonthTime;


    public String getMonthTime() {
        return MonthTime;
    }

    public void setMonthTime(String monthTime) {
        MonthTime = monthTime;
    }

    public String getFinalMeg() {
        return finalMeg;
    }

    public void setFinalMeg(String finalMeg) {
        this.finalMeg = finalMeg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getReferees() {
        return referees;
    }

    public void setReferees(String referees) {
        this.referees = referees;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "IndentListDto{" +
                "indentId='" + indentId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", brand='" + brand + '\'' +
                ", referees='" + referees + '\'' +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                ", createTime='" + createTime + '\'' +
                ", finalMeg='" + finalMeg + '\'' +
                ", MonthTime='" + MonthTime + '\'' +
                '}';
    }
}
