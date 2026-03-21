package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.service.*;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代驾订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-06-09
 */
@RestController
@RequestMapping("/appSubstituteDrivingIndent")
public class AppSubstituteDrivingIndentController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(AppSubstituteDrivingIndentController.class);


    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppSubstituteDrivingImgService appSubstituteDrivingImgService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private CUserService cUserService;

    @Resource
    private AppUserBMessageService appUserBMessageService;

    @Resource
    private UserService BUserService;

    @Resource
    private DefaultMQProducer mqProducer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;


    //b端确认到达指定地点
    @ApiOperation(value = "到达指定位置" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/toStartAddress", method = RequestMethod.GET)
    public JSONObject toStartAddress(String orderNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        //查询到代驾服务
        EntityWrapper<AppUserBMessageEntity> bMessageEntityEntityWrapper = new EntityWrapper<>();
        bMessageEntityEntityWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", 4);
        //代驾技师
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(bMessageEntityEntityWrapper);


        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "订单编号为空");
            return result;
        }

        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteWrapper = new EntityWrapper<>();
        appSubstituteWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(appSubstituteWrapper);
        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }

        if (!appUserBMessageEntity.getId().equals(appSubstitute.getUserBId())) {
            result.put("code", 405);
            result.put("msg", "请操作自己订单");
            return result;
        }

        //确认到达指定目标
        appSubstitute.setIndentState(3);
        appSubstitute.setBeginTime(new Date());
        appSubstituteDrivingIndentService.updateById(appSubstitute);

        Message sendMsg = new Message("all", "jgts_SC", appSubstitute.getSubstituteDrivingNumber().getBytes());

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


    @ApiOperation(value = "去接车" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/toPickCar", method = RequestMethod.GET)
    public JSONObject toPickCar(String orderNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "订单编号为空");
            return result;
        }
        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteWrapper = new EntityWrapper<>();
        appSubstituteWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(appSubstituteWrapper);


        EntityWrapper<AppUserBMessageEntity> bMessageEntityEntityWrapper = new EntityWrapper<>();
        bMessageEntityEntityWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", 4);
        //代驾技师
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(bMessageEntityEntityWrapper);

        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }

        if (!appUserBMessageEntity.getId().equals(appSubstitute.getUserBId())) {
            result.put("code", 405);
            result.put("msg", "请操作自己订单");
            return result;
        }

        appSubstitute.setIndentState(2);
        appSubstituteDrivingIndentService.updateById(appSubstitute);


        Message sendMsg = new Message("all", "jgts_SC", appSubstitute.getSubstituteDrivingNumber().getBytes());

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


    //B端确认接车
    @ApiOperation(value = "B端确认接车，到达目的地" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型（1,确认接车，2.到达目的地）", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "url", value = "图片地址", required = true, dataType = "String[] "),
    })
    @RequestMapping(value = "/reachDesignatedPosition", method = RequestMethod.POST)
    public JSONObject reachDesignatedPosition(@RequestBody JSONObject in, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //String orderNumber, String[] url, Integer type,
        //查询到代驾订单
        String orderNumber = in.getString("orderNumber");
        Integer type = in.getInteger("type");

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "订单编号为空");
            return result;
        }

        if (type != 1 && type != 2) {
            result.put("code", 405);
            result.put("msg", "检查type参数");
            return result;
        }

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray urls = (JSONArray) json.get("url");
        if (urls == null) {
            result.put("code", 407);
            result.put("msg", "检查图片参数");
            return result;
        }
        List<String> urlss = JSONArray.parseArray(urls.toString(), String.class);

        //String[] array2 = testList.toArray(new String[testList.size()]);
        String[] url = urlss.toArray(new String[urlss.size()]);

        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteWrapper = new EntityWrapper<>();
        appSubstituteWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(appSubstituteWrapper);
        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }
        Integer time;
        if (type == 1) {
            //保存等待时间，接车时间，接车图片
            appSubstitute.setPickCarTime(new Date());
            appSubstitute.setIndentState(4);
            time = appSubstituteDrivingIndentService.gettime(appSubstitute);
            if (time == null) {
                result.put("code", 405);
                result.put("msg", "检查订单编号");
                return result;
            }
            appSubstitute.setWaitTime(time);
            appSubstitute.setUpdateTime(new Date());
            appSubstituteDrivingIndentService.updateById(appSubstitute);
            //保存图片
            appSubstituteDrivingImgService.saveindentImg(url, appSubstitute.getId(), type);
        } else {
            //保存送车时间，送车图片
            appSubstitute.setSendTime(new Date());
            appSubstitute.setIndentState(5);
            appSubstitute.setEndTime(new Date());
            appSubstituteDrivingIndentService.updateById(appSubstitute);
            //保存图片
            appSubstituteDrivingImgService.saveindentImg(url, appSubstitute.getId(), type);

        }

        Message sendMsg = new Message("all", "jgts_SC", appSubstitute.getSubstituteDrivingNumber().getBytes());

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


    //代驾主页
    @ApiOperation(value = "代驾中心" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/subStituteDriIndentCenter", method = RequestMethod.GET)
    public JSONObject subStituteDriIndentCenter(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        //判断服务是否通过
        EntityWrapper<LableDetailsReviewTreeEntity> detaulsWrapper = new EntityWrapper<>();
        detaulsWrapper.eq("user_b_id", userBId)
                .eq("state", 1)
                .eq("lable_id", 19);
        LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.selectOne(detaulsWrapper);
        if (lableDetailsReviewTreeEntity == null) {
            result.put("code", 408);
            result.put("msg", "暂未开通代驾服务");
            return result;
        }

        JSONObject js = appSubstituteDrivingIndentService.findSubDirCenterDetails(userBId);
        if (js == null) {
            result.put("code", 408);
            result.put("msg", "暂未开通代驾服务");
            return result;
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", js);
        return result;
    }


    //代驾主页
    @ApiOperation(value = "代驾列表" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.全部，2.新订单，3.服务中, 4.待付款 5.待评价,6.已取消", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/subStituteDriIndentList", method = RequestMethod.GET)
    public JSONObject subStituteDriIndentList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //查询代驾列表
        List<AppCleanIndetEntity> subDirIndentList = appSubstituteDrivingIndentService.newSubDriIndentList(userBId, type, pagesize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", subDirIndentList);
        return result;
    }

    @ApiOperation(value = "代驾详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/subDriDetails", method = RequestMethod.GET)
    public JSONObject subDriDetails(String cleanIndentNumber) {
        JSONObject result = new JSONObject();
        //查询到订单详情
        EntityWrapper<AppSubstituteDrivingIndentEntity> appSubstituteWrapper = new EntityWrapper<>();
        appSubstituteWrapper.eq("substitute_driving_number", cleanIndentNumber);
        AppSubstituteDrivingIndentEntity appSubstituteDrivingIndentEntity = appSubstituteDrivingIndentService.selectOne(appSubstituteWrapper);

        if (appSubstituteDrivingIndentEntity == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }
        //地点，价格，车距
        JSONObject object = new JSONObject();

        //用户名，电话
        String startName = appSubstituteDrivingIndentEntity.getStartName();
        if (startName == null) {
            Integer userId = appSubstituteDrivingIndentEntity.getUserId();
            CUserEntity cUserEntity = cUserService.selectById(userId);
            object.put("username", cUserEntity.getName());
            object.put("phone", cUserEntity.getPhoneNumber());
            object.put("endName",null);
            object.put("endPhone",null);
        } else {
            object.put("username", startName);
            object.put("phone", appSubstituteDrivingIndentEntity.getStartPhone());
            object.put("endName",appSubstituteDrivingIndentEntity.getEndName());
            object.put("endPhone",appSubstituteDrivingIndentEntity.getEndPhone());
        }


        object.put("startPoint", appSubstituteDrivingIndentEntity.getStartPoint());
        object.put("startAddress", appSubstituteDrivingIndentEntity.getStartAddress());
        //经纬度
        object.put("startLng", appSubstituteDrivingIndentEntity.getStartLng());
        object.put("startLat", appSubstituteDrivingIndentEntity.getStartLat());

        object.put("endPoint", appSubstituteDrivingIndentEntity.getEndPoint());
        object.put("endAddress", appSubstituteDrivingIndentEntity.getEndAddress());
        object.put("endLng", appSubstituteDrivingIndentEntity.getEndLng());
        object.put("endLat", appSubstituteDrivingIndentEntity.getEndLat());

        //判断是否使用优惠卷
        Integer integer = appSprayPaintIndentService.selectByCoupon(cleanIndentNumber);
        if (integer==null){
            BigDecimal actualPrice = appSubstituteDrivingIndentEntity.getActualPrice();
            BigDecimal multiply = actualPrice.multiply(new BigDecimal("0.8")).setScale(2,BigDecimal.ROUND_HALF_UP);
            object.put("price", multiply);
        }
        object.put("price",appSubstituteDrivingIndentEntity.getActualPrice());

        object.put("distance", appSubstituteDrivingIndentEntity.getDistance());
        object.put("state", appSubstituteDrivingIndentEntity.getIndentState());
        object.put("payState", appSubstituteDrivingIndentEntity.getPayState());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date pickCarTime = appSubstituteDrivingIndentEntity.getPickCarTime();
        if (pickCarTime != null) {
            String format = sdf.format(pickCarTime);
            object.put("pickCarTime", format);
        } else {
            object.put("pickCarTime", pickCarTime);
        }

        Date sendTime = appSubstituteDrivingIndentEntity.getSendTime();
        if (sendTime != null) {
            String format1 = sdf.format(sendTime);
            object.put("sendCarTime", format1);
        } else {
            object.put("sendCarTime", sendTime);
        }


        //查询图片
        EntityWrapper<AppSubstituteDrivingImgEntity> appSubstituteImgWrapper = new EntityWrapper<>();
        appSubstituteImgWrapper.eq("subsitute_driving_id", appSubstituteDrivingIndentEntity.getId())
                .eq("type", 1);
        List<AppSubstituteDrivingImgEntity> appSubstituteDrivingImgEntities = appSubstituteDrivingImgService.selectList(appSubstituteImgWrapper);
        object.put("pickCarImg", appSubstituteDrivingImgEntities);

        EntityWrapper<AppSubstituteDrivingImgEntity> appSubstituteImgWrapper2 = new EntityWrapper<>();
        appSubstituteImgWrapper2.eq("subsitute_driving_id", appSubstituteDrivingIndentEntity.getId())
                .eq("type", 2);
        List<AppSubstituteDrivingImgEntity> appSubstituteDrivingImgEntities2 = appSubstituteDrivingImgService.selectList(appSubstituteImgWrapper2);
        object.put("sendCarImg", appSubstituteDrivingImgEntities2);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", object);
        return result;

    }

    //取消订单
    @ApiOperation(value = "取消订单" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderNumber", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reason", value = "取消原因", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/cancelSubstituteIndent", method = RequestMethod.GET)
    public JSONObject cancelSubstituteIndent(String orderNumber, String reason, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        if (StringUtils.isEmpty(orderNumber)) {
            result.put("code", 405);
            result.put("msg", "订单编号为空");
            return result;
        }
        //查询到代驾订单
        EntityWrapper<AppSubstituteDrivingIndentEntity> drivingIndentEntityWrapper = new EntityWrapper<>();
        drivingIndentEntityWrapper.eq("substitute_driving_number", orderNumber);
        AppSubstituteDrivingIndentEntity appSubstitute = appSubstituteDrivingIndentService.selectOne(drivingIndentEntityWrapper);
        if (appSubstitute == null) {
            result.put("code", 405);
            result.put("msg", "检查订单编号");
            return result;
        }

        EntityWrapper<AppUserBMessageEntity> bMessageEntityEntityWrapper = new EntityWrapper<>();
        bMessageEntityEntityWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", 4);
        //代驾技师
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(bMessageEntityEntityWrapper);

        //判断操作
        if (!appSubstitute.getUserBId().equals(appUserBMessageEntity.getId())) {
            result.put("code", 405);
            result.put("msg", "请操作自己订单");
            return result;
        }

        if (appSubstitute.getIndentState() > 3) {
            result.put("code", 405);
            result.put("msg", "目前订单状态不支持取消");
            return result;
        }

        appSubstitute.setIndentState(8);
        appSubstitute.setCancelReason(reason);
        appSubstitute.setCancelResource("B");
        appSubstituteDrivingIndentService.updateById(appSubstitute);

        Message sendMsg = new Message("all", "jgts_SC", appSubstitute.getSubstituteDrivingNumber().getBytes());

        //默认3秒超时
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(sendMsg);
            logger.error("###  mq推送#### sendResult={};sendMsg={}", sendResult, sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("code", 200);
        result.put("msg", "订单已取消");
        return result;


    }

    @ApiOperation(value = "上线听单" +
            "405：参数有误" +
            "408：未开通代驾服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/onlineListenSingle", method = RequestMethod.GET)
    public JSONObject onlineListenSingle(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        EntityWrapper<AppUserBMessageEntity> bMessageEntityEntityWrapper = new EntityWrapper<>();
        bMessageEntityEntityWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", 4);
        //代驾技师
        AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectOne(bMessageEntityEntityWrapper);

        EntityWrapper<LableDetailsReviewTreeEntity> detaulsWrapper = new EntityWrapper<>();
        detaulsWrapper.eq("user_b_id", userBId)
                .eq("state", 1)
                .eq("lable_id", 19);
        LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.selectOne(detaulsWrapper);
        if (lableDetailsReviewTreeEntity == null || appUserBMessage == null) {
            result.put("code", 408);
            result.put("msg", "暂未开通代驾服务");
            return result;
        }


        AppUserEntity appUserEntity = BUserService.selectById(userBId);
        Integer businessType = appUserBMessage.getBusinessType();

        if (businessType == null || businessType != 1) {
            //为空就添加数据
            //stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, substituteRedis);
            JSONObject js = new JSONObject();
            js.put("id", appUserBMessage.getId());
            js.put("merchantsName", appUserBMessage.getName());
            js.put("lat", appUserBMessage.getLat());
            js.put("lng", appUserBMessage.getLng());
            js.put("address", appUserBMessage.getWorkPlace());
            //修改geo数据
            stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), js.toJSONString());
            appUserEntity.setSubstituteRedis(js.toJSONString());
            appUserBMessage.setBusinessType(1);
        } else {
            stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, appUserEntity.getSubstituteRedis());
            appUserEntity.setSubstituteRedis(null);
            appUserBMessage.setBusinessType(2);
        }
        appUserBMessageService.updateById(appUserBMessage);
        BUserService.updateById(appUserEntity);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }



}
