package com.cheji.web.constant;

/**
 * 常量
 */
public enum ConsEnum {
    //拍卖图片状态
    AUCTION_IMG_CAR(1, "车辆图片"),
    AUCTION_IMG_INSURANCE(2, "保险资料"),
    AUCTION_IMG_TRANSFER_3(3, "过户资料"),
    AUCTION_IMG_TRANSFER_4(4, "过户资料"),
    AUCTION_IMG_TRANSFER_5(5, "过户资料"),
    //汽车品牌状态
    CAR_BRAND_OK(1, "汽车品牌 有效"),
    CAR_BRAND_DEL(2, "汽车品牌 无效"),

    //充值场景
    C_PLUS_MEMBER(1, "c端"),
    B_INFO(2, "b端 信息费"),
    B_PARTNER(3, "b端 商户充值"),

    //订单business_status  业务状态
    WX_PAY_ORDER_BUSINESS_STATUS_NOT(1, "未处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_OPS(2, "已处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_DEFAULT(3, "不用处理"),

    //统一下单订单支付状态
    WX_PAY_ORDER_DEFAULT(1, "默认状态"),
    WX_PAY_ORDER_SUCCESS(2, "支付成功"),
    WX_PAY_ORDER_FAIL(3, "支付失败"),
    WX_PAY_ORDER_REFUND(4, "已退款"),

    //统一下单类型
    PLUS_MEMBER(1, "开通plus会员"),
    INFO_INVEST (2, "信息费充值"),
    CLEAN_CAR_PAY(4, " 4：洗车"),
    BEAUTY_CAR_PAY(5,"5:美容"),
    RESCUE_CAR_PAY(6,"6:救援"),
    SPRAY_CAR_PAY(7,"7:喷漆"),
    CHECK_CAR_PAY(8,"8:年检"),
    SUBSITUDE_CAR_PAY(9,"9:代驾"),
    MESSAGE_CAR_PAY(10,"10：信息"),

    //用户银行卡状态
    USER_BANK_STATUS_OK(1, "有效"),
    USER_BANK_STATUS_UNOK(2, "无效"),
    USER_BANK_STATUS_DEL(3, "删除"),

    //提现订单状态
    CASH_OUT_DEFAULT(1, "默认"),
    CASH_OUT_SUCCESS(2, "提交成功"),
    CASH_OUT_FAIL(3, "提交失败"),

    //提现
    CASH_OUT_CLIENT(1, "c端提现"),
    CASH_OUT_BUSINESS(2, "b端提现"),
    CASH_OUT_VIP(3, "vip提现"),

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
