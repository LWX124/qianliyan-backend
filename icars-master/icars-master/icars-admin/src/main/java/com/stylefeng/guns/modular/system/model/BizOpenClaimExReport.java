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
 * 开放平台理赔单异常反馈表
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
@TableName("biz_open_claim_ex_report")
public class BizOpenClaimExReport extends Model<BizOpenClaimExReport> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 开放平台理赔单号
     */
    private String orderno;
    /**
     * 操作人账号
     */
    private String operator;
    /**
     * 操作人姓名
     */
    private String name;
    /**
     * 异常信息
     */
    private String message;
    /**
     * 图片链接
     */
    private String exImgUrls;
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

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExImgUrls() {
        return exImgUrls;
    }

    public void setExImgUrls(String exImgUrls) {
        this.exImgUrls = exImgUrls;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizOpenClaimExReport{" +
                "id=" + id +
                ", orderno='" + orderno + '\'' +
                ", operator='" + operator + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", exImgUrls='" + exImgUrls + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
