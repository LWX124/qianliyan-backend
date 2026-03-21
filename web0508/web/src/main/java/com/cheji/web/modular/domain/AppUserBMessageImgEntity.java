package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * b端服务信息表

 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_user_b_message_img")
public class AppUserBMessageImgEntity extends Model<AppUserBMessageImgEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * type(1.基本照片，2拖车照片.)
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer index;

    /**
     * 图片地址
     */
    private String url;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AppUserBMessageImgEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", type=" + type +
                ", index=" + index +
                ", url='" + url + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
