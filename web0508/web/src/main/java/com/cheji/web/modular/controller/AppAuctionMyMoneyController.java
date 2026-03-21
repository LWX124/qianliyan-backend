package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.modular.service.AppAuctionMyMoneyService;
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
@RequestMapping("/mymoney")
public class AppAuctionMyMoneyController extends BaseController {

    @Autowired
    private AppAuctionMyMoneyService appAuctionMyMoneyService;

    final Integer size = 20;

    @ApiOperation(value = "保证金")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "queryType", value = "查询类型(1:vip保证金  2：单车保证金)", required = true, dataType = "Integer"),})
    @RequestMapping(value = "/bail", method = RequestMethod.GET)
    public JSONObject bail(@RequestParam(required = true) Integer current, @RequestParam(required = false) Integer queryType, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        Page page = new Page<>(current, size);
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionMyMoneyService.bail(result, page, currentLoginUser.getAppUserEntity().getId(),queryType);

    }

    @ApiOperation(value = "服务费")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),})
    @RequestMapping(value = "/servicefee", method = RequestMethod.GET)
    public JSONObject servicefee(@RequestParam(required = false) Integer current, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        Page page = new Page<>(current, size);
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionMyMoneyService.servicefee(page, result, currentLoginUser.getAppUserEntity().getId());

    }

    @ApiOperation(value = "签约记录")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "current", value = "当前页", required = true, dataType = "Integer"),})
    @RequestMapping(value = "/signRecord", method = RequestMethod.GET)
    public JSONObject signRecord(@RequestParam(required = false) Integer current, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        Page page = new Page<>(current, size);
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionMyMoneyService.signRecord(page, result, currentLoginUser.getAppUserEntity().getId());

    }

}