package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 汽车保养套餐商品表 Mapper 接口
 * </p>
 *
 * @author kosan
 * @since 2018-11-21
 */
public interface BizMaintainPackageMapper extends BaseMapper<BizMaintainPackage> {
    List<BizMaintainPackage> selectPage(@Param("page") Page<BizMaintainPackage> page, @Param("bizMaintainPackage") BizMaintainPackage bizMaintainPackage, @Param("condition") String condition);
}
