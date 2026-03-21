/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppEmployeeSettlement;

/**
 * 理赔老师结算表DAO接口
 * @author zcq
 * @version 2020-07-02
 */
@MyBatisDao
public interface AppEmployeeSettlementDao extends CrudDao<AppEmployeeSettlement> {
	
}