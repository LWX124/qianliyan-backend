/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchants;

/**
 * 商户信息表DAO接口
 * @author zcq
 * @version 2019-08-05
 */
@MyBatisDao
public interface AppMerchantsDao extends CrudDao<AppMerchants> {
    AppMerchants findByName(String name);
}