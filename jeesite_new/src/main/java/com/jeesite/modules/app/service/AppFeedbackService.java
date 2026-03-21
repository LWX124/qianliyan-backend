/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.QueryService;
import com.jeesite.modules.app.entity.AppFeedback;
import com.jeesite.modules.app.dao.AppFeedbackDao;

/**
 * app_feedbackService
 * @author zcq
 * @version 2019-09-28
 */
@Service
public class AppFeedbackService extends QueryService<AppFeedbackDao, AppFeedback> {
	
	/**
	 * 获取单条数据
	 * @param appFeedback
	 * @return
	 */
	@Override
	public AppFeedback get(AppFeedback appFeedback) {
		return super.get(appFeedback);
	}
	
	/**
	 * 查询分页数据
	 * @param appFeedback 查询条件
	 * @param appFeedback.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppFeedback> findPage(AppFeedback appFeedback) {
		return super.findPage(appFeedback);
	}
	
}