package com.cheji.b.constant;

/**
 * 常量
 */
public enum ConsEnum {
    //汽车品牌状态
    CAR_BRAND_OK(1, "汽车品牌 有效"),
    CAR_BRAND_DEL(2, "汽车品牌 无效"),


    //订单business_status  业务状态
    WX_PAY_ORDER_BUSINESS_STATUS_NOT(1, "未处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_OPS(2, "已处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_DEFAULT(3, "不用处理"),

    //统一下单订单支付状态
    WX_PAY_ORDER_DEFAULT(1, "默认状态"),
    WX_PAY_ORDER_SUCCESS(2, "支付成功"),
    WX_PAY_ORDER_FAIL(3, "支付失败"),

    //充值场景 统一下单类型
    PLUS_MEMBER(1, "开通plus会员"),
    INFO_INVEST (2, "信息费充值"),
    PARTNER_INVEST(3, "b端商户充值"),

    //提现订单状态
    CASH_OUT_DEFAULT(1, "默认"),
    CASH_OUT_SUCCESS(2, "提交成功"),
    CASH_OUT_FAIL(3, "提交失败"),

    //用户银行卡状态
    USER_BANK_STATUS_OK(1, "有效"),
    USER_BANK_STATUS_UNOK(2, "无效"),
    USER_BANK_STATUS_DEL(3, "删除"),

    //提现
    CASH_OUT_CLIENT(1, "c端提现"),
    CASH_OUT_BUSINESS(2, "b端提现"),

    //提现记录表 状态
    C_SOURECE(1, "c端"),
    B_SOURCE(2, "b端");
    private Integer code;
    private String info;

    ConsEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
