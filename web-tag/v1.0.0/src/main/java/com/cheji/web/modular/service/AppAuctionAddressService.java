package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.modular.domain.AppAuctionAddressEntity;
import com.cheji.web.modular.domain.CityEntity;
import com.cheji.web.modular.mapper.AppAuctionAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     地址管理
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionAddressService extends ServiceImpl<AppAuctionAddressMapper, AppAuctionAddressEntity> implements IService<AppAuctionAddressEntity> {

    @Autowired
    private CityService cityService;

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public JSONObject addAddress(JSONObject result, AppAuctionAddressEntity address, Integer userId) {
        String isDefault = address.getIsDefault();

        if(AppAuctionConstant.ISDEFAULTADDRESS.equals(isDefault)){
            List<AppAuctionAddressEntity> adds = selectList(new EntityWrapper<AppAuctionAddressEntity>().eq("user_id", userId));
            for (AppAuctionAddressEntity addr : adds) {
                if(AppAuctionConstant.ISDEFAULTADDRESS.equals(addr.getIsDefault())){
                    addr.setIsDefault(AppAuctionConstant.ISNOTDEFAULTADDRESS);
                    updateById(addr);
                }
            }
        }
        if(address != null && address.getId() == null){
            address.setCreateTime(new Date());
            address.setUserId(Long.valueOf(userId));
            boolean insert = insert(address);
            if(insert){
                result.put("code", 200);
                result.put("msg", "保存成功!");
                return result;
            }else {
                result.put("code", 401);
                result.put("msg", "保存失败");
                return result;
            }
        }else if(address != null && address.getId() != null){

            address.setUpdateTime(new Date());
            boolean b = updateById(address);
           if(b){
               result.put("code", 200);
               result.put("msg", "修改成功!");
           }else {
               result.put("code", 402);
               result.put("msg", "修改失败");
           }
            return result;
        }
        result.put("code", 401);
        result.put("msg", "请选择是否是默认地址!");
        return result;
    }

    public JSONObject delAddress(JSONObject result, String delId) {
        if(delId != null){
            if(deleteById(Long.valueOf(delId))){
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

    public JSONObject queryAddress(JSONObject result, Integer userId) {
        List<AppAuctionAddressEntity> address = selectList(new EntityWrapper<AppAuctionAddressEntity>().eq("user_id", userId));
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", address);
        return result;
    }

    public JSONObject getCity() {
        JSONObject result = new JSONObject();
        List<CityEntity> cityEntities1 = cityService.selectList(null);
        JSONObject jsonObject = null;
        JSONObject city = null;
        JSONArray objects = null;
        JSONArray areas = null;
        JSONArray array = new JSONArray();
        String c = null;
        for (CityEntity cityEntity : cityEntities1) {
            String cityName = cityEntity.getCityName();
            if(cityEntity.getCitycode() == null){
                if(jsonObject != null){
                    jsonObject.put("capital",c);
                    jsonObject.put("citys",objects);
                    array.add(jsonObject);
                }
                jsonObject = new JSONObject();
                c = cityName;
                objects = new JSONArray();
            }else if(cityName.endsWith("市")){
                city = new JSONObject();
                city.put("cityName",cityEntity.getCityName());
                objects.add(city);
                areas = new JSONArray();
            }else if(cityName.endsWith("区")){

                areas.add(cityName);
                city.put("area",areas);
            }
        }
        result.put("code", 200);
        result.put("msg", "成功");
        result.put("data", array);
        return result;
    }


}
