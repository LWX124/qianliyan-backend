package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-10-09
 */
@TableName("app_car_brand")
public class CarBrandEntity extends Model<CarBrandEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer uniacid;
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 首字母
     */
    private String initials;
    private String name;
    @TableField("pic_url")
    private String picUrl;
    /**
     * 1:启用 2：禁用
     */
    private Integer status;
    private Integer sort;
    @TableField("is_hot")
    private Integer isHot;
    @TableField("create_time")
    private Integer createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUniacid() {
        return uniacid;
    }

    public void setUniacid(Integer uniacid) {
        this.uniacid = uniacid;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CarBrandEntity{" +
        ", id=" + id +
        ", uniacid=" + uniacid +
        ", parentId=" + parentId +
        ", initials=" + initials +
        ", name=" + name +
        ", picUrl=" + picUrl +
        ", status=" + status +
        ", sort=" + sort +
        ", isHot=" + isHot +
        ", createTime=" + createTime +
        "}";
    }
}
