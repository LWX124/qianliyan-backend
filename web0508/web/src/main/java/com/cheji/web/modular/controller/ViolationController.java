package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppUserCarInfoEntity;
import com.cheji.web.modular.mapper.AppUserCarInfoMapper;
import com.cheji.web.modular.service.ViolationServer;
import com.cheji.web.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 事故
 */
@RestController
@RequestMapping("/violation")
public class ViolationController extends BaseController {

    @Resource
    private ViolationServer violationServer;

    @Resource
    private AppUserCarInfoMapper appUserCarInfoMapper;

    //违章查询
    @ApiOperation(value = "违章查询" +
            "count    位置条数" +
            "degree   扣分" +
            "time     违章时间" +
            "address  违章地址" +
            "reason   罚款原因" +
            "money    罚款金额")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "carCode", value = "车架号（大于等于6位）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carEngineCode", value = "发动机号码（大于等于6位）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carNumber", value = "车牌", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "smsFlag", value = "发送短信提醒标记(1：提醒)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1 普通轿车，2 新能源汽车", required = true, dataType = "String")
    })
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public JSONObject indentMes(HttpServletRequest request, String carCode, String carEngineCode, String carNumber, int type, int smsFlag) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        if (type == 0) {
            result.put("code", 401);
            result.put("msg", "类型必传");
            return result;
        }

        if (StringUtils.isEmpty(carCode) || carCode.length() < 6) {
            result.put("code", 401);
            result.put("msg", "车架号不能小于6位");
            return result;
        }

        if (StringUtils.isEmpty(carEngineCode) || carEngineCode.length() < 6) {
            result.put("code", 401);
            result.put("msg", "发动机号不能小于6位");
            return result;
        }

        if (StringUtils.isEmpty(carNumber)) {
            result.put("code", 401);
            result.put("msg", "车牌不能为空");
            return result;
        }

        String s = violationServer.queryViolation(carCode, carEngineCode, carNumber, type);
        JSONObject object = JSONObject.parseObject(s);
        Boolean flag = object.getJSONObject("showapi_res_body").getBoolean("flag");
        if (flag != null && flag) {
            //查询成功 记录用户车辆信息
            AppUserCarInfoEntity entity = new AppUserCarInfoEntity();
            entity.setCarCode(carCode);
            entity.setCarEngineCode(carEngineCode);
            entity.setCarNumber(carNumber);
            entity.setType(type);
            entity.setSmsFlag(smsFlag);
            entity.setUserId(currentLoginUser.getAppUserEntity().getId());
            entity.setCreateTime(new Date());
            AppUserCarInfoEntity resultEntity = appUserCarInfoMapper.selectByCarNumber(entity);
            if (resultEntity == null) {
                appUserCarInfoMapper.insert(entity);
            }
        }

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", JSONObject.parseObject(s));
        return result;
    }

    @ApiOperation(value = "查询用户车辆信息")
    @RequestMapping(value = "/queryUserCar", method = RequestMethod.GET)
    public JSONObject queryUserCar(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        AppUserCarInfoEntity appUserCarInfoEntity = appUserCarInfoMapper.selectByUserId(currentLoginUser.getAppUserEntity().getId());

        result.put("code", 200);
        result.put("msg", appUserCarInfoEntity);
        return result;
    }
}
