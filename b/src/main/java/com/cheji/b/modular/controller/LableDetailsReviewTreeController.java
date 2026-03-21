package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.constant.RedisConstant;
import com.cheji.b.modular.domain.*;
import com.cheji.b.modular.dto.DetailsListDto;
import com.cheji.b.modular.dto.LableDto;
import com.cheji.b.modular.dto.ProjectDetailsDto;
import com.cheji.b.modular.excep.CusException;
import com.cheji.b.modular.service.*;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/lableDetailsReviewTree")
public class LableDetailsReviewTreeController extends BaseController {
    @Autowired
    public LableDetailsReviewTreeService lableDetailsReviewTreeService;
    @Resource
    private LableService lableService;
    @Resource
    private LableDetailsService lableDetailsService;
    @Resource
    private UserService userService;
    @Resource
    private AppRescueIndentService appRescueIndentService;
    @Resource
    private AppUserBMessageImgService appUserBMessageImgService;
    @Resource
    private AppUserBMessageService appUserBMessageService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //private Logger logger = LoggerFactory.getLogger(LableDetailsReviewTreeController.class);

    //保存申请记录
    @ApiOperation(value = "申请服务项目记录" +
            "DetailsListDto对象参数" +
            "detailsId:id和rebats:数组")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "list", value = "对象", required = true, dataType = "String")
    })
    @RequestMapping(value = "/lableDetails", method = RequestMethod.POST)
    public JSONObject lableDeatils(HttpServletRequest request, @RequestBody LableDto list) throws CusException {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //保存到数据库
        //先保存商户id
        LableDetailsReviewTreeEntity lablemerchants = lableDetailsReviewTreeService.addFirst(currentLoginUser.getAppUserEntity().getId());
        //  lablemerchants.setLableName(currentLoginUser.getAppUserEntity().getMerchantsName());
        //如果为null
        //就是添加或者修改数据，
        Integer lableId = list.getLableId();
        String remake = list.getRemake();
        if (lablemerchants == null) {
            //判断有无服务id，有就是修改，没有就是添加
            //获取到对象下得数据根据商户id来查询
            List<LableDetailsReviewTreeEntity> lablesrvic = lableDetailsReviewTreeService.findserviceById(userBId, lableId);
            if (lablesrvic.isEmpty()) {
                //为空就是添加数据。添加服务和明细
                //先添加服务
                LableDetailsReviewTreeEntity lableser = lableDetailsReviewTreeService.addSecond(userBId, lableId, remake);
                //再添加明细
                DetailsListDto[] listDtos = list.getListDtos();
                for (DetailsListDto listDto : listDtos) {
                    lableDetailsReviewTreeService.addThird(listDto, lableser);
                }
            } else {
                //修改数据
                //根据服务id查询出数据
                List<DetailsListDto> updatelist = lableDetailsReviewTreeService.updatelist(list, userBId, remake);
                if (updatelist.isEmpty()) {
                    result.put("code", 200);
                    result.put("msg", "重新进行审核");
                    return result;
                } else {
                    //添加数据
                    //查询出上一级数据
                    EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_b_id", userBId)
                            .eq("`show`", 1);
                    List<LableDetailsReviewTreeEntity> list1 = lableDetailsReviewTreeService.selectList(wrapper);
                    for (LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity : list1) {
                        for (DetailsListDto detailsListDto : updatelist) {
                            lableDetailsReviewTreeService.addThird(detailsListDto, lableDetailsReviewTreeEntity);
                        }
                    }
                }
            }
        } else {
            //也要保存数据
            LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity = lableDetailsReviewTreeService.addSecond(userBId, lableId, remake);
            DetailsListDto[] listDtos = list.getListDtos();
            for (DetailsListDto listDto : listDtos) {
                lableDetailsReviewTreeService.addThird(listDto, lableDetailsReviewTreeEntity);
            }
        }

        result.put("code", 200);
        result.put("msg", "等待审核");
        return result;
    }

    @ApiOperation(value = "项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/lableList", method = RequestMethod.GET)
    public JSONObject lableList(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer user_b_id = currentLoginUser.getAppUserEntity().getId();
        //根据商户id查询出对应信息
        List<LableEntity> lable = lableService.findLable(user_b_id);
        //查询到已经开通得服务个数
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", user_b_id)
                .eq("`show`", 1)
                .eq("state", 1);
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(wrapper);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", lable);
        result.put("count", list.size());
        return result;
    }


    @ApiOperation(value = "项目情况明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "服务对应id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/projectDetails", method = RequestMethod.GET)
    public JSONObject projectDetails(Integer id, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //根据id查询到对应信息
        //先判断有无数据
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.findserviceById(userBId, id);
        ProjectDetailsDto project = new ProjectDetailsDto();
        //无数据就是一次添加
        if (list.isEmpty()) {
            //正常反数据
            EntityWrapper<LableDetailsEntity> wrapper = new EntityWrapper<>();
            List<LableDetailsEntity> lableDetails = lableDetailsService.selectList(wrapper);
            project.setLableDetailsList(lableDetails);
        } else {
            //有数据就是修改或者重新审核
            //从表中查询数据
            List<LableDetailsEntity> detailsList = lableDetailsReviewTreeService.findProjectDetails(userBId, id);
            //还要加上备注
            //获取到第一条
            for (LableDetailsReviewTreeEntity lableDetailsReviewTreeEntity : list) {
                String reson = lableDetailsReviewTreeEntity.getReson();
                project.setReson(reson);
                project.setLableDetailsList(detailsList);
                project.setRemake(lableDetailsReviewTreeEntity.getRemake());
            }
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", project);
        return result;
    }

    @ApiOperation(value = "下架服务项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "服务对应id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/shelvesService", method = RequestMethod.GET)
    public JSONObject shelvesService(String id, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //根据服务id和token查询到对应服务
        LableDetailsReviewTreeEntity nowProjec = lableDetailsReviewTreeService.findNowProject(id, userBId);
        //判断是否有数据
        if (nowProjec == null) {
            result.put("code", 200);
            result.put("msg", "该服务未开通或已下架");
            return result;
        }
        //修改服务状态
        nowProjec.setState(-1);
        lableDetailsReviewTreeService.updateById(nowProjec);

        //查询到已经开通得服务个数
        EntityWrapper<LableDetailsReviewTreeEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("`show`", 1)
                .eq("state", 1);
        List<LableDetailsReviewTreeEntity> list = lableDetailsReviewTreeService.selectList(wrapper);

//        if (list.size()==1){
//            String ste = "商户服务项目只剩一个了,继续下架将不会被展示。请谨慎操作";
//            result.put("code", 200);
//            result.put("msg", "成功");
//            result.put("data",ste);
//        }
        //开通得服务个数如果小于1个就下架商户
        if (list.size() == 0) {
            AppUserEntity appUserEntity = userService.selectById(userBId);
            appUserEntity.setState(0);
            userService.updateById(appUserEntity);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    @ApiOperation(value = "添加救援,年检,喷漆,代驾等服务" +
            "405:重复提交" +
            "407：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "headImg", value = "头像", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workPlace", value = "工作地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "trailerPlate", value = "拖车车牌", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "introduction", value = "简介", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wrokType", value = "工作内容（1.救援,2.年检,3.喷漆,4.代驾）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "driverYear", value = "驾龄", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "technologyYear", value = "技龄", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "技师电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "基本图片列表 url index", required = true, dataType = "List"),
            @ApiImplicitParam(paramType = "query", name = "trailerUrl", value = "拖车图片列表", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "price", value = "价格/面", required = true, dataType = "Bigdecmial")
    })
    @RequestMapping(value = "/addNewSer", method = RequestMethod.POST)
    public JSONObject addNewSer(@RequestBody JSONObject in, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        EntityWrapper<AppUserBMessageEntity> appUserBMessageWrapper = new EntityWrapper<>();
        appUserBMessageWrapper.eq("user_b_id", userBId)
                .eq("wrok_type", in.getString("wrokType"));
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(appUserBMessageWrapper);
        if (appUserBMessageEntity != null) {
            result.put("code", 405);
            result.put("msg", "请勿重复提交");
            return result;
        }

        if (StringUtils.isEmpty(in.getString("name")) || StringUtils.isEmpty(in.getString("headImg"))
                || StringUtils.isEmpty(in.getString("workPlace")) || StringUtils.isEmpty(in.getString("wrokType"))) {
            result.put("code", 407);
            result.put("msg", "检查参数是否为空");
            return result;
        }
        if (in.getBigDecimal("lng") == null || in.getBigDecimal("lat") == null) {
            result.put("code", 407);
            result.put("msg", "经纬度不能为空");
            return result;
        }


        Integer wrokType = Integer.valueOf(in.getString("wrokType"));
        if (wrokType != 1 && wrokType != 2 && wrokType != 3 && wrokType != 4) {
            result.put("code", 407);
            result.put("msg", "检查工作内容参数");
            return result;
        }

        JSONObject addnewSer = lableDetailsReviewTreeService.addnewSer(in, wrokType, userBId);
        Integer code = addnewSer.getInteger("code");
        if (code != 200) {
            return addnewSer;
        }
        AppUserBMessageEntity appUserBMessage = (AppUserBMessageEntity) addnewSer.get("data");
        AppUserEntity appUser = userService.selectById(userBId);
        //添加1.救援,2.年检,3.喷漆,
        String savemes = null;

        if (wrokType == 1) {
            //添加到救援geo中
            JSONObject place = new JSONObject();
//            place.put("id", appUser.getId());
//            place.put("merchantsName", appUser.getMerchantsName());
//            place.put("lat", appUserBMessage.getLat());
//            place.put("lng", appUserBMessage.getLng());
//            place.put("address", appUserBMessage.getWorkPlace());
//            stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_RESCUE_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), place.toJSONString());
//            appUser.setRescueRedis(place.toJSONString());
            userService.updateById(appUser);
            savemes = appRescueIndentService.addService(userBId, 13);
        } else if (wrokType == 2) {
            savemes = appRescueIndentService.addService(userBId, 15);
//            //添加到 年检 geo中，
//            JSONObject place = new JSONObject();
//            place.put("id", appUser.getId());
//            place.put("merchantsName", appUser.getMerchantsName());
//            place.put("lat", appUserBMessage.getLat());
//            place.put("lng", appUserBMessage.getLng());
//            place.put("address", appUserBMessage.getWorkPlace());
//            stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_YEAR_CHECK_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), place.toJSONString());
//            appUser.setYearcheckRedis(place.toJSONString());
            userService.updateById(appUser);
        } else if (wrokType == 3) {
            //喷漆
            savemes = appRescueIndentService.addService(userBId, 12);
        } else {
            //代驾
            savemes = appRescueIndentService.addService(userBId, 19);
            //添加到redis中
//            JSONObject subDirving = new JSONObject();
//            subDirving.put("id", appUserBMessage.getId());//技师id
//            subDirving.put("merchantsName", appUserBMessage.getName());
//            subDirving.put("lat", appUserBMessage.getLat());
//            subDirving.put("lng", appUserBMessage.getLng());
//            subDirving.put("address",appUserBMessage.getWorkPlace());
//            stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), subDirving.toJSONString());
//            appUser.setSubstituteRedis(subDirving.toJSONString());
//            userService.updateById(appUser);
        }

        result.put("code", 200);
        result.put("msg", savemes);
        result.put("data", appUserBMessage);
        return result;
    }

    @ApiOperation(value = "修改服务内容" +
            "407参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "headImg", value = "头像", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workPlace", value = "工作地址", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(paramType = "query", name = "trailerPlate", value = "拖车车牌", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "introduction", value = "简介", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "driverYear", value = "驾龄", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "technologyYear", value = "技龄", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "技师电话", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessType", value = "营业状态1.开始，2.未营业", required = false, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "基本图片列表 index,url", required = false, dataType = "List"),
            @ApiImplicitParam(paramType = "query", name = "trailerUrl", value = "拖车图片列表", required = false, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "price", value = "价格/面", required = false, dataType = "Bigdecmial")
    })
    @RequestMapping(value = "/updataWorkPlace", method = RequestMethod.POST)
    public JSONObject updataWorkPlace(@RequestBody JSONObject in, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        if (in.isEmpty()) {
            result.put("code", 407);
            result.put("msg", "js参数为空");
            return result;
        }

        Integer id = in.getInteger("id");
        if (id == null) {
            result.put("code", 407);
            result.put("msg", "id为空");
            return result;
        }
        AppUserBMessageEntity appUserBMessage = appUserBMessageService.selectById(id);
        if (appUserBMessage == null) {
            result.put("code", 407);
            result.put("msg", "id有误");
            return result;
        }
        appUserBMessage.setId(id);
        String name = in.getString("name");
        if (StringUtils.isNotEmpty(name)) {
            appUserBMessage.setName(name);
        }
        if (StringUtils.isNotEmpty(in.getString("headImg"))) {
            appUserBMessage.setHeadImg(in.getString("headImg"));
        }
        if (StringUtils.isNotEmpty(in.getString("trailerPlate")))
            appUserBMessage.setTrailerPlate(in.getString("trailerPlate"));

        if (StringUtils.isNotEmpty(in.getString("introduction")))
            appUserBMessage.setIntroduction(in.getString("introduction"));

        if (StringUtils.isNotEmpty(in.getString("driverYear")))
            appUserBMessage.setDriverYear(in.getString("driverYear"));

        if (StringUtils.isNotEmpty(in.getString("technologyYear")))
            appUserBMessage.setTechnologyYear(in.getString("technologyYear"));

        if (StringUtils.isNotEmpty(in.getString("phone")))
            appUserBMessage.setPhone(in.getString("phone"));

        if (in.getBigDecimal("price") != null) {
            appUserBMessage.setPrice(in.getBigDecimal("price"));
        }
        appUserBMessageService.updateById(appUserBMessage);

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray jsonArray = (JSONArray) json.get("url");
        if (jsonArray != null) {
            List<AppUserBMessageImgEntity> urls = JSONArray.parseArray(jsonArray.toString(), AppUserBMessageImgEntity.class);
            for (AppUserBMessageImgEntity s : urls) {
                if (s != null) {
                    EntityWrapper<AppUserBMessageImgEntity> appUserImgWrapper = new EntityWrapper<>();
                    appUserImgWrapper.eq("b_message_id", id)
                            .eq("`index`", s.getIndex())
                            .eq("type", 1);
                    AppUserBMessageImgEntity appUserBMessageImgEntity = appUserBMessageImgService.selectOne(appUserImgWrapper);
                    if (appUserBMessageImgEntity == null) {
                        result.put("code", 407);
                        result.put("msg", "图片index有误");
                        return result;
                    }
                    appUserBMessageImgEntity.setUrl(s.getUrl());
                    appUserBMessageImgEntity.setUpdateTime(new Date());
                    appUserBMessageImgService.updateById(appUserBMessageImgEntity);
                }
            }
        }

        JSONArray trailer = (JSONArray) json.get("trailerUrl");
        if (trailer != null) {
            List<String> trailerurls = JSONArray.parseArray(trailer.toString(), String.class);
            for (int i = 0; i < trailerurls.size(); i++) {
                String s = trailerurls.get(i);
                if (StringUtils.isNotEmpty(s)) {
                    String substring = s.substring(0, 4);
                    if (!substring.equals("http")) {
                        result.put("code", 407);
                        result.put("msg", "图片路径");
                        return result;
                    }
                    EntityWrapper<AppUserBMessageImgEntity> appUserImgWrapper = new EntityWrapper<>();
                    appUserImgWrapper.eq("b_message_id", id)
                            .eq("`index`", i + 1)
                            .eq("type", 2);
                    AppUserBMessageImgEntity appUserBMessageImgEntity = appUserBMessageImgService.selectOne(appUserImgWrapper);
                    appUserBMessageImgEntity.setUrl(s);
                    appUserBMessageImgService.updateById(appUserBMessageImgEntity);
                }
            }
        }


        if (StringUtils.isNotEmpty(in.getString("workPlace"))) {
            if (in.getBigDecimal("lat") == null || in.getBigDecimal("lat").compareTo(BigDecimal.ZERO) == 0) {
                result.put("code", 407);
                result.put("msg", "纬度参数有误");
                return result;
            }

            if (in.getBigDecimal("lng") == null || in.getBigDecimal("lng").compareTo(BigDecimal.ZERO) == 0) {
                result.put("code", 407);
                result.put("msg", "经度参数有误");
                return result;
            }


            appUserBMessage.setWorkPlace(in.getString("workPlace"));
            appUserBMessage.setLat(in.getBigDecimal("lat"));
            appUserBMessage.setLng(in.getBigDecimal("lng"));
            appUserBMessageService.updateById(appUserBMessage);
            //修改geo数据
            AppUserEntity appUserEntity = userService.selectById(userBId);
            //1.救援,2.年检,3.喷漆,4.代驾
            /*if (appUserBMessage.getWrokType() == 2) {
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_YEAR_CHECK_GEO, appUserEntity.getYearcheckRedis());
                JSONObject js = new JSONObject();
                js.put("id", appUserEntity.getId());
                js.put("merchantsName", appUserEntity.getMerchantsName());
                js.put("lat", appUserBMessage.getLat());
                js.put("lng", appUserBMessage.getLng());
                js.put("address", appUserBMessage.getWorkPlace());
                //修改georedis数据
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_YEAR_CHECK_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), js.toJSONString());
                appUserEntity.setYearcheckRedis(js.toJSONString());
                userService.updateById(appUserEntity);
            } else if (appUserBMessage.getWrokType() == 1) {
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_RESCUE_GEO, appUserEntity.getRescueRedis());
                JSONObject js = new JSONObject();
                js.put("id", appUserEntity.getId());
                js.put("merchantsName", appUserEntity.getMerchantsName());
                js.put("lat", appUserBMessage.getLat());
                js.put("lng", appUserBMessage.getLng());
                js.put("address", appUserBMessage.getWorkPlace());
                //修改georedis数据
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_RESCUE_GEO, new Point(appUserBMessage.getLng().doubleValue(), appUserBMessage.getLat().doubleValue()), js.toJSONString());
                appUserEntity.setRescueRedis(js.toJSONString());
                userService.updateById(appUserEntity);
            }*/ /*else if (appUserBMessage.getWrokType() == 4) {
                //代驾
                stringRedisTemplate.opsForGeo().remove(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO,appUserEntity.getSubstituteRedis());
                JSONObject js = new JSONObject();
                js.put("id",appUserBMessage.getId());
                js.put("merchantsName",appUserBMessage.getName());
                js.put("lat",appUserBMessage.getLat());
                js.put("lng",appUserBMessage.getLng());
                js.put("address",appUserBMessage.getWorkPlace());
                //修改geo数据
                stringRedisTemplate.opsForGeo().add(RedisConstant.MERCHANTS_SUBSTITUTE_DIRVIE_GEO,new Point(appUserBMessage.getLng().doubleValue(),appUserBMessage.getLat().doubleValue()),js.toJSONString());
                appUserEntity.setSubstituteRedis(js.toJSONString());
                userService.updateById(appUserEntity);
            }*/
        }
        //修改服务状态去审核
        lableDetailsReviewTreeService.findser(appUserBMessage.getWrokType().toString(), userBId);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    @ApiOperation(value = "查询服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "wrokType", value = "工作内容（1.救援,2.年检,3.喷漆,4.代驾）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findServiceDetail", method = RequestMethod.GET)
    public JSONObject updataWorkPlace(String wrokType, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //查询服务详情
        EntityWrapper<AppUserBMessageEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_b_id", userBId)
                .eq("wrok_type", wrokType);
        AppUserBMessageEntity appUserBMessageEntity = appUserBMessageService.selectOne(wrapper);
        if (appUserBMessageEntity == null) {
            result.put("code", 200);
            result.put("msg", "数据为空");
            return result;
        }

        JSONObject in = new JSONObject();
        EntityWrapper<AppUserBMessageImgEntity> imgEntityWrapper1 = new EntityWrapper<>();
        imgEntityWrapper1.eq("b_message_id", appUserBMessageEntity.getId())
                .eq("type", 1);
        List<AppUserBMessageImgEntity> appUserBMessageImg1 = appUserBMessageImgService.selectList(imgEntityWrapper1);
        in.put("url", appUserBMessageImg1);

        EntityWrapper<AppUserBMessageImgEntity> imgEntityWrapper2 = new EntityWrapper<>();
        imgEntityWrapper2.eq("b_message_id", appUserBMessageEntity.getId())
                .eq("type", 2);
        List<AppUserBMessageImgEntity> appUserBMessageImg2 = appUserBMessageImgService.selectList(imgEntityWrapper2);
        if (appUserBMessageImg1.isEmpty()) {
            in.put("trailerUrl", appUserBMessageImg2);
        } else {
            ArrayList<String> strings = new ArrayList<>();
            for (AppUserBMessageImgEntity bMessageImgEntity : appUserBMessageImg2) {
                String url = bMessageImgEntity.getUrl();
                strings.add(url);
            }
            String[] str = strings.toArray(new String[0]);
            in.put("trailerUrl", str);
        }

        in.put("id", appUserBMessageEntity.getId());
        in.put("name", appUserBMessageEntity.getName());
        in.put("headImg", appUserBMessageEntity.getHeadImg());
        in.put("workPlace", appUserBMessageEntity.getWorkPlace());
        in.put("wrokType", appUserBMessageEntity.getWrokType());
        in.put("phone", appUserBMessageEntity.getPhone());
        in.put("lng", appUserBMessageEntity.getLng());
        in.put("lat", appUserBMessageEntity.getLat());
        in.put("trailerPlate", appUserBMessageEntity.getTrailerPlate());
        in.put("introduction", appUserBMessageEntity.getIntroduction());
        in.put("driverYear", appUserBMessageEntity.getDriverYear());
        in.put("technologyYear", appUserBMessageEntity.getTechnologyYear());
        in.put("businessType", appUserBMessageEntity.getBusinessType());
        in.put("score", appUserBMessageEntity.getScore());
        in.put("userBId", appUserBMessageEntity.getUserBId());
        in.put("price", appUserBMessageEntity.getPrice());
        in.put("serialNumber", appUserBMessageEntity.getSerialNumber());
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;

    }


}
