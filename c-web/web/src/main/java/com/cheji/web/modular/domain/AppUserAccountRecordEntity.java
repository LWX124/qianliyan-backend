package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户金额记录表
 */
@TableName("app_user_account_record")
public class AppUserAccountRecordEntity extends Model<AppUserAccountRecordEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private BigDecimal momey;
    private Integer userId;
    private Integer type;       //操作类型 1：提现
    private Date createTime;       //发生时间
    private Integer addFlag;    //1加钱  2 减钱
    private Integer source;    //1:c端  2：b端
    private String businessId;    //业务id

    @Override
    public String toString() {
        return "AppUserAccountRecordEntity{" +
                "id=" + id +
                ", momey=" + momey +
                ", userId=" + userId +
                ", type=" + type +
                ", createTime=" + createTime +
                ", addFlag=" + addFlag +
                ", source=" + source +
                ", businessId=" + businessId +
                '}';
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMomey() {
        return momey;
    }

    public void setMomey(BigDecimal momey) {
        this.momey = momey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(Integer addFlag) {
        this.addFlag = addFlag;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
