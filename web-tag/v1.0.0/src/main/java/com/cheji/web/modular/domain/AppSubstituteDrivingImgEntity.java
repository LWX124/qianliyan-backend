package com.cheji.web.modular.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;


import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 代驾图片表
 * </p>
 *
 * @author Ashes
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_substitute_driving_img")
public class AppSubstituteDrivingImgEntity extends Model<AppSubstituteDrivingImgEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联代价表
     */
    @TableField("subsitute_driving_id")
    private Integer subsituteDrivingId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer index;

    /**
     * 1,接车图片。2,送达图片
     */
    private Integer type;

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

    public Integer getSubsituteDrivingId() {
        return subsituteDrivingId;
    }

    public void setSubsituteDrivingId(Integer subsituteDrivingId) {
        this.subsituteDrivingId = subsituteDrivingId;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AppSubstituteDrivingImgEntity{" +
                "id=" + id +
                ", subsituteDrivingId=" + subsituteDrivingId +
                ", url='" + url + '\'' +
                ", index=" + index +
                ", type=" + type +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
