package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 保险和商户关联表
 * </p>
 *
 * @author Ashes
 * @since 2019-10-09
 */
@TableName("app_insurance_merchants")
public class InsuranceMerchantsEntity extends Model<InsuranceMerchantsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 保险公司id
     */
    @TableField("insurance_id")
    private Integer insuranceId;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    private Integer state;

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

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
    public String toString() {
        return "InsuranceMerchantsEntity{" +
                "id=" + id +
                ", insuranceId=" + insuranceId +
                ", userBId=" + userBId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
