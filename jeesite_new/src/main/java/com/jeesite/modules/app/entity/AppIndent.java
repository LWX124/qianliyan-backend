package com.jeesite.modules.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息表Entity
 *
 * @author zcq
 * @version 2019-08-05
 */
@Table(name = "app_indent", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "user_b_id", attrName = "userBId", label = "商户id", isInsert = false),
        @Column(name = "user_id", attrName = "userId", label = "用户id", isInsert = false),
        @Column(name = "username", attrName = "username", label = "订单上用户名", isInsert = false, isQuery = false),
        @Column(name = "phone_number", attrName = "phoneNumber", label = "电话号码", isInsert = false, isQuery = false),
        @Column(name = "plan", attrName = "plan", label = "维修方案", isInsert = false, isQuery = false),
        @Column(name = "license_plate", attrName = "licensePlate", label = "车牌", isInsert = false, queryType=QueryType.LIKE),
        @Column(name = "commission_rate", attrName = "commissionRate", label = "佣金比例", isInsert = false, isQuery = false),
        @Column(name = "brand_id", attrName = "brandId", label = "品牌id", isInsert = false, isQuery = false),
        @Column(name = "remake", attrName = "remake", label = "备注", comment = "备注(100字)", isInsert = false, isQuery = false),
        @Column(name = "order_number", attrName = "orderNumber", label = "订单号", isInsert = false, queryType=QueryType.LIKE),
        @Column(name = "send_people", attrName = "sendPeople", label = "送修人员", isInsert = false, queryType=QueryType.LIKE),
        @Column(name = "means_payments", attrName = "meansPayments", label = "结算方式", isInsert = false, isQuery = false),
        @Column(name = "state", attrName = "state", label = "状态", isInsert = false),
        @Column(name = "settle_accounts", attrName = "settleAccounts", label = "结算金额", isInsert = false, isQuery = false),
        @Column(name = "fixloss", attrName = "fixloss", label = "定损金额", isInsert = false),
        @Column(name = "fixloss_user", attrName = "fixlossUser", label = "结算到用户金额", isInsert = false, isQuery = false),
        @Column(name = "rescue_them_fee", attrName = "rescueThemFee", label = "施救费", isInsert = false, isQuery = false),
        @Column(name = "responsibility", attrName = "responsibility", label = "责任划分", isInsert = false, isQuery = false),
        @Column(name = "insurance_company", attrName = "insuranceCompany", label = "保险公司", isInsert = false, isQuery = false),
        @Column(name = "send_unit", attrName = "sendUnit", label = "送修单位", isInsert = false, queryType=QueryType.LIKE),
        @Column(name = "book_value", attrName = "bookValue", label = "到账金额", isInsert = false),

        @Column(name = "settle_fours_company", attrName = "settleFoursCompany", label = "4s店结算给公司金额", isInsert = false, isQuery = false),
        @Column(name = "settle_fours_company_rate", attrName = "settleFoursCompanyRate", label = "4s店结算给公司比例", isInsert = false, isQuery = false),

        @Column(name = "send_back", attrName = "sendBack", label = "send_back", isInsert = false),
        @Column(name = "is_rec", attrName = "isRec", label = "是否对账", isInsert = false, isQuery = false),
        @Column(name = "message_source", attrName = "messageSource", label = "message_source", isInsert = false),
        @Column(name = "deal_time", attrName = "dealTime", label = "deal_time", isInsert = false),
        @Column(name = "collate_time", attrName = "collateTime", label = "对账时间", isInsert = false),
        @Column(name = "creat_time", attrName = "creatTime", label = "creat_time", isInsert = false),
        @Column(name = "update_time", attrName = "updateTime", label = "update_time", isInsert = false),

        @Column(name = "manager_remake", attrName = "managerRemake", label = "经理备注", isInsert = false),
        @Column(name = "financial_remake", attrName = "financialRemake", label = "财务备注", isInsert = false),
        @Column(name = "audit", attrName = "audit", label = "审核流程", isInsert = false),
        @Column(name = "up_id", attrName = "upId", label = "上架商户id", isInsert = false),
        @Column(name = "account", attrName = "account", label = "是否到账", isInsert = false),

},
        // 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
        joinTable = {
                @JoinTable(type = Type.LEFT_JOIN, entity = AppUser.class, attrName = "this", alias = "b",
                        on = "b.id = a.user_id",
                        columns = {
                                @Column(name = "name", attrName = "name", label = "名称"),
                                @Column(name = "phone_number", attrName = "userPhone", label = "下单人电话"),
                        }),
                @JoinTable(type = Type.LEFT_JOIN, entity = AppBUser.class, attrName = "this", alias = "c",
                        on = "c.id = a.user_b_id",
                        columns = {
                                @Column(name = "merchants_name", attrName = "merchantsName", label = "商户名称"),
                                @Column(name = "phone_number", attrName = "merchantsPhone", label = "商户电话"),
                                @Column(name = "balance", attrName = "balance", label = "商户余额")
                        }),
                @JoinTable(type = Type.LEFT_JOIN, entity = AppCarBrand.class, attrName = "this", alias = "d",
                        on = "a.brand_id = d.id",
                        columns = {
                                @Column(name = "name", attrName = "brandName", label = "品牌名称"),
                        }),

        }, orderBy = "a.id DESC"
)
public class AppIndent extends DataEntity<AppIndent> {

