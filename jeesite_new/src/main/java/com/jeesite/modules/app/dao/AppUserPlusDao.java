/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUserPlus;

/**
 * plus会员DAO接口
 * @author dh
 * @version 2019-09-10
 */
@MyBatisDao
public interface AppUserPlusDao extends CrudDao<AppUserPlus> {

    AppUserPlus findPlusUserByUserId(Integer parentId);

    void insertSelf(AppUserPlus t);
}