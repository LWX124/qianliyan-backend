package com.stylefeng.guns.modular.system.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccidentVo implements Serializable {
    private static final long serialVersionUID = 3570436333293091646L;

    private Long id;

    /**
     * 上报人id
     */
    private String openid;
    /**
     * 视频url
     */
    private String video;
    /**
     * 事故上报经度
     */
    private BigDecimal lng;
    /**
     * 事故上报纬度
     */
    private BigDecimal lat;
    /**
     * 事故上报时间
     */
    private Date createTime;
    /**
     * 审核人id
     */
    private String checkId;
    /**
     * 审核时间
     */
    private Date checkTime;
    /**
     * 审核状态  1：未审核  2：审核通过  3：审核失败
     */
    private Integer status;
    /**
     * 上报地址名称
     */
    private String address;

    private String url;

    /**
     * 来源标识（SSP等，标识来自哪个小程序）
     */
    private String source;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
