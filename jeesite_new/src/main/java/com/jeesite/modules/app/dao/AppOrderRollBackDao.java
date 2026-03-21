/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppOrderRollBack;

import java.util.List;

/**
 * 库存回滚辅助表DAO接口
 * @author dh
 * @version 2019-12-31
 */
@MyBatisDao
public interface AppOrderRollBackDao extends CrudDao<AppOrderRollBack> {

    //查询所有需要回滚库存的订单
    List<AppOrderRollBack> selectUnOpsList();

}
