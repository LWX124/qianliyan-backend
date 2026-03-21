package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.cwork.BillList;
import com.cheji.web.modular.service.PushBillService;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/pushBill")
public class PushBillController extends BaseController {
    @Resource
    public PushBillService pushBillService;


    //账单列表
    @ApiOperation(value = "账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间", required = false, dataType = "String")
    })
    @RequestMapping(value = "billList", method = RequestMethod.GET)
    public JSONObject billList(HttpServletRequest request, String date) {
        JSONObject result = new JSONObject();
        //判断登陆
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getAppUserEntity().getId();
        //根据用户id查询到账单列表
        List<BillList> billListByid = pushBillService.findBillListByid(String.valueOf(id), date);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", billListByid);
        return result;
    }

}
