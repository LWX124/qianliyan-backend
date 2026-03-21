package com.stylefeng.guns.modular.system.service;

import com.stylefeng.guns.modular.system.model.XcxMaintenance;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 小程序 维修表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-31
 */
public interface IXcxMaintenanceService extends IService<XcxMaintenance> {

    List<XcxMaintenance> selectByType(Integer type);
}
