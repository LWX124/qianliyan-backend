-- 千里眼小程序后端改动 SQL
-- 为事故记录表添加 source 字段，用于区分不同小程序来源

ALTER TABLE app_accident_record ADD COLUMN source VARCHAR(50) DEFAULT NULL COMMENT '来源标识，区分不同小程序';

-- 创建索引方便按来源查询
ALTER TABLE app_accident_record ADD INDEX idx_source (source);
