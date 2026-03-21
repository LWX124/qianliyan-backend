package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.ConsEnum;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.*;
import com.cheji.b.modular.mapper.AppUpMerchantsMapper;
import com.cheji.b.modular.mapper.AppUserMapper;
import com.cheji.b.modular.mapper.AppWxpayOrderMapper;
import com.cheji.b.modular.service.*;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/merchantsTree")
public class MerchantsTreeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MerchantsTreeController.class);

    @Resource
    public AppUserMapper appUserMapper;

    @Resource
    private AppWxpayOrderMapper appWxpayOrderMapper;

    @Resource
    public UserService userService;

    @Resource
    private MerchantsServicerService merchantsServicerService;

    @Resource
    private InsuranceMerchantsService insuranceMerchantsService;

    @Resource
    private MerchantsBrandService merchantsBrandService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "首页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public JSONObject homePage(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到商户code
        Integer id = currentLoginUser.getAppUserEntity().getId();

        //根据到商户code查询信息
        HomePageDto homePage = userService.findHomePage(id);
        AppUserEntity appUserEntity = userService.selectById(id);
        String address = appUserEntity.getAddress();
        //地址为空
        if (StringUtils.isEmpty(address)) {
            homePage.setPrompt("请先添加商户所在地址,若无地址信息无法派单");
        } else {
            homePage.setPrompt("");     //前台要求
        }
        //查询是否是否是上线状态
        if (appUserEntity.getParentId() != null) {
            homePage.setIsOnline("1");
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", homePage);
        return result;
    }

    @ApiOperation(value = "评价上半截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public JSONObject evaluation(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到商户code
        Integer id = currentLoginUser.getAppUserEntity().getId();
        EvaluationDto evaluationDto = userService.findEvaluation(id);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", evaluationDto);
        return result;
    }


    @ApiOperation(value = "我的")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    public JSONObject mine(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //logger.error("mine currentLoginUser={}", currentLoginUser);
        //获取用户id
        MineDto mineDto = userService.findMine(id);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", mineDto);
        return result;
    }

    @ApiOperation(value = "服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/serviceDisplay", method = RequestMethod.GET)
    public JSONObject serviceDisplay(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        ServicDisplayDto displayDto = userService.getServiceDisplay(userBId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", displayDto);
        return result;
    }

    @ApiOperation(value = "门店")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/stores", method = RequestMethod.GET)
    public JSONObject stores(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到商户id
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        StoresDto storesDto = userService.findStroeMessage(userBId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", storesDto);

        return result;
    }

    //修改商户名称,门店电话,门店地址,门店分类,门店公告,营业开始时间，营业结束时间
    @ApiOperation(value = "修改门店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "merchantsName", value = "商户名字", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "merchantsPhone", value = "门店电话", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "门店分类", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "announcement", value = "门店公告", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessStart", value = "营业开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessEnd", value = "营业结束时间", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateStores", method = RequestMethod.POST)
    public JSONObject updateStores(@RequestBody AppUserEntity appUserEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        //获取到商户id
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

//        AppUserEntity appUserEntity1 = appUserMapper.selectUser(userBId);
//        logger.info("appUserEntity1.getMerchantsName()={};;;;;appUserEntity.getMerchantsName()={}", appUserEntity1.getMerchantsName(), appUserEntity.getMerchantsName());
//        if (!appUserEntity1.getMerchantsName().equals(appUserEntity.getMerchantsName())) {
//            logger.info("####开始修改别名###");
//
//            //如果名字不同说明有修改 需要到环信修改名字
//            JSONObject object = huanXinService.updateHxNickName(appUserEntity1.getHuanxinUserName(), appUserEntity1.getMerchantsName(), false);
//            logger.info("### object  修改别名 返回结果#### {}", object);
//        }

        appUserEntity.setId(userBId);
        //修改名称
        if (appUserEntity.getMerchantsName() != null) {
            //修改服务名称
            EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("user_b_id", userBId)
                    .eq("tree_level", 0);
            List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(wrapper);
            for (LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity : list) {
                lableDetailsReviewTreeEntity.setLableName(appUserEntity.getMerchantsName());
                lableDetailsReviewTreeService.updateById(lableDetailsReviewTreeEntity);
            }
        }
        userService.updateById(appUserEntity);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    //添加修改服务顾问
    @ApiOperation(value = "修改门店地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "门店地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "province", value = "省编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "city", value = "市编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "county", value = "区县编号", required = true, dataType = "String")

    })
    @RequestMapping(value = "/updateAddress", method = RequestMethod.GET)
    public JSONObject updateAddress(HttpServletRequest request, String address, String lng, String lat, String province, String city, String county) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        AppUserEntity appUserEntity = userService.selectById(userBId);
        appUserEntity.setAddress(address);
        BigDecimal bd = new BigDecimal(lng);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd = bd.setScale(10, BigDecimal.ROUND_HALF_UP);
        appUserEntity.setLng(bd);
        BigDecimal ad = new BigDecimal(lat);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        ad = ad.setScale(10, BigDecimal.ROUND_HALF_UP);
        appUserEntity.setLat(ad);

        appUserEntity.setProvince(province);
        appUserEntity.setCity(city);
        appUserEntity.setCounty(county);
        userService.updateById(appUserEntity);

        //修改geo中位置
        //先判断是否添加到geo中
        AppUserEntity appUser = userService.selectById(userBId);
        String addgeo = appUser.getAddgeo();
        if (addgeo.equals("1")) {
            //已经添加到geo中
            //根据redis 移除数据再重新添加
//            String redis = appUser.getRedis();
//            stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO,redis);
            String redis = appUser.getRedis();
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
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + brandId, redis);
            } else if (type == 2) {
                //修理厂
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 2, redis);
            } else {
                //专修店
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 3, redis);
            }
            //查询品牌数据
            List<String> brandList1 = userService.findBrand(userBId);
            //重新添加
            JSONObject in = new JSONObject();
            in.put("id", appUser.getId());
            in.put("merchantsName", appUser.getMerchantsName());
            in.put("lat", lat);
            in.put("lng", lng);
            in.put("address", appUser.getAddress());
            in.put("type", appUser.getType());
            in.put("brandList", brandList1);
            if (appUser.getType() == 1) {
                String s = brandList1.get(0);

                //4s店 拿到品牌数据
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + s, new Point(Double.parseDouble(lng), Double.parseDouble(lat)), in.toJSONString());
            } else if (appUser.getType() == 2) {

                //修理厂
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + 2, new Point(Double.parseDouble(lng), Double.parseDouble(lat)), in.toJSONString());
            } else {

                //专修店
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + 3, new Point(Double.parseDouble(lng), Double.parseDouble(lat)), in.toJSONString());
            }
            appUser.setRedis(in.toJSONString());


