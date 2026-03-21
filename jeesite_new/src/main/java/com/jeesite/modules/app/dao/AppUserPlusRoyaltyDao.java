/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUserPlusRoyalty;

/**
 * 用户开通plus会员提成DAO接口
 * @author dh
 * @version 2019-09-10
 */
@MyBatisDao
public interface AppUserPlusRoyaltyDao extends CrudDao<AppUserPlusRoyalty> {
	
}