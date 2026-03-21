package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 交易记录
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_transaction_log")
@Data
public class AppAuctionTransactionLogEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    @TableField("create_time")
    private Date createTime;

    private BigDecimal amount;      //交易金额
    private Integer type;        //交易类型,1减,2加
    private String desc;        //描述
    @TableField("order_id")
    private String orderId;        //订单编号
    @TableField("order_state")
    private Integer orderState;        //订单描述
    @TableField("pay_type")
    private Integer payType;        //支付方式
    @TableField("car_id")
    private Long carId; //单车保证金的车辆id
}
