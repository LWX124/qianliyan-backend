package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.dto.AppDrivingCardDTO;
import com.cheji.web.pojo.TokenPojo;
import com.cheji.web.util.IDCardUtil;
import com.cheji.web.util.OCRUtil;
import com.cheji.web.util.VinUtil;
import com.huaweicloud.sdk.ocr.v1.model.BusinessLicenseResult;
import com.huaweicloud.sdk.ocr.v1.model.IdCardResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/identify")
public class AppAuctionIdentifyController extends BaseController {


    @ApiOperation(value = "行驶证识别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "drivingObsUrl", value = "行驶证OBS地址", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/checkDriving", method = RequestMethod.GET)
    public JSONObject checkDriving(@RequestParam(required = false)String drivingObsUrl, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if(drivingObsUrl != null){
            try {
                AppDrivingCardDTO appDrivingCardEntity = OCRUtil.checkDrivingByUrl(drivingObsUrl);
                result.put("code", 200);
                result.put("msg", "成功");
                result.put("data", appDrivingCardEntity);
                return result;
            } catch (ParseException e) {
                e.printStackTrace();
                result.put("code", 403);
                result.put("msg", e.getMessage());
                return result;
            }
        }else {
            result.put("code", 405);
            result.put("msg", "请填写地址");
            return result;
        }

    }

    @ApiOperation(value = "身份证识别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "idcardObsUrl", value = "身份证OBS地址", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/checkIDCard", method = RequestMethod.GET)
    public JSONObject checkIDCard(@RequestParam(required = false)String idcardObsUrl,@RequestParam(required = false)String idcardObs64,HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if(idcardObsUrl != null){
            try {
                IdCardResult idCardResult = IDCardUtil.checkIDCardByUrl(idcardObsUrl);
                result.put("code", 200);
                result.put("msg", "成功");
                result.put("data", idCardResult);
                return result;
            } catch (Exception e){
                result.put("code", 403);
                result.put("msg", "失败");
                return result;
            }
        } else {
            result.put("code", 405);
            result.put("msg", "请填写地址");
            return result;
        }

    }

    @ApiOperation(value = "营业执照识别")
    @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "charterObsUrl", value = "营业执照OBS地址", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/checkCharter", method = RequestMethod.GET)
    public JSONObject checkCharter(@RequestParam(required = false)String charterObsUrl,HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if(charterObsUrl != null){
            try {
                BusinessLicenseResult businessLicenseResult = OCRUtil.checkCharter(charterObsUrl);
                result.put("code", 200);
                result.put("msg", "成功");
                result.put("data", businessLicenseResult);
                return result;
            } catch (Exception e){
                result.put("code", 403);
                result.put("msg", "失败");
                return result;
            }
        }else {
            result.put("code", 200);
            result.put("msg", "请传入url");
            result.put("data", null);
            return result;
        }

    }

    @ApiOperation(value = "车架号识别")
    @ApiImplicitParams({
                @ApiImplicitParam(paramType = "query", name = "vin", value = "车架号", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getVin", method = RequestMethod.GET)
    public JSONObject getVin(@RequestParam(required = false)String vin,HttpServletRequest request) {
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser == null) {
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        if(vin != null){
            try {
                JSONObject vinResult = VinUtil.getVin(vin);
                result.put("code", 200);
                result.put("msg", "成功");
                result.put("data", vinResult);
                return result;
            } catch (Exception e){
                result.put("code", 403);
                result.put("msg", "失败");
                return result;
            }
        }else {
            result.put("code", 200);
            result.put("msg", "vin");
            result.put("data", null);
            return result;
        }

    }



}