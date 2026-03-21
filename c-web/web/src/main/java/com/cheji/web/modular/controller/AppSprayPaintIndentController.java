package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.modular.cwork.SparyMerchantsDto;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 喷漆订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
@RestController
@RequestMapping("/appSprayPaintIndent")
public class AppSprayPaintIndentController extends BaseController {

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppSprayPaintImgService appSprayPaintImgService;

    @Resource
    private BUserService bUserService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

//    @Resource
//    private DefaultMQProducer mqProducer;

    @Resource
    private MerchantsInfoBannerService merchantsInfoBannerService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private AppSprayPaintDetailsService appSprayPaintDetailsService;

    private Logger logger = LoggerFactory.getLogger(AppSprayPaintIndentController.class);


//    @ApiOperation(value = "保存喷漆订单" +
//            "409未结算" +
//            "407参数有误")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "brandType", value = "车辆品牌和型号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "licensePlate", value = "车牌号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "technicianId", value = "技师id", required = true, dataType = "Integer"),
//            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话号码", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名称", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = true, dataType = "Bigdecimal"),
//            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = true, dataType = "Bigdecimal"),
//            @ApiImplicitParam(paramType = "query", name = "pickAddress", value = "接车地址", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "insurance", value = "有无保险(1.有，2.没有)", required = true, dataType = "Integer"),
//            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "url", value = "图片地址", required = true, dataType = "String[]")
//    })
//    @RequestMapping(value = "/saveSprayPaintIndent", method = RequestMethod.POST)
//    public JSONObject saveSprayPaintIndent(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        Integer userid = currentLoginUser.getAppUserEntity().getId();
//
//        //查询到有没有未支付的订单
//        List<Integer> paintIndent = appSprayPaintIndentService.findOtherIndent(userid);
//        if (!paintIndent.isEmpty()) {
//            result.put("code", 409);
//            result.put("msg", "用户有未结算的喷漆订单");
//            return result;
//        }
//        //拿到数据
//        String brandType = jsonObject.getString("brandType");
//        String licensePlate = jsonObject.getString("licensePlate");
//        String phone = jsonObject.getString("phone");
//        Integer insurance = jsonObject.getInteger("insurance");
//        String remark = jsonObject.getString("remark");
//        BigDecimal lng = jsonObject.getBigDecimal("lng");
//        BigDecimal lat = jsonObject.getBigDecimal("lat");
//        String pickAddres = jsonObject.getString("pickAddress");
//        Integer technicianId = jsonObject.getInteger("technicianId");
//        String username = jsonObject.getString("username");
//        if (technicianId == null) {
//            result.put("code", 407);
//            result.put("msg", "技师为空");
//            return result;
//        }
//
//        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectById(technicianId);
//        if (appUserBMessageEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "技师id有误");
//            return result;
//        }
//        //下单
//        //订单编号， 时间，订单状态
//        String orderNo = GenerateDigitalUtil.getOrderNo();
//        AppSprayPaintIndentEntity appSprayPaintIndent = new AppSprayPaintIndentEntity();
//        appSprayPaintIndent.setSprayPaintNumber("PQ" + orderNo);
//        appSprayPaintIndent.setBrandType(brandType);
//        appSprayPaintIndent.setLicensePlate(licensePlate);
//        appSprayPaintIndent.setPhone(phone);
//        appSprayPaintIndent.setUsername(username);
//        appSprayPaintIndent.setLng(lng);
//        appSprayPaintIndent.setLat(lat);
//        appSprayPaintIndent.setPickAddress(pickAddres);
//        appSprayPaintIndent.setInsurance(insurance);
//        appSprayPaintIndent.setRemark(remark);
//        appSprayPaintIndent.setState(1);
//        appSprayPaintIndent.setUserId(userid);
//        appSprayPaintIndent.setUserBId(appUserBMessageEntity.getUserBId());
//        appSprayPaintIndent.setTechnicianId(technicianId);
//        appSprayPaintIndent.setCreateTime(new Date());
//        appSprayPaintIndent.setUpdateTime(new Date());
//        appSprayPaintIndentService.insert(appSprayPaintIndent);
//
//
//        //保存图片
//        JSONObject json = JSONObject.parseObject(String.valueOf(jsonObject));
//        JSONArray jsonArray = (JSONArray) json.get("url");
//        List<String> bills = JSONArray.parseArray(jsonArray.toString(), String.class);
//        for (int i = 0; i < bills.size(); i++) {
//            AppSprayPaintImgEntity appSprayPaintImg = new AppSprayPaintImgEntity();
//            String imgurl = bills.get(i);
//            appSprayPaintImg.setCreateTime(new Date());
//            appSprayPaintImg.setSprayPaintId(appSprayPaintIndent.getId());
//            appSprayPaintImg.setIndex(i + 1);
//            appSprayPaintImg.setUrl(imgurl);
//            appSprayPaintImg.setUpdateTime(new Date());
//            appSprayPaintImgService.insert(appSprayPaintImg);
//        }
//        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndent.getSprayPaintNumber().getBytes());
//
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.put("code", 200);
//        result.put("msg", "成功");
//        result.put("data", appSprayPaintIndent);
//        return result;
//    }


