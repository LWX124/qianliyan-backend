-- 为 biz_accident 表添加 source 字段，标识事故上报来源于哪个小程序
-- 已于 2026-04-06 在 icars_test 数据库执行
ALTER TABLE biz_accident ADD COLUMN source VARCHAR(32) DEFAULT NULL COMMENT '来源标识（SSP等，标识来自哪个小程序）';
