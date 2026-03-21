package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 洗车订单表
 * </p>
 *
 * @author Ashes
 * @since 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_clean_indet")
public class AppCleanIndetEntity extends Model<AppCleanIndetEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 洗车订单编号
     */
    @TableField("clean_indent_number")
    private String cleanIndentNumber;

    /**
     * b端商户id
     */
    @TableField("user_b_id")
    private String userBId;

    /**
     * 用户id
     */
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

    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 支付金额，也就是优惠价格
     */
    private BigDecimal amount;

    /**
     * 支付订单编号
     */
    @TableField("merchants_pay_number")
    private String merchantsPayNumber;

    /**
     * 订单状态，1.开始，2.进行中，3完成, 4.取消
     */
    @TableField("indent_state")
    private String indentState;

    /**
     * 支付状态  0初始状态  1已支付   2支付失败  3退款
     */
    @TableField("pay_state")
    private String payState;

    /**
     * 是否评价.
     */
    @TableField("is_evaluation")
    private Integer isEvaluation;

    /**
     * 来源 1,洗车订单，2.美容订单
     */
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

    /**
     * 合约标识 1：是  2：否，  如果是合约订单，退款之后需要把次数加上
     */
    @TableField("contract_flag")
    private Integer contractFlag;

    /**
     * 商品编号
     */
    @TableField("bussiness_id")
    private String bussinessId;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String phoneTail;

    @TableField(exist = false)
    private String carName;

    @TableField(exist = false)
    private String cleanName;

    @TableField(exist = false)
    private String time;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private String imgUrl;

    @TableField(exist = false)
    private String rescueName;

    @TableField(exist = false)
    private String currentPosition;


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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneTail() {
        return phoneTail;
    }

    public void setPhoneTail(String phoneTail) {
        this.phoneTail = phoneTail;
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

    public String getCleanIndentNumber() {
        return cleanIndentNumber;
    }

    public void setCleanIndentNumber(String cleanIndentNumber) {
        this.cleanIndentNumber = cleanIndentNumber;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchantsPayNumber() {
        return merchantsPayNumber;
    }

    public void setMerchantsPayNumber(String merchantsPayNumber) {
        this.merchantsPayNumber = merchantsPayNumber;
    }

    public String getIndentState() {
        return indentState;
    }

    public void setIndentState(String indentState) {
        this.indentState = indentState;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public Integer getIsEvaluation() {
        return isEvaluation;
    }

    public void setIsEvaluation(Integer isEvaluation) {
        this.isEvaluation = isEvaluation;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    public Integer getContractFlag() {
        return contractFlag;
    }

    public void setContractFlag(Integer contractFlag) {
        this.contractFlag = contractFlag;
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }

    @Override
    public String toString() {
        return "AppCleanIndetEntity{" +
                "id=" + id +
                ", cleanIndentNumber='" + cleanIndentNumber + '\'' +
                ", userBId='" + userBId + '\'' +
                ", userId='" + userId + '\'' +
                ", carType=" + carType +
                ", cleanType=" + cleanType +
                ", originalPrice=" + originalPrice +
                ", amount=" + amount +
                ", merchantsPayNumber='" + merchantsPayNumber + '\'' +
                ", indentState='" + indentState + '\'' +
                ", payState='" + payState + '\'' +
                ", isEvaluation=" + isEvaluation +
                ", resource='" + resource + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", contractFlag=" + contractFlag +
                ", bussinessId='" + bussinessId + '\'' +
                ", username='" + username + '\'' +
                ", phoneTail='" + phoneTail + '\'' +
                ", carName='" + carName + '\'' +
                ", cleanName='" + cleanName + '\'' +
                ", time='" + time + '\'' +
                ", endTime='" + endTime + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rescueName='" + rescueName + '\'' +
                ", currentPosition='" + currentPosition + '\'' +
                '}';
    }
}
