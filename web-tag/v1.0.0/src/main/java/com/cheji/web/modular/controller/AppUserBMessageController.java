package com.cheji.web.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.cwork.BUserMessageDto;
import com.cheji.web.modular.domain.AppUserBMessageEntity;
import com.cheji.web.modular.service.AppUserBMessageService;
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
import java.util.List;

/**
 * <p>
 * 用户服务信息表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/appUserBMessage")
public class AppUserBMessageController extends BaseController {

    @Resource
    private AppUserBMessageService appUserBMessageService;

    //查询B端技师

    //查询喷漆技师
    @ApiOperation(value = "查询喷漆技师列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "score", value = "传1分数排序", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "Integer")

    })
    @RequestMapping(value = "/findTechnician", method = RequestMethod.GET)
    public JSONObject findTechnician(HttpServletRequest request, Integer score,@RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //查询营业中技师
        List<BUserMessageDto> bUserMessageDto = appUserBMessageService.findInWork(score,pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", bUserMessageDto);
        return result;
    }


    @ApiOperation(value = "查询维修技师详情" +
            "407：技师id有误")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "technicianId", value = "技师id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "genre", value = "全部1，好评2，中评3，差评4，有图5", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/technicianDetails", method = RequestMethod.GET)
    public JSONObject technicianDetails(HttpServletRequest request, Integer technicianId,Integer genre,@RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if (technicianId == null || technicianId == 0) {
            result.put("code", 407);
            result.put("msg", "技师id有误");
            return result;
        }

        //头像，评分，名称，服务次数，简介
        JSONObject jsonObject = appUserBMessageService.findTechnicaianDetails(technicianId, pagesize,genre);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", jsonObject);
        return result;

    }


}
