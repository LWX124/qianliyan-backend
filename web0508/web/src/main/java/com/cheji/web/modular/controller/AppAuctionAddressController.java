package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppAuctionAddressEntity;
import com.cheji.web.modular.service.AppAuctionAddressService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/address")
public class AppAuctionAddressController extends BaseController {


    @Autowired
    private AppAuctionAddressService appAuctionAddressService;

    @ApiOperation(value = "添加地址")
    @RequestMapping(value = "/addAddress", method = RequestMethod.POST)
    public JSONObject addAddress(@RequestBody AppAuctionAddressEntity address, HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionAddressService.addAddress(result, address, currentLoginUser.getAppUserEntity().getId());

    }

    @ApiOperation(value = "查询地址")
    @RequestMapping(value = "/queryAddress", method = RequestMethod.GET)
    public JSONObject queryAddress(HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionAddressService.queryAddress(result, currentLoginUser.getAppUserEntity().getId());

    }

    @ApiOperation(value = "删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "delId", value = "删除的ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/delAddress", method = RequestMethod.GET)
    public JSONObject delAddress(@RequestParam(required = false) String delId, HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionAddressService.delAddress(result, delId);
    }

    @ApiOperation(value = "城市")
    @RequestMapping(value = "/getCity", method = RequestMethod.GET)
    public JSONObject getCity() {
        return appAuctionAddressService.getCity();
    }

}