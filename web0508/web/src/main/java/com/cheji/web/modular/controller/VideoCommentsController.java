package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.domain.VideoCommentsEntity;
import com.cheji.web.modular.service.UserService;
import com.cheji.web.modular.service.VideoCommentsService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/videoComments")
public class VideoCommentsController extends BaseController {
    @Resource
    private VideoCommentsService videoCommentsService;
    @Resource
    private UserService userService;


    @ApiOperation(value = "查询用户评论数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "treeCode", value = "一级评论的id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/treeData", method = RequestMethod.GET)
    public JSONObject treeDaa(@RequestParam(required = false, defaultValue = "1") Integer pagesize, String treeCode, String videoId,HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();

        //如果一级评论的id不为空那就查询一级评论之下的数据
        if (StringUtils.isNotEmpty(treeCode)) {
            //根据一级评论的id查询到下面的评论
            List<VideoCommentsEntity> videoOtherComments = videoCommentsService.findOtherComment(id,videoId, treeCode, pagesize);
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", videoOtherComments);
            return result;
        }
        //查询用户评论数据
        //第一次查询一级评论分页查询一级评论
        List<VideoCommentsEntity> videoOneComments = videoCommentsService.findOneComments(id,videoId, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", videoOneComments);
        return result;

    }

    @ApiOperation(value = "保存视频评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentCode", value = "父级id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "count", value = "内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "videoId", value = "视频id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addenp", method = RequestMethod.POST)
    public JSONObject addenp(@RequestBody VideoCommentsEntity videoCommentsEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        videoCommentsEntity.setUserId(id);
        VideoCommentsEntity save = videoCommentsService.save(videoCommentsEntity,1);
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

}
