package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppRescueIndentEntity;
import com.cheji.b.modular.service.AppRescueIndentService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 救援表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2020-03-05
 */
@RestController
@RequestMapping("/appRescueIndent")
public class AppRescueIndentController extends BaseController {

    @Resource
    private AppRescueIndentService appRescueIndentService;


    @ApiOperation(value = "救援详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "rescueNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/rescueDetails", method = RequestMethod.GET)
    public JSONObject rescueDetails(String rescueNumber) {
        JSONObject result = new JSONObject();
        EntityWrapper<AppRescueIndentEntity> warpper = new EntityWrapper<>();
        warpper.eq("rescue_number", rescueNumber);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(warpper);
        if (appRescueIndentEntity == null) {
            result.put("code", 408);
            result.put("msg", "检查订单编号");
            return result;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state",appRescueIndentEntity.getState());
        //场景，类型
        jsonObject.put("type", appRescueIndentEntity.getType());
        jsonObject.put("rescueScene", appRescueIndentEntity.getRescueScene());
        //车牌
        jsonObject.put("licensePlate", appRescueIndentEntity.getLicensePlate());
        //联系人名称，
        jsonObject.put("rescueName", appRescueIndentEntity.getRescueName());
        //手机号
        jsonObject.put("phoneNumber", appRescueIndentEntity.getPhoneNumber());
        //紧急联系人电话
        jsonObject.put("emergencyNumber", appRescueIndentEntity.getEmergencyNumber());
        //订单金额
        jsonObject.put("price", appRescueIndentEntity.getPrice().multiply(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP)));
        //备注
        jsonObject.put("remark", appRescueIndentEntity.getRemark());
        //救援经纬度
        jsonObject.put("lng", appRescueIndentEntity.getLng());
        jsonObject.put("lat", appRescueIndentEntity.getLat());
        //目的地经纬度
        jsonObject.put("inlng", appRescueIndentEntity.getInlng());
        jsonObject.put("inlat", appRescueIndentEntity.getInlat());
        //救援地址
        jsonObject.put("currentPosition", appRescueIndentEntity.getCurrentPosition());
        //拖车目的地
        jsonObject.put("destination", appRescueIndentEntity.getDestination());
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", jsonObject);
        return result;
    }


    /**
     * @param request
     * @param type
     * @param pagesize
     * @return
     */
    @ApiOperation(value = "救援列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.全部,2.新订单，3.进行中，4，待评价, 5，已完成，6.取消 ", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "String")
    })
    @RequestMapping(value = "/rescueIndentList", method = RequestMethod.GET)
    public JSONObject rescueIndentList(HttpServletRequest request, Integer type, @RequestParam(required = false, defaultValue = "1") Integer pagesize) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        if (type==null){
            result.put("code", 407);
            result.put("msg", "type为空");
            return result;
        }

        switch (type){
            case 1:type=null;break;
            case 2:type=1;break;
            case 3:type=2;break;
            case 4:type=3;break;
            case 5:type=5;break;
            case 6:type=4;break;
        }
        List<AppCleanIndetEntity> rescueIndentList = appRescueIndentService.newRescueIndentList(userBId, type, pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", rescueIndentList);
        return result;
    }

    @ApiOperation(value = "去现场")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "rescueNumber", value = "订单编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/goToScene", method = RequestMethod.GET)
    public JSONObject goToScene(String rescueNumber) {
        JSONObject result = new JSONObject();
        //修改状态
        EntityWrapper<AppRescueIndentEntity> warpper = new EntityWrapper<>();
        warpper.eq("rescue_number", rescueNumber);
        AppRescueIndentEntity appRescueIndentEntity = appRescueIndentService.selectOne(warpper);
        if (appRescueIndentEntity == null) {
            result.put("code", 408);
            result.put("msg", "检查订单编号");
            return result;
        }
        if (appRescueIndentEntity.getState() != 1) {
            result.put("code", 408);
            result.put("msg", "订单状态不能修改");
            return result;
        } else {
            appRescueIndentEntity.setState(2);
        }
        appRescueIndentService.updateById(appRescueIndentEntity);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }

}
