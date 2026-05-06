package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("biz_balance_snapshot")
public class BizBalanceSnapshot extends Model<BizBalanceSnapshot> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long balance;

    @TableField("bill_date")
    private Date billDate;

    @TableField("alert_threshold")
    private Long alertThreshold;

    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    public Long getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Long alertThreshold) { this.alertThreshold = alertThreshold; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
