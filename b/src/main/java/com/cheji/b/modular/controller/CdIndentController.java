package com.cheji.b.modular.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.b.modular.domain.BizInsuranceCompanyEntity;
import com.cheji.b.modular.domain.CdCarEntity;
import com.cheji.b.modular.domain.CdImgEntity;
import com.cheji.b.modular.domain.CdIndentEntity;
import com.cheji.b.modular.dto.CdCarDto;
import com.cheji.b.modular.service.BizInsuranceCompanyService;
import com.cheji.b.modular.service.CdCarService;
import com.cheji.b.modular.service.CdImgService;
import com.cheji.b.modular.service.CdIndentService;
import com.cheji.b.pojo.TokenPojo;
import com.cheji.b.utils.GenerateDigitalUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车电订单表 前端控制器
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/cdIndent")
public class CdIndentController extends BaseController {

    @Resource
    private CdIndentService cdIndentService;

    @Resource
    private CdImgService cdImgService;

    @Resource
    private CdCarService cdCarService;

    @Resource
    private BizInsuranceCompanyService bizInsuranceCompanyService;

    @ApiOperation(value = "接车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "plate", value = "车牌号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "车辆型号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plateImg", value = "车牌照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "frameImg", value = "车架照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "maintPlan", value = "维修方案", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carIns", value = "车辆保险", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businesSource", value = "业务来源", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "维修时间", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "customerRebates", value = "客户返点", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "businessCommission", value = "业务佣金", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "rescueCost", value = "施救费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "车主嘱咐", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "lossPhotos", value = "损失照片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "claimInformation", value = "理赔资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "isInsert", value = "是否确定录入", required = true, dataType = "String"),
    })
    @PostMapping("/pickCar")
    public JSONObject register(@RequestBody JSONObject in) {

        JSONObject result = new JSONObject();

        //判断登陆

        String plate = in.getString("plate");
        String brandId = in.getString("brandId");
        String plateImg = in.getString("plateImg");
        String frameImg = in.getString("frameImg");
        String username = in.getString("username");
        String phone = in.getString("phone");
        String maintPlan = in.getString("maintPlan");
        String carIns = in.getString("carIns");
        String businesSource = in.getString("businesSource");
        String remark = in.getString("remark");
        if (StringUtils.isEmpty(plate) || StringUtils.isEmpty(brandId)
                || StringUtils.isEmpty(maintPlan)) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }
        //查询一周之内是否录过
        CdIndentEntity cdinde = cdIndentService.selectIsRepaid(plate);
        String isInsert = in.getString("isInsert");
        if (cdinde != null && StringUtils.isEmpty(isInsert)) {
            result.put("code", 411);
            result.put("msg", "近一周内已经收录，确定重复录入吗");
            return result;
        }


        Integer workTime = in.getInteger("workTime");
        BigDecimal customerRebates = in.getBigDecimal("customerRebates");
        BigDecimal businessCommission = in.getBigDecimal("businessCommission");
        BigDecimal rescueCost = in.getBigDecimal("rescueCost");

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray lossPhotosList = (JSONArray) json.get("lossPhotos");
//        if (lossPhotosList == null) {
//            result.put("code", 407);
//            result.put("msg", "损失照片不能为空");
//            return result;
//        }


        JSONArray claimInformationList = (JSONArray) json.get("claimInformation");
