package com.cheji.web.constant;

public interface AppAuctionWxPayConstant {

    Integer ORDERSTATE_DEFAULT = 1;
    Integer ORDERSTATE_SUCCESS = 2;
    Integer ORDERSTATE_FAIL = 3;

    Integer WX_PAY = 1;

    String WX_PAY_STATE_PAYLOG = "payLog"; //vip充值
    String WX_PAY_STATE_BAILLOG = "bailLog";//保证金充值

}
