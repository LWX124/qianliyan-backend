package com.cheji.web.modular.cwork;

public class FansVideoDto {
    private String id;      //事故视频id
    private String userId;  //用户id
    private String video;   //视频url
    private Integer statuse;    //审核状态
    private String reason;  //审核原因
    private String type;    //type=1
    private String introduce;   //简介
    private String createTime;  //提报时间
    private String finalMeg;

    public String getFinalMeg() {
        return finalMeg;
    }

    public void setFinalMeg(String finalMeg) {
        this.finalMeg = finalMeg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getStatuse() {
        return statuse;
    }

    public void setStatuse(Integer statuse) {
        this.statuse = statuse;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "FansVideoDto{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", video='" + video + '\'' +
                ", statuse=" + statuse +
                ", reason='" + reason + '\'' +
                ", type='" + type + '\'' +
                ", introduce='" + introduce + '\'' +
                ", createTime='" + createTime + '\'' +
                ", finalMeg='" + finalMeg + '\'' +
                '}';
    }
}
