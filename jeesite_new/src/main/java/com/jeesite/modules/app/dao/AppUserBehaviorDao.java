/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUserBehavior;

import java.util.List;

/**
 * 用户视频统计个数DAO接口
 * @author zcq
 * @version 2019-12-04
 */
@MyBatisDao
public interface AppUserBehaviorDao extends CrudDao<AppUserBehavior> {

    AppUserBehavior selectByUserId(String userId);

    List<AppUserBehavior> allselect(Integer pageoffset);

}