/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionSubscription;

/**
 * 订阅表DAO接口
 * @author y
 * @version 2022-12-09
 */
@MyBatisDao
public interface AppAuctionSubscriptionDao extends CrudDao<AppAuctionSubscription> {
	
}