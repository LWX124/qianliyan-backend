package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.modular.domain.AppAuctionBidEntity;
import com.cheji.web.modular.domain.AppAuctionVipControlEntity;
import com.cheji.web.modular.service.AppAuctionBidService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/auctionBid")
public class AppAuctionBidController extends BaseController {

    @Autowired
    private AppAuctionBidService appAuctionBidService;

    @ApiOperation(value = "记录出价")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车ID", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "bid", value = "出价", required = true, dataType = "String"), @ApiImplicitParam(paramType = "query", name = "endTimes", value = "距离结束的秒数", required = true, dataType = "Integer"),})
    @RequestMapping(value = "/bidLog", method = RequestMethod.POST)
    public JSONObject bidLog(@RequestBody @Valid AppAuctionBidEntity appAuctionBidEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (appAuctionBidEntity.getBid() == null || appAuctionBidEntity.getBid().compareTo(new BigDecimal(0)) < 1) {
            result.put("code", 531);
            result.put("msg", "出价金额必须大于0");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        return appAuctionBidService.bidLog(id, appAuctionBidEntity);
    }

    @ApiOperation(value = "出价列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车ID", required = true, dataType = "Long"),})
    @RequestMapping(value = "/bidRecord", method = RequestMethod.GET)
    public JSONObject bidRecord(@RequestParam(required = true) Long carId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
        return appAuctionBidService.bidRecord(result, carId);
    }

    @ApiOperation(value = "一口价")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "拍卖车ID", required = true, dataType = "Long"),})
    @RequestMapping(value = "/fixedPrice", method = RequestMethod.POST)
    public JSONObject fixedPrice(@RequestBody AppAuctionBidEntity appAuctionBidEntity, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionBidService.fixedPrice(result, Long.valueOf(appAuctionBidEntity.getCarId()), currentLoginUser.getAppUserEntity().getId());
    }
}