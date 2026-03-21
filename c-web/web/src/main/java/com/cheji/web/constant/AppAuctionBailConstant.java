package com.cheji.web.constant;

public interface AppAuctionBailConstant {

    Integer AUCTION_BAIL_INITIAL = 1;//支付初始状态
    Integer AUCTION_BAIL_SUCCESS = 2;//支付成功
    Integer AUCTION_BAIL_FAIL = 3;//支付失败

    Integer REFUND_BAIL_INITIAL = 0;//0初始状态
    Integer REFUND_BAIL_SUCCESS = 1;//1退款成功
    Integer REFUND_BAIL_OFF = 2;//2退款关闭
    Integer REFUND_BAIL_ON = 3;//3退款处理中
    Integer REFUND_BAIL_EXCEPTION = 4;//4退款异常

    String IS_PAY_BAIL = "1";//已支付保证金
    String IS_NOT_PAY_BAIL = "0";//未支付保证金

}
