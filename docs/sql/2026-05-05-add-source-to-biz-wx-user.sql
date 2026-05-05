-- 多源小程序账号隔离：biz_wx_user 表增加 source 字段
-- 存量数据默认标记为 SSP（拍事故小程序）

ALTER TABLE biz_wx_user ADD COLUMN source VARCHAR(20) DEFAULT 'SSP' COMMENT '小程序来源标识（SSP=拍事故, TTP=天天拍）';

UPDATE biz_wx_user SET source = 'SSP' WHERE source IS NULL;
