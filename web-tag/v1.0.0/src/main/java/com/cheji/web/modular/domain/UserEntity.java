package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
 */
@TableName("app_user")
public class UserEntity extends Model<UserEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 账户(和电话一致)
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    @TableField(exist = false)
    private String newPassword;
    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 名字
     */
    private String name;

    private BigDecimal balance;
    /**
     * 状态(1:启用，2:冻结，3:删除，4:注册)
     */
    private Integer status;

    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;
    /**
     * 微信unionId
     */
    @TableField("wx_union_id")
    private String wxUnionId;
    /**
     * 微信openId
     */
    @TableField("wx_open_id")
    private String wxOpenId;


    @TableField("is_location")
    private Integer isLocation;

    @TableField("authentication")
    private String authentication;

    public Integer getVipLv() {
        return vipLv;
    }

    public void setVipLv(Integer vipLv) {
        this.vipLv = vipLv;
    }

    @TableField(value = "vip_lv",strategy = FieldStrategy.IGNORED)
    private Integer vipLv;

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Integer getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Integer isLocation) {
        this.isLocation = isLocation;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", parentId=" + parentId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salt='" + salt + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", wxUnionId='" + wxUnionId + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                '}';
    }
}
