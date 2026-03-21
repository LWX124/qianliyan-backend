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
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@TableName("app_feedback")
public class FeedbackEntity extends Model<FeedbackEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联商户表
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 第一张图片
     */
    @TableField("img_first")
    private String imgFirst;
    /**
     * 第二张图片
     */
    @TableField("img_second")
    private String imgSecond;
    /**
     * 第三张图片
     */
    @TableField("img_third")
    private String imgThird;
    /**
     * 第四张图片
     */
    @TableField("img_fourth")
    private String imgFourth;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgFirst() {
        return imgFirst;
    }

    public void setImgFirst(String imgFirst) {
        this.imgFirst = imgFirst;
    }

    public String getImgSecond() {
        return imgSecond;
    }

    public void setImgSecond(String imgSecond) {
        this.imgSecond = imgSecond;
    }

    public String getImgThird() {
        return imgThird;
    }

    public void setImgThird(String imgThird) {
        this.imgThird = imgThird;
    }

    public String getImgFourth() {
        return imgFourth;
    }

    public void setImgFourth(String imgFourth) {
        this.imgFourth = imgFourth;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FeedbackEntity{" +
        ", id=" + id +
        ", userBId=" + userBId +
        ", phoneNumber=" + phoneNumber +
        ", content=" + content +
        ", imgFirst=" + imgFirst +
        ", imgSecond=" + imgSecond +
        ", imgThird=" + imgThird +
        ", imgFourth=" + imgFourth +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
