package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cheji.b.modular.dto.PriceDetailsDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_beauty_price_detail")
public class AppBeautyPriceDetailEntity extends Model<AppBeautyPriceDetailEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 美容类型
     */
    @TableField("beauty_type")
    private Integer beautyType;

    /**
     * 车辆类型
     */
    @TableField("car_type")
    private Integer carType;

    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 优惠价
     */
    @TableField("preferential_price")
    private BigDecimal preferentialPrice;

    /**
     * 到手价
     */
    @TableField("thrie_price")
    private BigDecimal thriePrice;

    /**
     * 服务解释
     */
    private String note;

    /**
     * 审核是否通过
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private List<PriceDetailsDto> data;

    @TableField(exist = false)
    private String businessType;         //营业状态

    @TableField(exist = false)
    private String startTime;           //开始时间

    @TableField(exist = false)
    private String endTime;             //结束时间

    @TableField(exist = false)
    private String openWashTime;        //是否开通营业时间

    @TableField(exist = false)
    private BigDecimal rabates;             //比例


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOpenWashTime() {
        return openWashTime;
    }

    public void setOpenWashTime(String openWashTime) {
        this.openWashTime = openWashTime;
    }

    public BigDecimal getRabates() {
        return rabates;
    }

    public void setRabates(BigDecimal rabates) {
        this.rabates = rabates;
    }

    public List<PriceDetailsDto> getData() {
        return data;
    }

    public void setData(List<PriceDetailsDto> data) {
        this.data = data;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
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

    public Integer getBeautyType() {
        return beautyType;
    }

    public void setBeautyType(Integer beautyType) {
        this.beautyType = beautyType;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public BigDecimal getThriePrice() {
        return thriePrice;
    }

    public void setThriePrice(BigDecimal thriePrice) {
        this.thriePrice = thriePrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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


    @Override
    public String toString() {
        return "AppBeautyPriceDetailEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", beautyType=" + beautyType +
                ", carType=" + carType +
                ", originalPrice=" + originalPrice +
                ", preferentialPrice=" + preferentialPrice +
                ", thriePrice=" + thriePrice +
                ", note='" + note + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", data=" + data +
                ", businessType='" + businessType + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", openWashTime='" + openWashTime + '\'' +
                ", rabates=" + rabates +
                '}';
    }
}
