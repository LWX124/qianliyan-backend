package com.cheji.b.modular.dto;

public class UserBDto {
    private String id;
    private String headImg;
    private String huanxinUserName;
    private String huanxinPassword;
    private String merchanstName;
    private Integer unreadMessage;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
    }

    public String getMerchanstName() {
        return merchanstName;
    }

    public void setMerchanstName(String merchanstName) {
        this.merchanstName = merchanstName;
    }

    public Integer getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserBDto{" +
                "id='" + id + '\'' +
                ", headImg='" + headImg + '\'' +
                ", huanxinUserName='" + huanxinUserName + '\'' +
                ", huanxinPassword='" + huanxinPassword + '\'' +
                ", merchanstName='" + merchanstName + '\'' +
                ", unreadMessage=" + unreadMessage +
                ", time='" + time + '\'' +
                '}';
    }
}
