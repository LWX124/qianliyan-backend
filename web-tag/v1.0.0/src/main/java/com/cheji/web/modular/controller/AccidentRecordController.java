package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.AccidentDetails;
import com.cheji.web.modular.cwork.AccidentList;
import com.cheji.web.modular.cwork.WorkList;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/AccidentRecord")
public class AccidentRecordController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AccidentRecordController.class);

    @Resource
    private AccidentRecordService AccidentRecordEntityService;

    @Resource
    private PushRecordService pushRecordService;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppRepeatAccidentService appRepeatAccidentService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    @RequestMapping("/test")
    public String test(){
        return "44444";
    }

    //查询事故信息列表
    @ApiOperation(value = "根据用户id和处理状态查询事故列表信息" +
            "事故真实性(-1,待处理.0,真现场.1,假现场.2,已撤离.3,非事故)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "事故状态", required = false, dataType = "int")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JSONObject list(HttpServletRequest request, String userId, Integer state) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //根据是否有状态判断查询得信息
        //没有状态就是默认查询
        //先查询推送记录根据用户id
        List<PushRecordEntity> pushRecordEntities = pushRecordService.findRecoreByUserId(userId);
        //查询待处理得事故信息，根据推送记录来查询
        List<AccidentList> accidents = AccidentRecordEntityService.findAccidByPush(pushRecordEntities, state);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", accidents);
        return result;
    }

    //事故详情页面
    @ApiOperation(value = "根据事故id查询事故详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "accid", value = "事故id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/listDetails", method = RequestMethod.GET)
    public JSONObject listDetails(HttpServletRequest request, String accid) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //事故id来查询事故，拿到上报人电话和姓名
        AccidentDetails deatilsByAccid = AccidentRecordEntityService.findDeatilsByAccid(accid);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", deatilsByAccid);
        return result;
    }


    //事故详情页面修改事故信息状态
    @ApiOperation(value = "修改事故信息状态" +
            "事故真实性(-1,待处理.0,真现场.1,假现场.2,已撤离.3,非事故)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "事故id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "realness", value = "事故真实性", required = true, dataType = "int")
    })
    @RequestMapping(value = "/updateState", method = RequestMethod.GET)
    public JSONObject updateState(HttpServletRequest request, Integer id, Integer realness) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        AccidentRecordEntityService.updateState(id, realness);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    //报事故 今红包页面，根据日期查询当前用户的红包信息
    @ApiOperation(value = "报事故，今红包界面根据用户id查询今天的红包信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userid", value = "用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/todayRedEnvelope", method = RequestMethod.GET)
    public JSONObject todayredEnvelope(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //根据用户id和当天日期来查询到对应的事故信息和支付金额
        List<AccidentList> accidListByIdAndDate = AccidentRecordEntityService.findAccidListByIdAndDate(id, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", accidListByIdAndDate);
        return result;
    }

    //报事故 粉丝数页面
    @ApiOperation(value = "报事故，粉丝数页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userid", value = "用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/fans", method = RequestMethod.GET)
    public JSONObject fans(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //根据用户id查询到他的粉丝数
        List<UserEntity> fansByUserid = userService.findFansByUserid(id, pagesize);

        //查询到没有环信账号和id的商户，注册
        List<AppUpMerchantsEntity> list = appUpMerchantsService.findNoHuanxin();
        if (!list.isEmpty()){
            //注册环信
            for (AppUpMerchantsEntity appUpMerchantsEntity : list) {
                double rand = Math.random();
//将随机数转换为字符串
                String str = String.valueOf(rand).replace("0.", "");
//截取字符串
                String newStr = str.substring(0, 11);

                //userService,registerHuanxin()
                appUpMerchantsService.registerHuanxin(appUpMerchantsEntity,newStr);
//            userService.registerHuanxin(appUserEntity);
            }
        }


        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", fansByUserid);
        return result;
    }

    //根据用户id来查询到对应的事故信息和支付金额，总红包
    @ApiOperation(value = "报事故总红包页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userid", value = "用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/redEnvelope", method = RequestMethod.GET)
    public JSONObject redEnvelope(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<AccidentList> accidByUserid = AccidentRecordEntityService.findAccidByUserid(id, pagesize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", accidByUserid);
        return result;
    }


    //报事故页面我得作品，根据用户id查询到上传得作品
    @ApiOperation(value = "报事故我的作品页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int"),
    })
    @RequestMapping(value = "/works", method = RequestMethod.GET)
    public JSONObject works(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<WorkList> worksByUserId = AccidentRecordEntityService.findWorksByUserId(String.valueOf(id), pagesize);

        result.put("code", 200);
        result.put("msg", "成功,我的作品");
        result.put("data", worksByUserId);
        return result;

    }

    //报事故页面,喜欢，查询他都给哪些视频点了赞
    @ApiOperation(value = "喜欢，查询他给哪些视频点了赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "int")
    })
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public JSONObject like(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        String s = String.valueOf(id);
        //从redis中获取到数据
        Long size = 20L;
        Long start = size * (pagesize - 1);
        Long end = (size - 1) * pagesize;
        Set<String> range = stringRedisTemplate.opsForZSet().range(RedisConstant.LIKE_USER_THUMBS_VIDEO + s, start, end);
        //如果为空
        if (range.isEmpty()) {
            result.put("code", 200);
            result.put("msg", "成功");
            //result.put("data", "没有数据");
            return result;
        }

        List<WorkList> workLists = new ArrayList<>();
        //根据视频id list查询到对应得视频
        for (String member : range) {
            //通过视频id查询到数据
            WorkList list = AccidentRecordEntityService.findVideoById(member);
            if (list == null) {
                logger.error("### 喜欢，查询他给哪些视频点了赞 通过视频id查询到数据 list为空   member={}", member);
                continue;
            }
            workLists.add(list);
        }
        // List<WorkList> videoByUserid = AccidentRecordEntityService.findVideoByUserid(id);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", workLists);
        return result;
    }

    //保存上传的视频
    @ApiOperation(value = "保存上传的视频" +
            "视频地址：url" + "视频简介：introduce" + "用户id:user_id")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "video", value = "视频url", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "introduce", value = "视频介绍", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imgUrl", value = "图片url", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = false, dataType = "Double"),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = false, dataType = "Double")
    })
    @RequestMapping(value = "/addVideo", method = RequestMethod.POST)
    public JSONObject addVideo(@RequestBody AccidentRecordEntity accidentRecordEntity, HttpServletRequest request) {

        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        accidentRecordEntity.setUserId(String.valueOf(id));

        //logger.info("### 保存上传的视频 ### accidentRecordEntity={}",accidentRecordEntity);
        if (null == accidentRecordEntity.getId()) {
//            //通过查询经纬度和用户id来找到对应id
////            List<AccidentRecordEntity> agomessage = AccidentRecordEntityService.selectByuserIdAndLngLat(accidentRecordEntity.getUserId(),accidentRecordEntity.getLng(),accidentRecordEntity.getLat());
////            if (agomessage!=null){
////                for (AccidentRecordEntity recordEntity : agomessage) {
////                    if (recordEntity.getUserId().equals(accidentRecordEntity.getUserId())
////                            &&
////                            recordEntity.getLat().toString().equals(accidentRecordEntity.getLat().toString())
////                            &&
////                            recordEntity.getLng().toString().equals(accidentRecordEntity.getLng().toString())
////                            ){
////                        result.put("code", 500);
////                        result.put("msg", "不能上传相同视频");
////                        return result;
////                    }
////                }
////            }
//            String video = accidentRecordEntity.getVideo();
//            String key = video.substring(video.length() - 12);
//            String s = stringRedisTemplate.opsForValue().get(RedisConstant.VIDEO_UP + key);
//            if (StringUtils.isEmpty(s)) {
//                stringRedisTemplate.opsForValue().set(RedisConstant.VIDEO_UP + key, "1", 60 * 5, TimeUnit.SECONDS);
//            } else {
//                result.put("code", 500);
//                result.put("msg", "不能上传相同视频");
//                return result;
//            }
            AccidentRecordEntity save = AccidentRecordEntityService.save(accidentRecordEntity, id);
//            if (accidentRecordEntity.getType() == 1) {
//                AccidentRecordEntityService.addGeoAndRepeat(save);
//            }
            result.put("code", 200);
            result.put("msg", "成功,上传作品");
            result.put("data", save.getId());
            if (accidentRecordEntity.getType() == 1) {
                try {
                    AccidentRecordEntityService.send();
                } catch (Exception e) {
                    logger.error("往管理端发送长连接通知失败！# error:{}", e.getStackTrace());
                }
            }
            return result;
        } else {
            AccidentRecordEntityService.updateById(accidentRecordEntity);
            result.put("code", 200);
            result.put("msg", "成功,上传作品");
            return result;
        }

    }


    //视频详情
    @ApiOperation(value = "视频详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "作品id", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/videoDetails", method = RequestMethod.GET)
    public JSONObject videoDetails(HttpServletRequest request, Integer id) {
        JSONObject result = new JSONObject();
        //根据视频查询
        AccidentRecordEntity accidentRecordEntity = AccidentRecordEntityService.selectById(id);
        if (accidentRecordEntity == null) {
            result.put("code", 407);
            result.put("msg", "检查视频id");
            return result;
        }
        JSONObject data = new JSONObject();
        //视频url
        data.put("videoUrl", accidentRecordEntity.getVideo());
        //地址，时间，金额
        data.put("address", accidentRecordEntity.getAddress());
        Date createTime = accidentRecordEntity.getCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(createTime);
        data.put("createTime", format);

        //查询是否发送了红包和金额
        BigDecimal amount = AccidentRecordEntityService.selectPayRecord(id.toString());
        if (amount == null) {
            data.put("amount", 0.00);
        } else {
            data.put("amount", amount);
        }

        //审核原因
        data.put("reason", accidentRecordEntity.getReason());
        //重复事故视频list
        //查询重复事故，来自app得事故
        ArrayList<String> urlList = new ArrayList<>();
        List<AppRepeatAccidentEntity> accidentList = appRepeatAccidentService.selectByAccid(id);
        for (AppRepeatAccidentEntity accidentEntity : accidentList) {
            Integer accidentSource = accidentEntity.getAccidentSource();
            //区别app和小程序上传
            if (accidentSource == 1) { //1是app上传
                //查询到地址
                AccidentRecordEntity repeatVideo = AccidentRecordEntityService.selectById(accidentEntity.getRepeatId());
                urlList.add(repeatVideo.getVideo());
            } else {
                //查询小程序
                String wxUrl = appRepeatAccidentService.selectWxVideo(accidentEntity.getAccidentSource());
                urlList.add(wxUrl);
            }
        }

        data.put("urlList", urlList);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", data);
        return result;

    }


}
