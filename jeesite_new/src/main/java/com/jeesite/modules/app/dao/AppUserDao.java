/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppUser;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表DAO接口
 * @author zcq
 * @version 2019-08-08
 */
@MyBatisDao
public interface AppUserDao extends CrudDao<AppUser> {

    AppUser findUserById(Long userId);

    AppUser selectFotUpdate(Integer userId);

    List<Map> select4PlusAll();

    void updateBlackName();

//    AppUser updateBalance(Integer id);
}