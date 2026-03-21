package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppSprayPaintDetailsEntity;
import com.cheji.b.modular.domain.AppSprayPaintImgEntity;
import com.cheji.b.modular.domain.AppSprayPaintIndentEntity;
import com.cheji.b.modular.service.AppSprayPaintDetailsService;
import com.cheji.b.modular.service.AppSprayPaintImgService;
import com.cheji.b.modular.service.AppSprayPaintIndentService;
import com.cheji.b.pojo.TokenPojo;
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
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 喷漆订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-03-17
 */
@RestController
@RequestMapping("/appSprayPaintIndent")
public class AppSprayPaintIndentController extends BaseController {


    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppSprayPaintImgService appSprayPaintImgService;

    @Resource
    private DefaultMQProducer mqProducer;

    @Resource
    private AppSprayPaintDetailsService appSprayPaintDetailsService;



    private Logger logger = LoggerFactory.getLogger(AppSprayPaintIndentController.class);


    //开通喷漆服务
//    @ApiOperation(value = "开通喷漆服务")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/openPainting", method = RequestMethod.GET)
//    public JSONObject openPainting(HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        Integer id = currentLoginUser.getAppUserEntity().getId();
//        //添加数据第一层。然后添加第二层。
//        try {
//            lableDetailsReviewTreeService.addFirst(id);
//            //判断是否添加过喷漆
//            EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("user_b_id", id)
//                    .eq("lable_id", 12);
//            LableDetailsReviewTreeEntity lableDetailsReview = lableDetailsReviewTreeService.selectOne(wrapper);
//            if (lableDetailsReview == null) {
//                lableDetailsReviewTreeService.addSecond(id, 12, null);
//            } else {
//                lableDetailsReview.setState(0);
//                lableDetailsReviewTreeService.updateById(lableDetailsReview);
//                result.put("code", 200);
//                result.put("msg", "等待重新审核");
//                return result;
//            }
//        } catch (CusException e) {
//            result.put("code", 402);
//            result.put("msg", e);
//            return result;
//        }
//        result.put("code", 200);
//        result.put("msg", "成功");
//        return result;
//    }


    @ApiOperation(value = "喷漆详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sprayDetails", method = RequestMethod.GET)
    public JSONObject sprayDetails(String cleanIndentNumber) {
        JSONObject result = new JSONObject();
        //根据订单编号查询订单详情
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }
        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
        paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type",1);
        List<AppSprayPaintImgEntity> appSprayPaintImgEntities = appSprayPaintImgService.selectList(paintImgWrapper);

        JSONObject in = new JSONObject();
        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper1 = new EntityWrapper<>();
        paintImgWrapper1.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type",2);
        List<AppSprayPaintImgEntity> appSprayPaintImgPickCar = appSprayPaintImgService.selectList(paintImgWrapper1);
        in.put("pickCarImg",appSprayPaintImgPickCar);

        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper2 = new EntityWrapper<>();
        paintImgWrapper2.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type",4);
        List<AppSprayPaintImgEntity> appSprayPaintImgSendCar = appSprayPaintImgService.selectList(paintImgWrapper2);
        in.put("sendCarImg",appSprayPaintImgSendCar);

        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper3 = new EntityWrapper<>();
        paintImgWrapper3.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
                .eq("type",3);
        List<AppSprayPaintImgEntity> inServiceImg = appSprayPaintImgService.selectList(paintImgWrapper3);
        in.put("inServiceImg",inServiceImg);


