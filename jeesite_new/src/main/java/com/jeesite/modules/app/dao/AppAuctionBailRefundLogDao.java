/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionBailRefundLog;

import java.util.List;

/**
 * 保证金退款记录DAO接口
 * @author y
 * @version 2023-03-19
 */
@MyBatisDao
public interface AppAuctionBailRefundLogDao extends CrudDao<AppAuctionBailRefundLog> {

    List<AppAuctionBailRefundLog> findRefundUser();
}