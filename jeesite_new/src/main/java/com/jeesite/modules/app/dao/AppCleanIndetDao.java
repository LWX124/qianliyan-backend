/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppCleanIndet;

/**
 * 洗车订单表DAO接口
 * @author dh
 * @version 2019-12-17
 */
@MyBatisDao
public interface AppCleanIndetDao extends CrudDao<AppCleanIndet> {

    AppCleanIndet selectByOutTradeNo(String outTradeNo);

    void updateStatus4SevenDay();


    AppCleanIndet selectByIndentNum(String clearIndetId);

}
