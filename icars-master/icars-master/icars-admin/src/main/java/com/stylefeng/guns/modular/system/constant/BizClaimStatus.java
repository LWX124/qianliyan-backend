package com.stylefeng.guns.modular.system.constant;

/**
 * 支付宝支付结果状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizClaimStatus {

    INIT(0, "已下单"),
    CLAIMING(1, "理赔中"),
    CLAIMED(2, "理赔完成"),
    CANCEL(3, "已作废");

    Integer code;
    String message;

    BizClaimStatus(Integer code, String message) {
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
            for (BizClaimStatus ms : BizClaimStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
