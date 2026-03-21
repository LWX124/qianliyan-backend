package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.b.modular.domain.AppMessageCarEntity;
import com.cheji.b.modular.domain.AppMessageUrlEntity;
import com.cheji.b.modular.mapper.AppMessageCarMapper;
import com.cheji.b.modular.mapper.AppMessageUrlMapper;
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
    private AppMessageCarMapper appMessageCarMapper;

    @Resource
    private AppMessageUrlMapper appMessageUrlMapper;


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


        return appMessageCar.getId().toString();
    }

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


        appMessageCar.setUpdateTime(new Date());
        this.updateById(appMessageCar);


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
        return appMessageCar;

    }
}
