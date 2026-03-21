package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONArray;
import com.jeesite.modules.constant2.JgTokenEnum;
import com.jeesite.modules.util2.HttpUtils;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.util.HashMap;
import java.util.Map;

@Component
public class JPushService {
    private final static Logger logger = LoggerFactory.getLogger(JPushService.class);
    private static String pushUrl = "https://api.jpush.cn/v3/push";   //https://api.jpush.cn/v3/push
    private static boolean apns_production = true;    //true 生产环境，false 测试环境
    private static int time_to_live = 86400;

    private static final String URL = "https://device.jpush.cn";//极光访问URL

    public static void jiguangPush(String alias, String alert, JgTokenEnum jgTokenEnum, String type) {
        try {
            String result = push(pushUrl, alias, alert, jgTokenEnum.getAppKey(), jgTokenEnum.getMasterSecret(), apns_production, time_to_live, type);
            JSONObject resData = JSONObject.fromObject(result);
            if (resData.containsKey("error")) {
                logger.error("### 信息推送失败！别名为 # alias={}", alias);
                // logger.info("### app上传 cdn地址 # replace={}", replace);
                JSONObject error = JSONObject.fromObject(resData.get("error"));
                logger.error("### 错误信息为 # error={}", error.get("message").toString());
            } else {
                logger.error("### 信息推送成功！别名为 # alias={}", alias);
            }
        } catch (Exception e) {
            logger.error("### 信息推送失败！ # e={}", e);
        }
    }

    //
    public static void main(String[] args) {
//        for (int i = 0; i < 100000; i++) {
//            if(i%2 != 0){
                     jiguangPush("13730875880", "您有红包到账", JgTokenEnum.C, "8");
       // jiguangPush("15826113405", "您有红包到账", JgTokenEnum.C, "8");
       // jiguangPush("13730875880", "zcq1", JgTokenEnum.B, "7");
      //  jiguangPush("13547930681", "wyh1", JgTokenEnum.B, "7");
//            } else{
        //jiguangPush("15826113405", "新的车己红包到账", JgTokenEnum.B, "7");
//        try {
//            String result = getAliases();
//            JSONObject resData = JSONObject.fromObject(result);
//            if (resData.containsKey("error")) {
//                logger.error("### 信息推送失败！别名为 # alias={}", "15826113405");
//                // logger.info("### app上传 cdn地址 # replace={}", replace);
//                JSONObject error = JSONObject.fromObject(resData.get("error"));
//                logger.error("### 错误信息为 # error={}", error.get("message").toString());
//            } else {
//                logger.error("### 信息推送成功！别名为 # alias={}", "15826113405");
//            }
//        } catch (Exception e) {
//            logger.error("### 信息推送失败！ # e={}", e);
//        }
//   //         }
//            try {
//                TimeUnit.SECONDS.sleep(30);
//                logger.info("推送第"+i+"次");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
    }
    // }
    //15826113405   //13547930681
    // }

    //GET /v3/aliases/{alias_value}

    /**
     * 组装极光推送专用json串
     *
     * @param alias
     * @param alert
     * @return json
     */
    public static JSONObject generateJson(String alias, String alert, boolean apns_production, int time_to_live, String type) {
        JSONObject json = new JSONObject();
        JSONArray platform = new JSONArray();//平台
        platform.add("android");
        platform.add("ios");

        JSONObject audience = new JSONObject();//推送目标
        JSONArray alias1 = new JSONArray();
        alias1.add(alias);
        audience.put("alias", alias1);

        JSONObject notification = new JSONObject();//通知内容
        JSONObject android = new JSONObject();//android通知内容
        android.put("alert", alert);
        android.put("builder_id", 1);
        JSONObject android_extras = new JSONObject();//android额外参数
        android_extras.put("type", type);
        android.put("extras", android_extras);

        JSONObject ios = new JSONObject();//ios通知内容
        ios.put("alert", alert);
        if (type.equals("6")){             //新的洗车订单
            ios.put("sound", "call2.caf");
        }else if (type.equals("7")){        //洗车订单结算
            ios.put("sound", "call3.caf");
        }else if(type.equals("8")){         //红包到账
            ios.put("sound", "call4.caf");
        }else {
            ios.put("sound", "default");
        }
        ios.put("badge", "+1");
        JSONObject ios_extras = new JSONObject();//ios额外参数
        // ios_extras.put("type", type);
        ios.put("extras", ios_extras);
        notification.put("android", android);
        notification.put("ios", ios);

        JSONObject options = new JSONObject();//设置参数
        options.put("time_to_live", Integer.valueOf(time_to_live));
        options.put("apns_production", apns_production);

        json.put("platform", platform);
        json.put("audience", audience);
        json.put("notification", notification);
        json.put("options", options);
        return json;

    }

