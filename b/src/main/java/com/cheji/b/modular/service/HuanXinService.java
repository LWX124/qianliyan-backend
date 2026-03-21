package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheji.b.constant.HuanxinConst;
import com.cheji.b.constant.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * <ul>
 * <li>文件包名 : com.stylefeng.guns.huanxin</li>
 * <li>创建时间 : 2019-03-13 14:57</li>
 * http://docs-im.easemob.com/im/server/ready/user  文档地址
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 * 环信操作工具类
 *
 * @author duanhong
 * @version 2.0.0
 */
@Component
public class HuanXinService {
    private final static Logger logger = LoggerFactory.getLogger(HuanXinService.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 获取管理员权限
     * 获取token
     */
    public String getToken() {
        logger.error("### 环形token过期 ###  重新获取token");
        JSONObject in = new JSONObject();
        in.put("grant_type", "client_credentials");
        in.put("client_id", HuanxinConst.clientId);
        in.put("client_secret", HuanxinConst.clientSecret);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(HuanxinConst.getTokenUrl, in, String.class);
        String body = responseEntity.getBody();
        JSONObject parse = JSON.parseObject(body);
        System.out.println(parse.getString("access_token"));
        //由于网络延迟等其他原因   key的有效期减200秒
        stringRedisTemplate.opsForValue().set(RedisConstant.HUANXIN_TOKEN, parse.getString("access_token"),
                parse.getInteger("expires_in") - 200, TimeUnit.SECONDS);
        return body;
    }

    /**
     * 注册单个用户
     */
    /**
     * @param userName 用户登录账号
     * @param passWord 用户密码
     * @param nickname 用户别名
     * @return {"duration":98,"path":"/users","application":"9cb6cf70-44b0-11e9-8e3b-578660e0147a","entities":[{"created":1552487982729,"nickname":"ç\u0090\u0086èµ\u0094å\u0091\u0098-æ®µæ´ª","modified":1552487982729,"type":"user","uuid":"d6ca879a-459d-11e9-8eed-9dbea3b97dea","username":"18180765139","activated":true}],"organization":"1108190312098629","action":"post","uri":"https://a1.easemob.com/1108190312098629/chejilp500/users","applicationName":"chejilp500","timestamp":1552487982729}
     */
    public JSONObject singleRegister(String userName, String passWord, String nickname, boolean exceptionFlag) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)) {
            //用户名和密码不能为空，昵称可以为空
            logger.info("环信注册单个用户失败 ### userName={};### passWord={}", userName, passWord);
            return null;
        }
        JSONObject in = new JSONObject();
        JSONArray array = new JSONArray();
        in.put("username", userName);
        in.put("password", passWord);
        in.put("nickname", nickname);
        array.add(in);
        HttpEntity<JSONArray> requestEntity = new HttpEntity<>(array, getHeader());
        JSONObject out = null;
        logger.info("### 注册环信  requestEntity ### requestEntity={}", requestEntity);
        try {
            out = exePost(requestEntity, HuanxinConst.singleRegisterUrl);
        } catch (HttpClientErrorException errorException) {
            errorException.printStackTrace();
            if (active.equals("test")) {
                //重新获取token;
                if (errorException.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    if (!exceptionFlag) {
                        getToken();
                        singleRegister(userName, passWord, nickname, true);
                    }
                }
                errorException.printStackTrace();
            } else {
                logger.error("####token过期  ####  请手动更新token");
            }
        }
        logger.error("#### out={}  #### ",out);
        return out;
    }

    /**
     * 修改 环信昵称
     */
    /**
     * @param userName 用户登录账号
     * @param nickname 用户别名
     *  该方法不可用
     */
    @Deprecated
    public JSONObject updateHxNickName(String userName, String nickname, boolean exceptionFlag) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(nickname)) {
            //用户名和密码不能为空，昵称可以为空
            logger.info("修改 环信昵称 入参错误  ### userName={};### nickname={}", userName, nickname);
            return null;
        }
        JSONObject in = new JSONObject();
        JSONArray array = new JSONArray();
        in.put("nickname", nickname);
        array.add(in);
        HttpEntity<JSONArray> requestEntity = new HttpEntity<>(array, getHeader());
        JSONObject out = null;
        try {
            out = exePost(requestEntity, HuanxinConst.updateNickNameUrl +"/"+ userName);
        } catch (HttpClientErrorException errorException) {
            if (active.equals("pro")) {
                //重新获取token;
                if (errorException.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    if (!exceptionFlag) {
                        getToken();
                        updateHxNickName(userName, nickname, true);
                    }
                }
                errorException.printStackTrace();
            } else {
                logger.error("####token过期  ####  请手动更新token");
            }
        }
        return out;
    }

    /**
     * RestTemplate 环信专用，获取环信统一header
     *
     * @return
     */
    private HttpHeaders getHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.HUANXIN_TOKEN);
        requestHeaders.add("Authorization", "Bearer " + s);
        requestHeaders.add("Accept", "application/json");
        return requestHeaders;
    }

    /**
     * RestTemplate 环信专用，执行post操作
     *
     * @param entity 请求体
     * @return
     */
    private static JSONObject exePost(HttpEntity entity, String url) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, entity, String.class);
        JSONObject out = JSONObject.parseObject(result);
        return out;
    }

    /**
     * 给用户添加好友
     *
     * @param owner_username  用户账号
     * @param friend_username 好友账号
     * @return {"duration":70,"path":"/users/d6ca879a-459d-11e9-8eed-9dbea3b97dea/contacts","application":"9cb6cf70-44b0-11e9-8e3b-578660e0147a","entities":[{"notification_no_disturbing":false,"created":1552458809903,"notification_display_style":1,"nickname":"æµ\u008Bè¯\u0095001","modified":1552458825373,"type":"user","uuid":"ea6e8bf0-4559-11e9-9603-4bf28b25018d","username":"ceshi001","activated":true}],"organization":"1108190312098629","action":"post","uri":"https://a1.easemob.com/1108190312098629/chejilp500/users/d6ca879a-459d-11e9-8eed-9dbea3b97dea/contacts","applicationName":"chejilp500","timestamp":1552489226023}
     */
    public JSONObject addFriend(String owner_username, String friend_username, boolean exceptionFlag) {
        if (StringUtils.isEmpty(owner_username) || StringUtils.isEmpty(friend_username)) {
            logger.info("环信给目标用户添加好友失败 ### owner_username={};### friend_username={}", owner_username, friend_username);
            return null;
        }
        JSONObject in = new JSONObject();
        in.put("owner_username", owner_username);
        in.put("friend_username", friend_username);
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(in, getHeader());

        JSONObject out = null;
        try {
            out = exePost(requestEntity, HuanxinConst.addFriendUrl + "/" + owner_username + "/contacts/users/" + friend_username);
        } catch (HttpClientErrorException errorException) {
            if (active.equals("pro")) {
                //重新获取token;
                if (errorException.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    if (!exceptionFlag) {
                        getToken();
                        addFriend(owner_username, friend_username, true);
                    }
                    logger.error("###token过期####");
                }
                errorException.printStackTrace();
            } else {
                logger.error("####token过期  ####  请手动更新token");
            }

        }
        return out;
    }

    /**
     * 查看某个用户的好友列表
     *
     * @param owner_username 目标用户的环信账号
     * @return
     */
    public JSONObject findFriendInfo(String owner_username, boolean exceptionFlag) {
        if (StringUtils.isEmpty(owner_username)) {
            logger.info("查看某个用户的好友列表 ### owner_username={}", owner_username);
            return null;
        }
        HttpEntity requestEntity = new HttpEntity(null, getHeader());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = null;
        try {
//            System.out.println(HuanxinConst.friendInfoUrl + "/" + owner_username + "/contacts/users");
            exchange = restTemplate.exchange(HuanxinConst.friendInfoUrl + "/" + owner_username + "/contacts/users", HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException errorException) {
            errorException.printStackTrace();
            if (active.equals("pro")) {
                //重新获取token;
                if (errorException.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    if (!exceptionFlag) {
                        getToken();
                        findFriendInfo(owner_username, true);
                    }
                    logger.error("###token过期####");
                }
                errorException.printStackTrace();
            } else {
                logger.error("####token过期  ####  请手动更新token");
            }

        }
        if (exchange != null) {
            return JSONObject.parseObject(exchange.getBody());
        }
        return null;
    }

    /**
     * 查看所有用户
     *
     * @return
     */
    public JSONObject findAllUser() {
        HttpEntity requestEntity = new HttpEntity(null, getHeader());
        RestTemplate restTemplate = new RestTemplate();

        //restTemplate中文乱码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> exchange = null;
        try {
            exchange = restTemplate.exchange(HuanxinConst.friendAllUserUrl + "?limit=1000", HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException errorException) {
            //重新获取token;
            logger.error("###token过期####");
            errorException.printStackTrace();
        }
        return JSONObject.parseObject(exchange.getBody());
    }

    public void initNewUser(String userName, String nickName) {
        //先注册，如果注册通过，则初始化好友
        try {
            singleRegister(userName, "123456", nickName, false);
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#######环信初始化好友失败##################userName={},nickName={}", userName, nickName);
            return;
        }
        JSONArray entities = findAllUser().getJSONArray("entities");
        for (int i = 0; i < entities.size(); i++) {
            JSONObject job = entities.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            addFriend(userName, job.getString("username"), true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        //获取token
//        System.out.println(HuanXinUtil.getToken());


        //注册单个用户
//        HuanXinUtil.singleRegister("18180765139", "123456", "理赔员-段洪");

        //添加好友
//        System.out.println(HuanXinUtil.addFriend("18180765139", "ceshi001"));
//
        //查看某个人的好友

//        String owner_username = "13198511506";
//        HttpHeaders requestHeaders = new HttpHeaders();
////        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        requestHeaders.add("Authorization", "Bearer YWMtRds7koI6EemVC93XwWBnQgAAAAAAAAAAAAAAAAAAAAGcts9wRLAR6Y47V4Zg4BR6AgMAAAFrBLcmwgBPGgDRYlnCNJ34x1IaA9RPL8xweP8D0TTjW_-Zop8oa1zMTQ");
//        requestHeaders.add("Accept", "application/json");
//
//        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> exchange = null;
//        try {
////            System.out.println(HuanxinConst.friendInfoUrl + "/" + owner_username + "/contacts/users");
//            exchange = restTemplate.exchange(HuanxinConst.friendInfoUrl + "/" + owner_username + "/contacts/users", HttpMethod.GET, requestEntity, String.class);
//        } catch (HttpClientErrorException errorException) {
//            errorException.printStackTrace();
//        }
//        System.out.println(exchange.getBody());


//        ----------------------------------------------------------------------------------

//        ResponseEntity<String> friendAllUserUrl = null;
//                RestTemplate restTemplate = new RestTemplate();
//                HttpHeaders requestHeaders = new HttpHeaders();requestHeaders.add("Authorization", "Bearer YWMtRds7koI6EemVC93XwWBnQgAAAAAAAAAAAAAAAAAAAAGcts9wRLAR6Y47V4Zg4BR6AgMAAAFrBLcmwgBPGgDRYlnCNJ34x1IaA9RPL8xweP8D0TTjW_-Zop8oa1zMTQ");
//        requestHeaders.add("Accept", "application/json");
//        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
//
//        try {
//            ///{org_name}/{app_name}/users/{owner_username}/contacts/users/{friend_username}
//            friendAllUserUrl = restTemplate.exchange(HuanxinConst.friendAllUserUrl + "/{owner_username}/contacts/users/{friend_username}", HttpMethod.GET, requestEntity, String.class);
//        } catch (HttpClientErrorException errorException) {
//            //重新获取token;
//            logger.error("###token过期####");
//            errorException.printStackTrace();
//        }
//        System.out.println();
//
//        JSONArray entities = JSONObject.parseObject(friendAllUserUrl.getBody()).getJSONArray("entities");
//
//        for (int i = 0; i < entities.size(); i++) {
//            JSONObject obj = entities.getJSONObject(i);
//            String account = obj.getString("username");
//
//            System.out.println(obj);
//        }
    }
}