//        if (claimInformationList == null) {
//            result.put("code", 407);
//            result.put("msg", "理赔资料不能为空");
//            return result;
//        }


        CdIndentEntity cd = new CdIndentEntity();
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "CD" + orderNo;
        cd.setIndentNumber(orderNo);
        cd.setPlate(plate);
        cd.setBrandId(Integer.parseInt(brandId));
        cd.setPlateImg(plateImg);
        cd.setFrameImg(frameImg);
        cd.setUsername(username);
        cd.setPhone(phone);
        cd.setMaintPlan(maintPlan);
        cd.setCarIns(carIns);
        cd.setBusinesSource(businesSource);
        cd.setWorkTime(workTime);
        cd.setRemark(remark);
        cd.setCustomerRebates(customerRebates);
        cd.setBusinessCommission(businessCommission);
        cd.setRescueCost(rescueCost);
        cd.setCreateTime(new Date());
        cdIndentService.insert(cd);

        //保存损失照片
        if (lossPhotosList != null) {
            List<String> lossPhotos = JSONArray.parseArray(lossPhotosList.toString(), String.class);
            for (int i = 0; i < lossPhotos.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(1);
                cdImgEntity.setImg(lossPhotos.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(cd.getId());
                cdImgService.insert(cdImgEntity);
            }
        }

        //保存理赔资料
        if (claimInformationList != null) {
            List<String> claimInformation = JSONArray.parseArray(claimInformationList.toString(), String.class);
            for (int i = 0; i < claimInformation.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(2);
                cdImgEntity.setImg(claimInformation.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(cd.getId());
                cdImgService.insert(cdImgEntity);
            }
        }

        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }


    @ApiOperation(value = "查询保险公司")
    @GetMapping("/insuranceCompany")
    public JSONObject insuranceCompany() {
        //查询到所有的合作保险
        EntityWrapper<BizInsuranceCompanyEntity> bizInsuranceWrapper = new EntityWrapper<>();
        List<BizInsuranceCompanyEntity> bizInsuranceCompanyList = bizInsuranceCompanyService.selectList(bizInsuranceWrapper);
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("data", bizInsuranceCompanyList);
        return result;

    }


    @ApiOperation(value = "修改订单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plate", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "车辆型号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "plateImg", value = "车牌照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "frameImg", value = "车架照片", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户姓名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "maintPlan", value = "维修方案", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "carIns", value = "车辆保险", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businesSource", value = "业务来源", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "维修时间", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "repairMoney", value = "维修金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "accessoriesAmount", value = "配件金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "customerRebates", value = "客户返点", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "customerMoney", value = "客户返点金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "businessCommission", value = "业务佣金", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "businessMoney", value = "业务佣金金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "waixiuAmount", value = "外修金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "qitaAmount", value = "其他金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "feeRebates", value = "定损返点", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "feeMoney", value = "定损费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "waiRebates", value = "外定比例", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "waiAmount", value = "外定金额", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "payment", value = "财务收款", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "rescueCost", value = "施救费用", required = true, dataType = "decimal"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "车主嘱咐", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "financialPayment", value = "财务收款", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "lossPhotos", value = "损失照片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "accessoriesPhoto", value = "配件清单照片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "claimInformation", value = "理赔资料", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "amountAccessImg", value = "配件金额图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "customerPic", value = "客户返点图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "businessPic", value = "业务佣金图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "lossPic", value = "定损费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "waiPic", value = "外定费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "qitaPic", value = "其他费用图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "partsListImg", value = "配件清单图片", required = true, dataType = "String[]"),
            @ApiImplicitParam(paramType = "query", name = "changeListImg", value = "换件清单图片", required = true, dataType = "String[]"),
    })
    @PostMapping("/updateIndent")
    public JSONObject updateIndent(HttpServletRequest request, @RequestBody JSONObject in) {
        //修改数据
        TokenPojo currentLoginUser = getCdCurrentLoginUser(request);
        if (currentLoginUser == null) {
            JSONObject result = new JSONObject();
            result.put("code", 530);
            result.put("msg", "用户未登录");
            return result;
        }
        return cdIndentService.updateIntent(in, currentLoginUser.getCdUserEntity().getId());
    }


    @ApiOperation(value = "车辆配件列表")
    @GetMapping("/vehicleParts")
    public JSONObject vehicleParts() {

        JSONObject result = new JSONObject();

        //查询配件列表
        List<CdCarDto> list = cdCarService.findCarVehicle();

        result.put("code", 200);
        result.put("data", list);
        return result;
    }


    //新增配件项目
    @ApiOperation(value = "新增配件项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "配件名字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "配件父id", required = true, dataType = "Integer"),
    })
    @PostMapping("/addProduct")
    public JSONObject addProduct(@RequestBody JSONObject in) {
        return cdCarService.addProduct(in);
    }


    @ApiOperation(value = "保存配件价格")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "配件id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "配件名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "commodityPrice", value = "商品售价", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "litFixPrice", value = "修复价格小", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "minFixPrice", value = "修复价格中", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "bigFixPrice", value = "修复价格大", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "mountingPriceOne", value = "拆装价格单", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "mountingPriceAll", value = "拆装价格全", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "halfSprayPrice", value = "喷漆价格中", required = true, dataType = "decmial"),
            @ApiImplicitParam(paramType = "query", name = "allSprayPrice", value = "喷漆价格全", required = true, dataType = "decmial"),
    })
    @PostMapping("/accessories")
    public JSONObject accessories(@RequestBody JSONObject in) {
        return cdCarService.saveAccess(in);
    }


    //订单列表
    @ApiOperation(value = "订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型，1.新订单，2.报价中。3.采配件，4.维修中，5.交车中，6.已收款，7.报费用，8.毛利润", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "text", value = "搜索文字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer"),
