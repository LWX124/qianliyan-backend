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
import com.jeesite.modules.app.dao.AppAccidentRecordDao;
import com.jeesite.modules.app.dao.AppCarBrandDao;
import com.jeesite.modules.app.dao.AppUserAccountRecordDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.util2.HuaweiSmsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * app上报事故信息表Controller
 *
 * @author zcq
 * @version 2019-08-29
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAccidentRecord")
public class AppAccidentRecordController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(AppAccidentRecordController.class);

    @Resource
    private AppAccidentRecordService appAccidentRecordService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppPushBillService appPushBillService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppVideoService appVideoService;

    @Resource
    private BizAccidentService bizAccidentService;

    @Resource
    private AppAccidentRecordDao appAccidentRecordDao;

    @Resource
    private AppCarBrandDao appCarBrandDao;

    @Resource
    private AppCarBrandService appCarBrandService;

    @Resource
    private HuaweiSmsService huaweiSmsService;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    @Resource
    private AppMessageCarService appMessageCarService;

    @Resource
    private BizWxUserService bizWxUserService;


    @Resource
    private ihuyiService ihuyiService;

    @Resource
    private AppIndentService appIndentService;

    @Resource
    private AppSendUrlService appSendUrlService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppAccidentRecord get(Long id, boolean isNewRecord) {
        return appAccidentRecordService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppAccidentRecord appAccidentRecord, Model model) {
        //查询今日普通红包个数   微信红包和app红包个数
        Integer appCount = appAccidentRecordService.findCommonappCount();   //普通红包个数
        if (appCount == null) {
            appCount = 0;
        }
        Integer wxCount = appAccidentRecordService.findWxCommonCount();     //微信红包个数
        if (wxCount == null) {
            wxCount = 0;
        }
        appAccidentRecord.setRedEnvelopes(String.valueOf(appCount + wxCount));
        //查询总共今日普通红包金额  微信和app红包金额
        BigDecimal CommonAppAmount = appAccidentRecordService.findCommonAppAmount();  //普通红包金额
        if (CommonAppAmount == null) {
            CommonAppAmount = new BigDecimal(0);
        }
        BigDecimal WxAmount = appAccidentRecordService.findWxAmount();              //微信红包金额
        if (WxAmount == null) {
            WxAmount = new BigDecimal(0);
        }
        appAccidentRecord.setEnvelopeAmount(String.valueOf(CommonAppAmount.add(WxAmount)));
        //查询今日plus会员红包金额
        BigDecimal plusAmount = appAccidentRecordService.findPlusAmount();         //plus红包金额
        if (plusAmount == null) {
            plusAmount = new BigDecimal(0);
        }
        appAccidentRecord.setPlusEnevlopeAmount(String.valueOf(plusAmount));
        //查询今日plus会员红包个数
        Integer plusCount = appAccidentRecordService.findPlusCount();               //plus会员金额
        if (plusCount == null) {
            plusCount = 0;
        }
        appAccidentRecord.setPlusEnevlopeCount(String.valueOf(plusCount));
        //查询今日事故个数
        Integer todayCount = appAccidentRecordService.findTodayCount();
        if (todayCount == null) {
            todayCount = 0;
        }
        appAccidentRecord.setTodayAccident(String.valueOf(todayCount));
        //查询今日小程序上传
        Integer todayWxCount = appAccidentRecordService.findTodayWxCount();
        if (todayWxCount == null) {
            todayWxCount = 0;
        }
        appAccidentRecord.setTodayWxup(String.valueOf(todayWxCount));
        //查询今日app上传
        Integer todayAppCount = appAccidentRecordService.findTodayAppCount();
        if (todayAppCount == null) {
            todayAppCount = 0;
        }
        appAccidentRecord.setTodayAppup(String.valueOf(todayAppCount));
        //查询今日推送事故个数
        Integer todayAppPushCount = appAccidentRecordService.findTodayAppPushCount();
        if (todayAppPushCount == null) {
            todayAppPushCount = 0;
        }
        appAccidentRecord.setPassAccident(String.valueOf(todayAppPushCount));
        //黑名单人数
        Integer allBlackNmber = appAccidentRecordService.findBlackNumber();
        if (allBlackNmber == null) {
            allBlackNmber = 0;
        }
        appAccidentRecord.setBlackNumber(allBlackNmber);

        Integer greenNumber = appAccidentRecordService.findGreenNumber();
        if (greenNumber == null) {
            greenNumber = 0;
        }
        appAccidentRecord.setGreenNumber(greenNumber);

        Integer audit = appAccidentRecordService.findAudit();
        if (greenNumber == null) {
            greenNumber = 0;
        }

        Integer day = appAccidentRecordService.findDayNumber();
        if (day == null) {
            day = 0;
        }

        Integer night = appAccidentRecordService.findNightNumber();
        if (night == null) {
            night = 0;
        }

        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/appAccidentRecordList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppAccidentRecord> listData(AppAccidentRecord appAccidentRecord, HttpServletRequest request, HttpServletResponse response) {
        appAccidentRecord.setPage(new Page<>(request, response));
        Page<AppAccidentRecord> page = appAccidentRecordService.findPage(appAccidentRecord);
        return page;
    }

    /**
     * 查看编辑表单upload
     */
    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = "form")
    public String form(AppAccidentRecord appAccidentRecord, Model model) {
        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/appAccidentRecordForm";
    }

    /**
     * 保存app上报事故信息表
     */
    @RequiresPermissions("app:appAccidentRecord:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppAccidentRecord appAccidentRecord, Integer state, String id, Double money) {
        appAccidentRecordService.save(appAccidentRecord);
        return renderResult(Global.TRUE, text("保存app上报事故信息表成功！"));
    }

    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = {"appAccidentTwoList", ""})
    public String appAccidentTwoList(AppAccidentRecord appAccidentRecord, Model model) {
        //查询今日普通红包个数   微信红包和app红包个数
        Integer appCount = appAccidentRecordService.findCommonappCount();   //普通红包个数
        if (appCount == null) {
            appCount = 0;
        }
        Integer wxCount = appAccidentRecordService.findWxCommonCount();     //微信红包个数
        if (wxCount == null) {
            wxCount = 0;
        }
        appAccidentRecord.setRedEnvelopes(String.valueOf(appCount + wxCount));
        //查询总共今日普通红包金额  微信和app红包金额
        BigDecimal CommonAppAmount = appAccidentRecordService.findCommonAppAmount();  //普通红包金额
        if (CommonAppAmount == null) {
            CommonAppAmount = new BigDecimal(0);
        }
        BigDecimal WxAmount = appAccidentRecordService.findWxAmount();              //微信红包金额
        if (WxAmount == null) {
            WxAmount = new BigDecimal(0);
        }
        appAccidentRecord.setEnvelopeAmount(String.valueOf(CommonAppAmount.add(WxAmount)));
        //查询今日plus会员红包金额
        BigDecimal plusAmount = appAccidentRecordService.findPlusAmount();         //plus红包金额
        if (plusAmount == null) {
            plusAmount = new BigDecimal(0);
        }
        appAccidentRecord.setPlusEnevlopeAmount(String.valueOf(plusAmount));
        //查询今日plus会员红包个数
        Integer plusCount = appAccidentRecordService.findPlusCount();               //plus会员金额
        if (plusCount == null) {
            plusCount = 0;
        }
        appAccidentRecord.setPlusEnevlopeCount(String.valueOf(plusCount));
        //查询今日事故个数
        Integer todayCount = appAccidentRecordService.findTodayCount();
        if (todayCount == null) {
            todayCount = 0;
        }
        appAccidentRecord.setTodayAccident(String.valueOf(todayCount));
        //查询今日小程序上传
        Integer todayWxCount = appAccidentRecordService.findTodayWxCount();
        if (todayWxCount == null) {
            todayWxCount = 0;
        }
        appAccidentRecord.setTodayWxup(String.valueOf(todayWxCount));
        //查询今日app上传
        Integer todayAppCount = appAccidentRecordService.findTodayAppCount();
        if (todayAppCount == null) {
            todayAppCount = 0;
        }
        appAccidentRecord.setTodayAppup(String.valueOf(todayAppCount));
        //查询今日推送事故个数
        Integer todayAppPushCount = appAccidentRecordService.findTodayAppPushCount();
        if (todayAppPushCount == null) {
            todayAppPushCount = 0;
        }
        appAccidentRecord.setPassAccident(String.valueOf(todayAppPushCount));
        //黑名单人数
        Integer allBlackNmber = appAccidentRecordService.findBlackNumber();
        if (allBlackNmber == null) {
            allBlackNmber = 0;
        }
        appAccidentRecord.setBlackNumber(allBlackNmber);

        Integer greenNumber = appAccidentRecordService.findGreenNumber();
        if (greenNumber == null) {
            greenNumber = 0;
        }
        appAccidentRecord.setGreenNumber(greenNumber);

        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/appAccidentTwoList";
    }

    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = "listDataTwo")
    @ResponseBody
    public Page<AppAccidentRecord> listDataTwo(AppAccidentRecord appAccidentRecord, HttpServletRequest request, HttpServletResponse response) {
        appAccidentRecord.setPage(new Page<>(request, response));
        Page<AppAccidentRecord> page = appAccidentRecordService.findTwoPage(appAccidentRecord);
        return page;
    }

    @RequiresPermissions("app:appAccidentRecord:view")
    @RequestMapping(value = "formtwo")
    public String formtwo(AppAccidentRecord appAccidentRecord, Model model) {
        model.addAttribute("appAccidentRecord", appAccidentRecord);
        return "modules/app/appAccidentTwoForm";
    }


    //审核失败
    @RequestMapping("failure")
    @ResponseBody
    public Object test(String id, String reason, String type) {
        return appAccidentRecordService.failure(id, reason, type);
    }


    @RequestMapping("showApp")
    @ResponseBody
    public String showApp(String id) {
        //先判断是否添加过到视频表中
        AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(id);
        if (null == appAccidentRecord.getIsaddvideo() || 0 == appAccidentRecord.getIsaddvideo()) {
            //先查询有没有这条数据
            //根据事故id查询到视频表中有没有这条数据
            String accid = appAccidentRecord.getId();
            AppVideo appVideo1 = appVideoService.findVideoByAccid(accid);
            //数据为空就是第一次添加
            if (appVideo1 == null) {
                AppVideo appVideo = appAccidentRecordService.saveApp(id);
                if (appVideo == null) {
                    return renderResult(Global.FALSE, text("添加失败  联系管理员！"));
                }
                String id1 = appVideo.getId();
                AppVideo appVideo2 = appVideoService.get(id1);
                String userId = appVideo2.getUserId();
                AppUser appUser = appUserService.get(userId);
                String name = appUser.getName();
                String avatar = appUser.getAvatar();
                appVideo2.setName(name);
                appVideo2.setAvatar(avatar);
                Date creatTime = appVideo2.getCreatTime();
                long time = creatTime.getTime();
                JSONObject in = new JSONObject();
                in.put("videoId", appVideo2.getId());
                in.put("url", appVideo2.getUrl());
                in.put("userId", appVideo2.getUserId());
                in.put("count", appVideo2.getCount());
                in.put("share", appVideo2.getShare());
                in.put("appViewCounts", appVideo2.getAppViewCounts());
                in.put("accidentId", appVideo2.getAccidentId());
                in.put("creatTime", appVideo2.getCreatTime());
                in.put("name", appVideo2.getName());
                in.put("introduce", appVideo2.getIntroduce());
                in.put("avatar", appVideo2.getAvatar());
                in.put("address", appVideo2.getAddress());
                in.put("isPay", appVideo2.getIsPay());
                //添加到缓存中
                redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
            } else {
                //不为空就是第三次修改，修改是否展示到app中
                appAccidentRecord.setIsaddvideo(1);
                appAccidentRecordService.update(appAccidentRecord);
                appVideo1.setAppShowFalg(1);
                String userId = appVideo1.getUserId();
                AppUser appUser = appUserService.get(userId);
                String name = appUser.getName();
                String avatar = appUser.getAvatar();
                appVideo1.setAvatar(avatar);
                appVideo1.setName(name);
                //又添加到缓存中
                Date creatTime = appVideo1.getCreatTime();
                long time = creatTime.getTime();
                JSONObject in = new JSONObject();
                in.put("videoId", appVideo1.getId());
                in.put("url", appVideo1.getUrl());
                in.put("userId", appVideo1.getUserId());
                in.put("count", appVideo1.getCount());
                in.put("share", appVideo1.getShare());
                in.put("appViewCounts", appVideo1.getAppViewCounts());
                in.put("accidentId", appVideo1.getAccidentId());
                in.put("creatTime", appVideo1.getCreatTime());
                in.put("name", appVideo1.getName());
                in.put("introduce", appVideo1.getIntroduce());
                in.put("avatar", appVideo1.getAvatar());
                in.put("address", appVideo1.getAddress());
                in.put("isPay", appVideo1.getIsPay());
                //添加到缓存中
                redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
            }
            //拿到事故id把视频放到app表中
        } else {
            //如果已经添加,改修状态，不在app中展示
            //根据事故id查询到视频表中信息
            String accid = appAccidentRecord.getId();
            AppVideo appVideo1 = appVideoService.findVideoByAccid(accid);
            String userId = appVideo1.getUserId();
            AppUser appUser = appUserService.get(userId);
            String name = appUser.getName();
            String avatar = appUser.getAvatar();
            appVideo1.setAvatar(avatar);
            appVideo1.setName(name);
            //在redis里面移除
            JSONObject in = new JSONObject();
            in.put("videoId", appVideo1.getId());
            in.put("url", appVideo1.getUrl());
            in.put("userId", appVideo1.getUserId());
            in.put("count", appVideo1.getCount());
            in.put("share", appVideo1.getShare());
            in.put("appViewCounts", appVideo1.getAppViewCounts());
            in.put("accidentId", appVideo1.getAccidentId());
            in.put("creatTime", appVideo1.getCreatTime());
            in.put("name", appVideo1.getName());
            in.put("introduce", appVideo1.getIntroduce());
            in.put("avatar", appVideo1.getAvatar());
            in.put("address", appVideo1.getAddress());
            in.put("isPay", appVideo1.getIsPay());
            redisTemplate.opsForZSet().remove(AppConstants.APP_VIDEO_LIST, in.toJSONString());

            appVideo1.setAppShowFalg(0);
            //修改视频表中字段
            appVideoService.update(appVideo1);
            appAccidentRecord.setIsaddvideo(0);
            appAccidentRecordService.update(appAccidentRecord);

            return renderResult(Global.TRUE, text(" 取消在app中展示！"));
        }
        return renderResult(Global.TRUE, text("成功添加到app视频中！"));
    }

    /**
     * 获取缓存品牌记录
     *
     * @return
     */
    @RequestMapping("getBrand4Redis2")
    @ResponseBody
    public Object getBrand4Redis2() {
        String temp = "{\"picUrl\":\"%s\",\"name\":\"%s\",\"id\":\"%s\"}";
//        appCarBrandDao
        List<String> upMerchants = appBUserService.findUpMerchants();
        ArrayList<Object> carBrandCache = new ArrayList<>();
//        final String custom1 = "{\"picUrl\":\"https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/merchatsnPhoto.jpg\",\"name\":\"修理厂\",\"id\":\"1\"}";
//        final String custom2 = "{\"picUrl\":\"https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/merchatsnPhoto.jpg\",\"name\":\"专修店\",\"id\":\"2\"}";
//        final String custom3 = "{\"picUrl\":\"https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/u%3D4205134036%2C1641827673%26fm%3D26%26gp%3D0.jpg\",\"name\":\"理赔专员\",\"id\":\"7\"}";
//        final String custom4 = "{\"picUrl\":\"https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/merchatsnPhoto.jpg\",\"name\":\"运营商\",\"id\":\"4\"}";
//        carBrandCache.add(custom1);
//        carBrandCache.add(custom2);
//        carBrandCache.add(custom3);
//        carBrandCache.add(custom4);
        if(upMerchants.size() > 0 && upMerchants != null){
            ArrayList<Integer> ids = (ArrayList<Integer>) upMerchants.stream().map(t1 -> {
                if(t1 != null && StringUtils.isNotEmpty(t1)){
                    return Integer.valueOf(t1);
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            List<String> brands = appUpMerchantsService.findBrands(ids);
            ArrayList<Integer> brandIds = (ArrayList<Integer>)brands.stream().map(Integer::valueOf).collect(Collectors.toList());
            ArrayList<AppCarBrand> carBrandsById = (ArrayList<AppCarBrand>) appCarBrandDao.findCarBrandsById(brandIds);
            carBrandsById.forEach(car -> carBrandCache.add(String.format(temp,car.getPicUrl(),car.getName(),car.getId())));
        }
        //reverseRange  分数从高到低
//        Object carBrandCache2 = redisTemplate.opsForZSet().reverseRange(RedisKeyUtils.CAR_BRAND_CACHE, 0, -1);
        JSONObject result = new JSONObject();
        result.put("carBrandCache", carBrandCache);
        return result;
    }

    /**
     * 获取缓存品牌记录
     *
     * @return
     */
    @RequestMapping("getBrand4Redis")
    @ResponseBody
    public Object getBrand4Redis() {
        //reverseRange  分数从高到低
        Object carBrandCache = redisTemplate.opsForZSet().reverseRange(RedisKeyUtils.CAR_BRAND_CACHE, 0, -1);
        JSONObject result = new JSONObject();
        result.put("carBrandCache", carBrandCache);
        return result;
    }

    /**
     * 搜索品牌
     *
     * @return
     */
    @RequestMapping("searchBrand")
    @ResponseBody
    public Object searchBrand(String name) {
        List<AppCarBrand> list = appCarBrandDao.findList4Like(name);
        return list;
    }

    /**
     * 增加redis zset该品牌的分数
     *
     * @return
     */
    @RequestMapping("addScore")
    @ResponseBody
    public Object addScore(String id) {
        AppCarBrand appCarBrand = appCarBrandDao.get(new AppCarBrand(id));

        if (id.equals("1")) {
            appCarBrand = new AppCarBrand();
            appCarBrand.setId("1");
            appCarBrand.setName("修理厂");
            appCarBrand.setPicUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2357939144,794782260&fm=26&gp=0.jpg");
        } else if (id.equals("2")) {
            appCarBrand = new AppCarBrand();
            appCarBrand.setId("2");
            appCarBrand.setName("专修店");
            appCarBrand.setPicUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2635558589,3901820695&fm=26&gp=0.jpg");
        } else if (id.equals("7")) {
            appCarBrand = new AppCarBrand();
            appCarBrand.setId("7");
            appCarBrand.setName("理赔专员");
            appCarBrand.setPicUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/u%3D4205134036%2C1641827673%26fm%3D26%26gp%3D0.jpg");
        } else if (id.equals("4")) {
            appCarBrand = new AppCarBrand();
            appCarBrand.setId("4");
            appCarBrand.setName("运营商");
            appCarBrand.setPicUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/merchatsnPhoto.jpg");
        }

        JSONObject object = new JSONObject();
        object.put("id", appCarBrand.getId());
        object.put("name", appCarBrand.getName());
        object.put("picUrl", appCarBrand.getPicUrl());
        Double score = redisTemplate.opsForZSet().score(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString());
        if (score == null) {
            redisTemplate.opsForZSet().add(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        } else {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        }
        return "success";
    }

    @RequestMapping("addGarage")
    @ResponseBody
    public Object addGarage() {
        AppCarBrand appCarBrand = new AppCarBrand();
        appCarBrand = new AppCarBrand();
        appCarBrand.setId("1");
        appCarBrand.setName("修理厂");
        appCarBrand.setPicUrl("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2357939144,794782260&fm=26&gp=0.jpg");
        JSONObject object = new JSONObject();
        object.put("id", appCarBrand.getId());
        object.put("name", appCarBrand.getName());
        object.put("picUrl", appCarBrand.getPicUrl());
        Double score = redisTemplate.opsForZSet().score(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString());
        if (score == null) {
            redisTemplate.opsForZSet().add(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        } else {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        }
        return "success";
    }


    @RequestMapping("addMer")
    @ResponseBody
    public Object addMer() {
        AppCarBrand appCarBrand = new AppCarBrand();
        appCarBrand = new AppCarBrand();
        appCarBrand.setId("4");
        appCarBrand.setName("运营商");
        appCarBrand.setPicUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/merchatsnPhoto.jpg");
        JSONObject object = new JSONObject();
        object.put("id", appCarBrand.getId());
        object.put("name", appCarBrand.getName());
        object.put("picUrl", appCarBrand.getPicUrl());
        Double score = redisTemplate.opsForZSet().score(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString());
        if (score == null) {
            redisTemplate.opsForZSet().add(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        } else {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        }
        return "success";
    }


    @RequestMapping("addClia")
    @ResponseBody
    public Object addClia() {
        AppCarBrand appCarBrand = new AppCarBrand();
        appCarBrand = new AppCarBrand();
        appCarBrand.setId("7");
        appCarBrand.setName("理赔专员");
        appCarBrand.setPicUrl("https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/img/u%3D4205134036%2C1641827673%26fm%3D26%26gp%3D0.jpg");
        JSONObject object = new JSONObject();
        object.put("id", appCarBrand.getId());
        object.put("name", appCarBrand.getName());
        object.put("picUrl", appCarBrand.getPicUrl());
        Double score = redisTemplate.opsForZSet().score(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString());
        if (score == null) {
            redisTemplate.opsForZSet().add(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        } else {
            redisTemplate.opsForZSet().incrementScore(RedisKeyUtils.CAR_BRAND_CACHE, object.toJSONString(), 1);
        }
        return "success";
    }


    @RequestMapping("find4sStore2")
    @ResponseBody
    public Object find4sStore2(String accId, String id) {
        //找到上传事故具体信息
        AppAccidentRecord appAccidentRecord = appAccidentRecordDao.get(new AppAccidentRecord(accId));
        if (appAccidentRecord == null) {
            BizAccident bizAccident = bizAccidentService.get(accId);
            if (bizAccident == null) {
                return null;
            }
            appAccidentRecord = new AppAccidentRecord();
            appAccidentRecord.setLat(bizAccident.getLat());
            appAccidentRecord.setLng(bizAccident.getLng());
        }

        List<TableMess> tableMesses = new ArrayList<>();
        Circle circle = new Circle(appAccidentRecord.getLng(), appAccidentRecord.getLat(), 50.0 * 1000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                            .includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_UP_GEO + id, circle, args);

//        // 查询所有关联的上架商铺
//        List<String> upMerchants = appBUserService.findUpMerchants();
//        // 获取当前品牌的上架商铺信息
//        List<AppUpMerchants> collect = appUpMerchantsService.findUpMerchantsByBrands(upMerchants)
//                .stream().filter(mer -> mer.getBrand().equals(id)).collect(Collectors.toList());
//
//        collect.forEach(mer ->
//                redisTemplate.opsForGeo()
//                        .add(RedisKeyUtils.MERCHANTS_UP_GEO + id
//                                ,new Point(Double.valueOf(mer.getLng().toString()),Double.valueOf(mer.getLat().toString()))
//                                , mer.toString()));

//        radius.getContent()


        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        int i = 1;
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
            Distance distance = geoLocationGeoResult.getDistance();
            RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
            String name = conten.getName();
            JSONObject jsonObject = JSONObject.parseObject(name);
            String merchantsName = jsonObject.getString("name");
            String id1 = jsonObject.getString("id");
            double value = distance.getValue();
            BigDecimal bigDecimal = new BigDecimal(value);
            BigDecimal divide = bigDecimal.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
            TableMess tableMess = new TableMess();
            tableMess.setId(id1);
            tableMess.setName(merchantsName);
            tableMess.setLeave(String.valueOf(divide) + "km");
            tableMess.setBalance(new BigDecimal(0));
            tableMess.setType(jsonObject.getInteger("brand"));
//            AppBUser appBUser = appBUserService.get(id1);
            AppBUser appBUser = appBUserService.finUserByUpId(id1);
            if (appBUser != null) {
                BigDecimal balance = appBUser.getBalance();
                tableMess.setBalance(balance);
            }
            if (id.equals("7")) {
                //理赔老师，查询到当前状态是否有进行中的订单
                //通过id查询是否有1小时内有状态为1的订单 就是进行中
                List<String> ids = appAccidentRecordService.findgoingIndent(id1);
                //判断是否有数据，有数据就是进行中
                if (ids.isEmpty()) {
                    tableMess.setNowState("未进行");
                } else {
                    tableMess.setNowState("进行中");
                }
                //查询  成交台词   成交金额   根据成交金额进行级别排序
                //查询本月数据
                //根据id查询成交台次
                Integer thisCount = appIndentService.findThisMonCount(id1);
                tableMess.setMonCount(thisCount);
                //金额
                BigDecimal thisAmount = appIndentService.findThisMonAmount(id1);
                tableMess.setMonAmount(thisAmount);

//                10w一个级别。1-10 D  10- 20  C    20-30 B   30以上 A

                if (thisAmount.compareTo(new BigDecimal("100000")) == -1) {
                    tableMess.setLevel("D");
                } else if (thisAmount.compareTo(new BigDecimal("200000")) == -1) {
                    tableMess.setLevel("C");
                } else if (thisAmount.compareTo(new BigDecimal("300000")) == -1) {
                    tableMess.setLevel("B");
                } else {
                    tableMess.setLevel("A");
                }

            } else {
                tableMess.setNowState("暂无");
                tableMess.setMonCount(0);
                tableMess.setMonAmount(new BigDecimal("0"));
                tableMess.setLevel("无");
            }
            tableMesses.add(tableMess);
            if (i == 20) {
                break;
            }
            i++;
        }
        JSONObject result = new JSONObject();
        result.put("mes", tableMesses);
        return result;
    }


    /**
     * 查找最近5个4s店
     * 查找最近5个4s店
     *
     * @param accId 事故id
     * @param id    4s店品牌id
     * @return
     */
    @RequestMapping("find4sStore")
    @ResponseBody
    public Object find4sStore(String accId, String id) {
        //找到上传事故具体信息
        AppAccidentRecord appAccidentRecord = appAccidentRecordDao.get(new AppAccidentRecord(accId));
        if (appAccidentRecord == null) {
            BizAccident bizAccident = bizAccidentService.get(accId);
            if (bizAccident == null) {
                return null;
            }
            appAccidentRecord = new AppAccidentRecord();
            appAccidentRecord.setLat(bizAccident.getLat());
            appAccidentRecord.setLng(bizAccident.getLng());

        }

        //专修店和修理厂的id固定
        //查询最近的5个4s店
        //根据品牌查询到4s店
        //获取到事故地点
        List<TableMess> tableMesses = new ArrayList<>();
        Circle circle = new Circle(appAccidentRecord.getLng(), appAccidentRecord.getLat(), 50.0 * 1000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_GEO + id, circle, args);
        //修理厂
        if (id.equals("1")) {
            radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_GEO + 2, circle, args);
            //专修店
        } else if (id.equals("2")) {
            radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_GEO + 3, circle, args);
        } else if (id.equals("7")) {
            //理赔专员
            radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.CLAIM_TEACHER_ADD, circle, args);
        } else if (id.equals("4")) {
            //店铺
            radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.CLAIM_TEACHER_MER, circle, args);
        }

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        int i = 1;
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
            TableMess tableMess = new TableMess();
            tableMess.setId(id1);
            tableMess.setName(merchantsName);
            tableMess.setLeave(String.valueOf(divide) + "km");
            tableMess.setBalance(new BigDecimal(0));
            tableMess.setType(jsonObject.getInteger("type"));
            AppBUser appBUser = appBUserService.get(id1);
            if (appBUser != null) {
                BigDecimal balance = appBUser.getBalance();
                tableMess.setBalance(balance);
            }
            if (id.equals("7")) {
                //理赔老师，查询到当前状态是否有进行中的订单
                //通过id查询是否有1小时内有状态为1的订单 就是进行中
                List<String> ids = appAccidentRecordService.findgoingIndent(id1);
                //判断是否有数据，有数据就是进行中
                if (ids.isEmpty()) {
                    tableMess.setNowState("未进行");
                } else {
                    tableMess.setNowState("进行中");
                }
                //查询  成交台词   成交金额   根据成交金额进行级别排序
                //查询本月数据
                //根据id查询成交台次
                Integer thisCount = appIndentService.findThisMonCount(id1);
                tableMess.setMonCount(thisCount);
                //金额
                BigDecimal thisAmount = appIndentService.findThisMonAmount(id1);
                tableMess.setMonAmount(thisAmount);

//                10w一个级别。1-10 D  10- 20  C    20-30 B   30以上 A

                if (thisAmount.compareTo(new BigDecimal("100000")) == -1) {
                    tableMess.setLevel("D");
                } else if (thisAmount.compareTo(new BigDecimal("200000")) == -1) {
                    tableMess.setLevel("C");
                } else if (thisAmount.compareTo(new BigDecimal("300000")) == -1) {
                    tableMess.setLevel("B");
                } else {
                    tableMess.setLevel("A");
                }

            } else {
                tableMess.setNowState("暂无");
                tableMess.setMonCount(0);
                tableMess.setMonAmount(new BigDecimal("0"));
                tableMess.setLevel("无");
            }
            tableMesses.add(tableMess);
            if (i == 20) {
                break;
            }
            i++;
        }
        JSONObject result = new JSONObject();
        result.put("mes", tableMesses);
        return result;

      /*  AppMerchantsBrand appMerchantsBrandParamer = new AppMerchantsBrand();
        appMerchantsBrandParamer.setUserBId(Long.valueOf(id));
        List<AppMerchantsBrand> list = appMerchantsBrandDao.findList(appMerchantsBrandParamer);*/

        /*logger.info("### radius ={} ", radius);
        Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator = radius.iterator();
        while (iterator.hasNext()) {
            GeoResult<RedisGeoCommands.GeoLocation<String>> next = iterator.next();
            RedisGeoCommands.GeoLocation<String> content = next.getContent();
            String name = content.getName();
//            Point point = content.getPoint();
            JSONObject object = JSONObject.parseObject(name);
            Integer type = object.getInteger("type");
            double distance = LocationUtils.getDistance(object.getDouble("lat"), object.getDouble("lng"), appAccidentRecord.getLat(), appAccidentRecord.getLng());


        }
//        appAccidentRecord.getLat()
        JSONObject result = new JSONObject();
//        result.put("carBrandCache", carBrandCache);
        return result;*/
    }


    @RequestMapping("pushBill")
    @ResponseBody
    public Object pushBill(String id, String accId, String amount, Integer type, String brand) {
        JSONObject result = new JSONObject();

        //推送的是理赔老师

        //传递的参数是商户id,事故id


        try {
//            if (brand.equals("7") || brand.equals("8") || brand.equals("1")) {
            AppPushBill appPushBill = new AppPushBill();
            AppUser appUser = appUserService.get(id);
            if (!brand.equals("4")) {
                String phone = appUser.getUsername();
                // appBUserService.langlock(Integer.valueOf(appBUser.getId()), amount, accId, id, type,brand);
                appPushBill.setAccid(Long.valueOf(accId));
                appPushBill.setDeduction(Double.valueOf(amount));
                appPushBill.setUserId(Long.valueOf(id));
                appPushBill.setCreateTime(new Date());
                appPushBill.setUpdateTime(new Date());
                appPushBill.setIsClaim(1);

                appPushBillService.insert(appPushBill);
                //推送信息费用
                //callPhoneService.callPhone(appUser.getName() + "的", phone);
                ihuyiService.ihuyiCall(phone);
                //发短信
                huaweiSmsService.sendSmsByTemplate("7", phone, appUser.getName());
            } else {
                //4 运营商
                //扣费
                String phone = appUser.getUsername();
                if (appUser.getBalance().compareTo(new BigDecimal("0")) == -1) {
                    result.put("stat", "false");
                    result.put("mes", "推送失败！商户余额不足");
                    return result;
                }
                //先扣钱
                appBUserService.langlock(Integer.valueOf(appUser.getId()), amount, accId, id, type, brand);
                //推送信息费用
                //callPhoneService.callPhone(appBUser.getMerchantsName() + "的", phone);
                ihuyiService.ihuyiCall(phone);
                //发短信
                huaweiSmsService.sendSmsByTemplate("7", phone, appUser.getName());
            }

            //事故来源(1,app,2.小程序)'
            if (type == 3) {
                appPushBill.setSource(2);
                //小程序
                BizAccident bizAccident = bizAccidentService.get(accId);
                bizAccident.setUpdateTime(new Date());
                bizAccident.setIsOrder(2);
                bizAccidentService.update(bizAccident);
            } else {
                appPushBill.setSource(1);
                AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
                appAccidentRecord.setIsOrder(2);
                appAccidentRecord.setUpdateTime(new Date());
                appAccidentRecordService.update(appAccidentRecord);
            }
            appPushBillService.update(appPushBill);

//                //根据商户id查询到商户
//                AppUser appBUser = appUserService.get(id);
//                //不能重复推送 判断是否推送过了
//                List<AppPushBill> list = appPushBillService.findisPush(id, accId);
//                if (!list.isEmpty()) {
//                    result.put("stat", "false");
//                    result.put("mes", "推送失败！");
//                    return result;
//                }
//
//
//                String phone = appBUser.getPhoneNumber();
//                if (StringUtils.isEmpty(appBUser.getName()) || appBUser.getPhoneNumber().length() != 11) {
//                    phone = appBUser.getPhoneNumber();
//                }
//                //先扣钱
//                appBUserService.langlock(Integer.valueOf(appBUser.getId()), amount, accId, id, type, brand);
//                //推送信息费用
//                //callPhoneService.callPhone(appBUser.getMerchantsName() + "的", phone);
//                ihuyiService.ihuyiCall(phone);
//                //发短信
//                huaweiSmsService.sendSmsByTemplate("7", phone, appBUser.getName());
//            }


            //添加到数据
//            AppUserAccountRecord appAccount = new AppUserAccountRecord();
//            appAccount.setMomey(new BigDecimal(amount));
//            appAccount.setUserId(Long.valueOf(id));
//            //操作类型 1：提现  2：提现失败回滚 3:发送红包 4:c端用户订单结算 5：b端充值 6:b端用户订单扣钱 7:plus会员订单提成一级  8:plus会员订单提成
//            appAccount.setType(9);
//            appAccount.setCreateTime(new Date());
//            appAccount.setAddFlag(2);
//            appAccount.setSource(2);
//            appAccount.setBusinessId(accId);
//            appUserAccountRecordDao.insertCus(appAccount);
            //添加到极光推送
            //B端推送 信息推送
//            AppJgPush appJgPush = new AppJgPush();
//            appJgPush.setSource("B");
//            appJgPush.setIspush("0");
//            appJgPush.setType("1");
//            appJgPush.setUserBId(id);
//            appJgPush.setCreateTime(new Date());
//            appJgPush.setUpdateTime(new Date());
            // appJgPushService.insertJpush(appJgPush);


//            String userId = appJgPush.getUserId();
//            String userBId = appJgPush.getUserBId();
//            if (StringUtils.isEmpty(appJgPush.getUserId())) {
//                userId = "0";
//            }
//            if (StringUtils.isEmpty(appJgPush.getUserBId())) {
//                userBId = "0";
//            }
//            logger.info("### 是否执行到 # iswork={}", "1111111");
            // Producer.productSend(appJgPush.getSource(),appJgPush.getType(),userId,userBId);

            result.put("stat", "success");
            result.put("mes", "推送成功！");
            return result;
        } catch (Exception e) {
            logger.error("### 推送失败e={}", e);
            result.put("stat", "false");
            result.put("mes", "推送失败！");
            return result;
        }
    }

    @RequestMapping("pushBill2")
    @ResponseBody
    public Object pushBill2(String id, String accId, String amount, Integer type, String brand) {
        JSONObject result = new JSONObject();

        //推送的是理赔老师

        //传递的参数是商户id,事故accId
        if(accId != null && id != null){
            List<AppPushBill> appPushBills = appPushBillService.findisPush(appBUserService.finUserByUpId(id).getId(), accId);
            if(appPushBills.size() > 0){
                result.put("stat", "false");
                result.put("mes", "推送失败！不能重复推送!");
                return result;
            }
        }

        try {
//            if (brand.equals("7") || brand.equals("8") || brand.equals("1")) {
            AppPushBill appPushBill = new AppPushBill();
//            AppUser appUser = appUserService.get(id);
            AppBUser appUser = appBUserService.finUserByUpId(id);
            if (!brand.equals("4")) {
                String phone = appUser.getUsername();
                // appBUserService.langlock(Integer.valueOf(appBUser.getId()), amount, accId, id, type,brand);
                appPushBill.setAccid(Long.valueOf(accId));
                appPushBill.setDeduction(Double.valueOf(amount));
                appPushBill.setUserId(Long.valueOf(appUser.getId()));
                appPushBill.setCreateTime(new Date());
                appPushBill.setUpdateTime(new Date());
                appPushBill.setIsClaim(1);

                appPushBillService.insert(appPushBill);
                //推送信息费用
                //callPhoneService.callPhone(appUser.getName() + "的", phone);
                ihuyiService.ihuyiCall(phone);
                //发短信
                huaweiSmsService.sendSmsByTemplate("7", phone, appUser.getName());
            } else {
                //4 运营商
                //扣费
                String phone = appUser.getUsername();
                if (appUser.getBalance().compareTo(new BigDecimal("0")) == -1) {
                    result.put("stat", "false");
                    result.put("mes", "推送失败！商户余额不足");
                    return result;
                }
                //先扣钱
                appBUserService.langlock(Integer.valueOf(appUser.getId()), amount, accId, id, type, brand);
                //推送信息费用
                //callPhoneService.callPhone(appBUser.getMerchantsName() + "的", phone);
                ihuyiService.ihuyiCall(phone);
                //发短信
                huaweiSmsService.sendSmsByTemplate("7", phone, appUser.getName());
            }

            //事故来源(1,app,2.小程序)'
            if (type == 3) {
                appPushBill.setSource(2);
                //小程序
                BizAccident bizAccident = bizAccidentService.get(accId);
                bizAccident.setUpdateTime(new Date());
                bizAccident.setIsOrder(2);
                bizAccidentService.update(bizAccident);
            } else {
                appPushBill.setSource(1);
                AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
                appAccidentRecord.setIsOrder(2);
                appAccidentRecord.setUpdateTime(new Date());
                appAccidentRecordService.update(appAccidentRecord);
            }
            appPushBillService.update(appPushBill);

            result.put("stat", "success");
            result.put("mes", "推送成功！");
            return result;
        } catch (Exception e) {
            logger.error("### 推送失败e={}", e);
            result.put("stat", "false");
            result.put("mes", "推送失败！");
            return result;
        }
    }


    @RequestMapping("black")
    @ResponseBody
    public String black(String id, String type) {
        //根据type来判断来源
        //查询到对应用户修改是否添加到黑名单
        try {
            if (type.equals("3")) {
                //微信
                BizAccident bizAccident = bizAccidentService.get(id);
                String openid = bizAccident.getOpenid();
                BizWxUser byOpenid = bizWxUserService.findByOpenid(openid);
                Integer blacklist = byOpenid.getBlacklist();
                if (blacklist == 0) {
                    byOpenid.setBlacklist(1);
                } else {
                    byOpenid.setBlacklist(0);
                }
                bizWxUserService.update(byOpenid);
            } else {
                //app
                AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(id);
                String userId = appAccidentRecord.getUserId();
                AppUser appUser = appUserService.get(userId);
                Integer balck = appUser.getBalck();
                if (balck == 0) {
                    appUser.setBalck(1);
                } else {
                    appUser.setBalck(0);
                }
                appUserService.update(appUser);
            }
            return renderResult(Global.TRUE, text("操作成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return renderResult(Global.FALSE, text("操作失败,请联系管理员！"));
        }

    }


    @RequestMapping("green")
    @ResponseBody
    public String green(String id, String type) {
        //根据type来判断来源
        //查询到对应用户修改是否添加到黑名单
        try {
            if (type.equals("3")) {
                //微信
                BizAccident bizAccident = bizAccidentService.get(id);
                String openid = bizAccident.getOpenid();
                BizWxUser byOpenid = bizWxUserService.findByOpenid(openid);
                if (byOpenid.getBlacklist() == 2) {
                    byOpenid.setBlacklist(0);
                } else {
                    byOpenid.setBlacklist(2);
                }
                bizWxUserService.update(byOpenid);
            } else {
                //app
                AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(id);
                String userId = appAccidentRecord.getUserId();
                AppUser appUser = appUserService.get(userId);
                if (appUser.getBalck() == 2) {
                    appUser.setBalck(0);
                } else {
                    appUser.setBalck(2);
                }
                appUserService.update(appUser);
            }
            return renderResult(Global.TRUE, text("操作成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            return renderResult(Global.FALSE, text("操作失败,请联系管理员！"));
        }

    }


    @RequestMapping("accidentTag")
    @ResponseBody
    public JSONObject accidentTag(String id, String type, String genre) {
        JSONObject reslut = new JSONObject();
        //根据事故id和事故上传来源来找到用户，
        return appAccidentRecordService.accidentTag(id, type, genre);
    }


    @RequestMapping("findImg")
    @ResponseBody
    public JSONObject findImg(String accId) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
        if (appAccidentRecord != null) {
            List<String> strings = new ArrayList<>();
            String imgUrl = appAccidentRecord.getImgUrl();
            if (StringUtils.isEmpty(imgUrl)) {
                result.put("code", "404");
            } else {
                strings.add(imgUrl);
                result.put("code", "200");
                result.put("imgList", strings);
            }
        }
        return result;
    }

    @RequestMapping("findNowPlaceImg")
    @ResponseBody
    public JSONObject findNowPlaceImg(String accId, Integer type) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        //查询倒pb
        if (type == 3) {   //微信事故表
            type = 2;
        } else if (type == 2) {
            type = 1;       //app事故表
        }
        List<AppPushBill> bySourceAccid = appPushBillService.findBySourceAccid(accId, type);
        if (bySourceAccid.isEmpty()) {
            result.put("code", "404");
        } else {
            AppPushBill appPushBill = bySourceAccid.get(0);
            List<String> lists = appPushBillService.findPbImg(appPushBill.getId(), 2);
            result.put("imgList", lists);
            result.put("code", "200");
        }
        return result;
    }


    @RequestMapping("findCarImg")
    @ResponseBody
    public JSONObject findCarImg(String accId, Integer type) {
        JSONObject result = new JSONObject();
        //遍历添加数据到新表种
        //查询倒pb
        if (type == 3) {   //微信事故表
            type = 2;
        } else if (type == 2) {
            type = 1;       //app事故表
        }
        List<AppPushBill> bySourceAccid = appPushBillService.findBySourceAccid(accId, type);
        if (bySourceAccid.isEmpty()) {
            result.put("code", "404");
        } else {
            AppPushBill appPushBill = bySourceAccid.get(0);
            List<String> carimg = appSendUrlService.findcarImg(appPushBill.getId(), 1, 1);
            result.put("imgList", carimg);
            result.put("code", "200");
        }
        return result;
    }


    @GetMapping("chongzhi")
    @ResponseBody
    public void chongzhi(String  amount) {
        String s = stringRedisTemplate.opsForValue().get(RedisKeyUtils.MESSAGE_AMOUNT);
        if (StringUtils.isEmpty(s)) {
            s = "0";
        }
        BigDecimal add = new BigDecimal(s).add(new BigDecimal(amount));
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.MESSAGE_AMOUNT, String.valueOf(add));
        AppUserAccountRecord appUser = new AppUserAccountRecord();
		/*private Integer momey;		// 金额
			private Long userId;		// 用户id
			private Integer type;		// 操作类型 1：提现  2：提现失败回滚 3:发送红包
			private Date createTime;		// 发生时间
			private Integer addFlag;		// 1加钱  2 减钱
			private Integer source;		// 1:c端  2：b端
			private Long bussinessId;		// 业务id*/
        appUser.setMomey(new BigDecimal(amount));
        appUser.setUserId(1L);
        appUser.setType(1);
        appUser.setCreateTime(new Date());
        appUser.setAddFlag(1);
        appUser.setSource(1);
        appUser.setBusinessId("1");
        appUserAccountRecordDao.insertCus(appUser);
    }

    @RequestMapping("findMessageAmount")
    @ResponseBody
    public JSONObject findMessageAmount() {
        JSONObject result = new JSONObject();
        String s = redisTemplate.opsForValue().get(RedisKeyUtils.MESSAGE_AMOUNT);
        if (StringUtils.isEmpty(s)) {
            s = "0";
        }
        result.put("amount", s);
        return result;
    }

    @RequestMapping("findMessage")
    @ResponseBody
    public JSONObject findMessage(String accId, String type) {
        JSONObject result = new JSONObject();
        //根据type来判断事故类型
        //查询pushBill
        if (type.equals("3")) {
            type = "2";
        } else {
            type = "1";
        }


        List<AppPushBill> appPushBillList = appPushBillService.findpushBill(accId, type);
        if (!appPushBillList.isEmpty()) {
            AppPushBill appPushBill = appPushBillList.get(0);
            Long userId = appPushBill.getUserId();
            AppUser appUser = appUserService.get(userId.toString());
            result.put("desc",appPushBill.getRemark());
            result.put("managerDesc",appPushBill.getManagerRemark());

            result.put("cliName", appUser.getName());
            result.put("accAmount", appPushBill.getDeduction());
            result.put("checkPlace", appPushBill.getCheckAddress());
            result.put("checkTime", appPushBill.getCheckTime());
            result.put("createTime", appPushBill.getCreateTime());
            //签到时间减去创建时间
            if (appPushBill.getCreateTime() != null && appPushBill.getCheckTime() != null) {
                // double v = calculatetimeGapHour(appPushBill.getCheckTime(), appPushBill.getCreateDate());
                result.put("presentTime", "暂无参数");
            } else {
                result.put("presentTime", "暂无");
            }
            AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
            String address = appAccidentRecord.getAddress();
            result.put("accidentPlace", address);
            result.put("lossVehicle", appAccidentRecord.getLossVehicle());
            result.put("lossRate", appAccidentRecord.getLossLevel());
            if (appPushBill.getCheckTime() == null) {
                result.put("serviceStatus", "未接单");
            } else {
                result.put("serviceStatus", "已接单");
            }
            result.put("signFeedback", "暂无字段");

            AppMessageCar appMessageCar = appMessageCarService.findPushBill(appPushBill.getId());   //查询到pushBill
            if (appMessageCar != null) {
                result.put("customerName", appMessageCar.getCustomerName());
                result.put("phone", appMessageCar.getPhone());
                result.put("company", appMessageCar.getLocalInsurance());
                result.put("accidentLiability", appMessageCar.getAccidentResponsibility());
                result.put("carUnit", appMessageCar.getBuyCar());
                result.put("casualties", appMessageCar.getCasualties());
                String brandId = appMessageCar.getBrandId();
                if (StringUtils.isNotEmpty(brandId)) {
                    AppCarBrand appCarBrand = appCarBrandService.get(brandId);
                    result.put("brand", appCarBrand.getName());
                } else {
                    result.put("brand", "暂无");
                }
                result.put("clinchDeal", appMessageCar.getVehicleResults());
                String userBId = appMessageCar.getUserBId();

                if (StringUtils.isNotEmpty(userBId)) {
                    if (userBId.endsWith("Z")) {
                        //z结尾就是上架的商户
                        String member = userBId.substring(0, userBId.length() - 1);
                        AppUpMerchants appUpMerchants = appUpMerchantsService.get(member);
                        result.put("maintenance", appUpMerchants.getName());
                    } else {
                        AppBUser appBUser = appBUserService.get(userBId);
                        result.put("maintenance", appBUser.getMerchantsName());
                    }
                }
            }
        }
        return result;
    }

    public static double calculatetimeGapHour(Date date1, Date date2) {
        double hour = 0;
        double millisecond = date2.getTime() - date1.getTime();
        hour = millisecond / (60 * 60 * 1000);
        return hour;
    }

    @RequestMapping("addText")
    @ResponseBody
    public JSONObject addText(String in, String accId, Integer type) {
        JSONObject result = new JSONObject();
        JSONObject object = JSONObject.parseObject(in);
        String desc = object.getString("desc");
        String managerDesc = object.getString("managerDesc");
        //信息审核备注
        //查询到pushbill
        if (type == 3) {
            //查询到派单id
            type = 2;
        } else {
            type = 1;
        }
        if (StringUtils.isNotEmpty(desc)) {
            List<AppPushBill> bySourceAccid = appPushBillService.findBySourceAccid(accId, type);
            if (!bySourceAccid.isEmpty()) {
                for (AppPushBill appPushBill : bySourceAccid) {
                    appPushBill.setRemark(desc);
                    appPushBill.setManagerRemark(managerDesc);
                    appPushBillService.update(appPushBill);
                }
            }
        }
        result.put("stat", "success");
        result.put("mes", "审核成功！");
        return result;
    }


    @RequestMapping("addTag")
    @ResponseBody
    public JSONObject addTag(String in, String accId, Integer type) {
        JSONObject result = new JSONObject();
        JSONObject object = JSONObject.parseObject(in);
        //{"quiz1":"没有损失","quiz2":"1","quiz3":"","quiz4":"","quiz5":""}
        String quiz1 = object.getString("quiz1");   //失败原因
        String quiz2 = object.getString("quiz2");   //视频标记
        String quiz3 = object.getString("quiz3");   //损失车辆
        String quiz4 = object.getString("quiz4");   //损失等级
        String quiz5 = object.getString("quiz5");   //风险系数

        if (StringUtils.isNotEmpty(quiz2)) {
            JSONObject object1 = appAccidentRecordService.accidentTag(accId, type.toString(), quiz2);
            if (object1.getString("stat").equals("false")) {
                result.put("stat", "false");
                result.put("mes", "审核失败！");
                return result;
            }
        }

        if (StringUtils.isNotEmpty(quiz1)) {
            JSONObject failure = appAccidentRecordService.failure(accId, quiz1, type.toString());
            if (failure.getString("stat").equals("false")) {
                result.put("stat", "false");
                result.put("mes", "审核失败！");
                return result;
            }
        }
        //xcx
        if (type == 3) {
            BizAccident accident = bizAccidentService.get(accId);
            accident.setLossVehicle(quiz3);
            accident.setLossLevel(quiz4);
            accident.setRiskFactor(quiz5);
            bizAccidentService.update(accident);
        } else {
            AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
            appAccidentRecord.setLossVehicle(quiz3);
            appAccidentRecord.setLossLevel(quiz4);
            appAccidentRecord.setRiskFactor(quiz5);
            appAccidentRecordService.update(appAccidentRecord);
        }
        //
        result.put("stat", "success");
        result.put("mes", "审核成功！");
        return result;
    }

    @RequestMapping(value = "/shiyishi")
    @ResponseBody
    public JSONObject shiyishi() {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    //五个参数
    @RequestMapping(value = "/echo")
    @ResponseBody
    public JSONObject echo(String accId, Integer type, Integer number) {
        JSONObject result = new JSONObject();
        //number   1.失败原因，2.视频标记，3，损失车辆，4.损失等级，5.维修产值
        if (type == 3) {
            //xcx
            BizAccident accident = bizAccidentService.get(accId);
            if (number == 1) {
                //失败原因
                String reason = accident.getReason();
                result.put("data", reason);
                return result;
            } else if (number == 3) {
                //损失车辆
                String lossVehicle = accident.getLossVehicle();
                result.put("data", lossVehicle);
                return result;
            } else if (number == 4) {
                //损失等级
                String lossLevel = accident.getLossLevel();
                result.put("data", lossLevel);
                return result;
            } else if (number == 5) {
                String riskFactor = accident.getRiskFactor();
                result.put("data", riskFactor);
                return result;
            }
        } else {
            AppAccidentRecord appAccidentRecord = appAccidentRecordService.get(accId);
            if (number == 1) {
                //失败原因
                String reason = appAccidentRecord.getReason();
                result.put("data", reason);
                return result;
            } else if (number == 3) {
                //损失车辆
                String lossVehicle = appAccidentRecord.getLossVehicle();
                result.put("data", lossVehicle);
                return result;
            } else if (number == 4) {
                //损失等级
                String lossLevel = appAccidentRecord.getLossLevel();
                result.put("data", lossLevel);
                return result;
            } else if (number == 5) {
                String riskFactor = appAccidentRecord.getRiskFactor();
                result.put("data", riskFactor);
                return result;
            }
        }
        return null;
    }


}