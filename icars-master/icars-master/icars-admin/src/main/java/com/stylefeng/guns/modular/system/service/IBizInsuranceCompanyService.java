package com.stylefeng.guns.modular.system.service;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.BizInsuranceCompany;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 保险公司信息表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-08
 */
public interface IBizInsuranceCompanyService extends IService<BizInsuranceCompany> {
    /**
     * 获取ztree的节点列表
     */
    List<BizInsuranceCompany> list();
}
