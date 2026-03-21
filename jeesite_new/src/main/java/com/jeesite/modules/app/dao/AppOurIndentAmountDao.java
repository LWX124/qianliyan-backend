/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppOurIndentAmount;

/**
 * 订单抽成记录表DAO接口
 * @author zcq
 * @version 2019-10-21
 */
@MyBatisDao
public interface AppOurIndentAmountDao extends CrudDao<AppOurIndentAmount> {
	
}