package com.stylefeng.guns.modular.system.service.impl;

import com.stylefeng.guns.modular.system.model.XcxMaintenance;
import com.stylefeng.guns.modular.system.dao.XcxMaintenanceMapper;
import com.stylefeng.guns.modular.system.service.IXcxMaintenanceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 小程序 维修表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-31
 */
@Service
public class XcxMaintenanceServiceImpl extends ServiceImpl<XcxMaintenanceMapper, XcxMaintenance> implements IXcxMaintenanceService {

    @Override
    public List<XcxMaintenance> selectByType(Integer type){
        return this.baseMapper.selectByType(type);
    }

}
