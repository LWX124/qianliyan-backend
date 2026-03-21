package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.AppUserPlusEntity;
import com.cheji.web.modular.domain.LeagueEntity;
import com.cheji.web.modular.service.AppUserPlusService;
import com.cheji.web.modular.service.LeagueService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/league")
public class LeagueController extends BaseController {
    @Resource
    private LeagueService leagueService;

    @Resource
    private AppUserPlusService appUserPlusService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${host}")
    private String host;

    /**
     * 保存加盟信息
     */
    @ApiOperation(value = "保存加盟信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityName", value = "城市名字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shopName", value = "店铺名字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brand", value = "品牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "加盟人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phoneNumber", value = "加盟人电话", required = true, dataType = "String")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JSONObject save(@RequestBody LeagueEntity leagueEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        //保存数据
        leagueService.save(leagueEntity);
        result.put("code", 200);
        result.put("msg", "加盟已提交");
        return result;
    }


    @ApiOperation(value = "返回网页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/backWeb", method = RequestMethod.GET)
    public JSONObject backWeb(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        AppUserPlusEntity plusUserByid = appUserPlusService.findPlusUserByid(String.valueOf(id));
        String url = host + "cServer/page/register1?code=";
        //判断环境是否是测试环境
        return setdata(url, result, plusUserByid);
    }

    @ApiOperation(value = "分享视频")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/backVideoWeb", method = RequestMethod.GET)
    public JSONObject backVideoWeb(HttpServletRequest request, String videoId) {
        String url = host + "cServer/page/share?videoId=" + videoId + "&code=";
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        //未登录
        if (currentLoginUser == null) {
            //获取标题
            String title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE + 18);
            //获取内容
            String content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + 18);
            result.put("data", url);
            result.put("code", 200);
            result.put("title", title);
            result.put("content", content);
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        AppUserPlusEntity plusUserByid = appUserPlusService.findPlusUserByid(String.valueOf(id));
        //分享视频
        //判断是否是plus会员
        //获取到视频id

        return setdata(url, result, plusUserByid);
    }

    @ApiOperation(value = "根据不同场景获取不同分享话术  1：洗车  2美容  3保养  4 维修  5 喷漆   6代驾  7加油  8救援 9违章 10 年检，13.注册，")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sceneId", value = "1", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getShareTitle", method = RequestMethod.GET)
    public JSONObject getShareTitle(HttpServletRequest request, Integer sceneId) {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();

        if (sceneId == null) {//获取默认场景
            String title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE);
            String content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT);
            data.put("title", title);
            data.put("content", content);
        } else {
            String title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE + sceneId);
            String content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + sceneId);
            if (StringUtils.isEmpty(title)) {
                title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE);
            }
            if (StringUtils.isEmpty(content)) {
                content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT);
            }
            data.put("title", title);
            data.put("content", content);
        }

        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    private JSONObject setdata(String url, JSONObject result, AppUserPlusEntity plusUserByid) {
        String setmeg = setmeg(url, plusUserByid);

        //获取标题
        String title = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_TITLE + 18 );
        //获取内容
        String content = stringRedisTemplate.opsForValue().get(RedisConstant.SHARE_CONTENT + 18);
        //加油分享内容
        String oilShareText = stringRedisTemplate.opsForValue().get(RedisConstant.APP_OIL_SHARE_TEXT);
        result.put("code", 200);
        result.put("title", title);
        result.put("content", content);
        result.put("oilShareText", oilShareText);
        result.put("data", setmeg);
        return result;
    }


    //判断用户是否是plus会员
    private String setmeg(String str, AppUserPlusEntity plusUserByid) {
        if (plusUserByid == null) {
            return str;
        }
        Date invalidTimeEnd = plusUserByid.getInvalidTimeEnd();
        Date date = new Date();
        if (invalidTimeEnd.before(date)) {
            return str;
        }
        //是plus会员
        return str + plusUserByid.getInviteCode();
    }

    @ApiOperation(value = "分享计数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/shareCount", method = RequestMethod.GET)
    public JSONObject shareCount(String videoId) {
        JSONObject result = new JSONObject();
        //分享次数
        String s = (String) stringRedisTemplate.opsForHash().get(RedisConstant.VIDEO_SHARE, videoId);
        if (StringUtils.isEmpty(s)) {
            //新增一个数据
            stringRedisTemplate.opsForHash().put(RedisConstant.VIDEO_SHARE, videoId, "1");
        } else {
            //+1
            stringRedisTemplate.opsForHash().increment(RedisConstant.VIDEO_SHARE, videoId, 1);
        }
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }




}
