package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.InsuranceMerchantsEntity;
import com.cheji.web.modular.mapper.InsuranceMerchantsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 保险和商户关联表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class InsuranceMerchantsService extends ServiceImpl<InsuranceMerchantsMapper, InsuranceMerchantsEntity> implements IService<InsuranceMerchantsEntity> {
    @Resource
    private InsuranceMerchantsMapper insuranceMerchantsMapper;

    public List<InsuranceMerchantsEntity> getInsByMerchants(String merchantsCode) {
        return insuranceMerchantsMapper.getInsByMerchants(merchantsCode);
    }

    public String getCompanyName(String merchantsCode) {
        return insuranceMerchantsMapper.getCompanyName(merchantsCode);
    }
}
