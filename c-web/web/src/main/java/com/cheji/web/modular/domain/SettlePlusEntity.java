package com.cheji.web.modular.domain;

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
 * 结算plus会员明细
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@TableName("app_settle_plus")
public class SettlePlusEntity extends Model<SettlePlusEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 结算到用户金额
     */
    @TableField("settlement_amount")
    private BigDecimal settlementAmount;
    /**
     * 结算比例
     */
    private BigDecimal proprotion;
    /**
     * 结算到plus会员金额
     */
    @TableField("settle_plus_amount")
    private BigDecimal settlePlusAmount;
    /**
     * plus会员id
     */
    @TableField("plus_id")
    private Integer plusId;
    /**
     * 订单id
     */
    @TableField("indent_id")
    private Integer indentId;
    /**
     * 通知
     */
    private String inform;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public BigDecimal getProprotion() {
        return proprotion;
    }

    public void setProprotion(BigDecimal proprotion) {
        this.proprotion = proprotion;
    }

    public BigDecimal getSettlePlusAmount() {
        return settlePlusAmount;
    }

    public void setSettlePlusAmount(BigDecimal settlePlusAmount) {
        this.settlePlusAmount = settlePlusAmount;
    }

    public Integer getPlusId() {
        return plusId;
    }

    public void setPlusId(Integer plusId) {
        this.plusId = plusId;
    }

    public Integer getIndentId() {
        return indentId;
    }

    public void setIndentId(Integer indentId) {
        this.indentId = indentId;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SettlePlusEntity{" +
        ", id=" + id +
        ", settlementAmount=" + settlementAmount +
        ", proprotion=" + proprotion +
        ", settlePlusAmount=" + settlePlusAmount +
        ", plusId=" + plusId +
        ", indentId=" + indentId +
        ", inform=" + inform +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
