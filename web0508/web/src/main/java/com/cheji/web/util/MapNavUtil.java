package com.cheji.web.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class MapNavUtil {
    private String startCoordinate;
    private String endCoordinate;
    private String applicationKey;
    private Integer type;
    private String output;
    private String param;
    /**
     * 必须要构造参数
     * @param startCoordinate 起点经纬度 经度在前，纬度在后
     * @param endCoordinate 终点经纬度 经度在前，纬度在后
     * @param applicationKey 高德地图应用key，需要Web服务类型的key
     */
    public MapNavUtil(String startCoordinate, String endCoordinate,
                      String applicationKey,Integer type,String output) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.applicationKey = applicationKey;
        this.type = type;
        this.output = output;
        this.param="origins="+this.startCoordinate+"&destination="+this.endCoordinate+"&type="+this.type+"&output="+this.output+"&key="+this.applicationKey;
    }
    /**
     * 获取地图导航返回值
     * @return
     */
    public Long getResults(){
        Long result = new Long(0);

        String sendGet = HttpRquestCustom.sendGet(" https://restapi.amap.com/v3/distance", param);
        JSONObject jo = new JSONObject().fromObject(sendGet);
        System.out.println(jo);
        JSONArray ja = jo.getJSONArray("results");

        result = Long.parseLong(new JSONObject().fromObject(ja.getString(0)).get("distance").toString());
        return result;

//        JSONObject jsonObject=JSONObject.fromObject(sendGet);
//        String routeJsonString = jsonObject.get("route").toString();
//        JSONObject routeObject=JSONObject.fromObject(routeJsonString);
//        JSONArray jsonArray = routeObject.getJSONArray("paths");
//        JSONObject zuiJson = jsonArray.getJSONObject(0);
//        MapNavResults mapResult=new MapNavResults();
//        mapResult.setDistance(zuiJson.get("distance").toString());
//        mapResult.setDuration(zuiJson.get("duration").toString());
//        mapResult.setTolls(zuiJson.get("tolls").toString());
//        return mapResult;
    }
}
