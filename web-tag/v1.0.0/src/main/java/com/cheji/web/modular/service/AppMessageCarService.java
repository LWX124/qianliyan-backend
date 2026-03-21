package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppMessageCarEntity;
import com.cheji.web.modular.domain.AppMessageUrlEntity;
import com.cheji.web.modular.mapper.AppMessageCarMapper;
import com.cheji.web.modular.mapper.AppMessageUrlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-14
 */
@Service
public class AppMessageCarService extends ServiceImpl<AppMessageCarMapper, AppMessageCarEntity> implements IService<AppMessageCarEntity> {

    @Resource
    private AppMessageUrlMapper appMessageUrlMapper;

    @Resource
    private AppMessageCarMapper appMessageCarMapper;


    //新增
    public String addCarMessage(AppMessageCarEntity messageCarEntity, Integer type, String messageId) {
        AppMessageCarEntity appMessageCar = new AppMessageCarEntity();
        appMessageCar.setLicensePlate(messageCarEntity.getLicensePlate());

        appMessageCar.setBrandId(messageCarEntity.getBrandId());

        appMessageCar.setUserBId(messageCarEntity.getUserBId());

        appMessageCar.setLocalInsurance(messageCarEntity.getLocalInsurance());

        appMessageCar.setCustomerName(messageCarEntity.getCustomerName());

        appMessageCar.setPhone(messageCarEntity.getPhone());

        appMessageCar.setCasualties(messageCarEntity.getCasualties());

        appMessageCar.setFinancialLoss(messageCarEntity.getFinancialLoss());

        appMessageCar.setBuyCar(messageCarEntity.getBuyCar());

        appMessageCar.setChannelsIns(messageCarEntity.getChannelsIns());

        appMessageCar.setVehicleResults(messageCarEntity.getVehicleResults());

        appMessageCar.setAccidentResponsibility(messageCarEntity.getAccidentResponsibility());

        //根据维修结果来修改状态

        appMessageCar.setLeaveMessage(messageCarEntity.getLeaveMessage());

        appMessageCar.setAmount(messageCarEntity.getAmount());

        appMessageCar.setMessId(messageId);
        appMessageCar.setType(type);
        appMessageCar.setCreateTime(new Date());
        appMessageCar.setUpdateTime(new Date());
        this.insert(appMessageCar);


//
//        String otherInsurance = in.getString("otherInsurance");
//        appMessageCar.setOtherInsurance(otherInsurance);
//        String helpAddress = in.getString("helpAddress");
//        appMessageCar.setHelpAddress(helpAddress);
//        BigDecimal helpLng = in.getBigDecimal("helpLng");
//        appMessageCar.setHelpLng(helpLng);
//        BigDecimal helpLat = in.getBigDecimal("helpLat");
//        appMessageCar.setHelpLat(helpLat);

//        Integer maintenanceMode = in.getInteger("maintenanceMode");
//        appMessageCar.setMaintenanceMode(maintenanceMode);
//        Integer vehicleLoss = in.getInteger("vehicleLoss");
//        appMessageCar.setVehicleLoss(vehicleLoss);
//        Integer customerIntention = in.getInteger("customerIntention");
//        appMessageCar.setCustomerIntention(customerIntention);
//        String leaveMessage = in.getString("leaveMessage");
//        appMessageCar.setLeaveMessage(leaveMessage);
//
//        String voice = in.getString("voice");
//        appMessageCar.setVoice(voice);
//
//        String mainPhone = in.getString("mainPhone");
//        appMessageCar.setMainPhone(mainPhone);
//
//        String mainName = in.getString("mainName");
//        appMessageCar.setMainName(mainName);
//
//        String mainInsurance = in.getString("mainInsurance");
//        appMessageCar.setMainInsurance(mainInsurance);
//
//
//
//
//
//        String usuallyMaintain = in.getString("usuallyMaintain");//          常用维修点
//        appMessageCar.setUsuallyMaintain(usuallyMaintain);
//
//
//
//
//
//        String accConditions = in.getString("accConditions");//                            事故情况",
//        appMessageCar.setAccConditions(accConditions);
//
//
//        String saveCosts = in.getString("saveCosts");           //施救费用
//        appMessageCar.setSaveCosts(saveCosts);
//
//
//        String fixIntention = in.getString("fixIntention");         //维修意向
//        appMessageCar.setFixIntention(fixIntention);


        //理赔照片
//        JSONArray claimPhotoImgList = (JSONArray) json.get("claimPhotoImgList");
//        if (claimPhotoImgList != null) {
//            List<String> claimList = JSONArray.parseArray(claimPhotoImgList.toString(), String.class);
//            for (int i = 0; i < claimList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(2);
//                appMessageUrlEntity.setUrl(claimList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }

        //推修凭证
        if (messageCarEntity.getCredentialsImgList() != null) {
            List<String> credntailsList = messageCarEntity.getCredentialsImgList();
            for (int i = 0; i < credntailsList.size(); i++) {
                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
                appMessageUrlEntity.setMessageId(appMessageCar.getId().toString());
                appMessageUrlEntity.setIndex(i + 1);
                appMessageUrlEntity.setType(3);
                appMessageUrlEntity.setUrl(credntailsList.get(i));
                appMessageUrlEntity.setCreateTime(new Date());
                appMessageUrlEntity.setUpdateTime(new Date());
                appMessageUrlMapper.insert(appMessageUrlEntity);
            }
        }

//        mainCarLicense", value = "车主车牌"

//        JSONArray mainCarLicense = (JSONArray) json.get("mainCarLicense");
//        if (mainCarLicense != null) {
//            List<String> mainCarLicenseList = JSONArray.parseArray(mainCarLicense.toString(), String.class);
//            for (int i = 0; i < mainCarLicenseList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(4);
//                appMessageUrlEntity.setUrl(mainCarLicenseList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }


//        insuredList", value = "被保险人", r
//        JSONArray insured = (JSONArray) json.get("insuredList");
//        if (insured != null) {
//            List<String> insuredList = JSONArray.parseArray(insured.toString(), String.class);
//            for (int i = 0; i < insuredList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(5);
//                appMessageUrlEntity.setUrl(insuredList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }

        return appMessageCar.getId().toString();
    }

