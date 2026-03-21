package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.huawei.utils.HttpsUtil;
import com.stylefeng.guns.huawei.utils.JsonUtil;
import com.stylefeng.guns.huawei.utils.StreamClosedHttpResponse;

import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;
import java.util.Map;

public class VoiceCall {
    // 语音回呼API的URL
    private String urlVoiceCall;
    // 接口响应的消息体
    private Map<String, String> Responsebody;
    // Https实体
    private HttpsUtil httpsUtil;

    public VoiceCall() {
        // 测试地址,应用上线后需要替换成商用地址
        urlVoiceCall = Constant.VOICE_CALL_TEST;
        // 商用地址
        // urlVoiceCall = Constant.VOICE_CALL_COMERCIAL;
        Responsebody = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    /*
     * 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置. 该示例不体现参数校验,请根据各参数的格式要求自行实现校验功能.
     */
    public String voiceCallAPI(String access_token, String callerNbr, String calleeNbr) throws Exception {

        httpsUtil = new HttpsUtil();

        // 忽略证书信任问题
        httpsUtil.trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(httpsUtil.hv);

        // 构造URL
        urlVoiceCall += "?app_key=" + Constant.CLICK2CALL_APPID + "&access_token=" + access_token;

        // 构造消息体
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("callerNbr", callerNbr);
        bodys.put("calleeNbr", calleeNbr);
        String jsonRequest = JsonUtil.jsonObj2Sting(bodys);

        /*
         * Content-Type为application/json且请求方法为post时, 使用doPostJsonGetStatusLine方法构造http
         * request并获取响应.
         */
        StreamClosedHttpResponse responseVoiceCall = httpsUtil.doPostJsonGetStatusLine(urlVoiceCall, jsonRequest);

        // 响应的消息体写入Responsebody.
        Responsebody = JsonUtil.jsonString2SimpleObj(responseVoiceCall.getContent(), Responsebody.getClass());

        // 返回响应的status.
        return responseVoiceCall.getStatusLine().toString();
    }

    // 获取整个响应消息体
    public Map<String, String> getResponsebody() {
        return this.Responsebody;
    }

    // 获取响应消息体中的单个参数
    public String getResponsePara(String ParaName) {
        return this.Responsebody.get(ParaName);
    }
}