package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.huawei.utils.HttpsUtil;
import com.stylefeng.guns.huawei.utils.JsonUtil;
import com.stylefeng.guns.huawei.utils.StreamClosedHttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRecordLink {
    // 获取录音文件下载地址API的URL
    private String urlRecordFile;
    // Https实体
    private HttpsUtil httpsUtil;
    // 接口响应的消息体
    private Map<String, String> Responsebody;
    // 录音文件下载地址
    private String Location;
    
    public GetRecordLink() {
        // 商用地址
        urlRecordFile = Constant.VOICE_FILE_DOWNLOAD;
        
        Responsebody = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public String getRecordLinkAPI(String access_token, String fileName, String recordDomain) throws Exception {

        httpsUtil = new HttpsUtil();

        // 忽略证书信任问题
        httpsUtil.trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(httpsUtil.hv);

        // 构造URL
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();
        keyValues.add(new BasicNameValuePair("app_key", Constant.CLICK2CALL_APPID));
        keyValues.add(new BasicNameValuePair("access_token", access_token));
        keyValues.add(new BasicNameValuePair("fileName", fileName));
        keyValues.add(new BasicNameValuePair("recordDomain", recordDomain));

        /*
         * Content-Type为application/json且请求方法为get时, 使用doGetWithParasGetStatusLine方法构造http
         * request并获取响应.
         */
        StreamClosedHttpResponse respRecordLink = httpsUtil.doGetWithParasGetStatusLine(urlRecordFile, keyValues, null);

        // 响应的消息体写入Responsebody.
        Responsebody = JsonUtil.jsonString2SimpleObj(respRecordLink.getContent(), Responsebody.getClass());

        // 从响应头域中获取location.
        String code = respRecordLink.getStatusLine().toString();
        if (code.indexOf("301") != -1) {
            Location = respRecordLink.getFirstHeader("Location").getValue();
        } else {
            Location = "";
        }

        // 返回响应的status.
        return code;
    }
    
    // 获取整个响应消息体
    public Map<String, String> getResponsebody() {
        return this.Responsebody;
    }

    // 获取响应消息体中的单个参数
    public String getResponsePara(String ParaName) {
        return this.Responsebody.get(ParaName);
    }
    
 // 获取location
    public String getLocation() {
        return this.Location;
    }
}