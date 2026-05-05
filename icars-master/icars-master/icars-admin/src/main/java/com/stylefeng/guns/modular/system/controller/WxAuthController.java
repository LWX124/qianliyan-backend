package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.model.BizWxUserGzh;
import com.stylefeng.guns.modular.system.service.IBizWxUserGzhService;
import com.stylefeng.guns.modular.system.service.IBizWxUserService;
import com.stylefeng.guns.modular.system.service.impl.WxService;
import com.stylefeng.guns.modular.system.utils.TokenCheckUtils;
import com.stylefeng.guns.modular.system.utils.XMLUtils;
import com.stylefeng.guns.modular.system.vo.ReceiveMsg;
import com.stylefeng.guns.modular.system.vo.WxUserInfoVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信用户认证相关
 *
 * @author xiaoqiang
 */
@RestController
public class WxAuthController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "styleFengWxService")
    private WxService wxService;

    @Autowired
    private IBizWxUserService bizWxUserService;

    @Resource
    private IBizWxUserGzhService bizWxUserGzhService;

    @Value("${spring.system.headimgHost:}")
    private String headimgHost;

    @Value("${spring.system.file-upload-path:}")
    private String fileUploadPath;

    private final static String token = "AshesToken";


    /**
     * 根据客户端传过来的code从微信服务器获取appid和session_key，然后生成3rdkey返回给客户端，后续请求客户端传3rdkey来维护客户端登录态
     *
     * @param wxCode 小程序登录时获取的code
     * @return
     */
    @ApiOperation(value = "获取sessionId", notes = "小用户允许登录后，使用code 换取 session_key api，将 code 换成 openid 和 session_key")
    @ApiImplicitParam(name = "code", value = "用户登录回调内容会带上 ", required = true, dataType = "String")
    @RequestMapping(value = "/api/v1/wx/getSession", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> createWxSession(@RequestParam(required = true, value = "code") String wxCode,
                                                @RequestParam(required = false, value = "source") String source) {
        log.info("/api/v1/wx/getSession source={}", source);
        long createSeconds = System.currentTimeMillis() / 1000;
        Map<String, Object> wxSessionMap = wxService.createWxSession(wxCode, source);

        if (null == wxSessionMap) {
            return rtnParam(50010, null);
        }
        //获取异常
        if (wxSessionMap.containsKey("errcode")) {
            return rtnParam(50020, null);
        }
        String wxOpenId = (String) wxSessionMap.get("openid");
        String wxSessionKey = (String) wxSessionMap.get("session_key");
        String unionId = (String) wxSessionMap.get("unionid");
        Long expires = 60 * 60L;
        if (StringUtils.isEmpty(wxOpenId)) {
            log.error("/api/v1/wx/getSession ### wxOpenId={};wxSessionKey={}", wxOpenId, wxSessionKey);
        }
        String thirdSession = wxService.create3rdSession(wxOpenId, wxSessionKey, expires, createSeconds, unionId, source);
        BizWxUser var1 = wxService.getWxSession(thirdSession).getBizWxUser();
        if (var1 == null) {
            //缓存内用户信息不在，查找数据库
            BizWxUser bizWxUser1 = bizWxUserService.selectBizWxUser(wxOpenId, source);
            if (bizWxUser1 == null) {
                BizWxUser bizWxUser = new BizWxUser();
                bizWxUser.setOpenid(wxOpenId);
                bizWxUser.setUnionId(unionId);
                bizWxUser.setType(1);
                bizWxUser.setSource(source);
                bizWxUser.setCreateTime(new Date());
                bizWxUserService.insert(bizWxUser);
                var1 = bizWxUser;
            } else {
                var1 = bizWxUser1;
            }
        }
        // 如果已有用户但 unionId 为空，补写 unionId
        if (var1 != null && StringUtils.isNotEmpty(unionId) && StringUtils.isEmpty(var1.getUnionId())) {
            var1.setUnionId(unionId);
            bizWxUserService.updateById(var1);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("sessionId", thirdSession);
        data.put("userId", var1 != null ? var1.getId() : 0);
        data.put("headImg", var1 != null ? var1.getHeadImg() : "");
        data.put("wxname", var1 != null ? var1.getWxname() : "");
        return rtnParam(0, data);
    }

    /**
     * 更新用户头像和昵称
     */
    @RequestMapping(value = "/api/v1/wx/user/updateProfile", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> param) {
        String thirdSessionKey = param.get("thirdSessionKey");
        String headImg = param.get("headImg");
        String wxname = param.get("wxname");
        if (StringUtils.isEmpty(thirdSessionKey)) {
            return rtnParam(530, "未登录");
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            return rtnParam(530, "登录已过期");
        }
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
        if (bizWxUser == null) {
            return rtnParam(500, "用户不存在");
        }
        if (StringUtils.isNotEmpty(headImg)) {
            bizWxUser.setHeadImg(headImg);
        }
        if (StringUtils.isNotEmpty(wxname)) {
            bizWxUser.setWxname(wxname);
        }
        bizWxUserService.updateById(bizWxUser);
        Map<String, Object> result = new HashMap<>();
        result.put("userId", bizWxUser.getId());
        result.put("headImg", bizWxUser.getHeadImg());
        result.put("wxname", bizWxUser.getWxname());
        return rtnParam(0, result);
    }

    /**
     * 上传头像到本地服务器（不走七牛云）
     */
    @RequestMapping(value = "/api/v1/wx/user/uploadAvatar", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile file,
                                            @RequestParam("thirdSessionKey") String thirdSessionKey) {
        if (StringUtils.isEmpty(thirdSessionKey)) {
            return rtnParam(530, "未登录");
        }
        WxSession wxSession = wxService.getWxSession(thirdSessionKey);
        if (wxSession == null) {
            return rtnParam(530, "登录已过期");
        }
        if (file == null || file.isEmpty()) {
            return rtnParam(500, "文件不能为空");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String ext = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;

            // 确保目录存在
            File dir = new File(fileUploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(fileUploadPath + fileName));

            String url = (headimgHost == null ? "" : headimgHost) + fileName;

            // 自动更新用户头像
            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
            if (bizWxUser != null) {
                bizWxUser.setHeadImg(url);
                bizWxUserService.updateById(bizWxUser);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("userId", bizWxUser != null ? bizWxUser.getId() : 0);
            data.put("headImg", url);
            data.put("wxname", bizWxUser != null ? bizWxUser.getWxname() : "");
            return rtnParam(0, data);
        } catch (Exception e) {
            log.error("上传头像异常", e);
            return rtnParam(500, "上传失败");
        }
    }

    @RequestMapping(value = "/api/v1/wx/testRedis", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> testRedis(@RequestParam(required = true, value = "code") String wxCode) {
        wxService.testRedis(wxCode);
        return rtnParam(50020, null);
    }


    /**
     * 获取用户openId和unionId数据(如果没绑定微信开放平台，解密数据中不包含unionId)
     *
     * @return
     */
    @ApiOperation(value = "解密微信用户信息", notes = "小用户允许登录后，使用encryptedData,iv,thirdSessionKey 换取 用户加密信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "encryptedData", value = "用户私密信息加密密文（小程序API获取得到）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "iv", value = "加密信息偏移量（解密用）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sessionId", value = "icars移动登陆的sessionid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/api/v1/wx/decodeUserInfo", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> decodeUserInfo(@Validated @RequestBody WxUserInfoVo param) throws UnsupportedEncodingException {
//        String  encryptedData = URLEncoder.encode(param.getEncryptedData(), "UTF-8").replace("%3D", "=").replace("%2F", "/");
//        String iv = URLEncoder.encode(param.getIv(), "UTF-8").replace("%3D", "=").replace("%2F", "/");
//        String sessionId = URLEncoder.encode(param.getSessionId(), "UTF-8").replace("%3D", "=").replace("%2F", "/");
        try {
            String decodeUserInfo = wxService.getDecodeUserInfo(param.getSessionId(), param.getIv(), param.getEncryptedData());
            Map<String, Object> map;
            try {
                map = rtnParam(0, decodeUserInfo);
            } catch (Exception e) {
                log.error("解密微信用户信息 ### decodeUserInfo={}", decodeUserInfo);
                e.printStackTrace();
                return rtnParam(500, "系统错误！");
            }
            WxSession wxSession = wxService.getWxSession(param.getSessionId());
            if (wxSession != null) {
                JSONObject userInfo = null;
                try {
                    userInfo = JSON.parseObject(String.valueOf(map.get("data")));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("###解密微信用户信息### map={}", map);
                }
                if (userInfo != null) {
                    bizWxUserService.setPhone(wxSession.getOpenId(), userInfo.get("phoneNumber") == null ? null : userInfo.get("phoneNumber").toString());
                }
            }
            return map;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtnParam(50021, null);
    }

    /**
     * 微信新版 getPhoneNumber（基于 code 换取手机号）
     * 小程序端调用 wx.getPhoneNumber 获取 code，传给后端
     * 后端用 code + access_token 调微信接口获取手机号并绑定
     */
    @ApiOperation(value = "绑定手机号", notes = "使用微信 getPhoneNumber 返回的 code 换取手机号并绑定")
    @RequestMapping(value = "/api/v1/wx/user/bindPhone", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> bindPhone(@RequestParam("thirdSessionKey") String thirdSessionKey,
                                         @RequestParam("code") String code) {
        try {
            // 1. 根据 thirdSessionKey 获取用户 session
            WxSession wxSession = wxService.getWxSession(thirdSessionKey);
            if (wxSession == null) {
                return rtnParam(401, "登录已过期，请重新登录");
            }
            String openId = wxSession.getOpenId();

            // 2. 获取小程序 access_token
            String accessToken = wxService.getXcxAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                log.error("bindPhone 获取 access_token 失败, openId={}", openId);
                return rtnParam(500, "系统错误，请重试");
            }

            // 3. 调微信接口用 code 换取手机号
            String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
            JSONObject reqBody = new JSONObject();
            reqBody.put("code", code);
            String res = com.stylefeng.guns.core.util.HttpRequest.sendPost(url, reqBody.toJSONString());
            log.info("bindPhone 微信返回: openId={}, res={}", openId, res);

            if (StringUtils.isEmpty(res)) {
                return rtnParam(500, "获取手机号失败，请重试");
            }
            JSONObject resJson = JSONObject.parseObject(res);
            int errcode = resJson.getIntValue("errcode");
            if (errcode != 0) {
                log.error("bindPhone 微信返回错误: errcode={}, errmsg={}", errcode, resJson.getString("errmsg"));
                return rtnParam(500, "获取手机号失败: " + resJson.getString("errmsg"));
            }

            JSONObject phoneInfo = resJson.getJSONObject("phone_info");
            String phoneNumber = phoneInfo != null ? phoneInfo.getString("phoneNumber") : null;
            if (StringUtils.isEmpty(phoneNumber)) {
                return rtnParam(500, "未获取到手机号");
            }

            // 4. 绑定手机号到 biz_wx_user
            bizWxUserService.setPhone(openId, phoneNumber);
            log.info("bindPhone 绑定成功: openId={}, phone={}", openId, phoneNumber);

            return rtnParam(0, "绑定成功");
        } catch (Exception e) {
            log.error("bindPhone 异常", e);
            return rtnParam(500, "系统错误，请重试");
        }
    }

    @RequestMapping(value = "/api/v1/wx/verify", method = RequestMethod.GET)
    @ResponseBody
    public void verify(@RequestParam(value = "signature", required = false) String signature,
                       @RequestParam(value = "timestamp", required = false) String timestamp,
                       @RequestParam(value = "nonce", required = false) String nonce,
                       @RequestParam(value = "echostr", required = false) String echostr, HttpServletResponse response) throws IOException {
        System.out.println(" PARAM VAL: >>>" + signature + "\t" + timestamp + "\t" + nonce + "\t" + echostr);
        log.info("开始签名验证：" + " PARAM VAL: >>>" + signature + "\t" + timestamp + "\t" + nonce + "\t" + echostr);
        if (StringUtils.isNotEmpty(signature) && StringUtils.isNotEmpty(timestamp)
                && StringUtils.isNotEmpty(nonce) && StringUtils.isNotEmpty(echostr)) {
            String sTempStr = "";
            try {
                String sortStr = sort("AshesToken", timestamp, nonce);
                sTempStr = getSHA1(sortStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotEmpty(sTempStr) && StringUtils.equals(signature, sTempStr)) {
                response.getWriter().write(echostr);
                log.info("验证成功：-----------：" + sTempStr);
            } else {
                log.info("验证失败：-----------：00000");
            }
        } else {
            log.info("验证失败：-----------：11111");
        }
    }

    private final static String appId = "wx4f9e7cdc772e9379";
    private final static String appSecret = "46eda183ad01e2d4e4c778cd9447d2e2";

    @ApiOperation(value = "公众号获取sessionId", notes = "小用户允许登录后，使用code 换取 session_key api，将 code 换成 openid 和 session_key")
    @ApiImplicitParam(name = "code", value = "用户登录回调内容会带上 ", required = true, dataType = "String")
    @RequestMapping(value = "/api/v1/wx/getGzhTokenAndOpenId", method = RequestMethod.POST)
    public Map<String, Object> getGzhTokenAndOpenId(@RequestBody JSONObject jsonObject) {
        String wxCode = jsonObject.getString("code");
        Map<String, Object> message = new HashMap<>();
        try {
            message = wxService.getAccessToken(appId, appSecret, wxCode);
            if (message.containsKey("errcode")) {
                log.error("/api/v1/wx/getGzhTokenAndOpenId失败的 ### message={}", message);
                return rtnParam(50020, message.get("errmsg"));
            }
            String wxOpenId = (String) message.get("openid");
            String unionId = (String) message.get("unionid");
            //查询数据库是否存在公众号用户
            BizWxUserGzh bizWxUserGzh = bizWxUserGzhService.selectBizWxUserGzh(wxOpenId);
            if (bizWxUserGzh == null) {
                BizWxUserGzh bizWxUser = new BizWxUserGzh();
                bizWxUser.setOpenid(wxOpenId);
                bizWxUser.setUnionId(unionId);
                bizWxUser.setType(1);
                bizWxUser.setCreateTime(new Date());
                bizWxUserGzhService.insert(bizWxUser);
                log.error("/api/v1/wx/getGzhTokenAndOpenId成功的 ### BizWxUserGzh={}", bizWxUser);
            }else {
                return rtnParam(50040, "已经绑定过用户，无需重复绑定");
            }
        } catch (IOException e) {
            log.error("/api/v1/wx/getGzhTokenAndOpenId失败的 ### message={}", message);
            throw new RuntimeException(e);
        }
        return rtnParam(0, "成功");
//        long createSeconds = System.currentTimeMillis() / 1000;
//        Map<String, Object> wxSessionMap = wxService.createWxSession(wxCode);
//
//        if (null == wxSessionMap) {
//            return rtnParam(50010, null);
//        }
//        //获取异常
//        if (wxSessionMap.containsKey("errcode")) {
//            return rtnParam(50020, null);
//        }
//        String wxOpenId = (String) wxSessionMap.get("openid");
//        String wxSessionKey = (String) wxSessionMap.get("session_key");
//        String unionId = (String) wxSessionMap.get("unionid");
//        Long expires = 60 * 60L;
//        if (StringUtils.isEmpty(wxOpenId)) {
//            log.error("/api/v1/wx/getSession ### wxOpenId={};wxSessionKey={}", wxOpenId, wxSessionKey);
//        }
//        String thirdSession = wxService.create3rdSession(wxOpenId, wxSessionKey, expires, createSeconds, unionId);
//        BizWxUser var1 = wxService.getWxSession(thirdSession).getBizWxUser();
//        if (var1 == null) {
//            //缓存内用户信息不在，查找数据库
//            BizWxUser bizWxUser1 = bizWxUserService.selectBizWxUser(wxOpenId);
//            if (bizWxUser1 == null) {
//                BizWxUser bizWxUser = new BizWxUser();
//                bizWxUser.setOpenid(wxOpenId);
//                bizWxUser.setUnionId(unionId);
//                bizWxUser.setType(1);
//                bizWxUser.setCreateTime(new Date());
//                bizWxUserService.insert(bizWxUser);
//            }
//        }
//        return rtnParam(0, ImmutableMap.of("sessionId", thirdSession));
    }


    @RequestMapping(value = "/api/v1/wx/verify", method = RequestMethod.POST)
    public void verify(HttpServletResponse response, HttpServletRequest request) throws Exception {
        boolean state = TokenCheckUtils.checkToken(response, request, token);
        log.error("/api/v1/wx/verify ### state={}", state);
        if (state) {
            ReceiveMsg receiveMsg = XMLUtils.XMLTOModel(request);
            log.error("/api/v1/wx/verify ### receiveMsg={}", receiveMsg);
            //指令回复
            wxService.orderReply(response, receiveMsg);
            log.error("/api/v1/wx/verify ### receiveMsg={}", "成功走完了");
            //
        } else {
            System.out.println("1");
        }
    }

    @RequestMapping(value = "/api/v1/wx/createMenu", method = RequestMethod.POST)
    public Map<String, Object> createMenu() {
        String token = wxService.getMenuAccessToken();
        if (token == null) {
            return rtnParam(500001, "失败没有token");
        }
        wxService.createMenu(token);
        return rtnParam(0, "成功");
    }


    @RequestMapping(value = "/api/v1/wx/getMenu", method = RequestMethod.POST)
    public Map<String, Object> getMenu() {
        String token = wxService.getMenuAccessToken();
        if (token == null) {
            return rtnParam(500001, "失败没有token");
        }
        wxService.getMenu(token);
        return rtnParam(0, "成功");
    }

    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getSHA1(String data) {
        try {
            //信息摘要器                                算法名称
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(data.getBytes());
            //16进制字符串
            String str = "0123456789abcdef";//把字符串转为字符串数组
            char[] ch = str.toCharArray();
            StringBuilder sb = new StringBuilder();

            //处理结果
            for (byte b : digest) {
                sb.append(ch[(b >> 4) & 15]);
                sb.append(ch[b & 15]);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
