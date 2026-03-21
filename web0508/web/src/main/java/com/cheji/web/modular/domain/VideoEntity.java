package com.cheji.web.modular.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 视频信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-17
 */
@TableName("app_video")
public class VideoEntity extends Model<VideoEntity> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 视频地址
     */
    private String url;
    /**
     * 关联user表
     */
    @TableField("user_id")
    private String  userId;

    //来源
    private Integer source;
    /**
     * 点赞数
     */
    private Integer count;
    /**
     * 分享次数
     */
    private Integer share;
    /**
     * 审核状态
     */
    private Integer state;
    /**
     * 未通过原因
     */
    private String reson;
    /**
     * 是否展示到app
     */
    @TableField("app_show_falg")
    private Integer appShowFalg;
    /**
     * 视频播放量
     */
    @TableField("app_view_counts")
    private Long appViewCounts;
    /**
     * 关联事故表
     */
    @TableField("accident_id")
    private Integer accidentId;

    private String address;

    private String introduce;
    /**
     * 缩略图；1:已处理   2:未处理
     */
    @TableField("thumbnail_flag")
    private Integer thumbnailFlag;
    /**
     * 缩略图地址
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;
    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public Integer getAppShowFalg() {
        return appShowFalg;
    }

    public void setAppShowFalg(Integer appShowFalg) {
        this.appShowFalg = appShowFalg;
    }

    public Long getAppViewCounts() {
        return appViewCounts;
    }

    public void setAppViewCounts(Long appViewCounts) {
        this.appViewCounts = appViewCounts;
    }

    public Integer getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(Integer accidentId) {
        this.accidentId = accidentId;
    }

    public Integer getThumbnailFlag() {
        return thumbnailFlag;
    }

    public void setThumbnailFlag(Integer thumbnailFlag) {
        this.thumbnailFlag = thumbnailFlag;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VideoEntity{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", source=" + source +
                ", count=" + count +
                ", share=" + share +
                ", state=" + state +
                ", reson='" + reson + '\'' +
                ", appShowFalg=" + appShowFalg +
                ", appViewCounts=" + appViewCounts +
                ", accidentId=" + accidentId +
                ", address='" + address + '\'' +
                ", introduce='" + introduce + '\'' +
                ", thumbnailFlag=" + thumbnailFlag +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
