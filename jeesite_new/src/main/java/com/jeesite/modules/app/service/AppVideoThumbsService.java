/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppVideoThumbs;
import com.jeesite.modules.app.dao.AppVideoThumbsDao;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * 视频点赞表Service
 * @author zcq
 * @version 2019-09-17
 */
@Service
@Transactional(readOnly=true)
public class AppVideoThumbsService extends CrudService<AppVideoThumbsDao, AppVideoThumbs> {

	@Resource
	private AppVideoThumbsDao appVideoThumbsDao;
	
	/**
	 * 获取单条数据
	 * @param appVideoThumbs
	 * @return
	 */
	@Override
	public AppVideoThumbs get(AppVideoThumbs appVideoThumbs) {
		return super.get(appVideoThumbs);
	}
	
	/**
	 * 查询分页数据
	 * @param appVideoThumbs 查询条件
	 * @param appVideoThumbs.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppVideoThumbs> findPage(AppVideoThumbs appVideoThumbs) {
		return super.findPage(appVideoThumbs);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appVideoThumbs
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppVideoThumbs appVideoThumbs) {
		super.save(appVideoThumbs);
	}
	
	/**
	 * 更新状态
	 * @param appVideoThumbs
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppVideoThumbs appVideoThumbs) {
		super.updateStatus(appVideoThumbs);
	}
	
	/**
	 * 删除数据
	 * @param appVideoThumbs
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppVideoThumbs appVideoThumbs) {
		super.delete(appVideoThumbs);
	}

	@Transactional(readOnly = false)
    public AppVideoThumbs getByVideoIdAndUserId( String videoId,String userId) {
		return appVideoThumbsDao.findByUserIdAndVideoId(videoId,userId);
    }
}