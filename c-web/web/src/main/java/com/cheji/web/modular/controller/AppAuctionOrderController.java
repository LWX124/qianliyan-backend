package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.modular.service.AppAuctionOrderService;
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

@RestController
@RequestMapping("/auctionOrder")
public class AppAuctionOrderController extends BaseController {

    @Autowired
    private AppAuctionOrderService appAuctionOrderService;


    @ApiOperation(value = "账单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
    })
    @RequestMapping(value = "/queryOrder", method = RequestMethod.GET)
    public JSONObject queryOrder(@RequestParam(required = false)Integer current,HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        final Integer size = 20;
        Page page = new Page<>(current, size);
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionOrderService.queryOrder(page, result, currentLoginUser.getAppUserEntity().getId());
    }


}