/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppWxCashOutResult;

/**
 * 查找微信 订单结果DAO接口
 * @author dh
 * @version 2019-09-04
 */
@MyBatisDao
public interface AppWxCashOutResultDao extends CrudDao<AppWxCashOutResult> {
	
}