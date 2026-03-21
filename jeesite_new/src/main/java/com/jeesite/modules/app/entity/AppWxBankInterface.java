/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.util.Date;

/**
 * app_wx_bank_interfaceEntity
 * @author dh
 * @version 2019-08-30
 */
@Table(name="app_wx_bank_interface", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="return_code", attrName="returnCode", label="SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否成功需要查看result_code来判断"),
		@Column(name="return_msg", attrName="returnMsg", label="返回信息，如非空，为错误原因"),
		@Column(name="result_code", attrName="resultCode", label="SUCCESS/FAIL，注意", comment="SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况，所以如果状态为FAIL，请务必通过查询接口确认此次付款的结果（关注错误码err_code字段）。如果要继续进行这笔付款，请务必用原商户订单号和原参数来重入此接口"),
		@Column(name="err_code", attrName="errCode", label="错误码信息，注意：出现未明确的错误码时，如（SYSTEMERROR）等，请务必用原商户订单号重试，或通过查询接口确认此次付款的结果"),
		@Column(name="err_code_des", attrName="errCodeDes", label="错误信息描述"),
		@Column(name="mch_id", attrName="mchId", label="微信支付分配的商户号"),
		@Column(name="partner_trade_no", attrName="partnerTradeNo", label="商户订单号，需要保持唯一"),
		@Column(name="parent_partner_trade_no", attrName="parentPartnerTradeNo", label="父级订单号。  如果出现账户余额不足等情况需要发送第二次提现请求，会记录上一次订单号"),
		@Column(name="amount", attrName="amount", label="代付金额"),
		@Column(name="nonce_str", attrName="nonceStr", label="随机字符串，长度小于32位"),
		@Column(name="sign", attrName="sign", label="返回包携带签名给商户"),
		@Column(name="payment_no", attrName="paymentNo", label="代付成功后，返回的内部业务单号"),
		@Column(name="cmms_amt", attrName="cmmsAmt", label="手续费金额"),
		@Column(name="paramer_xml", attrName="paramerXml", label="请求入参"),
		@Column(name="create_time", attrName="createTime", label="创建时间"),
		@Column(name="ops_flag", attrName="opsFlag", label="当 result_code=fail时   定时任务是否已经处理  1:未处理  2:已处理"),
	}, orderBy="a.id DESC"
)
public class AppWxBankInterface extends DataEntity<AppWxBankInterface> {
	
	private static final long serialVersionUID = 1L;
	private String returnCode;		// SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否
	private String returnMsg;		// 返回信息，
	private String resultCode;		// SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况，所以如果状
	private String errCode;		// 错误码信
	private String errCodeDes;		// 错误信息
	private String mchId;		// 微信支付
	private String partnerTradeNo;		// 商户订单号
	private String parentPartnerTradeNo;		// 父级订单号。  如果出现账户余额不足等情况需要发送第二次提现请求，会记录上一次订单号
	private String amount;		// 代付金额
	private String nonceStr;		// 随机字符
	private String sign;		// 返回包携
	private String paymentNo;		// 代付成功
	private String cmmsAmt;		// 手续费金额
	private String paramerXml;		// 请求入参
	private Date createTime;

	private Integer opsFlag;//当 result_code=fail时   定时任务是否已经处理  1:未处理  2:已处理

	public String getParentPartnerTradeNo() {
		return parentPartnerTradeNo;
	}

	public void setParentPartnerTradeNo(String parentPartnerTradeNo) {
		this.parentPartnerTradeNo = parentPartnerTradeNo;
	}

	public Integer getOpsFlag() {
		return opsFlag;
	}

	public void setOpsFlag(Integer opsFlag) {
		this.opsFlag = opsFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public AppWxBankInterface() {
		this(null);
	}

	public AppWxBankInterface(String id){
		super(id);
	}
	
	@Length(min=0, max=16, message="SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否长度不能超过 16 个字符")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	@Length(min=0, max=128, message="返回信息，长度不能超过 128 个字符")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	@Length(min=0, max=32, message="SUCCESS/FAIL，注意长度不能超过 32 个字符")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	@Length(min=0, max=32, message="错误码信长度不能超过 32 个字符")
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	@Length(min=0, max=128, message="错误信息长度不能超过 128 个字符")
	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	
	@Length(min=0, max=32, message="微信支付长度不能超过 32 个字符")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	
	@Length(min=0, max=32, message="商户订单号长度不能超过 32 个字符")
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
	

	
	@Length(min=0, max=32, message="随机字符长度不能超过 32 个字符")
	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
	@Length(min=0, max=32, message="返回包携长度不能超过 32 个字符")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	@Length(min=0, max=64, message="代付成功长度不能超过 64 个字符")
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCmmsAmt() {
		return cmmsAmt;
	}

	public void setCmmsAmt(String cmmsAmt) {
		this.cmmsAmt = cmmsAmt;
	}

	@Length(min=0, max=500, message="请求入参长度不能超过 500 个字符")
	public String getParamerXml() {
		return paramerXml;
	}

	public void setParamerXml(String paramerXml) {
		this.paramerXml = paramerXml;
	}
	
}