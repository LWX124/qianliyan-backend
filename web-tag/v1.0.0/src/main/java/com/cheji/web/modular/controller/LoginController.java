package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheji.web.config.WxProperties;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUserCouponEntity;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.domain.AppUserPlusEntity;
import com.cheji.web.modular.mapper.AppUserMapper;
import com.cheji.web.modular.mapper.AppUserPlusMapper;
import com.cheji.web.modular.service.AppUserCouponService;
import com.cheji.web.modular.service.AppUserPlusService;
import com.cheji.web.modular.service.HuaweiSmsService;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@Api("用户相关")
@RequestMapping("/user")
@RestController
public class LoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WxProperties wxProperties;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppUserPlusService appUserPlusService;

    @Resource
    private UserService userService;

    @Resource
    private AppUserPlusMapper appUserPlusMapper;

    @Resource
    private HuaweiSmsService huaweiSmsService;

    @Resource
    private AppUserCouponService appUserCouponService;

    private final String regex = "^1[0-9]{10}$";


    @ApiOperation(value = "获取注册验证码" +
            "401：参数错误 电话号码必须是长度11位的数字" +
            "402：同一用户一分钟内只能获取一次验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话号码", required = true, dataType = "String"),
    })
    @PostMapping("/getRegisterCode")
    public JSONObject getRegisterCode(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String phone = in.getString("phone");
        if (StringUtils.isEmpty(phone) || !Pattern.matches(regex, phone)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + phone);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_REGISTER_PHONE_CODE + phone, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + phone, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("1", phone, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }

    @ApiOperation(value = "注销验证码校验")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
    })
    @PostMapping("/logOutCode")
    public JSONObject logOutCode(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String userName = in.getString("userName");
        String code = in.getString("code");
        String systemCode = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_PHONE_CODE + userName);
        if(StringUtils.isNotEmpty(systemCode) && systemCode.equals(code)){
            result.put("code", 200);
            result.put("data", "请求已提交,审核中！");
            return result;
        }
        result.put("code", 402);
        result.put("msg", "验证码错误");
        return result;
    }

    @ApiOperation(value = "获取登录验证码" +
            "401：参数错误 电话号码必须是长度11位的数字" +
            "402：同一用户一分钟内只能获取一次验证码" +
            "403：账号不存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "账号", required = true, dataType = "String"),
    })
    @PostMapping("/getLoginCode")
    public JSONObject getLoginCode(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String userName = in.getString("userName");

        if (!userName.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }
        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(userName);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
        if (appUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "账号不存在！");
            return result;
        }

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + userName);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_PHONE_CODE + userName, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + userName, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("4", userName, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }

    @ApiOperation(value = "换绑手机号获取验证码" +
            "401：参数错误 电话号码必须是长度11位的数字" +
            "402：同一用户一分钟内只能获取一次验证码" +
            "530：当前用户未登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "新账号", required = true, dataType = "String"),
    })
    @PostMapping("/getBindingSmsCode")
    public JSONObject getBindingSmsCode(HttpServletRequest request, @RequestBody JSONObject in) {
        String phone = in.getString("phone");

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        JSONObject result = new JSONObject();

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "当前用户未登录");
            return result;
        }

        if (!phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + phone);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_BINDING_PHONE_CODE + phone, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + phone, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("2", phone, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }

    @ApiOperation(value = "换绑手机号(修改账号)" +
            "401：参数错误" +
            "402：验证码错误" +
            "530：当前用户未登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "新账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
    })
    @PostMapping("/convertPhone")
    public JSONObject convertPhone(HttpServletRequest request, @RequestBody JSONObject in) {
        String phone = in.getString("phone");
        String code = in.getString("code");

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        JSONObject result = new JSONObject();

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "当前用户未登录");
            return result;
        }

        if (StringUtils.isEmpty(phone) || !phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        String systemCode = stringRedisTemplate.opsForValue().get(RedisConstant.USER_BINDING_PHONE_CODE + phone);
        if (!systemCode.equals(code)) {
            result.put("code", 402);
            result.put("msg", "验证码错误");
            return result;
        }

        userService.convertPhone(currentLoginUser.getAppUserEntity().getId(), phone);

        result.put("code", 200);
        result.put("msg", "修改成功！");
        return result;
    }


    @ApiOperation(value = "注册账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "passWord", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "inviteCode", value = "邀请码", required = true, dataType = "String"),
    })
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject in) {
        logger.info("### 注册账号  register ### in={}", in);
        String userName = in.getString("userName");
        String passWord = in.getString("passWord");
        String smsCode = in.getString("smsCode");
        String inviteCode = in.getString("inviteCode");

        JSONObject result = validRegister(userName, passWord, smsCode);
        if (StringUtils.isNotEmpty(result.getString("code"))) {
            return result;
        }

        if (!userName.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "电话号码必须是长度11位的数字");
            return result;
        }

        AppUserEntity paramerUserName = new AppUserEntity();
        paramerUserName.setUsername(userName);
        AppUserEntity appUserEntity1 = appUserMapper.selectOne(paramerUserName);
        if (appUserEntity1 != null) {
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

        Integer inviteId = 0;//邀请人id

        if (StringUtils.isNotEmpty(inviteCode)) {
            AppUserPlusEntity paramerPlus = new AppUserPlusEntity();
            paramerPlus.setInviteCode(inviteCode);
            AppUserPlusEntity appUserPlusEntity = appUserPlusMapper.selectOne(paramerPlus);
            if (appUserPlusEntity == null) {
                result.put("code", 403);
                result.put("msg", "邀请码没有对应账号");
                return result;
            }
            inviteId = appUserPlusEntity.getUserId();
        }

        String salt = PasswordUtil.generateSalt();
        String encodePassword = PasswordUtil.encode(passWord, salt);//密码加密
        AppUserEntity appUserEntity = new AppUserEntity();
        appUserEntity.setUsername(userName);
        appUserEntity.setPassword(encodePassword);
        appUserEntity.setSalt(salt);
        appUserEntity.setPhoneNumber(userName);
        appUserEntity.setCreatTime(new Date());
        appUserEntity.setParentId(inviteId);
        Random random = new Random();
        appUserEntity.setName("车己" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "");
        appUserMapper.insert(appUserEntity);

        userService.registerHuanxin(appUserEntity);
        //新增优惠卷
        //查询是否有优惠卷了
        List<AppUserCouponEntity> coupon = appUserCouponService.findCoupon(appUserEntity.getId());
        if (coupon.isEmpty()){
            appUserCouponService.addCoupon(appUserEntity.getId());
        }


        result.put("code", 200);
        result.put("msg", "注册成功！");
        return result;

    }

    /**
     * 验证注册信息
     *
     * @return
     */
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
        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(userName);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
        if (appUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "账号不存在！");
            return result;
        }

        boolean passwordValid = PasswordUtil.isPasswordValid(appUserEntity.getPassword(), passWord, appUserEntity.getSalt());
        if (!passwordValid) {
            result.put("code", 402);
            result.put("msg", "密码错误！");
            return result;
        }

        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUserEntity);
        dissOldUser(appUserEntity.getId());
        setToken(thirdSessionKey, tokenPojo);//记录token

        JSONObject data = new JSONObject();
        data.put("thirdSessionKey", thirdSessionKey);
        data.put("u", appUserEntity.getHuanxinUserName());
        data.put("p", appUserEntity.getHuanxinPassword());
        data.put("userId", appUserEntity.getId());
        data.put("vipLv",appUserEntity.getVipLv());

        result.put("code", 200);
        result.put("msg", "登录成功！");
        result.put("data", data);
        return result;
    }


    @ApiOperation(value = "短信验证码登录" +
            "401：参数错误" +
            "402：验证码错误 " +
            "403：账号不存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String")
    })
    @PostMapping("/loginForCode")
    public JSONObject loginForCode(@RequestBody JSONObject in) {
        String userName = in.getString("userName");
        String code = in.getString("code");

        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(userName) || !userName.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "账号格式错误");
            return result;
        }

        if (StringUtils.isEmpty(code) || code.length() != 6) {
            result.put("code", 401);
            result.put("msg", "请输入正确的6位验证码");
            return result;
        }

        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(userName);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
        if (appUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "账号不存在！");
            return result;
        }

        String systemCode = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_PHONE_CODE + userName);
        if (!systemCode.equals(code)) {
            result.put("code", 402);
            result.put("msg", "验证码错误");
            return result;
        }


        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUserEntity);
        dissOldUser(appUserEntity.getId());
        setToken(thirdSessionKey, tokenPojo);//记录token

        JSONObject data = new JSONObject();
        data.put("thirdSessionKey", thirdSessionKey);
        data.put("u", appUserEntity.getHuanxinUserName());
        data.put("p", appUserEntity.getHuanxinPassword());
        data.put("userId", appUserEntity.getId());
        data.put("vipLv",appUserEntity.getVipLv());
        result.put("code", 200);
        result.put("msg", "登录成功！");
        result.put("data", data);
        return result;
    }

    @ApiOperation(value = "微信登录 401：参数错误" +
            "201：跳转到绑定手机号" +
            "501：用户授权失败！请联系管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "code", value = "code", required = true, dataType = "String")
    })
    @PostMapping("/wxAccessToken")
    public JSONObject wxAccessCoken(@RequestBody JSONObject in) {
        String code = in.getString("code");

        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(code)) {
            result.put("code", 401);
            result.put("msg", "参数错误");
            return result;
        }
        String appId = wxProperties.getAppid();
        String appsecret = wxProperties.getAppsecret();
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);

        if (forEntity.getStatusCode() == HttpStatus.OK) {
            String body = forEntity.getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);

            String unionid = jsonObject.getString("unionid");
            String openid = jsonObject.getString("openid");

            if (StringUtils.isEmpty(openid)) {
                result.put("code", 401);
                result.put("msg", "code参数错误，获取到的openId为空");
                logger.error("###  code参数错误，获取到的openId为空 code={};openId={}", code, openid);
                return result;
            }

            //根据用户openid查询用户
            AppUserEntity appUserEntity = appUserMapper.selectOne(new AppUserEntity(openid));
            if (appUserEntity == null) {
                JSONObject data = new JSONObject();
                data.put("open", openid);
                result.put("data", data);
                result.put("code", 201);
                result.put("msg", "用户需要绑定手机号！");//引导用户到绑定手机号页面注册账号
                return result;
            } else {
                //创建系统内部登录key
                String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
                TokenPojo tokenPojo = new TokenPojo();
                tokenPojo.setUnionId(unionid);
                tokenPojo.setAppUserEntity(appUserEntity);
                dissOldUser(appUserEntity.getId());
                setToken(thirdSessionKey, tokenPojo);//记录token

                JSONObject data = new JSONObject();
                data.put("thirdSessionKey", thirdSessionKey);
                data.put("u", appUserEntity.getHuanxinUserName());
                data.put("p", appUserEntity.getHuanxinPassword());
                data.put("userId", appUserEntity.getId());
                data.put("userName", appUserEntity.getUsername());
                data.put("vipLv",appUserEntity.getVipLv());
                result.put("code", 200);
                result.put("msg", "登录成功！");
                result.put("data", data);
                return result;
            }
        } else {
            result.put("code", 501);
            result.put("msg", "用户授权失败！");
        }
        return result;
    }

    /**
     * 记录token
     *
     * @param thirdSessionKey
     * @param tokenPojo
     */
    private void setToken(String thirdSessionKey, TokenPojo tokenPojo) {
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_TOKEN + thirdSessionKey, JSON.toJSONString(tokenPojo), 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        //记录用户的token和id之间的关系，用于踢下线
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_ID_TOKEN + tokenPojo.getAppUserEntity().getId(), thirdSessionKey);
    }

    /**
     * 把之前的用户挤下线
     */
    private void dissOldUser(int userId) {
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_ID_TOKEN + userId);
        if (StringUtils.isNotEmpty(s)) {
            stringRedisTemplate.delete(RedisConstant.USER_TOKEN + s);
        }
    }


    @ApiOperation(value = "微信登录,绑定手机号 " +
            "401：入参错误" +
            "402:验证码错误" +
            "405：新手机号已有账号，不能被绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "openId", value = "openId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "inviteCode", value = "邀请码", required = true, dataType = "String"),
    })
    @PostMapping("/bindPhone")
    public JSONObject bindPhone(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String phone = in.getString("phone");
        String code = in.getString("code");
        String openId = in.getString("openId");
        String inviteCode = in.getString("inviteCode");

        if (StringUtils.isEmpty(code)) {
            result.put("code", 401);
            result.put("msg", "验证码不能为空");
            return result;
        }

        if (StringUtils.isEmpty(phone)) {
            result.put("code", 401);
            result.put("msg", "手机号不能为空");
            return result;
        }

        if (!phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "手机号码格式错误");
            return result;
        }

        if (StringUtils.isEmpty(openId)) {
            result.put("code", 401);
            result.put("msg", "openId不能为空");
            return result;
        }

        String systemCode = stringRedisTemplate.opsForValue().get(RedisConstant.USER_BINDING_PHONE_CODE + phone);
        if (!code.equals(systemCode)) {
            result.put("code", 402);
            result.put("msg", "验证码错误");
            return result;
        }

        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(phone);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);//根据手机号查找用户是否注册过

        if (appUserEntity == null) {//根据手机号查找用户，没有找到表示需要新增用户，如果找到了只需要把用户的openId更新
            int inviteId = 0;
            if (StringUtils.isNotEmpty(inviteCode)) {
                AppUserPlusEntity paramerPlus = new AppUserPlusEntity();
                paramerPlus.setInviteCode(inviteCode);
                AppUserPlusEntity appUserPlusEntity = appUserPlusMapper.selectOne(paramerPlus);
                if (appUserPlusEntity == null) {
                    result.put("code", 403);
                    result.put("msg", "邀请码没有对应账号");
                    return result;
                }
                inviteId = appUserPlusEntity.getUserId();
            }

            appUserEntity = new AppUserEntity();
            appUserEntity.setCreatTime(new Date());
            appUserEntity.setParentId(inviteId);//只有新增用户才可以绑定推荐人
            appUserEntity.setUsername(phone);
            appUserEntity.setPhoneNumber(phone);
            Random random = new Random();
            appUserEntity.setName("车己" + ((int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000) + "");//新用户才需要设置默认名字
            appUserEntity.setWxOpenId(openId);//新用户绑定openId
            appUserMapper.insert(appUserEntity);

            userService.registerHuanxin(appUserEntity);//新用户才需要注册环信账号
        } else {
            appUserEntity.setWxOpenId(openId);//老用户绑定openId
            appUserMapper.updateById(appUserEntity);
        }

        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        TokenPojo tokenPojo = new TokenPojo();
        tokenPojo.setUnionId(null);
        tokenPojo.setAppUserEntity(appUserEntity);
        dissOldUser(appUserEntity.getId());
        setToken(thirdSessionKey, tokenPojo);//记录token

        JSONObject data = new JSONObject();
        data.put("thirdSessionKey", thirdSessionKey);
        data.put("u", appUserEntity.getHuanxinUserName());
        data.put("p", appUserEntity.getHuanxinPassword());
        data.put("userId", appUserEntity.getId());
        data.put("vipLv",appUserEntity.getVipLv());

        result.put("code", 200);
        result.put("data", data);
        result.put("msg", "绑定成功");
        return result;
    }

    @ApiOperation(value = "绑定手机号 获取验证码 " +
            "401：手机号格式错误" +
            "403：该号码已绑定有其他账号" +
            "402：同一用户一分钟内只能获取一次验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", required = true, dataType = "String"),
    })
    @PostMapping("/smsForBindPhone")
    public JSONObject smsForBindPhone(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String phone = in.getString("phone");
        if (StringUtils.isEmpty(phone) || !phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "手机号格式错误");
            return result;
        }

