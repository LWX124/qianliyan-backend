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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> createWxSession(@RequestParam(required = true, value = "code") String wxCode) {
        long createSeconds = System.currentTimeMillis() / 1000;
        Map<String, Object> wxSessionMap = wxService.createWxSession(wxCode);

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
        String thirdSession = wxService.create3rdSession(wxOpenId, wxSessionKey, expires, createSeconds, unionId);
        BizWxUser var1 = wxService.getWxSession(thirdSession).getBizWxUser();
        if (var1 == null) {
            //缓存内用户信息不在，查找数据库
            BizWxUser bizWxUser1 = bizWxUserService.selectBizWxUser(wxOpenId);
            if (bizWxUser1 == null) {
                BizWxUser bizWxUser = new BizWxUser();
                bizWxUser.setOpenid(wxOpenId);
                bizWxUser.setUnionId(unionId);
                bizWxUser.setType(1);
                bizWxUser.setCreateTime(new Date());
                bizWxUserService.insert(bizWxUser);
            }
        }
        return rtnParam(0, ImmutableMap.of("sessionId", thirdSession));
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
