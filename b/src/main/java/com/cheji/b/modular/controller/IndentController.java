package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.domain.AppJgPushEntity;
import com.cheji.b.modular.domain.IndentEntity;
import com.cheji.b.modular.dto.EarningListDto;
import com.cheji.b.modular.dto.IndentDetailsDto;
import com.cheji.b.modular.dto.IndentListDto;
import com.cheji.b.modular.service.AppJgPushService;
import com.cheji.b.modular.service.IndentService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/indent")
public class IndentController extends BaseController{
    @Autowired
    public IndentService indentService;

    @Resource
    private AppJgPushService appJgPushService;


    @ApiOperation(value = "今日收入")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "pagesize",value = "页数",required = false,dataType = "int")
    })
    @RequestMapping(value = "/todayEarning",method = RequestMethod.GET)
    public JSONObject todayEarning(HttpServletRequest request,@RequestParam(required = false,defaultValue = "1")Integer pagesize){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null){
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //查询到今日收入
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        List<EarningListDto> earningListDtoList = indentService.findTodayEarning(userBId,pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", earningListDtoList);
        return result;
    }

    @ApiOperation(value = "有效订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String")
    })
    @RequestMapping(value = "/effectiveOrder",method = RequestMethod.GET)
    public JSONObject effectiveOrder(HttpServletRequest request,@RequestParam(required = false,defaultValue = "1")Integer pagesize){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null){
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取到商户code
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        List<EarningListDto> earningListDtoList = indentService.findEffectiveOrder(userBId,pagesize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", earningListDtoList);
        return result;
    }

    @ApiOperation(value = "我的订单" +
            "全部:type:0" +
            "新订单:type:1" +
            "未到店:type:2" +
            "服务中:type:3" +
            "已交车:type:4" +
            "已结算:type:5")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "type",value = "类型",required = true,dataType = "int"),
            @ApiImplicitParam(paramType = "query",name = "searchText",value = "搜索内容",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "pageSize",value = "页数",required = false,dataType = "int")

    })
    @RequestMapping(value = "/myIndent",method = RequestMethod.GET)
    public JSONObject myIndent(HttpServletRequest request,Integer type,String searchText,@RequestParam(required = false,defaultValue = "1")Integer pageSize){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null){
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        //获取商户id
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        //根据商户id查询到订单信息
        List<IndentListDto> indentListDtos = indentService.findMesg(userBId,type,searchText,pageSize);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", indentListDtos);
        return result;
    }

    @ApiOperation(value = "消费扣款")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "token",value = "token",required = true,dataType = "String")
    })
    @RequestMapping(value = "/consumptionDeductions",method = RequestMethod.GET)
    public JSONObject consumptionDeductions(HttpServletRequest request,@RequestParam(required = false,defaultValue = "1")Integer pageSize){
        JSONObject result = new JSONObject();
        TokenPojo currentLoginUser = getCurrentLoginUser(request);
        if (currentLoginUser==null){
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }

        //获取商户id
        Integer userBId = currentLoginUser.getAppUserEntity().getId();
        List<EarningListDto> consumptionDuductions = indentService.findConsumptionDeductions(userBId,pageSize);

        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", consumptionDuductions);
        return result;
    }

    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "indentId",value = "订单id",required = true,dataType = "int")
    })
    @RequestMapping(value = "/indentDetails",method = RequestMethod.GET)
    public JSONObject indentDetails (Integer indentId){
        JSONObject result = new JSONObject();
        //根据订单id查询到数据
        IndentDetailsDto detailsDto =  indentService.findDetailsById(indentId);
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", detailsDto);
        return result;
    }


    @ApiOperation(value = "修改订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "indentId",value = "订单id",required = true,dataType = "int"),
            @ApiImplicitParam(paramType = "query",name = "state",value = "状态",required = true,dataType = "int"),
            @ApiImplicitParam(paramType = "query",name = "settleAccounts",value = "维修金额",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "commissionRate",value = "佣金比例",required = false,dataType = "String")
    })
    @RequestMapping(value = "/updateIndent",method = RequestMethod.GET)
    public JSONObject updateIndent(Integer indentId,Integer state,String settleAccounts,String commissionRate){
        JSONObject result = new JSONObject();
        //根据订单id修改订单状态  状态不能为空
        indentService.updateIndent(indentId,state,settleAccounts,commissionRate);
        //添加极光推送数据
        /*       /*private String source;		// 来源（C端，B端）
	private String ispush;		// 是否推送（0，没有，1，有）
	private String type;		// 操作类型()
	private String userId;		// C端用户id
	private String userBId;		// B端用户id
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间*/
//        IndentEntity indentEntity = indentService.selectById(indentId);
//        AppJgPushEntity appJgPushEntity = new AppJgPushEntity();
//        appJgPushEntity.setSource("C");
//        appJgPushEntity.setIspush("0");
//        appJgPushEntity.setType("5");
//        appJgPushEntity.setUserId(indentEntity.getUserId().toString());
//        appJgPushEntity.setCreateTime(new Date());
//        appJgPushEntity.setUpdateTime(new Date());
//        appJgPushService.insert(appJgPushEntity);
        result.put("code", 200);
        result.put("msg", "成功");
        return result;
    }






}
