package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * vip保证金支付记录
 */
@TableName("app_auction_pay_log")
@Data
public class AppAuctionPayLogEntity {
    @TableField("out_trade_no")
    private String outTradeNo;

    private BigDecimal amount;
    private Integer status; //支付状态：1 初始状态  2 支付成功  3 支付失败
    @TableField("user_id")
    private Integer userId;
    private Integer type; // 1:微信开通vip
    @TableField("prepay_id")
    private String prepayId;
    @TableField("notify_time")
    private Date notifyTime;
    @TableField("create_time")
    private Date createTime;
    @TableField("vip_lv")
    private String vipLv;
}
