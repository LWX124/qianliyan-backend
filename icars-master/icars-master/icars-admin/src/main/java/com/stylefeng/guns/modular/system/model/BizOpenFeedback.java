package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-20
 */
@TableName("biz_open_feedback")
public class BizOpenFeedback extends Model<BizOpenFeedback> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户账户
     */
    private String account;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 时间
     */
    @TableField("cre_time")
    private Date creTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizOpenFeedback{" +
        "id=" + id +
        ", account=" + account +
        ", content=" + content +
        ", creTime=" + creTime +
        "}";
    }
}
