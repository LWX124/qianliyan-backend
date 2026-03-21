package com.stylefeng.guns;

import com.stylefeng.guns.huanxin.HuanXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * SpringBoot方式启动类
 *
 * @author kosans
 * @Date 2018/7/18 12:06
 */
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class GunsApplication {
    @Autowired
    private RestTemplateBuilder builder;


    private final static Logger logger = LoggerFactory.getLogger(GunsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GunsApplication.class, args);
        logger.info("GunsApplication is success!");
    }

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
//    @Bean
//    public Connector connector(){
//
//        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//
//        connector.setScheme("http");
//
//        connector.setPort(8080);
//
//        connector.setSecure(false);
//
//        connector.setRedirectPort(8443);
//
//        return connector;
//
//    }
//
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
//
//        TomcatServletWebServerFactory tomcat =new TomcatServletWebServerFactory(){
//
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint=new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection=new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(connector());
//        return tomcat;
//    }
}
