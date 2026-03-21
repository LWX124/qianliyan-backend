package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.constant.AppAuctionVipSet;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppAuctionVipControlEntity;
import com.cheji.web.modular.domain.AppAuctionWarnCarEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppAuctionWarnCarMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *     提醒车
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionWarnCarService extends ServiceImpl<AppAuctionWarnCarMapper, AppAuctionWarnCarEntity> implements IService<AppAuctionWarnCarEntity> {

    @Autowired
    private AppAuctionVipControlService appAuctionVipControlService;

    @Autowired
    private AppAuctionCollectService appAuctionCollectService;

    @Autowired
    private AppAuctionWarnCarService appAuctionWarnCarService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppAuctionService appAuctionService;

    public JSONObject addWarnCar(JSONObject result, Integer id, Long carId) {
        if(Objects.isNull(carId) || carId < 0){
            result.put("code", 201);
            result.put("msg", "参数错误!");
            return result;
        }
        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", id).eq("state", AppAuctionConstant.ONE));
        UserEntity userEntity = userService.selectById(id);
        if(Objects.isNull(appAuctionVipControl) || appAuctionVipControl.getState() != AppAuctionVipSet.VIP_COMMON_ONE || StringUtils.isEmpty(userEntity.getVipLv().toString()) || userEntity.getVipLv() < AppAuctionVipSet.VIP_COMMON_ONE){
            result.put("code", 202);
            result.put("msg", "请升级vip!");
            return result;
        }
        AppAuctionWarnCarEntity appAuctionWarnCarEntity = selectOne(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", id).eq("car_id", carId));
        if(Objects.nonNull(appAuctionWarnCarEntity)){
            result.put("code", 203);
            result.put("msg", "请勿重复添加!");
            return result;
        }
        List<AppAuctionWarnCarEntity> list = selectList(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", id));
        if(list.size() > 10){
            result.put("code", 204);
            result.put("msg", "最多提醒10辆!");
            return result;
        }

        AppAuctionWarnCarEntity appAuctionWarnCarEntity1 = new AppAuctionWarnCarEntity();
        appAuctionWarnCarEntity1.setCarId(carId);
        appAuctionWarnCarEntity1.setUserId(Long.valueOf(id));
        appAuctionWarnCarEntity1.setCreateTime(new Date());
        appAuctionWarnCarEntity1.setIsEnabled(AppAuctionConstant.CAR_IS_ENABLED);
        boolean insert = appAuctionWarnCarService.insert(appAuctionWarnCarEntity1);
        if(insert){
            result.put("code", 200);
            result.put("msg", "添加成功!");
            return result;
        }else{
            result.put("code", 200);
            result.put("msg", "添加失败!");
            return result;
        }
    }

    public JSONObject delWarnCar(JSONObject result, Integer id, Long carId) {
        if(Objects.isNull(carId) || carId < 0){
            result.put("code", 202);
            result.put("msg", "参数错误!");
            return result;
        }
        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", id).eq("state",AppAuctionConstant.ONE));
        if(Objects.isNull(appAuctionVipControl)){
            result.put("code", 201);
            result.put("msg", "请升级vip!");
            return result;
        }

        boolean delete = delete(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", id).eq("car_id", carId));
        if(delete){
            result.put("code", 200);
            result.put("msg", "删除成功!");
            return result;
        }else{
            result.put("code", 200);
            result.put("msg", "删除失败!");
            return result;
        }
    }


    public JSONObject queryWarnCar(JSONObject result, Integer id) {
        AppAuctionVipControlEntity appAuctionVipControl = appAuctionVipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", id).eq("state",AppAuctionConstant.ONE));
        if(Objects.isNull(appAuctionVipControl)){
            result.put("code", 202);
            result.put("msg", "请升级vip!");
            return result;
        }

        List<AppAuctionWarnCarEntity> list = selectList(new EntityWrapper<AppAuctionWarnCarEntity>().eq("user_id", id));
        if(list.size() > 0){
            List<JSONObject> collect = list.stream().map(a -> {
                AppAuctionEntity auction = appAuctionService.selectById(a.getCarId());
                if (auction != null) {
                    JSONObject object = appAuctionCollectService.auc2collDto2(auction, id.longValue());
                    return object;
                }
                return null;
            }).collect(Collectors.toList());

            result.put("code", 200);
            result.put("msg", "查询成功!");
            result.put("data", collect);
            return result;
        }else {
            result.put("code", 200);
            result.put("msg", "暂无数据!");
            result.put("data", list);
            return result;
        }
    }


}
