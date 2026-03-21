package com.cheji.b.modular.dto;

import java.util.Arrays;

public class LableDto {
    private String remake;
    private Integer lableId;
    private DetailsListDto[] listDtos;

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public Integer getLableId() {
        return lableId;
    }

    public void setLableId(Integer lableId) {
        this.lableId = lableId;
    }

    public DetailsListDto[] getListDtos() {
        return listDtos;
    }

    public void setListDtos(DetailsListDto[] listDtos) {
        this.listDtos = listDtos;
    }

    @Override
    public String toString() {
        return "LableDto{" +
                "remake='" + remake + '\'' +
                ", lableId=" + lableId +
                ", listDtos=" + Arrays.toString(listDtos) +
                '}';
    }
}
