package com.stylefeng.guns.modular.system.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GaoDeUtils {

    private static final String key = "a11e3020a4b82ce9390044286910f02f";

    /**
     * 根据地址查询经纬度
     *
     * @param address
     * @return
     */
    public static JSONObject getLngAndLat(String address) {
        JSONObject positionObj = new JSONObject();

        try {
            // 拼接请求高德的url
            String url = "http://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=JSON&key=" + key;
            // 请求高德接口
            String result = HttpUtils.sendHttpGet(url);
            JSONObject resultJOSN = JSONObject.parseObject(result);
            System.out.println("高德接口返回原始数据：");
            System.out.println(resultJOSN);
            JSONArray geocodesArray = resultJOSN.getJSONArray("geocodes");
            if (geocodesArray.size() > 0) {
                String position = geocodesArray.getJSONObject(0).getString("location");
                String[] lngAndLat = position.split(",");
                String longitude = lngAndLat[0];
                String latitude = lngAndLat[1];
                positionObj.put("longitude", longitude);
                positionObj.put("latitude", latitude);
            }
            geocodesArray.getJSONObject(0).getString("location");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return positionObj;
    }


    /**
     * 高德地图--根据经纬度获取详细地址
     */
    public static String getGDAddress(String lat, String lng)throws IOException {
        String location = lng + "," + lat;
        // String urlString = "http://restapi.amap.com/v3/geocode/regeo?key="+key+"&s=rsv3&location="+location+"&radius=1000&callback="+jsonp+"&platform=JS&logversion=2.0&sdkversion=1.4.15&appname=http%3A%2F%2Flbs.amap.com%2Fconsole%2Fshow%2Fpicker&csid=5DF11A07-FC1F-4A5D-9E17-DF450BE76D7A";
        String urlString = "https://restapi.amap.com/v3/geocode/regeo?output=xml&location=" + location + "&key=" + key + "";

        String res = "";
      //  HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
//                logger.info("\n【根据经纬度信息获取实际的地理位置】getAdd----->调用获取位置的百度API地址,urlString={},经纬度：log={},lat={}",
//                        urlString, log, lat);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
           // logger.error("\n【根据经纬度信息获取实际的地理位置】getAdd----->异常信息:" + e.getMessage(), e);
        }

        int indexbegin = res.indexOf("<formatted_address>");
        int indexend = res.indexOf("</formatted_address>");
        if (indexbegin < 0 || indexend < 0 || indexend <= indexbegin + 19) {
            return "";
        }
        // 截取指定index之间的数据
        return res.substring(indexbegin + 19, indexend);
    }


}
