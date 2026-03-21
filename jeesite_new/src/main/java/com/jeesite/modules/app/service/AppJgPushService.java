/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppJgPush;
import com.jeesite.modules.app.dao.AppJgPushDao;

import javax.annotation.Resource;

/**
 * 激光推送表Service
 * @author zcq
 * @version 2019-11-13
 */
@Service
@Transactional(readOnly=true)
public class AppJgPushService extends CrudService<AppJgPushDao, AppJgPush> {

	@Resource
	private AppJgPushDao appJgPushDao;
	/**
	 * 获取单条数据
	 * @param appJgPush
	 * @return
	 */
	@Override
	public AppJgPush get(AppJgPush appJgPush) {
		return super.get(appJgPush);
	}
	
	/**
	 * 查询分页数据
	 * @param appJgPush 查询条件
	 * @param appJgPush.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppJgPush> findPage(AppJgPush appJgPush) {
		return super.findPage(appJgPush);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appJgPush
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppJgPush appJgPush) {
		super.save(appJgPush);
	}
	
	/**
	 * 更新状态
	 * @param appJgPush
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppJgPush appJgPush) {
		super.updateStatus(appJgPush);
	}
	
	/**
	 * 删除数据
	 * @param appJgPush
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppJgPush appJgPush) {
		super.delete(appJgPush);
	}


	@Transactional(readOnly = false)
	public void insertJpush(AppJgPush appJgPush){
		appJgPushDao.insertJpush(appJgPush);
	}

	public List<AppJgPush> selectCPush() {
		return appJgPushDao.selectCPush();
	}

	public List<AppJgPush> selectBPush() {
		return appJgPushDao.selectBPush();
	}
}