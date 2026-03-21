package com.stylefeng.guns.modular.system.constant;

/**
 * 汽车保养套餐订单状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum MaintainOrderStatus {

    CREATED("CREATED", "已下单"),
    RECEPT("RECEPT", "已接车"),
    FINISH("FINISH", "已完成"),
    CANCELED("CANCELED", "已取消");

    String code;
    String message;

    MaintainOrderStatus(String code, String message) {
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
