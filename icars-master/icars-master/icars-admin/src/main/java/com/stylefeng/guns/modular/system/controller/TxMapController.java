package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.impl.appServiceImpl.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <ul>
 * <li>文件包名 : com.stylefeng.guns.modular.system.controller</li>
 * <li>创建时间 : 2019-03-19 16:37</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 * 腾讯地图
 *
 * @author duanhong
 * @version 2.0.0
 */
@Controller
@RequestMapping("/tx/map")
public class TxMapController {

    @Resource
    AppService appService;
    private static String PREFIX = "/system/countData/";

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "map.html";
    }

    /**
     * 理赔员定位获取
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/addAdjustersLocation")
    public Object addAdjustersLocation(String account,Double lat,Double lng) {
        User user = new User();
        user.setAccount(account);
        appService.addUserDes(user, lat, lng);
        return "ok";
    }

    /**
     * 理赔员定位获取
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/adjustersLocation")
    public Object adjustersLocation() {
        JSONArray allUser = appService.getAllUser();

//        SMEMBERS ONLINE_MAN  todo  获取用户名字
        return allUser;
//        JSONArray array = new JSONArray();
//        double random = (Math.random()*100);
//        System.out.println(random % 2 );
//        Integer random2 = Math.toIntExact(Math.round(random));
//        if (random2 % 2 == 0) {
//            JSONObject out1 = new JSONObject();
//            out1.put("lat", 30.74670439486628);
//            out1.put("lng", 104.12691593170166);
//            out1.put("title", "理赔员-王玉生");
//            array.add(out1);
//            JSONObject out2 = new JSONObject();
//            out2.put("lat", 30.614277);
//            out2.put("lng", 104.026794);
//            out2.put("title", "理赔员-张杰");
//            array.add(out2);
//            JSONObject out3 = new JSONObject();
//            out3.put("lat", 30.553983);
//            out3.put("lng", 104.179230);
//            out3.put("title", "理赔员-齐胜福");
//            array.add(out3);
//        }else{
//            JSONObject out1 = new JSONObject();
//            out1.put("lat", 29.573457);
//            out1.put("lng", 106.490479);
//            out1.put("title", "理赔员-王玉生");
//            array.add(out1);
//            JSONObject out2 = new JSONObject();
//            out2.put("lat", 30.614277);
//            out2.put("lng", 104.026794);
//            out2.put("title", "理赔员-张杰");
//            array.add(out2);
//            JSONObject out3 = new JSONObject();
//            out3.put("lat", 30.477083);
//            out3.put("lng", 114.257813);
//            out3.put("title", "理赔员-齐胜福");
//            array.add(out3);
//        }
//
//
//        return array;
    }
}
