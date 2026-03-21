/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppVersion;

/**
 * app_versionDAO接口
 * @author dh
 * @version 2019-11-18
 */
@MyBatisDao
public interface AppVersionDao extends CrudDao<AppVersion> {
	
}