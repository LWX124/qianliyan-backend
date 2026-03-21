package com.cheji.web.modular.dto;

import com.cheji.web.modular.domain.AppAuctionOrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AuctionMyDto {

    private String signLv;       //签约等级

    private BigDecimal amount;      //签约保证金

    private String head;        //头像

    private String username;        //用户名

    private String authentication;        //认证,null没认证,1个人认证,2企业

    private BigDecimal serviceFee;      //服务费

    private BigDecimal penalty;     //单车保证金总数

    private BigDecimal capital;     //我的资金

    private Integer bidCar;     //竞买车
    private Integer waitTransfer;     //待过户
    private Integer transfered;     //已过户
    private Integer warnCar;     //提醒车
    private Integer collectCar;     //关注车

    private List<AppAuctionOrderEntity> orderList;

}
