package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 视频点赞表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-19
 */
@TableName("app_video_thumbs")
public class VideoThumbsEntity extends Model<VideoThumbsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("video_id")
    private String videoId;
    @TableField("user_id")
    private String userId;
    /**
     * 点赞状态，0取消，1点赞
     */
    private Integer status;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    public VideoThumbsEntity(){

    }

    public VideoThumbsEntity(String videoId, String userId, Integer status ){
        this.videoId = videoId;
        this.userId = userId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "VideoThumbsEntity{" +
        ", id=" + id +
        ", videoId=" + videoId +
        ", userId=" + userId +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
