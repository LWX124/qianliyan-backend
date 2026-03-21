package com.stylefeng.guns.modular.system.service.impl;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.BizInsuranceCompany;
import com.stylefeng.guns.modular.system.dao.BizInsuranceCompanyMapper;
import com.stylefeng.guns.modular.system.service.IBizInsuranceCompanyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 保险公司信息表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-08
 */
@Service
public class BizInsuranceCompanyServiceImpl extends ServiceImpl<BizInsuranceCompanyMapper, BizInsuranceCompany> implements IBizInsuranceCompanyService {
    @Override
    public List<BizInsuranceCompany> list() {
        return this.baseMapper.list();
    }
}
