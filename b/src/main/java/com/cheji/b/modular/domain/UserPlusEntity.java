package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@TableName("app_user_plus")
public class UserPlusEntity extends Model<UserPlusEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("create_time")
    private Date createTime;
    @TableField("user_id")
    private Integer userId;
    /**
     * 有效期开始时间
     */
    @TableField("invalid_time_start")
    private Date invalidTimeStart;
    /**
     * 有效期结束时间
     */
    @TableField("invalid_time_end")
    private Date invalidTimeEnd;
    /**
     * 邀请码
     */
    @TableField("invite_code")
    private String inviteCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserPlusEntity{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", userId=" + userId +
        ", invalidTimeStart=" + invalidTimeStart +
        ", invalidTimeEnd=" + invalidTimeEnd +
        ", inviteCode=" + inviteCode +
        "}";
    }
}
