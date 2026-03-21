package com.cheji.web.modular.cwork;

import java.math.BigDecimal;
import java.util.List;

//提现详情打工类
public class WithdDetails {
    private List<Progress> progresses;  //处理进度
    private String iconUrl;         //银行商标
    private BigDecimal amount;    //提现金额
    private BigDecimal fee;         //手续费
    private String createTime;      //申请时间
    private String successTime;     //到账时间
    private String bankName;        //提现银行
    private String partnerTradeNo;  //提现单号
    private String bankTail;        //银行卡尾号

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(List<Progress> progresses) {
        this.progresses = progresses;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getBankTail() {
        return bankTail;
    }

    public void setBankTail(String bankTail) {
        this.bankTail = bankTail;
    }

    @Override
    public String toString() {
        return "WithdDetails{" +
                "progresses=" + progresses +
                ", iconUrl='" + iconUrl + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", createTime='" + createTime + '\'' +
                ", successTime='" + successTime + '\'' +
                ", bankName='" + bankName + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", bankTail='" + bankTail + '\'' +
                '}';
    }
}
