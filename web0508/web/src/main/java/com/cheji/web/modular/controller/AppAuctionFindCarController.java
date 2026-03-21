package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppAuctionFindCarEntity;
import com.cheji.web.modular.service.AppAuctionFindCarService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 寻车
 */
@RestController
@RequestMapping("/findcar")
public class AppAuctionFindCarController extends BaseController {


    @Autowired
    private AppAuctionFindCarService addFindCar;

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/addFindCar", method = RequestMethod.POST)
    public JSONObject addFindCar(@RequestBody AppAuctionFindCarEntity findCar, HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return addFindCar.addFindCar(result, findCar, currentLoginUser.getAppUserEntity().getId());

    }

    @ApiOperation(value = "查询信息")
    @RequestMapping(value = "/queryFindCar", method = RequestMethod.GET)
    public JSONObject queryFindCar(HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return addFindCar.queryFindCar(result, currentLoginUser.getAppUserEntity().getId());

    }

    @ApiOperation(value = "删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "delId", value = "删除的ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/delFindCar", method = RequestMethod.GET)
    public JSONObject delFindCar(@RequestParam(required = false) String delId, HttpServletRequest httpServletRequest){
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return addFindCar.delFindCar(result, delId);
    }

}