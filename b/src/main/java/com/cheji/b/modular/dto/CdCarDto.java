package com.cheji.b.modular.dto;

import com.cheji.b.modular.domain.CdCarEntity;

import java.util.List;

public class CdCarDto {

    private String name;

    private Integer id;

    private Integer parentId;

    private Integer hierarchy;

    private List<CdCarDto> carEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public List<CdCarDto> getCarEntities() {
        return carEntities;
    }

    public void setCarEntities(List<CdCarDto> carEntities) {
        this.carEntities = carEntities;
    }
}
