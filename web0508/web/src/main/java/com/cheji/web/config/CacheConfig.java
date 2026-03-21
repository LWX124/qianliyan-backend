package com.cheji.web.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Resource
    private RedisConnectionFactory factory;


    //使用JSON进行序列化
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //JSON格式序列化
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
         //key的序列化
        redisTemplate.setKeySerializer(genericJackson2JsonRedisSerializer);
        //value的序列化
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        //hash结构key的虚拟化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash结构value的虚拟化
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }
    //缓存管理器
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues() //不允许空值
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));//值使用JSON虚拟化
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}