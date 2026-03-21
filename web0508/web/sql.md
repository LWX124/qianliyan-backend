-- 2023.4.19 添加字段----------
ALTER TABLE app_auction ADD ins_remark varchar(255) DEFAULT NULL COMMENT '保险备注' AFTER insurance;

-- 2023.4.22 添加字段----------
ALTER TABLE app_user ADD is_inner int(2) DEFAULT '2' COMMENT '是否内部人员，1是 2否' AFTER vip_lv;

ALTER TABLE app_auction_vip_lv
ADD COLUMN `inner_amount` decimal(10, 2) NULL COMMENT '内部人员开通vip价格' AFTER `close_explain`;


UPDATE `app_auction_vip_lv` SET `inner_amount` = 1.00 WHERE `id` = 394;
UPDATE `app_auction_vip_lv` SET `inner_amount` = 2.00 WHERE `id` = 395;
UPDATE `app_auction_vip_lv` SET `inner_amount` = 3.00 WHERE `id` = 396;
UPDATE `app_auction_vip_lv` SET `inner_amount` = 5.00 WHERE `id` = 397;

ALTER TABLE `app_auction`
MODIFY COLUMN `fuel` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '燃油类型' AFTER `transmission`;


CREATE TABLE `app_auction_one_price_car_log` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`out_trade_no` varchar(32) DEFAULT NULL COMMENT '支付订单号',
`amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额.单位:分',
`status` tinyint(2) DEFAULT NULL COMMENT '支付状态：1 初始状态  2 支付成功  3 支付失败 4 不用支付',
`user_id` int(11) DEFAULT NULL,
`prepay_id` varchar(64) DEFAULT NULL COMMENT '预支付id',
`notify_time` datetime DEFAULT NULL COMMENT '支付通知生成时间',
`create_time` datetime DEFAULT NULL COMMENT '订单生成时间',
`type` tinyint(1) DEFAULT NULL COMMENT '支付类型，1 微信 2支付宝',
`car_id` bigint(20) DEFAULT NULL COMMENT '支付保证金车辆的id',
`vip_lv` int(2) DEFAULT NULL COMMENT '当前用户支付时候的vip等级',
`update_time` datetime DEFAULT NULL,
`refund_status` tinyint(2) DEFAULT '1' COMMENT '1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败',
`apply_refund_time` datetime DEFAULT NULL COMMENT '申请退款时间',
`refund_success_time` datetime DEFAULT NULL COMMENT '退款成功时间',
`refund_fail_reason` varchar(255) DEFAULT NULL COMMENT '退款失败原因',
`refund_id` varchar(50) DEFAULT NULL COMMENT '退款单号',
`refund_result_code` varchar(255) DEFAULT NULL COMMENT '退款业务结果',
`refund_err_code` varchar(255) DEFAULT NULL COMMENT '退款错误码',
`refund_err_code_des` varchar(255) DEFAULT NULL COMMENT '退款错误码描述',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `idx_orderno` (`out_trade_no`) USING BTREE,
KEY `type` (`type`) USING BTREE,
KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='一口价车辆支付记录';