package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackageOrder;
import com.stylefeng.guns.modular.system.dao.BizMaintainPackageOrderMapper;
import com.stylefeng.guns.modular.system.service.IBizMaintainPackageOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 汽车保养订单表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-21
 */
@Service
public class BizMaintainPackageOrderServiceImpl extends ServiceImpl<BizMaintainPackageOrderMapper, BizMaintainPackageOrder> implements IBizMaintainPackageOrderService {
    @Override
    public String getMaintainOrderNo() {
        return this.baseMapper.getMaintainOrderNo();
    }

    @Override
    public List<BizMaintainPackageOrder> selectPage(Page<BizMaintainPackageOrder> page, BizMaintainPackageOrder order, String condition) {
        return this.baseMapper.selectPage(page, order, condition);
    }

    @Override
    public List<BizMaintainPackageOrder> selectPageForInner(Page<BizMaintainPackageOrder> page, BizMaintainPackageOrder order, String condition) {
        return this.baseMapper.selectPageForInner(page, order, condition);
    }

    @Override
    public Long countByCondition(BizMaintainPackageOrder order) {
        return this.baseMapper.countByCondition(order);
    }

    @Override
    public Long countByConditionForInner(BizMaintainPackageOrder order) {
        return this.baseMapper.countByConditionForInner(order);
    }
}
