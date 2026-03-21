package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppAuctionWarnCarService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/warnCar")
public class AppAuctionWarnCarController extends BaseController {

    @Autowired
    private AppAuctionWarnCarService appAuctionWarnCarService;

    @ApiOperation(value = "提醒车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车ID", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/addWarnCar", method = RequestMethod.GET)
    public JSONObject addWarnCar(Long carId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionWarnCarService.addWarnCar(result,id,carId);
    }

    @ApiOperation(value = "取消提醒车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车ID", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/delWarnCar", method = RequestMethod.GET)
    public JSONObject delWarnCar(Long carId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionWarnCarService.delWarnCar(result,id,carId);
    }

    @ApiOperation(value = "提醒车列表")
    @RequestMapping(value = "/queryWarnCar", method = RequestMethod.GET)
    public JSONObject queryWarnCar(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionWarnCarService.queryWarnCar(result,id);
    }
}