    /**
     * 推送方法-调用极光API
     *
     * @param reqUrl
     * @param alias
     * @param alert
     * @return result
     */
    public static String push(String reqUrl, String alias, String alert, String appKey, String masterSecret, boolean apns_production, int time_to_live, String type) {
        String base64_auth_string = encryptBASE64(appKey + ":" + masterSecret);
        String authorization = "Basic " + base64_auth_string;
        return sendPostRequest(reqUrl, generateJson(alias, alert, apns_production, time_to_live, type).toString(), "UTF-8", authorization);
    }

    /**
     * 发送Post请求（json格式）
     *
     * @param reqURL
     * @param data
     * @param encodeCharset
     * @param authorization
     * @return result
     */
    @SuppressWarnings({"resource"})
    public static String sendPostRequest(String reqURL, String data, String encodeCharset, String authorization) {
        HttpPost httpPost = new HttpPost(reqURL);
        //   HttpGet httpGet = new HttpGet(reqURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        String result = "";
        try {
            StringEntity entity = new StringEntity(data, encodeCharset);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", authorization.trim());
            response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), encodeCharset);
        } catch (Exception e) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            client.getConnectionManager().shutdown();
        }

        return result;
    }

    /**
     * 　　　　* BASE64加密工具
     */
    public static String encryptBASE64(String str) {
        byte[] key = str.getBytes();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String strs = base64Encoder.encodeBuffer(key);
        return strs;
    }


    public static String getAliases() {
        JSONObject response = null;
        try {
            StringBuilder url = new StringBuilder(URL).append("/v3/aliases/" + "15826113405");
            String authorValue = encryptBASE64(JgTokenEnum.C.getAppKey() + ":" + JgTokenEnum.C.getMasterSecret());
            Map<String, String> para = new HashMap<>();

             response = HttpUtils.doGet(url.toString(), "Basic " + authorValue, para);
            //{"registration_ids":["1114a89792c16a66295","1114a89792cd7572aac","161a3797c86ef8554ff","141fe1da9ec37314351","1517bfd3f7afb6d78b3","171976fa8ae9c5c8455","191e35f7e0dd084cc1f","121c83f76043705c31d","121c83f76041ef9f99c","171976fa8aeb2b2c5da"]}
            //["1517bfd3f7a32718f70","161a3797c860eb1bd4a","191e35f7e02fa18ee96","141fe1da9ec37314351","1114a89792cd029697e","171976fa8aeb2b2c5da","18171adc0363f7ea31b","191e35f7e0dd084cc1f","1114a89792cdbe5aab6","101d85590923a4f7ff5"]}
          //  String phone = "{\"registration_ids\":{\"remove\": [\"13165ffa4e6e9fc15ab\",\"1517bfd3f7a09289963\",\"191e35f7e02014bb86d\",\"121c83f760b64cda0bc\",\"141fe1da9e38b7ad50d\",\"13165ffa4e90972faad\",\"1a1018970ac015c6630\",\"191e35f7e02c3ba1ecf\",\"141fe1da9e393962de2\"]}}";
           // JSONObject json = JSONObject.fromObject(phone);
           //  response = HttpUtils.doPost(url.toString(), "Basic " + authorValue, json);
            //response = HttpUtils.doDelete(url.toString(), "Basic " + authorValue, para);
//                    JSONObject jsonResponse = (JSONObject) response.get("error"),
//            String string2 = (String) jsonResponse.get("message");
//            logger.info("---------" + string2);
//            if (StringUtils.isNotEmpty((String) response.get("error"))) {
            logger.info("getAliases:url+params----" + url + para);
            return String.valueOf(response.get("error"));
            //       }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getAliases:url+params----" + e);
        }
        return String.valueOf(response);
    }


    /**
     *      * post请求
     *      * @param url
     *      * @param authorValue
     *      * @param json
     *      * @return
     *      
     */
//    public static JSONObject doPost(String url, String authorValue, JSONObject json) {
//        DefaultHttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost(url);
//        if (StringUtils.isNotEmpty(authorValue)) {
//            post.setHeader("Authorization", authorValue);
//        }
//        JSONObject response = null;
//        try {
//            response = getJsonObject(json, client, post);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return response;
//    }


}
