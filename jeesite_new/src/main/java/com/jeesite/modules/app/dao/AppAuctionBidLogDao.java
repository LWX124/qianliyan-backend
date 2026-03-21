/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionBidLog;

import java.util.List;

/**
 * 出价记录DAO接口
 *
 * @author y
 * @version 2023-01-05
 */
@MyBatisDao
public interface AppAuctionBidLogDao extends CrudDao<AppAuctionBidLog> {

    //    根据车辆id查询中标人ID
    Long queryUserIdByCarId(Long carId);

    List<AppAuctionBidLog> findDistinctUserListByCarId(Long carId);

    AppAuctionBidLog selectMustOneByCarId(Long carId);
}