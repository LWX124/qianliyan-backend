package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class BUserMessageDto {
    private Integer technicianId;           //技师id
    private String name;          //名称
    private String orderNumber;    //订单数量
    private BigDecimal price;       //喷漆价格
    private String introduction;    //简介
    private String score;           //分数
    private String headImg;         //头像图片
    private BigDecimal lng;
    private BigDecimal lat;

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

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "BUserMessageDto{" +
                "technicianId=" + technicianId +
                ", name='" + name + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", price=" + price +
                ", introduction='" + introduction + '\'' +
                ", score='" + score + '\'' +
                ", headImg='" + headImg + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
