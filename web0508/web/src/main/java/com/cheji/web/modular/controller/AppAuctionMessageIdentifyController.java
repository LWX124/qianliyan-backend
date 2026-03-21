package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppAuctionMessageIdentifyEntity;
import com.cheji.web.modular.service.AppAuctionImgAuthService;
import com.cheji.web.modular.service.HuaweiSmsService;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.AssertUtil;
import com.cheji.web.util.PasswordUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/authentication")
public class AppAuctionMessageIdentifyController extends BaseController {

    @Autowired
    private AppAuctionImgAuthService appAuctionImgAuthService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HuaweiSmsService huaweiSmsService;

    @ApiOperation(value = "获取认证资料")
    @RequestMapping(value = "/getAuthentication", method = RequestMethod.GET)
    public JSONObject getAuthentication(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        return appAuctionImgAuthService.getAuthentication(result, currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "上传认证资料")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "authImg", value = "认证图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "认证类型,1个人,2企业", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "idName", value = "身份证姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "idNumber", value = "身份证号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessName", value = "企业名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessNumber", value = "企业信用代码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话号码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/addAuthentication", method = RequestMethod.POST)
    public JSONObject addAuthentication(@RequestBody AppAuctionMessageIdentifyEntity auth, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        String smsCode = auth.getCode();
        String phone = auth.getPhone();
        if(smsCode == null || phone == null){
            result.put("code", 532);
            result.put("msg", "请输入验证码或电话号码!");
            return result;
        }
        if (StringUtils.isEmpty(smsCode) || smsCode.length() != 6) {
            result.put("code", 401);
            result.put("msg", "短信验证码必须是6位数");
            return result;
        }

        try {
            AssertUtil.isPhone(phone,"手机格式错误");
        }catch (Exception e){
            result.put("code", 401);
            result.put("msg", "手机格式错误");
            return result;
        }

        String systemCode = (String)redisTemplate.opsForValue().get(RedisConstant.USER_REGISTER_PHONE_CODE + phone);
        if (StringUtils.isEmpty(systemCode)) {
            result.put("code", 402);
            result.put("msg", "验证码已过期！");
            return result;
        }

        if (!smsCode.equals(systemCode)) {
            result.put("code", 402);
            result.put("msg", "验证码错误");
            return result;
        }

        if(auth.getAuthImg() == null || auth.getAuthImg().size() < 1){
            result.put("code", 531);
            result.put("msg", "请传入图片");
            return result;
        }

        return appAuctionImgAuthService.addAuthentication(auth,currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "发送验证短信")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phoneNum", value = "电话号码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public JSONObject sendMessage(@RequestParam(required = false) String phoneNum, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        try {
            AssertUtil.isPhone(phoneNum,"手机格式错误");
        }catch (Exception e){
            result.put("code", 401);
            result.put("msg", "手机格式错误");
            return result;
        }
        String s = (String)redisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + phoneNum);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        redisTemplate.opsForValue().set(RedisConstant.USER_REGISTER_PHONE_CODE + phoneNum, randomCode, 5 * 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + phoneNum, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("7", phoneNum, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }



}