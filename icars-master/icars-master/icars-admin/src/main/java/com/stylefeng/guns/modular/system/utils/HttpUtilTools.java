package com.stylefeng.guns.modular.system.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
 
import java.util.Map;
 
public class HttpUtilTools {
 
    /**
     * 发起HTTP请求（支持GET和POST）
     *
     * @param serverPrefix 服务器前缀
     * @param urlSuffix    URL路径后缀
     * @param method       HTTP请求方法
     * @param headers      请求头
     * @param params       请求参数（POST时为JSON体，GET时为查询参数）
     * @return 请求结果字符串
     */
    public static String sendRequest(String serverPrefix, String urlSuffix, String method, Map<String, String> headers, Map<String, Object> params) {
        HttpRequest request;
        if ("GET".equalsIgnoreCase(method)) {
            request = HttpRequest.get(serverPrefix + urlSuffix);
            if (params != null) {
                params.forEach(request::form);
            }
        } else if ("POST".equalsIgnoreCase(method)) {
            String jsonStr = JSONUtil.toJsonStr(params);
            request = HttpRequest.post(serverPrefix + urlSuffix)
                    .body(jsonStr);
            headers.put("Content-Type", "application/json"); // 默认设置Content-Type为JSON
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
 
        // 设置请求头
        if (headers != null) {
            headers.forEach(request::header);
        }
 
        // 执行请求并获取响应
        HttpResponse response = request.execute();
        return response.body();
    }
 
    // 便捷的GET请求方法
    public static String sendGetRequest(String serverPrefix, String urlSuffix, Map<String, String> headers, Map<String, Object> params) {
        return sendRequest(serverPrefix, urlSuffix, "GET", headers, params);
    }
 
    // 便捷的POST请求方法
    public static String sendPostRequest(String serverPrefix, String urlSuffix, Map<String, String> headers, Map<String, Object> params) {
        return sendRequest(serverPrefix, urlSuffix, "POST", headers, params);
    }
}