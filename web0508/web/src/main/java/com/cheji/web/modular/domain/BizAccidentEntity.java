package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 事故上报信息表
 * </p>
 *
 * @author Ashes
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("biz_accident")
public class BizAccidentEntity extends Model<BizAccidentEntity> {

    private static final long serialVersionUID = 1L;


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
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 审核人id
     */
    @TableField("checkId")
    private String checkId;

    /**
     * 状态(1：未审核  2：审核通过  3：审核失败）
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
     * 事故真实性(0：真实 1：不真实)
     */
    private Integer realness;

    /**
     * 是否添加到评论表
     */
    private Integer isaddvideo;

    /**
     * geovalue值
     */
    private String georedis;

    /**
     * 是否从geo中删除
     */
    private String delgeo;

    /**
     * 关联视频id
     */
    @TableField("video_id")
    private Long videoId;

    /**
     * 上报时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 审核时间
     */
    @TableField("checkTime")
    private Date checkTime;

    /**
     * 保留字段
     */
    private Integer version;

    /**
     * 车总数
     */
    @TableField("carCount")
    private Integer carCount;

    /**
     * 上报地址
     */
    private String realaddress;

    /**
     * 现场的照片
     */
    @TableField("realImg")
    private String realImg;

    /**
     * 事故照片
     */
    @TableField("accImg")
    private String accImg;

    /**
     * 事故真实纬度
     */
    private BigDecimal lat2;

    /**
     * 事故真实经度
     */
    private BigDecimal lng2;

    /**
     * 是否置顶
     */
    @TableField("topFlag")
    private Integer topFlag;

    /**
     * 到达时间
     */
    @TableField("realArrTime")
    private String realArrTime;

    /**
     * 缩略图；1:已处理   2:未处理
     */
    @TableField("thumbnail_flag")
    private Integer thumbnailFlag;

    /**
     * 缩略图地址
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;


    @TableField("is_order")
    private Integer isOrder;


    @TableField("update_time")
    private Date updateTime;

    @TableField("loss_vehicle")
    private String lossVehicle;

    @TableField("risk_factor")
    private String riskFactor;

    public String getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(String riskFactor) {
        this.riskFactor = riskFactor;
    }

    public String getLossVehicle() {
        return lossVehicle;
    }

    public void setLossVehicle(String lossVehicle) {
        this.lossVehicle = lossVehicle;
    }

    public Integer getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(Integer isOrder) {
        this.isOrder = isOrder;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRealness() {
        return realness;
    }

    public void setRealness(Integer realness) {
        this.realness = realness;
    }

    public Integer getIsaddvideo() {
        return isaddvideo;
    }

    public void setIsaddvideo(Integer isaddvideo) {
        this.isaddvideo = isaddvideo;
    }

    public String getGeoredis() {
        return georedis;
    }

    public void setGeoredis(String georedis) {
        this.georedis = georedis;
    }

    public String getDelgeo() {
        return delgeo;
    }

    public void setDelgeo(String delgeo) {
        this.delgeo = delgeo;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public String getRealaddress() {
        return realaddress;
    }

    public void setRealaddress(String realaddress) {
        this.realaddress = realaddress;
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

    public Integer getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(Integer topFlag) {
        this.topFlag = topFlag;
    }

    public String getRealArrTime() {
        return realArrTime;
    }

    public void setRealArrTime(String realArrTime) {
        this.realArrTime = realArrTime;
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
}
