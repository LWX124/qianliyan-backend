package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app上报事故信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-28
 */
@TableName("app_accident_record")
public class AccidentRecordEntity extends Model<AccidentRecordEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 上报用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 视频url
     */
    private String video;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 审核人id
     */
    private String checkid;
    /**
     * 状态(1：未审核  2：审核通过  3：审核失败）
     */
    private Integer statuse;
    /**
     * 审核原因
     */
    private String reason;
    /**
     * 上报地址名称
     */
    private String address;
    /**
     * 事故真实性(-1,待处理.0,真现场.1,假现场.2,已撤离.3,非事故)
     */
    private Integer realness;
    /**
     * 视频类型(1,派单视频，2.用户上传)
     */
    private Integer type;

    //是否展示到app
    private Integer isaddvideo;

    private String introduce;

    private String georedis;

    private String delgeo;
    /**
     * 上报时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 审核时间
     */
    @TableField("check_time")
    private Date checkTime;
    /**
     * 上报地址
     */
    @TableField("real_address")
    private String realAddress;
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


    @TableField("img_url")
    private String imgUrl;

    @TableField("is_order")
    private Integer isOrder;

    @TableField("update_time")
    private Date updateTime;

    @TableField("loss_vehicle")
    private String lossVehicle;

    @TableField("risk_factor")
    private String riskFactor;

    private String plate;

    private String name;

    private String phone;

    private String company;

    private String remark;

    private String brand;

    @TableField(exist = false)
    private String pbid;

    @TableField(exist = false)
    private String cid;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPbid() {
        return pbid;
    }

    public void setPbid(String pbid) {
        this.pbid = pbid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public String getDelgeo() {
        return delgeo;
    }

    public void setDelgeo(String delgeo) {
        this.delgeo = delgeo;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGeoredis() {
        return georedis;
    }

    public void setGeoredis(String georedis) {
        this.georedis = georedis;
    }

    public String getRealAddress() {
        return realAddress;
    }

    public void setRealAddress(String realAddress) {
        this.realAddress = realAddress;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getIsaddvideo() {
        return isaddvideo;
    }

    public void setIsaddvideo(Integer isaddvideo) {
        this.isaddvideo = isaddvideo;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AccidentRecordEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", video='" + video + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", checkid='" + checkid + '\'' +
                ", statuse=" + statuse +
                ", reason='" + reason + '\'' +
                ", address='" + address + '\'' +
                ", realness=" + realness +
                ", type=" + type +
                ", isaddvideo=" + isaddvideo +
                ", introduce='" + introduce + '\'' +
                ", georedis='" + georedis + '\'' +
                ", createTime=" + createTime +
                ", checkTime=" + checkTime +
                ", realAddress='" + realAddress + '\'' +
                ", thumbnailFlag=" + thumbnailFlag +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
