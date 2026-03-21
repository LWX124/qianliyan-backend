package com.cheji.b.modular.domain;

import java.math.BigDecimal;
import java.util.Date;
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
 * @since 2019-09-29
 */
@TableName("app_lable_details")
public class LableDetailsEntity extends Model<LableDetailsEntity> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 服务内容
     */
    private String content;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;


    @TableField(exist = false)
    private BigDecimal rebates;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public BigDecimal getRebates() {
        return rebates;
    }

    public void setRebates(BigDecimal rebates) {
        this.rebates = rebates;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LableDetailsEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", rebates=" + rebates +
                '}';
    }
}
