/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppSendRepair;

/**
 * 事故车送修现场业务表DAO接口
 * @author y
 * @version 2022-07-18
 */
@MyBatisDao
public interface AppSendRepairDao extends CrudDao<AppSendRepair> {
	
}