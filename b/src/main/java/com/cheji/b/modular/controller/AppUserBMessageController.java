package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.service.AppUserBMessageService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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


    @ApiOperation(value = "首页服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1:推修，2：洗美，3：喷漆，4：救援，5：年检", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/serviceCenter", method = RequestMethod.GET)
    public JSONObject pushRepairCenter(HttpServletRequest request, Integer type) {
        //判断登陆
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();

        JSONObject json = appUserBMessageService.selectDetils(userBId, type);
        Integer code = json.getInteger("code");
        if (code != null) {
            return json;
        } else {
            result.put("code", 200);
            result.put("msg", "成功");
            result.put("data", json);
        }
        return result;
    }


    //修改营业状态    Modify business status
    @ApiOperation(value = "修改营业状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1:推修，2：洗美，3：喷漆，4：救援，5：年检", required = true, dataType = "Integer")

    })
    @RequestMapping(value = "/modifyBussiness", method = RequestMethod.GET)
    public JSONObject modifyBussiness(HttpServletRequest request, Integer type) {
        //判断登陆
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        JSONObject js = appUserBMessageService.updatamodifyBusssiness(userBId, type);
        Integer code = js.getInteger("code");
        if (code != 200) {
            return js;
        }else {
            result.put("code", 200);
            result.put("msg", "成功");
        }
        return result;
    }

}