//            //修改救援数据,如果不为空修改数据
//            if (StringUtils.isNotEmpty(appUser.getRescueRedis())){
//                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_RESCUE_GEO,appUser.getRescueRedis());
//                //重新添加
//                JSONObject js = new JSONObject();
//                js.put("id", appUser.getId());
//                js.put("merchantsName", appUser.getMerchantsName());
//                js.put("lat", lat);
//                js.put("lng", lng);
//                js.put("address", appUser.getAddress());
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_RESCUE_GEO, new Point(Double.parseDouble(lng), Double.parseDouble(lat)), in.toJSONString());
//                appUser.setRescueRedis(js.toJSONString());
//            }

//            //年检数据如果不为空就修改
//            if (StringUtils.isNotEmpty(appUser.getYearcheckRedis())) {
//                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_YEAR_CHECK_GEO,appUser.getYearcheckRedis());
//                JSONObject js = new JSONObject();
//                js.put("id", appUser.getId());
//                js.put("merchantsName", appUser.getMerchantsName());
//                js.put("lat", lat);
//                js.put("lng", lng);
//                js.put("address", appUser.getAddress());
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_YEAR_CHECK_GEO,new Point(Double.parseDouble(lng), Double.parseDouble(lat)),in.toJSONString());
//                appUser.setYearcheckRedis(js.toJSONString());
//            }


            userService.updateById(appUser);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    //添加修改服务顾问
    @ApiOperation(value = "添加修改服务顾问" +
            "顾问id:id Integer," +
            "顾问名字:name String," +
            "电话号码:phone String," +
            "图片地址:imgUrl String")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "servicerList", value = "服务顾问集合", required = true, dataType = "String")
    })
    @RequestMapping(value = "/servicerList", method = RequestMethod.POST)
    public JSONObject servicerList(HttpServletRequest request, @RequestBody ServicersListDto servicersList) {
        JSONObject result = new JSONObject();
        //Integer userBId = 1;
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        List<MerchantsServicerEntity> servicerList = servicersList.getServicerList();
        for (MerchantsServicerEntity servicerEntity : servicerList) {
            Long id = servicerEntity.getId();
            //id为空就是新增
            if (id == null) {
                servicerEntity.setUserBId(userBId);
                servicerEntity.setCreatTime(new Date());
                servicerEntity.setUpdateTime(new Date());
                merchantsServicerService.insert(servicerEntity);
            } else {
                //修改数据
                servicerEntity.setUserBId(userBId);
                merchantsServicerService.updateById(servicerEntity);
            }
        }
        EntityWrapper<MerchantsServicerEntity> merchantsWrapper = new EntityWrapper<>();
        merchantsWrapper.eq("user_b_id", userBId);
        List<MerchantsServicerEntity> merchantsServicerEntities = merchantsServicerService.selectList(merchantsWrapper);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", merchantsServicerEntities);
        return result;
    }


    //删除服务顾问
    @ApiOperation(value = "删除服务顾问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "服务顾问id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteServiers", method = RequestMethod.GET)
    public JSONObject deleteServiers(HttpServletRequest request, String id) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        merchantsServicerService.deleteById(Long.valueOf(id));
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    //添加修改门店保险情况
    @ApiOperation(value = "添加修改门店保险")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "insuranceDto", value = "选中的保险id", required = true, dataType = "ArrayList")
    })
    @RequestMapping(value = "/updateInsurance", method = RequestMethod.POST)
    public JSONObject updateInsurance(HttpServletRequest request, @RequestBody InsuranceDto insuranceDto) {
        //Integer userBId = 1;
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer[] id = insuranceDto.getId();
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        insuranceMerchantsService.update(userBId, id);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    @ApiOperation(value = "添加修改门店品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "选中的品牌id", required = true, dataType = "ArrayList")
    })
    @RequestMapping(value = "/updateBrand", method = RequestMethod.POST)
    public JSONObject updateBrand(HttpServletRequest request, @RequestBody InsuranceDto insuranceDto) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        Integer[] id = insuranceDto.getId();
        merchantsBrandService.updateBrand(userBId, id);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    @ApiOperation(value = "查询所有品牌")
    @RequestMapping(value = "/allBrand", method = RequestMethod.GET)
    public JSONObject foursStoresBrand() {
        JSONObject result = new JSONObject();

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.SYS_ALL_BRAND);
        if (StringUtils.isNotEmpty(s)) {
            result.put("code", 200);
            result.put("data", JSONArray.parseArray(s));
            return result;
        }

        EntityWrapper<CarBrandEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("status", ConsEnum.CAR_BRAND_OK.getCode());
        wrapper.orderBy("initials");
        List<CarBrandEntity> carBrandEntities = carBrandService.selectList(wrapper);
        stringRedisTemplate.opsForValue().set(RedisConstant.SYS_ALL_BRAND, JSONObject.toJSONString(carBrandEntities));
        JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(carBrandEntities));

        result.put("code", 200);
        result.put("data", array);
        return result;
    }


    //查询那些数据没得
    @ApiOperation(value = "查询所有必填信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/allMessage", method = RequestMethod.GET)
    public JSONObject allMessage(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //通过商户id查询到缺少的信息
        String mess = userService.findAllMessage(userBId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", mess);
        return result;
    }

    @ApiOperation(value = "提示充值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/targetPrice", method = RequestMethod.GET)
    public JSONObject targetPrice(HttpServletRequest request) {
        //提示充值了的商户，余额小于100就充值
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //查询是否充值过
        EntityWrapper<AppWxpayOrderEntity> orderEntityWrapper = new EntityWrapper<>();
        orderEntityWrapper.eq("user_id", userBId)
                .eq("type", 2);
        List<AppWxpayOrderEntity> appWxpayOrderEntities = appWxpayOrderMapper.selectList(orderEntityWrapper);
        //曾经充了钱
        if (!appWxpayOrderEntities.isEmpty()) {
            //判断账户余额
            AppUserEntity appUserEntity = appUserMapper.selectUser(userBId);
            BigDecimal balance = appUserEntity.getBalance();
            //获取到余额
            //余额小于100
            if (balance.compareTo(new BigDecimal(100)) == -1) {
                //提示充值
                result.put("code", 200);
                result.put("msg", "您的信息费不足，请及时充值");
                result.put("data", 1);
                return result;
            }
        }
        result.put("code", 200);
        result.put("msg", null);
        result.put("data", 2);
        return result;
    }


    @ApiOperation(value = "时实更新位置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "经度", value = "lng", required = true, dataType = "Bigdecimal"),
            @ApiImplicitParam(paramType = "query", name = "纬度", value = "lat", required = true, dataType = "Bigdecimal")
    })
    @RequestMapping(value = "/updateLocatioin", method = RequestMethod.GET)
    public JSONObject updateLocatioin(HttpServletRequest request, BigDecimal lng, BigDecimal lat) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //更新位置
        //查询信息
        AppUserEntity appUserEntity = userService.selectById(userBId);
        List<String> brand = userService.findBrand(userBId);
        JSONObject in = new JSONObject();
        in.put("id", appUserEntity.getId());
        in.put("merchantsName", appUserEntity.getMerchantsName());
        in.put("lat", lat);
        in.put("lng", lng);
        in.put("address", appUserEntity.getAddress());
        in.put("type", appUserEntity.getType());
        in.put("brandList", brand);

        //先删再加
        if (appUserEntity.getRealLocation() != null) {
            stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO_REA + userBId, appUserEntity.getRealLocation());
        }

        //添加
        stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO_REA + userBId, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());

        appUserEntity.setRealLocation(in.toJSONString());




//        //判断有无geo数据
//        String redis = appUserEntity.getRedis();
//        if (StringUtils.isNotEmpty(redis)) {
//            //不为空就处理
//            //先删除再添加
//            JSONObject object = JSONObject.parseObject(redis);
//            int type = object.getIntValue("type");
//            JSONArray brandList = object.getJSONArray("brandList");
//            String brandId = "";
//            if (!brandList.isEmpty()) {
//                for (int i = 0; i < brandList.size(); i++) {
//                    brandId = (String) brandList.get(0);
//                }
//            }
//            if (type == 1) {
//                String s = brand.get(0);
//                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + brandId, redis);
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + s, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
//            }else if (type == 2) {
//                //修理厂
//                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 2, redis);
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + 2, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
//
//            } else {
//                //专修店
//                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_GEO + 3, redis);
//                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_GEO + 3, new Point(lng.doubleValue(), lat.doubleValue()), in.toJSONString());
//            }
//
//            appUserEntity.setRedis(in.toJSONString());
//        }

        appUserMapper.updateById(appUserEntity);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


}
