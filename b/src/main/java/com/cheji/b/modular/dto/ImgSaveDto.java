package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.MerchantsInfoBannerEntity;

import java.util.Arrays;
import java.util.List;

//保存图片
public class ImgSaveDto {
   // private MerchantsInfoBannerEntity masterImg;
    private List<MerchantsInfoBannerEntity> otherImg;

    public List<MerchantsInfoBannerEntity> getOtherImg() {
        return otherImg;
    }

    public void setOtherImg(List<MerchantsInfoBannerEntity> otherImg) {
        this.otherImg = otherImg;
    }

    @Override
    public String toString() {
        return "ImgSaveDto{" +
                "otherImg=" + otherImg +
                '}';
    }
}
