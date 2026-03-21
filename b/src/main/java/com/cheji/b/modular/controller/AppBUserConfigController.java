package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.MerchantsInfoBannerEntity;
import com.cheji.b.modular.dto.UserBDto;
import com.cheji.b.modular.service.AppWxService;
import com.cheji.b.modular.service.MerchantsCommentsTreeService;
import com.cheji.b.modular.service.MerchantsInfoBannerService;
import com.cheji.b.modular.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商户关联配置表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2019-12-23
 */
@RestController
@RequestMapping("/buserConfig")
public class AppBUserConfigController extends BaseController {


    private final static Logger logger = LoggerFactory.getLogger(AppBUserConfigController.class);


    //查询到给b端有消息的账号
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private MerchantsInfoBannerService merchantsInfoBannerService;

    @RequestMapping(value = "/getBId", method = RequestMethod.GET)
    public JSONObject getBId() {
        JSONObject result = new JSONObject();
        //保存b端

        List<UserBDto> userBDtoList = new ArrayList<>();
        Set<String> members = stringRedisTemplate.opsForSet().members(RedisConstant.B_ACCOUNT_RECEIVED_MESSAGE);
        if (members != null) {
            for (String member : members) {
                //根据id查询到各种信息
                UserBDto userBDto = new UserBDto();
                if (member.endsWith("Z")){
                    userBDto.setId(member);
                    member = member.substring(0,member.length() - 1);
                    //查询到图片
                    String url = merchantsInfoBannerService.findUpmerImg(member);
                    String merName = userService.findUpName(member);

                    userBDto.setHeadImg(url);

                    AppUserEntity appUserEntity = userService.selectByIdupMerchats(member);
                    String password = appUserEntity.getHuanxinPassword();
                    String username = appUserEntity.getHuanxinUserName();
                    userBDto.setHuanxinPassword(password);
                    userBDto.setHuanxinUserName(username);


                    userBDto.setMerchanstName(merName);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date saveTime = appUserEntity.getSaveTime();
                    userBDto.setTime(formatter.format(saveTime));

                    userBDto.setUnreadMessage(appUserEntity.getUnreadMessage());
                    userBDtoList.add(userBDto);

                }else {
                    AppUserEntity appUserEntity = userService.selectById(member);
                    if (appUserEntity == null) {
                        result.put("code", 403);
                        result.put("msg", "商户id有误");
                        return result;
                    }

                    //查询到头像
                    EntityWrapper<MerchantsInfoBannerEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_b_id", member)
                            .eq("`index`", 1);
                    MerchantsInfoBannerEntity merchantsInfoBannerEntity = merchantsInfoBannerService.selectOne(wrapper);
                    if (merchantsInfoBannerEntity == null) {
                        continue;
                    }
                    userBDto.setId(member);
                    userBDto.setHeadImg(merchantsInfoBannerEntity.getUrl());
                    userBDto.setHuanxinPassword(appUserEntity.getHuanxinPassword());
                    userBDto.setHuanxinUserName(appUserEntity.getHuanxinUserName());
                    userBDto.setMerchanstName(appUserEntity.getMerchantsName());
                    userBDto.setUnreadMessage(appUserEntity.getUnreadMessage());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date saveTime = appUserEntity.getSaveTime();
                    if (saveTime==null) {
                        userBDto.setTime("2020-01-03 20:13:53");
                    }else {
                        userBDto.setTime(formatter.format(saveTime));
                    }
                    userBDtoList.add(userBDto);
                }
            }
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", userBDtoList);
        return result;

    }



    //已读消息
    @RequestMapping(value = "/readMessage", method = RequestMethod.GET)
    public JSONObject readMessage(String userBId) {
        JSONObject result = new JSONObject();
        if (userBId.endsWith("Z")){
            //upmerchants商户
            userBId = userBId.substring(0,userBId.length() - 1);
            //修改已读未读
            userService.updateUpMerMess(userBId);
        }else {
            AppUserEntity appUserEntity = userService.selectById(userBId);
            if (appUserEntity==null){
                result.put("code", 407);
                result.put("msg", "未查询到该商户");
                return result;
            }
            appUserEntity.setUnreadMessage(0);
            userService.updateById(appUserEntity);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

}
