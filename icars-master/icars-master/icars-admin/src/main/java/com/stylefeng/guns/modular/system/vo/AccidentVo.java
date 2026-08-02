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

    /**
     * 兼容小程序在定位失败时传空串的情况。
     *
     * 小程序 data 中 longitude/latitude 初始值为 ''，定位失败也会重置为 ''，
     * 上报时会以 {"lng":"","lat":""} 提交。BigDecimal 无法反序列化空串，
     * Jackson 会抛 HttpMessageNotReadableException，请求在进入方法体前就失败，
     * 小程序侧表现为「网络异常」。这里把空串按 null 处理，使无定位也能上报。
     *
     * 注：Jackson 会优先选用 String 参数的 setter 来处理 JSON 字符串值，
     * 数字值仍走上面的 BigDecimal setter。
     */
    public void setLng(String lng) {
        this.lng = parseDecimal(lng);
    }

    public void setLat(String lat) {
        this.lat = parseDecimal(lat);
    }

    private static BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
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