//            @ApiImplicitParam(paramType = "query", name = "status", value = "登录终端", required = true, dataType = "Integer"),
    })
    @PostMapping("/indentList")
    public JSONObject indentList(@RequestBody JSONObject in) {
//        JSONObject jsonObject = new JSONObject();
//        if(in.getString("status") == null){
//            jsonObject.put("msg","状态不能为空!");
//            return jsonObject;
//        }
//        if (String.valueOf(2).equals(in.getString("status"))){
//            jsonObject.put("msg","没有权限!");
//            return jsonObject;
//        }
        return cdIndentService.indentList(in);
    }


    //订单列表
    @ApiOperation(value = "数据统计")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "time", value = "年月：2022-06", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页数", required = true, dataType = "Integer"),
    })
    @PostMapping("/dataStatis")
    public JSONObject dataStatis(@RequestBody JSONObject in) {
        String time = in.getString("time");
        String type = in.getString("type");
        Integer pagesize = in.getInteger("pagesize");
        if (StringUtils.isEmpty(time) || pagesize == null || StringUtils.isEmpty(type)) {
            JSONObject object = new JSONObject();
            object.put("code", 407);
            object.put("msg", "time或pagesize或type为空");
            return object;
        }
        return cdIndentService.dataStatis(time,pagesize,type);
    }

    //订单列表
    @ApiOperation(value = "订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "token", value = "token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
    })
    @PostMapping("/indentDetails")
    public JSONObject indentDetails(@RequestBody JSONObject in) {
        return cdIndentService.indentDetails(in);
    }


    //订单列表
    @ApiOperation(value = "已完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
    })
    @PostMapping("/finishIndent")
    public JSONObject finishIndent(@RequestBody JSONObject in) {
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("mesg", "订单id为空");
            return result;
        }
        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);

        if (cdIndentEntity == null) {
            result.put("code", 407);
            result.put("mesg", "没有查询到订单");
            return result;
        }
        cdIndentEntity.setState(8);
        cdIndentService.updateById(cdIndentEntity);
        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }

    @ApiOperation(value = "删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "indentId", value = "订单id", required = true, dataType = "Integer"),
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
        Integer indentId = in.getInteger("indentId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("mesg", "订单id为空");
            return result;
        }
        CdIndentEntity cdIndentEntity = cdIndentService.selectById(indentId);

        if (cdIndentEntity == null) {
            result.put("code", 407);
            result.put("mesg", "没有查询到订单");
            return result;
        }
        if (id != 4 && id != 8 && id != 9) {
            result.put("code", 407);
            result.put("msg", "没有修改权限");
            return result;
        }
        cdIndentService.deleteById(indentId);
        result.put("code", 200);
        result.put("mesg", "成功");
        return result;
    }


    @ApiOperation(value = "理赔人员")
    @ApiImplicitParams({
    })
    @PostMapping("/lipei")
    public JSONObject lipei() {
        return cdIndentService.lipei();
    }


}
















