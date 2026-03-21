package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("app_b_user_bank")
public class AppUserBankEntity extends Model<AppUserBankEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer bankId;
    private Integer userId;
    private Integer status;
    private String code;
    private String bankUserName;
    private Date createTime;

    @TableField(exist = false)
    private String bankName;
    @TableField(exist = false)
    private String bankImg;

    @Override
    public String toString() {
        return "AppUserBankEntity{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", userId=" + userId +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", bankUserName='" + bankUserName + '\'' +
                ", createTime=" + createTime +
                ", bankName='" + bankName + '\'' +
                ", bankImg='" + bankImg + '\'' +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }


}
