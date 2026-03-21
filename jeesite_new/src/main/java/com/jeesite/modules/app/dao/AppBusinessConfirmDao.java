/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppBusinessConfirm;

import java.util.List;

/**
 * 商户业务确认表DAO接口
 * @author zcq
 * @version 2020-07-09
 */
@MyBatisDao
public interface AppBusinessConfirmDao extends CrudDao<AppBusinessConfirm> {

    void insertCus(AppBusinessConfirm appBusinessConfirm);

    List<AppBusinessConfirm> selectByUserBId(String userBId);
}