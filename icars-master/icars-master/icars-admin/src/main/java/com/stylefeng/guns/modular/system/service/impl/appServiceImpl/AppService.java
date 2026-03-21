package com.stylefeng.guns.modular.system.service.impl.appServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.core.util.ExtString;
import com.stylefeng.guns.core.util.JedisUtil;
import com.stylefeng.guns.huawei.demo.SmsTest;
import com.stylefeng.guns.modular.system.constant.AppSession;
import com.stylefeng.guns.modular.system.constant.HuaWei;
import com.stylefeng.guns.modular.system.constant.RedisKey;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserService userService;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private SmsTest smsTest;


    private static Map<String, AppSession> APP_APP_SESSION_CACHE = new ConcurrentHashMap<String, AppSession>();


    /**
     * 缓存APPsession
     *
     * @param expires 会话有效期, 以秒为单位, 例如2592000代表会话有效期为30天
     * @return
     */
    public String createAppSession(User user, Long expires, Long createSeconds) {
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        //上一次存储的登录key
//       try{
//           String s = jedisUtil.get("APP_LOGIN_" + user.getAccount());
//           jedisUtil
////           APP_APP_SESSION_CACHE.remove(s);
//       }catch (Exception e){
//           e.printStackTrace();
//       }
//
        AppSession appSession = new AppSession(expires, createSeconds, user);
//        APP_APP_SESSION_CACHE.put(thirdSessionKey, new AppSession(expires, createSeconds, user));
        jedisUtil.set("APP_LOGIN_" + thirdSessionKey, JSONObject.toJSONString(appSession), expires);
        return thirdSessionKey;
    }


    /**
     * 获取APPsession
     *
     * @param
     * @return
     */
    public AppSession getAppSession(String thirdSessionKey) {
//        long createSeconds = System.currentTimeMillis() / 1000;
//        AppSession appSession = APP_APP_SESSION_CACHE.get(thirdSessionKey);

//        if (appSession == null) {
//            return null;
//        }
//        if (createSeconds - appSession.getCreateSeconds() > appSession.getExpires()) {
//            //过期appSession
//            remove(thirdSessionKey);
//            return null;
//        }

        String str = jedisUtil.get("APP_LOGIN_" + thirdSessionKey);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(str);
        AppSession appSession = new AppSession();
        appSession.setCreateSeconds(jsonObject.getLong("createSeconds"));
        appSession.setExpires(jsonObject.getLong("expires"));
        appSession.setUser(JSON.toJavaObject(jsonObject.getJSONObject("user"), User.class));

        Long expires = 30 * 24 * 60 * 60L;
        jedisUtil.expire("APP_LOGIN_" + thirdSessionKey, expires);
        return appSession;
    }


    /**
     * 移除个别APPsession
     *
     * @param
     * @return
     */
    public void remove(String sessionId) {
        if (StringUtils.isNotEmpty(sessionId)) {
            jedisUtil.del("APP_LOGIN_" + sessionId);
        }
    }

    /**
     * 定时清理APPsession
     *
     * @param
     * @return
     */
