package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;
import com.cheji.web.modular.service.AppAuctionVipLvService;
import com.cheji.web.pojo.TokenPojo;
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
@RequestMapping("/auctionVip")
public class AppAuctionVipLvController extends BaseController {

    @Autowired
    private AppAuctionVipLvService appAuctionVipLvService;

    @ApiOperation(value = "vip说明" +
            "201: 不是vip")
    @RequestMapping(value = "/vipExplain", method = RequestMethod.GET)
    public JSONObject vipExplain(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionVipLvService.vipExplain(currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "vip相关List")
    @RequestMapping(value = "/vipInfoList", method = RequestMethod.GET)
    public JSONObject vipInfoList() {
        return appAuctionVipLvService.vipInfoList();
    }


//    @ApiOperation(value = "vip设置")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "id", value = "无id添加,有就修改", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "lv", value = "会员等级", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "lvName", value = "会员名称", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "amount", value = "金额", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "timeOut", value = "开通的月份", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "openExplain", value = "开通说明", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "closeExplain", value = "保证金结算说明", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "imgList", value = "保证金结算说明", required = true, dataType = "String"),
//    })
//    @RequestMapping(value = "/vipSet", method = RequestMethod.POST)
//    public JSONObject vipSet(@RequestBody AppAuctionVipLvEntity vip, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        Integer id = currentLoginUser.getAppUserEntity().getId();
//        if(id != 54 && id != 5374 && id != 102){
//            result.put("code", 555);
//            result.put("msg", "无权访问!");
//            return result;
//        }
//
//        return appAuctionVipLvService.vipSet(vip);
//    }

//    @ApiOperation(value = "开通vip")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "vipLv", value = "开通等级", required = true, dataType = "String"),
//    })
//    @RequestMapping(value = "/openVip", method = RequestMethod.GET)
//    public JSONObject openVip(@RequestBody(required = false) String vipLv, HttpServletRequest request) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        return appAuctionVipLvService.openVip(vipLv,currentLoginUser.getAppUserEntity().getId());
//    }


}