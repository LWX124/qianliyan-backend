package com.cheji.web.modular.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.mapper.AppUserMapper;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.cheji.web.constant.SourceEnum;

/**
 * 微信小程序登录控制器
 */
@Api("微信小程序登录")
@RestController
@RequestMapping("/user")
public class WxMiniLoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(WxMiniLoginController.class);

    @Resource
    private WxMaService wxMaService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private Map<String, String> sourceToAppIdMap;

    @ApiOperation("微信小程序登录")
    @PostMapping("/wxMiniLogin")
    public JSONObject wxMiniLogin(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String code = in.getString("code");
        String source = in.getString("source");

        if (StringUtils.isEmpty(code)) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }

        // 校验 source
        if (StringUtils.isEmpty(source) || !SourceEnum.isValid(source)) {
            result.put("code", 401);
            result.put("msg", "无效的来源标识");
            return result;
        }

        // 根据 source 获取对应的 appId
        String appId = sourceToAppIdMap.get(source);
        if (StringUtils.isEmpty(appId)) {
            result.put("code", 401);
            result.put("msg", "未配置该来源的微信应用");
            return result;
        }

        try {
            // 切换到对应小程序的配置
            wxMaService.switchoverTo(appId);
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();

            if (StringUtils.isEmpty(openid)) {
                result.put("code", 401);
                result.put("msg", "获取openid失败");
                return result;
            }

            // 根据 openid 查找用户
            AppUserEntity param = new AppUserEntity();
            param.setWxOpenId(openid);
            AppUserEntity appUserEntity = appUserMapper.selectOne(param);

            if (appUserEntity == null) {
                // 自动注册新用户
                appUserEntity = new AppUserEntity();
                appUserEntity.setWxOpenId(openid);
                appUserEntity.setCreatTime(new Date());
                appUserEntity.setSource(source);
                Random random = new Random();
                appUserEntity.setName("微信用户" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000));
                appUserMapper.insert(appUserEntity);
            }

            // 生成 token
            String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
            TokenPojo tokenPojo = new TokenPojo();
            tokenPojo.setUnionId(null);
            tokenPojo.setAppUserEntity(appUserEntity);
            tokenPojo.setSource(source);

            // 踢掉旧登录
            String oldToken = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ID_TOKEN + appUserEntity.getId());
            if (StringUtils.isNotEmpty(oldToken)) {
                stringRedisTemplate.delete(RedisConstant.USER_TOKEN + oldToken);
            }

            // 存储 token，30天有效
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.USER_TOKEN + thirdSessionKey,
                    JSON.toJSONString(tokenPojo),
                    60 * 60 * 24 * 30, TimeUnit.SECONDS
            );
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.USER_ID_TOKEN + appUserEntity.getId(),
                    thirdSessionKey
            );

            JSONObject data = new JSONObject();
            data.put("thirdSessionKey", thirdSessionKey);
            data.put("userId", appUserEntity.getId());
            data.put("name", appUserEntity.getName());

            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", data);
            return result;

        } catch (WxErrorException e) {
            logger.error("小程序登录失败, source={}", source, e);
            result.put("code", 500);
            result.put("msg", "登录失败：" + e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("小程序登录异常, source={}", source, e);
            result.put("code", 500);
            result.put("msg", "登录异常：" + e.getMessage());
            return result;
        }
    }
}
