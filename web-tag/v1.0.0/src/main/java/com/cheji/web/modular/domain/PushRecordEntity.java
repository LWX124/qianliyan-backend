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
 * app事故推送记录表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-27
 */
@TableName("app_push_record")
public class PushRecordEntity extends Model<PushRecordEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 事故id
     */
    private Integer accid;
    /**
     * 状态，0：未查看，1：已查看
     */
//    private Integer status;
    /**
     * 推送时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PushRecordEntity{" +
        ", id=" + id +
        ", userId=" + userId +
        ", accid=" + accid +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
