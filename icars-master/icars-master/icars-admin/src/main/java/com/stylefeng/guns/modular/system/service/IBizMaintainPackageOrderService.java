package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackageOrder;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 汽车保养订单表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-21
 */
public interface IBizMaintainPackageOrderService extends IService<BizMaintainPackageOrder> {
    String getMaintainOrderNo();

    List<BizMaintainPackageOrder> selectPage(@Param("page") Page<BizMaintainPackageOrder> page, @Param("order") BizMaintainPackageOrder order, @Param("condition") String condition);

    List<BizMaintainPackageOrder> selectPageForInner(@Param("page") Page<BizMaintainPackageOrder> page, @Param("order") BizMaintainPackageOrder order, @Param("condition") String condition);

    Long countByCondition(@Param("order") BizMaintainPackageOrder order);

    Long countByConditionForInner(@Param("order") BizMaintainPackageOrder order);
}
