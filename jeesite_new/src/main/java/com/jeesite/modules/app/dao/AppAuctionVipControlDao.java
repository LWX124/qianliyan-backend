/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAuctionVipControl;

import java.util.List;

/**
 * 竞拍账号联系表DAO接口
 * @author y
 * @version 2022-12-12
 */
@MyBatisDao
public interface AppAuctionVipControlDao extends CrudDao<AppAuctionVipControl> {

    AppAuctionVipControl getByUserId(String userId);

    List<AppAuctionVipControl> getVips();
}