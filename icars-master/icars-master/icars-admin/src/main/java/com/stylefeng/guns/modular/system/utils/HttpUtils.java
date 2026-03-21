package com.stylefeng.guns.modular.system.utils;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    public static JSONObject doPost(String url, String authorValue, String json) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        if (StringUtils.isNotEmpty(authorValue)) {
            post.setHeader("Authorization", authorValue);
        }
        JSONObject response = null;
        try {
            json.getBytes(StandardCharsets.UTF_8);
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity);// 返回json格式：
            response = JSONObject.fromObject(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private static JSONObject getJsonObject(JSONObject json, DefaultHttpClient client, HttpEntityEnclosingRequestBase base) throws IOException {
        JSONObject response;
        StringEntity s = new StringEntity(json.toString());
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");//发送json数据需要设置contentType
        base.setEntity(s);

        HttpResponse res = client.execute(base);
        HttpEntity entity = res.getEntity();
        String result = EntityUtils.toString(entity);// 返回json格式：
        response = JSONObject.fromObject(result);
        return response;
    }


    /**
     *      * delete请求
     *      * @param url
     *      * @param authorValue
     *      * @param dataForm
     *      * @return
     *      
     */
//    public static JSONObject doDelete(String url, String authorValue, Map<String, Object> dataForm) {
//        HttpClient httpClient = new HttpClient();
//        DeleteMethod deleteMethod = new DeleteMethod(url);
//        if (StringUtils.isNotEmpty(authorValue)) {
//            deleteMethod.setRequestHeader("Authorization", authorValue);
//        }
//        List<NameValuePair> data = new ArrayList<NameValuePair>();
//        if (dataForm != null) {
//            Set<String> keys = dataForm.keySet();
//            for (String key : keys) {
//                NameValuePair nameValuePair = new NameValuePair(key, (String) dataForm.get(key));
//                data.add(nameValuePair);
//            }
//        }
//        deleteMethod.setQueryString(data.toArray(new NameValuePair[0]));
//        try {
//            int statusCode = httpClient.executeMethod(deleteMethod);
//// Read the response body.
//            byte[] responseBody = deleteMethod.getResponseBody();
//            String s = new String(responseBody);
//           // System.out.println("s:"+s);
////            int i = s.indexOf("{");
////            s = s.substring(i);
////            int p = s.lastIndexOf("}");
////            s = s.substring(i - 1, p + 1);
////            s = s.replaceAll("\\\\", "");
////            JSONObject jsonObject = JSONObject.fromObject(s);
////            return jsonObject;
//            return JSONObject.fromObject(new String(responseBody));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            deleteMethod.releaseConnection();
//        }
//        return null;
//
//    }
    public static JSONObject doGet(String url, String authorValue, Map<String, String> para) {
        JSONObject response = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();

            URIBuilder builder = new URIBuilder(url);
            if (para != null) {
                Set<String> set = para.keySet();
                for (String key : set) {
                    builder.setParameter(key, para.get(key));
                }
            }
            HttpGet request = new HttpGet(builder.build());
            if (StringUtils.isNotEmpty(authorValue)) {
                request.setHeader("Authorization", authorValue);
            }
            request.setHeader("ContentTye", "application/json");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(6000)
                    .setConnectTimeout(6000)
                    .setConnectionRequestTimeout(6000).build();
            request.setConfig(requestConfig);


            HttpResponse res = client.execute(request);
//  if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(res.getEntity());// 返回json格式：
            response = JSONObject.fromObject(result);
//  }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * 发送Get请求
     *
     * @param url
     * @return
     */
    public static String sendHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(10000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        String result = "";
        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
            HttpEntity entity = closeableHttpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