        in.put("brandType", appSprayPaintIndentEntity.getBrandType());
        in.put("licensePlate", appSprayPaintIndentEntity.getLicensePlate());
        in.put("phone", appSprayPaintIndentEntity.getPhone());
        in.put("remark", appSprayPaintIndentEntity.getRemark());
        in.put("insurance", appSprayPaintIndentEntity.getInsurance());
        //查询是否有使用优惠卷
        Integer integer = appSprayPaintIndentService.selectByCoupon(cleanIndentNumber);
        if (integer==null){
            in.put("price",appSprayPaintIndentEntity.getPrice().multiply(new BigDecimal("0.8")).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        in.put("price",appSprayPaintIndentEntity.getPrice());

        in.put("pickAddress", appSprayPaintIndentEntity.getPickAddress());
        in.put("lng", appSprayPaintIndentEntity.getLng());
        in.put("lat", appSprayPaintIndentEntity.getLat());
        EntityWrapper<AppSprayPaintDetailsEntity> sprayPaintDetailsWrapper = new EntityWrapper<>();
        sprayPaintDetailsWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId());
        List<AppSprayPaintDetailsEntity> appSprayPaintDetailsEntities = appSprayPaintDetailsService.selectList(sprayPaintDetailsWrapper);
        in.put("sprayPaintDetails", appSprayPaintDetailsEntities);
        in.put("imgList", appSprayPaintImgEntities);
        in.put("payState", appSprayPaintIndentEntity.getPayState());
        in.put("state", appSprayPaintIndentEntity.getState());
        in.put("isOffer", appSprayPaintIndentEntity.getIsOffer());
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;
    }


    @ApiOperation(value = "商户提供报价" +
            "402:参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "price", value = "报价", required = true, dataType = "Bigdecmial"),
            @ApiImplicitParam(paramType = "query", name = "details", value = "价格详情，place，price", required = true, dataType = "List[]")
    })
    @RequestMapping(value = "/offerMerchants", method = RequestMethod.POST)
    public JSONObject offerMerchants(HttpServletRequest request, @RequestBody JSONObject in) {
        //商户提供用户报价
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String cleanIndentNumber = in.getString("cleanIndentNumber");
        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }
        if (appSprayPaintIndentEntity.getState() != 1) {
            result.put("code", 402);
            result.put("msg", "该订单已经提交过报价");
            return result;
        }
        //修改报价。
        BigDecimal price = in.getBigDecimal("price");
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            result.put("code", 402);
            result.put("msg", "价格为空");
            return result;
        }
        appSprayPaintIndentEntity.setPrice(price);
        appSprayPaintIndentEntity.setIsOffer(1);
        appSprayPaintIndentEntity.setState(2);

        //保存明细
        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray details = (JSONArray) json.get("details");
        if (details == null) {
            result.put("code", 402);
            result.put("msg", "明细为空");
            return result;
        }
        List<AppSprayPaintDetailsEntity> apptDetails = JSONArray.parseArray(details.toString(), AppSprayPaintDetailsEntity.class);
        BigDecimal bigDecimal = new BigDecimal(0);

        for (AppSprayPaintDetailsEntity apptDetail : apptDetails) {
            BigDecimal price1 = apptDetail.getPrice();
            bigDecimal = bigDecimal.add(price1);
        }

        if (bigDecimal.compareTo(price) != 0) {
            result.put("code", 402);
            result.put("msg", "检查价格参数");
            return result;
        }

        for (AppSprayPaintDetailsEntity apptDetail : apptDetails) {
            apptDetail.setSprayPaintId(appSprayPaintIndentEntity.getId());
            apptDetail.setCreateTime(new Date());
            apptDetail.setUpdateTime(new Date());
            appSprayPaintDetailsService.insert(apptDetail);
        }

        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);


        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

