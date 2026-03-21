/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppRescueIndent;

import java.util.List;

/**
 * 救援表DAO接口
 * @author dh
 * @version 2020-03-03
 */
@MyBatisDao
public interface AppRescueIndentDao extends CrudDao<AppRescueIndent> {

    AppRescueIndent selectByOutTradeNo(String outTradeNo);

    AppRescueIndent findRescueByOrder(String rescueNumber);

    List<AppRescueIndent> findOverThreeIndent();

}
