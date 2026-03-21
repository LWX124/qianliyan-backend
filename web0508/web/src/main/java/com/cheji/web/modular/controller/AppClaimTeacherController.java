package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.cheji.web.modular.service.AppClaimTeacherService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 理赔老师表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/appClaimTeacher")
public class AppClaimTeacherController extends BaseController {


    @Resource
    private AppClaimTeacherService appClaimTeacherService;


    //查询商户接口
    @ApiOperation(value = "事故信息账单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "页数", value = "pagesize", required = true, dataType = "String")
    })
    @RequestMapping(value = "/findAccidMessBill", method = RequestMethod.GET)
    public JSONObject findAccidMessBill(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {

        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();

        //根据id查询到对应扣费数据
        JSONArray list = appClaimTeacherService.findAccidMessBill(id, pagesize);

        result.put("code", 200);
        result.put("data", list);
        return result;
    }


    //查询理赔老师信息
    @ApiOperation(value = "事故信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "年份", value = "year", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "月份", value = "month", required = true, dataType = "String")
    })
    @RequestMapping(value = "/ClaimTeaMessage", method = RequestMethod.GET)
    public JSONObject ClaimTeaMessage(HttpServletRequest request, String year, String month) {
        //查询到服务顾问各种信息
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        Integer id = currentLoginUser.getAppUserEntity().getId();

        if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
            result.put("code", 407);
            result.put("msg", "年月数据为空");
            return result;
        }

        if (month.length() == 1) {
            month = "0" + month;
        }

        //根据id查询到对应扣费数据
        JSONObject list = appClaimTeacherService.findClaimTeaMessage(id, year, month);

        result.put("code", 200);
        result.put("data", list);
        return result;

    }


}
