package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.stylefeng.guns.modular.system.constant.ThumbnailFlag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
@TableName("biz_accident")
public class Accident extends Model<Accident> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 事故真实经度
     */
    private BigDecimal lng2;
    /**
     * 事故上报纬度
     */
    private BigDecimal lat;
    /**
     * 事故真实纬度
     */
    private BigDecimal lat2;
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
     * 审核原因
     */
    private String reason;
    /**
     * 上报地址名称
     */
    private String address;

    /**
     * 上报地址名称
     */
    private String realaddress;

    /**
     * 现场照片
     */
    private String realImg;
    /**
     * 事故照片
     */
    private String accImg;

    /**
     * @see ThumbnailFlag
     * 缩略图；1:已处理   2:未处理
     */
    @TableField(value = "thumbnail_flag")
    private Integer thumbnailFlag;

    /**
     * 缩略图地址
     */
    @TableField(value = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * 事故推送人员名字
     */
    @TableField(exist = false)
    private String name;

    /**
     * 经纬度拼接成腾讯地图url
     */
    @TableField(exist = false)
    private String mapUrl;

    /**
     * 是否真实
     */
    private Integer realness;

    /**
     * 车总数
     */
    private Integer carCount;
    /**
     * topFlag
     */
    private String topFlag;


    /**
     * 实际到达时间
     */
    private String realArrTime;

    /**
     * 是否黑名单用户
     */
    @TableField(exist = false)
    private Integer blackList;

    /**
     * 来源标识（SSP等，标识来自哪个小程序）
     */
    private String source;


    /**
     * 版本（乐观锁保留字段）
     */
    private Integer version;

    @Override
    public String toString() {
        return "Accident{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", video='" + video + '\'' +
                ", lng=" + lng +
                ", lng2=" + lng2 +
                ", lat=" + lat +
                ", lat2=" + lat2 +
                ", createTime=" + createTime +
                ", checkId='" + checkId + '\'' +
                ", checkTime=" + checkTime +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", address='" + address + '\'' +
                ", realaddress='" + realaddress + '\'' +
                ", realImg='" + realImg + '\'' +
                ", accImg='" + accImg + '\'' +
                ", thumbnailFlag=" + thumbnailFlag +
                ", thumbnailUrl=" + thumbnailUrl +
                ", name='" + name + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                ", realness=" + realness +
                ", carCount=" + carCount +
                ", topFlag='" + topFlag + '\'' +
                ", realArrTime='" + realArrTime + '\'' +
                ", blackList=" + blackList +
                ", version=" + version +
                '}';
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


    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getRealness() {
        return realness;
    }

    public void setRealness(Integer realness) {
        this.realness = realness;
    }

    public Integer getBlackList() {
        return blackList;
    }

    public void setBlackList(Integer blackList) {
        this.blackList = blackList;
    }

    public String getRealImg() {
        return realImg;
    }

    public void setRealImg(String realImg) {
        this.realImg = realImg;
    }

    public String getAccImg() {
        return accImg;
    }

    public void setAccImg(String accImg) {
        this.accImg = accImg;
    }

    public BigDecimal getLat2() {
        return lat2;
    }

    public void setLat2(BigDecimal lat2) {
        this.lat2 = lat2;
    }

    public BigDecimal getLng2() {
        return lng2;
    }

    public void setLng2(BigDecimal lng2) {
        this.lng2 = lng2;
    }

    public String getRealaddress() {
        return realaddress;
    }

    public void setRealaddress(String realaddress) {
        this.realaddress = realaddress;
    }


    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public String getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(String topFlag) {
        this.topFlag = topFlag;
    }

    public String getRealArrTime() {
        return realArrTime;
    }

    public void setRealArrTime(String realArrTime) {
        this.realArrTime = realArrTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
