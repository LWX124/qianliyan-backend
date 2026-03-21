package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.domain.MerchantsBrandEntity;
import com.cheji.b.modular.mapper.AppUserMapper;
import com.cheji.b.modular.mapper.MerchantsBrandMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 品牌和商户关联表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@Service
public class MerchantsBrandService extends ServiceImpl<MerchantsBrandMapper, MerchantsBrandEntity> implements IService<MerchantsBrandEntity> {
    @Resource
    private AppUserMapper appUserMapper;

    public void updateBrand(Integer userBId, Integer[] id) {
        //根据商户id修改商户关联品牌
        EntityWrapper<MerchantsBrandEntity> merchantsWrapper = new EntityWrapper<>();
        merchantsWrapper.eq("user_b_id", userBId);
        List<MerchantsBrandEntity> merchantsBrandEntities = selectList(merchantsWrapper);
        if (merchantsBrandEntities.isEmpty()){
            //为null就新增
            for (Integer integer : id) {
                MerchantsBrandEntity merchantsBrandEntity = new MerchantsBrandEntity();
                merchantsBrandEntity.setUserBId(String.valueOf(userBId));
                merchantsBrandEntity.setBrandId(String.valueOf(integer));
                merchantsBrandEntity.setCreateTime(new Date());
                merchantsBrandEntity.setState(1);
                insert(merchantsBrandEntity);
                AppUserEntity appUserEntity = appUserMapper.selectById(userBId);
                appUserEntity.setBrandId(String.valueOf(integer));
                appUserMapper.updateById(appUserEntity);
            }
            return;
        }else {
            for (MerchantsBrandEntity brandEntity : merchantsBrandEntities) {
                Long id1 = brandEntity.getId();
                deleteById(id1);
            }
        }
        for (Integer integer : id) {
            MerchantsBrandEntity merchantsBrandEntity = new MerchantsBrandEntity();
            merchantsBrandEntity.setUserBId(String.valueOf(userBId));
            merchantsBrandEntity.setBrandId(String.valueOf(integer));
            merchantsBrandEntity.setCreateTime(new Date());
            merchantsBrandEntity.setState(1);
            insert(merchantsBrandEntity);
            AppUserEntity appUserEntity = appUserMapper.selectById(userBId);
            appUserEntity.setBrandId(String.valueOf(integer));
            appUserMapper.updateById(appUserEntity);
        }
    }
}
