package com.cheji.web.modular.cwork;

import com.cheji.web.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.domain.CleanPriceDetailEntity;
import com.cheji.web.modular.service.AppBeautyPriceDetailService;

//清洗订单详情
public class CleanIndentDetailsDto {
    private AppBeautyPriceDetailEntity beautyPriceDetailEntity;
    private CleanPriceDetailEntity cleanPriceDetailEntity;
    private CleanIndetEntity cleanIndetEntity;      //订单信息
    private CleanMerDto cleanMerDto;            //商户信息
    private String directionsUse;  //使用说明

    public String getDirectionsUse() {
        return directionsUse;
    }

    public void setDirectionsUse(String directionsUse) {
        this.directionsUse = directionsUse;
    }

    public AppBeautyPriceDetailEntity getBeautyPriceDetailEntity() {
        return beautyPriceDetailEntity;
    }

    public void setBeautyPriceDetailEntity(AppBeautyPriceDetailEntity beautyPriceDetailEntity) {
        this.beautyPriceDetailEntity = beautyPriceDetailEntity;
    }

    public CleanPriceDetailEntity getCleanPriceDetailEntity() {
        return cleanPriceDetailEntity;
    }

    public void setCleanPriceDetailEntity(CleanPriceDetailEntity cleanPriceDetailEntity) {
        this.cleanPriceDetailEntity = cleanPriceDetailEntity;
    }

    public CleanIndetEntity getCleanIndetEntity() {
        return cleanIndetEntity;
    }

    public void setCleanIndetEntity(CleanIndetEntity cleanIndetEntity) {
        this.cleanIndetEntity = cleanIndetEntity;
    }

    public CleanMerDto getCleanMerDto() {
        return cleanMerDto;
    }

    public void setCleanMerDto(CleanMerDto cleanMerDto) {
        this.cleanMerDto = cleanMerDto;
    }

    @Override
    public String toString() {
        return "CleanIndentDetailsDto{" +
                "beautyPriceDetailEntity=" + beautyPriceDetailEntity +
                ", cleanPriceDetailEntity=" + cleanPriceDetailEntity +
                ", cleanIndetEntity=" + cleanIndetEntity +
                ", cleanMerDto=" + cleanMerDto +
                ", directionsUse='" + directionsUse + '\'' +
                '}';
    }
}
