package com.cheji.b.modular.dto;

import java.math.BigDecimal;

//首页数据
public class HomePageDto {
    private String merchantsName;       //商户名称
    private BigDecimal todayEarnings;   //商户今日收益
    private Integer indentCount;        //商户有效订单
    private BigDecimal wallet;          //钱包
    private String prompt;      //判断字符串
    private String isOnline;    //是否上线


    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public BigDecimal getTodayEarnings() {
        return todayEarnings;
    }

    public void setTodayEarnings(BigDecimal todayEarnings) {
        this.todayEarnings = todayEarnings;
    }

    public Integer getIndentCount() {
        return indentCount;
    }

    public void setIndentCount(Integer indentCount) {
        this.indentCount = indentCount;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "HomePageDto{" +
                "merchantsName='" + merchantsName + '\'' +
                ", todayEarnings=" + todayEarnings +
                ", indentCount=" + indentCount +
                ", wallet=" + wallet +
                ", prompt='" + prompt + '\'' +
                ", isOnline='" + isOnline + '\'' +
                '}';
    }
}
