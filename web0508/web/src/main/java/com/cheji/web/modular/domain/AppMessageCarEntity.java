package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_message_car")
public class AppMessageCarEntity extends Model<AppMessageCarEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 车牌
     */
    @TableField("license_plate")
    private String licensePlate;

    /**
     * 客户姓名
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 客户电话
     */
    private String phone;

    /**
     * 我的保险
     */
    @TableField("local_insurance")
    private String localInsurance;

    /**
     * 他车保险
     */
    @TableField("other_insurance")
    private String otherInsurance;

    /**
     * 施救地址
     */
    @TableField("help_address")
    private String helpAddress;

    /**
     * 施救经度
     */
    @TableField("help_lng")
    private BigDecimal helpLng;

    /**
     * 施救维度
     */
    @TableField("help_lat")
    private BigDecimal helpLat;

    /**
     * 事故责任
     */
    @TableField("accident_responsibility")
    private String accidentResponsibility;

    /**
     * 维修方式 1.保险，2。自费，3.酒驾
     */
    @TableField("maintenance_mode")
    private Integer maintenanceMode;

    /**
     * 车辆损失 1.一般，2.严重，3.全损
     */
    @TableField("vehicle_loss")
    private Integer vehicleLoss;

    /**
     * 客户意向 ，1.维修，2.卖车，3.报废
     */
    @TableField("customer_intention")
    private Integer customerIntention;

    /**
     * 留言
     */
    @TableField("leave_message")
    private String leaveMessage;

    /**
     * 车辆结果
     */
    @TableField("vehicle_results")
    private Integer vehicleResults;


    @TableField("user_b_id")
    private String userBId;

    private String voice;

    @TableField("brand_id")
    private String brandId;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("mess_id")
    private String messId;

    private Integer type;

    @TableField("main_phone")
    private String  mainPhone;

    @TableField("main_name")
    private String mainName;

    @TableField("main_insurance")
    private String mainInsurance;


    @TableField("financial_loss")
    private String financialLoss;   //财务损失

    @TableField("buy_car")
    private String buyCar;   //客户购车


    @TableField("usually_maintain")
    private String usuallyMaintain;   //常用维修点


    @TableField("channels_ins")
    private String channelsIns;   //保险渠道


    @TableField("casualties")
    private String casualties;   //人员伤亡



    @TableField("acc_conditions")
    private String accConditions;   //事故情况


    @TableField("save_costs")
    private String saveCosts;       //施救费用

    @TableField("fix_intention")
    private String fixIntention;       //维修意向

    private BigDecimal amount;

    @TableField("indent_id")
    private String indentId;

    private Integer source;

    private Integer suit;


    //维修图片
    @TableField(exist = false)
    private List<String> credentialsImgList;


    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSuit() {
        return suit;
    }

    public void setSuit(Integer suit) {
        this.suit = suit;
    }

    public String getIndentId() {
        return indentId;
    }

    public void setIndentId(String indentId) {
        this.indentId = indentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<String> getCredentialsImgList() {
        return credentialsImgList;
    }

    public void setCredentialsImgList(List<String> credentialsImgList) {
        this.credentialsImgList = credentialsImgList;
    }

    public String getSaveCosts() {
        return saveCosts;
    }

    public void setSaveCosts(String saveCosts) {
        this.saveCosts = saveCosts;
    }

    public String getFixIntention() {
        return fixIntention;
    }

    public void setFixIntention(String fixIntention) {
        this.fixIntention = fixIntention;
    }

    public String getFinancialLoss() {
        return financialLoss;
    }

    public void setFinancialLoss(String financialLoss) {
        this.financialLoss = financialLoss;
    }

    public String getBuyCar() {
        return buyCar;
    }

    public void setBuyCar(String buyCar) {
        this.buyCar = buyCar;
    }

    public String getUsuallyMaintain() {
        return usuallyMaintain;
    }

    public void setUsuallyMaintain(String usuallyMaintain) {
        this.usuallyMaintain = usuallyMaintain;
    }

    public String getChannelsIns() {
        return channelsIns;
    }

    public void setChannelsIns(String channelsIns) {
        this.channelsIns = channelsIns;
    }

    public String getCasualties() {
        return casualties;
    }

    public void setCasualties(String casualties) {
        this.casualties = casualties;
    }

    public String getAccConditions() {
        return accConditions;
    }

    public void setAccConditions(String accConditions) {
        this.accConditions = accConditions;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getMainInsurance() {
        return mainInsurance;
    }

    public void setMainInsurance(String mainInsurance) {
        this.mainInsurance = mainInsurance;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String  getUserBId() {
        return userBId;
    }

    public void setUserBId(String userBId) {
        this.userBId = userBId;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocalInsurance() {
        return localInsurance;
    }

    public void setLocalInsurance(String localInsurance) {
        this.localInsurance = localInsurance;
    }

    public String getOtherInsurance() {
        return otherInsurance;
    }

    public void setOtherInsurance(String otherInsurance) {
        this.otherInsurance = otherInsurance;
    }

    public String getHelpAddress() {
        return helpAddress;
    }

    public void setHelpAddress(String helpAddress) {
        this.helpAddress = helpAddress;
    }

    public BigDecimal getHelpLng() {
        return helpLng;
    }

    public void setHelpLng(BigDecimal helpLng) {
        this.helpLng = helpLng;
    }

    public BigDecimal getHelpLat() {
        return helpLat;
    }

    public void setHelpLat(BigDecimal helpLat) {
        this.helpLat = helpLat;
    }

    public String getAccidentResponsibility() {
        return accidentResponsibility;
    }

    public void setAccidentResponsibility(String accidentResponsibility) {
        this.accidentResponsibility = accidentResponsibility;
    }

    public Integer getMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(Integer maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Integer getVehicleLoss() {
        return vehicleLoss;
    }

    public void setVehicleLoss(Integer vehicleLoss) {
        this.vehicleLoss = vehicleLoss;
    }

    public Integer getCustomerIntention() {
        return customerIntention;
    }

    public void setCustomerIntention(Integer customerIntention) {
        this.customerIntention = customerIntention;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public Integer getVehicleResults() {
        return vehicleResults;
    }

    public void setVehicleResults(Integer vehicleResults) {
        this.vehicleResults = vehicleResults;
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
}
