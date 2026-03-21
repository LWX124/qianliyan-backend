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
 * 通过图片上架的4s店表
 * </p>
 *
 * @author Ashes
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_up_merchants")
public class AppUpMerchantsEntity extends Model<AppUpMerchantsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 维度
     */
    private BigDecimal lat;

    /**
     * 名字
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 分数
     */
    private Integer score;

    @TableField("huanxin_username")
    private String huanxinUsername;		//环信名称

    @TableField("huanxin_password")
    private String huanxinPassword;		//环信密码

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("unread_message")
    private Integer unreadMessage;

    @TableField("save_time")
    private Date saveTime;

    private BigDecimal proportion;

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public Integer getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(Integer unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getHuanxinUsername() {
        return huanxinUsername;
    }

    public void setHuanxinUsername(String huanxinUsername) {
        this.huanxinUsername = huanxinUsername;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
