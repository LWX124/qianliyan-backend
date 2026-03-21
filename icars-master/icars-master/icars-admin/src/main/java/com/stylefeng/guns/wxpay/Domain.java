package com.stylefeng.guns.wxpay;

public class Domain implements IWXPayDomain{

    public void report(String domain, long elapsedTimeMillis, Exception ex) {

    }

    public DomainInfo getDomain(WXPayConfig config) {
        IWXPayDomain.DomainInfo info = new IWXPayDomain.DomainInfo("api.mch.weixin.qq.com",true);
        return info;
    }
}
