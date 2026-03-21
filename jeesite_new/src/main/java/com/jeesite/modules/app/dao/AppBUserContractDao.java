/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppBUserContract;

import java.util.List;

/**
 * 商户合同图片表DAO接口
 * @author zcq
 * @version 2020-08-10
 */
@MyBatisDao
public interface AppBUserContractDao extends CrudDao<AppBUserContract> {

    void insertNewCon(AppBUserContract appBUserContract);

    Integer selectUrl(String newUrl);

    List<AppBUserContract> selectAll(String id);
}