package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppSendOutSheetService;
import com.cheji.web.pojo.TokenPojo;
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

@RestController
@RequestMapping("/appSendSheet")
public class AppSendSheetController extends BaseController {

    @Resource
    private AppSendOutSheetService appSendOutSheetService;

    //事故服务列表
    @ApiOperation(value = "事故列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "state", value = "事故状态", required = true, dataType = "int")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JSONObject list(HttpServletRequest request, Integer state, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        //查询列表
        JSONObject result = new JSONObject();

        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();


        JSONObject object = appSendOutSheetService.findSendSheentList(id,state,pagesize);

        result.put("code", 200);
        result.put("data", object);
        return result;

    }


    @ApiOperation(value = "事故服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "事故id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "cid", value = "事故cid", required = true, dataType = "String")
    })
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public JSONObject details(/*HttpServletRequest request*/Integer id,String cid) {
        //查询列表
        JSONObject result = new JSONObject();
        if (id==null){
            result.put("code", 407);
            result.put("msg", "id为空");
            return result;
        }
        JSONObject object = appSendOutSheetService.findAccidentDetails(id,cid);

        result.put("code", 200);
        result.put("data", object);
        return result;
    }


//    @ApiOperation(value = "事故服务评论详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "cid", value = "事故cid", required = true, dataType = "String")
//    })
//    @RequestMapping(value = "/evaluationDetails", method = RequestMethod.GET)
//    public JSONObject evaluationDetails(/*HttpServletRequest request*/String cid) {
//
//        JSONObject result = new JSONObject();
//        //根据车辆订单id查询
//        //carmessageId, 查询
//        if (cid==null){
//            result.put("code", 407);
//            result.put("mesg", "cid为空");
//            return result;
//        }
//
//        JSONObject object = appSendOutSheetService.findEvaluationDetails(cid);
//        result.put("code", 200);
//        result.put("data", object);
//        return result;
//
//    }



    @ApiOperation(value = "评价")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cid", value = "事故cid", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "source", value = "评价分数（1-5）", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "suit", value = "是否正装（0否，1是）", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public JSONObject evaluation(/*HttpServletRequest request*/String cid,Integer source,Integer suit) {
        JSONObject result = new JSONObject();

        if (StringUtils.isEmpty(cid)){
            result.put("code", 407);
            result.put("msg", "cid为空");
            return result;
        }

        appSendOutSheetService.updateMessageCar(cid,source,suit);

        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }




}
