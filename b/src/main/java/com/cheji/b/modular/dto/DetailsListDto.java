package com.cheji.b.modular.dto;

import java.math.BigDecimal;

public class DetailsListDto {
    private Integer detailsId;  //具体id
    private BigDecimal rebats;  //返点比例

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

    public BigDecimal getRebats() {
        return rebats;
    }

    public void setRebats(BigDecimal rebats) {
        this.rebats = rebats;
    }

    @Override
    public String toString() {
        return "DetailsListDto{" +
                "detailsId=" + detailsId +
                ", rebats=" + rebats +
                '}';
    }
}

