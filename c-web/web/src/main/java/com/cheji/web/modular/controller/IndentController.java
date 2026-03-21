package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.modular.cwork.IndentAndMerchants;
import com.cheji.web.modular.cwork.IndentDetails;
import com.cheji.web.modular.cwork.IndentList;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.GenerateDigitalUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/indent")
public class IndentController extends BaseController {
    @Resource
    private IndentService indentService;

    @Resource
    private BUserService bUserService;

    @Resource
    private UserService userService;

    @Resource
    private ImgService ImgService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @Resource
    private AppPhotoMerService appPhotoMerService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;


    //返回订单页面上的详情
    @ApiOperation(value = "下订单页面上面用户信息和商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "MerchanstId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "UserId", value = "用户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/indentMes", method = RequestMethod.GET)
    public JSONObject indentMes(HttpServletRequest request, String MerchanstId) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AppUserEntity appUserEntity = currentLoginUser.getAppUserEntity();
        String phoneNumber1 = appUserEntity.getPhoneNumber();
        String name1 = appUserEntity.getName();

        IndentAndMerchants getMer = new IndentAndMerchants();

        if (MerchanstId.endsWith("Z")) {
            MerchanstId = MerchanstId.substring(0, MerchanstId.length() - 1);
            AppUpMerchantsEntity appUpM = appUpMerchantsService.selectById(MerchanstId);

            getMer.setName(name1);
            getMer.setLevelName("理赔经理");
            getMer.setCounts(500);
            getMer.setPhoneNumber(phoneNumber1);
            getMer.setMerchantsName(appUpM.getName());

            EntityWrapper<AppPhotoMerEntity> appPhotoWrapper = new EntityWrapper<>();
            appPhotoWrapper.eq("up_id", MerchanstId)
                    .eq("`index`", 1);
            AppPhotoMerEntity appPhotoMer = appPhotoMerService.selectOne(appPhotoWrapper);
            getMer.setImgurl(appPhotoMer.getUrl());
            getMer.setAddress(appUpM.getAddress());
            getMer.setLat(appUpM.getLat());
            getMer.setLng(appUpM.getLng());

        } else {
            //根据商户id和userid查询到商户和用户的数据
            //先获取到商户信息
            getMer = bUserService.indentMerchants(MerchanstId);
            //获取到用户信息
            //   IndentAndMerchants getUser = userService.getUserByid(UserId);
            Integer id = currentLoginUser.getAppUserEntity().getId();
            IndentAndMerchants getUser = userService.userMerchants(String.valueOf(id));
            //拼接数据
            String name = getUser.getName();
            String levelName = getUser.getLevelName();
            Integer counts = getUser.getCounts();
            String phoneNumber = getUser.getPhoneNumber();

            getMer.setName(name);
            getMer.setLevelName(levelName);
            getMer.setCounts(counts);
            getMer.setPhoneNumber(phoneNumber);
        }

        //查询到送修单位
        List<String> sendUnit = indentService.findSendUnit();
        //查询到选择人员
        List<String> employeeName = indentService.findEmployee();
        employeeName.add(0, "暂无");

