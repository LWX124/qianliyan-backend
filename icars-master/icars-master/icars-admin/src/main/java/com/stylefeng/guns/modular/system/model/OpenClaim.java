package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 开放平台理赔单
 * </p>
 *
 * @author kosans
 * @since 2018-09-04
 */
@ApiModel(value="OpenClaim", description="开放平台下单实体bean")
@TableName("biz_open_claim")
public class OpenClaim extends Model<OpenClaim> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 理赔订单生成人openid
     */
    @ApiModelProperty(value="理赔订单生成人openid,app下单请默认入1",name="openid",example="1")
    private String openid;

    /**
     * 结算金额
     */
    @TableField(value = "settle_accounts")
    private BigDecimal settleAccounts;

    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号,新增不入",name="orderno",example="888888")
    private String orderno;
    /**
     * 客户手机号
     */
    @ApiModelProperty(value="客户手机号",name="phone")
    private String phone;
    /**
     * 客户姓名
     */
    @ApiModelProperty(value="客户姓名",name="name",example="段老板")
    private String name;
    /**
     * 车牌号
     */
    @ApiModelProperty(value="车牌号",name="cph",example="川A12345")
    private String cph;
    /**
     * 汽车品牌
     */
    @ApiModelProperty(value="汽车品牌",name="qcpp",example="宝马")
    private String qcpp;
    /**
     * 理赔状态,-1:未接车、0:已接车、1：服务中、2：已交车、3：结算完成、4：打款完成、5：作废
     */
    @ApiModelProperty(value="理赔状态,-1:未接车、0:已接车、1：服务中、2：已交车、3：结算完成、4：打款完成、5：作废",name="status",example="-1")
    private Integer status;
    /**
     * 描述
     */
    @ApiModelProperty(value="描述",name="desc",example="123456789")
    private String desc;
    /**
     * 推送4s店部门id
     */
    @ApiModelProperty(value="推送4s店部门id",name="deptid",example="45")
    private Integer deptid;
    /**
     * 经度
     */
    @ApiModelProperty(value="经度",name="lng",example="104.06143")
    private BigDecimal lng;
    /**
     * 纬度
     */
    @ApiModelProperty(value="纬度",name="lat",example="30.67072")
    private BigDecimal lat;
    /**
     * 定损金额
     */
    @ApiModelProperty(value="定损金额",name="fixloss",example="50000")
    private BigDecimal fixloss;
    /**
     * 定位地址名称
     */
    @ApiModelProperty(value="定位地址名称",name="address",example="四川省成都市")
    private String address;
    /**
     * 理赔图片
     */
    @ApiModelProperty(value="理赔图片，5张事故图片,请用|拼接",name="claimImg")
    private String claimImg;
    /**
     * 理赔资料图片
     */
    @ApiModelProperty(value="理赔资料图片,无限制添加,顺序为驾驶证、行驶证、身份证正面、银行卡、身份证反面、交警证明,请用|拼接",name="detailImg")
    private String detailImg;
    /**
     * 更新时间
     */
    private Date modifytime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间",name="createtime",example="2019-3-20 10:50:00")
    private Date createtime;
    /**
     * 车架号
     */
    @ApiModelProperty(value="车架号",name="cjh",example="8888888")
    private String cjh;
    /**
     * 4S店付款凭证号
     */
    private String payBillNo;
    /**
     * 4S店付款凭证号（针对理赔顾问）
     */
    private String payBillNoForClaim;
    /**
     * 保险公司名称
     */
    @ApiModelProperty(value="保险公司名称",name="insurer",example="太平保险")
    private String insurer;
    /**
     * 保险购买渠道
     */
    private String insurAccess;
    /**
     * 施救公司名称
     */
    @ApiModelProperty(value="施救公司名称",name="rescue",example="")
    private String rescue;
    /**
     * 施救费用
     */
    @ApiModelProperty(value="施救费用",name="rescueFee",example="")
    private BigDecimal rescueFee;
    /**
     * 承诺客户
     */
    @ApiModelProperty(value="承诺客户",name="promise",example="送代步车")
    private String promise;
    /**
     * 事故类型  单车  双车 三车  多车
     */
    @ApiModelProperty(value="事故类型  单车  双车 三车  多车",name="accType",example="单车")
    private String accType;
    /**
     * 预估费用
     */
    @ApiModelProperty(value="预估费用",name="preFee",example="10")
    private BigDecimal preFee;
    /**
     * 有无人伤
     */
    @ApiModelProperty(value="有无人伤",name="personHurts",example="有")
    private String personHurts;
    /**
     * 有无物损
     */
    @ApiModelProperty(value="有无物损",name="goodsHurts",example="有")
    private String goodsHurts;
    /**
     * 结算方式
     */
    @ApiModelProperty(value="结算方式",name="settleType",example="")
    private String settleType;
    /**
     * 救援费用收款方式
     */
    @ApiModelProperty(value="救援费用收款方式",name="rescueFeeSettleType",example="")
    private String rescueFeeSettleType;
    /**
     * 4S返点公司提成金额
     */
    @TableField("rebate_for_company")
    private BigDecimal rebateForCompany;
    /**
     * 公司给业务员返点提成金额
     */
    @TableField("rebate_for_emp")
    private BigDecimal rebateForEmp;
    /**
     * 是否反馈异常  0 否  1 是
     */
    private Integer hasException;
    /**
     * 服务4S店
     */
    @ApiModelProperty(value="服务4S店",name="deptName",example="某店")
    @TableField(exist = false)
    private String deptName;


    /**
     * 上报理赔顾问电话
     */
    @TableField(exist = false)
    private String claimerPhone;


    /**
     * 上报理赔顾问账号
     */

    private String claimer;

    /**
     * 理赔顾问银行卡号
     */
    @TableField(exist = false)
    private String bankcard;

    /**
     * 理赔顾问账户名
     */
    @TableField(exist = false)
    private String bankUserName;
    /**
     * 开户银行
     */
    @TableField(exist = false)
    private String bankName;
    /**
     * 开户银行支行
     */
    @TableField(exist = false)
    private String bankSecondName;
    /**
     * 理赔顾问身份证
     */
    @TableField(exist = false)
    private String idcard;


    /**
     * 驾驶证号
     * @return
     */
    @ApiModelProperty(value="驾驶证号",name="carcard")
    private String carcard;

    /**
     * 行驶证号
     * @return
     */
    @ApiModelProperty(value="行驶证号",name="drivecard")
    private String drivecard;
    /**
     * 客户身份证号
     * @return
     */
    @ApiModelProperty(value="客户身份证号",name="conidcard")
    private String conidcard;
    /**
     * 客户银行卡号
     * @return
     */
    @ApiModelProperty(value="客户银行卡号",name="conbankcard")
    private String conbankcard;


    public BigDecimal getSettleAccounts() {
        return settleAccounts;
    }

    public void setSettleAccounts(BigDecimal settleAccounts) {
        this.settleAccounts = settleAccounts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getQcpp() {
        return qcpp;
    }

    public void setQcpp(String qcpp) {
        this.qcpp = qcpp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getFixloss() {
        return fixloss;
    }

    public void setFixloss(BigDecimal fixloss) {
        this.fixloss = fixloss;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClaimImg() {
        return claimImg;
    }

    public void setClaimImg(String claimImg) {
        this.claimImg = claimImg;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCjh() {
        return cjh;
    }

    public void setCjh(String cjh) {
        this.cjh = cjh;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getclaimer() {
        return claimer;
    }

    public void setclaimer(String claimer) {
        this.claimer = claimer;
    }

    public String getClaimerPhone() {
        return claimerPhone;
    }

    public void setClaimerPhone(String claimerPhone) {
        this.claimerPhone = claimerPhone;
    }


    public String getPayBillNo() {
        return payBillNo;
    }

    public void setPayBillNo(String payBillNo) {
        this.payBillNo = payBillNo;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public void setDetailImg(String detailImg) {
        this.detailImg = detailImg;
    }

    public String getInsurer() {
        return insurer;
    }

    public void setInsurer(String insurer) {
        this.insurer = insurer;
    }

    public String getInsurAccess() {
        return insurAccess;
    }

    public void setInsurAccess(String insurAccess) {
        this.insurAccess = insurAccess;
    }

    public String getRescue() {
        return rescue;
    }

    public void setRescue(String rescue) {
        this.rescue = rescue;
    }

    public BigDecimal getRescueFee() {
        return rescueFee;
    }

    public void setRescueFee(BigDecimal rescueFee) {
        this.rescueFee = rescueFee;
    }

    public String getPromise() {
        return promise;
    }

    public void setPromise(String promise) {
        this.promise = promise;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public BigDecimal getPreFee() {
        return preFee;
    }

    public void setPreFee(BigDecimal preFee) {
        this.preFee = preFee;
    }

    public String getPersonHurts() {
        return personHurts;
    }

    public void setPersonHurts(String personHurts) {
        this.personHurts = personHurts;
    }

    public String getGoodsHurts() {
        return goodsHurts;
    }

    public void setGoodsHurts(String goodsHurts) {
        this.goodsHurts = goodsHurts;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getRescueFeeSettleType() {
        return rescueFeeSettleType;
    }

    public void setRescueFeeSettleType(String rescueFeeSettleType) {
        this.rescueFeeSettleType = rescueFeeSettleType;
    }

    public BigDecimal getRebateForCompany() {
        return rebateForCompany;
    }

    public void setRebateForCompany(BigDecimal rebateForCompany) {
        this.rebateForCompany = rebateForCompany;
    }

    public BigDecimal getRebateForEmp() {
        return rebateForEmp;
    }

    public void setRebateForEmp(BigDecimal rebateForEmp) {
        this.rebateForEmp = rebateForEmp;
    }

    public String getPayBillNoForClaim() {
        return payBillNoForClaim;
    }

    public void setPayBillNoForClaim(String payBillNoForClaim) {
        this.payBillNoForClaim = payBillNoForClaim;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankSecondName() {
        return bankSecondName;
    }

    public void setBankSecondName(String bankSecondName) {
        this.bankSecondName = bankSecondName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Integer getHasException() {
        return hasException;
    }

    public void setHasException(Integer hasException) {
        this.hasException = hasException;
    }

    public String getCarcard() {
        return carcard;
    }

    public void setCarcard(String carcard) {
        this.carcard = carcard;
    }

    public String getDrivecard() {
        return drivecard;
    }

    public void setDrivecard(String drivecard) {
        this.drivecard = drivecard;
    }

    public String getConidcard() {
        return conidcard;
    }

    public void setConidcard(String conidcard) {
        this.conidcard = conidcard;
    }

    public String getConbankcard() {
        return conbankcard;
    }

    public void setConbankcard(String conbankcard) {
        this.conbankcard = conbankcard;
    }

    @Override
    public String toString() {
        return "OpenClaim{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", settleAccounts='" + settleAccounts + '\'' +
                ", orderno='" + orderno + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", cph='" + cph + '\'' +
                ", qcpp='" + qcpp + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", deptid=" + deptid +
                ", lng=" + lng +
                ", lat=" + lat +
                ", fixloss=" + fixloss +
                ", address='" + address + '\'' +
                ", claimImg='" + claimImg + '\'' +
                ", detailImg='" + detailImg + '\'' +
                ", modifytime=" + modifytime +
                ", createtime=" + createtime +
                ", cjh='" + cjh + '\'' +
                ", payBillNo='" + payBillNo + '\'' +
                ", payBillNoForClaim='" + payBillNoForClaim + '\'' +
                ", insurer='" + insurer + '\'' +
                ", insurAccess='" + insurAccess + '\'' +
                ", rescue='" + rescue + '\'' +
                ", rescueFee=" + rescueFee +
                ", promise='" + promise + '\'' +
                ", accType='" + accType + '\'' +
                ", preFee=" + preFee +
                ", personHurts='" + personHurts + '\'' +
                ", goodsHurts='" + goodsHurts + '\'' +
                ", settleType='" + settleType + '\'' +
                ", rescueFeeSettleType='" + rescueFeeSettleType + '\'' +
                ", rebateForCompany=" + rebateForCompany +
                ", rebateForEmp=" + rebateForEmp +
                ", hasException=" + hasException +
                ", deptName='" + deptName + '\'' +
                ", claimerPhone='" + claimerPhone + '\'' +
                ", claimer='" + claimer + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", bankUserName='" + bankUserName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankSecondName='" + bankSecondName + '\'' +
                ", idcard='" + idcard + '\'' +
                ", carcard='" + carcard + '\'' +
                ", drivecard='" + drivecard + '\'' +
                ", conidcard='" + conidcard + '\'' +
                ", conbankcard='" + conbankcard + '\'' +
                '}';
    }
}
