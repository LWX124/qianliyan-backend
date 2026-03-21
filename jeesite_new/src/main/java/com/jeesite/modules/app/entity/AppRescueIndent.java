/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 救援表Entity
 *
 * @author dh
 * @version 2020-03-03
 */
@Table(name = "app_rescue_indent", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "rescue_number", attrName = "rescueNumber", label = "救援编号"),
        @Column(name = "user_id", attrName = "userId", label = "用户id"),
        @Column(name = "user_b_id", attrName = "userBId", label = "商户id"),
        @Column(name = "current_position", attrName = "currentPosition", label = "救援位置"),
        @Column(name = "merchants_position", attrName = "merchantsPosition", label = "商户位置"),
        @Column(name = "license_plate", attrName = "licensePlate", label = "车牌号"),
        @Column(name = "rescue_name", attrName = "rescueName", label = "救援联系人", queryType = QueryType.LIKE),
        @Column(name = "phone_number", attrName = "phoneNumber", label = "手机号码"),
        @Column(name = "emergency_number", attrName = "emergencyNumber", label = "紧急号码"),
        @Column(name = "distance", attrName = "distance", label = "距离"),
        @Column(name = "type", attrName = "type", label = "救援类型", comment = "救援类型(1.搭电)"),
        @Column(name = "lat", attrName = "lat", label = "纬度"),
        @Column(name = "lng", attrName = "lng", label = "经度"),
        @Column(name = "rescue_scene", attrName = "rescueScene", label = "救援场景", comment = "救援场景（2拖车：1事故，2故障，3地面，4地库，5困境，6其他，3换胎：1有备胎，2无备胎，3送备胎，4其他）"),
        @Column(name = "price", attrName = "price", label = "支付价格"),
        @Column(name = "state", attrName = "state", label = "订单状态 1.开始，2.进行中，3完成, 4.取消"),
        @Column(name = "pay_state", attrName = "payState", label = "支付状态 0初始状态  1已支付   2支付失败  3退款"),
        @Column(name = "merchants_pay_number", attrName = "merchantsPayNumber", label = "支付订单编号"),
        @Column(name = "remark", attrName = "remark", label = "备注"),
        @Column(name = "create_time", attrName = "createTime", label = "create_time"),
        @Column(name = "update_time", attrName = "updateTime", label = "update_time"),
        @Column(name = "back_state", attrName = "backState", label = "1 初始状态  2", comment = "1 初始状态  2：申请退款  3：退款成功  4：退款失败"),
},// 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
        joinTable = {
                @JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "this", alias = "b",
                        on = "b.id = a.user_id",
                        columns = {
                                @Column(name = "name", attrName = "name", label = "名称"),
                        }),
                @JoinTable(type = Type.LEFT_JOIN, entity = AppBUser.class, attrName = "this", alias = "c",
                        on = "c.id = a.user_b_id",
                        columns = {
                                @Column(name = "name", attrName = "name2", label = "名称"),
                                @Column(name = "username", attrName = "username", label = "商家号码"),
                        })
        }, orderBy = "a.id DESC"
)
public class AppRescueIndent extends DataEntity<AppRescueIndent> {

    private static final long serialVersionUID = 1L;
    private String rescueNumber;        // 救援编号
    private Long userId;        // 用户id
    private Long userBId;        // 商户id
    private String currentPosition;        // 救援位置
    private String merchantsPosition;        // 商户位置
    private String licensePlate;        // 车牌号
    private String rescueName;        // 救援联系人
    private String phoneNumber;        // 手机号码
    private String emergencyNumber;        // 紧急号码
    private Double distance;        // 距离
    private Integer type;        // 救援类型(1.搭电)
    private Double lat;        // 纬度
    private Double lng;        // 经度
    private Integer rescueScene;        // 救援场景（2拖车：1事故，2故障，3地面，4地库，5困境，6其他，3换胎：1有备胎，2无备胎，3送备胎，4其他）
    private Double price;        // 支付价格
    private Integer state;        // 订单状态 1.开始，2.进行中，3完成, 4.取消
    private Integer payState;        // 支付状态 0初始状态  1已支付   2支付失败  3退款
    private String merchantsPayNumber;        // 支付订单编号
    private String remark;        // 备注
    private Date createTime;        // create_time
    private Date updateTime;        // update_time
    private Integer backState;        // 1 初始状态  2：申请退款  3：退款成功  4：退款失败
    private String name;//用户名字
    private String name2;//商家名字
    private String username;//商家号码

    @Override
    public String toString() {
        return "AppRescueIndent{" +
                "rescueNumber='" + rescueNumber + '\'' +
                ", userId=" + userId +
                ", userBId=" + userBId +
                ", currentPosition='" + currentPosition + '\'' +
                ", merchantsPosition='" + merchantsPosition + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", rescueName='" + rescueName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emergencyNumber='" + emergencyNumber + '\'' +
                ", distance=" + distance +
                ", type=" + type +
                ", lat=" + lat +
                ", lng=" + lng +
                ", rescueScene=" + rescueScene +
                ", price=" + price +
                ", state=" + state +
                ", payState=" + payState +
                ", merchantsPayNumber='" + merchantsPayNumber + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", backState=" + backState +
                ", name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public AppRescueIndent() {
        this(null);
    }

    public AppRescueIndent(String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "救援编号长度不能超过 50 个字符")
    public String getRescueNumber() {
        return rescueNumber;
    }

    public void setRescueNumber(String rescueNumber) {
        this.rescueNumber = rescueNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserBId() {
        return userBId;
    }

    public void setUserBId(Long userBId) {
        this.userBId = userBId;
    }

    @Length(min = 0, max = 100, message = "救援位置长度不能超过 100 个字符")
    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Length(min = 0, max = 100, message = "商户位置长度不能超过 100 个字符")
    public String getMerchantsPosition() {
        return merchantsPosition;
    }

    public void setMerchantsPosition(String merchantsPosition) {
        this.merchantsPosition = merchantsPosition;
    }

    @Length(min = 0, max = 10, message = "车牌号长度不能超过 10 个字符")
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Length(min = 0, max = 10, message = "救援联系人长度不能超过 10 个字符")
    public String getRescueName() {
        return rescueName;
    }

    public void setRescueName(String rescueName) {
        this.rescueName = rescueName;
    }

    @Length(min = 0, max = 11, message = "手机号码长度不能超过 11 个字符")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Length(min = 0, max = 11, message = "紧急号码长度不能超过 11 个字符")
    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getRescueScene() {
        return rescueScene;
    }

    public void setRescueScene(Integer rescueScene) {
        this.rescueScene = rescueScene;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    @Length(min = 0, max = 50, message = "支付订单编号长度不能超过 50 个字符")
    public String getMerchantsPayNumber() {
        return merchantsPayNumber;
    }

    public void setMerchantsPayNumber(String merchantsPayNumber) {
        this.merchantsPayNumber = merchantsPayNumber;
    }

    @Length(min = 0, max = 100, message = "备注长度不能超过 100 个字符")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getBackState() {
        return backState;
    }

    public void setBackState(Integer backState) {
        this.backState = backState;
    }

}
