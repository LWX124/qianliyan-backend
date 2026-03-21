package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class AccidentReward {
    private Integer id;
    private String avatar;         //头像
    private String userId;         //名字
    private String video;           //视频
    private String imgUrl;          //图片地址
    private String address;        //地址
    private String createTime;     //时间
    private String thumbnailUrl;    //缩略图
    private Long commentNumber;     //评论数
    private BigDecimal redPay;      //红包金额
    private String playNumber;      //播放数量


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public BigDecimal getRedPay() {
        return redPay;
    }

    public void setRedPay(BigDecimal redPay) {
        this.redPay = redPay;
    }

    public String getPlayNumber() {
        return playNumber;
    }

    public void setPlayNumber(String playNumber) {
        this.playNumber = playNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
