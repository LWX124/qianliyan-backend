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

/**
 * <p>
 * web派单记录表
 * </p>
 *
 * @author Ashes
 * @since 2021-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_send_out_sheet")
public class AppSendOutSheetEntity extends Model<AppSendOutSheetEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 地址
     */
    private String adress;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 轨迹的开始时间
     */
    @TableField("tc_time")
    private Date tcTime;


    @TableField("check_address")
    private String  checkAddress;
    /**
     * 签到时间
     */
    @TableField("check_time")
    private Date checkTime;

    /**
     * 签到经度
     */
    @TableField("check_lng")
    private BigDecimal checkLng;

    /**
     * 备注
     */
    private String remark;

    /**
     * 签到纬度
     */
    @TableField("check_lat")
    private BigDecimal checkLat;

    /**
     * 反馈文字
     */
    @TableField("back_text")
    private String backText;

    /**
     * 反馈音频
     */
    @TableField("back_voice")
    private String backVoice;

    /**
     * 猎鹰轨迹id
     */
    @TableField("falcon_trajectory")
    private String falconTrajectory;

    /**
     * 设备编号
     */
    private String tid;

    /**
     * 现场状态
     */
    private Integer nowstate;

    /**
     * 状态 1，刚派单  2.在路上 3.已签到 4.已处理
     */
    private Integer state;

    /**
     * 信息费用
     */
    @TableField("infromation_costs")
    private BigDecimal infromationCosts;

    /**
     * 信息渠道
     */
    @TableField("access_information")
    private String accessInformation;

    /**
     * 是否成交(1,是，0.否)
     */
    @TableField("clinch_deal")
    private Integer clinchDeal;

    /**
     * 音频
     */
    private String voice;


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

    @TableField("track_state")
    private Integer trackState;

    @TableField("message_car_id")
    private String messageCarId;


    @TableField("user_id")
    private Integer userId;

    @TableField("plate_number")
    private String plateNumber;

    private String name;

    private String phone;

    private String company;

    @TableField("pay_amount")
    private BigDecimal payAmount;

    @TableField("mess_pay_number")
    private String messPayNumber;

    @TableField("pay_state")
    private Integer payState;

    @TableField("sos_number")
    private String sosNumber;

    @TableField("check_state")
    private Integer checkState;

    @TableField("accident_type")
    private Integer accidentType;


    public Integer getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(Integer accidentType) {
        this.accidentType = accidentType;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getMessPayNumber() {
        return messPayNumber;
    }

    public void setMessPayNumber(String messPayNumber) {
        this.messPayNumber = messPayNumber;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getSosNumber() {
        return sosNumber;
    }

    public void setSosNumber(String sosNumber) {
        this.sosNumber = sosNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMessageCarId() {
        return messageCarId;
    }

    public void setMessageCarId(String messageCarId) {
        this.messageCarId = messageCarId;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Date getTcTime() {
        return tcTime;
    }

    public void setTcTime(Date tcTime) {
        this.tcTime = tcTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public BigDecimal getCheckLng() {
        return checkLng;
    }

    public void setCheckLng(BigDecimal checkLng) {
        this.checkLng = checkLng;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getCheckLat() {
        return checkLat;
    }

    public void setCheckLat(BigDecimal checkLat) {
        this.checkLat = checkLat;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public String getBackVoice() {
        return backVoice;
    }

    public void setBackVoice(String backVoice) {
        this.backVoice = backVoice;
    }

    public String getFalconTrajectory() {
        return falconTrajectory;
    }

    public void setFalconTrajectory(String falconTrajectory) {
        this.falconTrajectory = falconTrajectory;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Integer getNowstate() {
        return nowstate;
    }

    public void setNowstate(Integer nowstate) {
        this.nowstate = nowstate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getInfromationCosts() {
        return infromationCosts;
    }

    public void setInfromationCosts(BigDecimal infromationCosts) {
        this.infromationCosts = infromationCosts;
    }

    public String getAccessInformation() {
        return accessInformation;
    }

    public void setAccessInformation(String accessInformation) {
        this.accessInformation = accessInformation;
    }

    public Integer getClinchDeal() {
        return clinchDeal;
    }

    public void setClinchDeal(Integer clinchDeal) {
        this.clinchDeal = clinchDeal;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
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

    public Integer getTrackState() {
        return trackState;
    }

    public void setTrackState(Integer trackState) {
        this.trackState = trackState;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
