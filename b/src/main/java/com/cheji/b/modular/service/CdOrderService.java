package com.cheji.b.modular.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.CarBrandEntity;
import com.cheji.b.modular.domain.CdImgEntity;
import com.cheji.b.modular.domain.CdOrderEntity;
import com.cheji.b.modular.mapper.CdOrderMapper;
import com.cheji.b.utils.GenerateDigitalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车电订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@Service
public class CdOrderService extends ServiceImpl<CdOrderMapper, CdOrderEntity> implements IService<CdOrderEntity> {

    @Autowired
    private CdImgService cdImgService;

    @Autowired
    private CarBrandService carBrandService;

    @Resource
    private CdOrderMapper cdOrderMapper;

    @Transactional
    public JSONObject pickCar( JSONObject in) {
        JSONObject result = new JSONObject();

        String plate = in.getString("plate");
        String brandId = in.getString("brandId");
        String username = in.getString("username");
        String phone = in.getString("phone");
        Date accidentTime = in.getDate("accidentTime");
        String accidentLiability = in.getString("accidentLiability");
        String businesSource = in.getString("businesSource");
        Integer accidentType = in.getInteger("accidentType");
        Integer state = in.getInteger("state");
        String clientWill = in.getString("clientWill");
        String remark = in.getString("remark");
        String carIns = in.getString("carIns");

        BigDecimal chesunCoverage = in.getBigDecimal("chesunCoverage");
        BigDecimal estimatedAmount = in.getBigDecimal("estimatedAmount");
        BigDecimal treatmentCost = in.getBigDecimal("treatmentCost");
        BigDecimal carPrices = in.getBigDecimal("carPrices");
        BigDecimal usedPrices = in.getBigDecimal("usedPrices");
        BigDecimal salvagePrices = in.getBigDecimal("salvagePrices");
        BigDecimal costUpkeep = in.getBigDecimal("costUpkeep");
        BigDecimal carProfits = in.getBigDecimal("carProfits");

        BigDecimal carmodel = in.getBigDecimal("carmodel");
        BigDecimal finalPayment = in.getBigDecimal("finalPayment");
        BigDecimal referralFee = in.getBigDecimal("referralFee");
        BigDecimal truckingFee = in.getBigDecimal("truckingFee");
        BigDecimal handingExpense = in.getBigDecimal("handingExpense");
        BigDecimal otherExpense = in.getBigDecimal("otherExpense");
        BigDecimal modelsAccount = in.getBigDecimal("modelsAccount");

        if (StringUtils.isEmpty(plate) || StringUtils.isEmpty(brandId)) {
            result.put("code", 407);
            result.put("msg", "参数不能为空");
            return result;
        }

        //查询一周之内是否录过
        CdOrderEntity cdinde = cdOrderMapper.selectIsRepaid(plate);
        String isInsert = in.getString("isInsert");
        if (cdinde != null && StringUtils.isEmpty(isInsert)) {
            result.put("code", 411);
            result.put("msg", "近一周内已经收录，确定重复录入吗");
            return result;
        }

        CdOrderEntity cd = new CdOrderEntity();
        String orderNo = GenerateDigitalUtil.getOrderNo();
        orderNo = "CD" + orderNo; // 订单编号
        cd.setIndentNumber(orderNo);
        cd.setPlate(plate);
        cd.setBrandId(Integer.parseInt(brandId));
        cd.setUsername(username);
        cd.setPhone(phone);
        cd.setAccidentTime(accidentTime);
        cd.setAccidentLiability(accidentLiability);
        cd.setBusinesSource(businesSource);
        cd.setState(state);
        if(StringUtils.isNotBlank(clientWill) && clientWill.contains("已")){
            cd.setState(9);
        }
        cd.setAccidentType(accidentType);
        cd.setClientWill(clientWill);
        cd.setRemark(remark);
        cd.setCarIns(carIns);
        cd.setChesunCoverage(chesunCoverage);
        cd.setEstimatedAmount(estimatedAmount);
        cd.setTreatmentCost(treatmentCost);
        cd.setCarPrices(carPrices);
        cd.setUsedPrices(usedPrices);
        cd.setSalvagePrices(salvagePrices);
        cd.setCostUpkeep(costUpkeep);
        cd.setCarProfits(carProfits);
        cd.setCreateTime(new Date());
        insert(cd);

        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray lossImgList = (JSONArray) json.get("lossImg");
        JSONArray carDataImgList = (JSONArray) json.get("carDataImg");
        JSONArray contractAwardImgList = (JSONArray) json.get("contractAwardImg");
        JSONArray authorizationImgList = (JSONArray) json.get("authorizationImg");
        JSONArray letterCommitmentImgList = (JSONArray) json.get("letterCommitmentImg");
        JSONArray claimSettlementImgList = (JSONArray) json.get("claimSettlementImg");

        JSONArray carModelImgList = (JSONArray) json.get("carModelImg");
        JSONArray finalPaymentImgList = (JSONArray) json.get("finalPaymentImg");
        JSONArray referralFeeImgList = (JSONArray) json.get("referralFeeImg");
        JSONArray truckingFeeImgList = (JSONArray) json.get("truckingFeeImg");
        JSONArray handingExpenseImgList = (JSONArray) json.get("handingExpenseImg");
        JSONArray otherExpenseImgList = (JSONArray) json.get("otherExpenseImg");
        JSONArray modelsAccountImgList = (JSONArray) json.get("modelsAccountImg");


        /**
         * 14.车辆资料,15.合同签约,16.授权书,17.承诺书,18.理赔资料,19.车款支付
         * 20.尾款支付,21.介绍费用,22.拖车费用,23.处理费用,13其他费用
         */
        //保存损失照片
        if (lossImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(lossImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(1);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        //保存理车辆资料14
        if (carDataImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(carDataImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(14);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        //保存合同签约15
        if (contractAwardImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(contractAwardImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(15);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        //保存授权书16
        if (authorizationImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(authorizationImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(16);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        //保存承诺书17
        if (letterCommitmentImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(letterCommitmentImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(17);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        //保存理赔资料18
        if (claimSettlementImgList != null) {
            List<String> tempImgList = JSONArray.parseArray(claimSettlementImgList.toString(), String.class);
            int tempIndex = 0;
            for (int i = 0; i < tempImgList.size(); i++) {
                //保存图片
                if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(18);
                    cdImgEntity.setImg(tempImgList.get(i));
                    cdImgEntity.setIndex(tempIndex += 1);
                    cdImgEntity.setIndentId(cd.getId());
                    cdImgService.insert(cdImgEntity);
                }
            }
        }

        if(null != carmodel){
            //保存车款支付19
            if (carModelImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(carModelImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(19);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setCarmodel(carmodel);
        }

        if(null != finalPayment){
            //尾款支付20
            if (finalPaymentImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(finalPaymentImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(20);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setFinalPayment(finalPayment);
        }

        if(null != referralFee){
            //介绍费用,21
            if (referralFeeImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(referralFeeImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(21);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setReferralFee(referralFee);
        }

        if(null != truckingFee){
            //拖车费用22
            if (truckingFeeImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(truckingFeeImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(22);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setTruckingFee(truckingFee);
        }

        if(null != handingExpense){
            //处理费用23
            if (handingExpenseImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(handingExpenseImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(23);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setHandingExpense(handingExpense);
        }

        if(null != otherExpense){
            //其他费用13
            if (otherExpenseImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(otherExpenseImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(13);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setOtherExpense(otherExpense);
        }

        if(null != modelsAccount){
            //车款到账24
            if (modelsAccountImgList != null) {
                List<String> tempImgList = JSONArray.parseArray(modelsAccountImgList.toString(), String.class);
                int tempIndex = 0;
                for (int i = 0; i < tempImgList.size(); i++) {
                    //保存图片
                    if(tempImgList.get(i) != null && tempImgList.get(i) != "null"){
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(24);
                        cdImgEntity.setImg(tempImgList.get(i));
                        cdImgEntity.setIndex(tempIndex += 1);
                        cdImgEntity.setIndentId(cd.getId());
                        cdImgService.insert(cdImgEntity);
                    }
                }
            } else {
                result.put("code", 409);
                result.put("msg", "请添加图片！");
                return result;
            }
            cd.setOtherExpense(otherExpense);
        }

        result.put("code", 200);
        result.put("mesg", "成功");
        result.put("data", cd.getId());
        return result;
    }

    //订单详情
    public JSONObject orderDetails(JSONObject in) {
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("msg", "indentId为空");
            return result;
        }
        JSONObject object = new JSONObject();
        CdOrderEntity cd = this.selectById(indentId);

        object.put("indentNumber",cd.getIndentNumber());
        object.put("plate",cd.getPlate());
        Integer brandId = cd.getBrandId();
        CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
        object.put("brand",carBrandEntity.getName());
        object.put("username",cd.getUsername());
        object.put("phone",cd.getPhone());
        object.put("accidentTime",cd.getAccidentTime());
        object.put("accidentLiability",cd.getAccidentLiability());
        object.put("businesSource",cd.getBusinesSource());
        object.put("state",cd.getState());
        object.put("accidentType",cd.getAccidentType());
        object.put("clientWill",cd.getClientWill());
        object.put("remark",cd.getRemark());
        object.put("carIns",cd.getCarIns());
        object.put("chesunCoverage",cd.getChesunCoverage());
        object.put("estimatedAmount",cd.getEstimatedAmount());
        object.put("treatmentCost",cd.getTreatmentCost());
        object.put("carPrices",cd.getCarPrices());
        object.put("usedPrices",cd.getUsedPrices());
        object.put("salvagePrices",cd.getSalvagePrices());
        object.put("costUpkeep",cd.getCostUpkeep());
        object.put("carProfits",cd.getCarProfits());
        object.put("carmodel",cd.getCarmodel());
        object.put("finalPayment",cd.getFinalPayment());
        object.put("referralFee",cd.getReferralFee());
        object.put("truckingFee",cd.getTruckingFee());
        object.put("handingExpense",cd.getHandingExpense());
        object.put("otherExpense",cd.getOtherExpense());
        object.put("createTime",cd.getCreateTime());
        object.put("modelsAccount",cd.getModelsAccount());

        // 1.损失照片,14.车辆资料,15.合同签约,16.授权书,17.承诺书,18.理赔资料,19.车款支付
        //损失照片
        List<String> list = cdImgService.selectByInAndTy(indentId, 1);
        object.put("lossImg", list);
        //车辆资料
        List<String> list14 = cdImgService.selectByInAndTy(indentId, 14);
        object.put("carDataImg", list14);
        //合同签约
        List<String> list15 = cdImgService.selectByInAndTy(indentId, 15);
        object.put("contractAwardImg", list15);
        //授权书
        List<String> list16 = cdImgService.selectByInAndTy(indentId, 16);
        object.put("authorizationImg", list16);
        //承诺书
        List<String> list17 = cdImgService.selectByInAndTy(indentId, 17);
        object.put("letterCommitmentImg", list17);
        //理赔资料
        List<String> list18 = cdImgService.selectByInAndTy(indentId, 18);
        object.put("claimSettlementImg", list18);
        //车款支付
        List<String> list19 = cdImgService.selectByInAndTy(indentId, 19);
        object.put("carModelImg", list19);
        //尾款支付图片
        List<String> list20 = cdImgService.selectByInAndTy(indentId, 20);
        object.put("finalPaymentImg", list20);
        //介绍费用图片
        List<String> list21 = cdImgService.selectByInAndTy(indentId, 21);
        object.put("referralFeeImg", list21);
        //拖车费用图片
        List<String> list22 = cdImgService.selectByInAndTy(indentId, 22);
        object.put("truckingFeeImg", list22);
        //处理费用图片
        List<String> list23 = cdImgService.selectByInAndTy(indentId, 23);
        object.put("handingExpenseImg", list23);
        //其他费用图片
        List<String> list13 = cdImgService.selectByInAndTy(indentId, 13);
        object.put("otherExpenseImg", list13);
        //车款到账图片
        List<String> list24 = cdImgService.selectByInAndTy(indentId, 24);
        object.put("modelsAccountImg", list24);


        result.put("code", 200);
        result.put("data", object);
        return result;
    }

    //订单列表
    public JSONObject orderList(JSONObject in) {
        JSONObject object1 = new JSONObject();
        Integer pagesize = in.getInteger("pagesize");
        pagesize = (pagesize - 1) * 20;

        JSONObject result = new JSONObject();

        Integer type = in.getInteger("type");
        String text = in.getString("text");
        String time = in.getString("time");
        //查询订单数字参数
        //新订单
        Integer newIndent = cdOrderMapper.findNewIndent(1);
        result.put("newIndent", newIndent);
        //意向修
        Integer offering = cdOrderMapper.findNewIndent(2);
        result.put("wantRepair", offering);
        //意向卖
        Integer buyAcc = cdOrderMapper.findNewIndent(3);
        result.put("wantSell", buyAcc);
        //待签车
        Integer maining = cdOrderMapper.findNewIndent(4);
        result.put("waitSignCar", maining);
        //已签车
        Integer inCar = cdOrderMapper.findNewIndent(5);
        result.put("signCar", inCar);
        //处理中
        Integer receiving = cdOrderMapper.findNewIndent(6);
        result.put("processing", receiving);
        //已收款
        Integer cost = cdOrderMapper.findNewIndent(7);
        result.put("payment", cost);
        //毛利润
        Integer margin = cdOrderMapper.findNewIndent(8);
        result.put("margin", margin);
        //已签车
        Integer signedCar = cdOrderMapper.findNewIndent(9);
        result.put("signedCar", signedCar);

        //查询订单数字参数
       // Integer newIndent = cdOrderMapper.findNewIndent(1);

        JSONArray jsonArray = new JSONArray();
        EntityWrapper<CarBrandEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("name",text);
        CarBrandEntity carBrandEntity1 = carBrandService.selectOne(wrapper);
        if (carBrandEntity1 != null){
            text = carBrandEntity1.getId().toString();
        }

        List<CdOrderEntity> list = cdOrderMapper.selectByType(type, text, pagesize, time, null);
        for (CdOrderEntity order : list) {
            JSONObject object = new JSONObject();
            Integer brandId = order.getBrandId();
            CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);

            object.put("id", order.getId());
            object.put("brandImg", carBrandEntity.getPicUrl());
            object.put("brand", carBrandEntity.getName());
            object.put("plate", order.getPlate());
            object.put("username", order.getUsername());
            object.put("phone", order.getPhone());
            object.put("carIns", order.getCarIns());
            object.put("chesunCoverage", order.getChesunCoverage());
            object.put("carPrices", order.getCarPrices());
            object.put("usedPrices", order.getUsedPrices());
            object.put("salvagePrices", order.getSalvagePrices());
            object.put("costUpkeep", order.getCostUpkeep());
            object.put("accidentType", order.getAccidentType());
            object.put("estimatedAmount", order.getEstimatedAmount());
            object.put("clientWill", order.getClientWill());
            object.put("remark", order.getRemark());

            Date accidentTime = order.getAccidentTime();
            //转String
           if(null != accidentTime){
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               String dateString = format.format(accidentTime);
               object.put("accidentTime", dateString);
           }

            if(String.valueOf(2).equals(String.valueOf(order.getAccidentType()))){
                if(null != order.getChesunCoverage() && null != order.getEstimatedAmount() && null != order.getCarPrices()){
                    BigDecimal subtract = order.getChesunCoverage().subtract(order.getEstimatedAmount());
                    object.put("chesunRatio",subtract.divide(order.getChesunCoverage(),BigDecimal.ROUND_HALF_UP));

                    BigDecimal subtract2 = order.getChesunCoverage().subtract(order.getCarPrices());
                    object.put("buyRatio",subtract2.divide(order.getCarPrices(),BigDecimal.ROUND_HALF_UP));
                }
            } else {
                if(null != order.getUsedPrices() && null != order.getSalvagePrices() && null != order.getCostUpkeep()){
                    BigDecimal subtract = order.getUsedPrices().subtract(order.getSalvagePrices());
                    object.put("salvageRatio",subtract.divide(order.getUsedPrices(),BigDecimal.ROUND_HALF_UP));

                    BigDecimal subtract2 = order.getUsedPrices().subtract(order.getSalvagePrices()).subtract(order.getCostUpkeep());
                    object.put("salvageProfitRatio",subtract2.divide(order.getUsedPrices(),BigDecimal.ROUND_HALF_UP));
                }
            }
            jsonArray.add(object);
        }
        result.put("list", jsonArray);

        object1.put("code", 200);
        object1.put("data", result);
        return object1;
    }

    @Transactional
    public JSONObject updateOrder(JSONObject in, Integer id) {
        JSONObject object = new JSONObject();

        //判断参数的值
        Integer orderId = in.getInteger("orderId");
        String plate = in.getString("plate");
        String brandId = in.getString("brandId");
        String username = in.getString("username");
        String phone = in.getString("phone");
        String accidentLiability = in.getString("accidentLiability");
        String businesSource = in.getString("businesSource");
        Integer accidentType = in.getInteger("accidentType");
        Integer state = in.getInteger("state");
        String clientWill = in.getString("clientWill");
        String remark = in.getString("remark");
        String carIns = in.getString("carIns");
        Date accidentTime = in.getDate("accidentTime");
        String contractStatus = in.getString("contractStatus");
        String finishOrder = in.getString("finishOrder");

        BigDecimal chesunCoverage = in.getBigDecimal("chesunCoverage");
        BigDecimal estimatedAmount = in.getBigDecimal("estimatedAmount");
        BigDecimal treatmentCost = in.getBigDecimal("treatmentCost");
        BigDecimal carPrices = in.getBigDecimal("carPrices");
        BigDecimal usedPrices = in.getBigDecimal("usedPrices");
        BigDecimal salvagePrices = in.getBigDecimal("salvagePrices");
        BigDecimal costUpkeep = in.getBigDecimal("costUpkeep");
        BigDecimal carProfits = in.getBigDecimal("carProfits");
        BigDecimal carmodel = in.getBigDecimal("carmodel");

        BigDecimal finalPayment = in.getBigDecimal("finalPayment");
        BigDecimal referralFee = in.getBigDecimal("referralFee");
        BigDecimal truckingFee = in.getBigDecimal("truckingFee");
        BigDecimal handingExpense = in.getBigDecimal("handingExpense");
        BigDecimal otherExpense = in.getBigDecimal("otherExpense");
        BigDecimal modelsAccount = in.getBigDecimal("modelsAccount");


        if (orderId != null) {
            CdOrderEntity cdOrder = selectById(orderId);
            if (cdOrder == null) {
                object.put("code", 408);
                object.put("msg", "订单为空");
                return object;
            }


            if(StringUtils.isNotEmpty(finishOrder) && String.valueOf(1).equals(finishOrder)){
                if(cdOrder.getState() < 8){
                    cdOrder.setState(8);
                }
            }

            if (StringUtils.isNotEmpty(String.valueOf(accidentTime)))
                cdOrder.setAccidentTime(accidentTime);

            if (StringUtils.isNotEmpty(plate))
                cdOrder.setPlate(plate);

            if (StringUtils.isNotEmpty(brandId))
                cdOrder.setBrandId(Integer.parseInt(brandId));

            if (StringUtils.isNotEmpty(username))
                cdOrder.setUsername(username);

            if (StringUtils.isNotEmpty(phone))
                cdOrder.setPhone(phone);

            if (StringUtils.isNotEmpty(accidentLiability))
                cdOrder.setAccidentLiability(accidentLiability);

            if (StringUtils.isNotEmpty(businesSource))
                cdOrder.setBusinesSource(businesSource);

            if (StringUtils.isNotEmpty(String.valueOf(accidentType))) {
                cdOrder.setAccidentType(accidentType);
            }
            if (StringUtils.isNotEmpty(clientWill)) {
                cdOrder.setClientWill(clientWill);
                if(clientWill.contains("修") && cdOrder.getState() < 4){
                    cdOrder.setState(2);
                } else if(clientWill.contains("卖") && cdOrder.getState() < 4){
                    cdOrder.setState(3);
                } else if(clientWill.contains("已")){
                    cdOrder.setState(9);
                }
            }
            if(String.valueOf(1).equals(contractStatus)){
                if(cdOrder.getState() < 4){
                    cdOrder.setState(4);
                }
            } else if(String.valueOf(0).equals(contractStatus)){
                if(cdOrder.getState() < 4){
                    cdOrder.setState(1);
                }
            }
            if (StringUtils.isNotEmpty(remark)) {
                cdOrder.setRemark(remark);
            }
            if (StringUtils.isNotEmpty(carIns)) {
                cdOrder.setCarIns(carIns);
            }
            if (null != chesunCoverage) {
                cdOrder.setChesunCoverage(chesunCoverage);
            }
            if (null != estimatedAmount) {
                cdOrder.setEstimatedAmount(estimatedAmount);
            }
            if (null != treatmentCost) {
                cdOrder.setTreatmentCost(treatmentCost);
            }
            if (null != carPrices) {
                cdOrder.setCarPrices(carPrices);
            }
            if (null != usedPrices) {
                cdOrder.setUsedPrices(usedPrices);
            }
            if (null != salvagePrices) {
                cdOrder.setSalvagePrices(salvagePrices);
            }
            if (null != costUpkeep) {
                cdOrder.setCostUpkeep(costUpkeep);
            }
            if (null != carProfits) {
                cdOrder.setCarProfits(carProfits);
            }

/**
 * @ApiImplicitParam(paramType = "query", name = "carDataImg", value = "车辆资料", required = true, dataType = "String[]"),
 * @ApiImplicitParam(paramType = "query", name = "contractAwardImg", value = "合同签约", required = true, dataType = "String[]"),
 * @ApiImplicitParam(paramType = "query", name = "authorizationImg", value = "授权书", required = true, dataType = "String[]"),
 * @ApiImplicitParam(paramType = "query", name = "letterCommitmentImg", value = "承诺书", required = true, dataType = "String[]"),
 * @ApiImplicitParam(paramType = "query", name = "claimSettlementImg", value = "另理赔资料", required = true, dataType = "String[]"),
 * @ApiImplicitParam(paramType = "query", name = "carModelImg", value = "车款支付", required = true, dataType = "String[]"),
 *
 */
            //修改图片
            JSONObject json = JSONObject.parseObject(String.valueOf(in));
            JSONArray lossPhotosList = (JSONArray) json.get("lossImg");//损失照片
            if (lossPhotosList != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 1);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(1);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray lossPhotosList2 = (JSONArray) json.get("carDataImg");//车辆资料
            if (lossPhotosList2 != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList2.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 14);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(14);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
            }
            JSONArray lossPhotosList3 = (JSONArray) json.get("contractAwardImg");//合同签约
            if (lossPhotosList3 != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList3.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 15);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(15);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
                if(cdOrder.getState() < 5){
                    cdOrder.setState(5);
                }
            }
            JSONArray lossPhotosList4 = (JSONArray) json.get("authorizationImg");//授权书
            if (lossPhotosList4 != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList4.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 16);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(16);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
            }
            JSONArray lossPhotosList5 = (JSONArray) json.get("letterCommitmentImg");//承诺书
            if (lossPhotosList5 != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList5.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 17);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(17);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
            }
            JSONArray lossPhotosList6 = (JSONArray) json.get("claimSettlementImg");//另理赔资料
            if (lossPhotosList6 != null) {
                //不为空修改照片
                List<String> lossPhotos = JSONArray.parseArray(lossPhotosList6.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", orderId)
                        .eq("type", 18);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < lossPhotos.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(18);
                    cdImgEntity.setImg(lossPhotos.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(orderId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray lossPhotosList7 = (JSONArray) json.get("carModelImg");//车款支付
            if (null != carmodel) {
                if (lossPhotosList7 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList7.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 19);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(19);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                    if(cdOrder.getState() < 6){
                        cdOrder.setState(6);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setCarmodel(carmodel);
            }

            JSONArray lossPhotosList8 = (JSONArray) json.get("finalPaymentImg");//尾款支付图片
            if (null != finalPayment) {
                if (lossPhotosList8 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList8.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 20);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(20);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setFinalPayment(finalPayment);
            }

            JSONArray lossPhotosList9 = (JSONArray) json.get("referralFeeImg");//介绍费用图片
            if (null != referralFee) {
                if (lossPhotosList9 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList9.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 21);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(21);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setReferralFee(referralFee);
            }

            JSONArray lossPhotosList10 = (JSONArray) json.get("truckingFeeImg");//拖车费用图片
            if (null != truckingFee) {
                if (lossPhotosList10 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList10.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 22);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(22);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setTruckingFee(truckingFee);
            }

            JSONArray lossPhotosList11 = (JSONArray) json.get("handingExpenseImg");//处理费用图片
            if (null != handingExpense) {
                if (lossPhotosList11 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList11.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 23);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(23);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setHandingExpense(handingExpense);
            }

            JSONArray lossPhotosList12 = (JSONArray) json.get("otherExpenseImg");//其他费用图片
            if (null != otherExpense) {
                if (lossPhotosList12 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList12.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 13);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(13);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setOtherExpense(otherExpense);
            }

            JSONArray lossPhotosList13 = (JSONArray) json.get("modelsAccountImg");//车款到账图片
            if (null != modelsAccount) {
                if (lossPhotosList13 != null) {
                    //不为空修改照片
                    List<String> lossPhotos = JSONArray.parseArray(lossPhotosList13.toString(), String.class);
                    //先删再存
                    EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                    wrapper.eq("indent_id", orderId)
                            .eq("type", 24);
                    cdImgService.delete(wrapper);

                    //添加
                    for (int i = 0; i < lossPhotos.size(); i++) {
                        //保存图片
                        CdImgEntity cdImgEntity = new CdImgEntity();
                        cdImgEntity.setType(24);
                        cdImgEntity.setImg(lossPhotos.get(i));
                        cdImgEntity.setIndex(i + 1);
                        cdImgEntity.setIndentId(orderId);
                        cdImgService.insert(cdImgEntity);
                    }
                    if(cdOrder.getState() < 7){
                        cdOrder.setState(7);
                    }
                } else {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdOrder.setModelsAccount(modelsAccount);
            }

            updateById(cdOrder);
            object.put("code", 200);
            object.put("msg", "成功");
            return object;
        } else {
            object.put("code", 407);
            object.put("msg", "orderId为空");
            return object;
        }
    }


}
