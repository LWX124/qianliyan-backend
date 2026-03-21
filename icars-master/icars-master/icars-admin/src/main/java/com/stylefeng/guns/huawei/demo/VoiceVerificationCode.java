package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.huawei.utils.HttpsUtil;
import com.stylefeng.guns.huawei.utils.JsonUtil;
import com.stylefeng.guns.huawei.utils.StreamClosedHttpResponse;

import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;
import java.util.Map;

public class VoiceVerificationCode {
    // 语音验证码API的URL
    private String urlVoiceVerificationCode;
    // 接口响应的消息体
    private Map<String, String> responsebody;
    // Https实体
    private HttpsUtil httpsUtil;

    public VoiceVerificationCode() {
        // 测试地址,应用上线后需要替换成商用地址
        urlVoiceVerificationCode = Constant.VOICE_VERIFICATION_TEST;
        // 商用地址
        // urlVoiceVerificationCode = Constant.VOICE_VERIFICATION_COMERCIAL;
        responsebody = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    /*
     * 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置. 该示例不体现参数校验,请根据各参数的格式要求自行实现校验功能.
     */
    public String voiceVerificationCodeAPI(String access_token, String calleeNbr, int languageType, String preTone,
            String verifyCode) throws Exception {

        httpsUtil = new HttpsUtil();

        // 忽略证书信任问题
        httpsUtil.trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(httpsUtil.hv);

        // 构造URL
        urlVoiceVerificationCode += "?app_key=" + Constant.CALLNOTIFYVERIFY_APPID + "&access_token=" + access_token;

        // 构造消息体
        Map<String, Object> bodys = new HashMap<String, Object>();
        bodys.put("calleeNbr", calleeNbr);
        bodys.put("languageType", languageType);
        bodys.put("preTone", preTone);
        bodys.put("verifyCode", verifyCode);
        String jsonRequest = JsonUtil.jsonObj2Sting(bodys);

        /*
         * Content-Type为application/json且请求方法为post时, 使用doPostJsonGetStatusLine方法构造http
         * request并获取响应.
         */
        StreamClosedHttpResponse responseVoiceVerificationCode = httpsUtil
                .doPostJsonGetStatusLine(urlVoiceVerificationCode, jsonRequest);

        // 响应的消息体写入responsebody.
        responsebody = JsonUtil.jsonString2SimpleObj(responseVoiceVerificationCode.getContent(),
                responsebody.getClass());

        // 返回响应的status.
        return responseVoiceVerificationCode.getStatusLine().toString();
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