//
//    @ApiOperation(value = "拍照接车" +
//            "407：参数有误" +
//            "406:订单状态不能操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "url", value = "图片list", required = true, dataType = "String[]")
//    })
//    @RequestMapping(value = "/merchantsPickCar", method = RequestMethod.POST)
//    public JSONObject merchantsPickCar(HttpServletRequest request, @RequestBody JSONObject in) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//
//        String cleanIndentNumber = in.getString("cleanIndentNumber");
//        if (StringUtils.isEmpty(cleanIndentNumber)) {
//            result.put("code", 407);
//            result.put("msg", "cleanIndentNumber为空");
//            return result;
//        }
//        JSONObject json = JSONObject.parseObject(String.valueOf(in));
//        JSONArray url = (JSONArray) json.get("url");
//        if (url == null) {
//            result.put("code", 407);
//            result.put("msg", "url为空");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "cleanIndentNumber有误");
//            return result;
//        }
//
//        if (appSprayPaintIndentEntity.getState() != 3) {
//            result.put("code", 406);
//            result.put("msg", "订单状态不能操作");
//            return result;
//        }
//
//        //保存喷漆接车图片
//        List<String> urls = JSONArray.parseArray(url.toString(), String.class);
//        for (int i = 0; i < urls.size(); i++) {
//            String s = urls.get(i);
//            AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
//            appSprayPaintImgEntity.setUrl(s);
//            appSprayPaintImgEntity.setType(2);
//            appSprayPaintImgEntity.setIndex(i + 1);
//            appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
//            appSprayPaintImgEntity.setCreateTime(new Date());
//            appSprayPaintImgEntity.setUpdateTime(new Date());
//            appSprayPaintImgService.insert(appSprayPaintImgEntity);
//        }
//        appSprayPaintIndentEntity.setState(4);
//        appSprayPaintIndentEntity.setPickCarTime(new Date());
//        appSprayPaintIndentEntity.setUpdateTime(new Date());
//        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
//
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

