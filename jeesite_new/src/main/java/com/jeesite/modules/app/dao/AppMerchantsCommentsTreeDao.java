/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchantsCommentsTree;

/**
 * 商户评论树表DAO接口
 * @author zcq
 * @version 2019-08-12
 */
@MyBatisDao
public interface AppMerchantsCommentsTreeDao extends TreeDao<AppMerchantsCommentsTree> {
	
}