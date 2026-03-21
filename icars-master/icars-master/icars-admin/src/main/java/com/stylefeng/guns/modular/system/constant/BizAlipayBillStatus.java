package com.stylefeng.guns.modular.system.constant;

/**
 * 支付宝支付结果状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizAlipayBillStatus {

    SUCCESS(0, "支付成功"),
    FAIL(1, "支付失败");

    Integer code;
    String message;

    BizAlipayBillStatus(Integer code, String message) {
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
            for (BizAlipayBillStatus ms : BizAlipayBillStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
