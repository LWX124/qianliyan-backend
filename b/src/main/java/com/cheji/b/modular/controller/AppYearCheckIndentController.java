package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppYearCheckImgEntity;
import com.cheji.b.modular.domain.AppYearCheckIndentEntity;
import com.cheji.b.modular.service.AppYearCheckImgService;
import com.cheji.b.modular.service.AppYearCheckIndentService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 年检订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
@RestController
@RequestMapping("/appYearCheckIndent")
public class AppYearCheckIndentController extends BaseController {
    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppYearCheckImgService appYearCheckImgService;

    //查询年检订单详情
    @ApiOperation(value = "年检详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/yearCheckDetails", method = RequestMethod.GET)
    public JSONObject yearCheckDetails(String cleanIndentNumber) {
        JSONObject result = new JSONObject();
        //根据订单编号查询订单详情
        EntityWrapper<AppYearCheckIndentEntity> yearCheckWrapper = new EntityWrapper<>();
        yearCheckWrapper.eq("year_check_number", cleanIndentNumber);
        AppYearCheckIndentEntity one = appYearCheckIndentService.selectOne(yearCheckWrapper);
        if (one == null) {
            result.put("code", 402);
            result.put("msg", "检查订单编号");
            return result;
        }

        EntityWrapper<AppYearCheckImgEntity> imgWarpper = new EntityWrapper<>();
        imgWarpper.eq("app_year_check_id", one.getId());
        List<AppYearCheckImgEntity> appYearCheckImgs = appYearCheckImgService.selectList(imgWarpper);

        JSONObject in = new JSONObject();
        in.put("licensePlate", one.getLicensePlate());
        in.put("username", one.getUsername());
        in.put("phone", one.getPhone());
        in.put("pickAddress", one.getPickAddress());
        in.put("lng", one.getLng());
        in.put("lat", one.getLat());
        in.put("yearCheckType", one.getYearCheckType());
        in.put("state", one.getState());
        in.put("payState", one.getPayState());
        in.put("imgList", appYearCheckImgs);
        // in.put("confirmCar",one.getConfirmCar());

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", in);
        return result;

    }


    @ApiOperation(value = "年检列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "0.全部，1.新订单，2.进行中，3 待评价, 4.取消", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/yearCheckIndentList", method = RequestMethod.GET)
    public JSONObject yearCheckIndentList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser((request));
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        if (type == 0) {
            type = null;
        }
        List<AppCleanIndetEntity> yearCheckIndentList = appYearCheckIndentService.newYearCheckIndentList(userBId, type, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", yearCheckIndentList);
        return result;
    }


    @ApiOperation(value = "去接车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/confirmCar ", method = RequestMethod.GET)
    public JSONObject confirmCar(String cleanIndentNumber, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //修改订单状态
        EntityWrapper<AppYearCheckIndentEntity> yearCheckWrapper = new EntityWrapper<>();
        yearCheckWrapper.eq("year_check_number", cleanIndentNumber);
        AppYearCheckIndentEntity one = appYearCheckIndentService.selectOne(yearCheckWrapper);
        if (one == null) {
            result.put("code", 407);
            result.put("msg", "检查订单编号");
            return result;
        }
        //确认接车
        if (one.getState() != 1) {
            result.put("code", 407);
            result.put("msg", "不能重复操作");
            return result;
        }
        if (one.getYearCheckType() == 1) {
            result.put("code", 407);
            result.put("msg", "免检代办没有接车");
            return result;
        }
        one.setState(2);
        appYearCheckIndentService.updateById(one);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;

    }


}
