package com.stylefeng.guns.modular.system.constant;

/**
 * 管理员的状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum AccdStatus {

    INIT(1, "未审核"), CHECK_SUCCESS(2, "审核通过"), CHECK_FAIL(3, "审核未通过");

    int code;
    String message;

    AccdStatus(int code, String message) {
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
            for (AccdStatus ms : AccdStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
