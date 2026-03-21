/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionMessageIdentify;

/**
 * 认证信息DAO接口
 * @author dh
 * @version 2023-04-22
 */
@MyBatisDao
public interface AppAuctionMessageIdentifyDao extends CrudDao<AppAuctionMessageIdentify> {
	
}