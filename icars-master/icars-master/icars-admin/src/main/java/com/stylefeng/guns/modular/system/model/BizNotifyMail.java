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
 * 系统邮件通知主表
 * </p>
 *
 * @author kosans
 * @since 2018-08-01
 */
@TableName("biz_notify_mail")
public class BizNotifyMail extends Model<BizNotifyMail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 邮件地址
     */
    private String email;
    /**
     * 接受通知类型 0：支付宝相关
     */
    @TableField("mail_type")
    private Integer mailType;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMailType() {
        return mailType;
    }

    public void setMailType(Integer mailType) {
        this.mailType = mailType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizNotifyMail{" +
        "id=" + id +
        ", email=" + email +
        ", mailType=" + mailType +
        ", createTime=" + createTime +
        "}";
    }
}
