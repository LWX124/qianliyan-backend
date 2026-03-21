package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.constant.UserConstant;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.CdUserEntity;
import com.cheji.b.pojo.TokenPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

public class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    protected TokenPojo getCurrentLoginUser(HttpServletRequest request) {
        String thirdsessionkey = request.getHeader(UserConstant.USER_LOGIN_THIRDSESSIONKEY);
        logger.info("###getCurrentLoginUser### thirdsessionkey={}", thirdsessionkey);
        Object o = stringRedisTemplate.opsForValue().get(RedisConstant.USER_B_TOKEN + thirdsessionkey);
        if (o == null) {
            return null;
        }

        JSONObject result = JSONObject.parseObject(String.valueOf(o));

        AppUserEntity appUser = JSONObject.parseObject(result.getString("appUserEntity"), AppUserEntity.class);

        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setThirdSessionKey(thirdsessionkey);
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUser);


        //刷新用户token有效期
        stringRedisTemplate.expire(RedisConstant.USER_B_TOKEN + thirdsessionkey, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return tokenPojo;
    }


    protected TokenPojo getCdCurrentLoginUser(HttpServletRequest request) {
        String thirdsessionkey = request.getHeader(UserConstant.USER_LOGIN_THIRDSESSIONKEY);
        logger.info("###getCurrentLoginUser### thirdsessionkey={}", thirdsessionkey);
        Object o = stringRedisTemplate.opsForValue().get(RedisConstant.CD_B_TOKEN + thirdsessionkey);
        if (o == null) {
            return null;
        }

        JSONObject result = JSONObject.parseObject(String.valueOf(o));

        CdUserEntity cdUserEntity = JSONObject.parseObject(result.getString("cdUserEntity"), CdUserEntity.class);

        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setThirdSessionKey(thirdsessionkey);
        tokenPojo.setUnionId(null);
        tokenPojo.setCdUserEntity(cdUserEntity);


        //刷新用户token有效期
        stringRedisTemplate.expire(RedisConstant.CD_B_TOKEN + thirdsessionkey, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return tokenPojo;
    }
}
