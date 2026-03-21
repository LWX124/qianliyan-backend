/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.dao.AppBUserDao;
import com.jeesite.modules.app.dao.AppPhotoMerDao;
import com.jeesite.modules.app.dao.AppUpMerchantsDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息表Controller
 *
 * @author zcq
 * @version 2019-10-16
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appBUser")
public class AppBUserController extends BaseController {


    private final static Logger logger = LoggerFactory.getLogger(AppBUserController.class);

    @Autowired
    private AppBUserService appBUserService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;

    @Resource
    private AppMerchantsInfoBannerService appMerchantsInfoBannerService;

    @Resource
    private AppBUserDao appBUserDao;

    @Resource
    private AppUpMerchantsDao appUpMerchantsDao;

    @Resource
    private AppPhotoMerDao appPhotoMerDao;

    @Resource
    private AppBUserContractService appBUserContractService;

    @Resource
    private AppInsuranceMerchantsService appInsuranceMerchantsService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppBUser get(Long id, boolean isNewRecord) {
        return appBUserService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appBUser:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppBUser appBUser, Model model) {
        //查询商户上架，下架和总数

        //查询上架数量
        Integer shelvesCount = appBUserService.findShelvesCount();
        //查询下架数量
        Integer outCount = appBUserService.findOutCount();
        //查询总数
        Integer allCount = appBUserService.findAllCount();

        appBUser.setShelvesCount(shelvesCount);
        appBUser.setOutCount(outCount);
        appBUser.setAllCount(allCount);

        model.addAttribute("appBUser", appBUser);
        return "modules/app/appBUserList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appBUser:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppBUser> listData(AppBUser appBUser, HttpServletRequest request, HttpServletResponse response) {
        appBUser.setPage(new Page<>(request, response));
        Page<AppBUser> page = appBUserService.findPage(appBUser);

        //获取app_b_user的上架4s店信息state=1,通过app_merchants_info_banner表查询图片,
//        List<AppBUser> allByState = appBUserDao.findAllByState();
//        if(allByState != null && allByState.size() > 0){
//            allByState.stream().map(s ->
//            {
//                List<AppMerchantsInfoBanner> imgList = appMerchantsInfoBannerService.findAllList(s.getId());
//                if(imgList == null){
//                    return null;
//                }
//                s.setImgList(imgList);
//                return s;
//            }).collect(Collectors.toList());
//        }
//
//        //通过app_up_merchants存入上架商铺信息,并且将图片关联app_photo_mer表
//
//        AppUpMerchants a = new AppUpMerchants();
//        allByState.stream().forEach(s -> {
//            AppUpMerchants appUpMerchants = new AppUpMerchants();
//            appUpMerchants.setAddress(s.getAddress());
//            appUpMerchants.setLng(new BigDecimal(s.getLng()).setScale(10,BigDecimal.ROUND_HALF_UP));
//            appUpMerchants.setLat(new BigDecimal(s.getLat()).setScale(10,BigDecimal.ROUND_HALF_UP));
//            appUpMerchants.setName(s.getMerchantsName());
//            appUpMerchants.setBrand(s.getBrandId());
//            appUpMerchants.setScore(s.getServiceSorce());
//            appUpMerchants.setCreateTime(s.getCreatTime());
//            appUpMerchants.setUpdateTime(s.getUpdateTime());
//            appUpMerchants.setHuanxinUsername(s.getHuanxinUserName());
//            appUpMerchants.setHuanxinPassword(s.getHuanxinPassword());
//            appUpMerchants.setAdcode(Integer.valueOf(s.getCounty()));
//            appUpMerchants.setCitycode(Integer.valueOf(s.getCity()));
//            logger.info("{}",appUpMerchants);
//            appUpMerchantsDao.insertUpMer(appUpMerchants);
//            //(#{url},#{index},#{upId},#{createTime},#{updateTime})
//                    if(s.getImgList() != null && s.getImgList().size() >0){
//                        Integer temp = 0;
//                        s.getImgList().forEach(img -> {
////                               Integer.valueOf(appUpMerchants.getId())
//                                AppPhotoMer appPhotoMer = new AppPhotoMer(img.getUrl(), img.getIndex().intValue(), Integer.valueOf(appUpMerchants.getId()), s.getCreatTime(), s.getUpdateTime());
//                                appPhotoMerDao.insertNew(appPhotoMer);
//                                logger.info("appPhotoMer{}",appPhotoMer);
//                            }
//                        );
//                    }
//            s.setState(2);
//            appBUserDao.update(s);
//        }
//
//        );
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appBUser:view")
    @RequestMapping(value = "form")
    public String form(AppBUser appBUser, Model model) {
        //添加商户保险
        //查询到对应的id，号拼接起来
        List<String> insuranceList = appBUserService.findMerchantsInsert(appBUser.getId());
        String insuran = null;
        for (String s : insuranceList) {
            insuran += "," + s;
        }
        appBUser.setInsurance(insuran);
        System.out.println("insuran :" + insuran);
        model.addAttribute("appBUser", appBUser);
        return "modules/app/appBUserForm";
    }

    /**
     * 保存用户信息表
     */
    @RequiresPermissions("app:appBUser:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppBUser appBUser) {
        FileUploadUtils.saveFileUpload(appBUser.getId(), "appBUser_image");
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
        String format = yyyyMM.format(new Date());
        List<FileUpload> fileUpload = FileUploadUtils.findFileUpload(appBUser.getId(), "appBUser_image");
        String s = new String();
        //删除所有的图片
        //查询到所有图片
        if (!fileUpload.isEmpty()) {
            List<AppMerchantsInfoBanner> list = appMerchantsInfoBannerService.findAllList(appBUser.getId());
            for (AppMerchantsInfoBanner appMerchantsInfoBanner : list) {
                appMerchantsInfoBannerService.delete(appMerchantsInfoBanner);
            }
            for (int i = 0; i < fileUpload.size(); i++) {
                FileUpload file = fileUpload.get(i);
                String uploadId = file.getId();
                //根据id来查询到file id，，根据fileId，直接查询到url
                logger.error("### uploadId：  uploadId={}", uploadId);

                String url = appBUserService.findNewUrl(uploadId);
                //每次都删除重新添加新的banner图片
                AppMerchantsInfoBanner infoBanner = new AppMerchantsInfoBanner();
                infoBanner.setUrl(url);
                infoBanner.setIndex((long) (i + 1));
                infoBanner.setUserBId(Integer.valueOf(appBUser.getId()));
                infoBanner.setCreatTime(new Date());
                infoBanner.setUpdateTime(new Date());
                appMerchantsInfoBannerService.insertNew(infoBanner);
            }
        }


        FileUploadUtils.saveFileUpload(appBUser.getId(), "appBUser_contract");
        List<FileUpload> fileUploadcontract = FileUploadUtils.findFileUpload(appBUser.getId(), "appBUser_contract");

        if (!fileUploadcontract.isEmpty()) {
            List<AppBUserContract> list1 = appBUserContractService.selectAll(appBUser.getId());
            for (AppBUserContract appBUserContract : list1) {
                appBUserContractService.delete(appBUserContract);
            }
            for (int i = 0; i < fileUploadcontract.size(); i++) {
                //添加到合同表中
                FileUpload fileContract = fileUploadcontract.get(i);
                String id = fileContract.getId();
                String newUrl = appBUserService.findNewUrl(id);

                //添加
                AppBUserContract appBUserContract = new AppBUserContract();
                appBUserContract.setUrl(newUrl);
                appBUserContract.setState(1L);
                appBUserContract.setIndex(i + 1);
                appBUserContract.setUserBId(Integer.valueOf(appBUser.getId()));
                appBUserContract.setCreateTime(new Date());
                appBUserContract.setUpdateTime(new Date());
                appBUserContractService.insertNewCon(appBUserContract);
            }
        }

        String insurance = appBUser.getInsurance();
        if (StringUtils.isNotEmpty(insurance)) {
            String[] as = insurance.split(",");
            for (int i = 0; i < as.length; i++) {
                //添加对应的保险
                //先查询是否有这个保险
                Integer id = appInsuranceMerchantsService.findAgoInsurance(as[i], appBUser.getId());
                //根据id和商户id查询，，没有就添加
                if (id == null) {
                    //id 为空，添加
                    AppInsuranceMerchants insuranceMerchants = new AppInsuranceMerchants();
                    insuranceMerchants.setInsuranceId(Integer.valueOf(as[i]));
                    insuranceMerchants.setUserBId(Integer.valueOf(appBUser.getId()));
                    insuranceMerchants.setState(1);
                    insuranceMerchants.setCreateTime(new Date());
                    insuranceMerchants.setUpdateTime(new Date());
                    appInsuranceMerchantsService.insertInsurance(insuranceMerchants);
                }
            }
        }

        //添加到对应表中
        appBUserService.save(appBUser);
        return renderResult(Global.TRUE, text("保存用户信息表成功！"));
    }


    /**
     * 添加商户到geo中
     */
    @RequestMapping(value = "addgeo")
    @ResponseBody
    public String addgeo(String id) {
        //判断4s店
        //根据id查询到商户
        AppBUser appBUser = appBUserService.get(id);
        if (appBUser.getAddgeo().equals("1")) {
            String redis = appBUser.getRedis();
            JSONObject jsonObject = JSONObject.parseObject(redis);
            int type = jsonObject.getIntValue("type");
            JSONArray brandList = jsonObject.getJSONArray("brandList");
            String brandId = "";
            if (!brandList.isEmpty()) {
                for (int i = 0; i < brandList.size(); i++) {
            brandId = (String) brandList.get(0);
        }
    }
            if (type == 1) {
                //4s店 拿到品牌数据
                redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_GEO + brandId, redis);
            } else if (type == 2) {
                //修理厂
                redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_GEO + 2, redis);
            } else {
                //专修店
                redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_GEO + 3, redis);
            }

