package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackageOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 汽车保养订单表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-21
 */
public interface BizMaintainPackageOrderMapper extends BaseMapper<BizMaintainPackageOrder> {

    String getMaintainOrderNo();

    List<BizMaintainPackageOrder> selectPage(@Param("page") Page<BizMaintainPackageOrder> page, @Param("order") BizMaintainPackageOrder bizMaintainPackage, @Param("condition") String condition);

    List<BizMaintainPackageOrder> selectPageForInner(@Param("page") Page<BizMaintainPackageOrder> page, @Param("order") BizMaintainPackageOrder bizMaintainPackage, @Param("condition") String condition);

    Long countByCondition(@Param("order") BizMaintainPackageOrder order);

    Long countByConditionForInner(@Param("order") BizMaintainPackageOrder order);
}
