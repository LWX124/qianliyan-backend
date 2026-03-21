package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

//作品列表打工类
public class WorkList {
    private String id;              //上传事故视频得id
    private String video;        //视频url
    private String thumbnailUrl;   //缩略图
    private String introduce;         //简介
    private String avatar;          //头像
    private Long thumbsCount;     //点赞数量
    private Long commentsCount;   //评论数量
    private Long shareCount;        //分享次数
    private String name;            //用户名
    private String address;         //地址
    private String userId;          //用户id
    private String videoId;         //关联视频id
    private Integer likeFlag;       //点赞状态
    private String isPass;           //是否通过审核
    private String reason;          //不通过原因
    private Long count;          //点在数量2

    private String createTime;  //创建时间
    private BigDecimal amount;  //金额

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getThumbsCount() {
        return thumbsCount;
    }

    public void setThumbsCount(Long thumbsCount) {
        this.thumbsCount = thumbsCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Integer likeFlag) {
        this.likeFlag = likeFlag;
    }

    @Override
    public String toString() {
        return "WorkList{" +
                "id='" + id + '\'' +
                ", video='" + video + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", introduce='" + introduce + '\'' +
                ", avatar='" + avatar + '\'' +
                ", thumbsCount=" + thumbsCount +
                ", commentsCount=" + commentsCount +
                ", shareCount=" + shareCount +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                ", videoId='" + videoId + '\'' +
                ", likeFlag=" + likeFlag +
                ", isPass='" + isPass + '\'' +
                ", reason='" + reason + '\'' +
                ", count=" + count +
                ", createTime='" + createTime + '\'' +
                ", amount=" + amount +
                '}';
    }
}
