package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.PushBillEntity;
import com.cheji.b.modular.service.PushBillService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/pushBill")
public class PushBillController extends BaseController {
    @Autowired
    public PushBillService pushBillService;

    @ApiOperation(value = "账单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int")

    })
    @RequestMapping(value = "/billList", method = RequestMethod.GET)
    public JSONObject billList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到用户id
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<PushBillEntity> billList = pushBillService.findBillList(id, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", billList);
        return result;
    }

    @ApiOperation(value = "账单筛选接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间", required = true, dataType = "String")
    })
    @RequestMapping(value = "/screenBill", method = RequestMethod.GET)
    public JSONObject screenBill(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") Integer pagesize, String date) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到用户id
        Integer id = currentLoginUser.getAppUserEntity().getId();
        List<PushBillEntity> billList = pushBillService.findScreenBill(id, pagesize, date);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", billList);
        return result;
    }




}
