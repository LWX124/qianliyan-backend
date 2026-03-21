package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.BizInsuranceCompany;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 保险公司信息表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-08
 */
public interface BizInsuranceCompanyMapper extends BaseMapper<BizInsuranceCompany> {
    /**
     * 获取ztree的节点列表
     */
    List<BizInsuranceCompany> list();
}
