/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.QueryDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppLeague;

/**
 * 加盟信息表DAO接口
 * @author zcq
 * @version 2019-08-01
 */
@MyBatisDao
public interface AppLeagueDao extends QueryDao<AppLeague> {
	
}