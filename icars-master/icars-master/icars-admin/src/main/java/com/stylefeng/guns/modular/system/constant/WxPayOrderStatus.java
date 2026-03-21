package com.stylefeng.guns.modular.system.constant;

/**
 * 微信订单支付的状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum WxPayOrderStatus {

    INIT(0, "未支付"), PAY_SUCCESS(1, "支付成功"), PAY_FAIL(2, "支付失败");

    int code;
    String message;

    WxPayOrderStatus(int code, String message) {
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
            for (WxPayOrderStatus ms : WxPayOrderStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
