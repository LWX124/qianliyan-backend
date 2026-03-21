package com.cheji.web.constant;

public interface AppAuctionConstant {

    String CAR_IMG_ONE = "1";

    String ZEROSTR = "0";//0,代表没有资料,或者order订单未完成
    String ONESTR = "1";//1,代表有资料,或者order订单完成

    String ISDEFAULTADDRESS = "1";//1,代表是默认地址
    String ISNOTDEFAULTADDRESS = "2";//2,代表不是默认地址

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

    String counselorHead = "https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/system/img/ic_service.png";


    String AUCTION_BID_LOCK = "AUCTION_BID_LOCK:";

    String END_BID_ORDER_CARID = "end_bid_order:%s";

    Integer BID_ON = 0;
    Integer BID_OFF = 1;

    Integer CAR_IS_ENABLED = 0;   //推荐启用
    Integer CAR_DEACTIVATE = 1;    //推荐停用

    String IS_DEFAULT_ADDRESS = "1"; //是默认地址


}
