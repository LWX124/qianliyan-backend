package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 微信企业付款到零钱红包支付明细表
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
@TableName("biz_wx_pay_record")
public class BizWxPayRecord extends Model<BizWxPayRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 支付主表id
     */
    @TableField("bill_id")
    private Integer billId;
    /**
     * 微信用户对应小程序openid
     */
    private String openid;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * SUCCESS/FAIL，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    @TableField("return_code")
    private String returnCode;
    /**
     * 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
     */
    @TableField("return_msg")
    private String returnMsg;
    /**
     * SUCCESS/FAIL，业务结果
     */
    @TableField("result_code")
    private String resultCode;
    /**
     * 商户appid
     */
    @TableField("mch_appid")
    private String mchAppid;
    /**
     * 商户号
     */
    private String mchid;
    /**
     * 设备号
     */
    @TableField("device_info")
    private String deviceInfo;
    /**
     * 随机字符串
     */
    @TableField("nonce_str")
    private String nonceStr;
    /**
     * 错误代码
     */
    @TableField("err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @TableField("err_code_des")
    private String errCodeDes;
    /**
     * 商户订单号
     */
    @TableField("partner_trade_no")
    private String partnerTradeNo;
    /**
     * 微信订单号
     */
    @TableField("payment_no")
    private String paymentNo;
    /**
     * 微信支付成功时间
     */
    @TableField("payment_time")
    private Date paymentTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMchAppid() {
        return mchAppid;
    }

    public void setMchAppid(String mchAppid) {
        this.mchAppid = mchAppid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizWxPayRecord{" +
        "id=" + id +
        ", billId=" + billId +
        ", openid=" + openid +
        ", amount=" + amount +
        ", returnCode=" + returnCode +
        ", returnMsg=" + returnMsg +
        ", resultCode=" + resultCode +
        ", mchAppid=" + mchAppid +
        ", mchid=" + mchid +
        ", deviceInfo=" + deviceInfo +
        ", nonceStr=" + nonceStr +
        ", errCode=" + errCode +
        ", errCodeDes=" + errCodeDes +
        ", partnerTradeNo=" + partnerTradeNo +
        ", paymentNo=" + paymentNo +
        ", paymentTime=" + paymentTime +
        ", createTime=" + createTime +
        "}";
    }
}
