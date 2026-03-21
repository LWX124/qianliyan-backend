package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppAuctionWithdrawCashEntity;
import com.cheji.web.modular.service.AppAuctionWithdrawCashService;
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
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/auctionWithdrawCash")
public class AppAuctionWithdrawCashController extends BaseController {

    @Autowired
    private AppAuctionWithdrawCashService appAuctionWithdrawCashService;

    @ApiOperation(value = "提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userBankId", value = "银行卡id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "提现金额(元)", required = true, dataType = "decimal")
    })
    @RequestMapping(value = "/applyFor", method = RequestMethod.POST)
    public JSONObject applyFor(@RequestBody AppAuctionWithdrawCashEntity withdrawCash, HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if(Objects.isNull(withdrawCash)){
            result.put("code", 531);
            result.put("msg", "参数错误");
            return result;
        }

        withdrawCash.setCreateTime(new Date());
        withdrawCash.setUserId(Long.valueOf(currentLoginUser.getAppUserEntity().getId()));
        return appAuctionWithdrawCashService.addApplyFor(result,withdrawCash);
    }
    @ApiOperation(value = "取消提现申请")
    @RequestMapping(value = "/cancelApplyFor", method = RequestMethod.POST)
    public JSONObject cancelApplyFor( HttpServletRequest httpServletRequest) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return appAuctionWithdrawCashService.cancelApplyFor(result,currentLoginUser.getAppUserEntity());
    }

}