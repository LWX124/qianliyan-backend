package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.VideoCommontsThumbsEntity;
import com.cheji.web.modular.service.VideoCommontsThumbsService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/videoCommontsThumbs")
public class VideoCommontsThumbsController extends BaseController {
    @Resource
    private VideoCommontsThumbsService videoCommontsThumbsService;


    //点击点赞按钮传入数据，用户id，评论id,然后调用redis
    @ApiOperation(value = "视频评论点赞数据，status,1,点赞，0，未赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoCommontsId", value = "视频评论id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "点赞状态", required = true, dataType = "String")
    })
    @RequestMapping(value = "/thumbs", method = RequestMethod.POST)
    public JSONObject thumbs(@RequestBody VideoCommontsThumbsEntity videoCommontsThumbsEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        // String user = videoCommontsThumbsEntity.getUserId();
        Integer id = currentLoginUser.getAppUserEntity().getId();
        String videoCommontsId = videoCommontsThumbsEntity.getVideoCommontsId();
        //如果点赞状态是零或者空就为1
        if (videoCommontsThumbsEntity.getStatus() == 0 || videoCommontsThumbsEntity.getStatus() == null) {
            //先判断有无数据
            String save = videoCommontsThumbsService.save(videoCommontsId, String.valueOf(id));
            result.put("code", 200);
            result.put("msg", "点赞成功");
            result.put("data", save);
            return result;
        } else {
            //不是空就为0
            String cancel = videoCommontsThumbsService.cancel(videoCommontsId, String.valueOf(id));
            result.put("code", 200);
            result.put("msg", "取消点赞");
            result.put("data", cancel);
            return result;
        }
    }


}
