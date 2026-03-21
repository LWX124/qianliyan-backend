/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuction;

/**
 * 拍卖业务表DAO接口
 * @author y
 * @version 2023-03-10
 */
@MyBatisDao
public interface AppAuctionDao extends CrudDao<AppAuction> {
    AppAuction findAuctionByCarId(Long carId);
}