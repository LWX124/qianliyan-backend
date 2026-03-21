package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUpMerchantsEntity;
import com.cheji.web.modular.domain.BUserEntity;
import com.cheji.web.modular.service.AppUpMerchantsService;
import com.cheji.web.modular.service.BUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 商户关联配置表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2019-12-25
 */
@RestController
@RequestMapping("/buserConfig")
public class AppBUserConfigController extends BaseController {
    //保存聊天时b端账号

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private BUserService bUserService;

    @RequestMapping(value = "/saveBId", method = RequestMethod.GET)
    public JSONObject saveBId(String userBId) {
        JSONObject result = new JSONObject();
        //保存b端
        if (StringUtils.isNotEmpty(userBId)) {
            if (userBId.endsWith("Z")){
                //z结尾就是上架的商户
                String  member = userBId.substring(0,userBId.length() - 1);
                AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsService.selectById(member);
                if (appUpMerchantsEntity == null) {
                    result.put("code", 200);
                    result.put("msg", "未查询到商户，检查商户id");
                    return result;
                }
                //添加未读消息字段
                appUpMerchantsEntity.setUnreadMessage(appUpMerchantsEntity.getUnreadMessage()+1);
                appUpMerchantsEntity.setSaveTime(new Date());
                appUpMerchantsService.updateById(appUpMerchantsEntity);
                stringRedisTemplate.opsForSet().add(RedisConstant.B_ACCOUNT_RECEIVED_MESSAGE, userBId);
            }else {
                BUserEntity bUserEntity = bUserService.selectById(userBId);
                if (bUserEntity == null) {
                    result.put("code", 200);
                    result.put("msg", "未查询到商户，检查商户id");
                    return result;
                }
                //未读消息字段
                bUserEntity.setUnreadMessage(bUserEntity.getUnreadMessage() + 1);
                bUserEntity.setSaveTime(new Date());
                bUserService.updateById(bUserEntity);
                stringRedisTemplate.opsForSet().add(RedisConstant.B_ACCOUNT_RECEIVED_MESSAGE, userBId);
            }
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }

}
