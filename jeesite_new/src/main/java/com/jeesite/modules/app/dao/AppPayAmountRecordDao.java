/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppPayAmountRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * 红包金额记录表DAO接口
 * @author zcq
 * @version 2019-08-30
 */
@MyBatisDao
public interface AppPayAmountRecordDao extends CrudDao<AppPayAmountRecord> {

     List<AppPayAmountRecord> findRecord(String id, String userId);

    BigDecimal findAllPayAmount(String id);
}