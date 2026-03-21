package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.MerchantsInfoBannerEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 商户详情banner图 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
public interface MerchantsInfoBannerMapper extends BaseMapper<MerchantsInfoBannerEntity> {

    String findUpMerImg(String member);

}
