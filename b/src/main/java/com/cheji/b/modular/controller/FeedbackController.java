package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.FeedbackEntity;
import com.cheji.b.modular.service.FeedbackService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController{
    @Autowired
    public FeedbackService feedbackService;

    @ApiOperation(value = "保存意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "content",value = "反馈内容",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "imgFirst",value = "第一张图片",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "imgSecond",value = "第二张图片",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "imgThird",value = "第三张图片",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "imgFourth",value = "第四张图片",required = false,dataType = "String")
    })
    @RequestMapping(value = "/saveFeedback",method = RequestMethod.POST)
    public JSONObject saveFeedback(HttpServletRequest request, @RequestBody FeedbackEntity feedbackEntity){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (feedbackEntity.getContent()==null){
            result.put("code", 404);
            result.put("msg", "返回不能为空");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        String phoneNumber = currentLoginUser.getAppUserEntity().getPhoneNumber();
        feedbackEntity.setPhoneNumber(phoneNumber);
        feedbackEntity.setUserBId(userBId);
        feedbackService.saveFeed(feedbackEntity);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


}
