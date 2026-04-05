package com.cheji.web.pojo;

import com.cheji.web.modular.domain.AppUserEntity;

public class TokenPojo {

    private String thirdSessionKey;

    private String unionId;

    private AppUserEntity appUserEntity;
    private String source;

    @Override
    public String toString() {
        return "TokenPojo{" +
                "thirdSessionKey='" + thirdSessionKey + '\'' +
                ", unionId='" + unionId + '\'' +
                ", appUserEntity=" + appUserEntity +
                ", source='" + source + '\'' +
                '}';
    }

    public String getThirdSessionKey() {
        return thirdSessionKey;
    }

    public void setThirdSessionKey(String thirdSessionKey) {
        this.thirdSessionKey = thirdSessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public AppUserEntity getAppUserEntity() {
        return appUserEntity;
    }

    public void setAppUserEntity(AppUserEntity appUserEntity) {
        this.appUserEntity = appUserEntity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
