package com.jeesite.modules.constant2;

public class AppAuctionRefundConstans {
    //redis缓存拼接key的前缀
    public static final Integer AUCTION_REFUND_INITIAL = 0;//退款初始状态
    public static final Integer AUCTION_REFUND_SUCCESS = 1;//退款成功
    public static final Integer AUCTION_REFUND_FAIL = 2;//退款关闭(失败)
    public static final Integer AUCTION_REFUND_RUNNING = 3;//退款处理中
    public static final String AUCTION_REFUND_DESC = "拍卖保证金";//退款处理中


}
