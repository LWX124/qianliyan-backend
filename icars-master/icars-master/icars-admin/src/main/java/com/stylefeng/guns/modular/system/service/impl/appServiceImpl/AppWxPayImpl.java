package com.stylefeng.guns.modular.system.service.impl.appServiceImpl;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AppWxPayImpl {

    private final String body = "车己汽车-app信息费预存款充值";

    @Resource
    private WxPayService wxService;

    /**
     * 支付回调通知地址
     */
    @Value("${spring.wxpay.notify-url}")
    private String notifyUrl;

    public WxPayAppOrderResult unifiedOrder(BigDecimal amount, String outTradeNo, String ipAddress) throws WxPayException {
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(body);
        orderRequest.setOutTradeNo(outTradeNo);
        //元转成分
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(amount.toString()));
        orderRequest.setSpbillCreateIp(ipAddress);
        orderRequest.setTradeType("APP");
        orderRequest.setNotifyUrl(notifyUrl);

        WxPayAppOrderResult order = wxService.createOrder(orderRequest);
        return order;
    }

    /**
     * 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知 主动向微信查询
     * 订单号
     * @param outTradeNo
     * @return
     * @throws WxPayException
     */
    public WxPayOrderQueryResult queryOrder(String outTradeNo) throws WxPayException {
        return wxService.queryOrder(null, outTradeNo);
    }


}
