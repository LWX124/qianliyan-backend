package com.cheji.web.util;
import com.alibaba.fastjson.JSONObject;
import com.cheji.web.exception.GlobalCustomException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtil {
    public static final String PHONE_REGEXP ="^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";
    //手机的正则表达式
    private static final Pattern CHINA_PATTERN_PHONE = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");


    /**--------------------------------------------------------
    手机号断言
    --------------------------------------------------------**/
    public static void isPhone(String phone,String message){
        Matcher m = CHINA_PATTERN_PHONE.matcher(phone);
        if(!m.matches()){
            throw new GlobalCustomException(message);
        }
    }


    /**--------------------------------------------------------
    断言 不为空，如果为空，抛异常
    --------------------------------------------------------**/
    public static void isNotEmpty(String text, String message) {
        if (text == null || text.trim().length() == 0) {
            throw new GlobalCustomException(message);
        }
    }


    /**--------------------------------------------------------
    断言对象为空
    --------------------------------------------------------**/
    public static void isNull(Object obj , String message){
        if(obj != null){
            throw new GlobalCustomException(message);
        }
    }
    public static void isNotNull(Object obj , String message){
        if(obj == null){
            throw new GlobalCustomException(message);
        }
    }

    /**--------------------------------------------------------
     断言false,如果为true,我报错
     --------------------------------------------------------**/
    public static void isFalse(boolean isFalse , String message){
        if(isFalse){
            throw new GlobalCustomException(message);
        }
    }
    public static void isTrue(boolean isTrue , String message){
        if(!isTrue){
            throw new GlobalCustomException(message);
        }
    }


    /**--------------------------------------------------------
     断言两个字符串一致
     --------------------------------------------------------**/
    public static void isEquals(String s1,String s2 , String message){
        isNotEmpty(s1, "不可为空");
        isNotEmpty(s2, "不可为空");
        if(!s1.equals(s2)){
            throw new GlobalCustomException(message);
        }
    }
    public static void isEquals(Integer s1,Integer s2 , String message){
        if(s1 != s2){
            throw new GlobalCustomException(message);
        }
    }
    public static void isEqualsTrim(String s1,String s2 , String message){
        isNotEmpty(s1, "不可为空");
        isNotEmpty(s2, "不可为空");
        if(!s1.trim().equals(s2.trim())){
            throw new GlobalCustomException(message);
        }
    }

    public static void isEqualsIgnoreCase(String s1,String s2 , String message){
        isNotEmpty(s1, "不可为空");
        isNotEmpty(s2, "不可为空");
        if(!s1.trim().equalsIgnoreCase(s2.trim())){
            throw new GlobalCustomException(message);
        }
    }

    public static void isContains(String s1,String s2 , String message){
        isNotEmpty(s1, "不可为空");
        isNotEmpty(s2, "不可为空");
        if(!s1.contains(s2)){
            throw new GlobalCustomException(message);
        }
    }

}
