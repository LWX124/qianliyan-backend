package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppAuctionAliPayService;
import com.cheji.web.pojo.TokenPojo;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auction/alipay")
public class AppAuctionAliPayController extends BaseController {


    @Autowired
    private AppAuctionAliPayService appAuctionAliPayService;

    @ApiOperation(value = "vip充值" +
            "401：参数错误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "vipLv", value = "vip充值等级", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/createVipOrder", method = RequestMethod.GET)
    public JSONObject createVipOrder(@RequestParam(required = false) String vipLv, HttpServletRequest httpServletRequest) throws WxPayException {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionAliPayService.createVipOrder(result,vipLv,httpServletRequest,currentLoginUser);

    }


}