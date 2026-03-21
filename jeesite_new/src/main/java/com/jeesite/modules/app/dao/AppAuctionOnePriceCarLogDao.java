/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionOnePriceCarLog;

import java.util.List;

/**
 * 一口价车辆支付记录DAO接口
 * @author dh
 * @version 2023-04-16
 */
@MyBatisDao
public interface AppAuctionOnePriceCarLogDao extends CrudDao<AppAuctionOnePriceCarLog> {

    List<AppAuctionOnePriceCarLog> queryOnePriceRefund();

}