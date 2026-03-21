package com.cheji.b.modular.dto;

import java.math.BigDecimal;

public class PriceDetailsDto {
    private String cleanType;
    private String carType;
    private String cleanName;
    private String carName;
    private BigDecimal originalPrice;
    private BigDecimal preferentialPrice;
    private BigDecimal thriePrice;
    private Integer contractProject; //合约项目
    private String residueDegree;   //剩余次数
    private String beautyType;      //美容类型
    private String beautyName;      //美容名称
    private String note;            //备注


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBeautyType() {
        return beautyType;
    }

    public void setBeautyType(String beautyType) {
        this.beautyType = beautyType;
    }

    public String getBeautyName() {
        return beautyName;
    }

    public void setBeautyName(String beautyName) {
        this.beautyName = beautyName;
    }

    public Integer getContractProject() {
        return contractProject;
    }

    public void setContractProject(Integer contractProject) {
        this.contractProject = contractProject;
    }

    public String getResidueDegree() {
        return residueDegree;
    }

    public void setResidueDegree(String residueDegree) {
        this.residueDegree = residueDegree;
    }

    public String getCleanType() {
        return cleanType;
    }

    public void setCleanType(String cleanType) {
        this.cleanType = cleanType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCleanName() {
        return cleanName;
    }

    public void setCleanName(String cleanName) {
        this.cleanName = cleanName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public BigDecimal getThriePrice() {
        return thriePrice;
    }

    public void setThriePrice(BigDecimal thriePrice) {
        this.thriePrice = thriePrice;
    }


    @Override
    public String toString() {
        return "PriceDetailsDto{" +
                "cleanType='" + cleanType + '\'' +
                ", carType='" + carType + '\'' +
                ", cleanName='" + cleanName + '\'' +
                ", carName='" + carName + '\'' +
                ", originalPrice=" + originalPrice +
                ", preferentialPrice=" + preferentialPrice +
                ", thriePrice=" + thriePrice +
                ", contractProject=" + contractProject +
                ", residueDegree='" + residueDegree + '\'' +
                ", beautyType='" + beautyType + '\'' +
                ", beautyName='" + beautyName + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
