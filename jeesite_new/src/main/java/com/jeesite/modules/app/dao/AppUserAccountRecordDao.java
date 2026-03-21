/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUserAccountRecord;

/**
 * 用户金额记录表DAO接口
 * @author dh
 * @version 2019-10-09
 */
@MyBatisDao
public interface AppUserAccountRecordDao extends CrudDao<AppUserAccountRecord> {

    void insertCus(AppUserAccountRecord appUserAccountRecordEntity);
}