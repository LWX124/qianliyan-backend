package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppAuctionFeedBackService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 意见反馈
 */
@RestController
@RequestMapping("/feedback")
public class AppAuctionFeedBackController extends BaseController {

    @Autowired
    private AppAuctionFeedBackService backService;


    @ApiOperation(value = "意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "content", value = "内容", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/subFeedback", method = RequestMethod.GET)
    public JSONObject subFeedback(@RequestParam String content, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return backService.subFeedback(result,content,currentLoginUser.getAppUserEntity().getId());
    }


}