//
//    @ApiOperation(value = "交车完成" +
//            "407：参数有误" +
//            "406:订单状态不能操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "url", value = "图片list", required = true, dataType = "String[]")
//    })
//    @RequestMapping(value = "/completeIncar", method = RequestMethod.POST)
//    public JSONObject completeIncar(HttpServletRequest request, @RequestBody JSONObject in) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        String cleanIndentNumber = in.getString("cleanIndentNumber");
//        if (StringUtils.isEmpty(cleanIndentNumber)) {
//            result.put("code", 407);
//            result.put("msg", "订单编号为空");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "cleanIndentNumber有误");
//            return result;
//        }
//        if (appSprayPaintIndentEntity.getState() != 6) {
//            result.put("code", 406);
//            result.put("msg", "订单状态不能操作");
//            return result;
//        }
//
//
//        //添加保存图片
//        JSONObject json = JSONObject.parseObject(String.valueOf(in));
//        JsonArray url = (JsonArray) json.get("url");
//        if (url == null) {
//            result.put("code", 407);
//            result.put("msg", "url为空");
//            return result;
//        }
//        List<String> strings = JSONArray.parseArray(String.valueOf(url), String.class);
//        for (int i = 0; i < strings.size(); i++) {
//            String s = strings.get(i);
//            AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
//            appSprayPaintImgEntity.setUrl(s);
//            appSprayPaintImgEntity.setType(4);
//            appSprayPaintImgEntity.setIndex(i + 1);
//            appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
//            appSprayPaintImgEntity.setCreateTime(new Date());
//            appSprayPaintImgEntity.setUpdateTime(new Date());
//            appSprayPaintImgService.insert(appSprayPaintImgEntity);
//        }
//
//        appSprayPaintIndentEntity.setState(7);
//        appSprayPaintIndentService.updateById(appSprayPaintIndentEntity);
//
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
//        result.put("code", 200);
//        result.put("msg", "成功");
//        return result;
//
//    }
//
//    @ApiOperation(value = "服务中上传图片" +
//            "407：参数有误" +
//            "406:订单状态不能操作")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "url", value = "图片list", required = true, dataType = "String[]")
//    })
//    @RequestMapping(value = "/inServiceUpload", method = RequestMethod.POST)
//    public JSONObject inServiceUpload(HttpServletRequest request, @RequestBody JSONObject in) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        String cleanIndentNumber = in.getString("cleanIndentNumber");
//        if (StringUtils.isEmpty(cleanIndentNumber)) {
//            result.put("code", 407);
//            result.put("msg", "cleanIndentNumber为空");
//            return result;
//        }
//        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
//        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
//        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
//        if (appSprayPaintIndentEntity == null) {
//            result.put("code", 407);
//            result.put("msg", "cleanIndentNumber有误");
//            return result;
//        }
//
//        //查询图片
//        EntityWrapper<AppSprayPaintImgEntity> paintImgWrapper = new EntityWrapper<>();
//        paintImgWrapper.eq("spray_paint_id", appSprayPaintIndentEntity.getId())
//                .eq("type", 3);
//        List<AppSprayPaintImgEntity> paintImgEntities = appSprayPaintImgService.selectList(paintImgWrapper);
//
//        //添加保存图片
//        JSONObject json = JSONObject.parseObject(String.valueOf(in));
//        JsonArray url = (JsonArray) json.get("url");
//        if (url == null) {
//            result.put("code", 407);
//            result.put("msg", "url为空");
//            return result;
//        }
//        //图片不为空
//        if (!paintImgEntities.isEmpty()) {
//            for (AppSprayPaintImgEntity paintImgEntity : paintImgEntities) {
//                appSprayPaintImgService.deleteById(paintImgEntity);
//            }
//        }
//        List<String> strings = JSONArray.parseArray(String.valueOf(url), String.class);
//        for (int i = 0; i < strings.size(); i++) {
//            String s = strings.get(i);
//            AppSprayPaintImgEntity appSprayPaintImgEntity = new AppSprayPaintImgEntity();
//            appSprayPaintImgEntity.setUrl(s);
//            appSprayPaintImgEntity.setType(3);
//            appSprayPaintImgEntity.setIndex(i + 1);
//            appSprayPaintImgEntity.setSprayPaintId(appSprayPaintIndentEntity.getId());
//            appSprayPaintImgEntity.setCreateTime(new Date());
//            appSprayPaintImgEntity.setUpdateTime(new Date());
//            appSprayPaintImgService.insert(appSprayPaintImgEntity);
//        }
//
//
//        result.put("code", 200);
//        result.put("msg", "成功");
//        return result;
//    }

    @ApiOperation(value = "拍照接车，服务完成，交车完成" +
            "407：参数有误" +
            "406:订单状态不能操作")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.拍照接车，2，修改服务图片，3.服务完成，4.交车完成", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "图片list", required = true, dataType = "String[]")
    })
    @RequestMapping(value = "/completeService", method = RequestMethod.POST)
    public JSONObject completeService(HttpServletRequest request, @RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        String cleanIndentNumber = in.getString("cleanIndentNumber");
        if (StringUtils.isEmpty(cleanIndentNumber)) {
            result.put("code", 407);
            result.put("msg", "cleanIndentNumber为空");
            return result;
        }

        EntityWrapper<AppSprayPaintIndentEntity> sprayPaintIndentWrapper = new EntityWrapper<>();
        sprayPaintIndentWrapper.eq("spray_paint_number", cleanIndentNumber);
        AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(sprayPaintIndentWrapper);
        if (appSprayPaintIndentEntity == null) {
            result.put("code", 407);
            result.put("msg", "cleanIndentNumber有误");
            return result;
        }

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray url = (JSONArray) json.get("url");
        if (url == null && !in.getString("type").equals("2")) {
            result.put("code", 407);
            result.put("msg", "url为空");
            return result;
        }

        JSONObject object = appSprayPaintIndentService.updataIndentState(in, url, appSprayPaintIndentEntity);
        if (object.getInteger("code")!=200){
            return result;
        }

        Message sendMsg = new Message("all", "jgts_PQ", appSprayPaintIndentEntity.getSprayPaintNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    /**
     * @param request
     * @param type
     * @param pagesize
     * @return
     */
    @ApiOperation(value = "喷漆列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.全部，2.新订单，3.服务中, 4.待付款 5.待评价,6.已取消", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/sprayPaintIndentList", method = RequestMethod.GET)
    public JSONObject sprayPaintIndentList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        List<AppCleanIndetEntity> sprayPaintList = appSprayPaintIndentService.newSprayPaintIndentList(userBId, pagesize, type);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", sprayPaintList);
        return result;
    }
}
