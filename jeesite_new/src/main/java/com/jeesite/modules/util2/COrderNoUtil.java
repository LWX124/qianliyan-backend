package com.jeesite.modules.util2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class COrderNoUtil {

    /**
     * 获得唯一订单号
     */
    public static String getUniqueOrder() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format2 = format.format(new Date());
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            //有可能是负数
            hashCodeV = -hashCodeV;
        }
        return "pk" + format2 + String.format("%012d", hashCodeV);
    }


    /**
     * 一口价订单获得退款单号
     */
    public static String getOnePriceRefundNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format2 = format.format(new Date());
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            //有可能是负数
            hashCodeV = -hashCodeV;
        }
        return "or" + format2 + String.format("%012d", hashCodeV);
    }
}
