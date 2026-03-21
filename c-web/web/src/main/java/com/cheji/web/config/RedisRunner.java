//package com.cheji.web.config;
//
//import com.cheji.web.modular.service.RedisService;
//import javax.annotation.Resource;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class RedisRunner implements CommandLineRunner {
//    @Resource
//    private RedisService redisService;
//
//    @Resource
//    private WbUserMapper wbUserMapper;
//
//    @Resource
//    private BloomFilterHelper bloomFilterHelper;
//
//    @Override
//    public void run(String... args) throws Exception {
//        log.info("**** RedisRunner ****");
//        List<WbUser> wbUsers = wbUserMapper.selectListForBloom();
//        // 初始化布隆过滤器内容
//        for (WbUser user : wbUsers) {
//            redisService.addByBloomFilter(bloomFilterHelper, "bloom", user.getName());
//        }
//    }
//}
