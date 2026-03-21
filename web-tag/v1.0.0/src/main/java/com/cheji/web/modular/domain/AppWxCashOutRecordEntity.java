package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信企业到银行卡提现记录表
 */
@TableName("app_wx_cash_out_record")
public class AppWxCashOutRecordEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Date createTime;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal sendAmount;
    private Integer userId;
    private Integer status;
    private String bankNumber;
    private String bankCode;
    private String userBankName;
    private String partnerTradeNo;//商户订单号
    private String result;//
    private String resultInfo;//提现结果展示给用户
    private Integer sources;//商户订单号

    @Override
    public String toString() {
        return "AppWxCashOutRecordEntity{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", amount=" + amount +
                ", fee=" + fee +
                ", sendAmount=" + sendAmount +
                ", userId=" + userId +
                ", status=" + status +
                ", bankNumber='" + bankNumber + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", userBankName='" + userBankName + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", result='" + result + '\'' +
                ", resultInfo='" + resultInfo + '\'' +
                ", sources=" + sources +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Integer getSources() {
        return sources;
    }

    public void setSources(Integer sources) {
        this.sources = sources;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getUserBankName() {
        return userBankName;
    }

    public void setUserBankName(String userBankName) {
        this.userBankName = userBankName;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }
}
