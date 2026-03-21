/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionWithdrawcash;

import java.util.List;

/**
 * 退vip提现申请DAO接口
 * @author dh
 * @version 2023-04-05
 */
@MyBatisDao
public interface AppAuctionWithdrawcashDao extends CrudDao<AppAuctionWithdrawcash> {

    List<AppAuctionWithdrawcash> findAllBack(int state);
}