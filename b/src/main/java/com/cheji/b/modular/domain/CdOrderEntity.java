package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 车电订单表
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cd_order")
public class CdOrderEntity extends Model<CdOrderEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("indent_number")
    private String indentNumber;  //'订单编号'
    private String plate;  //'车牌号',
    @TableField("brand_id")
    private Integer brandId; //'品牌id',
    private  String username;  //'客户姓名',
    private String  phone ;//'联系电话',
    @TableField("accident_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private  Date accidentTime  ;//'事故时间',
    @TableField("accident_liability")
    private String  accidentLiability;  //'事故责任',
    @TableField("busines_source")
    private String  businesSource;  //'业务来源',
    private  Integer state ; //'订单状态，1.新订单。2.报价中。3.采配件。4.维修中。5.交车中。6.收款中。7.报费用。8.毛利润',
    @TableField("accident_type")
    private  Integer accidentType ;// '事故类型, 1.酒驾,2.保险,3.自费',
    @TableField("client_will")
    private  String clientWill; // '客户意向',
    private  String remark; // '情况备注',
    private  BigDecimal carmodel; // '车款支付',
    @TableField("car_ins")
    private  String carIns; // '保险公司',
    @TableField("chesun_coverage")
    private  BigDecimal chesunCoverage; // '车损保额',
    @TableField("estimated_amount")
    private BigDecimal  estimatedAmount;  //'预估维修',
    @TableField("treatment_cost")
    private  BigDecimal treatmentCost;  // '处理成本',
    @TableField("car_prices")
    private BigDecimal  carPrices ; //'购车价格',
    @TableField("used_prices")
    private BigDecimal  usedPrices; // '二手车价',
    @TableField("salvage_prices")
    private  BigDecimal salvagePrices ; //'残值价格',
    @TableField("cost_upkeep")
    private BigDecimal  costUpkeep ; //'维修成本',
    @TableField("car_profits")
    private BigDecimal  carProfits ; //'购车利润',
    @TableField("final_payment")
    private  BigDecimal finalPayment; // '尾款支付',
    @TableField("referral_fee")
    private BigDecimal  referralFee; // '介绍费用',
    @TableField("trucking_fee")
    private BigDecimal  truckingFee ; //'拖车费用',
    @TableField("handing_expense")
    private  BigDecimal handingExpense; // '处理费用',
    @TableField("other_expense")
    private  BigDecimal otherExpense; // '其他费用',
    @TableField("models_account")
    private  BigDecimal modelsAccount; // '车款到账',

    public BigDecimal getModelsAccount() {
        return modelsAccount;
    }

    public void setModelsAccount(BigDecimal modelsAccount) {
        this.modelsAccount = modelsAccount;
    }

    @TableField("create_time")
    private Date createTime; // '创建时间',

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public BigDecimal getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(BigDecimal carmodel) {
        this.carmodel = carmodel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndentNumber() {
        return indentNumber;
    }

    public void setIndentNumber(String indentNumber) {
        this.indentNumber = indentNumber;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getAccidentTime() {
        return accidentTime;
    }

    public void setAccidentTime(Date accidentTime) {
        this.accidentTime = accidentTime;
    }

    public String getAccidentLiability() {
        return accidentLiability;
    }

    public void setAccidentLiability(String accidentLiability) {
        this.accidentLiability = accidentLiability;
    }

    public String getBusinesSource() {
        return businesSource;
    }

    public void setBusinesSource(String businesSource) {
        this.businesSource = businesSource;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(Integer accidentType) {
        this.accidentType = accidentType;
    }

    public String getClientWill() {
        return clientWill;
    }

    public void setClientWill(String clientWill) {
        this.clientWill = clientWill;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCarIns() {
        return carIns;
    }

    public void setCarIns(String carIns) {
        this.carIns = carIns;
    }

    public BigDecimal getChesunCoverage() {
        return chesunCoverage;
    }

    public void setChesunCoverage(BigDecimal chesunCoverage) {
        this.chesunCoverage = chesunCoverage;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public BigDecimal getCarPrices() {
        return carPrices;
    }

    public void setCarPrices(BigDecimal carPrices) {
        this.carPrices = carPrices;
    }

    public BigDecimal getUsedPrices() {
        return usedPrices;
    }

    public void setUsedPrices(BigDecimal usedPrices) {
        this.usedPrices = usedPrices;
    }

    public BigDecimal getSalvagePrices() {
        return salvagePrices;
    }

    public void setSalvagePrices(BigDecimal salvagePrices) {
        this.salvagePrices = salvagePrices;
    }

    public BigDecimal getCostUpkeep() {
        return costUpkeep;
    }

    public void setCostUpkeep(BigDecimal costUpkeep) {
        this.costUpkeep = costUpkeep;
    }

    public BigDecimal getCarProfits() {
        return carProfits;
    }

    public void setCarProfits(BigDecimal carProfits) {
        this.carProfits = carProfits;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public BigDecimal getReferralFee() {
        return referralFee;
    }

    public void setReferralFee(BigDecimal referralFee) {
        this.referralFee = referralFee;
    }

    public BigDecimal getTruckingFee() {
        return truckingFee;
    }

    public void setTruckingFee(BigDecimal truckingFee) {
        this.truckingFee = truckingFee;
    }

    public BigDecimal getHandingExpense() {
        return handingExpense;
    }

    public void setHandingExpense(BigDecimal handingExpense) {
        this.handingExpense = handingExpense;
    }

    public BigDecimal getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(BigDecimal otherExpense) {
        this.otherExpense = otherExpense;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
