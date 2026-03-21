/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppPromoteRecord;

/**
 * 推广金额记录表DAO接口
 * @author zcq
 * @version 2021-04-08
 */
@MyBatisDao
public interface AppPromoteRecordDao extends CrudDao<AppPromoteRecord> {
	
}