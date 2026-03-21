package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionFindCarEntity;
import com.cheji.web.modular.mapper.AppAuctionFindCarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     寻车
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionFindCarService extends ServiceImpl<AppAuctionFindCarMapper, AppAuctionFindCarEntity> implements IService<AppAuctionFindCarEntity> {

    @Autowired
    private AppAuctionFindCarService appAuctionFindCarService;

    public JSONObject addFindCar(JSONObject result, AppAuctionFindCarEntity findCar, Integer userId) {

        if(findCar != null && findCar.getId() == null){
            findCar.setCreateTime(new Date());
            findCar.setUserId(Long.valueOf(userId));
            boolean insert = appAuctionFindCarService.insert(findCar);
            if(insert){
                result.put("code", 200);
                result.put("msg", "保存成功!");
                return result;
            }else {
                result.put("code", 401);
                result.put("msg", "保存失败");
                return result;
            }
        }else if(findCar != null && findCar.getId() != null){
            findCar.setUpdateTime(new Date());
            boolean b = updateById(findCar);
            if(b){
                result.put("code", 200);
                result.put("msg", "修改成功!");
                return result;
            }else {
                result.put("code", 402);
                result.put("msg", "修改失败");
                return result;
            }
        }
        result.put("code", 401);
        result.put("msg", "操作失败");
        return result;
    }

    public JSONObject delFindCar(JSONObject result, String delId) {
        if(delId != null){
            if(appAuctionFindCarService.deleteById(Long.valueOf(delId))){
                result.put("code", 200);
                result.put("msg", "删除成功");
                return result;
            }else {
                result.put("code", 402);
                result.put("msg", "删除失败");
                return result;
            }
        }
        result.put("code", 401);
        result.put("msg", "删除失败");
        return result;
    }

    public JSONObject queryFindCar(JSONObject result, Integer userId) {
        List<AppAuctionFindCarEntity> findCar = selectList(new EntityWrapper<AppAuctionFindCarEntity>().eq("user_id", userId));
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", findCar);
        return result;
    }
}
