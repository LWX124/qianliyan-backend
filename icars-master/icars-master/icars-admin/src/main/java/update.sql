-- 用户表新增 是否黑名单字段
ALTER TABLE `sys_user`
ADD COLUMN `black_flag`  varchar(1) NULL AFTER `stars`;

-- 所有用户初始化为不是黑名单
update sys_user t  set t.black_flag='N';

-- 小程序保养表
CREATE TABLE `biz_xcx_maintenance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL COMMENT '1.保养  2.油漆 3.钣金',
  `content` mediumtext COMMENT '套餐内容',
  `car_type` mediumtext COMMENT '使用车型',
  `create_time` varchar(255) DEFAULT NULL,
  `update_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小程序 维修表';



