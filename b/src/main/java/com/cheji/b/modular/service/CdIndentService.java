package com.cheji.b.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CarBrandEntity;
import com.cheji.b.modular.domain.CdImgEntity;
import com.cheji.b.modular.domain.CdIndentEntity;
import com.cheji.b.modular.mapper.CdIndentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import freemarker.template.utility.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 车电订单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
@Service
public class CdIndentService extends ServiceImpl<CdIndentMapper, CdIndentEntity> implements IService<CdIndentEntity> {

    @Resource
    private CdIndentMapper cdIndentMapper;

    @Resource
    private CdImgService cdImgService;

    @Resource
    private CarBrandService carBrandService;

    @Resource
    private CdRepairOrderService repairOrderService;


    public JSONObject updateIntent(JSONObject in, Integer id) {
        JSONObject object = new JSONObject();

        //判断参数的值
        Integer indentId = in.getInteger("indentId");
        Date updateTime = in.getDate("updateTime");
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
        Integer workTime = in.getInteger("workTime");
        BigDecimal customerRebates = in.getBigDecimal("customerRebates");
        BigDecimal businessCommission = in.getBigDecimal("businessCommission");
        BigDecimal rescueCost = in.getBigDecimal("rescueCost");
        BigDecimal repairMoney = in.getBigDecimal("repairMoney");
        BigDecimal accessoriesAmount = in.getBigDecimal("accessoriesAmount");
        BigDecimal feeRebates = in.getBigDecimal("feeRebates");
        BigDecimal feeMoney = in.getBigDecimal("feeMoney");
        BigDecimal waiRebates = in.getBigDecimal("waiRebates");
        BigDecimal waiAmount = in.getBigDecimal("waiAmount");
        BigDecimal payment = in.getBigDecimal("payment");
        BigDecimal customerMoney = in.getBigDecimal("customerMoney");
        BigDecimal businessMoney = in.getBigDecimal("businessMoney");
        BigDecimal waixiuAmount = in.getBigDecimal("waixiuAmount");
        BigDecimal qitaAmount = in.getBigDecimal("qitaAmount");

        //修改图片
        JSONObject json = JSONObject.parseObject(String.valueOf(in));
        JSONArray lossPhotosList = (JSONArray) json.get("lossPhotos");//损失照片
        if (lossPhotosList != null) {
            //不为空修改照片
            List<String> lossPhotos = JSONArray.parseArray(lossPhotosList.toString(), String.class);
            //先删再存
            EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("indent_id", indentId)
                    .eq("type", 1);
            cdImgService.delete(wrapper);

            //添加
            for (int i = 0; i < lossPhotos.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(1);
                cdImgEntity.setImg(lossPhotos.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(indentId);
                cdImgService.insert(cdImgEntity);
            }
        }

        JSONArray repairMoneyImgList = (JSONArray) json.get("repairMoneyImg");//维修金额照片
        if (repairMoneyImgList != null) {
            //不为空修改照片
            List<String> repairMoneyPhotos = JSONArray.parseArray(repairMoneyImgList.toString(), String.class);
            //先删再存
            EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("indent_id", indentId)
                    .eq("type", 26);
            cdImgService.delete(wrapper);

            //添加
            for (int i = 0; i < repairMoneyPhotos.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(26);
                cdImgEntity.setImg(repairMoneyPhotos.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(indentId);
                cdImgService.insert(cdImgEntity);
            }
        }

        JSONArray claimInformationList = (JSONArray) json.get("claimInformation");//理赔资料
        if (claimInformationList != null) {
            List<String> claimInformation = JSONArray.parseArray(claimInformationList.toString(), String.class);

            EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("indent_id", indentId)
                    .eq("type", 2);
            cdImgService.delete(wrapper);

            //保存理赔资料
            for (int i = 0; i < claimInformation.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(2);
                cdImgEntity.setImg(claimInformation.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(indentId);
                cdImgService.insert(cdImgEntity);
            }
        }


        JSONArray accessoriesPhoto = (JSONArray) json.get("accessoriesPhoto");//配件清单照片
        if (accessoriesPhoto != null) {
            List<String> accessories = JSONArray.parseArray(accessoriesPhoto.toString(), String.class);

            EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("indent_id", indentId)
                    .eq("type", 3);
            cdImgService.delete(wrapper);

            //保存配件清单
            for (int i = 0; i < accessories.size(); i++) {
                //保存图片
                CdImgEntity cdImgEntity = new CdImgEntity();
                cdImgEntity.setType(3);
                cdImgEntity.setImg(accessories.get(i));
                cdImgEntity.setIndex(i + 1);
                cdImgEntity.setIndentId(indentId);
                cdImgService.insert(cdImgEntity);
            }
        }


        if (indentId != null) {
            CdIndentEntity cdIndent = selectById(indentId);
            if (cdIndent == null) {
                object.put("code", 408);
                object.put("msg", "订单为空");
                return object;
            }

            if (StringUtils.isNotEmpty(plate))
                cdIndent.setPlate(plate);

            if (StringUtils.isNotEmpty(brandId))
                cdIndent.setBrandId(Integer.parseInt(brandId));

            if (StringUtils.isNotEmpty(maintPlan))
                cdIndent.setMaintPlan(maintPlan);

            if (StringUtils.isNotEmpty(carIns))
                cdIndent.setCarIns(carIns);

            if (StringUtils.isNotEmpty(plateImg))
                cdIndent.setPlateImg(plateImg);

            if (StringUtils.isNotEmpty(frameImg))
                cdIndent.setFrameImg(frameImg);

            if (StringUtils.isNotEmpty(username))
                cdIndent.setUsername(username);

            if (StringUtils.isNotEmpty(phone))
                cdIndent.setPhone(phone);

            if (StringUtils.isNotEmpty(businesSource))
                cdIndent.setBusinesSource(businesSource);

            if (StringUtils.isNotEmpty(remark)) {
                cdIndent.setRemark(remark);
            }

            if (workTime != null)
                cdIndent.setWorkTime(workTime);

            if (customerRebates != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                cdIndent.setState(5);
                cdIndent.setCustomerRebates(customerRebates);
            }

            if (businessCommission != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if (cdIndent.getState() >= 4 && cdIndent.getState() <= 8) {
                    cdIndent.setState(7);
                }
                cdIndent.setBusinessCommission(businessCommission);
            }

            if (rescueCost != null) {
                cdIndent.setRescueCost(rescueCost);
            }


            if (repairMoney != null)
                cdIndent.setRepairMoney(repairMoney);

            JSONArray amountAccessImg = (JSONArray) json.get("amountAccessImg");//配件金额图片1
            if (accessoriesAmount != null){
                if(amountAccessImg == null || amountAccessImg.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdIndent.setAccessoriesAmount(accessoriesAmount);

                List<String> amountAccess = JSONArray.parseArray(amountAccessImg.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 8);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < amountAccess.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(8);
                    cdImgEntity.setImg(amountAccess.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }


            if (feeRebates != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                cdIndent.setFeeRebates(feeRebates);
            }

            JSONArray lossPic = (JSONArray) json.get("lossPic");//定损费用图片1
            if (feeMoney != null && feeMoney.compareTo(new BigDecimal("0")) != 0){
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(lossPic == null || lossPic.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }

                cdIndent.setFeeMoney(feeMoney);

                List<String> loss = JSONArray.parseArray(lossPic.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 11);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < loss.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(11);
                    cdImgEntity.setImg(loss.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }


            if (waiRebates != null){
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                cdIndent.setWaiRebates(waiRebates);
            }

            JSONArray waiPic = (JSONArray) json.get("waiPic");//外定费用图片1
            if (waiAmount != null && waiAmount.compareTo(new BigDecimal("0")) != 0){
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(waiPic == null || waiPic.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdIndent.setWaiAmount(waiAmount);

                List<String> wai = JSONArray.parseArray(waiPic.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 12);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < wai.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(12);
                    cdImgEntity.setImg(wai.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }


            if (waixiuAmount != null) {
                cdIndent.setWaixiuAmount(waixiuAmount);
            }

            JSONArray qitaPic = (JSONArray) json.get("qitaPic");//其他费用图片1
            if (qitaAmount != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(qitaPic == null || qitaPic.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                cdIndent.setQitaAmount(qitaAmount);

                //不为空修改照片
                List<String> qita = JSONArray.parseArray(qitaPic.toString(), String.class);
                //先删再存
                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 13);
                cdImgService.delete(wrapper);

                //添加
                for (int i = 0; i < qita.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(13);
                    cdImgEntity.setImg(qita.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray financialPayment = (JSONArray) json.get("financialPayment");//财务收款1
            if (payment != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(financialPayment == null || financialPayment.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }
                if (updateTime != null){
                    cdIndent.setUpdateTime(updateTime);
                }else {
                    object.put("code", 410);
                    object.put("msg", "收款时间不正确!");
                    return object;
                }

                cdIndent.setState(6);
                cdIndent.setPayment(payment);

                List<String> financialPaymentlist = JSONArray.parseArray(financialPayment.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 7);
                cdImgService.delete(wrapper);

                //保存理赔资料
                for (int i = 0; i < financialPaymentlist.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(7);
                    cdImgEntity.setImg(financialPaymentlist.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray customerPic = (JSONArray) json.get("customerPic");//客户返点图片1
            if (customerMoney != null) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(customerPic == null || customerPic.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }

                cdIndent.setCustomerMoney(customerMoney);

                List<String> customer = JSONArray.parseArray(customerPic.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 9);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < customer.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(9);
                    cdImgEntity.setImg(customer.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray businessPic = (JSONArray) json.get("businessPic");//业务佣金图片1
            if (businessMoney != null && businessMoney.compareTo(new BigDecimal("0")) != 0) {
                if (id != 4 && id != 8 && id != 9) {
                    object.put("code", 407);
                    object.put("msg", "没有修改权限");
                    return object;
                }
                if(businessPic == null || businessPic.isEmpty()) {
                    object.put("code", 409);
                    object.put("msg", "请添加图片！");
                    return object;
                }

                cdIndent.setBusinessMoney(businessMoney);

                List<String> business = JSONArray.parseArray(businessPic.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 10);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < business.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(10);
                    cdImgEntity.setImg(business.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray partsListImg = (JSONArray) json.get("partsListImg");//配件清单图片
            if (partsListImg != null) {

                List<String> business = JSONArray.parseArray(partsListImg.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 25);
                cdImgService.delete(wrapper);

                //保存配件清单
                for (int i = 0; i < business.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(25);
                    cdImgEntity.setImg(business.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }

            JSONArray changeListImg = (JSONArray) json.get("changeListImg");//换件清单图片
            if (changeListImg != null) {

                List<String> business = JSONArray.parseArray(changeListImg.toString(), String.class);

                EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("indent_id", indentId)
                        .eq("type", 27);
                cdImgService.delete(wrapper);

                //保存换件清单
                for (int i = 0; i < business.size(); i++) {
                    //保存图片
                    CdImgEntity cdImgEntity = new CdImgEntity();
                    cdImgEntity.setType(27);
                    cdImgEntity.setImg(business.get(i));
                    cdImgEntity.setIndex(i + 1);
                    cdImgEntity.setIndentId(indentId);
                    cdImgService.insert(cdImgEntity);
                }
            }


//            amountAccessImg", value = "配件金额图片
//            customerPic", value = "客户返点图片", r
//            businessPic", value = "业务佣金图片", r
//            lossPic", value = "定损费用图片", requi
//            waiPic", value = "外定费用图片", requir
//            qitaPic", value = "其他费用图片", requir

            updateById(cdIndent);
            object.put("code", 200);
            object.put("msg", "成功");
            return object;
        } else {
            object.put("code", 407);
            object.put("msg", "indentid为空");
            return object;
        }
    }


    public JSONObject indentList(JSONObject in) {
        JSONObject object1 = new JSONObject();
        Integer pagesize = in.getInteger("pagesize");
        pagesize = (pagesize - 1) * 20;

        JSONObject result = new JSONObject();


        //类型，1.新订单，2.报价中。3.采配件，4.维修中，5.交车中，6.已收款，7.报费用，8.毛利润
        Integer type = in.getInteger("type");
        String text = in.getString("text");


        //查询订单数字参数
        Integer newIndent = cdIndentMapper.findNewIndent(1);
        //新订单
        result.put("newIndent", newIndent);
        //报价中
        Integer offering = cdIndentMapper.findNewIndent(2);
        result.put("offering", offering);
        //采购配件
        Integer buyAcc = cdIndentMapper.findNewIndent(3);
        result.put("buyAcc", buyAcc);
        //维修中
        Integer maining = cdIndentMapper.findNewIndent(4);
        result.put("maining", maining);
        //交车中
        Integer inCar = cdIndentMapper.findNewIndent(5);
        result.put("inCar", inCar);
        //已收款
        Integer receiving = cdIndentMapper.findNewIndent(6);
        result.put("receiving", receiving);
        //报费用
        Integer cost = cdIndentMapper.findNewIndent(7);
        result.put("cost", cost);
        //毛利润
        Integer margin = cdIndentMapper.findNewIndent(8);
        result.put("margin", margin);

        JSONArray jsonArray = new JSONArray();
        List<CdIndentEntity> list = cdIndentMapper.selectByType(type, text, pagesize, null);
        for (CdIndentEntity indent : list) {
            JSONObject object = new JSONObject();
            Integer workTime = indent.getWorkTime();
            Integer brandId = indent.getBrandId();
            CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
            object.put("indentId", indent.getId());
            object.put("brandImg", carBrandEntity.getPicUrl());
            object.put("brand", carBrandEntity.getName());
            object.put("plate", indent.getPlate());
            object.put("username", indent.getUsername());
            object.put("phone", indent.getPhone());
            object.put("maintPlan", indent.getMaintPlan());
            object.put("carIns", indent.getCarIns());
            object.put("businesSource", indent.getBusinesSource());
            Date createTime = indent.getCreateTime();
            //转String
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(createTime);
            object.put("createTime", dateString);
            //机算交车时间
            if (workTime == null) {
                object.put("endTime", "暂无");
                object.put("countdown", "暂无");
            } else {
                Calendar ca = Calendar.getInstance();
                ca.setTime(createTime);
                ca.add(Calendar.DATE, workTime);
                createTime = ca.getTime();
                String enddate = format.format(createTime);
                object.put("endTime", enddate);
                //计算倒计时
                String s = dateDiff(new Date(), createTime);
                object.put("countdown", s);
            }

            object.put("remark", indent.getRemark());
            jsonArray.add(object);
        }
        result.put("list", jsonArray);

        Integer integer = cdIndentMapper.selectCount(null, null);
        result.put("carCount",integer);

        object1.put("code", 200);
        object1.put("data", result);
        return object1;
    }


    public String dateDiff(Date startTime, Date endTime) {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
//        long nh = 1000 * 60 * 60;//一小时的毫秒数
//        long nm = 1000 * 60;//一分钟的毫秒数
//        long ns = 1000;//一秒钟的毫秒数long diff;
        //获得两个时间的毫秒时间差异
        long diff;
        //diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        diff = endTime.getTime() - startTime.getTime();
        String day = diff / nd + "";//计算差多少天
//        String hour = diff % nd / nh + "";//计算差多少小时
//        String min = diff % nd % nh / nm + "";//计算差多少分钟
//        String sec = diff % nd % nh % nm / ns + "";//计算差多少秒//输出结果
        return day + "天";

    }


    public JSONObject indentDetails(JSONObject in) {
        JSONObject result = new JSONObject();
        Integer indentId = in.getInteger("indentId");
        if (indentId == null) {
            result.put("code", 407);
            result.put("msg", "indentId为空");
            return result;
        }
        JSONObject object = new JSONObject();
        CdIndentEntity cdIndent = this.selectById(indentId);
        object.put("indentId", cdIndent.getId());
        object.put("remark", cdIndent.getRemark());
        object.put("plate", cdIndent.getPlate());//车牌号
        Integer brandId = cdIndent.getBrandId();
        CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
        object.put("brand", carBrandEntity.getName());
        object.put("plateImg", cdIndent.getPlateImg());
        object.put("frameImg", cdIndent.getFrameImg());
        object.put("username", cdIndent.getUsername());
        object.put("phone", cdIndent.getPhone());
        object.put("maintPlan", cdIndent.getMaintPlan());
        object.put("carIns", cdIndent.getCarIns());
        object.put("qitaAmount", cdIndent.getQitaAmount());
        object.put("waixiuAmount", cdIndent.getWaixiuAmount());
        object.put("updateTime", cdIndent.getUpdateTime());


        List<String> list = cdImgService.selectByInAndTy(indentId, 1);
        //损失照片
        object.put("lossPhotos", list);
        List<String> list2 = cdImgService.selectByInAndTy(indentId, 2);
        //理赔资料
        object.put("claimInformation", list2);

        object.put("businesSource", cdIndent.getBusinesSource());
        if(cdIndent.getCustomerMoney() != null){
            object.put("businessAndCustomerMoney", cdIndent.getBusinessMoney().add(cdIndent.getCustomerMoney()));
        }else {
            object.put("businessAndCustomerMoney", cdIndent.getBusinessMoney());
        }
        object.put("businessMoney", cdIndent.getBusinessMoney());
        BigDecimal businessMoney1 = cdIndent.getBusinessMoney();
        if (businessMoney1 == null) {
            object.put("businessCommissionState", "未选择");
        } else if (businessMoney1.compareTo(new BigDecimal("0")) == 0) {
            object.put("businessCommissionState", "无佣金");
        } else {
            object.put("businessCommissionState", "已支付");
        }


        object.put("workTime", cdIndent.getWorkTime());
        object.put("rescueCost", cdIndent.getRescueCost());
        object.put("businessCommission", cdIndent.getBusinessCommission());

        object.put("waiRebates", cdIndent.getWaiRebates());

        //维修报价
        //查询维修总价
        object.put("repairQuotation", cdIndent.getRepairQuotation());

        //维修金额
        object.put("repairMoney", cdIndent.getRepairMoney());
        //维修金额图片
        EntityWrapper<CdImgEntity> wrapper26 = new EntityWrapper<>();
        wrapper26.eq("indent_id", indentId)
                .eq("type", 26);
        List<CdImgEntity> cdImgEntities26 = cdImgService.selectList(wrapper26);
        if (cdImgEntities26.isEmpty()) {
            object.put("repairMoneyImg", cdImgEntities26);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities26) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("repairMoneyImg", strings);
        }

        //外定费用
        object.put("waiAmount", cdIndent.getWaiAmount());
        BigDecimal waiAmount = cdIndent.getWaiAmount();
        if (waiAmount == null) {
            object.put("waiRebatesState", "未选择");
        } else if (waiAmount.compareTo(new BigDecimal("0")) == 0) {
            object.put("waiRebatesState", "无费用");
        } else {
            object.put("waiRebatesState", "已支付");
        }
        //退货补货
        Integer saleFood = repairOrderService.findSaleFood(indentId);
        object.put("saleFood", saleFood);
        object.put("accessoriesAmount", cdIndent.getAccessoriesAmount());
        BigDecimal accessoriesAmount1 = cdIndent.getAccessoriesAmount();
        if (accessoriesAmount1 == null) {
            object.put("accessoriesAmountState", "未选择");
        } else if (accessoriesAmount1.compareTo(new BigDecimal("0")) == 0) {
            object.put("accessoriesAmountState", "无配件");
        } else {
            object.put("accessoriesAmountState", "已选择");
        }
        //工单金额
        BigDecimal price = repairOrderService.findOrderMoney(indentId, 1);
        object.put("banjinOrder", price);
        BigDecimal price2 = repairOrderService.findOrderMoney(indentId, 2);
        object.put("jixiuOrder", price2);
        BigDecimal price3 = repairOrderService.findOrderMoney(indentId, 3);
        object.put("youqiOrder", price3);
        //财务收款
        object.put("payment", cdIndent.getPayment());
        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("indent_id", indentId)
                .eq("type", 7);
        List<CdImgEntity> cdImgEntities = cdImgService.selectList(wrapper);
        if (cdImgEntities.isEmpty()) {
            object.put("financialPayment", cdImgEntities);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("financialPayment", strings);
        }

        //            amountAccessImg", value = "配件金额图片
//            customerPic", value = "客户返点图片", r
//            businessPic", value = "业务佣金图片", r
//            lossPic", value = "定损费用图片", requi
//            waiPic", value = "外定费用图片", requir
        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("indent_id", indentId)
                .eq("type", 8);
        List<CdImgEntity> cdImgEntitie8 = cdImgService.selectList(wrapper1);
        if (cdImgEntitie8.isEmpty()) {
            object.put("amountAccessImg", cdImgEntitie8);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntitie8) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("amountAccessImg", strings);
        }

        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper2 = new EntityWrapper<>();
        wrapper2.eq("indent_id", indentId)
                .eq("type", 9);
        List<CdImgEntity> cdImgEntiti9 = cdImgService.selectList(wrapper2);
        if (cdImgEntiti9.isEmpty()) {
            object.put("customerPic", cdImgEntiti9);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntiti9) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("customerPic", strings);
        }

        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper3 = new EntityWrapper<>();
        wrapper3.eq("indent_id", indentId)
                .eq("type", 10);
        List<CdImgEntity> cdImgEntities10 = cdImgService.selectList(wrapper3);
        if (cdImgEntities10.isEmpty()) {
            object.put("businessPic", cdImgEntities10);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities10) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("businessPic", strings);
        }

        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper4 = new EntityWrapper<>();
        wrapper4.eq("indent_id", indentId)
                .eq("type", 11);
        List<CdImgEntity> cdImgEntities11 = cdImgService.selectList(wrapper4);
        if (cdImgEntities11.isEmpty()) {
            object.put("lossPic", cdImgEntities11);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities11) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("lossPic", strings);
        }

        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper5 = new EntityWrapper<>();
        wrapper5.eq("indent_id", indentId)
                .eq("type", 12);
        List<CdImgEntity> cdImgEntities12 = cdImgService.selectList(wrapper5);
        if (cdImgEntities12.isEmpty()) {
            object.put("waiPic", cdImgEntities12);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities12) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("waiPic", strings);
        }

        //财务收款图片
        EntityWrapper<CdImgEntity> wrapper6 = new EntityWrapper<>();
        wrapper6.eq("indent_id", indentId)
                .eq("type", 13);
        List<CdImgEntity> cdImgEntities13 = cdImgService.selectList(wrapper6);
        if (cdImgEntities13.isEmpty()) {
            object.put("qitaPic", cdImgEntities13);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities13) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("qitaPic", strings);
        }

        //配件清单图片
        EntityWrapper<CdImgEntity> wrapper7 = new EntityWrapper<>();
        wrapper7.eq("indent_id", indentId)
                .eq("type", 25);
        List<CdImgEntity> cdImgEntities14 = cdImgService.selectList(wrapper7);
        if (cdImgEntities14.isEmpty()) {
            object.put("partsListImg", cdImgEntities14);
        } else {
            List<String> strings = new ArrayList<>();
            for (CdImgEntity cdImgEntity : cdImgEntities14) {
                String img = cdImgEntity.getImg();
                if (StringUtils.isNotEmpty(img))
                    strings.add(img);
            }
            object.put("partsListImg", strings);
        }
        
        EntityWrapper<CdImgEntity> wrapper8 = new EntityWrapper<>();
        wrapper8.eq("indent_id", indentId)
                .eq("type", 27);
        List<CdImgEntity> cdImgEntities15 = cdImgService.selectList(wrapper8);
        if (cdImgEntities15.isEmpty()) {
            object.put("changeListImg", cdImgEntities15);
        } else {
//            List<String> strings = new ArrayList<>();
//            for (CdImgEntity cdImgEntity : cdImgEntities15) {
//                String img = cdImgEntity.getImg();
//                if (StringUtils.isNotEmpty(img))
//                    strings.add(img);
//            }
            List<String> strings = cdImgEntities15.stream().map(imgs -> imgs.getImg()).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.toList());
            object.put("changeListImg", strings);
        }

        //客户返点
        object.put("customerRebates", cdIndent.getCustomerRebates());
        BigDecimal customerRebates = cdIndent.getCustomerRebates();
        if (customerRebates == null) {
            object.put("customerRebatesState", "未选择");
        } else if (customerRebates.compareTo(new BigDecimal("0")) == 0) {
            object.put("customerRebatesState", "无返点");
        } else {
            object.put("customerRebatesState", "已支付");
        }
        //客户返点金额
        object.put("customerMoney", cdIndent.getCustomerMoney());

        //定损金额
        object.put("feeMoney", cdIndent.getFeeMoney());

        //定损比例
        object.put("feeRebates", cdIndent.getFeeRebates());
        BigDecimal feeRebates = cdIndent.getFeeRebates();
        if (feeRebates == null) {
            object.put("feeRebatesState", "未选择");
        } else if (feeRebates.compareTo(new BigDecimal("0")) == 0) {
            object.put("feeRebatesState", "无定损");
        } else {
            object.put("feeRebatesState", "已支付");
        }

        //配件金额比例
        object.put("accessoriesAmountRatio", BigDecimal.ZERO);
        //定损金额比例
        object.put("feeMoneyRatio", BigDecimal.ZERO);
        //工时费 比例
        object.put("workPriceRatio", BigDecimal.ZERO);
        //业务费比例
        object.put("businessMoneyRatio", BigDecimal.ZERO);
        //毛利润比例
        object.put("profitsRatio", BigDecimal.ZERO);
        //毛利润
        object.put("profits", BigDecimal.ZERO);

        BigDecimal workPrice = repairOrderService.selectWorkPrice(indentId);
        if (workPrice == null) {
            workPrice = BigDecimal.ZERO;
        }
        object.put("workPrice", workPrice);

        //获取各个参数减去利润

        //列表上的参数
        BigDecimal payment = cdIndent.getPayment();
        if (payment != null && payment.compareTo(BigDecimal.ZERO) != 0) {
            //收款金额为空或者为null
            //配件费比例ratio
            if (cdIndent.getAccessoriesAmount() != null) {
                BigDecimal accessoriesAmount = cdIndent.getAccessoriesAmount();
                BigDecimal divide = accessoriesAmount.divide(payment, 2, BigDecimal.ROUND_HALF_UP);
                object.put("accessoriesAmountRatio", divide.multiply(new BigDecimal(100)));
            }
            //定损非比例ratio
            if (cdIndent.getFeeMoney() != null) {
                BigDecimal feeMoney = cdIndent.getFeeMoney();
                BigDecimal divide = feeMoney.divide(payment, 2, BigDecimal.ROUND_HALF_UP);
                object.put("feeMoneyRatio", divide.multiply(new BigDecimal(100)));
            }

            //查询工时费比例
            if (workPrice.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal divide = workPrice.divide(payment, 2, BigDecimal.ROUND_HALF_UP);
                object.put("workPriceRatio", divide.multiply(new BigDecimal(100)));
            }
            //业务费用比例
            if (cdIndent.getBusinessMoney() != null && cdIndent.getCustomerMoney() != null) {
                BigDecimal businessMoney = cdIndent.getBusinessMoney().add(cdIndent.getCustomerMoney());
                BigDecimal divide = businessMoney.divide(payment, 2, BigDecimal.ROUND_HALF_UP);
                object.put("businessMoneyRatio", divide.multiply(new BigDecimal(100)));
            } else if (cdIndent.getBusinessMoney() != null){
                BigDecimal divide = cdIndent.getBusinessMoney().divide(payment, 2, BigDecimal.ROUND_HALF_UP);
                object.put("businessMoneyRatio", divide.multiply(new BigDecimal(100)));
            }
            //总消耗=配件费—工时费—定损费—业务费用—客户返点-外定金额-其他费用
            BigDecimal sum = new BigDecimal("0.00");
            BigDecimal sumAmount = sum
                    .add(cdIndent.getQitaAmount() == null ? new BigDecimal("0.00"): cdIndent.getQitaAmount())
                    .add(cdIndent.getCustomerMoney() == null ? new BigDecimal("0.00"): cdIndent.getCustomerMoney())
                    .add(cdIndent.getAccessoriesAmount())
                    .add(cdIndent.getFeeMoney())
                    .add(workPrice) //工单总费用
                    .add(cdIndent.getBusinessMoney())
                    .add(cdIndent.getWaiAmount() == null ? new BigDecimal("0.00"): cdIndent.getWaiAmount())
                    .add(cdIndent.getWaixiuAmount() == null ? new BigDecimal("0.00"): cdIndent.getWaixiuAmount())
                    .add(cdIndent.getRescueCost() == null ? new BigDecimal("0.00"): cdIndent.getRescueCost());
            //利润金额
            BigDecimal profits = payment.subtract(sumAmount);

            object.put("profits", profits);

            object.put("profitsRatio", profits.divide(payment, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));

        }


        result.put("code", 200);
        result.put("data", object);
        return result;
    }

    public JSONObject lipei() {
        JSONObject object = new JSONObject();
        List<String> lipri = cdIndentMapper.selectLiPei();
        object.put("code", 200);
        object.put("data", lipri);
        return object;
    }

    public CdIndentEntity selectIsRepaid(String plate) {
        return cdIndentMapper.selectIsRepaid(plate);
    }

    public JSONObject dataStatis(String time, Integer pagesize, String type) {
        pagesize = (pagesize - 1) * 20;
        JSONObject object = new JSONObject();
        JSONObject result = new JSONObject();

        if("0".equals(type)){
            //总收入，财务收款
            BigDecimal allIncome = cdIndentMapper.selectAllIncome(time);
            result.put("allIncome", allIncome);

            //配件费   配件金额
            BigDecimal accessories = cdIndentMapper.selectAccessories(time);
            result.put("accessories", accessories);
            //定损费   定损金额
            BigDecimal repairQuotation = cdIndentMapper.selectRepair(time);
            result.put("repairQuotation", repairQuotation);
            //工时费    workPrice
            BigDecimal workPrice = cdIndentMapper.selectAllWorkPrice(time);
            result.put("allWorkPrice", workPrice);
            //业务费    业务佣金
            BigDecimal business = cdIndentMapper.selectBusiness(time);
            result.put("business", business);

            BigDecimal subtract = allIncome.subtract(accessories).subtract(repairQuotation).subtract(workPrice).subtract(business);
            //毛利润
            result.put("gross", subtract);

            List<CdIndentEntity> list = cdIndentMapper.selectByType(null, null, pagesize,time);
            JSONArray jsonArray = new JSONArray();
            for (CdIndentEntity indent : list) {
                JSONObject object1 = new JSONObject();
                Integer workTime = indent.getWorkTime();
                Integer brandId = indent.getBrandId();
                CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                object1.put("indentId", indent.getId());
                object1.put("brandImg", carBrandEntity.getPicUrl());
                object1.put("brand", carBrandEntity.getName());
                object1.put("plate", indent.getPlate());
                object1.put("username", indent.getUsername());
                object1.put("phone", indent.getPhone());
                object1.put("maintPlan", indent.getMaintPlan());
                object1.put("carIns", indent.getCarIns());
                object1.put("businesSource", indent.getBusinesSource());
                Date createTime = indent.getCreateTime();
                //转String
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format.format(createTime);
                object1.put("createTime", dateString);
                //机算交车时间
                if (workTime == null) {
                    object1.put("endTime", "暂无");
                    object1.put("countdown", "暂无");
                } else {
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(createTime);
                    ca.add(Calendar.DATE, workTime);
                    createTime = ca.getTime();
                    String enddate = format.format(createTime);
                    object1.put("endTime", enddate);
                    //计算倒计时
                    String s = dateDiff(new Date(), createTime);
                    object1.put("countdown", s);
                }

                object.put("remark", indent.getRemark());
                jsonArray.add(object1);
            }
            result.put("list", jsonArray);

            Integer integer = cdIndentMapper.selectCount(time, type);
            result.put("carCount", integer);
        }
        else if ("1".equals(type)){
            //总收入，财务收款
            BigDecimal allIncome = cdIndentMapper.selectAllIncome2(time);
            result.put("allIncome", allIncome);

            //配件费   配件金额
            BigDecimal accessories = cdIndentMapper.selectAccessories2(time);
            result.put("accessories", accessories);
            //定损费   定损金额
            BigDecimal repairQuotation = cdIndentMapper.selectRepair2(time);
            result.put("repairQuotation", repairQuotation);
            //工时费    workPrice
            BigDecimal workPrice = cdIndentMapper.selectAllWorkPrice(time);
            result.put("allWorkPrice", workPrice);
            //业务费    业务佣金
            BigDecimal business = cdIndentMapper.selectBusiness2(time);
            result.put("business", business);

            BigDecimal subtract = allIncome.subtract(accessories).subtract(repairQuotation).subtract(workPrice).subtract(business);
            //毛利润
            result.put("gross", subtract);

            List<CdIndentEntity> list = cdIndentMapper.selectByType2(null, null, pagesize,time);
            JSONArray jsonArray = new JSONArray();
            for (CdIndentEntity indent : list) {
                JSONObject object1 = new JSONObject();
                Integer workTime = indent.getWorkTime();
                Integer brandId = indent.getBrandId();
                CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                object1.put("indentId", indent.getId());
                object1.put("brandImg", carBrandEntity.getPicUrl());
                object1.put("brand", carBrandEntity.getName());
                object1.put("plate", indent.getPlate());
                object1.put("username", indent.getUsername());
                object1.put("phone", indent.getPhone());
                object1.put("maintPlan", indent.getMaintPlan());
                object1.put("carIns", indent.getCarIns());
                object1.put("businesSource", indent.getBusinesSource());
                Date createTime = indent.getCreateTime();
                //转String
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format.format(createTime);
                object1.put("createTime", dateString);
                //机算交车时间
                if (workTime == null) {
                    object1.put("endTime", "暂无");
                    object1.put("countdown", "暂无");
                } else {
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(createTime);
                    ca.add(Calendar.DATE, workTime);
                    createTime = ca.getTime();
                    String enddate = format.format(createTime);
                    object1.put("endTime", enddate);
                    //计算倒计时
                    String s = dateDiff(new Date(), createTime);
                    object1.put("countdown", s);
                }

                object.put("remark", indent.getRemark());
                jsonArray.add(object1);
            }
            result.put("list", jsonArray);

            Integer integer = cdIndentMapper.selectCount(time, type);
            result.put("carCount", integer);
        }
        else if("2".equals(type)){
            List<CdIndentEntity> list = cdIndentMapper.selectByType3(pagesize,time);
            JSONArray jsonArray = new JSONArray();
            for (CdIndentEntity indent : list) {
                JSONObject object1 = new JSONObject();
                Integer workTime = indent.getWorkTime();
                Integer brandId = indent.getBrandId();
                CarBrandEntity carBrandEntity = carBrandService.selectById(brandId);
                object1.put("indentId", indent.getId());
                object1.put("brandImg", carBrandEntity.getPicUrl());
                object1.put("brand", carBrandEntity.getName());
                object1.put("plate", indent.getPlate());
                object1.put("username", indent.getUsername());
                object1.put("phone", indent.getPhone());
                object1.put("maintPlan", indent.getMaintPlan());
                object1.put("carIns", indent.getCarIns());
                object1.put("businesSource", indent.getBusinesSource());
                Date createTime = indent.getCreateTime();
                //转String
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = format.format(createTime);
                object1.put("createTime", dateString);
                //机算交车时间
                if (workTime == null) {
                    object1.put("endTime", "暂无");
                    object1.put("countdown", "暂无");
                } else {
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(createTime);
                    ca.add(Calendar.DATE, workTime);
                    createTime = ca.getTime();
                    String enddate = format.format(createTime);
                    object1.put("endTime", enddate);
                    //计算倒计时
                    String s = dateDiff(new Date(), createTime);
                    object1.put("countdown", s);
                }

                object.put("remark", indent.getRemark());
                jsonArray.add(object1);
            }
            result.put("list", jsonArray);

            Integer createTime = cdIndentMapper.selectCount(time, "0");
            Integer updateTime = cdIndentMapper.selectCount(time, "1");

            if (createTime != null && updateTime != null){
                result.put("carCount", (createTime-updateTime));
            }else {
                result.put("carCount", 0);
            }
        }

        object.put("code", 200);
        object.put("data", result);
        return object;
    }
}
