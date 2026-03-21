/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppJgPush;

import java.util.List;

/**
 * 激光推送表DAO接口
 * @author zcq
 * @version 2019-11-13
 */
@MyBatisDao
public interface AppJgPushDao extends CrudDao<AppJgPush> {

    void insertJpush(AppJgPush appJgPush);

    List<AppJgPush> selectCPush();

    List<AppJgPush> selectBPush();
}