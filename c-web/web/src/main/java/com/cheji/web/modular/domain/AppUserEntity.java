package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("app_user")
public class AppUserEntity extends Model<AppUserEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String avatar;
    private String username;
    private String password;
    private String phoneNumber;
    private String salt;
    private String name;
    private String authentication;
    private Integer status; //状态(1:启用，2:冻结，3:删除，4:注册)
    private Date creatTime;
    private Date updateTime;
    private String wxUnionId;
    private String wxOpenId;

    private String huanxinUserName;
    private String huanxinPassword;
    private BigDecimal balance;

    private Integer vipLv;

    private Integer isInner;


    @Override
    public String toString() {
        return "AppUserEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", wxUnionId='" + wxUnionId + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", huanxinPassword='" + huanxinPassword + '\'' +
                ", balance=" + balance +
                ", vipLv=" + vipLv +
                '}';
    }

    public Integer getIsInner() {
        return isInner;
    }

    public void setIsInner(Integer isInner) {
        this.isInner = isInner;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Integer getVipLv() {
        return vipLv;
    }

    public void setVipLv(Integer vipLv) {
        this.vipLv = vipLv;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public AppUserEntity() {
    }

    public AppUserEntity(Integer id) {
        this.id = id;
    }


    public AppUserEntity(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
