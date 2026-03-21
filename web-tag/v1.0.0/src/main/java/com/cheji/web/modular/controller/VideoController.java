package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.AccidentReward;
import com.cheji.web.modular.cwork.Report;
import com.cheji.web.modular.domain.AppUserPlusEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.domain.VideoCommentsEntity;
import com.cheji.web.modular.service.AccidentRecordService;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.modular.service.VideoCommentsService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.cheji.web.constant.RedisConstant.APP_VIDEO_LIST;

//首页展示视频
@RestController
@RequestMapping("/video")
public class VideoController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private VideoCommentsService videoCommentsService;

    @Resource
    private AccidentRecordService accidentRecordService;

    @Resource
    private UserService userService;

    @Value("${host}")
    private String host;

    /**
     * 每台手机拥有一个唯一标识，代表当前用户。如果该用户在redis保存的又视频下标，则在下标的基础上找到新的五条数据返回给用户。
     * 如果用户第一次进入app，那么给用户一个初始的下标值=从当前日期算30天以前的时间戳
     *
     * @param userCode
     * @return
     */
    @GetMapping("/queryVideos")
    @ApiOperation(value = "首页视频" +
            "401：入参错误" +
            "201:没有新的视频了")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userCode", value = "用户标识", required = true, dataType = "String"),
    })
    public JSONObject queryVideos(String userCode, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        if (StringUtils.isEmpty(userCode)) {
            result.put("code", 401);
            result.put("msg", "入参错误");
            return result;
        }
        JSONArray array = new JSONArray();
        getVideo(userCode, request, array);

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.APP_FIRST_VIDEO);
        if (!StringUtils.isEmpty(s)) {
            JSONObject object = JSONObject.parseObject(s);
            //获取实时点赞数量
            String videoId = object.getString("videoId");
            Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.SET_VIDEO_THUMBS + videoId);
            object.put("count", size);

            TokenPojo currentLoginUser = getCurrentLoginUser(request);
            boolean loginFlag = currentLoginUser != null;
            if (loginFlag) {//判断用户是否对该视频点赞
                Integer userId = currentLoginUser.getAppUserEntity().getId();
                Double score = stringRedisTemplate.opsForZSet().score(RedisConstant.LIKE_USER_THUMBS_VIDEO + userId, videoId);
                if (score == null || score == 0D) {
                    object.put("likeFlag", false);
                } else {
                    object.put("likeFlag", true);
                }
            } else {
                object.put("likeFlag", "");//前端要求返回一个空字符串
            }
            Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(videoId), 1);
            object.put("commentsCount", videoCommentCount);
            //分享次数
            String o = (String) stringRedisTemplate.opsForHash().get(RedisConstant.VIDEO_SHARE, videoId);
            object.put("shareCount", o);

            array.add(0, object);
        }

        result.put("code", 200);
        result.put("data", array);
        return result;
    }

    @GetMapping("/firstVideo")
    @ApiOperation(value = "首页视频" +
            "401：入参错误" +
            "201:没有新的视频了")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userCode", value = "用户标识", required = true, dataType = "String"),
    })
    public JSONObject firstVideo(String userCode) {
        JSONObject result = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(userCode)) {
            result.put("code", 401);
            result.put("msg", "入参错误");
            return result;
        }

        //app首页话术
        String appHomeText = stringRedisTemplate.opsForValue().get(RedisConstant.APP_HOME_TEXT);

        String s = stringRedisTemplate.opsForValue().get(RedisConstant.APP_FIRST_VIDEO);
        logger.info("首页视频 #### s={}", s);

        if (!StringUtils.isEmpty(s)) {
            JSONObject out = new JSONObject();
            out.put("appHomeText", appHomeText);
            out.put("data", s);
            result.put("code", 200);
            result.put("data", out);
            logger.info("首页视频 #### result={}", result);
            return result;
        }

        logger.error("### 没有在缓存中找到第一个视频信息###");


        String index = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE + userCode);
        if (StringUtils.isEmpty(index)) {
            Date now = new Date();
            Date startDate = DateUtils.addDays(now, -30);
            index = startDate.getTime() + "";
        }
        long time = new Date().getTime();
        Set range = stringRedisTemplate.opsForZSet().rangeByScore(APP_VIDEO_LIST, Long.valueOf(index) + 1, time, 0, 1);
        if (range.size() < 1) {
            Date now = new Date();
            Date startDate = DateUtils.addDays(now, -30);
            index = startDate.getTime() + "";
            Set range2 = stringRedisTemplate.opsForZSet().rangeByScore(APP_VIDEO_LIST, Long.valueOf(index) + 1, time, 0, 1);
            Iterator iterator = range2.iterator();
            Object next = iterator.next();
            jsonObject.put("data", next);
        } else {
            Iterator iterator = range.iterator();
            Object next = iterator.next();
            jsonObject.put("data", next);
        }

        jsonObject.put("appHomeText", appHomeText);
        result.put("code", 200);
        result.put("data", jsonObject);
        return result;
    }

    private void getVideo(String userCode, HttpServletRequest request, JSONArray array) {
        String index = stringRedisTemplate.opsForValue().get(RedisConstant.USER_PHONE_CODE + userCode);
        logger.info("### userCode={}，下标 index={}", userCode, index);
        if (StringUtils.isEmpty(index)) {
            Date now = new Date();
            Date startDate = DateUtils.addDays(now, -30);
            index = startDate.getTime() + "";
        }
        long time = new Date().getTime();
        //一次取20条数据
        Set range = stringRedisTemplate.opsForZSet().rangeByScore(APP_VIDEO_LIST, Long.valueOf(index) + 1, time, 0, 20);
        logger.info("### range.size={}", range.size());


        Iterator iterator = range.iterator();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        boolean loginFlag = currentLoginUser != null;
        while (iterator.hasNext()) {
            Object next = iterator.next();
            JSONObject object = JSONObject.parseObject(next.toString());
            Long creatTime = object.getLong("creatTime");
            index = creatTime + "";//跟新用户观看视频的下标

            //名字节码
            String name = object.getString("name");
            if (!StringUtils.isEmpty(name)) {
                int i = name.indexOf("%");
                if (i != -1) {
                    try {
                        name = java.net.URLDecoder.decode(name, "UTF-8");
                        object.put("name", name);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            //获取实时点赞数量
            String videoId = object.getString("videoId");
            Long size = stringRedisTemplate.opsForZSet().size(RedisConstant.SET_VIDEO_THUMBS + videoId);
            object.put("count", size);

            if (loginFlag) {//判断用户是否对该视频点赞
                Integer userId = currentLoginUser.getAppUserEntity().getId();
                Double score = stringRedisTemplate.opsForZSet().score(RedisConstant.LIKE_USER_THUMBS_VIDEO + userId, videoId);
                if (score == null || score == 0D) {
                    object.put("likeFlag", false);
                } else {
                    object.put("likeFlag", true);
                }
            } else {
                object.put("likeFlag", "");//前端要求返回一个空字符串
            }
            Long videoCommentCount = videoCommentsService.findVideoCommentCount(Long.valueOf(videoId), 1);
            object.put("commentsCount", videoCommentCount);
            //分享次数
            String o = (String) stringRedisTemplate.opsForHash().get(RedisConstant.VIDEO_SHARE, videoId);
            object.put("shareCount", o);

            array.add(object);

        }
        stringRedisTemplate.opsForValue().set(RedisConstant.USER_PHONE_CODE + userCode, index, 60 * 60 * 24 * 30, TimeUnit.SECONDS);//过期时间30天
        if (array.size() < 8) {
            //删除key 重新轮回一遍
            stringRedisTemplate.delete(RedisConstant.USER_PHONE_CODE + userCode);
            logger.info("###递归 ### userCode={}", userCode);
            getVideo(userCode, request, array);
        }
    }


    @ApiOperation(value = "保存举报信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "reportId", value = "举报数据id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "count", value = "内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "firstImg", value = "第一张图片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "secondImg", value = "第二张图片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "thirdImg", value = "第三张图片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fourthImg", value = "第四张图片", required = true, dataType = "String")
    })
    @RequestMapping(value = "/saveReport", method = RequestMethod.POST)
    public JSONObject saveReport(@RequestBody Report report, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser != null) {
            Integer id = currentLoginUser.getAppUserEntity().getId();
            report.setUserId(String.valueOf(id));
        }

        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


    @ApiOperation(value = "不感兴趣")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/disLike", method = RequestMethod.GET)
    public JSONObject disLike(/*HttpServletRequest request,String videoId*/) {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

    @ApiOperation(value = "个人信息")
    @RequestMapping(value = "/personMessage", method = RequestMethod.GET)
    public JSONObject personMessage() {
        JSONObject result = new JSONObject();
        String str = "1.在仅浏览时，为保障服务所需,我们会" +
                "申请系统权限收集设备信息、日志信息，" +
                "用于信息推送和安全风控，并申请存储权" +
                "限，用于下载短视频及缓存相关文件。\n" +
                "2.为了基于你的所在位置向你推荐内容." +
                "在你的个人主页显示位置信息，或经你选" +
                "择在你发布的视频中显示位置信息，我们" +
                "可能会申请位置权限。\n" +
                "3.为帮助你在APP中拨打投诉电话或其他" +
                "咨询热线，我们可能会申请拨打电话权" +
                "限，该权限不会收集隐私信息，且仅在你" +
                "使用前述功能时使用。\n" +
                "4.通讯录、GPS、摄像头、麦克风、相册" +
                "等敏感权限均不会默认开启，只有经过明" +
                "示授权才会为实现功能或服务时使用，你" +
                "均可以拒绝且不影响你继续使用车己。";
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", str);
        return result;
    }


    //事故奖励，数据从 app表中和小程序表中查询，查询出来除了播放量之外的数据。
    //现金奖励 修改评论查询的条件。评论绑定事故表
    //操作，
    @ApiOperation(value = "事故奖励")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型，1：近三天事故，2：重大事故", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/accidentReward", method = RequestMethod.GET)
    public JSONObject accidentReward(Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject object = new JSONObject();
        //查询近一天数据
        if (type == 1) {
            List<AccidentReward> array = accidentRecordService.findAccidentReward(pagesize);
            object.put("code", 200);
            object.put("msg", "成功");
            object.put("data", array);
            return object;
        } else if (type == 2) {
            //重大事故
            List<AccidentReward> seriousAccident = accidentRecordService.findSeriousAcc(pagesize);
            object.put("code", 200);
            object.put("msg", "成功");
            object.put("data", seriousAccident);
            return object;
        } else {
            object.put("code", 407);
            object.put("msg", "检查type参数");
            return object;
        }
    }


    @ApiOperation(value = "保存事故奖励评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentCode", value = "父级id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "count", value = "内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "事故id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/saveAccidentComments", method = RequestMethod.POST)
    public JSONObject saveAccidentComments(@RequestBody VideoCommentsEntity videoCommentsEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        videoCommentsEntity.setUserId(id);
        VideoCommentsEntity save = videoCommentsService.save(videoCommentsEntity, 2);
        Integer userId = save.getUserId();
        UserEntity userEntity = userService.selectById(userId);
        String avatar = userEntity.getAvatar();
        String name = userEntity.getName();
        save.setName(name);
        save.setAvatar(avatar);
        BigDecimal treeLevel = save.getTreeLevel();
        int i = treeLevel.intValue();
        if (i >= 2) {
            //获取到回复评论得人
            String parentCode = save.getParentCode();
            VideoCommentsEntity videoCommentsEntity1 = videoCommentsService.selectById(parentCode);
            Integer userId1 = videoCommentsEntity1.getUserId();
            UserEntity userEntity1 = userService.selectById(userId1);
            String name1 = userEntity1.getName();
            save.setReplyUser(name1);

        }
        result.put("code", 200);
        result.put("msg", "保存评论成功");
        result.put("data", save);
        return result;
    }

    @ApiOperation(value = "查询事故奖励评论数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "事故id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "treeCode", value = "一级评论的id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/getAccidentComment", method = RequestMethod.GET)
    public JSONObject getAccidentComment(@RequestParam(required = false, defaultValue = "1") Integer pagesize, String treeCode, String videoId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();

        //如果一级评论的id不为空那就查询一级评论之下的数据
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(treeCode)) {
            //根据一级评论的id查询到下面的评论
            List<VideoCommentsEntity> AccidentOtherComments = videoCommentsService.findAccidentOtherComment(treeCode, pagesize);
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", AccidentOtherComments);
            return result;
        }
        //查询用户评论数据
        //第一次查询一级评论分页查询一级评论
        List<VideoCommentsEntity> AccidentOneComments = videoCommentsService.findAccidentOneComments(videoId, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", AccidentOneComments);
        return result;

    }


    @ApiOperation(value = "分享事故奖励视频")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accid", value = "事故id", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/accidentVideoWeb", method = RequestMethod.GET)
    public JSONObject accidentVideoWeb(String accid) {
        String backurl = host + "cServer/page/accWebs?accid=" + accid + "&code=";
        JSONObject result = new JSONObject();
        //获取标题
        String title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE);
        //获取内容
        String content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT);
        result.put("data", backurl);
        result.put("code", 200);
        result.put("title", title);
        result.put("content", content);
        return result;
    }


}
