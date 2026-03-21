package com.cheji.b.modular.dto;

import java.math.BigDecimal;

//我的页面
public class MineDto {
    private Integer visiter;                    //今日访客
    private String name;                        //名字
    private String avatar;                      //头像
    private Integer todayIndent;                //今日订单
    private Integer allIndent;                  //全部订单
    private Integer allbill;                    //全部账单
    private BigDecimal wallet;                  //我的钱包
    private BigDecimal consumptionDeduction;   //消费抵扣
    private String username;                    //账户名
    private String merchantsName;               //店铺名字
    private String phoneOn;               //关联的人员电话
//    private String empName;               //关联的人员的id

    public String getPhoneOn() {
        return phoneOn;
    }

    public void setPhoneOn(String phoneOn) {
        this.phoneOn = phoneOn;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public Integer getVisiter() {
        return visiter;
    }

    public void setVisiter(Integer visiter) {
        this.visiter = visiter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getTodayIndent() {
        return todayIndent;
    }

    public void setTodayIndent(Integer todayIndent) {
        this.todayIndent = todayIndent;
    }

    public Integer getAllIndent() {
        return allIndent;
    }

    public void setAllIndent(Integer allIndent) {
        this.allIndent = allIndent;
    }

    public Integer getAllbill() {
        return allbill;
    }

    public void setAllbill(Integer allbill) {
        this.allbill = allbill;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getConsumptionDeduction() {
        return consumptionDeduction;
    }

    public void setConsumptionDeduction(BigDecimal consumptionDeduction) {
        this.consumptionDeduction = consumptionDeduction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "MineDto{" +
                "visiter=" + visiter +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", todayIndent=" + todayIndent +
                ", allIndent=" + allIndent +
                ", allbill=" + allbill +
                ", wallet=" + wallet +
                ", consumptionDeduction=" + consumptionDeduction +
                ", username='" + username + '\'' +
                '}';
    }
}
