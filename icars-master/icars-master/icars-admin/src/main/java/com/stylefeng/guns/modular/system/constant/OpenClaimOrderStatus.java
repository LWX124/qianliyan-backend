package com.stylefeng.guns.modular.system.constant;

/**
 * 管理员的状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum OpenClaimOrderStatus {

    INIT(-1, "未接车"), RECEPTED(0, "已接车"), SERVICING(1, "服务中"), PLACED(2, "已交车"), SETTLED(3, "已结算"), TRANSED(4, "打款完成"), CANCEL(5, "作废"), UNREACH(6, "未到店"),CERTRECEIVE(7,"确认接车"),CERTMONEY(8,"已定损");

    int code;
    String message;

    OpenClaimOrderStatus(int code, String message) {
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
            for (OpenClaimOrderStatus ms : OpenClaimOrderStatus.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
