package com.cheji.web.modular.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.context.SourceContext;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.dto.AuctionCarListDto;
import com.cheji.web.modular.dto.UsedCarVo;
import com.cheji.web.modular.mapper.AppAuctionMapper;
import com.cheji.web.modular.mapper.AppAuctionUpMapper;
import com.cheji.web.modular.service.AppAuctionCollectService;
import com.cheji.web.modular.service.AppAuctionService;
import com.cheji.web.modular.service.AppAuctionUpService;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.AssertUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @apiDefine appAuction 拍卖车二手车
 */

/**
 * <p>
 * 拍卖信息添加表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/appAuction")
public class AppAuctionContrlloer extends BaseController {

    @Autowired
    private AppAuctionService appAuctionService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionMapper appAuctionMapper;

    @Autowired
    private AppAuctionCollectService appAuctionCollectService;

    @Resource
    private AppAuctionUpMapper appAuctionUpMapper;

    final Integer size = 20;


    @Autowired
    private WxMaService wxMaService;

    @Resource
    private Map<String, WxMaService> sourceToWxMaServiceMap;


    /**
     * 获取小程序token
     *
     * @return
     */
    @ApiOperation(value = "获取小程序token")
    @RequestMapping(value = "/getMinAppToken", method = RequestMethod.POST)
    public JSONObject getMinAppToken() {
        String accessToken = "";
        try {
            // 根据当前请求的 source 获取对应的 WxMaService
            String source = SourceContext.getSource();
            WxMaService maService = (source != null) ? sourceToWxMaServiceMap.get(source) : null;
            if (maService == null) {
                // 兜底：使用默认的共享 service
                maService = wxMaService;
            }
            accessToken = maService.getAccessToken();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

        JSONObject result = new JSONObject();
        result.put("token", accessToken);
        //app下载地址，该地址是 应用宝下载app的二维码  安卓和ios通用
        result.put("downUrl", "https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/system/cjlp_download.png");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("data", result);
        return jsonObject;
    }


    //添加拍卖车辆基本信息
    @ApiOperation(value = "保存拍卖对象")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "days", value = "拍卖天数", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "phone", value = "车辆所有人电话", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "insuredAmount", value = "保险金额", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "productDate", value = "出厂日期", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "price", value = "起拍价", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "是否是一口价,0否,1是", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "plateNo", value = "车牌号", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "name", value = "所有人名字", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌型号", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "auctionType", value = "拍卖类型", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "displacement", value = "排量", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "transmission", value = "传动类型,1,自动,0,手动", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "fuel", value = "燃油类型,0,汽油,1,柴油,2电,3混动", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "registerDate", value = "注册日期", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "issueDate", value = "发证日期", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "annualCheck", value = "年检期限", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "compulsoryInsuranceVilidity", value = "交强险有效期", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "registeredResidence", value = "户籍地", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "accidentType", value = "车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.无记录", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "sunroof", value = "是否有天窗", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "frameNo", value = "车架号", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "useNature", value = "使用性质,1运营,0非运营", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "frameNoDamagedCondition", value = "车架号是否受损,0否,1是", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "engine", value = "发动机", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "damageReason", value = "车损原因", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "mileage", value = "行驶里程", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "key", value = "钥匙", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "purchaseTax", value = "购置税", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "plateNumber", value = "车牌数", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "registrationCertificate", value = "登记证书", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "createTime", value = "创建时间", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "updateTime", value = "修改时间", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "overhaul", value = "是否拆检,0否,1是", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "announcements", value = "注意事项", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "carState", value = "车辆拍卖状态码:0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "upState", value = "上架状态:0,未上架,1,已上架", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "otherField", value = "扩展字段", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "color", value = "车身颜色", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "mortgage", value = "抵押按揭", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "second", value = "二次事故", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "change", value = "是否改装", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "transfer", value = "过户次数", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "owner", value = "所有人,个人,公司", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "duty", value = "事故责任", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "dutyBook", value = "责任书", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "drivingLicense", value = "行驶证", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "licence", value = "驾照", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "insurance", value = "保险公司", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "repair", value = "维修预估", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "insRemark", value = "保险备注", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "imgList", value = "车辆图片集合", required = true, dataType = "String[]"),})
    @RequestMapping(value = "/addAuctionCar", method = RequestMethod.POST)
    public JSONObject addAuctionCar(@RequestBody @Valid AppAuctionEntity auctionEntity, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        JSONObject result = new JSONObject();
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (auctionEntity.getAccidentType() != null) {
            try {
                AssertUtil.isContains("12345", auctionEntity.getAccidentType(), "车辆状态设置错误");
            } catch (Exception e) {
                result.put("code", 531);
                result.put("msg", e.getMessage());
                return result;
            }
        }

        if (auctionEntity.getId() != null) {
            //有id就是修改，没有id就是新增，
            try {
                return appAuctionService.updateAuctionCar(auctionEntity);
            } catch (ParseException e) {
                e.printStackTrace();
                result.put("code", 4001);
                result.put("msg", "行驶证解析失败!");
                return result;
            }
        } else {
            try {
                return appAuctionService.addAuctionCar(auctionEntity, currentLoginUser.getAppUserEntity().getId().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                result.put("code", 4001);
                result.put("msg", "行驶证解析失败!");
                return result;
            }
        }
    }


    /**
     * @api {POST} /appAuction/
     * addUsedCar      保存二手车
     * @apiGroup appAuction
     * @apiSampleRequest off
     * @apiVersion 1.0.0
     * @apiDescription ……
     * @apiUse BaseHeader
     * @apiParam {Integer}      id            有就是修改，没有就是新增
     * @apiParam {String}      drivingLicense           行驶证图片
     * @apiParam {String}      plateNo          车牌号
     * @apiParam {String}      brand           品牌型号
     * @apiParam {String}      name           车主名称
     * @apiParam {String}      phone      电话
     * @apiParam {String}      licenseAddress     行驶证地址
     * @apiParam {String}      carIdentificationNumber           车辆识别码
     * @apiParam {String}      engine           发动机号码
     * @apiParam {String}      registerDate           注册日期
     * @apiParam {String}      issueDate           发证日期
     * @apiParam {String}      useNature           1运营,0非运营'
     * @apiParam {String}      productDate           出厂日期
     * @apiParam {String}      color       颜色
     * @apiParam {String}      parkingPlace         停放地
     * @apiParam {String}      accidentType            事故类型 '车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.无记录',
     * @apiParam {String}      fuel           燃油类型
     * @apiParam {String}      displacement     排量
     * @apiParam {String}      mileage          行驶里程
     * @apiParam {String}      change         是否改装，0否，1是
     * @apiParam {String}      key           钥匙
     * @apiParam {String}      plateNumber          车牌数量
     * @apiParam {String}      overhaul     是否拆解
     * @apiParam {String}      frameNoDamagedCondition          车架号是否受损
     * @apiParam {String}      transfer          过户次数
     * @apiParam {String}      annualCheck          年检期限
     * @apiParam {String}      compulsoryInsuranceVilidity         交强险日期
     * @apiParam {String}      owner        所有人，公司，个人
     * @apiParam {String}      carSourceType         车源类型
     * @apiParam {String}      damageReason         车损原因
     * @apiParam {String}      auctionType         车源类型
     * @apiParam {String}      fixedPrice         拍卖模式（一口价/拍卖）
     * @apiParam {String}      price         一口价
     * @apiParam {String}      carIntroduction         车辆介绍
     * @apiParam {String}      carState         提交状态 0,保存1,待审核
     * @apiParam {String[]}     beforeRepair         修复之前图片地址
     * @apiParam {String[]}      afterRepair         修复之后图片地址
     * @apiParamExample {json} 请求示例：
     * <p>
     * {
     * "id":"1582937691542438891",
     * "drivingLicense": "行驶证图片",
     * "plateNo": "车牌号",
     * "brand": "品牌型号",
     * "name": "车主名称121",
     * "phone": "车主电话",
     * "licenseAddress": "行驶证地址",
     * "carIdentificationNumber": "车辆识别码",
     * "engine": "发动机i号码",
     * "registerDate": "2022-05-16",
     * "issueDate": "2022-05-16",
     * "useNature": "使用性质，1，运营",
     * "productDate": "2022-05-16",
     * "color": "颜色",
     * "parkingPlace": "停放地",
     * "accidentType": "事故类型",
     * "fuel": "燃油类型",
     * "displacement": "排量",
     * "mileage": "行驶里程",
     * "change": "是否改装，0否，1是",
     * "key": "钥匙",
     * "plateNumber": "车牌数量",
     * "overhaul": "是否拆解，0，否。1，是",
     * "frameNoDamagedCondition": "0",
     * "transfer": "过户次数",
     * "annualCheck": "2022-05-16",
     * "compulsoryInsuranceVilidity": "2022-05-16",
     * "owner": "所有人",
     * "damageReason": "车损原因",
     * "auctionType": "车源类型",
     * "fixedPrice": "拍卖模式",
     * "price": 10000,
     * "beforeRepair": [
     * "修复之前图片地址",
     * "地址2"
     * ],
     * "afterRepair": [
     * "修复之后图片地址",
     * "地址12"
     * ]
     * }
     * @apiSuccess (成功响应) {Object}      data                        数据
     * @apiUse BaseResult
     * @apiSuccessExample {json} 成功响应示例：
     * {
     * "msg": "成功",
     * "code": 200
     * }
     * @apiUse CommonError
     */
    @RequestMapping(value = "/addUsedCar", method = RequestMethod.POST)
    public JSONObject addUsedCar(@RequestBody @Valid UsedCarVo usedCarVo, HttpServletRequest request) {
        //判断登录
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        JSONObject result = new JSONObject();
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
//        if (usedCarVo.getBeforeRepair().isEmpty()){
//            result.put("code", 407);
//            result.put("msg", "修复前照片不能为空");
//            return result;
//        }
        return appAuctionService.saveOrUpdate(usedCarVo, currentLoginUser.getAppUserEntity().getId());
    }


    /**
     * @api {POST} /appAuction/findListByUsedCar      查询二手车列表
     * @apiGroup appAuction
     * @apiSampleRequest off
     * @apiVersion 1.0.0
     * @apiDescription ……
     * @apiUse BaseHeader
     * @apiParam {Integer}      type            新能源，全损车，.无记录，.水淹车，.质保车，一口价，
     * @apiParam {String}      current           当前页数。从1开始
     * @apiParam {String}      keyWord           当前页数。从1开始
     * @apiParamExample {json} 请求示例：
     * <p>
     * {
     * "type":"5",
     * "keyWord":"大众",
     * "current":1
     * }
     * @apiSuccess (成功响应) {Object}      data                            数据
     * @apiSuccess (成功响应) {String}      data.carId                        车辆id
     * @apiSuccess (成功响应) {String}      data.brand                        品牌
     * @apiSuccess (成功响应) {String}      data.label                        标签
     * @apiSuccess (成功响应) {String}      data.url                        图片地址
     * @apiSuccess (成功响应) {Double}      data.price                        价格
     * @apiUse BaseResult
     * @apiSuccessExample {json} 成功响应示例：
     * {
     * "msg": "成功",
     * "code": 200,
     * "data": [
     * {
     * "carId": 1582937691542438891,
     * "brand": "品牌型号",
     * "label": "标签空格隔开",
     * "url": "图片地址",
     * "price": 10000.00
     * }
     * ]
     * }
     * @apiUse CommonError
     */
    //查询列表
    @RequestMapping(value = "/findListByUsedCar", method = RequestMethod.POST)
    public JSONObject findListByUsedCar(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Integer type = jsonObject.getInteger("type");
        Integer current = jsonObject.getInteger("current");
        String  keyWord = jsonObject.getString("keyWord");

        Map<String, Object> params = jsonObject.getInnerMap();
        com.cheji.web.util.Page page = new com.cheji.web.util.Page();
        page.setCurrentPage(current == null ? 1 : current);
        page.setShowCount(20);
        params.put("page", page);

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        return appAuctionService.findListByUsedCar(params, currentLoginUser == null ? null : currentLoginUser.getAppUserEntity().getId());
    }


    /**
     * @api {POST} /appAuction/findListHotByUsedCar      查询热门推荐
     * @apiGroup appAuction
     * @apiSampleRequest off
     * @apiVersion 1.0.0
     * @apiDescription ……
     * @apiUse BaseHeader
     * @apiParam {Integer}      current           当前页数。从1开始
     * @apiParam {String}      city           城市
     * @apiParamExample {json} 请求示例：
     * <p>
     * {
     * "current":1,
     * "city":"成都市"
     * }
     * @apiSuccess (成功响应) {Object}      data                            数据
     * @apiSuccess (成功响应) {String}      data.carId                        车辆id
     * @apiSuccess (成功响应) {String}      data.brand                        品牌
     * @apiSuccess (成功响应) {String}      data.label                        标签
     * @apiSuccess (成功响应) {String}      data.url                        图片地址
     * @apiSuccess (成功响应) {Double}      data.price                        价格
     * @apiUse BaseResult
     * @apiSuccessExample {json} 成功响应示例：
     * {
     * "msg": "成功",
     * "code": 200,
     * "data": [
     * {
     * "carId": 1582937691542438891,
     * "brand": "品牌型号",
     * "label": "标签空格隔开",
     * "url": "图片地址",
     * "price": 10000.00
     * }
     * ]
     * }
     * @apiUse CommonError
     */
    //查询列表
    @RequestMapping(value = "/findListHotByUsedCar", method = RequestMethod.POST)
    public JSONObject findListHotByUsedCar(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Integer current = jsonObject.getInteger("current");
        String  city = jsonObject.getString("city");
        com.cheji.web.util.Page page = new com.cheji.web.util.Page();
        page.setCurrentPage(current == null ? 1 : current);
        page.setShowCount(20);
        Map<String, Object> params = new HashMap<String, Object>(1) {{
            put("page", page);
            put("city", city);
        }};
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        return appAuctionService.findListHotByUsedCar(params, currentLoginUser == null ? null : currentLoginUser.getAppUserEntity().getId());
    }

    /**
     * @api {POST} /appAuction/findUsedCarDetail      查询二手车详情
     * @apiGroup appAuction
     * @apiSampleRequest off
     * @apiVersion 1.0.0
     * @apiDescription ……
     * @apiUse BaseHeader
     * @apiParam {Integer}      carId            车辆id
     * @apiParamExample {json} 请求示例：
     * <p>
     * {
     * "carId": 1582937691542438895
     * }
     * @apiSuccess (成功响应) {Object}      data                            数据
     * @apiUse BaseResult
     * @apiSuccessExample {json} 成功响应示例：
     * {
     * "msg": "成功",
     * "code": 200,
     * "data": {
     * "id": "1582937691542438895",
     * "drivingLicense": "https://watermark-a33d.obs.cn-southwest-2.myhuaweicloud.com/img/541684308734298327.png",
     * "plateNo": "川RK15978",
     * "brand": "大众 途锐 2017款 3.0 TSI 拓野版",
     * "name": "杨雪锋",
     * "phone": "15826111234",
     * "licenseAddress": "四川省阆中市阆南桥街13号附47号",
     * "carIdentificationNumber": "WVGAP97P5HD023006",
     * "engine": "CYJ023659",
     * "registerDate": "2017-05-10",
     * "issueDate": "2017-05-10",
     * "useNature": "0",
     * "productDate": "2016-06-15",
     * "color": "白色",
     * "parkingPlace": "河北省·石家庄市",
     * "accidentType": "2",
     * "fuel": "汽油",
     * "displacement": "3.0T",
     * "mileage": "2580",
     * "change": "0",
     * "key": "2",
     * "plateNumber": "2",
     * "overhaul": "0",
     * "frameNoDamagedCondition": "0",
     * "transfer": "25",
     * "annualCheck": "2018-05",
     * "compulsoryInsuranceVilidity": "2021-05-13T00:00:00.000+08:00",
     * "owner": "0",
     * "carSourceType": "客户拍卖",
     * "damageReason": "碰撞",
     * "auctionType": "1",
     * "price": 500.00,
     * "carState": 0,
     * "carIntroduction": null,
     * "beforeRepair": [
     * "https://watermark-a33d.obs.cn-southwest-2.myhuaweicloud.com/img/168430881661555.png"
     * ],
     * "afterRepair": [
     * "https://watermark-a33d.obs.cn-southwest-2.myhuaweicloud.com/img/168430881661555.png"
     * ]
     * }
     * }
     * @apiUse CommonError
     */
    //查询详情
    @RequestMapping(value = "/findUsedCarDetail", method = RequestMethod.POST)
    public JSONObject findUsedCarDetail(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        JSONObject result = new JSONObject();
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Long carId = jsonObject.getLong("carId");
        if (carId == null) {
            result.put("code", 407);
            result.put("msg", "参数有误");
            return result;
        }
        return appAuctionService.findUsedCarDetail(carId);
    }


    @ApiOperation(value = "提交过户审核")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "车辆id", required = true, dataType = "Long"), @ApiImplicitParam(paramType = "query", name = "imgList", value = "图片集合", required = true, dataType = "String[]"),})
    @RequestMapping(value = "/setTransfer", method = RequestMethod.POST)
    public JSONObject setTransfer(@RequestBody JSONObject obj, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.nonNull(currentLoginUser)) {
            Integer userId = currentLoginUser.getAppUserEntity().getId();
            obj.put("userId", userId);
            return appAuctionService.setTransfer(obj);
        } else {
            JSONObject object = new JSONObject();
            object.put("code", 200);
            object.put("msg", "请登录后提交!");
            return object;
        }
    }

    @ApiOperation(value = "获取过户资料")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "车辆ID", required = true, dataType = "Long")})
    @RequestMapping(value = "/getTransfer", method = RequestMethod.GET)
    public JSONObject getTransfer(@RequestParam(required = false) Long carId, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.nonNull(currentLoginUser)) {
            Integer userId = currentLoginUser.getAppUserEntity().getId();
            return appAuctionService.getTransfer(carId, userId);
        } else {
            JSONObject object = new JSONObject();
            object.put("code", 200);
            object.put("msg", "请登录后提交!");
            return object;
        }
    }

    @ApiOperation(value = "热门推荐")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市", required = true, dataType = "String")})
    @RequestMapping(value = "/auctionHot", method = RequestMethod.GET)
    public JSONObject auctionHot(@RequestParam(required = false) Integer current, @RequestParam(required = false) String city, HttpServletRequest request) {
        if (current > 1) {
            JSONObject result = new JSONObject();
            result.put("code", 200);
            result.put("msg", "无数据");
            result.put("data", null);
            return result;
        }
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.isNull(currentLoginUser)) {
            return appAuctionService.auctionHot(city, null);
        } else {
            return appAuctionService.auctionHot(city, currentLoginUser.getAppUserEntity().getId().longValue());
        }
    }

    @ApiOperation(value = "随机推荐")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "页数", required = true, dataType = "Integer")})
    @RequestMapping(value = "/auctionRandom", method = RequestMethod.GET)
    public JSONObject auctionRandom(@RequestParam(required = false) Integer current, HttpServletRequest request) {
        if (current > 1) {
            JSONObject result = new JSONObject();
            result.put("code", 200);
            result.put("msg", "无数据");
            result.put("data", null);
            return result;
        }
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.isNull(currentLoginUser)) {
            return appAuctionService.auctionRandom(null);
        } else {
            return appAuctionService.auctionRandom(currentLoginUser.getAppUserEntity().getId().longValue());
        }
    }

    @ApiOperation(value = "我的")
    @RequestMapping(value = "/myAuction", method = RequestMethod.GET)
    public JSONObject myAuction(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionService.myAuction(result, id);
    }

    @ApiOperation(value = "详情")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车辆id", required = true, dataType = "Long")})
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JSONObject detail(@RequestParam(required = false) Long carId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        JSONObject detail = appAuctionService.detail(carId);
//        AppAuctionEntity data = (AppAuctionEntity)detail.get("data");
//        if(data.getUserId() != null && !data.getUserId().equals(String.valueOf(id)))
//        data.setPhone("");
        return detail;
    }

    @ApiOperation(value = "详情2")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车辆id", required = true, dataType = "Long")})
    @RequestMapping(value = "/detail2", method = RequestMethod.GET)
    public JSONObject detail2(@RequestParam(required = false) Long carId, HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            return appAuctionService.detail2(carId, null);
        } else {
            Integer id = currentLoginUser.getAppUserEntity().getId();
            return appAuctionService.detail2(carId, Long.valueOf(id));
        }
    }

    @ApiOperation(value = "今日新")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "accType", value = "车辆状态", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "一口价", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "priceMax", value = "起拍价格最大值", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "priceMin", value = "起拍价格最小值", required = true, dataType = "String"),})
    @RequestMapping(value = "/todayNewCar", method = RequestMethod.POST)
    public JSONObject todayNewCar(@RequestBody @Valid AuctionCarListDto auctionCarListDto, HttpServletRequest request) {
        AssertUtil.isNotNull(auctionCarListDto.getCurrent(), "current必填");
        JSONObject result = new JSONObject();
        Integer page = (Integer.valueOf(auctionCarListDto.getCurrent()) - 1) * 20;
        TokenPojo currentLoginUser = getCurrentLoginUser(request);


        List<AppAuctionEntity> appAuctionEntities = appAuctionUpMapper.todayNew(page, auctionCarListDto);
        auctionCarListDto.setAuctionState(1);//查询拍卖中数量
        Integer auctionIngCount = appAuctionUpMapper.todayNewCount(page, auctionCarListDto);
        auctionCarListDto.setAuctionState(2);//查询待开始数量
        Integer auctionWaitCount = appAuctionUpMapper.todayNewCount(page, auctionCarListDto);
        JSONArray array = new JSONArray();

        JSONArray resultArray = appAuctionService.auction2Dto(array, appAuctionEntities, currentLoginUser != null ? currentLoginUser.getAppUserEntity().getId().longValue() : null);

        result.put("auctionIngCount", auctionIngCount);
        result.put("auctionWaitCount", auctionWaitCount);
        if (resultArray.size() > 0) {
            List<Object> collect = resultArray.stream().collect(Collectors.toList());
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", collect);

            return result;
        } else {
            result.put("code", 200);
            result.put("msg", "无结果");
            result.put("date", null);
            return result;
        }
    }


    @ApiOperation(value = "添加或取消收藏")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车Id", required = true, dataType = "Long"),})
    @RequestMapping(value = "/collectHandle", method = RequestMethod.GET)
    public JSONObject collectHandle(Long carId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionService.collectHandle(result, id, carId);

    }


    @ApiOperation(value = "车辆列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "auctionCarListDto", value = "查询列表对象", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "accType", value = "车辆状态", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "是否是一口价", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "priceMax", value = "起拍价格最大值", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "priceMin", value = "起拍价格最小值", required = true, dataType = "String"),})
    @RequestMapping(value = "/getAuctionList", method = RequestMethod.POST)
    public JSONObject getAuctionList(@RequestBody @Valid AuctionCarListDto auctionCarListDto, HttpServletRequest request) {
        auctionCarListDto.setPageSize(size);
        Integer current = auctionCarListDto.getCurrent();
        com.cheji.web.util.Page page = new com.cheji.web.util.Page();
        page.setCurrentPage(current == null ? 1 : current);
        page.setShowCount(20);
        auctionCarListDto.setPage(page);
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.isNull(currentLoginUser)) {
            return appAuctionService.getAuctionList(auctionCarListDto, null);
        } else {
            return appAuctionService.getAuctionList(auctionCarListDto, currentLoginUser.getAppUserEntity().getId().longValue());
        }
    }

    @ApiOperation(value = "我的卖车列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "state", value = "状态码1新订单,2拍卖中,3已拍,4过户", required = true, dataType = "String"),})
    @RequestMapping(value = "/getMyAuctionList", method = RequestMethod.GET)
    public JSONObject getMyAuctionList(HttpServletRequest request, @RequestParam Integer current, Integer state) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Page<AppAuctionEntity> page = new Page<>(current, size);
        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionService.getMyAuctionList(id, page, state);
    }

    @ApiOperation(value = "我的卖车列表统计数字，新订单数量，拍卖中数量，已拍卖数量，已过户数量")
    @RequestMapping(value = "/countMyAuction", method = RequestMethod.GET)
    public JSONObject countMyAuction(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionService.countMyAuction(id);
    }

    @ApiOperation(value = "关注列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),})
    @RequestMapping(value = "/collectList", method = RequestMethod.GET)
    public JSONObject collectList(@RequestParam(required = false) Integer current, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Page page = new Page<>(current, size);
        return appAuctionCollectService.collectList(currentLoginUser.getAppUserEntity().getId(), page);
    }

    @ApiOperation(value = "竞买车列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),})
    @RequestMapping(value = "/bidList", method = RequestMethod.GET)
    public JSONObject bidList(@RequestParam(required = false) Integer current, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Page page = new Page<>(current, size);
        return appAuctionCollectService.bidList(page, currentLoginUser.getAppUserEntity().getId().longValue());
    }

    @ApiOperation(value = "待过户")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),})
    @RequestMapping(value = "/waitTransfer", method = RequestMethod.GET)
    public JSONObject waitTransfer(@RequestParam(required = false) Integer current, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Page page = new Page<>(current, size);
        return appAuctionCollectService.waitTransfer(page, currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "已过户")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),})
    @RequestMapping(value = "/transfered", method = RequestMethod.GET)
    public JSONObject transfered(@RequestParam(required = false) Integer current, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Page page = new Page<>(current, size);
        return appAuctionCollectService.transfered(page, currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "城市")
    @RequestMapping(value = "/getCity", method = RequestMethod.GET)
    public JSONObject getCity() {
        return appAuctionService.getCity();
    }

    @ApiOperation(value = "搜索")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "address", value = "地址", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "current", value = "页数", required = true, dataType = "Integer"),})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JSONObject search(@RequestParam(required = false) String address, @RequestParam(required = false) String brand, @RequestParam(required = false) Integer current, HttpServletRequest request) {
        Integer current1 = (current - 1) * size;
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.isNull(currentLoginUser)) {
            return appAuctionService.search(address, brand, current1, size, null);
        } else {
            return appAuctionService.search(address, brand, current1, size, currentLoginUser.getAppUserEntity().getId().longValue());
        }
    }

    @ApiOperation(value = "获取总数")
    @RequestMapping(value = "/getCarCount", method = RequestMethod.GET)
    public JSONObject getCarCount(HttpServletRequest request) {
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (Objects.isNull(currentLoginUser)) {
            return appAuctionService.getCarCount(null);
        }
        return appAuctionService.getCarCount(currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "获取二手车在架数量")
    @RequestMapping(value = "/getOldCarCount", method = RequestMethod.GET)
    public JSONObject getOldCarCount(HttpServletRequest request) {
        AuctionCarListDto dto = new AuctionCarListDto();
        dto.setAuctionState(1); //查询拍卖中数量
        Integer oldCarNum = appAuctionMapper.countOldCarNum(dto);
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "查询成功!");
        result.put("data", oldCarNum);
        return result;
    }

}