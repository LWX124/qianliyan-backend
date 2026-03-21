/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppWxpayBack;

/**
 * 退款DAO接口
 * @author dh
 * @version 2019-12-16
 */
@MyBatisDao
public interface AppWxpayBackDao extends CrudDao<AppWxpayBack> {
	
}