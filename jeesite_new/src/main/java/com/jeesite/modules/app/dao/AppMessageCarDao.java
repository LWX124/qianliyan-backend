/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMessageCar;

/**
 * app_message_carDAO接口
 * @author zcq
 * @version 2021-09-13
 */
@MyBatisDao
public interface AppMessageCarDao extends CrudDao<AppMessageCar> {

    AppMessageCar findPushBill(String id);
}