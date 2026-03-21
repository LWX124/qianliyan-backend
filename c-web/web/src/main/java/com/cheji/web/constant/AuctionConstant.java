package com.cheji.web.constant;

public class AuctionConstant {
    public static final String AUCTION_TEMP_ORDER = "auction_temp_order";
    //记录拍卖最高价
    public static final String KEY_AUCTION_BID_TOKEN = "auction_car_id:%s";

    //记录进入过拍卖详情页的用户  （发长连接用）  key=CAR_DETAIL_INFO_SET:#{carId} -> [userId1,userId2]
    public static final String CAR_DETAIL_INFO_SET = "CAR_DETAIL_INFO_SET:";

    //记录出价记录
    public static final String KEY_AUCTION_BID_LIST = "KEY_AUCTION_BID_LIST:";


    public static final String OLD_CAR_BROWSE_NUM = "OLD_CAR_BROWSE_NUM:";

}
