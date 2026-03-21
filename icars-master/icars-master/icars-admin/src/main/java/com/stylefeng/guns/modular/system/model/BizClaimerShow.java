package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 模范理赔顾问表
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-28
 */
@TableName("biz_claimer_show")
public class BizClaimerShow extends Model<BizClaimerShow> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String imgUrl;
    /**
     * 座右铭
     */
    private String motto;
    /**
     * 事迹简介
     */
    private String story;
    /**
     * 展示状态 0：展示   1下架
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizClaimerShow{" +
        "id=" + id +
        ", name=" + name +
        ", imgUrl=" + imgUrl +
        ", motto=" + motto +
        ", story=" + story +
        ", status=" + status +
        ", createtime=" + createtime +
        ", userId=" + userId +
        "}";
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
