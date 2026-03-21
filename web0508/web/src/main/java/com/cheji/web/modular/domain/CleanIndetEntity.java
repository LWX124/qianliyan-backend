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
 * 洗车订单表
 * </p>
 *
 * @author Ashes
 * @since 2019-12-13
 */
@TableName("app_clean_indet")
public class CleanIndetEntity extends Model<CleanIndetEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 洗车订单编号
     */
    @TableField("clean_indent_number")
    private String cleanIndentNumber;

    @TableField("user_b_id")
    private String userBId;


    @TableField("user_id")
    private String userId;


    /**
     * 车型
     */
    @TableField("car_type")
    private Integer carType;
    /**
     * 清洗类型
     */
    @TableField("clean_type")
    private Integer cleanType;

    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 支付状态
     */
    @TableField("pay_state")
    private String payState;


    private String resource;
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
    private String time;

    @TableField("merchants_pay_number")
    private String merchantsPayNumber;

    @TableField("indent_state")
    private String indentState;

    @TableField("bussiness_id")//商品id
    private String bussinessId;

    @TableField("contract_flag")//合约标识 1：是  2：否，  如果是合约订单，退款之后需要把次数加上
    private Integer contractFlag;

    @TableField(exist = false)
    private String carName;

    @TableField(exist = false)
    private String cleanName;

    @TableField(exist = false)
    private String imgUrl;

    @TableField("is_evaluation")
    private String isEvaluation;        //是否评价

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private String rescueName;

    @TableField(exist = false)
    private String currentPosition;

    @TableField(exist = false)
    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRescueName() {
        return rescueName;
    }

    public void setRescueName(String rescueName) {
        this.rescueName = rescueName;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsEvaluation() {
        return isEvaluation;
    }

    public void setIsEvaluation(String isEvaluation) {
        this.isEvaluation = isEvaluation;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }

    public Integer getContractFlag() {
        return contractFlag;
    }

    public void setContractFlag(Integer contractFlag) {
        this.contractFlag = contractFlag;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getIndentState() {
        return indentState;
    }

    public void setIndentState(String indentState) {
        this.indentState = indentState;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getMerchantsPayNumber() {
        return merchantsPayNumber;
    }

    public void setMerchantsPayNumber(String merchantsPayNumber) {
        this.merchantsPayNumber = merchantsPayNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCleanIndentNumber() {
        return cleanIndentNumber;
    }

    public void setCleanIndentNumber(String cleanIndentNumber) {
        this.cleanIndentNumber = cleanIndentNumber;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
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
        return "CleanIndetEntity{" +
                "id=" + id +
                ", cleanIndentNumber='" + cleanIndentNumber + '\'' +
                ", userBId='" + userBId + '\'' +
                ", userId='" + userId + '\'' +
                ", carType=" + carType +
                ", cleanType=" + cleanType +
                ", originalPrice=" + originalPrice +
                ", amount=" + amount +
                ", payState='" + payState + '\'' +
                ", resource='" + resource + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", time='" + time + '\'' +
                ", merchantsPayNumber='" + merchantsPayNumber + '\'' +
                ", indentState='" + indentState + '\'' +
                ", bussinessId='" + bussinessId + '\'' +
                ", contractFlag=" + contractFlag +
                ", carName='" + carName + '\'' +
                ", cleanName='" + cleanName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", isEvaluation='" + isEvaluation + '\'' +
                ", endTime='" + endTime + '\'' +
                ", rescueName='" + rescueName + '\'' +
                ", currentPosition='" + currentPosition + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
