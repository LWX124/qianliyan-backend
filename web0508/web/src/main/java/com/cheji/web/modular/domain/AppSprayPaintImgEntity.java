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
import java.util.Date;

/**
 * <p>
 * 喷漆图片表
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_spray_paint_img")
public class AppSprayPaintImgEntity extends Model<AppSprayPaintImgEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联喷漆表
     */
    @TableField("spray_paint_id")
    private Integer sprayPaintId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer index;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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
        return "AppSprayPaintImgEntity{" +
                "id=" + id +
                ", sprayPaintId=" + sprayPaintId +
                ", url='" + url + '\'' +
                ", index=" + index +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
