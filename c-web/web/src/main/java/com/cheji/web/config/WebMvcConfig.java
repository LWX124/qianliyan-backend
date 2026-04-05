package com.cheji.web.config;

import com.cheji.web.interceptor.SourceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SourceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 小程序登录（从 body 读取 source）
                        "/user/wxMiniLogin",
                        // 非小程序登录/注册接口（无 X-Source）
                        "/user/loginForPass",
                        "/user/loginForCode",
                        "/user/getRegisterCode",
                        "/user/getLoginCode",
                        "/user/register",
                        "/user/wxAccessToken",
                        "/user/forgetUpdatePass",
                        "/user/bindPhone",
                        // 微信支付回调（微信服务器发起，无 X-Source）
                        "/auction/wxPay/payLogNotify",
                        "/auction/wxPay/bailLogNotify",
                        "/auction/wxPay/onePriceNotify",
                        // 基础设施
                        "/actuator/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/error"
                );
    }
}
