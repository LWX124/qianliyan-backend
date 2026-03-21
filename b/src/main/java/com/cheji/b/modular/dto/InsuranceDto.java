package com.cheji.b.modular.dto;

import java.util.Arrays;

public class InsuranceDto {
    private Integer[] id;

    public Integer[] getId() {
        return id;
    }

    public void setId(Integer[] id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "InsuranceDto{" +
                "id=" + Arrays.toString(id) +
                '}';
    }
}
