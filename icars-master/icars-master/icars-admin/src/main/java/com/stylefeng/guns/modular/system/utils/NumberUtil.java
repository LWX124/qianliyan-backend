package com.stylefeng.guns.modular.system.utils;

import java.util.regex.Pattern;

public class NumberUtil {
    static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

    /**
     * 验证字符串是否是纯数字
     * @param str 字符
     * @return true  是存数字   false  不是
     */
    public static boolean validNumber(String str){
        return pattern.matcher(str).matches();
    }
}
