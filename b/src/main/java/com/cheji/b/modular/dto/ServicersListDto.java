package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.MerchantsServicerEntity;

import java.util.List;

//服务顾问
public class ServicersListDto {
    List<MerchantsServicerEntity> servicerList;

    public List<MerchantsServicerEntity> getServicerList() {
        return servicerList;
    }

    public void setServicerList(List<MerchantsServicerEntity> servicerList) {
        this.servicerList = servicerList;
    }

    @Override
    public String toString() {
        return "ServicersListDto{" +
                "servicerList=" + servicerList +
                '}';
    }
}
