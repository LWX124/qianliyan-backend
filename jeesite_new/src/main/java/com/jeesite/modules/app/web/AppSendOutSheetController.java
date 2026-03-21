/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.HuaweiSmsService;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * web派单记录表Controller
 *
 * @author zcq
 * @version 2020-09-24
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appSendOutSheet")
public class AppSendOutSheetController extends BaseController {

    @Autowired
    private AppSendOutSheetService appSendOutSheetService;

    @Resource
    private FileUploadServiceExtendImpl fileUploadServiceExtend;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private HuaweiSmsService huaweiSmsService;

    @Resource
    private CallPhoneService callPhoneService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private WebSocketService webSocketService;

    @Resource
    private ihuyiService ihuyiService;

    @Resource
    private AppAccidentRecordService appAccidentRecordService;

    private static String key = "ab0017c871f764bc88dfb59858c39d81";

    private static String serverid = "207013";

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppSendOutSheet get(Integer id, boolean isNewRecord) {
        return appSendOutSheetService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appSendOutSheet:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppSendOutSheet appSendOutSheet, Model model) {
        model.addAttribute("appSendOutSheet", appSendOutSheet);
        return "modules/app/appSendOutSheetList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appSendOutSheet:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppSendOutSheet> listData(AppSendOutSheet appSendOutSheet, HttpServletRequest request, HttpServletResponse response) {
        appSendOutSheet.setPage(new Page<>(request, response));
        Page<AppSendOutSheet> page = appSendOutSheetService.findPage(appSendOutSheet);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appSendOutSheet:view")
    @RequestMapping(value = "form")
    public String form(AppSendOutSheet appSendOutSheet, Model model) {
        model.addAttribute("appSendOutSheet", appSendOutSheet);
        return "modules/app/appSendOutSheetForm";
    }

    /**
     * 保存web派单记录表
     */
    @RequiresPermissions("app:appSendOutSheet:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppSendOutSheet appSendOutSheet) {
        appSendOutSheetService.save(appSendOutSheet);
        return renderResult(Global.TRUE, text("保存web派单记录表成功！"));
    }

    /**
     * 删除web派单记录表
     */
    @RequiresPermissions("app:appSendOutSheet:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppSendOutSheet appSendOutSheet) {
        appSendOutSheetService.delete(appSendOutSheet);
        return renderResult(Global.TRUE, text("删除web派单记录表成功！"));
    }


    //    @RequiresPermissions("app:appSendOutSheet:view")
    @RequestMapping(value = "sendSinglePage")
    public String sendSinglePage(AppSendOutSheet appSendOutSheet, Model model) {
        model.addAttribute("AppSendOutSheet", appSendOutSheet);
        return "modules/app/sendSinglePage";
    }


    //新增数据
    @GetMapping(value = "/addSend")
    public JSONObject addSend(String lat, String lng, String address) {
        JSONObject result = new JSONObject();
        //查询到重复事故的url
        //List<AppAccidentRecord> stringList =  appAccidentRecordService.selectRepart(accId,type);
//        AppSendOutSheet appSendOutSheet = new AppSendOutSheet();
//        appSendOutSheet.setAdress(address);
//        appSendOutSheet.setLat(new BigDecimal(lat));
//        appSendOutSheet.setLng(new BigDecimal(lng));
//        appSendOutSheet.setCreateTime(new Date());
//        appSendOutSheet.setUpdateTime(new Date());
//        appSendOutSheetService.addSendSheet(appSendOutSheet);
        //新建一个accident订单
        AppAccidentRecord appAccidentRecord = new AppAccidentRecord();
        //添加一个默认图片
//        https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/4bb8e7002e4ca33cea8863ebeba7183.png
        appAccidentRecord.setLng(new BigDecimal(lng).doubleValue());
        appAccidentRecord.setLat(new BigDecimal(lat).doubleValue());
        appAccidentRecord.setCheckid("调度");
        appAccidentRecord.setStatuse(2);
        appAccidentRecord.setAddress(address);
        appAccidentRecord.setType(1);
        appAccidentRecord.setIsaddvideo(0);
        appAccidentRecord.setDelgeo("1");
        appAccidentRecord.setCreateTime(new Date());
        appAccidentRecord.setUpdateTime(new Date());
        appAccidentRecord.setCheckTime(new Date());
        appAccidentRecord.setUserId("55");
        appAccidentRecord.setImgUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/4bb8e7002e4ca33cea8863ebeba7183.png");
        appAccidentRecordService.insetNewAcc(appAccidentRecord);
        webSocketService.sendMessageToAllUsers(new TextMessage("1"));
        return result;
    }


    @PostMapping(value = "/claimAdjusters")
    public String claimAdjusters(Model model, String sendId) {
        //查询到重复事故的url
        //List<AppAccidentRecord> stringList =  appAccidentRecordService.selectRepart(accId,type);
        AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(sendId);
//        AppSendOutSheet appSendOutSheet = new AppSendOutSheet();
//
//        appSendOutSheet.setAdress(address);
//        appSendOutSheet.setLat(new BigDecimal(lat));
//        appSendOutSheet.setLng(new BigDecimal(lng));
        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/claimAdjusters";
    }


    @PostMapping(value = "/findTrack")
    public String findTrack(Model model, String sid) {
        //查询到重复事故的url
        //List<AppAccidentRecord> stringList =  appAccidentRecordService.selectRepart(accId,type);
        AppSendOutSheet appSendOutSheet = new AppSendOutSheet();
        appSendOutSheet.setId(sid);
        model.addAttribute("appSendOutSheet", appSendOutSheet);
        return "modules/app/findTrack";
    }


    @GetMapping(value = "/sendSingle")
    public String sendSingle(Model model, String sendId, String id) {
        //查询到重复事故的url
        //List<AppAccidentRecord> stringList =  appAccidentRecordService.selectRepart(accId,type);
        AppSendOutSheet appSendOutSheet = appSendOutSheetService.get(sendId);
        appSendOutSheet.setAdress(sendId);
        appSendOutSheet.setId(id);
        model.addAttribute("appSendOutSheet", appSendOutSheet);
        return "modules/app/sendUploadFile";
    }


    @RequestMapping(value = "/findClaimAdjusters")
    @ResponseBody
    public JSONObject findClaimAdjusters(String lat, String lng) {
        JSONObject result = new JSONObject();
        //查询数据
//        List<AppUser> claimList = appSendOutSheetService.findNowClaimAdjusters();
        List<EmployeeClaim> employeeClaims = new ArrayList<>();

//        for (AppUser appUser : claimList) {
//            EmployeeClaim employeeClaim = new EmployeeClaim();
//            employeeClaim.setId(appUser.getParentId());
//            employeeClaim.setName(appUser.getUsername());
//            employeeClaim.setPhone(appUser.getPhoneNumber());
//
//            AppBUser bByPhone = appBUserService.findBByPhone(appUser.getPhoneNumber());
//
//            //查询距离
//            BigDecimal distance = appSendOutSheetService.getdistance(lat,lng,bByPhone);
//            employeeClaim.setDistance(distance);
//
//            employeeClaims.add(employeeClaim);
//        }
        //查询当前位置的理赔老师
        AppAccidentRecord appAccidentRecord = new AppAccidentRecord();
        appAccidentRecord.setLat(Double.parseDouble(lat));
        appAccidentRecord.setLng(Double.parseDouble(lng));

        Circle circle = new Circle(appAccidentRecord.getLng(), appAccidentRecord.getLat(), 50.0 * 1000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.CLAIM_TEACHER_ADD, circle, args);

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
            Distance distance = geoLocationGeoResult.getDistance();
            RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
            String name = conten.getName();
            JSONObject jsonObject = JSONObject.parseObject(name);
            String merchantsName = jsonObject.getString("merchantsName");
            String id1 = jsonObject.getString("id");
            double value = distance.getValue();
            BigDecimal bigDecimal = new BigDecimal(value);
            BigDecimal divide = bigDecimal.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
            EmployeeClaim employeeClaim = new EmployeeClaim();
            employeeClaim.setId(Integer.parseInt(id1));
            employeeClaim.setName(merchantsName);
            employeeClaim.setDistance(divide);
            employeeClaim.setPhone(appUserService.get(id1).getPhoneNumber());
            employeeClaims.add(employeeClaim);
        }

        result.put("code", 0);
        result.put("msg", "");
        result.put("count", employeeClaims.size());
        result.put("data", employeeClaims);
        return result;
    }


