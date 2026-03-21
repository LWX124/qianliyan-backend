package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONObject;
import com.cheji.b.modular.service.CdPartsDetailsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 配件明细 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2022-01-19
 */
@RestController
@RequestMapping("cdPartsDetails")
public class CdPartsDetailsController extends BaseController {

    @Resource
    private CdPartsDetailsService cdPartsDetailsService;

    //保存配件明细
    @ApiOperation(value = "保存配件明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "配件类型 1.换配件，2.拆装，3.修复，4.喷漆", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "price", value = "价格", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "partsId", value = "配件id", required = true, dataType = "Integer"),
    })
    @PostMapping("/partsDetails")
    public JSONObject partsDetails(@RequestBody JSONObject in) {
        return cdPartsDetailsService.savePartsDetails(in);
    }







    @ApiOperation(value = "报价详情页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1.全部，2.配件，3.拆装，4.修复，5.喷漆", required = true, dataType = "Integer"),
    })
    @PostMapping("/offerDeatils")
    public JSONObject offerDeatils(@RequestBody JSONObject in) {
        Integer indentId = in.getInteger("indentId");
        Integer type = in.getInteger("type");
        return cdPartsDetailsService.offerDetails(indentId, type);
    }




    @ApiOperation(value = "采购配件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "idList", value = "选择的配件id", required = true, dataType = "String[]"),
    })
    @PostMapping("/buyAcc")
    public JSONObject buyAcc(@RequestBody JSONObject in) {
        return cdPartsDetailsService.buyAcc(in);
    }







    //    删除配件明细表
    @ApiOperation(value = "删除配件明细表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "Integer"),
    })
    @PostMapping("/deleParts")
    public JSONObject deleParts(@RequestBody JSONObject in) {
        return cdPartsDetailsService.deleParts(in);
    }







    //配件采购详情列表
    @ApiOperation(value = "配件采购详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
    })
    @PostMapping("/procurement")
    public JSONObject procurement(@RequestBody JSONObject in) {
        return cdPartsDetailsService.addProcurement(in);
    }







    //工单页面，保存工单
    @ApiOperation(value = "保存工单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "sheetMetal", value = "钣金人员", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "工单类型 1.钣金，2.机修，3.油漆", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "workPrice", value = "钣金工时", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "维修时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "picking", value = "领料", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "replenishment", value = "补货", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "sales", value = "退货", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "changeJob", value = "转工", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "imgList", value = "维修照片", required = true, dataType = "String[]"),

    })
    @PostMapping("/saveWorkOrder")
    public JSONObject saveWorkOrder(@RequestBody JSONObject in) {
        return cdPartsDetailsService.saveWorkOrder(in);
    }





    //工单详情
    @ApiOperation(value = "查询工单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "工单类型 1.钣金，2.机修，3.油漆", required = true, dataType = "Integer"),

    })
    @PostMapping("/workOrDetails")
    public JSONObject workOrDetails(@RequestBody JSONObject in) {
        return cdPartsDetailsService.workOrDetails(in);
    }



    //工单详情
    @ApiOperation(value = "查询配件价格")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "配件id", required = true, dataType = "Integer"),
    })
    @GetMapping("/priceDetails")
    public JSONObject priceDetails(Integer id) {
        return cdPartsDetailsService.findPriceDetails(id);
    }



}
