/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppSprayPaintIndent;

import java.util.List;

/**
 * 喷漆订单表DAO接口
 * @author zcq
 * @version 2020-03-24
 */
@MyBatisDao
public interface AppSprayPaintIndentDao extends CrudDao<AppSprayPaintIndent> {

    List<String> findImgs(String sprayId,Integer type);

    AppSprayPaintIndent findByNumber(String rescueNumber);
}