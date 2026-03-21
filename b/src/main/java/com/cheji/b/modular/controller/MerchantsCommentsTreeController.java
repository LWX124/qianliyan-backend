package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.MerchantsCommentsTree;
import com.cheji.b.modular.dto.CommentsDto;
import com.cheji.b.modular.service.MerchantsCommentsTreeService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/merchantsCommentsTree")
public class MerchantsCommentsTreeController extends BaseController {
    @Autowired
    public MerchantsCommentsTreeService merchantsCommentsTreeService;

    @ApiOperation(value = "评价下半截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "评价分类", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/merchComments", method = RequestMethod.GET)
    public JSONObject merchComments(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<CommentsDto> commentsDtoList = merchantsCommentsTreeService.findMerchantsComments(id, type, pagesize);
        //根据商户id查询到对应的评论
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", commentsDtoList);
        return result;
    }


    //commentsCode='4',
    @ApiOperation(value = "回复评价")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "commentsCode", value = "评论code", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "content", value = "回复内容", required = true, dataType = "String")
    })
    @RequestMapping(value = "/replyComment", method = RequestMethod.POST)
    public JSONObject replyComment(@RequestBody MerchantsCommentsTree merchantsComments, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        merchantsCommentsTreeService.addReplyComment(merchantsComments);

        result.put("code", 200);
        result.put("msg", "保存成功");
        return result;

    }


    @ApiOperation(value = "推修评价" +
            "407 参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "lableId", value = "服务id（1：推修，2：洗车，3：喷漆，4：救援，5：年检，6：代驾）", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/pushIndentComment", method = RequestMethod.GET)
    public JSONObject pushIndentComment(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize, Integer lableId) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        if (lableId == null) {
            result.put("code", 407);
            result.put("msg", "lableId为空");
            return result;
        }
        if (lableId == 2) {
            lableId = 4;            //洗美
        } else if (lableId == 3) {
            lableId = 12;            //喷漆
        } else if (lableId == 4) {
            lableId = 13;           //救援
        } else if (lableId == 5) {
            lableId = 15;           //年检
        } else if (lableId == 6) {
            lableId = 19;
        }
        List<CommentsDto> commentsDtoList = merchantsCommentsTreeService.findPushIndentComments(userBId, pagesize, lableId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", commentsDtoList);
        return result;
    }


}
