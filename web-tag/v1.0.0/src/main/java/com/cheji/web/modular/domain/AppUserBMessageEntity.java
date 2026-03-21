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
 * 用户服务信息表
 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_user_b_message")
public class AppUserBMessageEntity extends Model<AppUserBMessageEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    @TableField("head_img")
    private String headImg;

    /**
     * 工作地址
     */
    @TableField("work_place")
    private String workPlace;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 拖车车牌
     */
    @TableField("trailer_plate")
    private String trailerPlate;

    /**
     * 分数
     */
    private BigDecimal score;

    /**
     * 订单个数
     */
    @TableField("order_number")
    private Integer orderNumber;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 工作等级
     */
    private Integer level;

    private String phone;

    /**
     * 工作内容（1.救援，2.年检，3。喷漆，4.代驾）
     */
    @TableField("wrok_type")
    private Integer wrokType;

    /**
     * 驾龄
     */
    @TableField("driver_year")
    private String driverYear;

    /**
     * 营业状态 1.开始 2未营业
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 技术年龄
     */
    @TableField("technology_year")
    private String technologyYear;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    @Override
    public String toString() {
        return "AppUserBMessageEntity{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", userBId=" + userBId +
                ", name='" + name + '\'' +
                ", headImg='" + headImg + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", trailerPlate='" + trailerPlate + '\'' +
                ", score=" + score +
                ", orderNumber=" + orderNumber +
                ", introduction='" + introduction + '\'' +
                ", level=" + level +
                ", phone='" + phone + '\'' +
                ", wrokType=" + wrokType +
                ", driverYear='" + driverYear + '\'' +
                ", businessType=" + businessType +
                ", technologyYear='" + technologyYear + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
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

    public String getTrailerPlate() {
        return trailerPlate;
    }

    public void setTrailerPlate(String trailerPlate) {
        this.trailerPlate = trailerPlate;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getWrokType() {
        return wrokType;
    }

    public void setWrokType(Integer wrokType) {
        this.wrokType = wrokType;
    }

    public String getDriverYear() {
        return driverYear;
    }

    public void setDriverYear(String driverYear) {
        this.driverYear = driverYear;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTechnologyYear() {
        return technologyYear;
    }

    public void setTechnologyYear(String technologyYear) {
        this.technologyYear = technologyYear;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
