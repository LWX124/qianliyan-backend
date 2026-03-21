package com.cheji.b.modular.domain;

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
 * web派单记录表
 * </p>
 *
 * @author Ashes
 * @since 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_send_out_sheet")
public class AppSendOutSheetEntity extends Model<AppSendOutSheetEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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


    @TableField("tc_time")
    private Date tcTime;

    /**
     * 签到时间
     */
    @TableField("check_time")
    private Date checkTime;

    @TableField("check_address")
    private String  checkAddress;

    @TableField("message_car_id")
    private String messageCarId;

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

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @TableField("check_state")
    private Integer checkState;

    @TableField("check_lng")
    private BigDecimal checkLng;

    @TableField("check_lat")
    private BigDecimal checkLat;


    @TableField("back_text")
    private String  backText;


    @TableField("falcon_trajectory")
    private String falconTrajectory;

    private String tid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 反馈音频
     */
    @TableField("back_voice")
    private String backVoice;

    /**
     * 音频
     */
    private String voice;

    private Integer nowstate;

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
    private String time;

    @TableField(exist = false)
    private String loge;

    @TableField(exist = false)
    private List<String> url;

    @TableField(exist = false)
    private String src;

    @TableField(exist = false)
    private List<String> imgList;

    @TableField("pay_amount")
    private BigDecimal payAmount;

    @TableField("pay_state")
    private Integer payState;

    @TableField("accident_type")
    private Integer accidentType;

    public Integer getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(Integer accidentType) {
        this.accidentType = accidentType;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getTcTime() {
        return tcTime;
    }

    public void setTcTime(Date tcTime) {
        this.tcTime = tcTime;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    public BigDecimal getCheckLng() {
        return checkLng;
    }

    public void setCheckLng(BigDecimal checkLng) {
        this.checkLng = checkLng;
    }

    public BigDecimal getCheckLat() {
        return checkLat;
    }

    public void setCheckLat(BigDecimal checkLat) {
        this.checkLat = checkLat;
    }

    public String getFalconTrajectory() {
        return falconTrajectory;
    }

    public void setFalconTrajectory(String falconTrajectory) {
        this.falconTrajectory = falconTrajectory;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public String getLoge() {
        return loge;
    }

    public void setLoge(String loge) {
        this.loge = loge;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBackVoice() {
        return backVoice;
    }

    public void setBackVoice(String backVoice) {
        this.backVoice = backVoice;
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


}
