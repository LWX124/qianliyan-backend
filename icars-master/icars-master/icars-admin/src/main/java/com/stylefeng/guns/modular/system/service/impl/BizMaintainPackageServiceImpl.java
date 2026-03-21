package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackage;
import com.stylefeng.guns.modular.system.dao.BizMaintainPackageMapper;
import com.stylefeng.guns.modular.system.service.IBizMaintainPackageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 汽车保养套餐商品表 服务实现类
 * </p>
 *
 * @author kosan
 * @since 2018-11-21
 */
@Service
public class BizMaintainPackageServiceImpl extends ServiceImpl<BizMaintainPackageMapper, BizMaintainPackage> implements IBizMaintainPackageService {

    @Override
    public List<BizMaintainPackage> selectPage(Page<BizMaintainPackage> page, BizMaintainPackage bizMaintainPackage, String condition) {
        return this.baseMapper.selectPage(page, bizMaintainPackage, condition);
    }
}
