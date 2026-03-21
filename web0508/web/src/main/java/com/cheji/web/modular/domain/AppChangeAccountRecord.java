package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * 用户更换手机号记录
 */
@TableName("app_change_account_record")
public class AppChangeAccountRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Date createTime;
    private String oldAccount;
    private String newAccount;

    @Override
    public String toString() {
        return "AppChangeAccountRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", oldAccount='" + oldAccount + '\'' +
                ", newAccount='" + newAccount + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOldAccount() {
        return oldAccount;
    }

    public void setOldAccount(String oldAccount) {
        this.oldAccount = oldAccount;
    }

    public String getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(String newAccount) {
        this.newAccount = newAccount;
    }
}
