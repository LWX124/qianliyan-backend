/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionHot;

/**
 * 热点表DAO接口
 * @author y
 * @version 2023-01-04
 */
@MyBatisDao
public interface AppAuctionHotDao extends CrudDao<AppAuctionHot> {
    AppAuctionHot findHotByCarId(Long carId);
	
}