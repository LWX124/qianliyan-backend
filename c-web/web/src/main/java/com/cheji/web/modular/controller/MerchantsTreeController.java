package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.ListMessage;
import com.cheji.web.modular.cwork.MerchantsComment;
import com.cheji.web.modular.cwork.MerchantsDetails;
import com.cheji.web.modular.cwork.StoreDisplayDto;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.mapper.AppCarBrandEntityMapper;
import com.cheji.web.modular.mapper.AppUpMerchantsMapper;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/merchantsTree")
public class MerchantsTreeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MerchantsTreeController.class);

    @Resource
    private BUserService bUserService;

    @Resource
    private UserService userService;

    @Resource
    private IndentService indentService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppCarBrandEntityMapper appCarBrandEntityMapper;

    @Resource
    private MerchantsCommentsTreeService merchantsCommentsTreeService;

    @Resource
    private LableDetailsReviewTreeService lableDetailsReviewTreeService;

    @Resource
    private CleanIndetService cleanIndetService;

    @Resource
    private MerchantsInfoBannerService merchantsInfoBannerService;

    @Resource
    private AppUpMerchantsMapper appUpMerchantsMapper;



    /**
     * 查看所有的商户信息.,根据用户所在城市来查
     *
     * @return
     */
    @ApiOperation(value = "商户信息根据城市查找")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "商户城市", required = true, dataType = "String")
    })
    @RequestMapping(value = "/listall", method = RequestMethod.GET)
    public JSONObject listall(String city, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(city)) {
            city = "028";
        }
        List<ListMessage> listAll = new ArrayList<>();
        if (city.length() < 5) {
            listAll = bUserService.findListAll(Integer.valueOf(city));
        } else {
            listAll = bUserService.findListByCounty(Integer.valueOf(city));
        }
        result.put("code", 200);
        result.put("msg", "成功");
        if (pagesize == 2) {
            listAll = null;
        }
        result.put("data", listAll);
        return result;
    }

    /**
     * 查看商户信息.,修理厂
     *
     * @return
     */
    //根据判断参数来判断是查询什么
    @ApiOperation(value = "筛选修理厂", notes = "根据有无参数筛选查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lable", value = "标签id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "level", value = "等级排序", required = false, dataType = "String")
    })
    @RequestMapping(value = "/garage", method = RequestMethod.GET)
    public JSONObject garage(String city, String brand, String lable, String level, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {

        JSONObject result = new JSONObject();

        List<ListMessage> lisgarage = new ArrayList<>();
        //没有品牌参数和标签参数就是默认城市的修理厂
//        if (brand == null && lable == null && level == null) {
//            lisgarage = bUserService.findLisgarage(Integer.valueOf(city));
//        } else if (brand != null && lable == null && level == null) {
//            //有这个brand就是有品牌的查询
//            lisgarage = bUserService.findListByBrand(Integer.valueOf(city), brand);
//        } else if (brand != null && lable != null && level == null) {
//            //品牌不为空，标签不为空，就是品牌和标签的查询
//            lisgarage = bUserService.findgarageByBrandAndLable(Integer.valueOf(city), brand, lable);
//        } else if (brand != null && lable == null) {
//            //品牌不为空，级别不为空，品牌和级别的查询
//            lisgarage = bUserService.findgarageByBrandAndLevel(Integer.valueOf(city), brand);
//        } else if (brand == null && lable != null && level == null) {
//            //品牌为空，标签不为空,标签的查询
//            lisgarage = bUserService.findgarageBylable(Integer.valueOf(city), lable);
//        } else if (brand == null && lable == null) {
//            //品牌为空，标签为空，就是级别的查询
//            lisgarage = bUserService.findgarageBylevel(Integer.valueOf(city));
//        } else {
//            lisgarage = bUserService.findLisgarage(Integer.valueOf(city));
//        }
        lisgarage = bUserService.findAllGarage(Integer.valueOf(city), brand, lable, level);
        if (lisgarage.isEmpty()) {
            lisgarage = bUserService.findLisgarage(Integer.valueOf(city));
        }

        result.put("code", 200);
        result.put("msg", "成功");
        if (pagesize == 2) {
            lisgarage = null;
        }
        result.put("data", lisgarage);
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

        EntityWrapper<AppCarBrandEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("status", ConsEnum.CAR_BRAND_OK.getCode());
        wrapper.orderBy("initials");
        List<AppCarBrandEntity> appCarBrandEntities = appCarBrandEntityMapper.selectList(wrapper);
        stringRedisTemplate.opsForValue().set(RedisConstant.SYS_ALL_BRAND, JSONObject.toJSONString(appCarBrandEntities));
        JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(appCarBrandEntities));

        result.put("code", 200);
        result.put("data", array);
        return result;
    }

    /**
     * 筛选4s店和专修店
     *
     * @return
     */
    @ApiOperation(value = "筛选4s店和专修店", notes = "4S店的查询，根据有无参数来判断")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lable", value = "标签id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "level", value = "等级排序", required = false, dataType = "String")
    })
    @RequestMapping(value = "/foursStoresBrand", method = RequestMethod.GET)
    public JSONObject foursStoresBrand(String city, String brand, String lable, String level, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {

        //logger.error("###  筛选4s店和专修店开始时间##  date={}", new Date());
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(city)) {
            city = "028";
        }

        List<ListMessage> listByStores = new ArrayList<>();
        //没有品牌参数和标签参数就是默认城市的4s店
        if (brand == null && lable == null && level == null) {
            listByStores = bUserService.findListByStores(Integer.valueOf(city));
        } else if (brand != null && lable == null && level == null) {
            //有这个brand就是有品牌的查询
            listByStores = bUserService.findListByfoursStoresBrand(Integer.valueOf(city), brand);
        } else if (brand != null && lable != null && level == null) {
            //品牌不为空，标签不为空，就是品牌和标签的查询
            listByStores = bUserService.findStoresByBrandAndLable(Integer.valueOf(city), brand, lable);
        } else if (brand != null && lable == null) {
            //品牌不为空，级别不为空，品牌和级别的查询
            listByStores = bUserService.findStoresByBrandAndLevel(Integer.valueOf(city), brand);
        } else if (brand == null && lable != null && level == null) {
            //品牌为空，标签不为空,标签的查询
            listByStores = bUserService.findStoresBylable(Integer.valueOf(city), lable);
        } else if (brand == null && lable == null) {
            //品牌为空，标签为空，就是级别的查询
            listByStores = bUserService.findStoresBylevel(Integer.valueOf(city));
        } else {
            listByStores = bUserService.findListByStores(Integer.valueOf(city));
        }
        result.put("code", 200);
        result.put("msg", "成功");
        if (pagesize == 2) {
            listByStores = null;
        }
        result.put("data", listByStores);
        //logger.error("###  筛选4s店和专修店结束时间##  date={}", new Date());
        return result;
    }


    /*商户详情页面接口*/
    @ApiOperation(value = "商户详情页面数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "merchantsCode", value = "商户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/merchantsDetails", method = RequestMethod.GET)
    public JSONObject merchantsDetails(String merchantsCode, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        if (StringUtils.isEmpty(merchantsCode)) {
            result.put("code", 401);
            result.put("msg", "商户id不能为空！");
            return result;
        }

        //传个id过来，查询拿到数据，返回对应商户的详细数据
        MerchantsDetails merchantsDetails = new MerchantsDetails();
        if (merchantsCode.endsWith("Z")) {
            merchantsDetails = bUserService.findZDetailts(merchantsCode);
            merchantsDetails.setIsCanComment("N");
        } else {
            merchantsDetails = bUserService.getMerchantsDetails(merchantsCode);
            if (merchantsDetails == null) {
                result.put("code", 401);
                result.put("msg", "商户id有误！");
                return result;
            }

            //今日访客，每个商户对应一个key，value是用户id
            double currentTime = new Long(new Date().getTime()).doubleValue();
            Date date = new Date();
            String string = date.toString();
            stringRedisTemplate.opsForZSet().add(RedisConstant.MERCHANTS_VISIT_COUNT + merchantsCode, string, currentTime);
            //判断size就是访问量

            //根据用户id查询订单list
            TokenPojo currentLoginUser = getCurrentLoginUser(request);
            if (currentLoginUser == null) {
                merchantsDetails.setIsCanComment("N");
            } else {
                Integer id = currentLoginUser.getAppUserEntity().getId();
                EntityWrapper<IndentEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("user_id", id)
                        .eq("user_b_id", merchantsCode);
                //查询订单
                List<IndentEntity> indentEntities = indentService.selectList(wrapper);
                //判断list是否为空
                if (indentEntities == null || indentEntities.size() == 0) {
                    merchantsDetails.setIsCanComment("N");
                } else {
                    //根据id查询到评论数据和订单数据
                    List<MerchantsCommentsTreeEntity> commentsList = merchantsCommentsTreeService.findListByUseridAndMer(merchantsCode, id);
                    if (indentEntities.size() > commentsList.size()) {
                        merchantsDetails.setIsCanComment("Y");
                    } else {
                        merchantsDetails.setIsCanComment("N");
                    }
                }
            }
        }

        EntityWrapper<BUserEntity> wra = new EntityWrapper<>();
        wra.eq("up_id", merchantsCode);
        BUserEntity bUserEntity = bUserService.selectOne(wra);
        if (bUserEntity != null){
            result.put("userUpId", bUserEntity.getUsername());
        }else{
            result.put("userUpId", null);
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", merchantsDetails);
        return result;
    }


    //商户评论接口
    @ApiOperation(value = "商户评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "merchantsCode", value = "商户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/merchantsComment", method = RequestMethod.GET)
    public JSONObject merchantsComment(String merchantsCode, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        if (!merchantsCode.endsWith("Z")) {
            //根据商户id查询到对应的评论

            List<MerchantsComment> merchantsCommentByid = bUserService.findMerchantsCommentByid(merchantsCode, pagesize, null, null);
            String s = stringRedisTemplate.opsForValue().get(RedisConstant.MERCHANTS_COMMENT_COUNT + merchantsCode + pagesize);
            if (StringUtils.isEmpty(s)) {
                stringRedisTemplate.opsForValue().set(RedisConstant.MERCHANTS_COMMENT_COUNT + merchantsCode + pagesize, "0");
            } else {
                //stringRedisTemplate.boundValueOps("test").increment(1);//val +1
                stringRedisTemplate.boundValueOps(RedisConstant.MERCHANTS_COMMENT_COUNT + merchantsCode + pagesize).increment(1);
            }
            result.put("data", merchantsCommentByid);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    //空订单的情况下，推荐的4s店的列表
    @ApiOperation(value = "推荐4s店")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public JSONObject recommend(String city) {
        JSONObject result = new JSONObject();
        //先从redis里面拿
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.RECOMMEND_MERCHANTS);
        List<ListMessage> recommendMer = new ArrayList<>();
        if (StringUtils.isEmpty(s)) {
            recommendMer = bUserService.findRecommendMer(Integer.valueOf(city));
            stringRedisTemplate.opsForValue().set(RedisConstant.RECOMMEND_MERCHANTS, JSONArray.toJSONString(recommendMer), 60 * 5, TimeUnit.SECONDS);
        } else {
            recommendMer = JSONArray.parseArray(s, ListMessage.class);
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", recommendMer);
        return result;
    }


    @ApiOperation(value = "首页推荐商户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/recommendMerchants", method = RequestMethod.GET)
    public JSONObject recommendMerchants(String city, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        //先从redis里面拿
        if (StringUtils.isEmpty(city)) {
            city = "028";
        }
        List<ListMessage> recommendMer = bUserService.findRecommendFirstMer(city, pagesize);
//        String s = stringRedisTemplate.opsForValue().get(RedisConstant.RECOMMEND_HOME_PAGE_MERCHANTS);
//        List<ListMessage> recommendMer = new ArrayList<>();
//        if (StringUtils.isEmpty(s)) {
//            recommendMer = bUserService.findRecommendFirstMer(Integer.valueOf(city),pagesize);
//            stringRedisTemplate.opsForValue().set(RedisConstant.RECOMMEND_HOME_PAGE_MERCHANTS, JSONArray.toJSONString(recommendMer), 60 * 5, TimeUnit.SECONDS);
//
//        } else {
//            recommendMer = JSONArray.parseArray(s, ListMessage.class);
//        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", recommendMer);
        return result;
    }


    @ApiOperation(value = "商户详情标签比例")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "标签id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")

    })
    @RequestMapping(value = "/lableDetailsList", method = RequestMethod.GET)
    public JSONObject lableDetailsList(String id, HttpServletRequest request) {
        //根据标签id拿到具体数据
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        EntityWrapper<LableDetailsReviewTreeEntity> lableWrapper = new EntityWrapper<>();
        lableWrapper.eq("parent_code", id);
        List<LableDetailsReviewTreeEntity> lableDetailsReview = lableDetailsReviewTreeService.selectList(lableWrapper);
        if (lableDetailsReview.isEmpty()) {
            result.put("code", 404);
            result.put("msg", "未找到数据");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", lableDetailsReview);
        return result;
    }


    @ApiOperation(value = "店铺展示")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")

    })
    @RequestMapping(value = "/sotreDisplay", method = RequestMethod.GET)
    public JSONObject sotreDisplay(String userBId) {
        //判断用户是否登录
        //根据标签id拿到具体数据
        JSONObject result = new JSONObject();
        //判断登陆

        StoreDisplayDto storeDispla = bUserService.findstoreDisplay(userBId);
        //查询到订单总数
        Integer indetCount = cleanIndetService.findIndentCountNumber(userBId);

        storeDispla.setAllIndent(indetCount);

        //查询轮播图
        EntityWrapper<MerchantsInfoBannerEntity> infoBannerEntityWrapper = new EntityWrapper<>();
        infoBannerEntityWrapper.eq("user_b_id", userBId);
        List<MerchantsInfoBannerEntity> merchantsInfoBannerEntities = merchantsInfoBannerService.selectList(infoBannerEntityWrapper);
        if (merchantsInfoBannerEntities != null) {
            storeDispla.setInfoBannerList(merchantsInfoBannerEntities);
        }
        result.put("code", 200);
        result.put("data", storeDispla);
        return result;
    }

    @ApiOperation(value = "绑定员工")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phoneOn", value = "员工电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "merName", value = "商户名", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/bindEmployee", method = RequestMethod.GET)
    public JSONObject bindEmployee(String phoneOn, String merName, String userBId, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        Integer id = currentLoginUser.getAppUserEntity().getId();
        if (id != 102 && id != 56) {
            result.put("code", 401);
            result.put("msg", "没有权限");
            return result;
        }
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        final String regex = "^1[0-9]{10}$";
        if (StringUtils.isEmpty(phoneOn) || !phoneOn.matches(regex)) {
            result.put("code", 401);
            result.put("msg", "手机号格式错误");
            return result;
        }

        AppUpMerchantsEntity appUpMerchantsEntity = appUpMerchantsMapper.selectById(userBId);
        if(StringUtils.isNotBlank(merName) && !merName.equals(appUpMerchantsEntity.getName())){
            result.put("code", 402);
            result.put("msg", "店铺错误,请联系管理员");
            return result;
        }
        EntityWrapper<BUserEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("username",phoneOn);
        BUserEntity bUserEntity = bUserService.selectOne(wrapper);

        if(appUpMerchantsEntity != null){
            if(bUserEntity != null){
               if(bUserEntity.getUpId() != null && StringUtils.isNotBlank(bUserEntity.getUpId())){
                   result.put("code", 403);
                   result.put("msg", "该账号已绑定,请勿重复绑定");
                   return result;
               }
                bUserEntity.setUpId(userBId);
                bUserService.updateById(bUserEntity);
            } else {
                result.put("code", 402);
                result.put("msg", "该用户不存在,请重新输入!");
                return result;
            }
        } else {
            result.put("code", 402);
            result.put("msg", "店铺ID错误");
            return result;
        }

        result.put("code", 200);
        return result;
    }


}
