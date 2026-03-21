/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 查找微信 订单结果Entity
 * @author dh
 * @version 2019-09-04
 */
@Table(name="app_wx_cash_out_result", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="return_code", attrName="returnCode", label="SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否成功需要查看result_code来判断"),
		@Column(name="return_msg", attrName="returnMsg", label="返回信息，如非空，为错误原因  签名失败  参数格式校验错误"),
		@Column(name="result_code", attrName="resultCode", label="SUCCESS/FAIL，非付款标识，付款是否成功需要查看status字段来判断"),
		@Column(name="err_code", attrName="errCode", label="错误码信息"),
		@Column(name="err_code_des", attrName="errCodeDes", label="结果信息描述"),
		@Column(name="mch_id", attrName="mchId", label="商户号"),
		@Column(name="partner_trade_no", attrName="partnerTradeNo", label="商户单号"),
		@Column(name="payment_no", attrName="paymentNo", label="1"),
		@Column(name="bank_no_md5", attrName="bankNoMd5", label="1"),
		@Column(name="true_name_md5", attrName="trueNameMd5", label="1", queryType=QueryType.LIKE),
		@Column(name="amount", attrName="amount", label="1"),
		@Column(name="status", attrName="status", label="1", isUpdate=false),
		@Column(name="cmms_amt", attrName="cmmsAmt", label="1"),
		@Column(name="create_time", attrName="createTime", label="1"),
		@Column(name="pay_succ_time", attrName="paySuccTime", label="1"),
		@Column(name="reason", attrName="reason", label="1"),
		@Column(name="cre_time", attrName="creTime", label="1"),
		@Column(name="req_xml", attrName="reqXml", label="1"),
	}, orderBy="a.id DESC"
)
public class AppWxCashOutResult extends DataEntity<AppWxCashOutResult> {
	
	private static final long serialVersionUID = 1L;
	private String returnCode;		// SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否成功需要查看result_code来判断
	private String returnMsg;		// 返回信息，如非空，为错误原因  签名失败  参数格式校验错误
	private String resultCode;		// SUCCESS/FAIL，非付款标识，付款是否成功需要查看status字段来判断
	private String errCode;		// 错误码信息
	private String errCodeDes;		// 结果信息描述
	private String mchId;		// 商户号
	private String partnerTradeNo;		// 商户单号
	private String paymentNo;		// 1
	private String bankNoMd5;		// 1
	private String trueNameMd5;		// 1
	private Long amount;		// 1
	private Long cmmsAmt;		// 1
	private String createTime;		// 1
	private String paySuccTime;		// 1
	private String reason;		// 1
	private String reqXml;		// 1
	private Date creTime;		// 1

	public String getReqXml() {
		return reqXml;
	}

	public void setReqXml(String reqXml) {
		this.reqXml = reqXml;
	}

	public AppWxCashOutResult() {
		this(null);
	}

	public AppWxCashOutResult(String id){
		super(id);
	}
	
	@Length(min=0, max=16, message="SUCCESS/FAIL 此字段是通信标识，非付款标识，付款是否成功需要查看result_code来判断长度不能超过 16 个字符")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	@Length(min=0, max=128, message="返回信息，如非空，为错误原因  签名失败  参数格式校验错误长度不能超过 128 个字符")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	@Length(min=0, max=16, message="SUCCESS/FAIL，非付款标识，付款是否成功需要查看status字段来判断长度不能超过 16 个字符")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	@Length(min=0, max=32, message="错误码信息长度不能超过 32 个字符")
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	@Length(min=0, max=128, message="结果信息描述长度不能超过 128 个字符")
	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	
	@Length(min=0, max=32, message="商户号长度不能超过 32 个字符")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	
	@Length(min=0, max=32, message="商户单号长度不能超过 32 个字符")
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
	
	@Length(min=0, max=64, message="1长度不能超过 64 个字符")
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	
	@Length(min=0, max=32, message="1长度不能超过 32 个字符")
	public String getBankNoMd5() {
		return bankNoMd5;
	}

	public void setBankNoMd5(String bankNoMd5) {
		this.bankNoMd5 = bankNoMd5;
	}
	
	@Length(min=0, max=32, message="1长度不能超过 32 个字符")
	public String getTrueNameMd5() {
		return trueNameMd5;
	}

	public void setTrueNameMd5(String trueNameMd5) {
		this.trueNameMd5 = trueNameMd5;
	}
	
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	public Long getCmmsAmt() {
		return cmmsAmt;
	}

	public void setCmmsAmt(Long cmmsAmt) {
		this.cmmsAmt = cmmsAmt;
	}
	
	@Length(min=0, max=32, message="1长度不能超过 32 个字符")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=32, message="1长度不能超过 32 个字符")
	public String getPaySuccTime() {
		return paySuccTime;
	}

	public void setPaySuccTime(String paySuccTime) {
		this.paySuccTime = paySuccTime;
	}
	
	@Length(min=0, max=128, message="1长度不能超过 128 个字符")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreTime() {
		return creTime;
	}

	public void setCreTime(Date creTime) {
		this.creTime = creTime;
	}
	
}