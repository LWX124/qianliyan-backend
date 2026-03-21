package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppSubUsualAddressEntity;
import com.cheji.web.modular.service.AppSubUsualAddressService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 常用地址 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-06-10
 */
@RestController
@RequestMapping("/appSubUsualAddress")
public class AppSubUsualAddressController extends BaseController {


    @Resource
    private AppSubUsualAddressService appSubUsualAddressService;


    @ApiOperation(value = "保存常用地址" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "point", value = "地点", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址(详情)", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "lng", value = "经度", required = true, dataType = "decimal "),
            @ApiImplicitParam(paramType = "query", name = "lat", value = "纬度", required = true, dataType = "decimal "),
            @ApiImplicitParam(paramType = "query", name = "username", value = "名称", required = true, dataType = "String "),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", required = true, dataType = "String "),

    })
    @RequestMapping(value = "/saveAddress", method = RequestMethod.POST)
    public JSONObject saveAddress(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();

        //保存数据
        String point = jsonObject.getString("point");
        String address = jsonObject.getString("address");
        String username = jsonObject.getString("username");
        String phone = jsonObject.getString("phone");
        BigDecimal lng = jsonObject.getBigDecimal("lng");
        BigDecimal lat = jsonObject.getBigDecimal("lat");

        if (StringUtils.isEmpty(point) || StringUtils.isEmpty(address) || StringUtils.isEmpty(username) || StringUtils.isEmpty(phone)) {
            result.put("code", 405);
            result.put("msg", "有参数为空");
            return result;
        }
        if (lng == null || lat == null) {
            result.put("code", 405);
            result.put("msg", "经纬度参数为空");
            return result;
        }

        AppSubUsualAddressEntity appSubUsualAddressEntity = new AppSubUsualAddressEntity();
        appSubUsualAddressEntity.setAddress(address);
        appSubUsualAddressEntity.setPhone(phone);
        appSubUsualAddressEntity.setPoint(point);
        appSubUsualAddressEntity.setUsername(username);
        appSubUsualAddressEntity.setUserId(userid);
        appSubUsualAddressEntity.setCreateTime(new Date());
        appSubUsualAddressEntity.setUpdateTime(new Date());
        appSubUsualAddressEntity.setLng(lng);
        appSubUsualAddressEntity.setLat(lat);
        appSubUsualAddressService.insert(appSubUsualAddressEntity);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", appSubUsualAddressEntity);
        return result;
    }


    //历史地址，常用地址
    @ApiOperation(value = "查询历史地址和常用地址" +
            "405：参数有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "地址类型(1.历史地址。2.常用地址)", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String "),
    })
    @RequestMapping(value = "/inquireAddress", method = RequestMethod.GET)
    public JSONObject inquireAddress(Integer type, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userid = currentLoginUser.getAppUserEntity().getId();
        //查询数据
        List<AppSubUsualAddressEntity> addressEntity;
        if (type == 1) {
            //查询历史数据
            addressEntity = appSubUsualAddressService.selectHistoryAddress(userid);
        } else if (type == 2) {
            //查询常用地址
            addressEntity = appSubUsualAddressService.selectUsualAddress(userid);
        } else {
            result.put("code", 405);
            result.put("msg", "检查type");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", addressEntity);
        return result;
    }


}