    @ApiOperation(value = "喷漆首页数据")
    @RequestMapping(value = "/sprayPaint", method = RequestMethod.GET)
    public JSONObject sprayPaint() {
        JSONObject result = new JSONObject();
        //视频地址，商户默认经纬度
        BUserEntity bUserEntity = bUserService.selectById(1);
        if (bUserEntity == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "");
            jsonObject.put("merchants", null);
            jsonObject.put("video", "");

            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", jsonObject);
            return result;
        } else {
            SparyMerchantsDto sparyMerchantsDto = new SparyMerchantsDto();
            sparyMerchantsDto.setAddress(bUserEntity.getAddress());
            sparyMerchantsDto.setLat(bUserEntity.getLat());
            sparyMerchantsDto.setLng(bUserEntity.getLng());
            sparyMerchantsDto.setMerchantsName(bUserEntity.getMerchantsName());
            sparyMerchantsDto.setUserBId(bUserEntity.getId().intValue());
            EntityWrapper<MerchantsInfoBannerEntity> infoBannerEntityWrapper = new EntityWrapper<>();
            infoBannerEntityWrapper.eq("user_b_id", bUserEntity.getId())
                    .eq("`index`", 1);
            MerchantsInfoBannerEntity merchantsInfoBannerEntity = merchantsInfoBannerService.selectOne(infoBannerEntityWrapper);
            if (merchantsInfoBannerEntity == null) {
                sparyMerchantsDto.setImgUrl("");
            } else {
                sparyMerchantsDto.setImgUrl(merchantsInfoBannerEntity.getUrl());
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "");
            jsonObject.put("merchants", sparyMerchantsDto);
            jsonObject.put("video", "");

            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", jsonObject);
            return result;
        }
    }

    @ApiOperation(value = "查询喷漆商户数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityCode", value = "城市号码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "评分查询", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/sprayPaintMerchants", method = RequestMethod.GET)
    public JSONObject sprayPaintMerchants(Integer cityCode, String score, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        //查询出开通了喷漆的商户，查询到商户id，再查询商户数据
        //查询开通了喷漆的商户
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("lable_id", 12)
                .eq("state", 1);
        List<LableDetailsReviewTreeEntity> lableDetailsReview = lableDetailsReviewTreeService.selectList(wrapper);
        if (lableDetailsReview.isEmpty()) {
            result.put("code", 405);
            result.put("msg", "暂无数据");
            return result;
        }
        //查询到商户id，再查询商户数据
        List<SparyMerchantsDto> userbList = new ArrayList<>();
        for (LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity : lableDetailsReview) {
            Integer userBId = lableDetailsReviewTreeEntity.getUserBId();
            //查询商户数据
            SparyMerchantsDto sparyMerchants = bUserService.findSprayMerchants(userBId, cityCode, score, pagesize);
            if (sparyMerchants == null) {
                continue;
            }
            userbList.add(sparyMerchants);
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", userbList);
        return result;
    }


    @ApiOperation(value = "查询维修工艺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/maintenanceProcess", method = RequestMethod.GET)
    public JSONObject maintenanceProcess(String indentNumber) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }

        JSONObject in = new JSONObject();
        //接车状态
        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
        paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type", 2);
        List<AppSprayPaintImgEntity> appSprayPaintImgPickCar = appSprayPaintImgService.selectList(paintImgWrapper);
        in.put("pickCarImg", appSprayPaintImgPickCar);
        //送达状态
        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper2 = new EntityWrapper<>();
        paintImgWrapper2.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type", 4);
        List<AppSprayPaintImgEntity> appSprayPaintImgSendCar = appSprayPaintImgService.selectList(paintImgWrapper2);
        in.put("sendCarImg", appSprayPaintImgSendCar);
        //服务中照片
        List<AppSprayPaintImgEntity> appSprayPaintImgInServiceCar = appSprayPaintImgService.selectInService(appSprayPaintIndentEntity.getId());
        in.put("inServiceImg", appSprayPaintImgInServiceCar);

        EntityWrapper<AppSprayPaintDetailsEntity> sprayPaintDetailsWrapper = new EntityWrapper<>();
        sprayPaintDetailsWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId());
        List<AppSprayPaintDetailsEntity> appSprayPaintDetailsEntities = appSprayPaintDetailsService.selectList(sprayPaintDetailsWrapper);
        in.put("sprayPaintDetails", appSprayPaintDetailsEntities);

        in.put("price", appSprayPaintIndentEntity.getPrice());

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    @ApiOperation(value = "查询喷漆订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sprayPaintDetails", method = RequestMethod.GET)
    public JSONObject sprayPaintDetails(String indentNumber) {
        JSONObject result = new JSONObject();
        //订单详情
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }

        Integer userBId = appSprayPaintIndentEntity.getUserBId();
        BUserEntity bUserEntity = bUserService.selectById(userBId);
        if (bUserEntity == null) {
            result.put("code", 402);
            result.put("msg", "商户id有误");
            return result;
        }
        JSONObject in = new JSONObject();
        Integer technicianId = appSprayPaintIndentEntity.getTechnicianId();
        //技师头像，订单价格
        AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectById(technicianId);
        if (appUserBMessage == null) {
            result.put("code", 402);
            result.put("msg", "技师id有误");
            return result;
        }
        in.put("headImg", appUserBMessage.getHeadImg());
        in.put("name", appUserBMessage.getName());
        in.put("price", appSprayPaintIndentEntity.getPrice());
        //订单编号，订单类型，联系电话，接车时间，接车地点，
        in.put("sprayPaintNumber", appSprayPaintIndentEntity.getSprayPaintNumber());
        in.put("indentType", "汽车喷漆");
        in.put("phone", appSprayPaintIndentEntity.getPhone());
        in.put("pickAddress", appSprayPaintIndentEntity.getPickAddress());
        if (appSprayPaintIndentEntity.getPickCarTime() != null) {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString1 = formatter1.format(appSprayPaintIndentEntity.getPickCarTime());
            in.put("pickCarTime", dateString1);
        } else {
            in.put("pickCarTime", appSprayPaintIndentEntity.getPickCarTime());
        }

        //送达时间，送达地点，
        in.put("sendAddress", appSprayPaintIndentEntity.getPickAddress());
        if (appSprayPaintIndentEntity.getSendCarTime() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(appSprayPaintIndentEntity.getSendCarTime());
            in.put("sendCarTime", dateString);
        } else {
            in.put("sendCarTime", appSprayPaintIndentEntity.getSendCarTime());
        }

        //订单状态，
        Integer state = appSprayPaintIndentEntity.getState();
        String ste = "未知";
        switch (state) {
            case 1:
                ste = "新订单";
                break;
            case 2:
                ste = "商户已报价";
                break;
            case 3:
                ste = "已确认报价";
                break;
            case 4:
                ste = "商户准备接车";
                break;
            case 5:
                ste = "确认接车";
                break;
            case 6:
                ste = "商户服务完成";
                break;
            case 7:
                ste = "商户已经交车";
                break;
            case 8:
                ste = "已支付";
                break;
            case 9:
                ste = "已评价";
                break;
            case 10:
                ste = "订单已关闭";
                break;
            default:
                ste = "未知";
                break;
        }
        in.put("indentState", ste);

        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
        paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type", 1);
        List<AppSprayPaintImgEntity> appSprayPaintImgPickCar = appSprayPaintImgService.selectList(paintImgWrapper);
        in.put("imgList", appSprayPaintImgPickCar);

        in.put("username", appSprayPaintIndentEntity.getUsername());
        in.put("id", appSprayPaintIndentEntity.getId());
        in.put("userBId", appSprayPaintIndentEntity.getUserBId());
        in.put("merchantsName", bUserEntity.getMerchantsName());
        in.put("brandType", appSprayPaintIndentEntity.getBrandType());
        in.put("licensePlate", appSprayPaintIndentEntity.getLicensePlate());
        in.put("remark", appSprayPaintIndentEntity.getRemark());
        in.put("insurance", appSprayPaintIndentEntity.getInsurance());
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }

