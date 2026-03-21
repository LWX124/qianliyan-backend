package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.WxPayProperties;
import com.stylefeng.guns.modular.system.constant.WxPayOrderStatus;
import com.stylefeng.guns.modular.system.model.BizWxpayOrder;
import com.stylefeng.guns.modular.system.service.IBizWxpayOrderService;
import com.stylefeng.guns.wxpay.MyConfig;
import com.stylefeng.guns.wxpay.WXPay;
import com.stylefeng.guns.wxpay.WXPayUtil;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付结果回调
 *
 * @author kosans
 */
@RestController
public class WxPayNotifyController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WxPayProperties wxPayProperties;

    @Autowired
    IBizWxpayOrderService bizWxpayOrderService;

    private final String SUCCESS = "SUCCESS";

    @ApiOperation(value = "微信支付结果回调", notes = "微信支付结果回调")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/api/v1/wxpay/notify", method = RequestMethod.POST, produces = "application/json")
    public void wxpayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = response.getWriter();
        Map<String, String> return_data = new HashMap<>();
        try {
            ServletInputStream instream = request.getInputStream();
            StringBuilder sb = new StringBuilder();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = instream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            instream.close();
            log.info("接收微信支付通知信息：" + sb.toString());
            //接受微信的通知参数
            Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());
            MyConfig myConfig = new MyConfig(wxPayProperties.getAppid(), wxPayProperties.getMchid(), wxPayProperties.getKey(), wxPayProperties.getCertPath());
            WXPay wxpay = new WXPay(myConfig);
            boolean signFlag = wxpay.isPayResultNotifySignatureValid(map);
            if (signFlag) {
                if (map.get("return_code").equals(SUCCESS) && map.get("result_code").equals(SUCCESS)) {
                    BizWxpayOrder obj = bizWxpayOrderService.selectByOutTradeNo(map.get("out_trade_no"));
                    //根据状态判断重复通知
                    if (obj.getStatus() == WxPayOrderStatus.PAY_SUCCESS.getCode()) {
                        log.info("###重复通知  prepay_id={} ###", map.get("prepay_id"));
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                        writer.write(WXPayUtil.mapToXml(return_data));
                        return;
                    }
                    if (bizWxpayOrderService.dealNotifyBusiness(map)) {
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                        writer.write(WXPayUtil.mapToXml(return_data));
                        return;
                    }
                }
            }
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "信息不匹配");
            writer.write(WXPayUtil.mapToXml(return_data));
        } finally {
            writer.close();
        }
    }

}
