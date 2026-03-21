/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppYearCheckIndent;

import java.util.List;

/**
 * 年检订单表DAO接口
 * @author zcq
 * @version 2020-04-03
 */
@MyBatisDao
public interface AppYearCheckIndentDao extends CrudDao<AppYearCheckIndent> {

    List<String> findImgs(String sprayId);

    AppYearCheckIndent findByCheckNumber(String yearCheckNumber);

}