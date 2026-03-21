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
 * 
 * </p>
 *
 * @author Ashes
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_spray_paint_details")
public class AppSprayPaintDetailsEntity extends Model<AppSprayPaintDetailsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 喷漆订单id
     */
    @TableField("spray_paint_id")
    private Integer sprayPaintId;

    /**
     * 部位
     */
    private String place;

    /**
     * 价格
     */
    private BigDecimal price;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSprayPaintId() {
        return sprayPaintId;
    }

    public void setSprayPaintId(Integer sprayPaintId) {
        this.sprayPaintId = sprayPaintId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "AppSprayPaintDetailsEntity{" +
                "id=" + id +
                ", sprayPaintId=" + sprayPaintId +
                ", place='" + place + '\'' +
                ", price=" + price +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
