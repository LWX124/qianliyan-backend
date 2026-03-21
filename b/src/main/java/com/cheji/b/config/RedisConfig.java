package com.cheji.b.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Resource
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit() {
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

//    /**
//     * <注册BloomFilterHelper>
//     *
//     * @param
//     * @return com.zy.crawler.config.redis.BloomFilterHelper<java.lang.String>
//     * @author Lifeifei
//     * @date 2019/4/8 13:18
//     */
//    @Bean
//    public BloomFilterHelper<String> initBloomFilterHelper() {
//        return new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8)
//                .putString(from, Charsets.UTF_8), 1000000, 0.01);
//    }

}
