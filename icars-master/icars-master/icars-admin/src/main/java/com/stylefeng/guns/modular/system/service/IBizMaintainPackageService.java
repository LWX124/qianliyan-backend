package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizMaintainPackage;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 汽车保养套餐商品表 服务类
 * </p>
 *
 * @author kosan
 * @since 2018-11-21
 */
public interface IBizMaintainPackageService extends IService<BizMaintainPackage> {
    List<BizMaintainPackage> selectPage(@Param("page") Page<BizMaintainPackage> page, @Param("bizMaintainPackage") BizMaintainPackage bizMaintainPackage, @Param("condition") String condition);
}
