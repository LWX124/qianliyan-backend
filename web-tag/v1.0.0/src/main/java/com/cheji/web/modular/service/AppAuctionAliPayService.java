package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.cheji.web.exception.GlobalCustomException;
import com.cheji.web.modular.domain.AppAuctionAlipayInfoEntity;
import com.cheji.web.pojo.TokenPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *     ali支付
 * </p>
 *
 * @author yang
 */
@Service
@Slf4j
public class AppAuctionAliPayService  {

    @Autowired
    private AppAuctionAlipayInfoService appAuctionAlipayInfoService;

    public JSONObject createVipOrder(JSONObject result, String vipLv, HttpServletRequest httpServletRequest, TokenPojo currentLoginUser) {
        List<AppAuctionAlipayInfoEntity> appAuctionAlipayInfoEntities = appAuctionAlipayInfoService.selectList(null);
        if(appAuctionAlipayInfoEntities.size() < 1){
            result.put("code",405);
            result.put("msg","配置异常");
            return result;
        }
        Factory.setOptions(getOptions(appAuctionAlipayInfoEntities.get(0)));
        try {
            // 2. 发起API调用-电脑网站支付
            AlipayTradePagePayResponse response = Factory.Payment.Page()
                    .pay("",   //标题
                            "payOrder.getOrderNo()", //订单号
                            "payOrder.getAmount().toString()",  //金额
                            "dto.getCallUrl()"); //支付成功后的跳转页面(我的页面)

            //处理响应或异常
            if (ResponseChecker.success(response)) {

                //* 5.把返回的html表单交给页面
                result.put("code",200);
                result.put("msg","成功！");
                result.put("data",response.getBody());
                return result;
            } else {
                log.error("支付申请失败 {}" , response.getBody());
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new GlobalCustomException(e.getMessage());
        }
        return null;
    }

    //支付宝全局配置
    private Config getOptions(AppAuctionAlipayInfoEntity alipayInfo) {
        Config config = new Config();
        //协议
        config.protocol = alipayInfo.getProtocol();
        //网关地址
        config.gatewayHost = alipayInfo.getGatewayHost();
        //签名协议
        config.signType = alipayInfo.getSignType() ;
        //应用ID
        config.appId = alipayInfo.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayInfo.getMerchantPrivateKey();
        // 阿里公钥
        config.alipayPublicKey = alipayInfo.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayInfo.getNotifyUrl();
        return config;
    }
}
