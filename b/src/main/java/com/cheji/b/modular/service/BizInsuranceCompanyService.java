package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.BizInsuranceCompanyEntity;
import com.cheji.b.modular.mapper.BizInsuranceCompanyMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保险公司信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@Service
public class BizInsuranceCompanyService extends ServiceImpl<BizInsuranceCompanyMapper, BizInsuranceCompanyEntity> implements IService<BizInsuranceCompanyEntity> {

}
