package com.cheji.b.pojo;

import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.CdUserEntity;

public class TokenPojo {

    private String thirdSessionKey;

    private String unionId;

    private AppUserEntity appUserEntity;

    private CdUserEntity cdUserEntity;

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

    public CdUserEntity getCdUserEntity() {
        return cdUserEntity;
    }

    public void setCdUserEntity(CdUserEntity cdUserEntity) {
        this.cdUserEntity = cdUserEntity;
    }
}
