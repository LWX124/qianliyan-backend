/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionCollect;

import java.util.List;

/**
 * 收藏表DAO接口
 * @author y
 * @version 2023-02-16
 */
@MyBatisDao
public interface AppAuctionCollectDao extends CrudDao<AppAuctionCollect> {

    List<AppAuctionCollect> findCollectByCarId(Long carId);

}