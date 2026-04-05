-- 多小程序来源识别：增加 source 字段
-- 执行时间：2026-04-05

-- app_user 表增加 source 字段
ALTER TABLE app_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_user_source ON app_user(source);

-- app_b_user 表增加 source 字段
ALTER TABLE app_b_user ADD COLUMN source VARCHAR(20) DEFAULT NULL COMMENT '小程序来源标识';
CREATE INDEX idx_app_b_user_source ON app_b_user(source);

-- 存量数据迁移：现有用户全部来自千里眼小程序
UPDATE app_user SET source = 'SSP' WHERE source IS NULL;
UPDATE app_b_user SET source = 'SSP' WHERE source IS NULL;
