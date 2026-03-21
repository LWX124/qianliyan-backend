package com.stylefeng.guns.modular.system.constant;

/**
 * 支付宝活动状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum AlipayActivityStatus {

    CREATED("CREATED", "已创建未打款"),
    PAID("PAID", "已打款"),
    READY("READY", "活动已开始"),
    PAUSE("PAUSE", "活动已暂停"),
    CLOSED("CLOSED", "活动已结束"),
    SETTLED("SETTLED", "活动已清算");

    String code;
    String message;

    AlipayActivityStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public static String valueOf(String value) {
//        if (value == null) {
//            return "";
//        } else {
//            for (AlipayActivityStatus ms : AlipayActivityStatus.values()) {
//                if (ms.getCode().equals(value)) {
//                    return ms.getMessage();
//                }
//            }
//            return "";
//        }
//    }
}
