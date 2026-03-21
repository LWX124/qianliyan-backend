package com.stylefeng.guns.modular.system.constant;

/**
 * 支付宝支付结果状态
 *
 * @author kosan
 * @Date 2017年1月10日 下午9:54:13
 */
public enum BizClaimType {

    INIT(-1, "未知订单"),
    SGDB(0, "事故代办"),
    WSPF(1, "物损赔付"),
    RSDF(2, "人伤垫付"),
    JDLP(3, "交单理赔"),
    FLFW(4, "法律服务"),
    SGXCLP(5, "代步车"),
    DBC(6, "事故咨询"),
    QTYW(7, "现场理赔"),
    UPKEEP(8, "保养"),
    PAINT(9, "油漆"),
    METAL(10, "钣金"),
    MAINTAIN(11, "维修");
    Integer code;
    String message;

    BizClaimType(Integer code, String message) {
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
            for (BizClaimType ms : BizClaimType.values()) {
                if (ms.getCode() == value) {
                    return ms.getMessage();
                }
            }
            return "";
        }
    }
}
