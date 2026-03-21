package com.cheji.web.modular.cwork;

import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;

//plus会员信息
public class UserPlus {
    private String name;                    //名字
    private String avatar;                  //头像
    private String userLevel;               //会员等级
    private String inviteCode;              //邀请码
    private BigDecimal todayReward;         //今日奖励
    private BigDecimal monthReward;         //本月奖励

    private BigDecimal accidReward;         //事故奖励
    private BigDecimal promtionReward;      //推广奖励
    private BigDecimal allReward;           //累计奖励
    private BigDecimal balance;             //可提奖励
    private Integer invitationPeople;       //邀请人数
    private Integer invitationMerchants;    //邀请店铺
    private String levelReward;             //奖励文字

    private String originalPrice;           //原价
    private String preferentialPrice;       //优惠后价格
    private String marketingAgency;         //营销代理权
    private String maintainZone;            //养车专区
    private String warmPrompt;              //温馨提示
    private String levelRules;          //升级规则


    public String getLevelRules() {
        return levelRules;
    }

    public void setLevelRules(String levelRules) {
        this.levelRules = levelRules;
    }

    public BigDecimal getAccidReward() {
        return accidReward;
    }

    public void setAccidReward(BigDecimal accidReward) {
        this.accidReward = accidReward;
    }

    public BigDecimal getPromtionReward() {
        return promtionReward;
    }

    public void setPromtionReward(BigDecimal promtionReward) {
        this.promtionReward = promtionReward;
    }

    public String getLevelReward() {
        return levelReward;
    }

    public void setLevelReward(String levelReward) {
        this.levelReward = levelReward;
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

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public BigDecimal getTodayReward() {
        return todayReward;
    }

    public void setTodayReward(BigDecimal todayReward) {
        this.todayReward = todayReward;
    }

    public BigDecimal getMonthReward() {
        return monthReward;
    }

    public void setMonthReward(BigDecimal monthReward) {
        this.monthReward = monthReward;
    }

    public BigDecimal getAllReward() {
        return allReward;
    }

    public void setAllReward(BigDecimal allReward) {
        this.allReward = allReward;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getInvitationPeople() {
        return invitationPeople;
    }

    public void setInvitationPeople(Integer invitationPeople) {
        this.invitationPeople = invitationPeople;
    }

    public Integer getInvitationMerchants() {
        return invitationMerchants;
    }

    public void setInvitationMerchants(Integer invitationMerchants) {
        this.invitationMerchants = invitationMerchants;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(String preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public String getMarketingAgency() {
        return marketingAgency;
    }

    public void setMarketingAgency(String marketingAgency) {
        this.marketingAgency = marketingAgency;
    }

    public String getMaintainZone() {
        return maintainZone;
    }

    public void setMaintainZone(String maintainZone) {
        this.maintainZone = maintainZone;
    }

    public String getWarmPrompt() {
        return warmPrompt;
    }

    public void setWarmPrompt(String warmPrompt) {
        this.warmPrompt = warmPrompt;
    }

    @Override
    public String toString() {
        return "UserPlus{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", todayReward=" + todayReward +
                ", monthReward=" + monthReward +
                ", allReward=" + allReward +
                ", balance=" + balance +
                ", invitationPeople=" + invitationPeople +
                ", invitationMerchants=" + invitationMerchants +
                ", originalPrice='" + originalPrice + '\'' +
                ", preferentialPrice='" + preferentialPrice + '\'' +
                ", marketingAgency='" + marketingAgency + '\'' +
                ", maintainZone='" + maintainZone + '\'' +
                ", warmPrompt='" + warmPrompt + '\'' +
                '}';
    }
}