    private static final long serialVersionUID = 1L;
    private Integer userBId;        //商户id
    private Integer userId;        // 用户id
    private String username;        //订单上用户名
    private String phoneNumber;    //电话号码
    private String plan;        // 维修方案
    private String licensePlate;        // 车牌
    private BigDecimal commissionRate;    //佣金比例
    private Integer brandId;        // 品牌id
    private String remake;        // 备注(100字)
    private String orderNumber;        // 订单号
    private String meansPayments;    //结算方式
    private Integer state;        // 状态
    private BigDecimal settleAccounts;        // 结算金额
    private BigDecimal fixloss;        // 定损金额
    private BigDecimal fixlossUser;    //结算到用户金额
    private Date creatTime;        // creat_time
    private Date updateTime;        // update_time
    private String name;
    private String merchantsName;
    private String brandName;       //品牌名
    private String userPhone;       //下单人的电话
    private String merchantsPhone;  //商户电话
    private BigDecimal balance;
    private String sendPeople;

    private BigDecimal settleFoursCompany;  //4s店结算给公司金额
    private BigDecimal settleFoursCompanyRate; //4s店结算给公司比例

    private BigDecimal rescueThemFee;
    private String responsibility;
    private String insuranceCompany;
    private String sendUnit;

    private Integer sendBack;

    private Integer messageSource;
    private Integer dealTime;
    private BigDecimal bookValue;
    //总产值
    private BigDecimal allFixuser;
    private Integer isRec;
    private Date collateTime;

    private Integer newIndentNumber;//新订单

    private Integer inServiceNumber;//服务中

    private Integer forwardNumber; //已交车

    private Integer settledNumber;//已结算

    private Integer finishNumber;//已完成

    private BigDecimal estimatedAmountNew;    //预估金额新订单

    private BigDecimal estimatedAmountInSer;    //预估金额服务中

    private BigDecimal allSettlementAmount;      //结算金额

    private BigDecimal settlementRatio;   //结算比例

    private String financialRemake;         //财务备注

    private String managerRemake;           //经理备注

    private Integer audit;              //审核流程

    private Integer type;

    private String upId;

    private String account; //是否到账

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAudit() {
        return audit;
    }

    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    public String getFinancialRemake() {
        return financialRemake;
    }

    public void setFinancialRemake(String financialRemake) {
        this.financialRemake = financialRemake;
    }

    public String getManagerRemake() {
        return managerRemake;
    }

    public void setManagerRemake(String managerRemake) {
        this.managerRemake = managerRemake;
    }

    public BigDecimal getSettlementRatio() {
        return settlementRatio;
    }

    public void setSettlementRatio(BigDecimal settlementRatio) {
        this.settlementRatio = settlementRatio;
    }

    public Date getCollateTime() {
        return collateTime;
    }

    public void setCollateTime(Date collateTime) {
        this.collateTime = collateTime;
    }

    public BigDecimal getEstimatedAmountNew() {
        return estimatedAmountNew;
    }

    public void setEstimatedAmountNew(BigDecimal estimatedAmountNew) {
        this.estimatedAmountNew = estimatedAmountNew;
    }

    public BigDecimal getEstimatedAmountInSer() {
        return estimatedAmountInSer;
    }

    public void setEstimatedAmountInSer(BigDecimal estimatedAmountInSer) {
        this.estimatedAmountInSer = estimatedAmountInSer;
    }

    public BigDecimal getAllSettlementAmount() {
        return allSettlementAmount;
    }

    public void setAllSettlementAmount(BigDecimal allSettlementAmount) {
        this.allSettlementAmount = allSettlementAmount;
    }

    public Integer getIsRec() {
        return isRec;
    }

    public void setIsRec(Integer isRec) {
        this.isRec = isRec;
    }

    public Integer getNewIndentNumber() {
        return newIndentNumber;
    }

    public void setNewIndentNumber(Integer newIndentNumber) {
        this.newIndentNumber = newIndentNumber;
    }

    public Integer getInServiceNumber() {
        return inServiceNumber;
    }

    public void setInServiceNumber(Integer inServiceNumber) {
        this.inServiceNumber = inServiceNumber;
    }

    public Integer getForwardNumber() {
        return forwardNumber;
    }

    public void setForwardNumber(Integer forwardNumber) {
        this.forwardNumber = forwardNumber;
    }

    public Integer getSettledNumber() {
        return settledNumber;
    }

    public void setSettledNumber(Integer settledNumber) {
        this.settledNumber = settledNumber;
    }

    public Integer getFinishNumber() {
        return finishNumber;
    }

    public void setFinishNumber(Integer finishNumber) {
        this.finishNumber = finishNumber;
    }

