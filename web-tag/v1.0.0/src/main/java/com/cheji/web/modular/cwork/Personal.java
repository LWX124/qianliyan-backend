package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

//个人中心界面
public class Personal {
    private String userid;           //用户id
    private String userName;        //用户名字
    private String avatar;           //头像
    private String levelName;       //理赔顾问等级
    private String phoneNumber;     //手机号
    private String salesRevenue;    //销售收入
    private Integer coupons;        //优惠卷
    //private String cheJiCoin;       //车己币
    private String  wallet;      //我的钱包
    private String newIndent;       //新订单订单数量
    private String beforeShop;      //未到店订单数量
    private String inService;       //服务中订单数量
    private String endService;      //已交车订单数量
    private String alreadySolved;   //已结算订单数量
    private String plusMember;      //plus会员状态
    private String inviteCode;      //邀请码

    private Integer waitReceiving;       //待收货
    private Integer inServiceing;        //服务中
    private Integer toEvaluate;          //待评价

    private Integer isMerchants;        //是否是商户


    public Integer getIsMerchants() {
        return isMerchants;
    }

    public void setIsMerchants(Integer isMerchants) {
        this.isMerchants = isMerchants;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getCoupons() {
        return coupons;
    }

    public void setCoupons(Integer coupons) {
        this.coupons = coupons;
    }

    public Integer getWaitReceiving() {
        return waitReceiving;
    }

    public void setWaitReceiving(Integer waitReceiving) {
        this.waitReceiving = waitReceiving;
    }

    public Integer getInServiceing() {
        return inServiceing;
    }

    public void setInServiceing(Integer inServiceing) {
        this.inServiceing = inServiceing;
    }

    public Integer getToEvaluate() {
        return toEvaluate;
    }

    public void setToEvaluate(Integer toEvaluate) {
        this.toEvaluate = toEvaluate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getSalesRevenue() {
        return salesRevenue;
    }

    public void setSalesRevenue(String salesRevenue) {
        this.salesRevenue = salesRevenue;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getNewIndent() {
        return newIndent;
    }

    public void setNewIndent(String newIndent) {
        this.newIndent = newIndent;
    }

    public String getBeforeShop() {
        return beforeShop;
    }

    public void setBeforeShop(String beforeShop) {
        this.beforeShop = beforeShop;
    }

    public String getInService() {
        return inService;
    }

    public void setInService(String inService) {
        this.inService = inService;
    }

    public String getEndService() {
        return endService;
    }

    public void setEndService(String endService) {
        this.endService = endService;
    }

    public String getAlreadySolved() {
        return alreadySolved;
    }

    public void setAlreadySolved(String alreadySolved) {
        this.alreadySolved = alreadySolved;
    }

    public String getPlusMember() {
        return plusMember;
    }

    public void setPlusMember(String plusMember) {
        this.plusMember = plusMember;
    }


    @Override
    public String toString() {
        return "Personal{" +
                "userid='" + userid + '\'' +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", levelName='" + levelName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salesRevenue='" + salesRevenue + '\'' +
                ", coupons=" + coupons +
                ", wallet='" + wallet + '\'' +
                ", newIndent='" + newIndent + '\'' +
                ", beforeShop='" + beforeShop + '\'' +
                ", inService='" + inService + '\'' +
                ", endService='" + endService + '\'' +
                ", alreadySolved='" + alreadySolved + '\'' +
                ", plusMember='" + plusMember + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", waitReceiving=" + waitReceiving +
                ", inServiceing=" + inServiceing +
                ", toEvaluate=" + toEvaluate +
                ", isMerchants=" + isMerchants +
                '}';
    }
}
