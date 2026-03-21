package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.CleanMerDto;
import com.cheji.web.modular.domain.AppBUserConfigEntity;
import com.cheji.web.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.web.modular.domain.CleanIndetEntity;
import com.cheji.web.modular.service.AppBUserConfigService;
import com.cheji.web.modular.service.AppBeautyPriceDetailService;
import com.cheji.web.modular.service.BUserService;
import com.cheji.web.modular.service.CleanIndetService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
@RestController
@RequestMapping("/beautyPriceDetail")
public class AppBeautyPriceDetailController extends BaseController {


    @Resource
    private AppBeautyPriceDetailService appBeautyPriceDetailService;



    @ApiOperation(value = "查询美容店铺", notes = "美容类型对应1到10")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityCode", value = "城市id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beautyType", value = "清洗类型", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findBeautyMerchants", method = RequestMethod.GET)
    public JSONObject findBeautyMerchants(String cityCode, @RequestParam(required = true, defaultValue = "1") String beautyType) {
        JSONObject result = new JSONObject();

        if (StringUtils.isEmpty(cityCode)) {
            cityCode = "028";
        }
        if (StringUtils.isEmpty(beautyType)) {
            result.put("code", 401);
            result.put("msg", "美容类型不能为空！");
            return result;
        }
        List<String> userBs = appBeautyPriceDetailService.findUserByBeauty(beautyType, Integer.valueOf(cityCode));
        ArrayList<CleanMerDto> cleanMerDtos = new ArrayList<>();
        for (String userB : userBs) {
            CleanMerDto cleanMerDto = appBeautyPriceDetailService.findMerchantsById(userB, beautyType);
            if (cleanMerDto==null){
                continue;
            }
            cleanMerDtos.add(cleanMerDto);
        }
        result.put("code", 200);
        result.put("data", cleanMerDtos);
        return result;
    }


    @ApiOperation(value = "查询美容门店详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userBId", value = "商户id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/beautyMerchantsDetails", method = RequestMethod.GET)
    public JSONObject beautyMerchantsDetails(String userBId) {
        JSONObject result = new JSONObject();
        //判断登陆
        if (StringUtils.isEmpty(userBId)){
            result.put("code", 401);
            result.put("msg", "商户id不能为空！");
            return result;
        }
        result = appBeautyPriceDetailService.findBeautyDetails(userBId, result);
        return result;
    }


}
