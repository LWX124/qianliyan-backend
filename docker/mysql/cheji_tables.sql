-- ============================================================
-- Auto-generated CREATE TABLE statements from Java entity files
-- Generated: 2026-03-25
-- NOTE: app_user table is skipped (already created)
-- ============================================================

-- -----------------------------------------------------------
-- 1. app_accident_record (AccidentRecordEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_accident_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `video` VARCHAR(500) DEFAULT NULL,
  `lng` DOUBLE DEFAULT NULL,
  `lat` DOUBLE DEFAULT NULL,
  `checkid` VARCHAR(255) DEFAULT NULL,
  `statuse` INT(11) DEFAULT NULL,
  `reason` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `realness` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `isaddvideo` INT(11) DEFAULT NULL,
  `introduce` VARCHAR(2000) DEFAULT NULL,
  `georedis` VARCHAR(255) DEFAULT NULL,
  `delgeo` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `check_time` DATETIME DEFAULT NULL,
  `real_address` VARCHAR(255) DEFAULT NULL,
  `thumbnail_flag` INT(11) DEFAULT NULL,
  `thumbnail_url` VARCHAR(500) DEFAULT NULL,
  `img_url` VARCHAR(500) DEFAULT NULL,
  `is_order` INT(11) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `loss_vehicle` VARCHAR(255) DEFAULT NULL,
  `risk_factor` VARCHAR(255) DEFAULT NULL,
  `plate` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `company` VARCHAR(255) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  `source` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 2. app_auction_address (AppAuctionAddressEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_address` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `area` VARCHAR(255) DEFAULT NULL,
  `is_default` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 3. app_auction_alipay_info (AppAuctionAlipayInfoEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_alipay_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `merchant_private_key` VARCHAR(2000) DEFAULT NULL,
  `app_id` VARCHAR(255) DEFAULT NULL,
  `alipay_public_key` VARCHAR(2000) DEFAULT NULL,
  `crate_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `protocol` VARCHAR(255) DEFAULT NULL,
  `gateway_host` VARCHAR(255) DEFAULT NULL,
  `sign_type` VARCHAR(255) DEFAULT NULL,
  `notify_url` VARCHAR(500) DEFAULT NULL,
  `return_url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 4. app_auction_authentication (AppAuctionAuthenticationEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_authentication` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `sign_state` VARCHAR(255) DEFAULT NULL,
  `auth_state` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 5. app_auction_bail_log (AppAuctionBailLogEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_bail_log` (
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  `notify_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 6. app_auction_bail_refund_log (AppAuctionBailRefundLogEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_bail_refund_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `out_refund_no` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  `notify_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `explain` VARCHAR(255) DEFAULT NULL,
  `refund_id` VARCHAR(255) DEFAULT NULL,
  `back_status` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 7. app_auction_bid_log (AppAuctionBidEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_bid_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `valid` INT(11) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `bid` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 8. app_auction_brand_subscription (AppAuctionBrandSubEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_brand_subscription` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `img_url` VARCHAR(500) DEFAULT NULL,
  `vip_lv` INT(11) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 9. app_auction_car_count (AppAuctionCarCountEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_car_count` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `car_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `bid_state` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 10. app_auction_collect (AppAuctionCollectEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_collect` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `is_enabled` INT(11) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 11. app_auction_counselor (AppAuctionCounselorEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_counselor` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `head` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 12. app_auction (AppAuctionEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `brand` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `displacement` VARCHAR(255) DEFAULT NULL,
  `transmission` VARCHAR(255) DEFAULT NULL,
  `fuel` VARCHAR(255) DEFAULT NULL,
  `register_date` DATETIME DEFAULT NULL,
  `issue_date` DATETIME DEFAULT NULL,
  `annual_check` DATETIME DEFAULT NULL,
  `compulsory_insurance_vilidity` DATETIME DEFAULT NULL,
  `registered_residence` VARCHAR(255) DEFAULT NULL,
  `parking_place` VARCHAR(255) DEFAULT NULL,
  `accident_type` VARCHAR(255) DEFAULT NULL,
  `sunroof` VARCHAR(255) DEFAULT NULL,
  `frame_no` VARCHAR(255) DEFAULT NULL,
  `use_nature` VARCHAR(255) DEFAULT NULL,
  `frame_no_damaged_condition` VARCHAR(255) DEFAULT NULL,
  `engine` VARCHAR(255) DEFAULT NULL,
  `mileage` VARCHAR(255) DEFAULT NULL,
  `key` VARCHAR(255) DEFAULT NULL,
  `purchase_tax` VARCHAR(255) DEFAULT NULL,
  `plate_number` VARCHAR(255) DEFAULT NULL,
  `registration_certificate` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `overhaul` VARCHAR(255) DEFAULT NULL,
  `announcements` VARCHAR(2000) DEFAULT NULL,
  `car_state` INT(11) DEFAULT NULL,
  `other_field` VARCHAR(255) DEFAULT NULL,
  `color` VARCHAR(255) DEFAULT NULL,
  `mortgage` VARCHAR(255) DEFAULT NULL,
  `second` VARCHAR(255) DEFAULT NULL,
  `change` VARCHAR(255) DEFAULT NULL,
  `transfer` VARCHAR(255) DEFAULT NULL,
  `owner` VARCHAR(255) DEFAULT NULL,
  `duty` VARCHAR(255) DEFAULT NULL,
  `duty_book` VARCHAR(255) DEFAULT NULL,
  `driving_license` VARCHAR(255) DEFAULT NULL,
  `licence` VARCHAR(255) DEFAULT NULL,
  `insurance` VARCHAR(255) DEFAULT NULL,
  `ins_remark` VARCHAR(255) DEFAULT NULL,
  `repair` VARCHAR(255) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `plate_no` VARCHAR(255) DEFAULT NULL,
  `auction_type` VARCHAR(255) DEFAULT NULL,
  `damage_reason` VARCHAR(255) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `fixed_price` VARCHAR(255) DEFAULT NULL,
  `insured_amount` DECIMAL(10,2) DEFAULT NULL,
  `up_state` INT(11) DEFAULT NULL,
  `product_date` DATETIME DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `license_address` VARCHAR(255) DEFAULT NULL,
  `car_identification_number` VARCHAR(255) DEFAULT NULL,
  `source_type` INT(11) DEFAULT NULL,
  `car_introduction` VARCHAR(2000) DEFAULT NULL,
  `car_bond_amt` DECIMAL(10,2) DEFAULT NULL,
  `car_service_amt` DECIMAL(10,2) DEFAULT NULL,
  `luxury_car_price` DECIMAL(10,2) DEFAULT NULL,
  `first_amount` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 13. app_auction_feedback (AppAuctionFeedBackEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_feedback` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `feedback` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 14. app_auction_find_car (AppAuctionFindCarEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_find_car` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  `series` VARCHAR(255) DEFAULT NULL,
  `color` VARCHAR(255) DEFAULT NULL,
  `car_age` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `validity` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 15. app_auction_home_page (AppAuctionHomePageEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_home_page` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `info` VARCHAR(255) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 16. app_auction_hot (AppAuctionHotEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_hot` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `car_id` BIGINT(20) DEFAULT NULL,
  `is_enabled` INT(11) DEFAULT NULL,
  `hits` BIGINT(20) DEFAULT NULL,
  `city` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 17. app_auction_img_adv (AppAuctionImgAdvEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_img_adv` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `info` VARCHAR(255) DEFAULT NULL,
  `vip_lv` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 18. app_auction_img_auth (AppAuctionImgAuthEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_img_auth` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 19. app_auction_img (AppAuctionImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_img` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 20. app_auction_message_identify (AppAuctionMessageIdentifyEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_message_identify` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_name` VARCHAR(255) DEFAULT NULL,
  `id_number` VARCHAR(255) DEFAULT NULL,
  `business_name` VARCHAR(255) DEFAULT NULL,
  `business_number` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 21. app_auction_my (AppAuctionMyEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_my` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `fund` VARCHAR(255) DEFAULT NULL,
  `order_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 22. app_auction_one_price_car_log (AppAuctionOnePriceCarLogEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_one_price_car_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `amount` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  `notify_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `vip_lv` INT(11) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 23. app_auction_order (AppAuctionOrderEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `car_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `order_no` VARCHAR(255) DEFAULT NULL,
  `order_amount` DECIMAL(10,2) DEFAULT NULL,
  `service_fee` DECIMAL(10,2) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `desc` VARCHAR(2000) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 24. app_auction_pay_log (AppAuctionPayLogEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_pay_log` (
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  `notify_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `vip_lv` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 25. app_auction_session (AppAuctionSessionEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_session` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `begin_time` DATETIME DEFAULT NULL,
  `duration` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  `title` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 26. app_auction_subscription (AppAuctionSubscriptionEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_subscription` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `rss` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 27. app_auction_transaction_log (AppAuctionTransactionLogEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_transaction_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `desc` VARCHAR(2000) DEFAULT NULL,
  `order_id` VARCHAR(255) DEFAULT NULL,
  `order_state` INT(11) DEFAULT NULL,
  `pay_type` INT(11) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 28. app_auction_up (AppAuctionUpEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_up` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `car_id` BIGINT(20) DEFAULT NULL,
  `counselor_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `begin_time` DATETIME DEFAULT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `service_fee` INT(11) DEFAULT NULL,
  `other_fee` DECIMAL(10,2) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  `explain` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 29. app_auction_vip_control (AppAuctionVipControlEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_vip_control` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `vip_lv` VARCHAR(255) DEFAULT NULL,
  `vip_id` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `car_count` INT(11) DEFAULT NULL,
  `freeze_count` INT(11) DEFAULT NULL,
  `offer` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 30. app_auction_vip_lv (AppAuctionVipLvEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_vip_lv` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `lv` VARCHAR(255) DEFAULT NULL,
  `lv_name` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `inner_amount` DECIMAL(10,2) DEFAULT NULL,
  `time_out` INT(11) DEFAULT NULL,
  `brand_num` INT(11) DEFAULT NULL,
  `open_explain` VARCHAR(255) DEFAULT NULL,
  `close_explain` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 31. app_auction_warn_car (AppAuctionWarnCarEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_warn_car` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `car_id` BIGINT(20) DEFAULT NULL,
  `is_enabled` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 32. app_auction_withdrawcash (AppAuctionWithdrawCashEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_auction_withdrawcash` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `allow` INT(11) DEFAULT NULL,
  `des` VARCHAR(2000) DEFAULT NULL,
  `vip_lv` INT(11) DEFAULT NULL,
  `user_bank_id` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `outcash_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 33. app_beauty_price_detail (AppBeautyPriceDetailEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_beauty_price_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `beauty_type` INT(11) DEFAULT NULL,
  `car_type` INT(11) DEFAULT NULL,
  `original_price` DECIMAL(10,2) DEFAULT NULL,
  `preferential_price` DECIMAL(10,2) DEFAULT NULL,
  `thrie_price` DECIMAL(10,2) DEFAULT NULL,
  `note` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 34. app_b_user_config (AppBUserConfigEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_b_user_config` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_b_id` VARCHAR(255) DEFAULT NULL,
  `business_type` VARCHAR(255) DEFAULT NULL,
  `pass_free_carwash` VARCHAR(255) DEFAULT NULL,
  `manual_antomatic` VARCHAR(255) DEFAULT NULL,
  `start_time` VARCHAR(255) DEFAULT NULL,
  `end_time` VARCHAR(255) DEFAULT NULL,
  `open_wash_time` VARCHAR(255) DEFAULT NULL,
  `night_wash` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 35. app_business_confirm (AppBusinessConfirmEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_business_confirm` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `business_confirm` VARCHAR(255) DEFAULT NULL,
  `accident_reponsibility` VARCHAR(255) DEFAULT NULL,
  `customers_have` DECIMAL(10,2) DEFAULT NULL,
  `not_united_customer` DECIMAL(10,2) DEFAULT NULL,
  `charter_shop` DECIMAL(10,2) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 36. app_car_brand (AppCarBrandEntity - no @TableId AUTO)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_car_brand` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `uniacid` INT(11) DEFAULT NULL,
  `parent_id` INT(11) DEFAULT NULL,
  `initials` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `pic_url` VARCHAR(500) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `sort` INT(11) DEFAULT NULL,
  `is_hot` INT(11) DEFAULT NULL,
  `create_time` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 37. app_change_account_record (AppChangeAccountRecord)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_change_account_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `old_account` VARCHAR(255) DEFAULT NULL,
  `new_account` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 38. app_claim_teacher (AppClaimTeacherEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_claim_teacher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) DEFAULT NULL,
  `level` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `on_lion` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `current_position` VARCHAR(255) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 39. app_coupon_type (AppCouponTypeEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_coupon_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` INT(11) DEFAULT NULL,
  `use_period` INT(11) DEFAULT NULL,
  `money` DECIMAL(10,2) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 40. app_file_url (AppFileUrlEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_file_url` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `file_id` BIGINT(20) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 41. app_jg_push (AppJgPushEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_jg_push` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(255) DEFAULT NULL,
  `ispush` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `user_b_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 42. app_message_car (AppMessageCarEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_message_car` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `license_plate` VARCHAR(255) DEFAULT NULL,
  `customer_name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `local_insurance` VARCHAR(255) DEFAULT NULL,
  `other_insurance` VARCHAR(255) DEFAULT NULL,
  `help_address` VARCHAR(255) DEFAULT NULL,
  `help_lng` DECIMAL(10,2) DEFAULT NULL,
  `help_lat` DECIMAL(10,2) DEFAULT NULL,
  `accident_responsibility` VARCHAR(255) DEFAULT NULL,
  `maintenance_mode` INT(11) DEFAULT NULL,
  `vehicle_loss` INT(11) DEFAULT NULL,
  `customer_intention` INT(11) DEFAULT NULL,
  `leave_message` VARCHAR(2000) DEFAULT NULL,
  `vehicle_results` INT(11) DEFAULT NULL,
  `user_b_id` VARCHAR(255) DEFAULT NULL,
  `voice` VARCHAR(500) DEFAULT NULL,
  `brand_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `mess_id` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `main_phone` VARCHAR(255) DEFAULT NULL,
  `main_name` VARCHAR(255) DEFAULT NULL,
  `main_insurance` VARCHAR(255) DEFAULT NULL,
  `financial_loss` VARCHAR(255) DEFAULT NULL,
  `buy_car` VARCHAR(255) DEFAULT NULL,
  `usually_maintain` VARCHAR(255) DEFAULT NULL,
  `channels_ins` VARCHAR(255) DEFAULT NULL,
  `casualties` VARCHAR(255) DEFAULT NULL,
  `acc_conditions` VARCHAR(255) DEFAULT NULL,
  `save_costs` VARCHAR(255) DEFAULT NULL,
  `fix_intention` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `indent_id` VARCHAR(255) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `suit` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 43. app_message_url (AppMessageUrlEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_message_url` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `type` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `message_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 44. app_order_roll_back (AppOrderRollBack)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_order_roll_back` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME DEFAULT NULL,
  `order_id` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `pay_flag` INT(11) DEFAULT NULL,
  `ops_flag` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 45. app_photo_mer (AppPhotoMerEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_photo_mer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `up_id` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 46. app_push_mess (AppPushMessEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_push_mess` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `message_car_id` INT(11) DEFAULT NULL,
  `voice` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 47. app_repeat_accident (AppRepeatAccidentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_repeat_accident` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `accident_id` VARCHAR(255) DEFAULT NULL,
  `accident_source` INT(11) DEFAULT NULL,
  `repeat_id` VARCHAR(255) DEFAULT NULL,
  `repeat_srouce` INT(11) DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 48. app_rescue_indent (AppRescueIndentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_rescue_indent` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `rescue_number` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `current_position` VARCHAR(255) DEFAULT NULL,
  `merchants_position` VARCHAR(255) DEFAULT NULL,
  `license_plate` VARCHAR(255) DEFAULT NULL,
  `rescue_name` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `emergency_number` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `distance` DECIMAL(10,2) DEFAULT NULL,
  `rescue_scene` INT(11) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `merchants_pay_number` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `inlng` DECIMAL(10,2) DEFAULT NULL,
  `inlat` DECIMAL(10,2) DEFAULT NULL,
  `destination` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 49. app_send_out_sheet (AppSendOutSheetEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_send_out_sheet` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `adress` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `tc_time` DATETIME DEFAULT NULL,
  `check_address` VARCHAR(255) DEFAULT NULL,
  `check_time` DATETIME DEFAULT NULL,
  `check_lng` DECIMAL(10,2) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `check_lat` DECIMAL(10,2) DEFAULT NULL,
  `back_text` VARCHAR(255) DEFAULT NULL,
  `back_voice` VARCHAR(500) DEFAULT NULL,
  `falcon_trajectory` VARCHAR(255) DEFAULT NULL,
  `tid` VARCHAR(255) DEFAULT NULL,
  `nowstate` INT(11) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `infromation_costs` DECIMAL(10,2) DEFAULT NULL,
  `access_information` VARCHAR(255) DEFAULT NULL,
  `clinch_deal` INT(11) DEFAULT NULL,
  `voice` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `track_state` INT(11) DEFAULT NULL,
  `message_car_id` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `plate_number` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `company` VARCHAR(255) DEFAULT NULL,
  `pay_amount` DECIMAL(10,2) DEFAULT NULL,
  `mess_pay_number` VARCHAR(255) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `sos_number` VARCHAR(255) DEFAULT NULL,
  `check_state` INT(11) DEFAULT NULL,
  `accident_type` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 50. app_send_url (AppSendUrlEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_send_url` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `type` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `send_id` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 51. app_spray_paint_details (AppSprayPaintDetailsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_spray_paint_details` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `spray_paint_id` INT(11) DEFAULT NULL,
  `place` VARCHAR(255) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 52. app_spray_paint_img (AppSprayPaintImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_spray_paint_img` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `spray_paint_id` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 53. app_spray_paint_indent (AppSprayPaintIndentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_spray_paint_indent` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `spray_paint_number` VARCHAR(255) DEFAULT NULL,
  `license_plate` VARCHAR(255) DEFAULT NULL,
  `brand_type` VARCHAR(255) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `pick_address` VARCHAR(255) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `username` VARCHAR(255) DEFAULT NULL,
  `insurance` INT(11) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `technician_id` INT(11) DEFAULT NULL,
  `is_evaluation` INT(11) DEFAULT NULL,
  `pay_number` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `is_offer` INT(11) DEFAULT NULL,
  `pick_car_time` DATETIME DEFAULT NULL,
  `send_car_time` DATETIME DEFAULT NULL,
  `cancel_reason` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 54. app_substitute_driving_img (AppSubstituteDrivingImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_substitute_driving_img` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `subsitute_driving_id` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 55. app_substitute_driving_indent (AppSubstituteDrivingIndentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_substitute_driving_indent` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `substitute_driving_number` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `start_point` VARCHAR(255) DEFAULT NULL,
  `start_address` VARCHAR(255) DEFAULT NULL,
  `start_lng` DECIMAL(10,2) DEFAULT NULL,
  `start_lat` DECIMAL(10,2) DEFAULT NULL,
  `start_name` VARCHAR(255) DEFAULT NULL,
  `start_phone` VARCHAR(255) DEFAULT NULL,
  `start_remark` VARCHAR(255) DEFAULT NULL,
  `end_point` VARCHAR(255) DEFAULT NULL,
  `end_address` VARCHAR(255) DEFAULT NULL,
  `end_lng` DECIMAL(10,2) DEFAULT NULL,
  `end_lat` DECIMAL(10,2) DEFAULT NULL,
  `end_name` VARCHAR(255) DEFAULT NULL,
  `end_phone` VARCHAR(255) DEFAULT NULL,
  `end_remark` VARCHAR(255) DEFAULT NULL,
  `distance` DECIMAL(10,2) DEFAULT NULL,
  `estimate_price` DECIMAL(10,2) DEFAULT NULL,
  `actual_price` DECIMAL(10,2) DEFAULT NULL,
  `mileage_price` DECIMAL(10,2) DEFAULT NULL,
  `tolls_price` DECIMAL(10,2) DEFAULT NULL,
  `time_price` DECIMAL(10,2) DEFAULT NULL,
  `cancel_resource` VARCHAR(255) DEFAULT NULL,
  `cancel_reason` VARCHAR(255) DEFAULT NULL,
  `indent_state` INT(11) DEFAULT NULL,
  `package_type` INT(11) DEFAULT NULL,
  `begin_time` DATETIME DEFAULT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `wait_time` INT(11) DEFAULT NULL,
  `sid` VARCHAR(255) DEFAULT NULL,
  `tid` VARCHAR(255) DEFAULT NULL,
  `trid` VARCHAR(255) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `pay_number` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 56. app_sub_usual_address (AppSubUsualAddressEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_sub_usual_address` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) DEFAULT NULL,
  `point` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `username` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 57. app_up_merchants (AppUpMerchantsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_up_merchants` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  `score` INT(11) DEFAULT NULL,
  `huanxin_username` VARCHAR(255) DEFAULT NULL,
  `huanxin_password` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `unread_message` INT(11) DEFAULT NULL,
  `save_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 58. app_user_account_record (AppUserAccountRecordEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_account_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `momey` DECIMAL(10,2) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `add_flag` INT(11) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `business_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 59. app_user_bank (AppUserBankEntity / UserBankEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_bank` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `bank_id` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `code` VARCHAR(255) DEFAULT NULL,
  `bank_user_name` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 60. app_user_b_message (AppUserBMessageEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_b_message` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `serial_number` VARCHAR(255) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `head_img` VARCHAR(500) DEFAULT NULL,
  `work_place` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `trailer_plate` VARCHAR(255) DEFAULT NULL,
  `score` DECIMAL(10,2) DEFAULT NULL,
  `order_number` INT(11) DEFAULT NULL,
  `introduction` VARCHAR(2000) DEFAULT NULL,
  `level` INT(11) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `wrok_type` INT(11) DEFAULT NULL,
  `driver_year` VARCHAR(255) DEFAULT NULL,
  `business_type` INT(11) DEFAULT NULL,
  `technology_year` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 61. app_user_b_message_img (AppUserBMessageImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_b_message_img` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 62. app_user_car_info (AppUserCarInfoEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_car_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `car_code` VARCHAR(255) DEFAULT NULL,
  `car_engine_code` VARCHAR(255) DEFAULT NULL,
  `car_number` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `sms_flag` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 63. app_user_coupon (AppUserCouponEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_coupon` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `coupon_id` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `validity_time` DATETIME DEFAULT NULL,
  `is_use` INT(11) DEFAULT NULL,
  `order_number` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 64. app_user_plus (AppUserPlusEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_plus` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `invalid_time_start` DATETIME DEFAULT NULL,
  `invalid_time_end` DATETIME DEFAULT NULL,
  `invite_code` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 65. app_version (AppVersionEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_version` (
  `id` VARCHAR(255) NOT NULL,
  `version_num_min` INT(11) DEFAULT NULL,
  `version_num_max` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `version_name` VARCHAR(255) DEFAULT NULL,
  `info` VARCHAR(255) DEFAULT NULL,
  `href` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 66. app_wx_bank (AppWxBankEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_wx_bank` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `bank_name` VARCHAR(255) DEFAULT NULL,
  `bank_code` VARCHAR(255) DEFAULT NULL,
  `icon_url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 67. app_wx_cash_out_record (AppWxCashOutRecordEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_wx_cash_out_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `fee` DECIMAL(10,2) DEFAULT NULL,
  `send_amount` DECIMAL(10,2) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `bank_number` VARCHAR(255) DEFAULT NULL,
  `bank_code` VARCHAR(255) DEFAULT NULL,
  `user_bank_name` VARCHAR(255) DEFAULT NULL,
  `partner_trade_no` VARCHAR(255) DEFAULT NULL,
  `result` VARCHAR(255) DEFAULT NULL,
  `result_info` VARCHAR(255) DEFAULT NULL,
  `sources` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 68. app_wxpay_order (AppWxpayOrderEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_wxpay_order` (
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  `notify_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `business_status` INT(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 69. app_wx_unifiedorder_record (AppWxUnifiedorderRecord)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_wx_unifiedorder_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `appid` VARCHAR(255) DEFAULT NULL,
  `mch_id` VARCHAR(255) DEFAULT NULL,
  `nonce_str` VARCHAR(255) DEFAULT NULL,
  `sign` VARCHAR(255) DEFAULT NULL,
  `body` VARCHAR(255) DEFAULT NULL,
  `out_trade_no` VARCHAR(255) DEFAULT NULL,
  `total_fee` INT(11) DEFAULT NULL,
  `spbill_create_ip` VARCHAR(255) DEFAULT NULL,
  `notify_url` VARCHAR(500) DEFAULT NULL,
  `trade_type` VARCHAR(255) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `return_code` VARCHAR(255) DEFAULT NULL,
  `return_msg` VARCHAR(255) DEFAULT NULL,
  `wx_nonce_str` VARCHAR(255) DEFAULT NULL,
  `wx_sign` VARCHAR(255) DEFAULT NULL,
  `result_code` VARCHAR(255) DEFAULT NULL,
  `err_code` VARCHAR(255) DEFAULT NULL,
  `err_code_des` VARCHAR(255) DEFAULT NULL,
  `wx_trade_type` VARCHAR(255) DEFAULT NULL,
  `prepay_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 70. app_year_check_img (AppYearCheckImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_year_check_img` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `app_year_check_id` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 71. app_year_check_indent (AppYearCheckIndentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_year_check_indent` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `year_check_number` VARCHAR(255) DEFAULT NULL,
  `license_plate` VARCHAR(255) DEFAULT NULL,
  `username` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `pick_address` VARCHAR(255) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `year_check_type` INT(11) DEFAULT NULL,
  `pay_number` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 72. biz_accident (BizAccidentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `biz_accident` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `openid` VARCHAR(255) DEFAULT NULL,
  `video` VARCHAR(500) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `checkId` VARCHAR(255) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `reason` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `realness` INT(11) DEFAULT NULL,
  `isaddvideo` INT(11) DEFAULT NULL,
  `georedis` VARCHAR(255) DEFAULT NULL,
  `delgeo` VARCHAR(255) DEFAULT NULL,
  `video_id` BIGINT(20) DEFAULT NULL,
  `createTime` DATETIME DEFAULT NULL,
  `checkTime` DATETIME DEFAULT NULL,
  `version` INT(11) DEFAULT NULL,
  `carCount` INT(11) DEFAULT NULL,
  `realaddress` VARCHAR(255) DEFAULT NULL,
  `realImg` VARCHAR(500) DEFAULT NULL,
  `accImg` VARCHAR(500) DEFAULT NULL,
  `lat2` DECIMAL(10,2) DEFAULT NULL,
  `lng2` DECIMAL(10,2) DEFAULT NULL,
  `topFlag` INT(11) DEFAULT NULL,
  `realArrTime` VARCHAR(255) DEFAULT NULL,
  `thumbnail_flag` INT(11) DEFAULT NULL,
  `thumbnail_url` VARCHAR(500) DEFAULT NULL,
  `is_order` INT(11) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `loss_vehicle` VARCHAR(255) DEFAULT NULL,
  `risk_factor` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 73. app_b_user (BUserEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_b_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `avatar` VARCHAR(500) DEFAULT NULL,
  `parent_id` INT(11) DEFAULT NULL,
  `username` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `salt` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `balance` DECIMAL(10,2) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `wx_union_id` VARCHAR(255) DEFAULT NULL,
  `wx_open_id` VARCHAR(255) DEFAULT NULL,
  `huanxin_user_name` VARCHAR(255) DEFAULT NULL,
  `huanxin_password` VARCHAR(255) DEFAULT NULL,
  `merchants_name` VARCHAR(255) DEFAULT NULL,
  `service_sorce` INT(11) DEFAULT NULL,
  `effciency_score` INT(11) DEFAULT NULL,
  `score` DECIMAL(10,2) DEFAULT NULL,
  `lat` DECIMAL(10,2) DEFAULT NULL,
  `lng` DECIMAL(10,2) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `business_start` VARCHAR(255) DEFAULT NULL,
  `business_end` VARCHAR(255) DEFAULT NULL,
  `announcement` VARCHAR(2000) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `reason` VARCHAR(255) DEFAULT NULL,
  `merchants_phone` VARCHAR(255) DEFAULT NULL,
  `lable_id` VARCHAR(255) DEFAULT NULL,
  `merchants_level` VARCHAR(255) DEFAULT NULL,
  `details_lable` VARCHAR(255) DEFAULT NULL,
  `up_id` VARCHAR(255) DEFAULT NULL,
  `province` VARCHAR(255) DEFAULT NULL,
  `city` VARCHAR(255) DEFAULT NULL,
  `county` VARCHAR(255) DEFAULT NULL,
  `unread_message` INT(11) DEFAULT NULL,
  `real_location` VARCHAR(255) DEFAULT NULL,
  `save_time` DATETIME DEFAULT NULL,
  `brand_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 74. app_city (CityEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_city` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `city_name` VARCHAR(255) DEFAULT NULL,
  `adcode` INT(11) DEFAULT NULL,
  `citycode` INT(11) DEFAULT NULL,
  `recommend_city` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 75. app_clean_indet (CleanIndetEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_clean_indet` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `clean_indent_number` VARCHAR(255) DEFAULT NULL,
  `user_b_id` VARCHAR(255) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `car_type` INT(11) DEFAULT NULL,
  `clean_type` INT(11) DEFAULT NULL,
  `original_price` DECIMAL(10,2) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `pay_state` VARCHAR(255) DEFAULT NULL,
  `resource` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `merchants_pay_number` VARCHAR(255) DEFAULT NULL,
  `indent_state` VARCHAR(255) DEFAULT NULL,
  `bussiness_id` VARCHAR(255) DEFAULT NULL,
  `contract_flag` INT(11) DEFAULT NULL,
  `is_evaluation` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 76. app_clean_price_detail (CleanPriceDetailEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_clean_price_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `clean_type` INT(11) DEFAULT NULL,
  `car_type` INT(11) DEFAULT NULL,
  `original_price` DECIMAL(10,2) DEFAULT NULL,
  `preferential_price` DECIMAL(10,2) DEFAULT NULL,
  `thrie_price` DECIMAL(10,2) DEFAULT NULL,
  `note` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `contract_project` INT(11) DEFAULT NULL,
  `residue_degree` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 77. js_sys_file_upload (FileUploadEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `js_sys_file_upload` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `file_id` VARCHAR(255) DEFAULT NULL,
  `file_name` VARCHAR(255) DEFAULT NULL,
  `file_type` VARCHAR(255) DEFAULT NULL,
  `biz_key` VARCHAR(255) DEFAULT NULL,
  `status` CHAR(1) DEFAULT NULL,
  `create_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 78. app_img (ImgEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_img` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `key_id` VARCHAR(255) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 79. app_indent (IndentEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_indent` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `username` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `plan` VARCHAR(255) DEFAULT NULL,
  `commission_rate` DECIMAL(10,2) DEFAULT NULL,
  `license_plate` VARCHAR(255) DEFAULT NULL,
  `brand_id` INT(11) DEFAULT NULL,
  `remake` VARCHAR(255) DEFAULT NULL,
  `send_people` VARCHAR(255) DEFAULT NULL,
  `order_number` VARCHAR(255) DEFAULT NULL,
  `means_payments` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `day` INT(11) DEFAULT NULL,
  `settle_accounts` VARCHAR(255) DEFAULT NULL,
  `fixloss_user` DECIMAL(10,2) DEFAULT NULL,
  `fixloss` VARCHAR(255) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `rescue_them_fee` DECIMAL(10,2) DEFAULT NULL,
  `responsibility` VARCHAR(255) DEFAULT NULL,
  `insurance_company` VARCHAR(255) DEFAULT NULL,
  `send_unit` VARCHAR(255) DEFAULT NULL,
  `message_source` INT(11) DEFAULT NULL,
  `deal_time` INT(11) DEFAULT NULL,
  `up_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 80. biz_insurance_company (InsuranceCompanyEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `biz_insurance_company` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `pid` INT(11) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 81. app_insurance_merchants (InsuranceMerchantsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_insurance_merchants` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `insurance_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 82. app_lable_details_review_tree (LableDetailsReviewTreeEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_lable_details_review_tree` (
  `lable_code` VARCHAR(255) NOT NULL,
  `lable_name` VARCHAR(255) DEFAULT NULL,
  `parent_code` VARCHAR(255) DEFAULT NULL,
  `parent_codes` VARCHAR(255) DEFAULT NULL,
  `tree_sort` DECIMAL(10,2) DEFAULT NULL,
  `tree_sorts` VARCHAR(255) DEFAULT NULL,
  `tree_leaf` VARCHAR(255) DEFAULT NULL,
  `tree_level` DECIMAL(10,2) DEFAULT NULL,
  `tree_names` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(255) DEFAULT NULL,
  `create_date` DATETIME DEFAULT NULL,
  `update_date` DATETIME DEFAULT NULL,
  `lable_id` INT(11) DEFAULT NULL,
  `lable_details_id` INT(11) DEFAULT NULL,
  `rebates` DECIMAL(10,2) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `show` INT(11) DEFAULT NULL,
  `reson` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `remake` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`lable_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 83. app_lable (LableEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_lable` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `enable` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 84. app_league (LeagueEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_league` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `shop_name` VARCHAR(255) DEFAULT NULL,
  `brand` VARCHAR(255) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `city_name` VARCHAR(255) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 85. app_merchants_brand (MerchantsBrandEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_merchants_brand` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `brand_id` VARCHAR(255) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 86. app_merchants_comments_tree (MerchantsCommentsTreeEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_merchants_comments_tree` (
  `comments_code` VARCHAR(255) NOT NULL,
  `comments_name` VARCHAR(255) DEFAULT NULL,
  `parent_code` VARCHAR(255) DEFAULT NULL,
  `parent_codes` VARCHAR(255) DEFAULT NULL,
  `tree_sort` DECIMAL(10,2) DEFAULT NULL,
  `tree_sorts` VARCHAR(255) DEFAULT NULL,
  `tree_leaf` VARCHAR(255) DEFAULT NULL,
  `tree_level` DECIMAL(10,2) DEFAULT NULL,
  `tree_names` VARCHAR(255) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `content` VARCHAR(2000) DEFAULT NULL,
  `state` VARCHAR(255) DEFAULT NULL,
  `service_score` INT(11) DEFAULT NULL,
  `efficiensy_score` INT(11) DEFAULT NULL,
  `create_date` DATETIME DEFAULT NULL,
  `update_date` DATETIME DEFAULT NULL,
  `lable` VARCHAR(255) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  `have_img` INT(11) DEFAULT NULL,
  PRIMARY KEY (`comments_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 87. app_merchants_info_banner (MerchantsInfoBannerEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_merchants_info_banner` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(500) DEFAULT NULL,
  `index` INT(11) DEFAULT NULL,
  `user_b_id` INT(11) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 88. app_merchants_lable (MerchantsLableEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_merchants_lable` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `lable_id` INT(11) DEFAULT NULL,
  `rebates` DECIMAL(10,2) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 89. app_merchants_servicer (MerchantsServicerEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_merchants_servicer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_b_id` INT(11) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `Img_url` VARCHAR(500) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 90. app_pay_amount_record (PayAmountRecordEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_pay_amount_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `accid` INT(11) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `pay_amount` DECIMAL(10,2) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 91. app_push_bill (PushBillEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_push_bill` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `accid` INT(11) DEFAULT NULL,
  `user_id` BIGINT(20) DEFAULT NULL,
  `deduction` DECIMAL(10,2) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `track_state` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `check_time` DATETIME DEFAULT NULL,
  `check_address` VARCHAR(255) DEFAULT NULL,
  `check_lng` DECIMAL(10,2) DEFAULT NULL,
  `check_lat` DECIMAL(10,2) DEFAULT NULL,
  `mess_pay_number` VARCHAR(255) DEFAULT NULL,
  `pay_state` INT(11) DEFAULT NULL,
  `pb_number` VARCHAR(255) DEFAULT NULL,
  `check_state` INT(11) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `accident_type` INT(11) DEFAULT NULL,
  `manager_remark` VARCHAR(255) DEFAULT NULL,
  `voice` VARCHAR(500) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `reward` DECIMAL(10,2) DEFAULT NULL,
  `message_car_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 92. app_push_record (PushRecordEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_push_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `accid` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 93. app_settle_plus (SettlePlusEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_settle_plus` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `settlement_amount` DECIMAL(10,2) DEFAULT NULL,
  `proprotion` DECIMAL(10,2) DEFAULT NULL,
  `settle_plus_amount` DECIMAL(10,2) DEFAULT NULL,
  `plus_id` INT(11) DEFAULT NULL,
  `indent_id` INT(11) DEFAULT NULL,
  `inform` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 94. app_user_plus_royalty (UserPlusRoyaltyEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_user_plus_royalty` (
  `id` VARCHAR(255) NOT NULL,
  `royalty_user_id` INT(11) DEFAULT NULL,
  `plus_user_id` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 95. app_video_comments (VideoCommentsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_video_comments` (
  `tree_code` VARCHAR(255) NOT NULL,
  `tree_name` VARCHAR(255) DEFAULT NULL,
  `parent_code` VARCHAR(255) DEFAULT NULL,
  `parent_codes` VARCHAR(255) DEFAULT NULL,
  `tree_sort` DECIMAL(10,2) DEFAULT NULL,
  `tree_sorts` VARCHAR(255) DEFAULT NULL,
  `tree_leaf` VARCHAR(255) DEFAULT NULL,
  `tree_level` DECIMAL(10,2) DEFAULT NULL,
  `tree_names` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(255) DEFAULT NULL,
  `create_date` DATETIME DEFAULT NULL,
  `update_date` DATETIME DEFAULT NULL,
  `parent_id` INT(11) DEFAULT NULL,
  `count` VARCHAR(255) DEFAULT NULL,
  `video_id` BIGINT(20) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `thumbs_up` INT(11) DEFAULT NULL,
  `type` INT(11) DEFAULT NULL,
  PRIMARY KEY (`tree_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 96. app_video_commonts_thumbs (VideoCommontsThumbsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_video_commonts_thumbs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `video_commonts_id` VARCHAR(255) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 97. app_video (VideoEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_video` (
  `id` VARCHAR(255) NOT NULL,
  `url` VARCHAR(500) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `source` INT(11) DEFAULT NULL,
  `count` INT(11) DEFAULT NULL,
  `share` INT(11) DEFAULT NULL,
  `state` INT(11) DEFAULT NULL,
  `reson` VARCHAR(255) DEFAULT NULL,
  `app_show_falg` INT(11) DEFAULT NULL,
  `app_view_counts` BIGINT(20) DEFAULT NULL,
  `accident_id` INT(11) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `introduce` VARCHAR(2000) DEFAULT NULL,
  `thumbnail_flag` INT(11) DEFAULT NULL,
  `thumbnail_url` VARCHAR(500) DEFAULT NULL,
  `creat_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 98. app_video_thumbs (VideoThumbsEntity)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_video_thumbs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `video_id` VARCHAR(255) DEFAULT NULL,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `status` INT(11) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- NOTE: RescueMerchantsEntity is skipped (no @TableName annotation)
-- NOTE: AppUserEntity (app_user) is skipped (already created)
-- NOTE: UserEntity (app_user) is skipped (same table as AppUserEntity)
-- NOTE: UserBankEntity maps to same table app_user_bank as AppUserBankEntity (already included above)
-- -----------------------------------------------------------