            appBUser.setAddgeo("0");
            appBUserService.update(appBUser);
            return renderResult(Global.TRUE, text("下架成功！"));
        } else {
            //判断服务项目是否添加
            //根据商户id查询到对应是否添加了服务项目，和商户名称，地址信息，品牌
//			String merchantsName = appBUser.getMerchantsName();
            String address = appBUser.getAddress();
//			//查询是否有服务
//			List<AppLableDetailsReviewTree> appLableDetails = appLableDetailsReviewTreeService.findLable(appBUser.getId());
//
//			AppMerchantsInfoBanner appMerchantsInfoBanner = appMerchantsInfoBannerService.findIndex1Banner(appBUser.getId());
            //查询品牌
            List<String> brandList = appBUserService.findBrand(appBUser.getId());
//			if (StringUtils.isEmpty(merchantsName)){
//				return renderResult(Global.FALSE, text("缺少商户名称！"));
//			}
            Integer type = appBUser.getType();
            if (type == null) {
                return renderResult(Global.FALSE, text("缺少门店类型！"));
            }
            if (StringUtils.isEmpty(address)) {
                return renderResult(Global.FALSE, text("缺少地址信息！"));
            }
//			if (appLableDetails.isEmpty()){
//				return renderResult(Global.FALSE, text("缺少服务项目！"));
//			}
//			if (appMerchantsInfoBanner==null){
//				return renderResult(Global.FALSE, text("缺少门店主图！"));
//			}
            if (appBUser.getType() == 1 || appBUser.getType() == 3) {
                if (brandList.isEmpty()) {
                    return renderResult(Global.FALSE, text("缺少品牌！"));
                }
            }
            appBUser.setAddgeo("1");
            try {
                //查询到品牌
                JSONObject in = new JSONObject();
                in.put("id", appBUser.getId());
                in.put("merchantsName", appBUser.getMerchantsName());
                in.put("lat", appBUser.getLat());
                in.put("lng", appBUser.getLng());
                in.put("address", appBUser.getAddress());
                in.put("type", appBUser.getType());
                in.put("brandList", brandList);

                if (appBUser.getType() == 1) {
                    String s = brandList.get(0);

                    //4s店 拿到品牌数据
                    redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_GEO + s, new Point(appBUser.getLng(), appBUser.getLat()), in.toJSONString());
                } else if (appBUser.getType() == 2) {

                    //修理厂
                    redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_GEO + 2, new Point(appBUser.getLng(), appBUser.getLat()), in.toJSONString());
                } else {

                    //专修店
                    redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_GEO + 3, new Point(appBUser.getLng(), appBUser.getLat()), in.toJSONString());
                }
                appBUser.setRedis(in.toJSONString());
                //修改状态
                appBUserService.update(appBUser);
                return renderResult(Global.TRUE, text("添加成功！"));
            } catch (Exception e) {
                e.printStackTrace();
                return renderResult(Global.FALSE, text("操作失败！"));
            }
        }
    }


    @RequestMapping(value = "updateState")
    @ResponseBody
    public String updateState(String id) {
        AppBUser appBUser = appBUserService.get(id);
        String merchantsName = appBUser.getMerchantsName();
        Integer state = appBUser.getState();
        String address = appBUser.getAddress();
        //查询是否有服务
        List<AppLableDetailsReviewTree> appLableDetails = appLableDetailsReviewTreeService.findLable(appBUser.getId());

        if (appLableDetails.isEmpty()) {
            //return "modules/app/addBusinessList";
            return renderResult(Global.FALSE, text("缺少服务！"));
        }
        AppMerchantsInfoBanner appMerchantsInfoBanner = appMerchantsInfoBannerService.findIndex1Banner(appBUser.getId());
        //查询品牌
        List<String> brandList = appBUserService.findBrand(appBUser.getId());
        if (StringUtils.isEmpty(merchantsName)) {
            return renderResult(Global.FALSE, text("缺少商户名称！"));
        }
        if (StringUtils.isEmpty(address)) {
            return renderResult(Global.FALSE, text("缺少地址信息！"));
        }
        if (appMerchantsInfoBanner == null && state != 1) {
            return renderResult(Global.FALSE, text("缺少门店主图！"));
        }
        if (appBUser.getType() == 1 || appBUser.getType() == 3) {
            if (brandList.isEmpty()) {
                return renderResult(Global.FALSE, text("缺少品牌！"));
            }
        }
        if (state == 1) {
            //上架状态，修改为下架
            appBUser.setState(2);
        } else {
            //下架状态，修改上架
            appBUser.setState(1);
        }
        appBUserService.update(appBUser);
        return renderResult(Global.TRUE, text("操作成功！"));
    }


    @RequestMapping(value = "passAccount")
    @ResponseBody
    public String passAccount(String id) {
        //根据商户di修改状态
        try {
            AppBUser appBUser = appBUserService.get(id);
            appBUser.setStatus("1");
            appBUserService.update(appBUser);
            return renderResult(Global.TRUE, text("审核账号成功！"));
        } catch (Exception e) {
            return renderResult(Global.FALSE, text("审核账号失败，请联系管理员！"));
        }

    }


    @RequestMapping(value = "banLogin")
    @ResponseBody
    public String banLogin(String id) {
        //根据商户di修改状态
        try {
            AppBUser appBUser = appBUserService.get(id);
            appBUser.setStatus("2");
            appBUserService.update(appBUser);
            dissOldUser(Integer.parseInt(appBUser.getId()));
            return renderResult(Global.TRUE, text("禁止登陆成功！"));
        } catch (Exception e) {
            return renderResult(Global.FALSE, text("禁止登陆失败,请联系管理员"));
        }

    }

    /**
     * 把之前的用户挤下线
     */
    private void dissOldUser(int userId) {
        String s = (String) redisTemplate.opsForValue().get(RedisKeyUtils.USER_B_ID_TOKEN + userId);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(s)) {
            redisTemplate.delete(RedisKeyUtils.USER_B_TOKEN + s);
        }
    }


    @RequiresPermissions("app:appBUser:view")
    @RequestMapping(value = "test")
    public String test(AppBUser appBUser, Model model) {
        model.addAttribute("appBUser", appBUser);
        return "modules/app/test";
    }


    @RequestMapping(value = "/addRescueMerchants")
    @ResponseBody
    public JSONObject addRescueMerchants(String phone) {
        JSONObject data = new JSONObject();
        //根据商户di修改状态
        //System.out.println(phone);
        //查询到商户数据
        //把该商户数据添加到geo中


        AppBUser one = appBUserDao.findOne(phone);
        //为空就返回
        if (one == null) {
            data.put("stat", "false");
            return data;
        }
        if (StringUtils.isEmpty(one.getAddress())) {
            data.put("stat", "address");
            return data;
        }
        AppLableDetailsReviewTree appLable = appLableDetailsReviewTreeService.selectLable(Integer.valueOf(one.getId()), 13, 1);
        if (appLable != null) {
            if (appLable.getState().equals("1")) {
                //状态为1，添加过了
                data.put("stat", "false");
                return data;
            } else if (appLable.getState().equals("0")) {
                //开启
                appLable.setState("1");


                JSONObject in = new JSONObject();
                in.put("id", one.getId());
                in.put("merchantsName", one.getMerchantsName());
                in.put("lat", one.getLat());
                in.put("lng", one.getLng());
                in.put("address", one.getAddress());
                redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_RESCUE_GEO, new Point(one.getLng(), one.getLat()), in.toJSONString());
                one.setRescueRedis(in.toJSONString());
                appBUserService.update(one);
                appLableDetailsReviewTreeService.update(appLable);
                data.put("stat", "success");
                return data;
            }
        }


        //给对应商户添加服务项目
        appLableDetailsReviewTreeService.addFirst(Integer.valueOf(one.getId()));
        //判断是否添加过服务项目
        appLableDetailsReviewTreeService.addRescueSecond(Integer.valueOf(one.getId()), 13, null);

        //查询到数据放到geo中
        JSONObject in = new JSONObject();
        in.put("id", one.getId());
        in.put("merchantsName", one.getMerchantsName());
        in.put("lat", one.getLat());
        in.put("lng", one.getLng());
        in.put("address", one.getAddress());
        redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_RESCUE_GEO, new Point(one.getLng(), one.getLat()), in.toJSONString());
        one.setRescueRedis(in.toJSONString());
        appBUserService.update(one);
        data.put("stat", "success");
        return data;
    }

    //查询到geo中救援数据
    @RequestMapping(value = "/findGeoMerchants")
    @ResponseBody
    public JSONObject findGeoMerchants() {
        AppBUser one = new AppBUser();
        one.setLat(30.657487);  //天府广场
        one.setLng(104.065735);
        List<RescueMerchants> tableMesses = new ArrayList<>();
        Circle circle = new Circle(one.getLng(), one.getLat(), 50.0 * 10000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_RESCUE_GEO, circle, args);
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
            RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
            String name = conten.getName();
            JSONObject jsonObject = JSONObject.parseObject(name);
            String merchantsName = jsonObject.getString("merchantsName");
            BigDecimal lng = jsonObject.getBigDecimal("lng");
            String address = jsonObject.getString("address");
            BigDecimal lat = jsonObject.getBigDecimal("lat");
            String id1 = jsonObject.getString("id");
            RescueMerchants tableMess = new RescueMerchants();
            tableMess.setId(id1);
            tableMess.setMerchantsName(merchantsName);
            tableMess.setAddress(address);
            tableMess.setLng(lng);
            tableMess.setLat(lat);
            tableMesses.add(tableMess);
        }
        JSONObject result = new JSONObject();

        result.put("code", 0);
        result.put("msg", "");
        result.put("count", tableMesses.size());
        result.put("data", tableMesses);
        return result;
    }


    //删除geo中救援商户
    @RequestMapping(value = "/delRescueMerchants")
    @ResponseBody
    public JSONObject delRescueMerchants(String userBid) {
        JSONObject result = new JSONObject();
        AppBUser appBUser = appBUserService.get(userBid);
        String rescueRedis = appBUser.getRescueRedis();
        redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_RESCUE_GEO, rescueRedis);

        //修改商户服务项目状态
        AppLableDetailsReviewTree appLable = appLableDetailsReviewTreeService.selectLable(Integer.valueOf(userBid), 13, 1);
        appLable.setState("0");
        appLableDetailsReviewTreeService.update(appLable);
        appBUser.setRescueRedis("");
        appBUserService.update(appBUser);

        result.put("stat", "success");
        return result;
    }

}