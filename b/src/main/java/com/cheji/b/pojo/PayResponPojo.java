package com.cheji.b.pojo;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;

public class PayResponPojo {
    private WxPayAppOrderResult wxPayAppOrderResult;
    private String outTradeNo;

    @Override
    public String toString() {
        return "PayResponPojo{" +
                "wxPayAppOrderResult=" + wxPayAppOrderResult +
                ", outTradeNo='" + outTradeNo + '\'' +
                '}';
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
}
