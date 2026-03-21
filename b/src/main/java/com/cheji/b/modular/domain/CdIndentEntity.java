package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("cd_indent")
public class CdIndentEntity extends Model<CdIndentEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    @TableField("indent_number")
    private String indentNumber;

    /**
     * 车牌号
     */
    private String plate;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Integer brandId;

    /**
     * 车牌照片
     */
    @TableField("plate_img")
    private String plateImg;

    /**
     * 车架照片
     */
    @TableField("frame_img")
    private String frameImg;

    /**
     * 客户姓名
     */
    private String username;

    /**
     * 电话号码
     */
    private String phone;

    private Integer state;

    /**
     * 维修方案
     */
    @TableField("maint_plan")
    private String maintPlan;

    /**
     * 车辆保险
     */
    @TableField("car_ins")
    private String carIns;

    /**
     * 业务来源
     */
    @TableField("busines_source")
    private String businesSource;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @TableField("waixiu_amount")
    private BigDecimal waixiuAmount;


    @TableField("qita_amount")
    private BigDecimal qitaAmount;

    /**
     * 客户返点
     */
    @TableField("customer_rebates")
    private BigDecimal customerRebates;

    /**
     * 返点金额
     */
    @TableField("customer_money")
    private BigDecimal customerMoney;

    /**
     * 是否支付客户返点
     */
    @TableField("customer_pay")
    private String customerPay;

    /**
     * 业务佣金
     */
    @TableField("business_commission")
    private BigDecimal businessCommission;

    /**
     * 业务佣金金额
     */
    @TableField("business_money")
    private BigDecimal businessMoney;

    /**
     * 是否结算业务佣金
     */
    @TableField("business_pay")
    private String businessPay;

    /**
     * 施救费用
     */
    @TableField("rescue_cost")
    
    private BigDecimal rescueCost;

    /**
     * 车主嘱咐
     */
    private String remark;

    /**
     * 维修时间
     */
    @TableField("work_time")
    private Integer workTime;

    /**
     * 维修报价
     */
    @TableField("repair_quotation")
    private BigDecimal repairQuotation;

    /**
     * 维修金额
     */
    @TableField("repair_money")
    private BigDecimal repairMoney;

    /**
     * 机修工单
     */
    @TableField("mechanic_id")
    private Integer mechanicId;

    /**
     * 钣金工单
     */
    @TableField("metal_id")
    private Integer metalId;

    /**
     * 喷漆工单
     */
    @TableField("spray_id")
    private Integer sprayId;

    /**
     * 配件金额
     */
    @TableField("accessories_amount")
    private BigDecimal accessoriesAmount;

    /**
     * 是否支付配件金额
     */
    @TableField("pay_acc_amount")
    private Integer payAccAmount;

    /**
     * 财务收款
     */
    private BigDecimal payment;

    /**
     * 定损返点
     */
    @TableField("fee_rebates")
    private BigDecimal feeRebates;

    /**
     * 定损金额
     */
    @TableField("fee_money")
    private BigDecimal feeMoney;

    /**
     * 是否支付定损返点
     */
    @TableField("fee_pay")
    private String feePay;

    /**
     * 外定费用
     */
    @TableField("wai_rebates")
    private BigDecimal waiRebates;

    /**
     * 外定金额
     */
    @TableField("wai_amount")
    private BigDecimal waiAmount;

    /**
     * 是否支付外定金额
     */
    @TableField("wai_pay")
    private String waiPay;


    public BigDecimal getWaixiuAmount() {
        return waixiuAmount;
    }

    public void setWaixiuAmount(BigDecimal waixiuAmount) {
        this.waixiuAmount = waixiuAmount;
    }

    public BigDecimal getQitaAmount() {
        return qitaAmount;
    }

    public void setQitaAmount(BigDecimal qitaAmount) {
        this.qitaAmount = qitaAmount;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() {
        return id;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getPlateImg() {
        return plateImg;
    }

    public void setPlateImg(String plateImg) {
        this.plateImg = plateImg;
    }

    public String getFrameImg() {
        return frameImg;
    }

    public void setFrameImg(String frameImg) {
        this.frameImg = frameImg;
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

    public String getMaintPlan() {
        return maintPlan;
    }

    public void setMaintPlan(String maintPlan) {
        this.maintPlan = maintPlan;
    }

    public String getCarIns() {
        return carIns;
    }

    public void setCarIns(String carIns) {
        this.carIns = carIns;
    }

    public String getBusinesSource() {
        return businesSource;
    }

    public void setBusinesSource(String businesSource) {
        this.businesSource = businesSource;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getCustomerRebates() {
        return customerRebates;
    }

    public void setCustomerRebates(BigDecimal customerRebates) {
        this.customerRebates = customerRebates;
    }

    public BigDecimal getCustomerMoney() {
        return customerMoney;
    }

    public void setCustomerMoney(BigDecimal customerMoney) {
        this.customerMoney = customerMoney;
    }

    public String getCustomerPay() {
        return customerPay;
    }

    public void setCustomerPay(String customerPay) {
        this.customerPay = customerPay;
    }

    public BigDecimal getBusinessCommission() {
        return businessCommission;
    }

    public void setBusinessCommission(BigDecimal businessCommission) {
        this.businessCommission = businessCommission;
    }

    public BigDecimal getBusinessMoney() {
        return businessMoney;
    }

    public void setBusinessMoney(BigDecimal businessMoney) {
        this.businessMoney = businessMoney;
    }

    public String getBusinessPay() {
        return businessPay;
    }

    public void setBusinessPay(String businessPay) {
        this.businessPay = businessPay;
    }

    public BigDecimal getRescueCost() {
        return rescueCost;
    }

    public void setRescueCost(BigDecimal rescueCost) {
        this.rescueCost = rescueCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public BigDecimal getRepairQuotation() {
        return repairQuotation;
    }

    public void setRepairQuotation(BigDecimal repairQuotation) {
        this.repairQuotation = repairQuotation;
    }

    public BigDecimal getRepairMoney() {
        return repairMoney;
    }

    public void setRepairMoney(BigDecimal repairMoney) {
        this.repairMoney = repairMoney;
    }

    public Integer getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Integer mechanicId) {
        this.mechanicId = mechanicId;
    }

    public Integer getMetalId() {
        return metalId;
    }

    public void setMetalId(Integer metalId) {
        this.metalId = metalId;
    }

    public Integer getSprayId() {
        return sprayId;
    }

    public void setSprayId(Integer sprayId) {
        this.sprayId = sprayId;
    }

    public BigDecimal getAccessoriesAmount() {
        return accessoriesAmount;
    }

    public void setAccessoriesAmount(BigDecimal accessoriesAmount) {
        this.accessoriesAmount = accessoriesAmount;
    }

    public Integer getPayAccAmount() {
        return payAccAmount;
    }

    public void setPayAccAmount(Integer payAccAmount) {
        this.payAccAmount = payAccAmount;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getFeeRebates() {
        return feeRebates;
    }

    public void setFeeRebates(BigDecimal feeRebates) {
        this.feeRebates = feeRebates;
    }

    public BigDecimal getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(BigDecimal feeMoney) {
        this.feeMoney = feeMoney;
    }

    public String getFeePay() {
        return feePay;
    }

    public void setFeePay(String feePay) {
        this.feePay = feePay;
    }

    public BigDecimal getWaiRebates() {
        return waiRebates;
    }

    public void setWaiRebates(BigDecimal waiRebates) {
        this.waiRebates = waiRebates;
    }

    public BigDecimal getWaiAmount() {
        return waiAmount;
    }

    public void setWaiAmount(BigDecimal waiAmount) {
        this.waiAmount = waiAmount;
    }

    public String getWaiPay() {
        return waiPay;
    }

    public void setWaiPay(String waiPay) {
        this.waiPay = waiPay;
    }
}
