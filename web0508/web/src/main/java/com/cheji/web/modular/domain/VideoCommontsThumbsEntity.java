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
 * 用户点赞表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-14
 */
@TableName("app_video_commonts_thumbs")
public class VideoCommontsThumbsEntity extends Model<VideoCommontsThumbsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 被点赞的视频评论id
     */
    @TableField("video_commonts_id")
    private String videoCommontsId;
    /**
     * 点赞的用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 点赞状态，0取消，1点赞
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    public VideoCommontsThumbsEntity(){}

    public VideoCommontsThumbsEntity(String videoCommontsId, String userId, Integer status) {
        this.videoCommontsId = videoCommontsId;
        this.userId = userId;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoCommontsId() {
        return videoCommontsId;
    }

    public void setVideoCommontsId(String videoCommontsId) {
        this.videoCommontsId = videoCommontsId;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VideoCommontsThumbsEntity{" +
        ", id=" + id +
        ", videoCommontsId=" + videoCommontsId +
        ", userId=" + userId +
        ", status=" + status +
        ", createTime=" + createTime +
        "}";
    }
}
