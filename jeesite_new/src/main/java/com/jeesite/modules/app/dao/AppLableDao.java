/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppLable;

/**
 * 标签表DAO接口
 * @author zcq
 * @version 2019-08-19
 */
@MyBatisDao
public interface AppLableDao extends CrudDao<AppLable> {
	
}