    //修改其中的数据
    public AppMessageCarEntity updateCarMessage(AppMessageCarEntity messageCarEntity, String id) {
        //删除覆盖图片
        AppMessageCarEntity appMessageCar = this.selectById(id);
        //先修改数据，然后查询是否有图片
        if (StringUtils.isNotEmpty(messageCarEntity.getLicensePlate()))
            appMessageCar.setLicensePlate(messageCarEntity.getLicensePlate());

        if (StringUtils.isNotEmpty(messageCarEntity.getBrandId()))
            appMessageCar.setBrandId(messageCarEntity.getBrandId());


        if (StringUtils.isNotEmpty(messageCarEntity.getLocalInsurance()))
            appMessageCar.setLocalInsurance(messageCarEntity.getLocalInsurance());

        if (StringUtils.isNotEmpty(messageCarEntity.getCustomerName()))
            appMessageCar.setCustomerName(messageCarEntity.getCustomerName());

        if (StringUtils.isNotEmpty(messageCarEntity.getPhone()))
            appMessageCar.setPhone(messageCarEntity.getPhone());

        if (StringUtils.isNotEmpty(messageCarEntity.getCasualties()))
            appMessageCar.setCasualties(messageCarEntity.getCasualties());

        //  财务损失",
        if (StringUtils.isNotEmpty(messageCarEntity.getFinancialLoss()))
            appMessageCar.setFinancialLoss(messageCarEntity.getFinancialLoss());

        //   客户购车",
        if (StringUtils.isNotEmpty(messageCarEntity.getBuyCar()))
            appMessageCar.setBuyCar(messageCarEntity.getBuyCar());

        //              保险渠道",
        if (StringUtils.isNotEmpty(messageCarEntity.getChannelsIns()))
            appMessageCar.setChannelsIns(messageCarEntity.getChannelsIns());


        Integer vehicleResults = messageCarEntity.getVehicleResults();
        if (vehicleResults != null) {
            appMessageCar.setVehicleResults(vehicleResults);
        }

        BigDecimal amount = messageCarEntity.getAmount();
        if (amount != null) {
            appMessageCar.setAmount(amount);
        }

        String userBId = messageCarEntity.getUserBId();
        if (StringUtils.isNotEmpty(userBId)){
            appMessageCar.setUserBId(userBId);
        }

        if (StringUtils.isNotEmpty(messageCarEntity.getLeaveMessage())) {
            appMessageCar.setLeaveMessage(messageCarEntity.getLeaveMessage());
        }


        if (StringUtils.isNotEmpty(messageCarEntity.getAccidentResponsibility())) {
            appMessageCar.setAccidentResponsibility(messageCarEntity.getAccidentResponsibility());
        }

//        String otherInsurance = in.getString("otherInsurance");
//        if (StringUtils.isNotEmpty(otherInsurance)) {
//            appMessageCar.setOtherInsurance(otherInsurance);
//        }
//
//        String helpAddress = in.getString("helpAddress");
//        if (StringUtils.isNotEmpty(helpAddress)) {
//            appMessageCar.setHelpAddress(helpAddress);
//        }
//
//        BigDecimal helpLng = in.getBigDecimal("helpLng");
//        appMessageCar.setHelpLng(helpLng);
//
//        BigDecimal helpLat = in.getBigDecimal("helpLat");
//        appMessageCar.setHelpLat(helpLat);


//
//        Integer maintenanceMode = in.getInteger("maintenanceMode");
//        if (maintenanceMode != null) {
//            appMessageCar.setMaintenanceMode(maintenanceMode);
//        }
//
//        Integer vehicleLoss = in.getInteger("vehicleLoss");
//        if (vehicleLoss != null) {
//            appMessageCar.setVehicleLoss(vehicleLoss);
//        }

//        Integer customerIntention = in.getInteger("customerIntention");
//        if (customerIntention != null) {
//            appMessageCar.setCustomerIntention(customerIntention);
//        }
//
//        String voice = in.getString("voice");
//        if (StringUtils.isNotEmpty(voice)) {
//            appMessageCar.setVoice(voice);
//        }
//
//
//        String mainPhone = in.getString("mainPhone");
//        if (StringUtils.isNotEmpty(mainPhone))
//            appMessageCar.setMainPhone(mainPhone);
//
//        String mainName = in.getString("mainName");
//        if (StringUtils.isNotEmpty(mainName))
//            appMessageCar.setMainName(mainName);
//
//        String mainInsurance = in.getString("mainInsurance");
//        if (StringUtils.isNotEmpty(mainInsurance))
//            appMessageCar.setMainInsurance(mainInsurance);
//
//
//
//
//        String usuallyMaintain = in.getString("usuallyMaintain");//          常用维修点
//        if (StringUtils.isNotEmpty(usuallyMaintain))
//            appMessageCar.setUsuallyMaintain(usuallyMaintain);
//
//
//        String accConditions = in.getString("accConditions");//                            事故情况",
//        if (StringUtils.isNotEmpty(accConditions))
//            appMessageCar.setAccConditions(accConditions);
//
//
//        String saveCosts = in.getString("saveCosts");           //施救费用
//        if (StringUtils.isNotEmpty(saveCosts))
//            appMessageCar.setSaveCosts(saveCosts);
//
//
//        String fixIntention = in.getString("fixIntention");         //维修意向
//        if (StringUtils.isNotEmpty(fixIntention))
//            appMessageCar.setFixIntention(fixIntention);


        appMessageCar.setUpdateTime(new Date());
        this.updateById(appMessageCar);

        //修改三种图片
        //车损照片
//        JSONArray vehiclePhotoImgList = (JSONArray) json.get("vehiclePhotoImgList");
//        if (vehiclePhotoImgList != null) {
//            //先删除
//            //查询到原本图片
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", in.getString("messageId"))
//                    .eq("type", 1)
//                    .eq("source", in.getInteger("type"));
//            appMessageUrlMapper.delete(wrapper);
////            List<AppMessageUrlEntity> appMessageUrlList = appMessageUrlMapper.selectList(wrapper);
////            //查询出来了就删除
////            if (!appMessageUrlList.isEmpty()){
////                appMessageUrlMapper.delete(wrapper);
////            }
//
//            List<String> vehicleList = JSONArray.parseArray(vehiclePhotoImgList.toString(), String.class);
//            for (int i = 0; i < vehicleList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(in.getInteger("messageId"));
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(1);
//                appMessageUrlEntity.setSource(in.getInteger("type"));
//                appMessageUrlEntity.setUrl(vehicleList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        } else {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 1);
//            appMessageUrlMapper.delete(wrapper);
//        }

        //理赔照片
//        JSONArray claimPhotoImgList = (JSONArray) json.get("claimPhotoImgList");
//        if (claimPhotoImgList != null) {
//
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 2);
//            appMessageUrlMapper.delete(wrapper);
//
//            List<String> claimList = JSONArray.parseArray(claimPhotoImgList.toString(), String.class);
//            for (int i = 0; i < claimList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(2);
//                appMessageUrlEntity.setUrl(claimList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }else {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 2);
//            appMessageUrlMapper.delete(wrapper);
//        }

