package com.cheji.web.modular.cwork;

import java.util.Date;

//事故详情页面返回类
public class AccidentDetails {
    public Integer id;      //事故id
    public String video;    //视频地址
    public String userId;   //上报人id
    public Double lng;      //经度
    public Double lat;      //纬度
    public String address;  //上报地址
    public Date createTime; //上报时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        createTime = createTime;
    }

    @Override
    public String toString() {
        return "AccidentDetails{" +
                "id=" + id +
                ", video='" + video + '\'' +
                ", userId='" + userId + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", address='" + address + '\'' +
                ", CreateTime=" + createTime +
                '}';
    }
}
