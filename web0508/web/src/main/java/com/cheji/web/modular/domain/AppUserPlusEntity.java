package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

@TableName("app_user_plus")
public class AppUserPlusEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Date createTime;
    private Date invalidTimeStart;
    private Date invalidTimeEnd;
    private String inviteCode;

    @Override
    public String toString() {
        return "AppUserPlusEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", invalidTimeStart=" + invalidTimeStart +
                ", invalidTimeEnd=" + invalidTimeEnd +
                ", inviteCode='" + inviteCode + '\'' +
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

    public Date getInvalidTimeStart() {
        return invalidTimeStart;
    }

    public void setInvalidTimeStart(Date invalidTimeStart) {
        this.invalidTimeStart = invalidTimeStart;
    }

    public Date getInvalidTimeEnd() {
        return invalidTimeEnd;
    }

    public void setInvalidTimeEnd(Date invalidTimeEnd) {
        this.invalidTimeEnd = invalidTimeEnd;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
