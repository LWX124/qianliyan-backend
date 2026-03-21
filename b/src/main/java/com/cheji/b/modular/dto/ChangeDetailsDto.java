package com.cheji.b.modular.dto;



import java.math.BigDecimal;
import java.util.List;

//零钱详情
public class ChangeDetailsDto {
    private String bankImg;     //银行图片
    private String bankName;    //银行名称
    private String bankTail;    //银行卡号
    private BigDecimal amount;  //金额
    private BigDecimal serviceFee;//服务费
    private String createTime;  //申请时间
    private String successTime; //到账时间
    private List<ProgressDto> listProgress; //处理进度
    private String resultInfo;              //处理情况
    private String partnerTradeNo;          //订单号


    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
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


    public List<ProgressDto> getListProgress() {
        return listProgress;
    }

    public void setListProgress(List<ProgressDto> listProgress) {
        this.listProgress = listProgress;
    }

    public String getBankTail() {
        return bankTail;
    }

    public void setBankTail(String bankTail) {
        this.bankTail = bankTail;
    }

    @Override
    public String toString() {
        return "ChangeDetailsDto{" +
                "bankImg='" + bankImg + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankTail='" + bankTail + '\'' +
                ", amount=" + amount +
                ", serviceFee=" + serviceFee +
                ", createTime='" + createTime + '\'' +
                ", successTime='" + successTime + '\'' +
                ", listProgress=" + listProgress +
                ", resultInfo='" + resultInfo + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                '}';
    }
}
