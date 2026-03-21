package com.cheji.web.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class VinUtil {
    public static JSONObject getVin(String vin) {
        String host = "https://slyvinstd.market.alicloudapi.com";
        String path = "/vin/query";
        String method = "GET";
        String appcode = "321589b1df7348a5a66e37c4a0eccd3d";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("vin", vin);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String s = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONObject parse = (JSONObject) JSONObject.parse(s);
            JSONObject data = (JSONObject) parse.get("data");
            JSONObject result = (JSONObject) data.get("result");
            JSONObject object = new JSONObject();
            object.put("transmission", result.get("geartype"));
            object.put("fuel", result.get("fueltype"));
            object.put("brand", result.get("name"));
            object.put("productDate", result.get("listdate"));
            object.put("displacement", result.get("displacement"));
            return object;
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        JSONObject vin = VinUtil.getVin("LFVVB9E63M5014994");
        JSONObject vin = VinUtil.getVin("LVHFC1666H6157181");
        System.out.println(vin);
    }
//    public static JSONObject getVin(String vin){
//        String host = "https://jisuvindm.market.alicloudapi.com";
//        String path = "/vin/query";
//        String method = "GET";//GET/POST 任意
//        String appcode = "321589b1df7348a5a66e37c4a0eccd3d";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        //根据API的要求，定义相对应的Content-Type
//        headers.put("Content-Type", "application/json; charset=UTF-8");
//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("vin", vin);
//
//        try {
//            HttpResponse httpResponse = HttpUtils.doGet(host, path, method, headers, querys);
//            //获取response的body
//            String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
//            JSONObject parse = (JSONObject)JSONObject.parse(s);
//            JSONObject result = (JSONObject)parse.get("result");
//            JSONObject object = new JSONObject();
//            object.put("transmission",result.get("geartype"));
//            object.put("fuel",result.get("fueltype"));
//            object.put("brand",result.get("name"));
//            object.put("productDate",result.get("listdate"));
//            return object;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
