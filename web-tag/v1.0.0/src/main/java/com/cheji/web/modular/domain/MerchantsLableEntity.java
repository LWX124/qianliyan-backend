package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商户和标签关系表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-20
 */
@TableName("app_merchants_lable")
public class MerchantsLableEntity extends Model<MerchantsLableEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 关联商户表
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 关联标签表
     */
    @TableField("lable_id")
    private Integer lableId;
    /**
     * 返点数
     */
    private BigDecimal rebates;
    /**
     * 标签启用状态(1.启用,0.停用)
     */

    @TableField(exist = false)
    private String lableName;

    @TableField(exist = false)
   private String url;

    private Integer state;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

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

    public Integer getLableId() {
        return lableId;
    }

    public void setLableId(Integer lableId) {
        this.lableId = lableId;
    }

    public BigDecimal getRebates() {
        return rebates;
    }

    public void setRebates(BigDecimal rebates) {
        this.rebates = rebates;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MerchantsLableEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", lableId=" + lableId +
                ", rebates=" + rebates +
                ", lableName='" + lableName + '\'' +
                ", url='" + url + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