//    @ApiOperation(value = "同意报价")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/agreeSyrayPaintSttlement", method = RequestMethod.GET)
//    public JSONObject agreeSyrayPaintSttlement(String indentNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 402);
//            result.put("msg", "检查订单编号");
//            return result;
//        }
//
//        appSprayPaintIndentEntity.setState(3);
//        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
//        //喷漆
//        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.put("code", 200);
//        result.put("msg", "同意喷漆报价");
//        return result;
//    }


//    @ApiOperation(value = "喷漆结算")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/sprayPaintSettlement", method = RequestMethod.GET)
//    public JSONObject sprayPaintSettlement(String indentNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 402);
//            result.put("msg", "检查订单编号");
//            return result;
//        }
//        if (appSprayPaintIndentEntity.getPayState() == 0) {
//            result.put("code", 402);
//            result.put("msg", "该订单还未支付");
//            return result;
//        } else if (appSprayPaintIndentEntity.getPayState() == 2) {
//            result.put("code", 402);
//            result.put("msg", "该订单已支付，请勿重新支付");
//            return result;
//        }
//
//        //查询订单数据
//        //商户加钱
//        //行锁
//        try {
//            appSprayPaintIndentService.SprayPaintAddMerchantsAmount(appSprayPaintIndentEntity);
//            //修改订单状态
//            appSprayPaintIndentEntity.setState(3);
//            appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //喷漆
//        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result.put("code", 200);
//        result.put("msg", "喷漆结算成功");
//        return result;
//    }


    @ApiOperation(value = "取消订单" +
            " 402:该订单已支付，检查订单编号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cancelReason", value = "取消原因", required = true, dataType = "String")
    })
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public JSONObject cancelOrder(String indentNumber, String cancelReason, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }
        if (appSprayPaintIndentEntity.getPayState() != null && appSprayPaintIndentEntity.getPayState() == 2) {
            result.put("code", 402);
            result.put("msg", "该订单已支付，取消订单请联系客服");
            return result;
        }

        if (appSprayPaintIndentEntity.getState() > 2) {
            result.put("code", 402);
            result.put("msg", "已经同意报价，取消订单请联系客服");
            return result;
        }

        appSprayPaintIndentEntity.setCancelReason(cancelReason);
        appSprayPaintIndentEntity.setState(10);
        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
        //喷漆
