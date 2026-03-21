package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 图片表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
@TableName("app_img")
public class ImgEntity extends Model<ImgEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * keyId,订单或者商户评论外键id
     */
    @TableField("key_id")
    private String keyId;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 图片类型(1,商户评论图,2,订单资料图)
     */
    private Integer type;
    /**
     * 图片顺序
     */
    private Integer index;
    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
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

    @Override
    public String toString() {
        return "ImgEntity{" +
        ", id=" + id +
        ", keyId=" + keyId +
        ", url=" + url +
        ", type=" + type +
        ", index=" + index +
        ", creatTime=" + creatTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
