package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.huawei.utils.HttpsUtil;
import com.stylefeng.guns.huawei.utils.JsonUtil;
import com.stylefeng.guns.huawei.utils.StreamClosedHttpResponse;
import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auth {
    // 业务平台提供的应用标示和应用密匙
    private String app_key;
    private String app_secret;
    // API调用的URL
    private String urlLogin;
    private String urlRefresh;
    private String urlLogout;
    private String urlOceanStor;
    // 接口响应的消息体
    private Map<String, String> responsebody;
    // Https实体
    private HttpsUtil httpsUtil;

    public Auth(String appKey, String appSecret) {
        app_key = appKey;
        app_secret = appSecret;
        urlLogin = Constant.APP_AUTH;
        urlRefresh = Constant.REFRESH_TOKEN;
        urlLogout = Constant.DELETE_AUTH;
        urlOceanStor = Constant.REFRESH_OCEANSTOR;
        responsebody = new HashMap<>();
    }

    /*
     * 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置. 该示例不体现SSL证书鉴权,请自行实现.
     * 该示例不体现参数校验,请根据各参数的格式要求自行实现校验功能.
     */
    @SuppressWarnings("unchecked")
    public String fastlogin(String username, String password) throws Exception {

        httpsUtil = new HttpsUtil();

        // 构造URL
        urlLogin += "?app_key=" + app_key + "&username=" + username;

        // 构造头域
        Map<String, String> headers = new HashMap<>();
        headers.put(Constant.HEADER_APP_AUTH, password);

        /*
         * Content-Type为application/x-www-form-urlencoded且请求方法为post,
         * 使用doPostFormUrlEncodedGetStatusLine方法构造http request并获取响应.
         */
        StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, headers);

        // 响应的消息体写入responsebody.
        responsebody = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), responsebody.getClass());
        // 返回响应的status.
        return responseLogin.getStatusLine().toString();
    }

    @SuppressWarnings("unchecked")
    public String refresh(String refresh_token) throws Exception {

        httpsUtil = new HttpsUtil();

        // 构造消息体
        Map<String, Object> body = new HashMap<>();
        body.put("app_key", app_key);
        body.put("app_secret", app_secret);
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refresh_token);
        List<NameValuePair> requestBody = httpsUtil.paramsConverter(body);

        /*
         * Content-Type为application/x-www-form-urlencoded且请求方法为post,
         * 使用doPostFormUrlEncodedGetStatusLine方法构造http request并获取响应.
         */
        StreamClosedHttpResponse responseRefresh = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlRefresh, requestBody);

        // 响应的消息体写入responsebody.
        responsebody = JsonUtil.jsonString2SimpleObj(responseRefresh.getContent(), responsebody.getClass());

        // 返回响应的status.
        return responseRefresh.getStatusLine().toString();
    }

    @SuppressWarnings("unchecked")
    public String logout(String access_token) throws Exception {

        httpsUtil = new HttpsUtil();

        // 构造URL
        urlLogout += "?app_key=" + app_key + "&" + "access_token=" + access_token;

        /*
         * Content-Type为application / x-www-form-urlencoded且请求方法为post时,
         * 使用doPostFormUrlEncodedGetStatusLine方法构造http request并获取响应.
         */
        StreamClosedHttpResponse responseLogout = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogout);

        // 响应的消息体写入responsebody.
        responsebody = JsonUtil.jsonString2SimpleObj(responseLogout.getContent(), responsebody.getClass());

        // 返回响应的status.
        return responseLogout.getStatusLine().toString();
    }

    @SuppressWarnings("unchecked")
    public String refreshOceanStor(String access_token, String oldAK) throws Exception {

        httpsUtil = new HttpsUtil();

        // 构造URL
        urlOceanStor += "?app_key=" + app_key + "&" + "access_token=" + access_token;

        // 构造消息体
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("AK", oldAK);
        String jsonRequest = JsonUtil.jsonObj2Sting(body);

        /*
         * Content-Type为application/json且请求方法为post时, 使用doPostJsonGetStatusLine方法构造http
         * request并获取响应.
         */
        StreamClosedHttpResponse responseOceanStor = httpsUtil.doPostJsonGetStatusLine(urlOceanStor, jsonRequest);

        // 响应的消息体写入responsebody.
        responsebody = JsonUtil.jsonString2SimpleObj(responseOceanStor.getContent(), responsebody.getClass());

        // 返回响应的status.
        return responseOceanStor.getStatusLine().toString();
    }

    // 获取整个响应消息体
    public Map<String, String> getResponsebody() {
        return this.responsebody;
    }

    // 获取响应消息体中的单个参数
    public String getResponsePara(String paraName) {
        return this.responsebody.get(paraName);
    }
}