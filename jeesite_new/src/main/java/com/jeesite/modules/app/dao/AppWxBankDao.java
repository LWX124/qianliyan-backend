/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppWxBank;

/**
 * app_wx_bankDAO接口
 * @author dh
 * @version 2019-10-14
 */
@MyBatisDao
public interface AppWxBankDao extends CrudDao<AppWxBank> {
	
}