package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.huawei.utils.HttpsUtil;
import com.stylefeng.guns.huawei.utils.JsonUtil;
import com.stylefeng.guns.huawei.utils.StreamClosedHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallNotify {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    // 语音通知API的调用地址
    private String urlCallNotify;
    // 接口响应的消息体
    private Map<String, String> Responsebody;
    // Https实体
    private HttpsUtil httpsUtil;

    public CallNotify() {
        // 测试地址,应用上线后需要替换成商用地址
//        urlCallNotify = Constant.CALL_NOTIFY_TEST;
        // 商用地址
        urlCallNotify = Constant.CALL_NOTIFY_COMERCIAL;
        Responsebody = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    /*
     * 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置. 该示例不体现参数校验,请根据各参数的格式要求自行实现校验功能.
     * playInfoList为最大个数为5的放音内容参数列表,每个放音内容参数以Map<String,Object>格式存储,
     * 放音内容参数的构造方法请参考getplayInfo方法.
     */
    public String callNotifyAPI(String access_token, String bindNbr, String calleeNbr,
                                List<Map<String, Object>> playInfoList) throws Exception {

        httpsUtil = new HttpsUtil();

        // 忽略证书信任问题
        httpsUtil.trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(httpsUtil.hv);

        // 语音通知API有v1.0和v2.0两个版本,请提前和管理员确认使用API的版本,并修改以下的值.
        // 注意：v要求小写.
        String apiVersion = "v2.0";

        // 构造URL,
        urlCallNotify += apiVersion + "?app_key=" + Constant.CALLNOTIFYVERIFY_APPID + "&access_token=" + access_token;
        System.out.println(urlCallNotify);

        // 构造消息体
        Map<String, Object> bodys = new HashMap<>();
        bodys.put("bindNbr", bindNbr);
        bodys.put("calleeNbr", calleeNbr);
        bodys.put("playInfoList", playInfoList);
        bodys.put("displayNbr", "+8678880010135");
        String jsonRequest = JsonUtil.jsonObj2Sting(bodys);

        /*
         * Content-Type为application/json且请求方法为post时, 使用doPostJsonGetStatusLine方法构造http
         * request并获取响应.
         */
        StreamClosedHttpResponse responseCallNotify = httpsUtil.doPostJsonGetStatusLine(urlCallNotify, jsonRequest);
        urlCallNotify = Constant.CALL_NOTIFY_COMERCIAL;

        // 响应的消息体写入Responsebody.
        Responsebody = JsonUtil.jsonString2SimpleObj(responseCallNotify.getContent(), Responsebody.getClass());

        // 返回响应的status.
        return responseCallNotify.getStatusLine().toString();
    }

    /*
     * 构造playInfoList中携带的放音内容参数 使用语音文件或者v1.0版本接口的TTS文本作为放音内容
     */
    public Map<String, Object> getplayInfo(String fileorTTS) {
        Map<String, Object> body = new HashMap<String, Object>();
        // 音频文件只支持wave格式,文件名以.wav结尾
        if (fileorTTS.endsWith(".wav")) {
            body.put("notifyVoice", fileorTTS);
        } else {
            System.out.println("Only .wav file is supported.");
        }
        return body;
    }

    /*
     * 构造playInfoList中携带的放音内容参数 使用v2.0版本接口的TTS模板作为放音内容 重构getplayInfo方法
     */
    public Map<String, Object> getplayInfo(String templateId, List<String> templateParas) {
        Map<String, Object> bodys = new HashMap<String, Object>();
        bodys.put("templateId", templateId);
        bodys.put("templateParas", templateParas);
        return bodys;
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