//    public void clean() {
//        int oldCount = APP_APP_SESSION_CACHE.size();
//        int removeCount = 0;
//        Iterator<Map.Entry<String, AppSession>> it = APP_APP_SESSION_CACHE.entrySet().iterator();
//        while (it.hasNext()) {
//            AppSession appxSession = it.next().getValue();
//            //内部员工，session如果存在超过1天就清除
//            if (((System.currentTimeMillis() / 1000) - appxSession.getCreateSeconds()) >= 60 * 60 * 24) {
//                it.remove();
//                removeCount++;
//            }
//        }
//        log.info("app用户缓存清理数量：" + oldCount + " -> " + removeCount);
//    }

    /**
     * 短信发送方法
     *
     * @param phone
     * @return
     */

    public JSONObject sendSms(String flag, String phone, String name) {
        JSONObject obj = new JSONObject();
        //生成随机验证码
        String randomCode = ExtString.getRandomCode(6);

        if (flag.equals("3")) {
            randomCode = name;
        }

        smsTest.sendSmsByTemplate(flag, phone, randomCode);

        //录入缓存当中
        jedisUtil.setEx("SMSCODE_" + phone, randomCode, 300);

        obj.put("retCode", "0");
        obj.put("retMsg", "成功");
        return obj;
    }

    /**
     * 校验短信
     *
     * @param phone
     * @param smsCode
     * @return
     */
    public Boolean checkSms(String phone, String smsCode) {
        if (jedisUtil.exists("SMSCODE_" + phone)) {
            if (jedisUtil.get("SMSCODE_" + phone).equals(smsCode)) {
                jedisUtil.del("SMSCODE_" + phone);
                return true;
            } else {
                log.error("###短信验证失败### 缓存验证码={};用户输入验证码={};用户手机号={}", jedisUtil.get("SMSCODE_" + phone), smsCode, phone);
            }
        } else {
            log.error("###短信验证 reids为null###  phone={},smsCode={}", phone, smsCode);
        }
        return false;
    }

    /**
     * 上传理赔员坐标
     */
    public void addUserDes(User user, Double lat, Double lng) {
        jedisUtil.geoADD("location", lng, lat, "CLAIM_" + user.getAccount());
        jedisUtil.setSet("ONLINE_MAN", user.getName());
        setStatus(user.getAccount(), "1");
    }


    /**
     * 查询版本号
     */
    public JSONObject getVersion(String versionCode) {
        log.debug("####查询版本号###versionCode=?", versionCode);
        Integer versionDouble = 0;
        try {
            versionDouble = Integer.parseInt(versionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (versionDouble == null || versionDouble == 0) {
            return null;
        }
        String outStr = jedisUtil.get(RedisKey.CJ_LI_APP);
        log.debug("####查询版本号###outStr=?", outStr);
        if (StringUtils.isEmpty(outStr)) {
            return null;
        }

        JSONObject outJson = JSONObject.parseObject(outStr);
        log.debug("####查询版本号###outJson=?", outJson);
        if (outJson.getInteger("VERSIONCODE") == null || outJson.getInteger("VERSIONCODE") <= versionDouble) {
            return null;
        }

        return outJson;
    }


    /**
     * 清理理赔员坐标 腾讯坐标
     */
    public void removeUserDes(User user) {

        jedisUtil.geoDEL("location", "CLAIM_" + user.getAccount());
        setStatus(user.getAccount(), "0");
        jedisUtil.delSet("ONLINE_MAN", user.getName());

    }

    /**
     * 获取理赔员是否在工作状态
     */
    public String getStatus(String account) {
        String s = jedisUtil.get("WORKING_" + account);
        if (s == null || "".equals(s)) {
            s = "0";
        }
        return s;
    }

    /**
     * 获取理赔员是否在工作状态
     */
    public void setStatus(String account, String flag) {
        jedisUtil.set("WORKING_" + account, flag);

    }

    /**
     * 算法中查询最近的理赔员
     */
    public User getClosestUser(Double lat, Double lng) {
        User byAccount = null;
        List list = jedisUtil.geoRadius("location", lng, lat, 50, "km", true);
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int j = 0; j < list.size(); j++) {
            String o1 = String.valueOf(list.get(j));
            String account = o1.substring(6);
            byAccount = userService.getByAccount(account);
            if (byAccount == null) {
                return null;
            }
            //获取到最近用户
            String black_flag = byAccount.getBlack_flag();
            String sfkf = byAccount.getSfkf();
            if ("Y".equals(black_flag)) {
                //清理黑名单用户坐标
                byAccount = null;
                continue;
            } else {
                break;
            }
        }
        return byAccount;
    }

    /**
     * 算法中查询最近的理赔员
     */
    public JSONArray getAllUser() {
        final Double lat = 30.656779D;
        final Double lng = 104.065676;
        List list = jedisUtil.geoRadius("location", lng, lat, 3000, "km", true);
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONArray resultArray = new JSONArray();
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
        for (int i = 0; i < array.size(); i++) {
            String key = array.getString(i);
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            List location = jedisUtil.geoPos("location", key);
            if (location == null || location.size() == 0) {
                continue;
            }
            JSONObject resultObj = new JSONObject();
            resultObj.put("lng", location.get(0));
            resultObj.put("lat", location.get(1));

            User byAccount = userService.getByAccount(key.split("_")[1]);
            if (byAccount == null) {
                continue;
            }
            resultObj.put("title", byAccount.getName());
            resultArray.add(resultObj);
        }
        return resultArray;
    }


    /**
     * 获取聊天AK
     */
    public JSONObject getAk() {
        String ak = jedisUtil.get(RedisKey.HUA_WEI_AK);
        if (StringUtils.isEmpty(ak)) {
            JSONObject obj = new JSONObject();
            obj.put("userName", HuaWei.USER_NAME);
            obj.put("accessKeyId", HuaWei.ACCESS_KEY_ID);
            obj.put("secretAccessKey", HuaWei.SECRET_ACCESS_KEY);
            jedisUtil.set(RedisKey.HUA_WEI_AK, obj.toJSONString());

            return obj;
        }
        return JSONObject.parseObject(ak);
    }

    public String getHxFriendList() {
        String s = jedisUtil.get(RedisKey.HUANXIN_FRIEND_LIST);
        return s;
    }

    public String getHxFriendList(String roleId) {
        if (roleId.equals("7") || roleId.equals("8")) {
            return jedisUtil.get(RedisKey.HUANXIN_FRIEND_LIST_4S);
        }
        return jedisUtil.get(RedisKey.HUANXIN_FRIEND_LIST_LP);
    }

}