        //推修凭证
        List<String> credentialsImgList = messageCarEntity.getCredentialsImgList();
        //JSONArray credentialsImgList = (JSONArray) json.get("credentialsImgList");
        if (credentialsImgList != null) {
            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("message_id", id)
                    .eq("type", 3);
            appMessageUrlMapper.delete(wrapper);

            for (int i = 0; i < credentialsImgList.size(); i++) {
                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
                appMessageUrlEntity.setMessageId(appMessageCar.getId().toString());
                appMessageUrlEntity.setIndex(i + 1);
                appMessageUrlEntity.setType(3);
                appMessageUrlEntity.setUrl(credentialsImgList.get(i));
                appMessageUrlEntity.setCreateTime(new Date());
                appMessageUrlEntity.setUpdateTime(new Date());
                appMessageUrlMapper.insert(appMessageUrlEntity);
            }
        }


//        mainCarLicense", value = "车主车牌"

//        JSONArray mainCarLicense = (JSONArray) json.get("mainCarLicense");
//        if (mainCarLicense != null) {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 4);
//            appMessageUrlMapper.delete(wrapper);
//
//            List<String> mainCarLicenseList = JSONArray.parseArray(mainCarLicense.toString(), String.class);
//            for (int i = 0; i < mainCarLicenseList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(4);
//                appMessageUrlEntity.setUrl(mainCarLicenseList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }else {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 4);
//            appMessageUrlMapper.delete(wrapper);
//        }


//        insuredList", value = "被保险人", r
//        JSONArray insured = (JSONArray) json.get("insuredList");
//        if (insured != null) {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 5);
//            appMessageUrlMapper.delete(wrapper);
//
//            List<String> insuredList = JSONArray.parseArray(insured.toString(), String.class);
//            for (int i = 0; i < insuredList.size(); i++) {
//                AppMessageUrlEntity appMessageUrlEntity = new AppMessageUrlEntity();
//                appMessageUrlEntity.setMessageId(appMessageCar.getId().intValue());
//                appMessageUrlEntity.setIndex(i + 1);
//                appMessageUrlEntity.setType(5);
//                appMessageUrlEntity.setUrl(insuredList.get(i));
//                appMessageUrlEntity.setCreateTime(new Date());
//                appMessageUrlEntity.setUpdateTime(new Date());
//                appMessageUrlMapper.insert(appMessageUrlEntity);
//            }
//        }else {
//            EntityWrapper<AppMessageUrlEntity> wrapper = new EntityWrapper<>();
//            wrapper.eq("message_id", id)
//                    .eq("type", 5);
//            appMessageUrlMapper.delete(wrapper);
//        }
        return appMessageCar;

    }

    public List<Integer> findIsPush(String userBId, String carMid) {
        return appMessageCarMapper.findIsPush(userBId, carMid);
    }

    public Integer findAgain(Integer userId) {
        return appMessageCarMapper.findAgain(userId);
    }

    public Integer findAgree(Integer userId) {
        return appMessageCarMapper.findAgree(userId);
    }

    public Integer findRefused(Integer userId) {
        return appMessageCarMapper.findRefused(userId);
    }

    public Integer findFinish(Integer userId) {
        return appMessageCarMapper.findFinish(userId);
    }


