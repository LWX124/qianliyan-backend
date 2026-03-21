package com.cheji.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateDigitalUtil {

    public static String getOrderNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum= (int)(random.nextDouble()*(99999-10000 + 1))+ 10000;// 获取5位随机数
        return str + rannum;// 当前时间 + 系统5随机生成位数
    }
}
