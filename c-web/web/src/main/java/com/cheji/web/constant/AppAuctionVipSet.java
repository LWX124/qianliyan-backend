package com.cheji.web.constant;

public interface AppAuctionVipSet {

    int VIP_ONE_CARCOUNT = 8; //1级会员剩余参拍台次
    int VIP_TWO_CARCOUNT = 20;
    int VIP_THREE_CARCOUNT = 50;
    int VIP_FOUR_CARCOUNT = 100000;

    int VIP_ONE_OFFER = 30;
    int VIP_TWO_OFFER = 80;
    int VIP_THREE_OFFER = 100000;
    int VIP_FOUR_OFFER = 100000;

    int VIP_COMMON_ZERO = 0;
    int VIP_COMMON_ONE = 1;

    String openTitle = "会员特权说明";
    String closeTitle = "保证金说明";

    Integer VIP_STATE_USE = 1;
    Integer VIP_STATE_CASHOUT = 2;
    Integer VIP_STATE_LOSE = 3;




}
