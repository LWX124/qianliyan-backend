package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商户服务顾问表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@TableName("app_merchants_servicer")
public class MerchantsServicerEntity extends Model<MerchantsServicerEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 顾问名字
     */
    private String name;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 图片地址
     */
    @TableField("Img_url")
    private String ImgUrl;
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

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
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
        return "MerchantsServicerEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
