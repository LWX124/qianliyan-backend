/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

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
 * 提现记录表Entity
 *
 * @author dh
 * @version 2019-10-08
 */
@Table(name = "app_wx_cash_out_record", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "sources", attrName = "sources", label = "来源"),
        @Column(name = "create_time", attrName = "createTime", label = "提现时间", isInsert = false, isUpdate = false),
        @Column(name = "amount", attrName = "amount", label = "提现金额", isInsert = false, isUpdate = false),
        @Column(name = "user_id", attrName = "userId", label = "提现用户id", isInsert = false, isUpdate = false, isQuery = false),
//		@Column(name="status", attrName="status", label="状态", isInsert=false, isUpdate=false),
        @Column(name = "bank_number", attrName = "bankNumber", label = "银行卡号", isInsert = false, isUpdate = false),
        @Column(name = "bank_code", attrName = "bankCode", label = "银行编码", isInsert = false, isUpdate = false, isQuery = false),
        @Column(name = "user_bank_name", attrName = "userBankName", label = "收款姓名", isInsert = false, isUpdate = false, queryType = QueryType.LIKE),
        @Column(name = "partner_trade_no", attrName = "partnerTradeNo", label = "商户订单号", isInsert = false, isUpdate = false),
        @Column(name = "fee", attrName = "fee", label = "手续费", isInsert = false, isUpdate = false, isQuery = false),
        @Column(name = "result", attrName = "result", label = "结果", isInsert = false, isUpdate = false, isQuery = false),
        @Column(name = "result_info", attrName = "resultInfo", label = "结果详情", isInsert = false, isUpdate = false, isQuery = false),
        @Column(name = "send_amount", attrName = "sendAmount", label = "最终订单打款的钱", isInsert = false, isUpdate = false, isQuery = false),
        @Column(name = "success_time", attrName = "successTime", label = "到账时间", isInsert = false, isUpdate = false),
}, orderBy = "a.id DESC"
        , joinTable = {
        @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = AppWxBank.class, attrName = "appWxBank", alias = "appWxBank",
                on = "a.bank_code = appWxBank.bank_code", columns = {
                @Column(name = "bank_name"),
        }),}
)
public class AppWxCashOutRecord extends DataEntity<AppWxCashOutRecord> {

    private static final long serialVersionUID = 1L;
    private Integer sources;        // 来源
    private Date createTime;        // 提现时间
    private Integer amount;        // 提现金额
    private Long userId;        // 提现用户id
    private String bankNumber;        // 银行卡号
    private String bankCode;        // 银行编码
    private String userBankName;        // 收款姓名
    private String partnerTradeNo;        // 商户订单号
    private Integer fee;        // 手续费
    private String result;        // 结果
    private String resultInfo;        // 结果详情
    private Integer sendAmount;        // 最终订单打款的钱
    private String successTime;        // 到账时间
    private AppWxBank appWxBank;        // 微信银行卡编码

    public AppWxBank getAppWxBank() {
        return appWxBank;
    }

    public void setAppWxBank(AppWxBank appWxBank) {
        this.appWxBank = appWxBank;
    }

    public AppWxCashOutRecord() {
        this(null);
    }

    public AppWxCashOutRecord(String id) {
        super(id);
    }

    public Integer getSources() {
        return sources;
    }

    public void setSources(Integer sources) {
        this.sources = sources;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Length(min = 0, max = 50, message = "银行卡号长度不能超过 50 个字符")
    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    @Length(min = 0, max = 10, message = "银行编码长度不能超过 10 个字符")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Length(min = 0, max = 10, message = "收款姓名长度不能超过 10 个字符")
    public String getUserBankName() {
        return userBankName;
    }

    public void setUserBankName(String userBankName) {
        this.userBankName = userBankName;
    }

    @Length(min = 0, max = 32, message = "商户订单号长度不能超过 32 个字符")
    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    @Length(min = 0, max = 100, message = "结果长度不能超过 100 个字符")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Length(min = 0, max = 255, message = "结果详情长度不能超过 255 个字符")
    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public Integer getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Integer sendAmount) {
        this.sendAmount = sendAmount;
    }

    @Length(min = 0, max = 20, message = "到账时间长度不能超过 20 个字符")
    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
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

    public String getSuccessTime_gte() {
        return sqlMap.getWhere().getValue("success_time", QueryType.GTE);
    }

    public void setSuccessTime_gte(String successTime) {
        sqlMap.getWhere().and("success_time", QueryType.GTE, successTime);
    }

    public String getSuccessTime_lte() {
        return sqlMap.getWhere().getValue("success_time", QueryType.LTE);
    }

    public void setSuccessTime_lte(String successTime) {
        sqlMap.getWhere().and("success_time", QueryType.LTE, successTime);
    }

}
