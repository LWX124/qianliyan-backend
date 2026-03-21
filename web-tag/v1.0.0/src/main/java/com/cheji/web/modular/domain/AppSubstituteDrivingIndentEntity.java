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
 * 代驾订单表
 * </p>
 *
 * @author Ashes
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_substitute_driving_indent")
public class AppSubstituteDrivingIndentEntity extends Model<AppSubstituteDrivingIndentEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 代驾订单编号
     */
    @TableField("substitute_driving_number")
    private String substituteDrivingNumber;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    /**
     * 代驾类型（1.送车代驾，2.日常代驾 ，3.包时代驾）
     */
    private Integer type;

    /**
     * 起点地址
     */
    @TableField("start_point")
    private String startPoint;


    @TableField("start_address")
    private String startAddress;

    /**
     * 起点经度
     */
    @TableField("start_lng")
    private BigDecimal startLng;

    /**
     * 起点纬度
     */
    @TableField("start_lat")
    private BigDecimal startLat;

    /**
     * 送车人
     */
    @TableField("start_name")
    private String startName;

    /**
     * 送车人电话
     */
    @TableField("start_phone")
    private String startPhone;

    /**
     * 送车备注
     */
    @TableField("start_remark")
    private String startRemark;

    /**
     * 终点地址
     */
    @TableField("end_point")
    private String endPoint;


    @TableField("end_address")
    private String endAddress;

    /**
     * 终点经度
     */
    @TableField("end_lng")
    private BigDecimal endLng;

    /**
     * 终点纬度
     */
    @TableField("end_lat")
    private BigDecimal endLat;

    /**
     * 接车名字
     */
    @TableField("end_name")
    private String endName;

    /**
     * 接车人电话
     */
    @TableField("end_phone")
    private String endPhone;

    /**
     * 接车备注
     */
    @TableField("end_remark")
    private String endRemark;



    private BigDecimal distance;

    /**
     * 预估价格
     */
    @TableField("estimate_price")
    private BigDecimal estimatePrice;

    /**
     * 实际价格
     */
    @TableField("actual_price")
    private BigDecimal actualPrice;

    /**
     * 里程价格
     */
    @TableField("mileage_price")
    private BigDecimal mileagePrice;

    /**
     * 过路费
     */
    @TableField("tolls_price")
    private BigDecimal tollsPrice;

    /**
     * 时长费
     */
    @TableField("time_price")
    private BigDecimal timePrice;

    /**
     * 取消来源
     */
    @TableField("cancel_resource")
    private String cancelResource;

    /**
     * 取消原因
     */
    @TableField("cancel_reason")
    private String cancelReason;

    /**
     * 订单状态 1.开始，2.进行中，3完成, 4.取消
     */
    @TableField("indent_state")
    private Integer indentState;

    /**
     * 包时代驾套餐类型
     */
    @TableField("package_type")
    private Integer packageType;

    /**
     * 开始时间
     */
    @TableField("begin_time")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 等待时长
     */
    @TableField("wait_time")
    private Integer waitTime;

    /**
     * sid为终端所属service唯一编号
     */
    private String sid;

    /**
     * tid为终端唯一编号
     */
    private String tid;

    /**
     * trid为轨迹唯一编号
     */
    private String trid;

    /**
     * 支付状态 0初始状态  1已支付   2支付失败  3退款
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 支付订单编号
     */
    @TableField("pay_number")
    private String payNumber;

    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @TableField("pick_car_time")
    private String pickCarTime;

    @TableField("send_time")
    private String sendTime;



    @TableField(exist = false)
    private String time;


    @TableField(exist = false)
    private String overtime;


    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getPickCarTime() {

        return pickCarTime;
    }

    public void setPickCarTime(String pickCarTime) {
        this.pickCarTime = pickCarTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
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

    public String getSubstituteDrivingNumber() {
        return substituteDrivingNumber;
    }

    public void setSubstituteDrivingNumber(String substituteDrivingNumber) {
        this.substituteDrivingNumber = substituteDrivingNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public BigDecimal getStartLng() {
        return startLng;
    }

    public void setStartLng(BigDecimal startLng) {
        this.startLng = startLng;
    }

    public BigDecimal getStartLat() {
        return startLat;
    }

    public void setStartLat(BigDecimal startLat) {
        this.startLat = startLat;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getStartPhone() {
        return startPhone;
    }

    public void setStartPhone(String startPhone) {
        this.startPhone = startPhone;
    }

    public String getStartRemark() {
        return startRemark;
    }

    public void setStartRemark(String startRemark) {
        this.startRemark = startRemark;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public BigDecimal getEndLng() {
        return endLng;
    }

    public void setEndLng(BigDecimal endLng) {
        this.endLng = endLng;
    }

    public BigDecimal getEndLat() {
        return endLat;
    }

    public void setEndLat(BigDecimal endLat) {
        this.endLat = endLat;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getEndPhone() {
        return endPhone;
    }

    public void setEndPhone(String endPhone) {
        this.endPhone = endPhone;
    }

    public String getEndRemark() {
        return endRemark;
    }

    public void setEndRemark(String endRemark) {
        this.endRemark = endRemark;
    }

    public BigDecimal getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(BigDecimal estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getMileagePrice() {
        return mileagePrice;
    }

    public void setMileagePrice(BigDecimal mileagePrice) {
        this.mileagePrice = mileagePrice;
    }

    public BigDecimal getTollsPrice() {
        return tollsPrice;
    }

    public void setTollsPrice(BigDecimal tollsPrice) {
        this.tollsPrice = tollsPrice;
    }

    public BigDecimal getTimePrice() {
        return timePrice;
    }

    public void setTimePrice(BigDecimal timePrice) {
        this.timePrice = timePrice;
    }

    public String getCancelResource() {
        return cancelResource;
    }

    public void setCancelResource(String cancelResource) {
        this.cancelResource = cancelResource;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getIndentState() {
        return indentState;
    }

    public void setIndentState(Integer indentState) {
        this.indentState = indentState;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
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

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}