//    String licensePlate = in.getString("licensePlate");
//    String customerName = in.getString("customerName");
//    String phone = in.getString("phone");
//    String localInsurance = in.getString("localInsurance");
//    String otherInsurance = in.getString("otherInsurance");
//    String helpAddress = in.getString("helpAddress");
//    BigDecimal helpLng = in.getBigDecimal("helpLng");
//    BigDecimal helpLat = in.getBigDecimal("helpLat");
//    String accidentResponsibility = in.getString("accidentResponsibility");
//    Integer maintenanceMode = in.getInteger("maintenanceMode");
//    Integer vehicleLoss = in.getInteger("vehicleLoss");
//    Integer customerIntention = in.getInteger("customerIntention");
//    String leaveMessage = in.getString("leaveMessage");
//    Integer vehicleResults = in.getInteger("vehicleResults");
//
//    JSONObject json = JSONObject.parseObject(String.valueOf(in));
//    //车损照片
//    JSONArray vehiclePhotoImgList = (JSONArray) json.get("vehiclePhotoImgList");
//    List<String> vehicleList = JSONArray.parseArray(vehiclePhotoImgList.toString(), String.class);
//
//    //理赔照片
//    JSONArray claimPhotoImgList = (JSONArray) json.get("claimPhotoImgList");
//    List<String> claimList = JSONArray.parseArray(claimPhotoImgList.toString(), String.class);
//
//    //推修凭证
//    JSONArray credentialsImgList = (JSONArray) json.get("credentialsImgList");
//    List<String> credntailsList = JSONArray.parseArray(credentialsImgList.toString(), String.class);
//

}
