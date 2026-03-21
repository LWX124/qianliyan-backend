package com.stylefeng.guns.modular.system.constant;

/**
 * 微信用户是否被列入黑名单
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizWxUserBlackListStatus {

    INIT(0, "否"), IS_BLACKLIST(1, "是");

    int code;
    String message;

    BizWxUserBlackListStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
            for (BizWxUserBlackListStatus ms : BizWxUserBlackListStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
