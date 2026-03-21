package com.jeesite.modules.constant2;

public interface AppAuctionConstant {

    String CAR_IMG_ONE = "1";

    Integer CAR_ALL_IS_ENABLED = 0;   //推荐启用
    Integer CAR_ALL_DEACTIVATE = 1;    //推荐停用

    Integer ZERO = 0;//0,保存
    Integer ONE = 1;//1,待审核
    Integer TWO = 2;//2,未通过
    Integer THREE = 3;//3,通过
    Integer FOUR = 4;
    Integer FIVE = 5;
    Integer SIX = 6;
    Integer SEVEN = 7;//7,拍卖中
    Integer EIGHT = 8;//8,拍卖完成
    Integer NINE = 9;//9,流拍
    Integer TEN = 10;//10,过户审核
    Integer ELEVEN = 11;//11,过户审核未通过
    Integer TWELVE = 12;//12过户完成
    Integer FIFTEEN = 3;//自动拍卖天数


    Integer UP_STATE_DOWN = 0;//未上架
    Integer UP_STATE_UP = 1;//已上架

    String SUCCESS = "200";

    String CAR_AUCTION_OVERDUE_KEY = "CAR_AUCTION_OVERDUE_KEY:";
    String BEGIN_BID_ORDER_CARID = "begin_bid_order:%s";

    String AUCTION_BID_LOCK = "AUCTION_BID_LOCK:";

    String TRANSFER_INFORMATION = "345:";  //过户资料

    Integer BID_ON = 0; //拍卖启用
    Integer BID_OFF = 1;    //拍卖停用

}
