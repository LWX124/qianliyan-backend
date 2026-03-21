package com.cheji.web.modular.dto;

import com.cheji.web.util.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AuctionCarListDto {

    private String accType;     //车辆状态
    private String fixedPrice;         //是否是一口价 1.一口价 0.拍卖
    @NotNull
    private Integer current;     //当前页
    private Integer pageSize;     //当前页
    private String brand;       //品牌
    private String parkingPlace;       //停放地
    private BigDecimal priceMax;     //起拍价格最大值
    private BigDecimal priceMin;     //起拍价格最小值
    private Integer auctionState;  //1 拍卖中  2 待开始
    private Page page;
}
