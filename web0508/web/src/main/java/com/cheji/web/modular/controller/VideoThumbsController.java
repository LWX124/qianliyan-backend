package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.VideoThumbsEntity;
import com.cheji.web.modular.service.VideoThumbsService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/videoThumbs")
public class VideoThumbsController extends BaseController {
    @Resource
    private VideoThumbsService videoThumbsService;


    /*
     * 点赞按钮传入数据， 用户id，调用reids
     * */
    @ApiOperation(value = "视频点赞数据，status,1,点赞，0，未赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "点赞状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/thumbs", method = RequestMethod.POST)
    public JSONObject thumbs(@RequestBody VideoThumbsEntity videoThumbsEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String videoId = videoThumbsEntity.getVideoId();
        // String userId = videoThumbsEntity.getUserId();
        Integer userId = currentLoginUser.getAppUserEntity().getId();
        //如果点赞状态是零或者空就修改为1
        if (videoThumbsEntity.getStatus() == 0 || videoThumbsEntity.getStatus() == null) {
            videoThumbsService.save(videoId, String.valueOf(userId));
            result.put("code", 200);
            result.put("msg", "点赞成功");
            return result;
        } else {
            //不是空就为修改状态零
            videoThumbsService.cancel(videoId, String.valueOf(userId));
            result.put("code", 200);
            result.put("msg", "取消点赞");
            return result;
        }
    }


}
