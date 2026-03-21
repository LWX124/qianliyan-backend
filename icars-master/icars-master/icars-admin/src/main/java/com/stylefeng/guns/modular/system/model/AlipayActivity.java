package com.stylefeng.guns.modular.system.model;

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
 * 支付宝营销红包活动表
 * </p>
 *
 * @author kosans
 * @since 2018-07-31
 */
@TableName("biz_alipay_activity")
public class AlipayActivity extends Model<AlipayActivity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 活动号
     */
    @TableField("crowd_no")
    private String crowdNo;
    /**
     * 支付链接
     */
    @TableField("pay_url")
    private String payUrl;
    /**
     * 原始活动号
     */
    @TableField("origin_crowd_no")
    private String originCrowdNo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 活动有效开始时间
     */
    @TableField("startTime")
    private Date startTime;
    /**
     * 活动有效结束时间
     */
    @TableField("endTime")
    private Date endTime;
    /**
     * 活动总金额
     */
    @TableField(exist = false)
    private BigDecimal totalAmount;
    /**
     * 活动已发放金额
     */
    @TableField(exist = false)
    private BigDecimal sendAmount;
    /**
     * 红包总个数
     */
    @TableField(exist = false)
    private BigDecimal totalCount;
    /**
     * 活动状态
     * CREATED: 已创建未打款
     * PAID:已打款
     * READY:活动已开始
     * PAUSE:活动已暂停
     * CLOSED:活动已结束
     * SETTLE:活动已清算
     */
    @TableField(exist = false)
    private String campStatus;

    /**
     * 活动名称
     */
    @TableField(exist = false)
    private String couponName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCrowdNo() {
        return crowdNo;
    }

    public void setCrowdNo(String crowdNo) {
        this.crowdNo = crowdNo;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getOriginCrowdNo() {
        return originCrowdNo;
    }

    public void setOriginCrowdNo(String originCrowdNo) {
        this.originCrowdNo = originCrowdNo;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }

    public String getCampStatus() {
        return campStatus;
    }

    public void setCampStatus(String campStatus) {
        this.campStatus = campStatus;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @Override
    public String toString() {
        return "AlipayActivity{" +
                "id=" + id +
                ", crowdNo='" + crowdNo + '\'' +
                ", payUrl='" + payUrl + '\'' +
                ", originCrowdNo='" + originCrowdNo + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalAmount=" + totalAmount +
                ", sendAmount=" + sendAmount +
                ", totalCount=" + totalCount +
                ", campStatus='" + campStatus + '\'' +
                ", couponName='" + couponName + '\'' +
                '}';
    }
}
