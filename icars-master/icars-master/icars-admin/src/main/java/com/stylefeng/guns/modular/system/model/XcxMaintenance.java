package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 小程序 维修表
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-31
 */
@TableName("biz_xcx_maintenance")
public class XcxMaintenance extends Model<XcxMaintenance> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private BigDecimal price;
    @TableField("image_url")
    private String imageUrl;
    /**
     * 1.保养  2.油漆 3.钣金
     */
    private String type;
    /**
     * 套餐内容
     */
    private String content;
    /**
     * 使用车型
     */
    @TableField("car_type")
    private String carType;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "XcxMaintenance{" +
        "id=" + id +
        ", title=" + title +
        ", price=" + price +
        ", imageUrl=" + imageUrl +
        ", type=" + type +
        ", content=" + content +
        ", carType=" + carType +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
