package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 员工账户明细表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-06
 */
@TableName("biz_open_claimer_bill")
public class BizOpenClaimerBill extends Model<BizOpenClaimerBill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * sys_user主键
     */
    private Integer userid;
    /**
     * 账户金额
     */
    private BigDecimal balance;
    /**
     * 支付订单号
     */
    private String orderno;
    /**
     * 支付结果 0：支付失败   1支付成功
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizOpenClaimerBill{" +
        "id=" + id +
        ", userid=" + userid +
        ", balance=" + balance +
        ", orderno=" + orderno +
        ", status=" + status +
        ", createtime=" + createtime +
        "}";
    }
}