//        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        result.put("code", 200);
        result.put("msg", "订单删除成功");
        return result;
    }

//    @ApiOperation(value = "确认接车" +
//            "407:检查订单编号" +
//            "406:订单状态不能操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "indentNumber", value = "订单编号", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/confirmPickCar", method = RequestMethod.GET)
//    public JSONObject confirmPickCar(String indentNumber, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        //判断登陆
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//
//        if (StringUtils.isEmpty(indentNumber)) {
//            result.put("code", 407);
//            result.put("msg", "订单编号为空");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", indentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "订单编号有误");
//            return result;
//        }
//        if (appSprayPaintIndentEntity.getState() != 4) {
//            result.put("code", 406);
//            result.put("msg", "订单状态不能操作");
//            return result;
//        }
//        appSprayPaintIndentEntity.setState(5);
//
//        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
//
//        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());
//
//        //默认3秒超时
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(sendMsg);
//            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        result.put("code", 200);
//        result.put("msg", "成功");
//        return result;
//    }


    @ApiOperation(value = "等待技师" +
            "407:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sprayPaintNumber", value = "喷漆订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/waitTechnician", method = RequestMethod.GET)
    public JSONObject waitTechnician(HttpServletRequest request, String sprayPaintNumber) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (StringUtils.isEmpty(sprayPaintNumber)) {
            result.put("code", 407);
            result.put("msg", "订单编号为空");
            return result;
        }
        //商户经纬度，技师经纬度，头像，名称，编号，技龄，驾龄，分数，商户电话
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", sprayPaintNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 407);
            result.put("msg", "订单编号有误");
            return result;
        }
        //技师id
        JSONObject in = appSprayPaintIndentService.findmessage(appSprayPaintIndentEntity);

        if (in == null) {
            result.put("code", 407);
            result.put("msg", "未查询到技师");
        } else {
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", in);
        }

        return result;


    }


}