    public BigDecimal getAllFixuser() {
        return allFixuser;
    }

    public void setAllFixuser(BigDecimal allFixuser) {
        this.allFixuser = allFixuser;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public Integer getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(Integer messageSource) {
        this.messageSource = messageSource;
    }

    public Integer getDealTime() {
        return dealTime;
    }

    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }

    public Integer getSendBack() {
        return sendBack;
    }

    public void setSendBack(Integer sendBack) {
        this.sendBack = sendBack;
    }

    public BigDecimal getSettleFoursCompany() {
        return settleFoursCompany;
    }

    public void setSettleFoursCompany(BigDecimal settleFoursCompany) {
        this.settleFoursCompany = settleFoursCompany;
    }

    public BigDecimal getSettleFoursCompanyRate() {
        return settleFoursCompanyRate;
    }

    public void setSettleFoursCompanyRate(BigDecimal settleFoursCompanyRate) {
        this.settleFoursCompanyRate = settleFoursCompanyRate;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public BigDecimal getRescueThemFee() {
        return rescueThemFee;
    }

    public void setRescueThemFee(BigDecimal rescueThemFee) {
        this.rescueThemFee = rescueThemFee;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getSendUnit() {
        return sendUnit;
    }

    public void setSendUnit(String sendUnit) {
        this.sendUnit = sendUnit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMerchantsPhone() {
        return merchantsPhone;
    }

    public void setMerchantsPhone(String merchantsPhone) {
        this.merchantsPhone = merchantsPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    public BigDecimal getFixlossUser() {
        return fixlossUser;
    }

    public void setFixlossUser(BigDecimal fixlossUser) {
        this.fixlossUser = fixlossUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantsName() {
        return merchantsName;
    }

    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }

    public AppIndent() {
        this(null);
    }

    public AppIndent(String id) {
        super(id);
    }

    @NotNull(message = "用户id不能为空")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Length(min = 0, max = 50, message = "维修方案长度不能超过 50 个字符")
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @NotBlank(message = "车牌不能为空")
    @Length(min = 0, max = 20, message = "车牌长度不能超过 20 个字符")
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @NotNull(message = "品牌id不能为空")
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    @Length(min = 0, max = 100, message = "备注长度不能超过 100 个字符")
    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    @NotBlank(message = "订单号不能为空")
    @Length(min = 0, max = 100, message = "订单号长度不能超过 100 个字符")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @NotNull(message = "状态不能为空")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public BigDecimal getFixloss() {
        return fixloss;
    }

    public void setFixloss(BigDecimal fixloss) {
        this.fixloss = fixloss;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getMeansPayments() {
        return meansPayments;
    }

    public void setMeansPayments(String meansPayments) {
        this.meansPayments = meansPayments;
    }

    public Date getCreateTime_gte() {
        return sqlMap.getWhere().getValue("creat_time", QueryType.GTE);
    }

    public void setCreateTime_gte(Date createTime) {
        sqlMap.getWhere().and("creat_time", QueryType.GTE, createTime);
    }

    public Date getCreateTime_lte() {
        return sqlMap.getWhere().getValue("creat_time", QueryType.LTE);
    }

    public void setCreateTime_lte(Date createTime) {
        sqlMap.getWhere().and("creat_time", QueryType.LTE, createTime);
    }


    public Date getUpdateTime_gte() {
        return sqlMap.getWhere().getValue("update_time", QueryType.GTE);
    }

    public void setUpdateTime_gte(Date updateTime) {
        sqlMap.getWhere().and("update_time", QueryType.GTE, updateTime);
    }

    public Date getUpdateTime_lte() {
        return sqlMap.getWhere().getValue("update_time", QueryType.LTE);
    }

    public void setUpdateTime_lte(Date updateTime) {
        sqlMap.getWhere().and("update_time", QueryType.LTE, updateTime);
    }



    @Override
    public String toString() {
        return "AppIndent{" +
                "userBId=" + userBId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", plan='" + plan + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", commissionRate=" + commissionRate +
                ", brandId=" + brandId +
                ", remake='" + remake + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", meansPayments='" + meansPayments + '\'' +
                ", state=" + state +
                ", settleAccounts=" + settleAccounts +
                ", fixloss=" + fixloss +
                ", fixlossUser=" + fixlossUser +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", name='" + name + '\'' +
                ", merchantsName='" + merchantsName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", merchantsPhone='" + merchantsPhone + '\'' +
                ", balance=" + balance +
                ", sendPeople='" + sendPeople + '\'' +
                ", settleFoursCompany=" + settleFoursCompany +
                ", settleFoursCompanyRate=" + settleFoursCompanyRate +
                ", rescueThemFee=" + rescueThemFee +
                ", responsibility='" + responsibility + '\'' +
                ", insuranceCompany='" + insuranceCompany + '\'' +
                ", sendUnit='" + sendUnit + '\'' +
                ", sendBack=" + sendBack +
                ", messageSource=" + messageSource +
                ", dealTime=" + dealTime +
                '}';
    }
}