package com.stylefeng.guns.modular.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.config.properties.WxAuthProperties;
import com.stylefeng.guns.core.util.HttpRequest;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.modular.system.constant.WxSession;
import com.stylefeng.guns.modular.system.dao.AccdMapper;
import com.stylefeng.guns.modular.system.dao.BizWxPayRecordMapper;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IBizWxUserService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.utils.HttpUtilTools;
import com.stylefeng.guns.modular.system.utils.HttpUtils;
import com.stylefeng.guns.modular.system.vo.AccidentListVo;
import com.stylefeng.guns.modular.system.vo.ReceiveMsg;
import com.stylefeng.guns.modular.system.vo.ReplyMsg;
import com.stylefeng.guns.modular.system.vo.WxMyMessageVo;
import com.stylefeng.guns.modular.system.vo.wxmenuvo.*;
import com.stylefeng.guns.wx.AES;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("styleFengWxService")
public class WxService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    private WxAuthProperties wxAuthProperties;

    @Resource
    private IBizWxUserService bizWxUserService;

    @Resource
    private IUserService userService;

    @Resource
    private JedisUtil jedisUtil;

    @Resource
    private BizWxPayRecordMapper bizWxPayRecordMapper;

    @Resource
    private AccdMapper accdMapper;

//    private static Map<String, WxSession> WX_APP_SESSION_CACHE = new ConcurrentHashMap<String, WxSession>();


    /**
     * 根据小程序登录返回的code获取openid和session_key
     * https://mp.weixin.qq.com/debug/wxadoc/dev/api/api-login.html?t=20161107
     *
     * @param wxCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createWxSession(String wxCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=").append(wxAuthProperties.getAppId());
        sb.append("&secret=").append(wxAuthProperties.getSecret());
        sb.append("&js_code=").append(wxCode);
        sb.append("&grant_type=").append(wxAuthProperties.getGrantType());
        log.info("HTTP GET:");
        log.info("URL = " + wxAuthProperties.getSessionHost() + ", param : " + sb.toString());
        String res = HttpRequest.sendGet(wxAuthProperties.getSessionHost(), sb.toString());
        log.info("RESPONSE =" + res);
        if (res == null || res.equals("")) {
            return null;
        }
        return JSON.parseObject(res, Map.class);
    }

    public WxSession getWxSession(String thirdSessionKey) {

//        WxSession wxSession = WX_APP_SESSION_CACHE.get(thirdSessionKey);
        String s = jedisUtil.get("XCX_LOGIN_" + thirdSessionKey);
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        jedisUtil.expire("XCX_LOGIN_" + thirdSessionKey, 60 * 60L);
        JSONObject jsonObject = JSONObject.parseObject(s);

        String openId = jsonObject.getString("openId");
        String wxSessionKey = jsonObject.getString("wxSessionKey");
        String unionId = jsonObject.getString("unionId");
        Long expires = jsonObject.getLong("expires");
        Long createSeconds = jsonObject.getLong("createSeconds");
        JSONObject bizWxUser = jsonObject.getJSONObject("bizWxUser");
        JSONObject userJson = jsonObject.getJSONObject("user");

        BizWxUser bizWxUserBean = null;
        if (bizWxUser != null) {
            bizWxUserBean = JSON.toJavaObject(bizWxUser, BizWxUser.class);
        }
        User user = null;
        if (userJson != null) {
            user = JSON.toJavaObject(userJson, User.class);
        }
        WxSession wxSession = new WxSession(openId, wxSessionKey, expires, createSeconds, bizWxUserBean, user, unionId);
        if (wxSession != null) {
            wxSession.setCreateSeconds(System.currentTimeMillis() / 1000);
//            if ((System.currentTimeMillis() / 1000) <= (wxSession.getExpires() + wxSession.getCreateSeconds())){
//                remove(thirdSessionKey);
//                wxSession = null;
//            }
        }
        return wxSession;
    }

    /**
     * 缓存微信openId和session_key
     *
     * @param wxOpenId     微信用户唯一标识
     * @param wxSessionKey 微信服务器会话密钥
     * @param expires      会话有效期, 以秒为单位, 例如2592000代表会话有效期为30天
     * @param unionId
     * @return
     */
    public String create3rdSession(String wxOpenId, String wxSessionKey, Long expires, Long createSeconds, String unionId) {
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxOpenId);
        if (bizWxUser == null) {
            log.error("创建小程序登录信息 bizWxUser==null wxOpenId={}", wxOpenId);
        }
        User user = null;
        if (bizWxUser != null && StringUtils.isNotEmpty(bizWxUser.getPhone())) {
            List<User> users = userService.getByPhone(bizWxUser.getPhone());
            user = (users == null || users.size() != 1) ? null : users.get(0);
        }
        if (user == null) {
            log.error("缓存微信openId和session_key user为null   wxOpenId={}", wxOpenId);
        }
        String s = JSONObject.toJSONString(new WxSession(wxOpenId, wxSessionKey, expires, createSeconds, bizWxUser, user, unionId));
        jedisUtil.set("XCX_LOGIN_" + thirdSessionKey, s, 60 * 60);
        return thirdSessionKey;
    }

    /**
     * 缓存微信openId和session_key
     *
     * @param sessionId     系统生成的sessionid
     * @param iv            加密算法的初始向量
     * @param encryptedData 微信返回的用户私密信息的加密字符串
     * @return
     */
    public String getDecodeUserInfo(String sessionId, String iv, String encryptedData) throws UnsupportedEncodingException {

        WxSession wxSession = getWxSession(sessionId);
        if (wxSession != null) {
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(wxSession.getWxSessionKey()), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                String userInfo = new String(resultByte, "UTF-8");
                return userInfo;
            }
        }
        return null;
    }

    public void remove(String sessionId) {
//        if(StringUtils.isNotEmpty(sessionId)){
//            WX_APP_SESSION_CACHE.remove(sessionId);
//        }
    }

    public void testRedis(String wxCode) {
        jedisUtil.set("test" + wxCode, wxCode, 60 * 60);
    }

    /**
     * 发送微信code获取tokan
     *
     * @param appId
     * @param appSecret
     * @param wxCode
     * @return
     */
    public Map<String, Object> getAccessToken(String appId, String appSecret, String wxCode) throws IOException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + appId + "&secret=" + appSecret + "&code=" + wxCode + "&grant_type=authorization_code";

        // 发送HTTP GET请求
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        // 获取响应
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // 返回 JSON 格式的结果
        return (Map<String, Object>) JSON.parse(response.toString());
    }

    public JSONObject orderReply(HttpServletResponse response, ReceiveMsg receiveMsg) throws IOException {
        JSONObject mav = new JSONObject();
//        if (receiveMsg.getMsgType().equals(MsgTypeConstant.EVENT) && receiveMsg.getEvent().equals("subscribe")) {
//            //关注
//            receiveMsg.setContent("关注");
//        }
        log.error("receiveMsg  receiveMsg={}", receiveMsg);
        ReplyMsg replyMsg = new ReplyMsg();
        replyMsg.setMsgType("text");
        replyMsg.setFromUserName(receiveMsg.getToUserName());
        replyMsg.setToUserName(receiveMsg.getFromUserName());
        replyMsg.setCreateTime(System.currentTimeMillis());
        //指令未存在于指令库中
        replyMsg.setContent("感谢关注！为了保证你的红包能及时到账，请务必点击 <a href='https://amiba.tech/mp/auth.html'>绑定账号</a>");
        String msg = objectToXml(replyMsg);
        log.error("orderReplyMsg信息  msg={}", msg);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.close();
//        mongoTemplate.save(replyMsg);
        mav.put("msg", "操作成功");
        return mav;
    }

    /**
     * 将文本消息对象转化成XML格式
     *
     * @param message 文本消息对象
     * @return 返回转换后的XML格式
     */

    public static String objectToXml(ReplyMsg message) {

        XStream xs = new XStream();

//由于转换后xml根节点默认为class类，需转化为<xml>

        xs.alias("xml", message.getClass());

        return xs.toXML(message);

    }


    public static Menu getMenu() {
//        ViewButton btn11 = new ViewButton();
//        btn11.setName("提报事故");
//        btn11.setUrl("https://amiba.tech/index.html");
//
//        ViewButton btn12 = new ViewButton();
//        btn11.setName("联系我们");
//        btn11.setUrl("https://amiba.tech/index.html");

//        ClickButton btn21 = new ClickButton();
//        btn21.setName("测试21");
//        btn21.setKey("21");
//
//        ClickButton btn22 = new ClickButton();
//        btn22.setName("测试22");
//        btn22.setKey(Menu.VIEW);

        //一级菜单(没有二级菜单)
        ComplexMenu mainBtn1 = new ComplexMenu();
        mainBtn1.setName("提报事故");
//        mainBtn1.setUrl("https://amiba.tech/index.html");
        mainBtn1.setType("miniprogram");
        mainBtn1.setAppId("wx580effc9532bd2a6");
        mainBtn1.setPagePath("pages/index/index");

        //一级菜单(有二级菜单)
        ComplexMenu mainBtn2 = new ComplexMenu();
        mainBtn2.setName("联系我们");
        mainBtn2.setUrl("https://amibahw.tech/imgs/7986190b8d4cfd2a53a92a565e7740e.jpg");
        mainBtn2.setType("view");

        Menu menu = new Menu();
        menu.setButton(new BasicButton[]{mainBtn1, mainBtn2});
        return menu;

    }

    public void createMenu(String token) {
        Menu menu = getMenu();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", token);
//        String string = JSONObject.toJSONString(menu);
//        log.error("menu的参数 menu={}", string);
//        net.sf.json.JSONObject doPost = HttpUtils.doPost(url, "", string);
        log.error("menu的参数 menu={}", JSONObject.toJSONString(menu));
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(menu);
        Map<String, String> getHeaders = new HashMap<>();
        String s = HttpUtilTools.sendPostRequest("https://api.weixin.qq.com/cgi-bin/menu/create?", "access_token=" + token, getHeaders, stringObjectMap);
        log.error("doPost的参数 doPost={}", s);
    }

    public String getMenuAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        url = url.replace("APPID", "wx4f9e7cdc772e9379").replace("APPSECRET", "46eda183ad01e2d4e4c778cd9447d2e2");
        net.sf.json.JSONObject jsonObject = HttpUtils.doGet(url, "", null);
        log.error("getAccessToken返参:" + jsonObject);
        if (jsonObject.containsKey("errcode")) {
            return null;
        }
        return jsonObject.getString("access_token") != null ? jsonObject.getString("access_token") : null;
    }

    public void getMenu(String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", token);
        net.sf.json.JSONObject jsonObject = HttpUtils.doGet(url, "", null);
        log.error("getAccessToken返参:" + jsonObject);
    }

    public WxMyMessageVo getMyMessage(String openId) {
        BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(openId);
        Integer id = bizWxUser.getId()+1000;
        WxMyMessageVo wxMyMessageVo = new WxMyMessageVo();
        wxMyMessageVo.setUserIdNumber(id);
        //查询该用户所有红包奖励总额
        BigDecimal amount = bizWxPayRecordMapper.getAllAmount(openId);
        wxMyMessageVo.setReportAmount(amount);
        //查询提报次数
        Integer count = accdMapper.selectCount(
                new EntityWrapper<Accident>().eq("openid", openId)
        );
        wxMyMessageVo.setSubmitNumber(count);
        //查询用户信息
        return wxMyMessageVo;
    }

    public List<AccidentListVo> mySubmitList(String openId) {
        //查询提交记录
        List<Accident> accidentList = accdMapper.selectList(new EntityWrapper<Accident>()
                .eq("openid", openId)
                .orderBy("createTime", false)
        );
        List<AccidentListVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accidentList)){
             vos = accidentList.stream().map(acc -> {
                AccidentListVo vo = new AccidentListVo();
                vo.setAccId(acc.getId());
                vo.setUrl(acc.getVideo());
                vo.setAddress(acc.getAddress());
                vo.setIsPass(acc.getStatus());
                vo.setReason(acc.getReason());
                return vo;
            }).collect(Collectors.toList());

        }
        return vos;
    }
    /**
     * 使用ConcurrentHashMap储存登录信息时 需要清除登录信息
     */
//    public void clean(){
//        int oldCount = WX_APP_SESSION_CACHE.size();
//        int removeCount = 0;
//        Iterator<Map.Entry<String, WxSession>> it = WX_APP_SESSION_CACHE.entrySet().iterator();
//        while (it.hasNext()){
//            WxSession wxSession = it.next().getValue();
//            BizWxUser bizWxUser = bizWxUserService.selectBizWxUser(wxSession.getOpenId());
//            if(bizWxUser.getUserType() == 0){
//                //普通用户，session如果存在超过1小时就清除
//                if(((System.currentTimeMillis()/1000) - wxSession.getCreateSeconds()) >= wxSession.getExpires()){
//                    it.remove();
//                    removeCount++;
//                }
//            } else {
//                //内部员工，session如果存在超过1天就清除
//                if(((System.currentTimeMillis()/1000) - wxSession.getCreateSeconds()) >= 60*60*24){
//                    it.remove();
//                    removeCount++;
//                }
//            }
//        }
//        log.info("微信用户缓存清理数量："+oldCount+" -> "+removeCount);
//    }
}
