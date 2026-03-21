/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.QueryDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppFeedback;

/**
 * app_feedbackDAO接口
 * @author zcq
 * @version 2019-09-28
 */
@MyBatisDao
public interface AppFeedbackDao extends QueryDao<AppFeedback> {
	
}