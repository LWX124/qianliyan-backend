/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppVideoComments;

/**
 * 视频评论表DAO接口
 * @author zcq
 * @version 2019-08-07
 */
@MyBatisDao
public interface AppVideoCommentsDao extends TreeDao<AppVideoComments> {
	
}