package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.XcxMaintenance;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 小程序 维修表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-31
 */
public interface XcxMaintenanceMapper extends BaseMapper<XcxMaintenance> {

    List<XcxMaintenance> selectByType(Integer type);
}