    //派单给理赔老师，新增一条数据
    @RequestMapping(value = "/addSendOutSheet")
    @ResponseBody
    public JSONObject addSendOutSheet( Integer sendId, String remark, String url) {
        JSONObject result = new JSONObject();
        //去掉userID第一个字符
        AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(sendId.toString());
        try {
            if (StringUtils.isNotEmpty(remark)){
                appAccidentRecord.setIntroduce(remark);
            }
            if (StringUtils.isNotEmpty(url)){
                appAccidentRecord.setImgUrl(url);
            }
            appAccidentRecordService.update(appAccidentRecord);
            result.put("stat", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("stat", "false");
        }
        return result;
    }


    //上传图片
    @ResponseBody
    @RequestMapping("upload")
    public JSONObject upload(MultipartFile file, HttpServletRequest request) throws IOException {

        JSONObject res = new JSONObject();

        //创建uuid
        String fileName = UUID.randomUUID().toString().substring(0, 10);
        String s1 = fileName.replaceAll("-", "");

//        for (int i = 0; i < files.length; i++) {
//            MultipartFile file = files[i];
//            }
        String s = fileUploadServiceExtend.uploadWebimg(file, s1 + ".jpg");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("src", s);
        res.put("code", 0);
        res.put("msg", "");
        res.put("data", map2);

        return res;


    }


    //查询地址
    @RequestMapping(value = "/findSendOutTrack")
    @ResponseBody
    public JSONObject findSendOutTrack(String sid) {
        JSONObject result = new JSONObject();
        //查询到经纬度
        //查询到必要参数
        AppSendOutSheet appSendOutSheet = appSendOutSheetService.get(sid);
        String falconTrajectory = appSendOutSheet.getFalconTrajectory();
        String tid = appSendOutSheet.getTid();
        Date createTime = appSendOutSheet.getTcTime();
        Date checkTime = appSendOutSheet.getCheckTime();

        if (checkTime == null) {
            result.put("result", "false");
            result.put("msg", "该派单还未完成");
            return result;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createString = formatter.format(createTime);
        Long starttime = Date2TimeStamp(createString);

        String checkString = formatter.format(checkTime);
        Long endtime = Date2TimeStamp(checkString);
        Integer pagesize = 999;

        String url = "https://tsapi.amap.com/v1/track/terminal/trsearch?key=" + key + "&sid=" + serverid + "&tid=" + tid + "&trid=" + falconTrajectory + "&starttime=" + starttime + "&endtime=" + endtime + "&pagesize=" + pagesize + "";
        String gdres = sendHttpGet(url);
        JSONObject resultJOSN = JSONObject.parseObject(gdres);

        List<Track> tracksList = new ArrayList<>();

        JSONObject data = resultJOSN.getJSONObject("data");
        JSONArray tracks = data.getJSONArray("tracks");

        //拿到count
        //看遍历几次
        //添加数据


        for (int i = 0; i < tracks.size(); i++) {
            JSONObject object = tracks.getJSONObject(i);
            //Track stu = JSON.toJavaObject(object, Track.class);
            JSONArray points = object.getJSONArray("points");
            for (int j = 0; j < points.size(); j++) {
                JSONObject jsonPoint = points.getJSONObject((j));
                Track stu = JSON.toJavaObject(jsonPoint, Track.class);
                tracksList.add(stu);
            }
        }
        result.put("data", tracksList);
        result.put("status", 1);
        return result;
    }


    //查询签到图片
    @RequestMapping(value = "/findSendOutImg", method = RequestMethod.GET)
    public JSONObject findIndentImg(String indentId) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        List<String> imgs = appSendOutSheetService.findCheckImg(indentId);
        result.put("imgList", imgs);
        return result;
    }


    private Long Date2TimeStamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public String sendHttpGet(String url) {
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