package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.mapper.AppAuctionMapper;
import com.cheji.web.modular.service.AppAuctionWxPayService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auction/wxPay")
@Slf4j
public class AppAuctionWxPayController extends BaseController {


    @Resource
    private AppAuctionWxPayService appAuctionWxPayService;
    @Resource
    private AppAuctionMapper appAuctionMapper;


    @ApiOperation(value = "vip充值" + "401：参数错误")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "vipLv", value = "vip充值等级", required = true, dataType = "String"),})
    @RequestMapping(value = "/createVipOrder", method = RequestMethod.GET)
    public JSONObject createVipOrder(@RequestParam String vipLv, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        return appAuctionWxPayService.createVipOrder(result, vipLv, httpServletRequest, currentLoginUser);

    }

    @ApiOperation(value = "一口价支付  返回201 表示vip用户不需要支付，直接生成订单")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "车辆Id", required = true, dataType = "Long"),})
    @PostMapping(value = "/createOnePriceOrder")
    public JSONObject createOnePriceOrder(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (in.getLong("carId") == null) {
            result.put("code", 401);
            result.put("msg", "车辆id错误");
            return result;
        }

        return appAuctionWxPayService.createOnePriceOrder(currentLoginUser, in.getLong("carId"), httpServletRequest);
    }

    @ApiOperation(value = "一口价订单查询支付回调状态" + "401：参数错误" + "530：用户未登录" + "402：根据预支付id没有找到对应订单" + "403：支付失败" + "200：支付成功" + "201：用户支付中，稍后重新查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "outTradeNo", value = "订单号", required = true, dataType = "string"),})
    @PostMapping("/queryOnePriceOrderStatus")
    public JSONObject queryOnePriceOrderStatus(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String outTradeNo = in.getString("outTradeNo");
        return appAuctionWxPayService.queryOnePriceOrderStatus(result, outTradeNo);
    }

    @ApiOperation(value = "保证金充值" + "401：参数错误")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "车辆id", required = true, dataType = "Long"),})
    @RequestMapping(value = "/createBailOrder", method = RequestMethod.GET)
    public JSONObject createBailOrder(@RequestParam Long carId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        return appAuctionWxPayService.createBailOrder(result, carId, httpServletRequest, currentLoginUser);

    }

    @ResponseBody
    @RequestMapping(value = "/payLogNotify", method = RequestMethod.POST, produces = "application/json")
    public String wxpayNotify(HttpServletRequest request) throws Exception {
        return appAuctionWxPayService.wxpayNotify(request);
    }

    @ResponseBody
    @RequestMapping(value = "/bailLogNotify", method = RequestMethod.POST, produces = "application/json")
    public String wxpayBaiNotify(HttpServletRequest request) throws Exception {
        return appAuctionWxPayService.wxpayBaiNotify(request);
    }

    //一口价订单支付回调通知
    @ResponseBody
    @RequestMapping(value = "/onePriceNotify", method = RequestMethod.POST, produces = "application/json")
    public String onePriceNotify(HttpServletRequest request) throws Exception {
        return appAuctionWxPayService.onePriceNotify(request);
    }

    @ApiOperation(value = "统一下单，查询订单状态" + "401：参数错误" + "530：用户未登录" + "402：根据预支付id没有找到对应订单" + "403：支付失败" + "200：支付成功" + "201：用户支付中，稍后重新查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "outTradeNo", value = "订单号", required = true, dataType = "string"),})
    @PostMapping("/queryOrderStatus")
    public JSONObject queryOrderStatus(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String outTradeNo = in.getString("outTradeNo");
        return appAuctionWxPayService.queryOrderStatus(result, outTradeNo);
    }

    @ApiOperation(value = "统一下单，查询订单状态" + "401：参数错误" + "530：用户未登录" + "402：根据预支付id没有找到对应订单" + "403：支付失败" + "200：支付成功" + "201：用户支付中，稍后重新查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "outTradeNo", value = "订单号", required = true, dataType = "string"),})
    @PostMapping("/queryBailOrderStatus")
    public JSONObject queryBailOrderStatus(@RequestBody JSONObject in, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        String outTradeNo = in.getString("outTradeNo");
        return appAuctionWxPayService.queryBailOrderStatus(result, outTradeNo);
    }

    //甲乙两方反悔扣保证金需要在出价表中删除相关出价记录 @todo

    @ApiOperation(value = "获取支付金额")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "vipLv", value = "vip充值等级", required = true, dataType = "String"),})
    @RequestMapping(value = "/getPayAmount", method = RequestMethod.GET)
    public JSONObject getPayAmount(@RequestParam String vipLv, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionWxPayService.getPayAmount(result, vipLv, currentLoginUser.getAppUserEntity().getId());
    }

    @ApiOperation(value = "获取信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "carId", value = "参拍车辆ID", required = true, dataType = "String"),

    })
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public JSONObject getInfo(@RequestParam Long carId, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);

        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        return appAuctionWxPayService.getInfo(carId, currentLoginUser.getAppUserEntity().getId());
    }
}