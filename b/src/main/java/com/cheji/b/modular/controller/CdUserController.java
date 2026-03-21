package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.CdUserEntity;
import com.cheji.b.modular.mapper.CdUserMapper;
import com.cheji.b.pojo.TokenPojo;
import com.cheji.b.utils.PasswordUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 车店用户表
 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2022-01-11
 */
@RestController
@RequestMapping("/cdUser")
public class CdUserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CdUserController.class);

    private final String regex = "^1[0-9]{10}$";

    @Resource
    private CdUserMapper cdUserMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "注册账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "passWord", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码", required = true, dataType = "String"),
    })
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject in) {
        logger.info("### 注册账号  register ### in={}", in);
        String userName = in.getString("userName");
        String passWord = in.getString("passWord");
        String smsCode = in.getString("smsCode");
        String status = in.getString("status");

        JSONObject result = validRegister(userName, passWord, smsCode);
        if (StringUtils.isNotEmpty(result.getString("code"))) {
            return result;
        }
        if (StringUtils.isEmpty(status)) {
            result.put("code", 408);
            result.put("msg", "状态不能为空");
            return result;
        }

        if (!userName.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        CdUserEntity paramerUserName = new CdUserEntity();
        paramerUserName.setUsername(userName);
        CdUserEntity CdUserEntity1 = cdUserMapper.selectOne(paramerUserName);
        if (CdUserEntity1 != null) {
            result.put("code", 404);
            result.put("msg", "当前账号已注册！");
            return result;
        }

        String code = stringRedisTemplate.opsForValue().get(RedisConstant.USER_REGISTER_PHONE_CODE + userName);
        if (StringUtils.isEmpty(code)) {
            result.put("code", 402);
            result.put("msg", "验证码已过期！");
            return result;
        }

        if (!code.equals(smsCode)) {
            result.put("code", 402);
            result.put("msg", "验证码错误！");
            return result;
        }

        String salt = PasswordUtil.generateSalt();
        String encodePassword = PasswordUtil.encode(passWord, salt);//密码加密
        CdUserEntity cdUserEntity = new CdUserEntity();
        cdUserEntity.setUsername(userName);
        cdUserEntity.setPassword(encodePassword);
        cdUserEntity.setSalt(salt);
        cdUserEntity.setPhoneNumber(userName);
        cdUserEntity.setCreatTime(new Date());
        cdUserEntity.setStatus(Integer.valueOf(status));
        Random random = new Random();
        String name = "车己" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000);
        cdUserEntity.setName(name);
        cdUserMapper.insert(cdUserEntity);

        //userService.registerHuanxin(appUserEntity);
        result.put("code", 200);
        result.put("msg", "注册成功！");
        return result;
    }


    private JSONObject validRegister(String userName, String passWord, String smsCode) {
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(userName)) {
            result.put("code", 401);
            result.put("msg", "登录账号不能为空或者不是正常的手机格式");
            return result;
        }
        if (StringUtils.isEmpty(smsCode) || smsCode.length() != 6) {
            result.put("code", 401);
            result.put("msg", "短信验证码必须是6位数");
            return result;
        }
        if (StringUtils.isEmpty(passWord) || passWord.length() < 8 || passWord.length() > 14) {
            result.put("code", 401);
            result.put("msg", "密码不能为空或者长度小于8");
            return result;
        }

        if (!userName.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "账号必须是正常的手机格式");
            return result;
        }
        return result;
    }

    @ApiOperation(value = "账号密码登录 ")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "passWord", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("/loginForPass")
    public JSONObject loginForPass(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String userName = in.getString("userName");
        String passWord = in.getString("passWord");

        if (StringUtils.isEmpty(userName) || userName.length() < 11) {
            result.put("code", 401);
            result.put("msg", "账号格式错误");
            return result;
        }
        if (StringUtils.isEmpty(passWord) || passWord.length() < 8) {
            result.put("code", 401);
            result.put("msg", "密码格式错误");
            return result;
        }
        CdUserEntity cdUserEntityParam = new CdUserEntity();
        cdUserEntityParam.setUsername(userName);
        CdUserEntity cdUserEntity = cdUserMapper.selectOne(cdUserEntityParam);
        if (cdUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "账号不存在！");
            return result;
        }
        if (cdUserEntity.getStatus() == null || cdUserEntity.getStatus() != 1) {
            result.put("code", 405);
            result.put("msg", "账号未启用！请联系管理员");
            return result;
        }

        boolean passwordValid = PasswordUtil.isPasswordValid(cdUserEntity.getPassword(), passWord, cdUserEntity.getSalt());
        if (!passwordValid) {
            result.put("code", 402);
            result.put("msg", "密码错误！");
            return result;
        }

        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUnionId(null);
        tokenPojo.setCdUserEntity(cdUserEntity);
        dissOldUser(cdUserEntity.getId());
        setToken(thirdSessionKey, tokenPojo);//记录token

        JSONObject data = new JSONObject();
        data.put("thirdSessionKey", thirdSessionKey);
//        data.put("u", appUserEntity.getHuanxinUserName());
//        data.put("p", appUserEntity.getHuanxinPassword());

        result.put("code", 200);
        result.put("msg", "登录成功！");
        result.put("data", data);
        return result;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/signOut")
    public JSONObject signOut(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getCdUserEntity().getId();
        dissOldUser(id);//退出登录
        result.put("code", 200);
        result.put("msg", "成功！");
        return result;
    }

    /**
     * 记录token
     *
     * @param thirdSessionKey
     * @param tokenPojo
     */
    private void setToken(String thirdSessionKey, TokenPojo tokenPojo) {
        stringRedisTemplate.opsForValue().set(RedisConstant.CD_B_TOKEN + thirdSessionKey, JSON.toJSONString(tokenPojo), 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        //记录用户的token和id之间的关系，用于踢下线
        stringRedisTemplate.opsForValue().set(RedisConstant.CD_B_ID_TOKEN + tokenPojo.getCdUserEntity().getId(), thirdSessionKey);
    }

    /**
     * 把之前的用户挤下线
     */
    private void dissOldUser(int userId) {
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.CD_B_ID_TOKEN + userId);
        if (StringUtils.isNotEmpty(s)) {
            stringRedisTemplate.delete(RedisConstant.CD_B_TOKEN + s);
        }
    }


}