package com.cheji.web.modular.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.dto.AuctionCarListDto;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private AppAuctionCollectService appAuctionCollectService;

    @Resource
    private AppAuctionUpMapper appAuctionUpMapper;

    final Integer size = 20;


    @Autowired
    private WxMaService wxMaService;


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
            accessToken = wxMaService.getAccessToken();
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "days", value = "拍卖天数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "车辆所有人电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "insuredAmount", value = "保险金额", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "productDate", value = "出厂日期", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "price", value = "起拍价", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "是否是一口价,0否,1是", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plateNo", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "所有人名字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌型号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "auctionType", value = "拍卖类型", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "displacement", value = "排量", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "transmission", value = "传动类型,1,自动,0,手动", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fuel", value = "燃油类型,0,汽油,1,柴油,2电,3混动", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "registerDate", value = "注册日期", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "issueDate", value = "发证日期", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "annualCheck", value = "年检期限", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "compulsoryInsuranceVilidity", value = "交强险有效期", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "registeredResidence", value = "户籍地", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentType", value = "车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.无记录", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sunroof", value = "是否有天窗", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "frameNo", value = "车架号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "useNature", value = "使用性质,1运营,0非运营", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "frameNoDamagedCondition", value = "车架号是否受损,0否,1是", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "engine", value = "发动机", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "damageReason", value = "车损原因", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mileage", value = "行驶里程", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "key", value = "钥匙", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "purchaseTax", value = "购置税", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plateNumber", value = "车牌数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "registrationCertificate", value = "登记证书", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createTime", value = "创建时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "updateTime", value = "修改时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "overhaul", value = "是否拆检,0否,1是", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "announcements", value = "注意事项", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carState", value = "车辆拍卖状态码:0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "upState", value = "上架状态:0,未上架,1,已上架", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "otherField", value = "扩展字段", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "color", value = "车身颜色", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mortgage", value = "抵押按揭", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "second", value = "二次事故", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "change", value = "是否改装", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "transfer", value = "过户次数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "owner", value = "所有人,个人,公司", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "duty", value = "事故责任", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "dutyBook", value = "责任书", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "drivingLicense", value = "行驶证", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "licence", value = "驾照", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "insurance", value = "保险公司", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "repair", value = "维修预估", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "insRemark", value = "保险备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imgList", value = "车辆图片集合", required = true, dataType = "String[]"),})
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


    @ApiOperation(value = "提交过户审核")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "车辆id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "imgList", value = "图片集合", required = true, dataType = "String[]"),})
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
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "页数", required = true, dataType = "Integer"), @ApiImplicitParam(paramType = "query", name = "city", value = "城市", required = true, dataType = "String")})
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "accType", value = "车辆状态", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "一口价", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "priceMax", value = "起拍价格最大值", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "priceMin", value = "起拍价格最小值", required = true, dataType = "String"),
    })
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "auctionCarListDto", value = "查询列表对象", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accType", value = "车辆状态", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fixedPrice", value = "是否是一口价", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parkingPlace", value = "停放地", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "priceMax", value = "起拍价格最大值", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "priceMin", value = "起拍价格最小值", required = true, dataType = "String"),
    })
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
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "状态码1新订单,2拍卖中,3已拍,4过户", required = true, dataType = "String"),})
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

}