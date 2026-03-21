package com.cheji.web.modular.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AlipayNotifyDto {

    //交易状态
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    public static final String TRADE_CLOSED = "TRADE_CLOSED";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String TRADE_FINISHED = "TRADE_FINISHED";

    private String charset;
    private String gmt_create;
    private String gmt_payment;
    private String notify_time;
    private String subject;
    private String sign;
    private String buyer_id;
    private String invoice_amount;
    private String version;
    private String notify_id;
    private String fund_bill_list;
    private String notify_type;
    @NotEmpty(message = "订单号为空")
    private String out_trade_no;
    private String total_amount;
    //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、	TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
    private String trade_status;
    private String trade_no;
    private String auth_app_id;
    private String receipt_amount;
    private String point_amount;
    private String app_id;
    private String buyer_pay_amount;
    private String sign_type;
    private String seller_id;
    private String code;
    private String msg;
    private String passback_params;

    //交易成功
    @JSONField(serialize = false)
    public boolean isTradeSuccess(){
        return this.trade_status.equals(TRADE_SUCCESS) || this.trade_status.equals(TRADE_FINISHED);
    }

    @JSONField(serialize = false)
    public boolean isTradeWit(){
        return this.trade_status.equals(WAIT_BUYER_PAY);
    }

    @JSONField(serialize = false)
    public boolean isTradeFail(){
        //未付款交易超时关闭，或支付完成后全额退款。
        return this.trade_status.equals(TRADE_CLOSED);
    }
}