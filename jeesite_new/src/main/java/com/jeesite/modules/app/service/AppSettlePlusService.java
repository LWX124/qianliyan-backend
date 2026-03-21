/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */

package com.jeesite.modules.app.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppSettlePlusDao;
import com.jeesite.modules.app.entity.AppSettlePlus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 结算plus会员明细Service
 *
 * @author zcq
 * @version 2019-09-21
 */

@Service
@Transactional(readOnly = true)
public class AppSettlePlusService extends CrudService<AppSettlePlusDao, AppSettlePlus> {


    /**
     * 获取单条数据
     *
     * @param appSettlePlus
     * @return
     */

    @Override
    public AppSettlePlus get(AppSettlePlus appSettlePlus) {
        return super.get(appSettlePlus);
    }


    /**
     * 查询分页数据
     *
     * @param appSettlePlus      查询条件
     * @param appSettlePlus.page 分页对象
     * @return
     */

    @Override
    public Page<AppSettlePlus> findPage(AppSettlePlus appSettlePlus) {
        return super.findPage(appSettlePlus);
    }


    /**
     * 保存数据（插入或更新）
     *
     * @param appSettlePlus
     */

    @Override
    @Transactional(readOnly = false)
    public void save(AppSettlePlus appSettlePlus) {
        super.save(appSettlePlus);
    }


    /**
     * 更新状态
     *
     * @param appSettlePlus
     */

    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppSettlePlus appSettlePlus) {
        super.updateStatus(appSettlePlus);
    }


    /**
     * 删除数据
     *
     * @param appSettlePlus
     */

    @Override
    @Transactional(readOnly = false)
    public void delete(AppSettlePlus appSettlePlus) {
        super.delete(appSettlePlus);
    }

}
