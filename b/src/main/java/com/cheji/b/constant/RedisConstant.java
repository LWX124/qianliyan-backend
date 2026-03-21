package com.cheji.b.constant;

public interface RedisConstant {

    String VIDEO_Z_SET = "VIDEO_Z_SET";

    //视频评论缓存key
    String  COMMENT_VIDEO_COMMENT = "COMMENT:VIDEO_COMMENT_";

    //用户登录标志
    String USER_TOKEN = "USER:USER_TOKEN_";

    //用户登录标志
    String USER_B_TOKEN = "USER_B:USER_TOKEN_";

    String CD_B_TOKEN = "CD_B:USER_TOKEN_";

    //用户token
    String CD_B_ID_TOKEN = "CD_B:ID_TOKEN:";


    //用户token
    String USER_B_ID_TOKEN = "USER_B:ID_TOKEN:";

    //用户注册短信验证码
    String USER_REGISTER_PHONE_CODE = "USER:PHONE_CODE:REGISTER:";

    //用户登录短信验证码
    String USER_LOGIN_PHONE_CODE = "USER:PHONE_CODE:LOGIN:";

    //用户换绑手机号验证码
    String USER_BINDING_PHONE_CODE = "USER:PHONE_CODE:BINDING:";

    //忘记密码
    String USER_PHONE_FORGET_PASS_CODE = "USER:PHONE_CODE:FORGET_PASS:";

    //限制用户获取验证码次数   60s以内只能获取一次
    String USER_PHONE_CODE_TIME = "USER:PHONE_CODE:TIME:";

    //限制用户每天获取验证码次数   一天最多十条
    String USER_PHONE_CODE_LIMIT = "USER:PHONE_CODE:LIMIT:";

    //微信支持的银行卡列表
    String WX_BANK_LIST = "WX_BANK_LIST";


    //用户提现锁
    String ADD_CASH_OUT_LOCK = "ADD_CASH_OUT_LOCK:";

    //plus会员价格
    String PLUS_MEMBER_PRICE = "PLUS_MEMBER_PRICE";

    String HUANXIN_TOKEN = "HUANXIN_TOKEN";

    //商户访问量
    String MERCHANTS_VISIT_COUNT = "MERCHANTS:VISIT_COUNT";

    //商户评论访问量
    String MERCHANTS_COMMENT_COUNT = "MERCHANTS:COMMENT_COUNT";

    //充值
    String MONEY_ADD_LOCK = "LOCK:MONEY_ADD:";

    //所有汽车品牌
    String SYS_ALL_BRAND = "SYS:ALL_BRAND";

    //保存商户到geo中的key
    String MERCHANTS_GEO = "MERCHANTS_GEO:GEO";

    //保存商户到geo中的key
    String MERCHANTS_GEO_REA = "MERCHANTS_GEO:REA";


    //洗车服务平台抽成比例  Car wash service platform ratio
    String CAR_WASH_SERVICE_PLATFORM_RATIO = "CAR_WASH:SERVICE_PLATFORM_RATIO";


    //保存救援商户到geo中的key
    String MERCHANTS_RESCUE_GEO = "MERCHANTS_RESCUE:RESCUE";

    //保存年检商户到geo中的key
    String MERCHANTS_YEAR_CHECK_GEO = "MERCHANTS_YEAR:CHECK_GEO";

    //保存代驾师傅的位置
    String MERCHANTS_SUBSTITUTE_DIRVIE_GEO = "MERCHANTS_SUBSTITUTE:DIRVIE_GEO";

    //保存给c端给b端发信息的id
    String B_ACCOUNT_RECEIVED_MESSAGE = "B_ACCOUNT_RECEIVED_MESSAGE";


}
