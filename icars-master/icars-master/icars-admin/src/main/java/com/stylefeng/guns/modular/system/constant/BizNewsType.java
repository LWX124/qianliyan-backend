package com.stylefeng.guns.modular.system.constant;

/**
 * 支付宝支付结果状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizNewsType {

    INIT(-1, "未知类型"),
    SGDB(0, "事故代办"),
    WSPF(1, "物损赔付"),
    RSDF(2, "人伤垫付"),
    JDLP(3, "交单理赔"),
    FLFW(4, "法律服务"),
    SGXCLP(5, "代步车"),
    DBC(6, "事故咨询"),
    QTYW(7, "现场理赔"),
    GDXW(8, "滚动新闻"),
    FX(9, "发现"),
    ZX(10, "资讯"),
    APPHOME(11, "APP首页"),
    XCXNEWSLIST(12, "小程序首页新闻");

    Integer code;
    String message;

    BizNewsType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer value) {
        if (value == null) {
            return "";
        } else {
            for (BizNewsType ms : BizNewsType.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
