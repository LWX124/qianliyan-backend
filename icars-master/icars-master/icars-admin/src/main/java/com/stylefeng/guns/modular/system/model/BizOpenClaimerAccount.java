package com.stylefeng.guns.modular.system.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 员工账户主表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
@TableName("biz_open_claimer_account")
public class BizOpenClaimerAccount extends Model<BizOpenClaimerAccount> {

    private static final long serialVersionUID = 1L;

    /**
     * sys_user主键
     */
    private Integer userid;
    /**
     * 账户金额
     */
    private BigDecimal balance;


    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    protected Serializable pkVal() {
        return this.userid;
    }

    @Override
    public String toString() {
        return "BizOpenClaimerAccount{" +
        "userid=" + userid +
        ", balance=" + balance +
        "}";
    }
}
