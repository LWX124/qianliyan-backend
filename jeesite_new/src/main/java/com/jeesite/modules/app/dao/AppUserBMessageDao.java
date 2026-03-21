/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUserBMessage;

/**
 * 用户服务信息表DAO接口
 * @author zcq
 * @version 2020-06-22
 */
@MyBatisDao
public interface AppUserBMessageDao extends CrudDao<AppUserBMessage> {

    AppUserBMessage findEmployee(String userBid, int type);
}