package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
@TableName("app_indent")
public class IndentEntity extends Model<IndentEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 填写订单得用户名
     */
    private String username;
    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 维修方案(1.保险理赔2.钣金喷漆3.机电维修4.常规保养）
     */
    private String plan;
    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;
    /**
     * 佣金比例
     */
    @TableField("commission_rate")
    private BigDecimal commissionRate;
    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Integer brandId;
    /**
     * 备注(100字)
     */
    private String remake;
    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;
    /**
     * 结算方式
     */
    @TableField("means_payments")
    private String meansPayments;
    /**
     * 状态(1,新订单,2.未到店,3.服务中,4.已交车,5.已结算）
     */
    private Integer state;
    /**
     * 结算金额
     */
    @TableField("settle_accounts")
    private BigDecimal settleAccounts;

    @TableField("send_people")
    private String sendPeople;
    /**
     * 结算到用户金额
     */
    @TableField("fixloss_user")
    private BigDecimal fixlossUser;
    /**
     * 定损金额
     */
    private BigDecimal fixloss;
    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;

    @TableField("rescue_them_fee")       //施救费
    private BigDecimal rescueThemFee;

    private String responsibility;      //责任划分

    @TableField("insurance_company")
    private String insuranceCompany;    //保险公司

    @TableField("send_unit")
    private String sendUnit;

    @TableField("message_source")
    private Integer messageSource;

    @TableField("deal_time")
    private Integer dealTime;

    @TableField("up_id")
    private String upId;

    @TableField(exist = false)
    private String time;

    @TableField(exist = false)
    private String brandName;

    @TableField(exist = false)
    private String[] imgEntityList;

    @TableField(exist = false)
    private List<ImgEntity> confirmImg;


    public BigDecimal getRescueThemFee() {
        return rescueThemFee;
    }

    public void setRescueThemFee(BigDecimal rescueThemFee) {
        this.rescueThemFee = rescueThemFee;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public Integer getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(Integer messageSource) {
        this.messageSource = messageSource;
    }

    public Integer getDealTime() {
        return dealTime;
    }

    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String[] getImgEntityList() {
        return imgEntityList;
    }

    public void setImgEntityList(String[] imgEntityList) {
        this.imgEntityList = imgEntityList;
    }

    public List<ImgEntity> getConfirmImg() {
        return confirmImg;
    }

    public void setConfirmImg(List<ImgEntity> confirmImg) {
        this.confirmImg = confirmImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMeansPayments() {
        return meansPayments;
    }

    public void setMeansPayments(String meansPayments) {
        this.meansPayments = meansPayments;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public BigDecimal getFixlossUser() {
        return fixlossUser;
    }

    public void setFixlossUser(BigDecimal fixlossUser) {
        this.fixlossUser = fixlossUser;
    }

    public BigDecimal getFixloss() {
        return fixloss;
    }

    public void setFixloss(BigDecimal fixloss) {
        this.fixloss = fixloss;
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
        return "IndentEntity{" +
                "id=" + id +
                ", userBId=" + userBId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", plan='" + plan + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", commissionRate=" + commissionRate +
                ", brandId=" + brandId +
                ", remake='" + remake + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", meansPayments='" + meansPayments + '\'' +
                ", state=" + state +
                ", settleAccounts=" + settleAccounts +
                ", sendPeople='" + sendPeople + '\'' +
                ", fixlossUser=" + fixlossUser +
                ", fixloss=" + fixloss +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
