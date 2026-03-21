package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
@TableName("app_user_bank")
public class UserBankEntity extends Model<UserBankEntity> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @TableField("bank_id")
    private Integer bankId;
    @TableField("user_id")
    private Integer userId;
    @TableField("create_time")
    private Date createTime;
    /**
     * 1:有效  2：无效  3：删除
     */
    private Integer status;
    /**
     * 银行卡号
     */
    private String code;

    @TableField(exist = false)
    private String tail;    //手机尾号


    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserBankEntity{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", tail='" + tail + '\'' +
                '}';
    }
}
