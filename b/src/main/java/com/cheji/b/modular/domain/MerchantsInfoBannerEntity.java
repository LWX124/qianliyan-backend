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
 * 商户详情banner图
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@TableName("app_merchants_info_banner")
public class MerchantsInfoBannerEntity extends Model<MerchantsInfoBannerEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 从小到大排序
     */
    private Integer index;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;
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

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
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
        return "MerchantsInfoBannerEntity{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", index=" + index +
                ", userBId=" + userBId +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
