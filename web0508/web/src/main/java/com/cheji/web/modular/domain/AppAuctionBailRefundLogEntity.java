package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单车保证金退款记录
 */
@TableName("app_auction_bail_refund_log")
@Data
public class AppAuctionBailRefundLogEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("out_trade_no")
    private String outTradeNo;//支付单号
    @TableField("out_refund_no")
    private String outRefundNo;//退款单号

    private BigDecimal amount;
    private Integer state; //退款状态：0初始状态 1退款成功 2退款关闭 3退款处理中 4退款异常
    @TableField("user_id")
    private Integer userId;
    @TableField("prepay_id")
    private String prepayId;//微信生成预支付id
    @TableField("notify_time")
    private Date notifyTime;
    @TableField("create_time")
    private Date createTime;
    @TableField("car_id")
    private Long carId;
    @TableField("explain")
    private String explain;//退款失败说明
    @TableField("refund_id")
    private String refundId;		// 微信退款单号
    @TableField("back_status")
    private String backStatus;		// 退款状态
}
