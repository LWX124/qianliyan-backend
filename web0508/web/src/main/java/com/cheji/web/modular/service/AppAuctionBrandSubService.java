package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionBrandSubEntity;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppAuctionBrandSubMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 品牌订阅
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionBrandSubService extends ServiceImpl<AppAuctionBrandSubMapper, AppAuctionBrandSubEntity> implements IService<AppAuctionBrandSubEntity> {

    @Autowired
    private AppAuctionVipLvService appAuctionVipLvService;

    public JSONObject addBrand(JSONObject result, AppAuctionBrandSubEntity appAuctionBrandSub, UserEntity appUserEntity) {
        if (appAuctionBrandSub != null) {
            appAuctionBrandSub.setUserId(Long.valueOf(appUserEntity.getId()));
            appAuctionBrandSub.setVipLv(appUserEntity.getVipLv());
        }

        Integer brandNum = selectCount(new EntityWrapper<AppAuctionBrandSubEntity>().eq("user_id", appUserEntity.getId()));

        AppAuctionVipLvEntity lv = appAuctionVipLvService.selectOne(new EntityWrapper<AppAuctionVipLvEntity>().eq("lv", appUserEntity.getVipLv()));

        if (brandNum >= lv.getBrandNum()) {
            result.put("code", 202);
            result.put("msg", "请升级vip添加更多品牌!");
            return result;
        }

        if (appAuctionBrandSub.getId() != null) {
            updateById(appAuctionBrandSub);
            result.put("code", 200);
            result.put("msg", "修改成功!");
            return result;
        } else {
            AppAuctionBrandSubEntity appAuctionBrandSubEntity = selectOne(new EntityWrapper<AppAuctionBrandSubEntity>()
                    .eq("brand", appAuctionBrandSub.getBrand()).eq("user_id", appUserEntity.getId()));
            if (Objects.nonNull(appAuctionBrandSubEntity)) {
                result.put("code", 201);
                result.put("msg", "请勿重复订阅!");
                return result;
            }
            insert(appAuctionBrandSub);

            result.put("code", 200);
            result.put("msg", "添加成功!");
            return result;
        }
    }
}
