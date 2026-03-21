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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户金额记录表Entity
 *
 * @author dh
 * @version 2019-10-31
 */
@Table(name = "app_user_account_record", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "momey", attrName = "momey", label = "金额"),
        @Column(name = "user_id", attrName = "userId", label = "用户id"),
        @Column(name = "type", attrName = "type", label = "操作类型 1", comment = "操作类型 1：提现"),
        @Column(name = "create_time", attrName = "createTime", label = "发生时间"),
        @Column(name = "add_flag", attrName = "addFlag", label = "1加钱  2 减钱"),
        @Column(name = "source", attrName = "source", label = "1", comment = "1:c端  2：b端"),
        @Column(name = "business_id", attrName = "businessId", label = "业务id"),
}, // 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
        joinTable = {
                @JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "appUser", alias = "appUser",
                        on = "appUser.id = a.user_id",
                        columns = {
                                @Column(name = "id", attrName = "id", label = "id"),
                                @Column(name = "name", attrName = "name", label = "名字"),
                        }),
                @JoinTable(type = Type.LEFT_JOIN, entity = AppBUser.class, attrName = "appBUser", alias = "appBUser",
                        on = "appBUser.id = a.user_id",
                        columns = {
                                @Column(name = "id", attrName = "id", label = "id"),
                                @Column(name = "merchants_name", attrName = "name", label = "名字"),
                        })
        }, orderBy = "a.id DESC"
)
public class AppUserAccountRecord extends DataEntity<AppUserAccountRecord> {

    private static final long serialVersionUID = 1L;
    private BigDecimal momey;        // 金额
    private Long userId;        // 用户id
    private AppUser appUser;
    private AppBUser appBUser;
    private Integer type;        // 操作类型 1：提现
    private Date createTime;        // 发生时间
    private Integer addFlag;        // 1加钱  2 减钱
    private Integer source;        // 1:c端  2：b端
    private String businessId;        // 业务id

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppBUser getAppBUser() {
        return appBUser;
    }

    public void setAppBUser(AppBUser appBUser) {
        this.appBUser = appBUser;
    }

    public AppUserAccountRecord() {
        this(null);
    }

    public AppUserAccountRecord(String id) {
        super(id);
    }

    public BigDecimal getMomey() {
        return momey;
    }

    public void setMomey(BigDecimal momey) {
        this.momey = momey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(Integer addFlag) {
        this.addFlag = addFlag;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Length(min = 0, max = 50, message = "业务id长度不能超过 50 个字符")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Date getCreateTime_gte() {
        return sqlMap.getWhere().getValue("create_time", QueryType.GTE);
    }

    public void setCreateTime_gte(Date createTime) {
        sqlMap.getWhere().and("create_time", QueryType.GTE, createTime);
    }

    public Date getCreateTime_lte() {
        return sqlMap.getWhere().getValue("create_time", QueryType.LTE);
    }

    public void setCreateTime_lte(Date createTime) {
        sqlMap.getWhere().and("create_time", QueryType.LTE, createTime);
    }
}