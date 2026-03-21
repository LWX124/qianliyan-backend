package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.LableDetailsEntity;

import java.util.List;

//服务明细
public class ProjectDetailsDto {
    private String reson;
    private String remake;
    private List<LableDetailsEntity> lableDetailsList;

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public List<LableDetailsEntity> getLableDetailsList() {
        return lableDetailsList;
    }

    public void setLableDetailsList(List<LableDetailsEntity> lableDetailsList) {
        this.lableDetailsList = lableDetailsList;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    @Override
    public String toString() {
        return "ProjectDetailsDto{" +
                "reson='" + reson + '\'' +
                ", remake='" + remake + '\'' +
                ", lableDetailsList=" + lableDetailsList +
                '}';
    }
}
