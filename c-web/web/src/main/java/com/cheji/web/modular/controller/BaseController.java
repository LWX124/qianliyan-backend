package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.constant.UserConstant;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.pojo.TokenPojo;
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
        Object o = stringRedisTemplate.opsForValue().get(RedisConstant.USER_TOKEN + thirdsessionkey);
        if (o == null) {
            return null;
        }

        JSONObject result = JSONObject.parseObject(String.valueOf(o));

        AppUserEntity appUser = JSONObject.parseObject(result.getString("appUserEntity"), AppUserEntity.class);

        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setThirdSessionKey(thirdsessionkey);
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUser);

        String source = result.getString("source");
        tokenPojo.setSource(source);

        //刷新用户token有效期
        stringRedisTemplate.expire(RedisConstant.USER_TOKEN + thirdsessionkey, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return tokenPojo;
    }
}
