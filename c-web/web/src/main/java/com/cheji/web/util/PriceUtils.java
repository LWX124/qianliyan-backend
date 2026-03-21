package com.cheji.web.util;

import java.math.BigDecimal;

/**
 * 价格计算工具类
 */
public class PriceUtils {


    /**
     * 一口价车辆计算订金
     * @param price
     * @return
     */
    public static BigDecimal onePriceDJ(BigDecimal price) {
        //非vip会员需要支付订金
        //五万以上  百分之5
        //五万以下  百分之10

        if (price.compareTo(new BigDecimal(50000)) > 0) {
            //大于5万
            price = price.divide(new BigDecimal(20), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            price = price.divide(new BigDecimal(10), 2, BigDecimal.ROUND_HALF_UP);
        }
        return price;
    }

}
