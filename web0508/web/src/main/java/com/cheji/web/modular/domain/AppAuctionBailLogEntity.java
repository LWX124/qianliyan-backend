package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单车保证金支付记录
 */
@TableName("app_auction_bail_log")
@Data
public class AppAuctionBailLogEntity {
    @TableField("out_trade_no")
    private String outTradeNo;

    private BigDecimal amount;
    private Integer status; //支付状态：1初始状态 2支付成功 3支付失败 4已退款
    @TableField("user_id")
    private Integer userId;
    private Integer type; // 1:微信开通vip
    @TableField("prepay_id")
    private String prepayId;
    @TableField("notify_time")
    private Date notifyTime;
    @TableField("create_time")
    private Date createTime;
    @TableField("car_id")
    private Long carId;
}