//        AppUserEntity appUserEntityParam = new AppUserEntity();
//        appUserEntityParam.setUsername(phone);
//        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
//        if (appUserEntity != null) {
//            result.put("code", 403);
//            result.put("msg", "该号码已绑定有其他账号！");
//            return result;
//        }

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + phone);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_BINDING_PHONE_CODE + phone, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + phone, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("5", phone, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }

    @ApiOperation(value = "忘记密码-获取短信" +
            "401:手机号格式错误" +
            "403：当前手机号没有绑定账号" +
            "402：同一用户一分钟内只能获取一次验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", required = true, dataType = "String"),
    })
    @PostMapping("/smsForgetPass")
    public JSONObject smsForgetPass(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String phone = in.getString("phone");
        if (StringUtils.isEmpty(phone) || !phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "手机号格式错误");
            return result;
        }

        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(phone);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
        if (appUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "当前手机号没有绑定账号！");
            return result;
        }

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE_TIME + phone);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 402);
            result.put("msg", "同一用户一分钟内只能获取一次验证码");
            return result;
        }

        String randomCode = PasswordUtil.getRandomCode(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_FORGET_PASS_CODE + phone, randomCode, 5 * 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE_TIME + phone, "1", 60, TimeUnit.SECONDS);

        huaweiSmsService.sendSmsByTemplate("6", phone, randomCode);

        result.put("code", 200);
        result.put("msg", "短信验证码发送成功！");
        return result;
    }

    @ApiOperation(value = "忘记密码-修改密码" +
            "401：参数错误" +
            "402：验证码错误" +
            "403：当前手机号没有绑定账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "新密码", required = true, dataType = "String"),
    })
    @PostMapping("/forgetUpdatePass")
    public JSONObject forgetUpdatePass(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        String phone = in.getString("phone");
        String code = in.getString("code");
        String newPassword = in.getString("newPassword");
        if (StringUtils.isEmpty(phone) || !phone.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "手机号格式错误");
            return result;
        }

        if (StringUtils.isEmpty(newPassword) || newPassword.length() < 8 || newPassword.length() > 14) {
            result.put("code", 401);
            result.put("msg", "密码不能为空或者长度小于8");
            return result;
        }

        if (StringUtils.isEmpty(code)) {
            result.put("code", 401);
            result.put("msg", "验证码不能为空");
            return result;
        }

        String systemCode = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_FORGET_PASS_CODE + phone);
        if (!code.equals(systemCode)) {
            result.put("code", 402);
            result.put("msg", "验证码错误");
            return result;
        }

        AppUserEntity appUserEntityParam = new AppUserEntity();
        appUserEntityParam.setUsername(phone);
        AppUserEntity appUserEntity = appUserMapper.selectOne(appUserEntityParam);
        if (appUserEntity == null) {
            result.put("code", 403);
            result.put("msg", "当前手机号没有绑定账号");
            return result;
        }

        String salt = PasswordUtil.generateSalt();
        String encodePassword = PasswordUtil.encode(newPassword, salt);//密码加密
        appUserEntity.setPassword(encodePassword);
        appUserEntity.setSalt(salt);

        appUserMapper.updateById(appUserEntity);
        dissOldUser(appUserEntity.getId());

        result.put("code", 200);
        result.put("msg", "修改成功！");
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
        Integer id = currentLoginUser.getAppUserEntity().getId();
        dissOldUser(id);//退出登录
        result.put("code", 200);
        result.put("msg", "成功！");
        return result;
    }
}
