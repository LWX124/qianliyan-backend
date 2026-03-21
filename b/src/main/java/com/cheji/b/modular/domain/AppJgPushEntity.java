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
 * 激光推送表
 * </p>
 *
 * @author Ashes
 * @since 2019-11-13
 */
@TableName("app_jg_push")
public class AppJgPushEntity extends Model<AppJgPushEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 来源（C端，B端）
     */
    private String source;
    /**
     * 是否推送（0，没有，1，有）
     */
    private String ispush;
    /**
     * 操作类型()
     */
    private String type;
    /**
     * C端用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * B端用户id
     */
    @TableField("user_b_id")
    private String userBId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
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
        return "AppJgPushEntity{" +
        ", id=" + id +
        ", source=" + source +
        ", ispush=" + ispush +
        ", type=" + type +
        ", userId=" + userId +
        ", userBId=" + userBId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
