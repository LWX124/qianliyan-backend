package com.stylefeng.guns.modular.system.constant;

/**
 * 事故推送记录状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum PushRecordStatus {

    INIT(0, "未查看"), CHECK_SUCCESS(1, "已查看");

    int code;
    String message;

    PushRecordStatus(int code, String message) {
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
            for (PushRecordStatus ms : PushRecordStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
