package com.cheji.b.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WxPayNotifyPojo {

    @XStreamAlias("return_code")
    private String returnCode;

    @XStreamAlias("return_msg")
    private String returnMsg;

    @Override
    public String toString() {
        return "WxPayNotifyPojo{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                '}';
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
}
