/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionPayLog;

import java.util.List;

/**
 * 支付记录 vip充值DAO接口
 * @author dh
 * @version 2023-04-05
 */
@MyBatisDao
public interface AppAuctionPayLogDao extends CrudDao<AppAuctionPayLog> {

    List<AppAuctionPayLog> queryByUserId(Integer userId);
}