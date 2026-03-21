package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.MerchantsInfoBannerEntity;
import com.cheji.web.modular.mapper.MerchantsInfoBannerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商户详情banner图 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class MerchantsInfoBannerService extends ServiceImpl<MerchantsInfoBannerMapper, MerchantsInfoBannerEntity> implements IService<MerchantsInfoBannerEntity> {
    @Resource
    private MerchantsInfoBannerMapper bannerMapper;

    //根据商户id拿到banner图
    public List<MerchantsInfoBannerEntity> getBannerById(String merchantsId) {
        return bannerMapper.getBannerById(merchantsId);
    }
}
