/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppRepeatAccident;

import java.util.List;

/**
 * 重复事故记录表DAO接口
 * @author zcq
 * @version 2019-11-29
 */
@MyBatisDao
public interface AppRepeatAccidentDao extends CrudDao<AppRepeatAccident> {

    AppRepeatAccident updateRepaer(String reapetAccId, String source,String accId);

    List<AppRepeatAccident> selectAllrepeat(String accId, String type);
}