package com.cheji.b.modular.domain;

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
 * 用户扣费记录表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
@TableName("app_push_bill")
public class PushBillEntity extends Model<PushBillEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 事故id
     */
    private Integer accid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 推送扣费
     */
    private BigDecimal deduction;

    private Integer source;

    @TableField("track_state")
    private Integer trackState;

    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    @TableField("check_time")
    private Date checkTime;

    @TableField("check_address")
    private String  checkAddress;

    @TableField("check_lng")
    private BigDecimal  checkLng;

    @TableField("check_lat")
    private BigDecimal  checkLat;

    @TableField("mess_pay_number")
    private String messPayPumber;  //信息支付编号

    @TableField("pay_state")
    private Integer payState;       //支付状态  0初始状态  1已支付   2支付失败

    @TableField("pb_number")
    private String pbNumber;            //app小程序推送编号

    @TableField("check_state")
    private Integer checkState;

    private Integer state;

    @TableField("accident_type")
    private Integer accidentType;

    @TableField("manager_remark")
    private String managerRemark;

    @TableField(exist = false)
    private String address;             // 地址
    @TableField(exist = false)
    private String thumbnailUrl;        //缩略图

    @TableField(exist = false)
    private String loge;                //标识
    @TableField(exist = false)
    private BigDecimal payAmount;       //支出
    @TableField(exist = false)
    private String time;

    @TableField(exist = false)
    private String type;            //类型

    @TableField(exist = false)
    private String videoUrl;        //视频地址

    @TableField("message_car_id")
    private String messageCarId;

    private BigDecimal reward;

    private String voice;

    private String remark;      //审核备注

    @TableField(exist = false)
    private BigDecimal lng;

    @TableField(exist = false)
    private BigDecimal lat;


    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLoge() {
        return loge;
    }

    public void setLoge(String loge) {
        this.loge = loge;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getTrackState() {
        return trackState;
    }

    public void setTrackState(Integer trackState) {
        this.trackState = trackState;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
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

    public String getMessPayPumber() {
        return messPayPumber;
    }

    public void setMessPayPumber(String messPayPumber) {
        this.messPayPumber = messPayPumber;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getPbNumber() {
        return pbNumber;
    }

    public void setPbNumber(String pbNumber) {
        this.pbNumber = pbNumber;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(Integer accidentType) {
        this.accidentType = accidentType;
    }

    public String getManagerRemark() {
        return managerRemark;
    }

    public void setManagerRemark(String managerRemark) {
        this.managerRemark = managerRemark;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMessageCarId() {
        return messageCarId;
    }

    public void setMessageCarId(String messageCarId) {
        this.messageCarId = messageCarId;
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

    @Override
    public String toString() {
        return "PushBillEntity{" +
                "id=" + id +
                ", accid=" + accid +
                ", userId=" + userId +
                ", deduction=" + deduction +
                ", source=" + source +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", address='" + address + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", loge='" + loge + '\'' +
                ", payAmount=" + payAmount +
                ", time='" + time + '\'' +
                '}';
    }
}
