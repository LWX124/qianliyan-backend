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
 * 品牌和商户关联表
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@TableName("app_merchants_brand")
public class MerchantsBrandEntity extends Model<MerchantsBrandEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private String userBId;
    /**
     * 汽车品牌id
     */
    @TableField("brand_id")
    private String brandId;
    /**
     * 品牌启用状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
        return "MerchantsBrandEntity{" +
                "id=" + id +
                ", userBId='" + userBId + '\'' +
                ", brandId='" + brandId + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
