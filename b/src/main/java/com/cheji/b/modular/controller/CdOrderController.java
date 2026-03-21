package com.cheji.b.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.CdIndentEntity;
import com.cheji.b.modular.domain.CdOrderEntity;
import com.cheji.b.modular.service.CdOrderService;
import com.cheji.b.pojo.TokenPojo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cdOrder")
public class CdOrderController extends BaseController{

    @Autowired
    private CdOrderService cdOrderService;

    @ApiOperation(value = "接事故车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "plate", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "车辆型号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lossImg", value = "损失照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentTime", value = "事故时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentLiability", value = "事故责任", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businesSource", value = "业务来源", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "订单状态", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "accidentType", value = "事故类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "clientWill", value = "客户意向", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "情况备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carIns", value = "保险公司", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chesunCoverage", value = "车损保额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "estimatedAmount", value = "预估维修", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "treatmentCost", value = "处理成本", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carPrices", value = "购车价格", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "usedPrices", value = "二手车价", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "salvagePrices", value = "残值价格", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "costUpkeep", value = "维修成本", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carProfits", value = "购车利润", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carDataImg", value = "车辆资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "contractAwardImg", value = "合同签约", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "authorizationImg", value = "授权书", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "letterCommitmentImg", value = "承诺书", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "claimSettlementImg", value = "理赔资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "carmodel", value = "车款支付", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carModelImg", value = "车款支付图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "finalPaymentImg", value = "尾款支付图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "referralFeeImg", value = "介绍费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "truckingFeeImg", value = "拖车费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "handingExpenseImg", value = "处理费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "otherExpenseImg", value = "其他费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "modelsAccountImg", value = "车款到账图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "finalPayment", value = "尾款支付", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "referralFee", value = "介绍费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "truckingFee", value = "拖车费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "handingExpense", value = "处理费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "otherExpense", value = "其他费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "modelsAccount", value = "车款到账", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "isInsert", value = "是否确定录入", required = true, dataType = "String"),
    })
    @PostMapping("/pickCar")
    public JSONObject pickCar(@RequestBody JSONObject in) {
       return cdOrderService.pickCar(in);
    }

    //订单详情
    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
    })
    @PostMapping("/orderDetails")
    public JSONObject orderDetails(@RequestBody JSONObject in) {
        return cdOrderService.orderDetails(in);
    }

    //订单列表
    @ApiOperation(value = "订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型，1.新订单，2.意向修。3.意向卖，4.待签车，5.已签车，6.处理中，7.已收款，8.毛利润,9.已处理", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "text", value = "搜索文字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "time", value = "时间", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "status", value = "登录终端", required = true, dataType = "Integer"),
    })
    @PostMapping("/orderList")
    public JSONObject orderList(@RequestBody JSONObject in) {
        return cdOrderService.orderList(in);
    }

    @ApiOperation(value = "修改订单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plate", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "车辆型号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lossImg", value = "损失照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentTime", value = "事故时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accidentLiability", value = "事故责任", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businesSource", value = "业务来源", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "state", value = "订单状态", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "accidentType", value = "事故类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "clientWill", value = "客户意向", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "情况备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carIns", value = "保险公司", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contractStatus", value = "合同状态", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "finishOrder", value = "订单完成", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chesunCoverage", value = "车损保额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "estimatedAmount", value = "预估维修", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "treatmentCost", value = "处理成本", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carPrices", value = "购车价格", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "usedPrices", value = "二手车价", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "salvagePrices", value = "残值价格", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "costUpkeep", value = "维修成本", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carProfits", value = "购车利润", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "carDataImg", value = "车辆资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "contractAwardImg", value = "合同签约", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "authorizationImg", value = "授权书", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "letterCommitmentImg", value = "承诺书", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "claimSettlementImg", value = "理赔资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "carModelImg", value = "车款支付", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "finalPayment", value = "尾款支付", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "referralFee", value = "介绍费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "truckingFee", value = "拖车费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "handingExpense", value = "处理费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "otherExpense", value = "其他费用", required = true, dataType = "decimal"),
    })
    @PostMapping("/updateOrder")
    public JSONObject updateOrder(HttpServletRequest request, @RequestBody JSONObject in) {
        //修改数据
        TokenPojo currentLoginUser = getCdCurrentLoginUser(request);
        if (currentLoginUser == null) {
            JSONObject result = new JSONObject();
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return cdOrderService.updateOrder(in, currentLoginUser.getCdUserEntity().getId());
    }

    @ApiOperation(value = "删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true, dataType = "Integer"),
    })
    @PostMapping("/deletIndent")
    public JSONObject deletIndent(HttpServletRequest request, @RequestBody JSONObject in) {

        TokenPojo currentLoginUser = getCdCurrentLoginUser(request);
        if (currentLoginUser == null) {
            JSONObject result = new JSONObject();
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        Integer id = currentLoginUser.getCdUserEntity().getId();
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("orderId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("mesg", "订单id为空");
            return result;
        }
        CdOrderEntity cdOrder = cdOrderService.selectById(indentId);

        if (cdOrder == null) {
            result.put("code", 407);
            result.put("mesg", "没有查询到订单");
            return result;
        }
        if (id != 4 && id != 8) {
            result.put("code", 407);
            result.put("msg", "没有修改权限");
            return result;
        }
        cdOrderService.deleteById(indentId);
        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }
}
