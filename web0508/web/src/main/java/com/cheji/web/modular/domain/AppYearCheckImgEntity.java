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
 * 年检图片表
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_year_check_img")
public class AppYearCheckImgEntity extends Model<AppYearCheckImgEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 年检订单表id
     */
    @TableField("app_year_check_id")
    private Integer appYearCheckId;

    /**
     * 图片路径
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

    public Integer getAppYearCheckId() {
        return appYearCheckId;
    }

    public void setAppYearCheckId(Integer appYearCheckId) {
        this.appYearCheckId = appYearCheckId;
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
        return "AppYearCheckImgEntity{" +
                "id=" + id +
                ", appYearCheckId=" + appYearCheckId +
                ", url='" + url + '\'' +
                ", index=" + index +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
