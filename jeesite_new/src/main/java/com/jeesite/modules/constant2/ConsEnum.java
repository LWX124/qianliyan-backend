package com.jeesite.modules.constant2;

/**
 * 常量
 */
public enum ConsEnum {
    //上架信息表 上架状态
    AUCTION_UP_UP_STATE_NOT_SHELF("0", "未上架"),
    AUCTION_UP_UP_STATE_SHELFED("1", "已上架"),

    //上架信息表 拍卖状态
    AUCTION_UP_AUCTION_STATE_WAIT("0", "待拍卖"),
    AUCTION_UP_AUCTION_STATE_ON("1", "拍卖中"),
    AUCTION_UP_AUCTION_STATE_END("2", "拍卖完成"),

    //上架信息表 过户状态
    AUCTION_UP_TRANSFER_STATE_ON("0", "过户中"),
    AUCTION_UP_TRANSFER_STATE_AUDIT("1", "过户审核"),
    AUCTION_UP_TRANSFER_STATE_FAIL("2", "过户审核失败"),
    AUCTION_UP_TRANSFER_STATE_PASS("3", "过户审核通过"),

    //上架信息表 审核状态
    AUCTION_UP_AUDIT_STATE_SAVE("0", "保存"),
    AUCTION_UP_AUDIT_STATE_AUDIT("1", "待审核"),
    AUCTION_UP_AUDIT_STATE_FAIL("2", "审核失败"),
    AUCTION_UP_AUDIT_STATE_PASS("3", "审核通过"),

    //订单business_status  业务状态
    WX_PAY_ORDER_BUSINESS_STATUS_NOT(1, "未处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_OPS(2, "已处理"),
    WX_PAY_ORDER_BUSINESS_STATUS_DEFAULT(3, "不用处理"),

    //提现记录表 状态
    CASH_OUT_STATUS_DEFAULT(1, "默认"),
    CASH_OUT_STATUS_SUCCESS(2, "提交成功"),
    CASH_OUT_STATUS_FAIL(3, "提交失败");

    private String scode;
    private Integer code;
    private String info;

    ConsEnum(int code, String info){
        this.code = code;
        this.info = info;
    }

    ConsEnum(String scode, String info){
        this.scode = scode;
        this.info = info;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
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
