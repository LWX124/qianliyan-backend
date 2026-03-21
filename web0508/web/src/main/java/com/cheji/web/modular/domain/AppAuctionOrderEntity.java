package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 拍卖订单
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_order")
@Data
public class AppAuctionOrderEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //关联拍卖车id
    @TableField("car_id")
    private Long carId;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
    //订单号

    @TableField("order_no")
    private String orderNo;
    //订单总价
    @TableField("order_amount")
    private BigDecimal orderAmount;
    //服务价格
    @TableField("service_fee")
    private BigDecimal serviceFee;

    @TableField("user_id")
    private Long userId;
    //车辆描述
    private String desc;
    //订单状态
    private Integer state;
}
