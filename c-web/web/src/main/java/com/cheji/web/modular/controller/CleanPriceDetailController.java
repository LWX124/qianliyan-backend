package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CleanMerDto;
import com.cheji.web.modular.domain.AppBUserConfigEntity;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.domain.CleanPriceDetailEntity;
import com.cheji.web.modular.service.*;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cleanPriceDetail")
public class CleanPriceDetailController extends BaseController {
    @Autowired
    public CleanPriceDetailService cleanPriceDetailService;

    @Resource
    private AppBeautyPriceDetailService appBeautyPriceDetailService;

    /**
     * 查询洗车门店
     *
     * @param cityCode
     * @param cleanType
     * @return
     */
    @ApiOperation(value = "查询洗车店铺", notes = "普洗到免费对应1到5")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityCode", value = "城市id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanType", value = "清洗类型", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findCleanMerchants", method = RequestMethod.GET)
    public JSONObject findCleanMerchants(String cityCode, @RequestParam(required = true, defaultValue = "1") String cleanType) {
        JSONObject result = new JSONObject();

        //传清洗类型，查询到所有有这个清洗类型的商户
        if (StringUtils.isEmpty(cityCode)) {
            cityCode = "028";
        }
        if (StringUtils.isEmpty(cleanType)) {
            result.put("code", 401);
            result.put("msg", "清洗类型不能为空！");
            return result;
        }
        List<String> userBs = cleanPriceDetailService.findUsersByCleanType(cleanType, Integer.valueOf(cityCode));

        //根据商户id查询到对应数据
        ArrayList<CleanMerDto> bUserEntities = new ArrayList<>();
        for (String userB : userBs) {
            CleanMerDto bUser = cleanPriceDetailService.findMerchantsByid(userB, cleanType);
            if (bUser == null) {
                continue;
            }
            bUserEntities.add(bUser);
        }
        result.put("code", 200);
        result.put("data", bUserEntities);
        return result;
    }

    /**
     * @param userBId
     * @param cleanType
     * @return
     */
    @ApiOperation(value = "查询洗车门店详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "门店id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanType", value = "清洗类型", required = true, dataType = "String")

    })
    @RequestMapping(value = "/merchantsCleanDetails", method = RequestMethod.GET)
    public JSONObject merchantsCleanDetails(String userBId, String cleanType) {
        JSONObject result = new JSONObject();
        //判断登陆

        if (StringUtils.isEmpty(userBId)) {
            result.put("code", 401);
            result.put("msg", "商户id不能为空！");
            return result;
        }

        result = cleanPriceDetailService.findCleanDetilas(userBId,result,cleanType);

        return result;
    }



    @ApiOperation(value = "查询门店详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "门店id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型1开始", required = true, dataType = "String")
    })
    @RequestMapping(value = "/merchantsPricesDetails", method = RequestMethod.GET)
    public JSONObject merchantsPricesDetails(String userBId, String type) {
        JSONObject result = new JSONObject();
        //判断登陆
        if (StringUtils.isEmpty(userBId)) {
            result.put("code", 401);
            result.put("msg", "商户id不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(type)){
            result.put("code", 401);
            result.put("msg", "type不能为空！");
            return result;
        }
        //洗车
        if (type.equals("1")){
            result = cleanPriceDetailService.findCleanDetilas(userBId, result, null);
            return result;
        }else if (type .equals("2")){
            result = appBeautyPriceDetailService.findBeautyDetails(userBId, result);
            return result;
        }

        result.put("code", 200);
        result.put("data", null);
        return result;
    }

}
