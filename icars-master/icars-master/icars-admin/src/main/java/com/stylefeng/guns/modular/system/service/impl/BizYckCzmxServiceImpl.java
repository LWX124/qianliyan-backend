package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizYckCzmx;
import com.stylefeng.guns.modular.system.dao.BizYckCzmxMapper;
import com.stylefeng.guns.modular.system.service.IBizYckCzmxService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外部理赔用户预存款操作明细表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-15
 */
@Service
public class BizYckCzmxServiceImpl extends ServiceImpl<BizYckCzmxMapper, BizYckCzmx> implements IBizYckCzmxService {
    @Override
    public List<Map<String, Object>> selectList(Page<BizYckCzmx> page, String condition, String createStartTime, String createEndTime, Integer detailType, String orderByField, boolean isAsc) {
        return this.baseMapper.selectList(page, condition, createStartTime, createEndTime, detailType, orderByField, isAsc);
    }

    @Override
    public List<Map<String, Object>> getOrderWaste(Page<BizYckCzmx> page, String account) {
        return this.baseMapper.getOrderWaste(page, account);
    }

}
