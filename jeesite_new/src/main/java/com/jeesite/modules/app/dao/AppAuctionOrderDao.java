/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionOrder;

/**
 * 拍卖订单表DAO接口
 * @author y
 * @version 2023-03-12
 */
@MyBatisDao
public interface AppAuctionOrderDao extends CrudDao<AppAuctionOrder> {
	
}