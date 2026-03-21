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
 * 商户清洗价格明细表
 * </p>
 *
 * @author Ashes
 * @since 2019-12-12
 */
@TableName("app_clean_price_detail")
public class CleanPriceDetailEntity extends Model<CleanPriceDetailEntity> {

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
    private Integer userBId;
    /**
     * 清洗类型5种
     */
    @TableField("clean_type")
    private Integer cleanType;
    /**
     * 车型8种
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

    /**
     * 是否合约项目
     */
    @TableField("contract_project")
    private Integer contractProject;

    /**
     * 剩余次数
     */
    @TableField("residue_degree")
    private Integer residueDegree;

    @TableField(exist = false)
    private String carName;

    @TableField(exist = false)
    private String cleanName;

    @TableField(exist = false)
    private String imgUrl;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getContractProject() {
        return contractProject;
    }

    public void setContractProject(Integer contractProject) {
        this.contractProject = contractProject;
    }

    public Integer getResidueDegree() {
        return residueDegree;
    }

    public void setResidueDegree(Integer residueDegree) {
        this.residueDegree = residueDegree;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCleanName() {
        return cleanName;
    }

    public void setCleanName(String cleanName) {
        this.cleanName = cleanName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CleanPriceDetailEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", cleanType=" + cleanType +
                ", carType=" + carType +
                ", originalPrice=" + originalPrice +
                ", preferentialPrice=" + preferentialPrice +
                ", thriePrice=" + thriePrice +
                ", note='" + note + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", contractProject=" + contractProject +
                ", residueDegree=" + residueDegree +
                ", carName='" + carName + '\'' +
                ", cleanName='" + cleanName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
