package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商户关联配置表
 * </p>
 *
 * @author Ashes
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_b_user_config")
public class AppBUserConfigEntity extends Model<AppBUserConfigEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private String userBId;

    /**
     * 营业状态
     */
    @TableField("business_type")
    private String businessType;    //1营业中，0 未营业

    /**
     * 是否开通免费洗车
     */
    @TableField("pass_free_carwash")
    private String passFreeCarwash;         //1，开通，0，不开通

    /**
     * 人工/自动(1.手动，2.自动，3.人工/自动)
     */
    @TableField("manual_antomatic")
    private String manualAntomatic;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private String endTime;

    /**
     * 是否开通营业时间
     */
    @TableField("open_wash_time")
    private String openWashTime;

    @TableField("night_wash")
    private String nightWash;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public String getNightWash() {
        return nightWash;
    }

    public void setNightWash(String nightWash) {
        this.nightWash = nightWash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPassFreeCarwash() {
        return passFreeCarwash;
    }

    public void setPassFreeCarwash(String passFreeCarwash) {
        this.passFreeCarwash = passFreeCarwash;
    }

    public String getManualAntomatic() {
        return manualAntomatic;
    }

    public void setManualAntomatic(String manualAntomatic) {
        this.manualAntomatic = manualAntomatic;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOpenWashTime() {
        return openWashTime;
    }

    public void setOpenWashTime(String openWashTime) {
        this.openWashTime = openWashTime;
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
        return "AppBUserConfigEntity{" +
                "id=" + id +
                ", userBId='" + userBId + '\'' +
                ", businessType='" + businessType + '\'' +
                ", passFreeCarwash='" + passFreeCarwash + '\'' +
                ", manualAntomatic='" + manualAntomatic + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", openWashTime='" + openWashTime + '\'' +
                ", nightWash='" + nightWash + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
