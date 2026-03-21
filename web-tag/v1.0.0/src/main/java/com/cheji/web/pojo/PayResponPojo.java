package com.cheji.web.pojo;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;

public class PayResponPojo {
    private WxPayAppOrderResult wxPayAppOrderResult;
    private String outTradeNo;
    private String CleanIndentNumber;       //洗车订单编号
    private String isFreeWash;          //是否免费洗车

    public String getIsFreeWash() {
        return isFreeWash;
    }

    public void setIsFreeWash(String isFreeWash) {
        this.isFreeWash = isFreeWash;
    }

    public WxPayAppOrderResult getWxPayAppOrderResult() {
        return wxPayAppOrderResult;
    }

    public void setWxPayAppOrderResult(WxPayAppOrderResult wxPayAppOrderResult) {
        this.wxPayAppOrderResult = wxPayAppOrderResult;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCleanIndentNumber() {
        return CleanIndentNumber;
    }

    public void setCleanIndentNumber(String cleanIndentNumber) {
        CleanIndentNumber = cleanIndentNumber;
    }

    @Override
    public String toString() {
        return "PayResponPojo{" +
                "wxPayAppOrderResult=" + wxPayAppOrderResult +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", CleanIndentNumber='" + CleanIndentNumber + '\'' +
                ", isFreeWash='" + isFreeWash + '\'' +
                '}';
    }
}
