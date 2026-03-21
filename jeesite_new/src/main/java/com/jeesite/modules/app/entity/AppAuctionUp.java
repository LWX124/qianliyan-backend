/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;

import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 拍卖车上架信息表Entity
 *
 * @author y
 * @version 2023-03-10
 */
@Table(name = "app_auction_up", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "主键id", isPK = true),
        @Column(name = "car_id", attrName = "carId", label = "关联拍卖车id"),
        @Column(name = "create_time", attrName = "createTime", label = "create_time"),
        @Column(name = "begin_time", attrName = "beginTime", label = "拍卖开始时间"),
        @Column(name = "end_time", attrName = "endTime", label = "拍卖结束时间"),
        @Column(name = "update_time", attrName = "updateTime", label = "修改时间"),
        @Column(name = "service_fee", attrName = "serviceFee", label = "服务费率"),
        @Column(name = "other_fee", attrName = "otherFee", label = "其他费用"),
        @Column(name = "brand", attrName = "brand", label = "车牌号", isQuery = true, queryType = QueryType.LIKE),
        @Column(name = "explain", attrName = "explain", label = "失败说明"),
        @Column(name = "counselor_id", attrName = "counselorId", label = "顾问id", comment = "顾问id:操作人员id"),
        @Column(name = "back_info", attrName = "backInfo", label = "管理员备注"),
        @Column(name = "up_date_num", attrName = "upDateNum", label = "上架天数"),
}, // 支持联合查询，如左右连接查询，支持设置查询自定义关联表的返回字段列
        joinTable = {
                @JoinTable(type = Type.LEFT_JOIN, entity = AppAuction.class, attrName = "appAuction", alias = "o",
                        on = "o.id = a.car_id", columns = {
                        @Column(name = "brand", label = "型号", isQuery = true, queryType = QueryType.LIKE),
                        @Column(name = "price", label = "起拍价", isQuery = true, queryType = QueryType.EQ),
                        @Column(name = "car_state", label = "状态", isQuery = true, queryType = QueryType.EQ),
                        @Column(name = "up_state", label = "上架状态", isQuery = true, queryType = QueryType.EQ),
                        @Column(name = "fixed_price", label = "是否一口价", isQuery = true, queryType = QueryType.EQ),
                        @Column(name = "source_type", label = "车辆类型", isQuery = true, queryType = QueryType.EQ),
                        @Column(name = "id", label = "id", isPK = true, isQuery = true, queryType = QueryType.LIKE),
                        @Column(name = "car_bond_amt", label = "车辆保证金（元）", isQuery = false),
                        @Column(name = "car_service_amt", label = "车辆服务费（元）", isQuery = false),
                        @Column(name = "luxury_car_price", label = "精品车价格（万元）", isQuery = false),
                        @Column(name = "first_amount", label = "首付价格（万元）", isQuery = false),
                }),
        }, orderBy = "a.id DESC"
)
public class AppAuctionUp extends DataEntity<AppAuctionUp> {

    private static final long serialVersionUID = 1L;
    private Long carId;        // 关联拍卖车id
    private Date createTime;        // create_time
    private Date beginTime;        // 拍卖开始时间
    private Date endTime;        // 拍卖结束时间
    private Date updateTime;        // 修改时间
    private Integer serviceFee;        // 服务费率
    private Integer otherFee;        // 其他费用
    private String brand;        // 车牌号
    private String explain;        // 失败说明
    private String counselorId;        // 顾问id:操作人员id

    private String backInfo; // 管理员备注
    private Integer upDateNum; // 上架天数
    private Integer timeFlag; // 时间拍卖中 1 是  0 否

    private String bidInfo;

    public String getBidInfo() {
        return bidInfo;
    }

    public void setBidInfo(String bidInfo) {
        this.bidInfo = bidInfo;
    }

    public Integer getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(Integer timeFlag) {
        this.timeFlag = timeFlag;
    }

    private AppAuction appAuction;

    public AppAuction getAppAuction() {
        return appAuction;
    }

    public void setAppAuction(AppAuction appAuction) {
        this.appAuction = appAuction;
    }

    public Integer getUpDateNum() {
        return upDateNum;
    }

    public void setUpDateNum(Integer upDateNum) {
        this.upDateNum = upDateNum;
    }

    public String getBackInfo() {
        return backInfo;
    }

    public void setBackInfo(String backInfo) {
        this.backInfo = backInfo;
    }

    public AppAuctionUp() {
        this(null);
    }

    public AppAuctionUp(String id) {
        super(id);
    }

    @NotNull(message = "关联拍卖车id不能为空")
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Integer serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Integer getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(Integer otherFee) {
        this.otherFee = otherFee;
    }

    @Length(min = 0, max = 255, message = "车牌号长度不能超过 255 个字符")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Length(min = 0, max = 255, message = "失败说明长度不能超过 255 个字符")
    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Length(min = 0, max = 20, message = "顾问id长度不能超过 20 个字符")
    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }


}