package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 保养订单推送记录表
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-23
 */
@TableName("biz_maintain_order_pushrecord")
public class BizMaintainOrderPushrecord extends Model<BizMaintainOrderPushrecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("maintain_order_id")
    private Integer maintainOrderId;
    private String account;
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaintainOrderId() {
        return maintainOrderId;
    }

    public void setMaintainOrderId(Integer maintainOrderId) {
        this.maintainOrderId = maintainOrderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizMaintainOrderPushrecord{" +
        "id=" + id +
        ", maintainOrderId=" + maintainOrderId +
        ", account=" + account +
        ", createTime=" + createTime +
        "}";
    }
}
