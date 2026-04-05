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
                .excludePathPatterns("/user/wxMiniLogin");
    }
}
