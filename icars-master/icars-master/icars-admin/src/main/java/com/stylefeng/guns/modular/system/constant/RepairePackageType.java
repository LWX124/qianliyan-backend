package com.stylefeng.guns.modular.system.constant;

/**
 * 维修套餐的状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum RepairePackageType {

    INIT(0, "保养套餐"), REPAIRE_PACKAGE(1, "维修套餐");

    int code;
    String message;

    RepairePackageType(int code, String message) {
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
            for (RepairePackageType ms : RepairePackageType.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
