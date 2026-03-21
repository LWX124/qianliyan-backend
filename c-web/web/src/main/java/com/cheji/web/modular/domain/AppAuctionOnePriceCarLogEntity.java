/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * 一口价车辆支付记录Entity
 * @author dh
 * @version 2023-04-16
 */
@TableName("app_auction_one_price_car_log")
@Data
public class AppAuctionOnePriceCarLogEntity{
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private static final long serialVersionUID = 1L;
	@TableField("out_trade_no")
	private String outTradeNo;		// 支付订单号
	private Integer amount;		// 支付金额.单位:分
	@TableField("user_id")
	private Integer userId;		// user_id
	@TableField("prepay_id")
	private String prepayId;		// 预支付id
	@TableField("notify_time")
	private Date notifyTime;		// 支付通知生成时间
	@TableField("create_time")
	private Date createTime;		// 订单生成时间
	@TableField("update_time")
	private Date updateTime;		// 订单生成时间
	private Integer type;		// 支付类型，1 微信 2支付宝
	@TableField("car_id")
	private Long carId;		// 支付保证金车辆的id
	@TableField("vip_lv")
	private Integer vipLv;		// 当前用户支付时候的vip等级
	private Integer status;		// 支付状态：1 初始状态  2 支付成功  3 支付失败 4 不用支付

}