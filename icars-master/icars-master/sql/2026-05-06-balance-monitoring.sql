-- 1. 新建余额快照表
CREATE TABLE IF NOT EXISTS biz_balance_snapshot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    balance BIGINT NOT NULL COMMENT '日终余额（分）',
    bill_date DATE NOT NULL COMMENT '账单日期',
    alert_threshold BIGINT NOT NULL DEFAULT 50000 COMMENT '报警阈值（分），默认500元',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
    UNIQUE KEY uk_bill_date (bill_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户余额快照';

-- 2. biz_wxpay_bill 新增转账跟踪字段
ALTER TABLE biz_wxpay_bill
    ADD COLUMN transfer_status TINYINT NOT NULL DEFAULT 0 COMMENT '转账状态: 0=未发起, 1=已发起待确认, 2=用户已领取, 3=转账失败, 4=余额不足失败',
    ADD COLUMN fail_reason VARCHAR(200) DEFAULT NULL COMMENT '失败原因',
    ADD COLUMN transfer_time DATETIME DEFAULT NULL COMMENT '最近一次发起转账时间';

-- 3. 历史数据迁移：将现有 status 映射到 transfer_status
UPDATE biz_wxpay_bill SET transfer_status = 2 WHERE status = 0;  -- 支付成功 → 已领取
UPDATE biz_wxpay_bill SET transfer_status = 3 WHERE status = 1;  -- 支付失败 → 转账失败
UPDATE biz_wxpay_bill SET transfer_status = 1 WHERE status = 2;  -- 待确认 → 已发起待确认