        getMer.setSendUnit(sendUnit);
        getMer.setEmployeeName(employeeName);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", getMer);
        return result;
    }

    //保存订单详情
    @ApiOperation(value = "保存订单详情" +
            "商户id：userBId" +
            "维修方案：plan" +
            "施救费：rescueThemFee" +
            "责任划分：responsibility" +
            "保险公司：insuranceCompany" +
            "姓名：username" +
            "电话：phoneNumber" +
            "车牌号：licensePlate" +
            "品牌：brandId" +
            "理赔资料图片：imgEntityList" +
            "结款方式：meansPayMents" +
            "送修人：sendPeople" +
            "送修单位：sendUnit" +
            "成交时间 1，白天。 2，夜晚：dealTime" +
            "信息来源：1.公司信息，2，自开发：messageSource" +
            "订单备注(100字)：remake")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indent", value = "订单对象", required = true, dataType = "String")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JSONObject save(@RequestBody IndentEntity indent, HttpServletRequest request) {
        // Log log = LogFactory.getLog(getClass());
        //  log.info("indent:"+indent);
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (StringUtils.isEmpty(indent.getUserBId().toString())) {
            result.put("code", 500);
            result.put("msg", "商户id为空");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        String userBId = indent.getUserBId();
        //log.info("商户id=========:"+merchantsId);
        if (userBId.endsWith("Z")) {
            //车己汽车
            indent.setUserBId("150");
            String upid = userBId.substring(0, userBId.length() - 1);
            indent.setUpId(upid);
        } else {
            indent.setUserBId(userBId);
        }
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "sh" + orderNo;
        indent.setOrderNumber(orderNo);
        indent.setUserId(userid);
        indent.setCreatTime(new Date());
        indentService.insert(indent);
        //获取到图片数据，保存到表
        String[] imgEntityList = indent.getImgEntityList();
        Long id = indent.getId();
        ImgService.save(imgEntityList, id);
        /*
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;
        private String source;
        private String ispush;
        private String type;
        @TableField("user_id")
        private String userId;
        @TableField("user_b_id")
        private String userBId;
        @TableField("create_time")
        private Date createTime;
        @TableField("update_time")
        private Date updateTime;*/
//        AppJgPushEntity appJgPushEntity = new AppJgPushEntity();
//        appJgPushEntity.setSource("B");
//        appJgPushEntity.setIspush("0");
//        appJgPushEntity.setType("4");
//        appJgPushEntity.setUserBId(indent.getUserBId().toString());
//        appJgPushEntity.setCreateTime(new Date());
//        appJgPushEntity.setUpdateTime(new Date());
//        appJgPushService.insert(appJgPushEntity);
//        try {
//            Producer.productSend(appJgPushEntity);
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indent);
        return result;
    }

    //订单列表2
    @ApiOperation(value = "我的订单列表" +
            "状态(1,新订单,2.未到店,3.服务中,4.已交车,5.已结算）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "订单状态", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/indentList22", method = RequestMethod.GET)
    public JSONObject indentList22(HttpServletRequest request /*Integer id*/,
                                   String state, String year, String month, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        JSONObject indentListByUserid = indentService.findIndentListByUserid22(String.valueOf(id), year, month, state, pagesize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentListByUserid);
        return result;
    }


    //订单列表
    @ApiOperation(value = "我的订单列表" +
            "状态(1,新订单,2.未到店,3.服务中,4.已交车,5.已结算）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "month", value = "月", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "订单状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/indentList", method = RequestMethod.GET)
    public JSONObject indentList(HttpServletRequest request, String state, String year, String month, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<IndentList> indentListByUserid = indentService.findIndentListByUserid(String.valueOf(id), year, month, state, pagesize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentListByUserid);
        return result;
    }

    //订单详情,根据订单id查询到订单详情
    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "IndentId", value = "订单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/IndentDetails", method = RequestMethod.GET)
    public JSONObject IndentDetails(HttpServletRequest request, String IndentId) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        IndentDetails indentDetilsByIndentId = indentService.findIndentDetilsByIndentId(IndentId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentDetilsByIndentId);
        return result;
    }

    @ApiOperation(value = "确认订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentOrder", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.GET)
    public JSONObject confirmOrder(String indentOrder) {
        JSONObject result = new JSONObject();

        //根据订单号查询到下单成功页面
        IndentEntity indentByOrderNum = indentService.findIndentByOrderNum(indentOrder);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentByOrderNum);
        return result;
    }


    @ApiOperation(value = "其他订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "全部订单到已结算对应0-5", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/otherIndentList", method = RequestMethod.GET)
    public JSONObject otherIndentList(HttpServletRequest request, String type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        if (StringUtils.isEmpty(type)) {
            result.put("code", 520);
            result.put("msg", "type不能为空");
            return result;
        }
        //根据用户id查询到对应数据
        List<CleanIndetEntity> cleanIndent = cleanIndetService.findCleanIndent(id, pagesize, type);
        for (CleanIndetEntity cleanIndetEntity : cleanIndent) {
            if (cleanIndetEntity.getResource().equals("4")) {
                EntityWrapper<AppSprayPaintIndentEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("spray_paint_number", cleanIndetEntity.getCleanIndentNumber());
                AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(wrapper);
                //喷漆
                if (appSprayPaintIndentEntity.getIsOffer() == 0) {
                    //待报价
                    cleanIndetEntity.setIndentState("-1");
                } else if (cleanIndetEntity.getIndentState().equals("2")) {

                    if (appSprayPaintIndentEntity.getPayState() == null || appSprayPaintIndentEntity.getPayState() != 1) {
                        //待支付
                        cleanIndetEntity.setIndentState("-2");
                    }
                }
            }
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", cleanIndent);
        return result;
    }


    @ApiOperation(value = "查询车辆型号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "图片", required = true, dataType = "String")
    })
    @RequestMapping(value = "/selectCarType", method = RequestMethod.GET)
    public JSONObject selectCarType(HttpServletRequest request,String url) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (StringUtils.isEmpty(url)) {
            result.put("code", 407);
            result.put("msg", "url不能为空");
            return result;
        }

        return indentService.selectCarType(result,url);

    }


}
