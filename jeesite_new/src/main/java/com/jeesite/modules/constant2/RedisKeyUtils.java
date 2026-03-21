package com.jeesite.modules.constant2;

public class RedisKeyUtils {
    //商户结算给用户实际比例
    public static final String MERCHANTS_REBATES = "MERCHANTS_REBATES";

     //商户订单二级开关key
    public static final String MERCHANTS_PAY_SWITCH = "MERCHANTS_PAY_SWITCH";

     //app首页话术
    public static final String APP_HOME_TEXT = "APP_HOME_TEXT";

     //加油分享话术
    public static final String APP_OIL_SHARE_TEXT = "APP_OIL_SHARE_TEXT";

    //分享标题
    public static final String SHARE_TITLE = "SHARE_TITLE";

    //分享内容
    public static final String SHARE_CONTENT = "SHARE_CONTENT";

    //保存商户到geo中的key
    public static final String MERCHANTS_GEO = "MERCHANTS_GEO:GEO";

    //保存上架商户到geo中的key
    public static final String MERCHANTS_UP_GEO = "MERCHANTS_UP_GEO:GEO";

    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存用户被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    //保存视频点赞数据的key
    public static final String MAP_KEY_VIDEO_LIKED = "MAP_KEY_VIDEO_LIKED";
    //保存视频被点赞数量的key
    public static final String MAP_KEY_VIDEO_LIKED_COUNT = "MAP_KEY_VIDEO_LIKED_COUNT";

    //放结算一级plus会员的金额比例
    public static final String AMOUNT_FIRST_AMOUNT_PROPORTION = "AMOUNT:FIRST_AMOUNT_PROPORTION";

    //放结算二级plus会员的金额比例
    public static final String AMOUNT_SECOND_AMOUNT_PROPORTION = "AMOUNT:SECOND_AMOUNT_PROPORTION";

    public static final String BANK_PUBLIC_KEY = "BANK_PUBLIC_KEY";

    //缓存品牌记录
    public static final String CAR_BRAND_CACHE = "CAR_BRAND_CACHE";

    //用户token
    public static final String USER_B_ID_TOKEN = "USER_B:ID_TOKEN:";

    //用户登录标志
    public static final String USER_B_TOKEN = "USER_B:USER_TOKEN_";

    //事故保存GEO
    public static final String ACCIDENT_GEO = "ACCIDENT_GEO:ACCID";

    //退款回调通知
    public static final String REDIS_LOCK_REFUND_NOTIFY = "REDIS_LOCK_REFUND_NOTIFY";

    //洗车取消订单
    public static final String CANCEL_CLEAN_INDENT = "CANCEL_CLEAN_INDENT";

    //保存救援商户到geo中的key
    public static final String MERCHANTS_RESCUE_GEO = "MERCHANTS_RESCUE:RESCUE";

    //保存年检商户到geo中的key
    public static final String MERCHANTS_YEAR_CHECK_GEO = "MERCHANTS_YEAR:CHECK_GEO";

    //取消救援支付订单
    public static final String CANCEL_RESCUE_INDENT = "CANCEL_RESCUE_INDENT";

    //取消年检支付订单
    public static final String CANCEL_YEARCHECK_INDENT ="CANCEL_YEARCHECK_INDENT";

    //取消代驾支付订单
    public static final String CANCEL_SUBDRIVE_INDENT ="CANCEL_SUBDRIVE_INDENT";

    //商户访问数量
    public static final String MERCHANTS_VISIT_COUNT = "MERCHANTS:VISIT_COUNT";

    //保存代驾师傅的位置
    public static final String MERCHANTS_SUBSTITUTE_DIRVIE_GEO = "MERCHANTS_SUBSTITUTE:DIRVIE_GEO";

    //理赔老师key
    public static final String CLAIM_TEACHER_ADD = "CLAIM_TEACHER:ADD";

    public static final String CLAIM_TEACHER_MER = "CLAIM_TEACHER:MER";

    //环信redis
    public static final String HUANXIN_TOKEN = "HUANXIN_TOKEN";

    public static final String MESSAGE_AMOUNT = "MESSAGE_AMOUNT";
    public static final String ONE_PRICE_REFUND = "ONE_PRICE_REFUND";

    /**
     * 拼接被点赞的评论id和点赞的人的id作为key。格式 222222::333333
     * @param videoCommontsId 被点赞视频评论的id
     * @param userId 点赞的人的id
     * @return
     */
    public static String getKey(String videoCommontsId, String userId){
        StringBuilder builder = new StringBuilder();
        builder.append(videoCommontsId);
        builder.append("::");
        builder.append(userId);
        return builder.toString();
    }
}
