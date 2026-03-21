package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * @author stylefeng
 * @since 2019-03-22
 */
@TableName("biz_open_notify")
public class BizOpenNotify extends Model<BizOpenNotify> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("cre_time")
    private Date creTime;
    private String title;
    /**
     * 默认为0，如果存在账户号则为单独发送的消息
     */
    private String type;
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizOpenNotify{" +
        "id=" + id +
        ", creTime=" + creTime +
        ", title=" + title +
        ", type=" + type +
        ", content=" + content +
        "}";
    }
}
