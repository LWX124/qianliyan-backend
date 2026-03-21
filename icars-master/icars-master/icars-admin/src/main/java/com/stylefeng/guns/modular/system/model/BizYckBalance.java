package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
@TableName("biz_yck_balance")
public class BizYckBalance extends Model<BizYckBalance> {

    private static final long serialVersionUID = 1L;

    private String openid;
    private BigDecimal balance;

    private  String account;
    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    protected Serializable pkVal() {
        return this.openid;
    }

    @Override
    public String toString() {
        return "BizYckBalance{" +
                "openid='" + openid + '\'' +
                ", balance=" + balance +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                ", account=" + account +
                '}';
    }



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
