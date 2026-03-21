package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.service.AppCleanIndetService;
import com.cheji.b.modular.service.AppSprayPaintIndentService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 洗车订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2019-12-31
 */
@RestController
@RequestMapping("/appleanIndet")
public class AppCleanIndetController extends BaseController {

    @Resource
    private AppCleanIndetService appCleanIndetService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

//
//    @ApiOperation(value = "洗车等订单列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "type", value = "全部到已取消对应0-3", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/otherIndentList", method = RequestMethod.GET)
//    public JSONObject otherIndentList(HttpServletRequest request, String type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
//        JSONObject result = new JSONObject();
//        TokenPojo currentLoginUser = getCurrentLoginUser(request);
//        if (currentLoginUser == null) {
//            result.put("code", 530);
//            result.put("msg", "用户未登录");
//            return result;
//        }
//        Integer id = currentLoginUser.getAppUserEntity().getId();
//        if (StringUtils.isEmpty(type)) {
//            result.put("code", 520);
//            result.put("msg", "type不能为空");
//            return result;
//        }
//        //根据用户id查询到对应数据
//        List<AppCleanIndetEntity> cleanIndent = appCleanIndetService.findCleanIndent(id, pagesize, type);
//        for (AppCleanIndetEntity appCleanIndetEntity : cleanIndent) {
//            String indentState = appCleanIndetEntity.getIndentState();
//
//            if (appCleanIndetEntity.getResource().equals("4")) {
//                //查询
//                String cleanIndentNumber = appCleanIndetEntity.getCleanIndentNumber();
//                EntityWrapper<AppSprayPaintIndentEntity> wrapper = new EntityWrapper<>();
//                wrapper.eq("spray_paint_number", cleanIndentNumber);
//                AppSprayPaintIndentEntity appSprayPaintIndentEntity = appSprayPaintIndentService.selectOne(wrapper);
//                //喷漆，
//                if (appSprayPaintIndentEntity.getIsOffer() == 0) {
//                    appCleanIndetEntity.setIndentState("-1");           //待报价
//                } else if (indentState.equals("2")) {
//
//                    if (appSprayPaintIndentEntity.getIsOffer() == 1) {
//                        appCleanIndetEntity.setIndentState("1");
//                    } else {
//                        if (appSprayPaintIndentEntity.getPayState() == null || appSprayPaintIndentEntity.getPayState() != 1) {
//                            appCleanIndetEntity.setIndentState("-2");   //待支付
//                        } else {
//                            appCleanIndetEntity.setIndentState("1");
//                        }
//                    }
//                } else if (indentState.equals("3")) {
//                    appCleanIndetEntity.setIndentState("2");
//                } else if (indentState.equals("4")) {
//                    appCleanIndetEntity.setIndentState("3");
//                }
//            } else {
//                if (indentState.equals("2")) {
//                    appCleanIndetEntity.setIndentState("1");    //待服务
//                } else if (indentState.equals("3")) {
//                    appCleanIndetEntity.setIndentState("2");    //已完成
//                } else if (indentState.equals("4")) {
//                    appCleanIndetEntity.setIndentState("3");    //已取消
//                }
//            }
//        }
//        result.put("code", 200);
//        result.put("msg", "成功");
//        result.put("data", cleanIndent);
//        return result;
//    }


    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cleanIndentNumber", value = "订单编号", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/indentDetails", method = RequestMethod.GET)
    public JSONObject indentDetails(HttpServletRequest request, String cleanIndentNumber) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (StringUtils.isEmpty(cleanIndentNumber)) {
            result.put("code", 411);
            result.put("msg", "订单编号不能为空");
            return result;
        }
        //根据订单编号查询到数据
        AppCleanIndetEntity appCleanIndet = appCleanIndetService.findCleanIndentDetails(cleanIndentNumber);
        if (appCleanIndet == null) {
            result.put("code", 412);
            result.put("msg", "未查询到该订单，请稍后再试");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", appCleanIndet);
        return result;
    }


    @ApiOperation(value = "洗美列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.全部，2.进行中，3待评价, 4.取消 5.过期", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/cleanIndentList", method = RequestMethod.GET)
    public JSONObject cleanIndentList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        if (type == 1) {
            type = null;
        }
        List<AppCleanIndetEntity> cleanIndent = appCleanIndetService.newCleanIndentList(id, pagesize, type);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", cleanIndent);
        return result;
    }


}
