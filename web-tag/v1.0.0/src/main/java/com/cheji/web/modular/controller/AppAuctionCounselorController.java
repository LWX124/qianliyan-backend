package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppAuctionCounselorService;
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
@RequestMapping("/counselor")
public class AppAuctionCounselorController extends BaseController {

    @Autowired
    private AppAuctionCounselorService appAuctionCounselorService;


    @ApiOperation(value = "删除顾问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "counselorId", value = "删除的ID", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/delCounselor", method = RequestMethod.GET)
    public JSONObject delCounselor(@RequestParam Long counselorId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionCounselorService.delCounselor(result,counselorId);
    }

    @ApiOperation(value = "查询顾问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "carId", value = "车辆Id", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/queryCounselor", method = RequestMethod.GET)
    public JSONObject queryCounselor(@RequestParam(required = false) Long carId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
//        return appAuctionCounselorService.queryCounselor(result,carId);
        return null;
    }




}