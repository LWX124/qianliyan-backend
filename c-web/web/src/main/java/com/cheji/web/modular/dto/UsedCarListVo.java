package com.cheji.web.modular.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UsedCarListVo implements Serializable {
    private Long carId;
    private String brand;           //品牌
    private BigDecimal price;       //一口价
    private String label;           //标签
    private String url;
    private Date productDate;  //车辆注册日期
    private Date registerDate;  //车辆注册日期
    private String mileage;  // 车辆表现公里数
    private Integer collect;  // 是否收藏

    private BigDecimal luxuryCarPrice;  // 二手车——精品车价格

    private BigDecimal firstAmount;  // 二手车——首付价格

    private String phoneNumber;     //电话号码


}
