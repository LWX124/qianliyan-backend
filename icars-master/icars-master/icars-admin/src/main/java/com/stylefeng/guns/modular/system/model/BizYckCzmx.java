package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 外部理赔用户预存款操作明细表
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-15
 */
@TableName("biz_yck_czmx")
public class BizYckCzmx extends Model<BizYckCzmx> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String openid;

    /**
     * 充值订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 明细类型（0、充值  1、扣款）
     */
    @TableField("detail_type")
    private Integer detailType;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 充值用户账户
     */
    private String account;


    /**
     * 充值用户手机
     */
    @TableField(exist = false)
    private String phone;
    /**
     * 充值用户姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 事故id
     */
    private Integer accid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    @Override
    public String toString() {
        return "BizYckCzmx{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", amount=" + amount +
                ", operator='" + operator + '\'' +
                ", detailType=" + detailType +
                ", createTime=" + createTime +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", accid=" + accid +
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
