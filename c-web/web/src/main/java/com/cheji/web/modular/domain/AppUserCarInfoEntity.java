package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app上报事故信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-28
 */
@TableName("app_user_car_info")
public class AppUserCarInfoEntity extends Model<AppUserCarInfoEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车架号（大于等于6位）
     */
    @TableField("car_code")
    private String carCode;

    /**
     * 发动机号码（大于等于6位）
     */
    @TableField("car_engine_code")
    private String carEngineCode;

    /**
     * 车牌
     */
    @TableField("car_number")
    private String carNumber;

    /**
     * 1 普通轿车，2 新能源汽车
     */
    @TableField("type")
    private Integer type;

    @TableField("user_id")
    private Integer userId;

    /**
     * 发送短信提醒标记(1：提醒)
     */
    @TableField("sms_flag")
    private Integer smsFlag;

    private Date createTime;

    @Override
    public String toString() {
        return "AppUserCarInfoEntity{" +
                "id=" + id +
                ", carCode='" + carCode + '\'' +
                ", carEngineCode='" + carEngineCode + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", smsFlag=" + smsFlag +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(Integer smsFlag) {
        this.smsFlag = smsFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public String getCarEngineCode() {
        return carEngineCode;
    }

    public void setCarEngineCode(String carEngineCode) {
        this.carEngineCode = carEngineCode;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
