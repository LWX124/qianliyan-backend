package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.dto.ListDetailsDto;
import com.cheji.b.modular.dto.UpdateDto;
import com.cheji.b.modular.service.AccidentRecordService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/accidentRecord")
public class AccidentRecordController extends BaseController{
    @Autowired
    public AccidentRecordService accidentRecordService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String listDetails(){
        return "1111111";
    }

    @ApiOperation(value = "事故详情" +
            "-1,待处理.0,真现场.1.信息晚.2.已撤离.3.假现场.4.非事故")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "accid",value = "事故id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "source",value = "来源",required = true,dataType = "String")
    })
    @RequestMapping(value = "/listDetails",method = RequestMethod.GET)
    public JSONObject listDetails(HttpServletRequest request,String accid,String source){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null){
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        ListDetailsDto detailsDto = accidentRecordService.findAccidentDetails(accid,source);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", detailsDto);
        return result;
    }

/*-1,待处理.0,真现场.1.信息晚.2.已撤离.3.假现场.4.非事故*/
    @ApiOperation(value = "修改事故信息状态" +
            "0,真现场.1.信息晚.2.已撤离.3.假现场.4.非事故")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType ="query",name ="accid",value ="事故id",required = true, dataType ="String" ),
            @ApiImplicitParam(paramType ="query",name ="realness",value ="事故真实性",required = true, dataType ="int" ),
            @ApiImplicitParam(paramType ="query",name ="source",value ="来源",required = true, dataType ="String" )
    })
    @RequestMapping(value = "/updateReal",method = RequestMethod.GET)
    public JSONObject updateReal(String accid,Integer realness,String source){
        JSONObject result = new JSONObject();
        accidentRecordService.updateRealness(accid,realness,source);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }


    @ApiOperation(value = "更新B")
    @RequestMapping(value = "/updateB",method = RequestMethod.GET)
    public JSONObject updateB(){
        JSONObject result = new JSONObject();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setDescribe("检测到新版本,请直接点击更新按钮更新。卸载可能会丢失数据噢！");
        updateDto.setUrl("https://apps.apple.com/cn/app/%E8%BD%A6%E5%B7%B1%E5%95%86%E6%88%B7/id1485695460");
        result.put("code",200);
        result.put("msg","成功" );
        result.put("data", updateDto);
        return result;
    }
}
