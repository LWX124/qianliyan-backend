package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.MerchantsInfoBannerEntity;

import java.util.List;

/**
 * <p>
 * 商户详情banner图 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
public interface MerchantsInfoBannerMapper extends BaseMapper<MerchantsInfoBannerEntity> {

    //通过id查询到商户详情banner图片，根据index排序
    List<MerchantsInfoBannerEntity> getBannerById(String merchantsId);
}
