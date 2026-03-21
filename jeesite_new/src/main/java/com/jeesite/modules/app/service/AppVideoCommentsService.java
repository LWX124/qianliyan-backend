/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.app.entity.AppVideoComments;
import com.jeesite.modules.app.dao.AppVideoCommentsDao;

/**
 * 视频评论表Service
 * @author zcq
 * @version 2019-08-07
 */
@Service
@Transactional(readOnly=true)
public class AppVideoCommentsService extends TreeService<AppVideoCommentsDao, AppVideoComments> {
	
	/**
	 * 获取单条数据
	 * @param appVideoComments
	 * @return
	 */
	@Override
	public AppVideoComments get(AppVideoComments appVideoComments) {
		return super.get(appVideoComments);
	}
	
	/**
	 * 查询列表数据
	 * @param appVideoComments
	 * @return
	 */
	@Override
	public List<AppVideoComments> findList(AppVideoComments appVideoComments) {
		return super.findList(appVideoComments);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appVideoComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppVideoComments appVideoComments) {
		super.save(appVideoComments);
	}
	
	/**
	 * 更新状态
	 * @param appVideoComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppVideoComments appVideoComments) {
		super.updateStatus(appVideoComments);
	}
	
	/**
	 * 删除数据
	 * @param appVideoComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppVideoComments appVideoComments) {
		super.delete(appVideoComments);
	}
	
}