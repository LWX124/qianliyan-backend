package com.cheji.b.modular.dto;

//商户详情数据
public class ListDetailsDto {
    private String accid;       //事故id
    private String video;       //视频地址
    private String address;     //地址
    private String phoneNumber; //电话号码
    private String name;        //用户名称
    private String time;        //时间
    private Double lng;      //经度
    private Double lat;      //纬度
    private Integer realness;   //事故真实性

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Integer getRealness() {
        return realness;
    }

    public void setRealness(Integer realness) {
        this.realness = realness;
    }

    @Override
    public String toString() {
        return "ListDetailsDto{" +
                "accid='" + accid + '\'' +
                ", video='" + video + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", realness=" + realness +
                '}';
    }
}

