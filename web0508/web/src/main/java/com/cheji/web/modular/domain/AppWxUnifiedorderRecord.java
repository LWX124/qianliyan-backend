package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("app_wx_unifiedorder_record")
public class AppWxUnifiedorderRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String body;
    private String outTradeNo;
    private Integer totalFee;
    private String spbillCreateIp;
    private String notifyUrl;
    private String tradeType;
    private Integer source;
    private Integer userId;
    private String returnCode;
    private String returnMsg;
    private String wxNonceStr;
    private String wxSign;
    private String resultCode;
    private String errCode;
    private String errCodeDes;
    private String wxTradeType;
    private String prepayId;

    @Override
    public String toString() {
        return "AppWxUnifiedorderRecord{" +
                "id=" + id +
                ", appid='" + appid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                ", body='" + body + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", totalFee=" + totalFee +
                ", spbillCreateIp='" + spbillCreateIp + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", source=" + source +
                ", userId=" + userId +
                ", returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", wxNonceStr='" + wxNonceStr + '\'' +
                ", wxSign='" + wxSign + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errCodeDes='" + errCodeDes + '\'' +
                ", wxTradeType='" + wxTradeType + '\'' +
                ", prepayId='" + prepayId + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getWxNonceStr() {
        return wxNonceStr;
    }

    public void setWxNonceStr(String wxNonceStr) {
        this.wxNonceStr = wxNonceStr;
    }

    public String getWxSign() {
        return wxSign;
    }

    public void setWxSign(String wxSign) {
        this.wxSign = wxSign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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

    public String getWxTradeType() {
        return wxTradeType;
    }

    public void setWxTradeType(String wxTradeType) {
        this.wxTradeType = wxTradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
