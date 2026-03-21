package com.cheji.web.constant;

public interface RedisConstant {

    String APP_VIDEO_LIST = "APP_VIDEO_LIST";

    //首页视频信息
    String  APP_FIRST_VIDEO = "APP_FIRST_VIDEO";

    //app首页话术
    String APP_HOME_TEXT = "APP_HOME_TEXT";

    //加油分享话术
    String APP_OIL_SHARE_TEXT = "APP_OIL_SHARE_TEXT";

    //用户手机标识
    String USER_PHONE_CODE = "USER:PHONE_CODE:";

    //视频评论缓存key
    String COMMENT_VIDEO_COMMENT = "COMMENT:VIDEO_COMMENT_";

    //用户登录标志
    String USER_TOKEN = "USER:USER_TOKEN_";

    //用户token
    String USER_ID_TOKEN = "USER:ID_TOKEN:";

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

    //拍卖出价
    String AUCTION_BID_LOCK = "AUCTION_BID_LOCK:";

    //plus会员价格
    String PLUS_MEMBER_PRICE = "PLUS_MEMBER_PRICE";

    String HUANXIN_TOKEN = "HUANXIN_TOKEN";

    //保存用户点赞数据的key
    String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";

    //保存视频点赞数据的key
    String MAP_KEY_VIDEO_LIKED = "MAP_KEY_VIDEO_LIKED";

    //保存视频被点赞数量的key
    String MAP_KEY_VIDEO_LIKED_COUNT = "MAP_KEY_VIDEO_LIKED_COUNT";

    //用户点赞得视频
    String LIKE_USER_THUMBS_VIDEO = "LIKE:USER_THUMBS_VIDEO";

    //所有汽车品牌
    String SYS_ALL_BRAND = "SYS:ALL_BRAND";

    //所有汽车品牌
    String SYS_PUSH_BRAND = "SYS:PUSH_BRAND";

    //首页视频点赞数据
    String SET_VIDEO_THUMBS = "VIDEO:THUMBS:";

    //商户访问数量
    String MERCHANTS_VISIT_COUNT = "MERCHANTS:VISIT_COUNT";

    //视频一级评论内容的key
    String COMMENT_VIDEO_LEVEL_ONE = "COMMENT:VIDEO_COMMENT:LEVEL_ONE";

    //视频一级评论之外内容得key
    String COMMENT_VIDEO_LEVEL_OTHER = "COMENT:VIDEO_OTHER_COMMENT:OTHER";

    //视频评论点赞数据
    String COMMENT_VIDEO_THUMBS_COUNT = "COMMENT:VIDEO_THUMBS:THUMBS_COUNT";

    //推荐城市key
    String RECOMMEND_CITY = "RECOMMEND:CITY";

    //放结算一级plus会员的金额比例
    String AMOUNT_FIRST_AMOUNT_PROPORTION = "AMOUNT:FIRST_AMOUNT_PROPORTION";

    //放结算二级plus会员的金额比例
    String AMOUNT_SECOND_AMOUNT_PROPORTION = "AMOUNT:SECOND_AMOUNT_PROPORTION";


    //所有人免费开通plus会员开关
    String  PLUS_MEMBER_FREE_FLAG = "PLUS_MEMBER_FREE_FLAG";

    //开通plus会员redis锁
    String PLUS_MEMBER_LOCK = "LOCK:PLUS_MEMBER:";

    //开通plus会员原价
    String PLUS_USER_ORIGINAL_PRICE = "PLUS_USER:ORIGINAL_PRICE";

    //开通plus会员优惠后价
    String PLUS_USER_PREFERENTIAL_PRICE = "PLUS_USER:PREFERENTIAL_PRICE";

    //plus会员营销代理权
    String PLUS_USER_MARKETING_AGENCY = "PLUS_USER:MARKETING_AGENCY";

    //plus会员养车专区
    String PLUS_USER_MAINTAIN_ZONE = "PLUS_USER:MAINTAIN_ZONE";

    //plus会员页面温馨提示
    String PLUS_USER_WARM_PROMPT = "PLUS_USER:WARM_PROMPT";

    //商户评论访问量
    String MERCHANTS_COMMENT_COUNT = "MERCHANTS:COMMENT_COUNT";

    //商户订单二级开关key
  //  String MERCHANTS_PAY_SWITCH = "MERCHANTS_PAY_SWITCH";

    //商户结算给用户实际比例
  //  String MERCHANTS_REBATES = "MERCHANTS_REBATES";

    //分享标题
    String SHARE_TITLE = "SHARE_TITLE";

    //分享内容
    String SHARE_CONTENT = "SHARE_CONTENT";

   // String MERCHANTS_GEO = "MERCHANTS_GEO:GEO";


   // String CAR_BRAND_CACHE = "CAR_BRAND_CACHE";

    //分享数次
    String VIDEO_SHARE = "VIDEO:SHARE";

    //上传视频的key
    String VIDEO_UP = "VIDEO:UP:SAME";

    //上报视频的geo
    String ACCIDENT_GEO = "ACCIDENT_GEO:ACCID";

    //限行
    String LIMIT_DATE_ROW = "LIMIT:DATE_ROW";

    //洗车服务平台抽成比例  Car wash service platform ratio
    String CAR_WASH_SERVICE_PLATFORM_RATIO = "CAR_WASH:SERVICE_PLATFORM_RATIO";

    //洗车服务Plus会员提成价格  Car wash membership royalty rates
    String CAR_WASH_MEMBERSHIP_ROYALTY_RATES = "CAR_WASH:MEMBERSHIP_ROYALTY_RATES";

    //推荐商户key
    String RECOMMEND_MERCHANTS = "RECOMMEND_MERCHANTS";

    //推荐首页商户
    String RECOMMEND_HOME_PAGE_MERCHANTS = "RECOMMEND_HOME_PAGE_MERCHANTS";

    //保存救援商户到geo中的key
    String MERCHANTS_RESCUE_GEO = "MERCHANTS_RESCUE:RESCUE";

    //年检价格key 线上
    String YEAR_CHECK_IN_ONLINE = "YEAR_CHECK:IN_ONLINE";

    //年检价格线下
    String YEAR_CHECK_ON_OFFLINE = "YEAR_CHECK:ON_OFFLINE";

    //保存年检商户到geo中的key
    String MERCHANTS_YEAR_CHECK_GEO = "MERCHANTS_YEAR:CHEzCK_GEO";

    //保存代驾师傅的位置
    String MERCHANTS_SUBSTITUTE_DIRVIE_GEO = "MERCHANTS_SUBSTITUTE:DIRVIE_GEO";

    //保存给c端给b端发信息的id
    String B_ACCOUNT_RECEIVED_MESSAGE = "B_ACCOUNT_RECEIVED_MESSAGE";
    //The B-side account that received the message


    //保存商户到geo中的key
    String MERCHANTS_GEO = "MERCHANTS_GEO:GEO";


    //保存商户到geo中的key实际位置
    String MERCHANTS_GEO_REA = "MERCHANTS_GEO:REA";


    //理赔老师key
    String CLAIM_TEACHER_ADD = "CLAIM_TEACHER:ADD";

    //店铺key
    String CLAIM_TEACHER_MER = "CLAIM_TEACHER:MER";


    //视频地址
    String APP_VIDEO_URL =  "APP_VIDEO_URL";

//    String AUCATION_







}
