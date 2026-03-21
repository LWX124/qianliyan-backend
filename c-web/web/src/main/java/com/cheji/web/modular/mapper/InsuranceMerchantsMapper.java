package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.InsuranceMerchantsEntity;

import java.util.List;

/**
 * <p>
 * 保险和商户关联表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
public interface InsuranceMerchantsMapper extends BaseMapper<InsuranceMerchantsEntity> {

    List<InsuranceMerchantsEntity> getInsByMerchants(String merchantsId);

    String getCompanyName(String merchantsCode);
}
