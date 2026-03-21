package com.cheji.b;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
@EnableAsync
@MapperScan("com.cheji.b.modular.mapper")
public class BApplication {

    public static void main(String[] args) {
        SpringApplication.run(BApplication.class, args);
    }

}
