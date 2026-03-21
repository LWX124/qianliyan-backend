package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.InsuranceCompanyEntity;
import com.cheji.web.modular.mapper.InsuranceCompanyMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保险公司信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class InsuranceCompanyService extends ServiceImpl<InsuranceCompanyMapper, InsuranceCompanyEntity> implements IService<InsuranceCompanyEntity> {

}
