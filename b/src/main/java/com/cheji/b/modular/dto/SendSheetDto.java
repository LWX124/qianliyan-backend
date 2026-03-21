package com.cheji.b.modular.dto;

import java.math.BigDecimal;
import java.util.List;

public class SendSheetDto {
    private String id;
    private String falconTrajectory;
    private String tid;
    private List<String> urlList;
    private BigDecimal lat;
    private BigDecimal lng;
    private Integer nowState;
    private String backText;


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFalconTrajectory() {
        return falconTrajectory;
    }

    public void setFalconTrajectory(String falconTrajectory) {
        this.falconTrajectory = falconTrajectory;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public Integer getNowState() {
        return nowState;
    }

    public void setNowState(Integer nowState) {
        this.nowState = nowState;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }
}
