/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionUp;

import java.util.List;

/**
 * 拍卖车上架信息表DAO接口
 * @author y
 * @version 2023-03-10
 */
@MyBatisDao
public interface AppAuctionUpDao extends CrudDao<AppAuctionUp> {
    List<AppAuctionUp> findTodayUpCar();

    List<AppAuctionUp> findTodayEndCar();

    AppAuctionUp findAuctionUpByCarId(Long carId);

    AppAuctionUp findAuctionUp(Long carId);

}