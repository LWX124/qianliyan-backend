package com.stylefeng.guns.modular.system.constant;

/**
 * 外部理赔用户预存款操作明细类型
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizYckCzmxStatus {

    INIT(-1, "未知类型"), RECHARGE(0, "充值"),  EXPEND(1, "事故费用支出");

    int code;
    String message;

    BizYckCzmxStatus(int code, String message) {
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
            for (BizYckCzmxStatus ms : BizYckCzmxStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
