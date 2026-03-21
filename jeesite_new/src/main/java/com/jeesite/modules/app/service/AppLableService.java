/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppLable;
import com.jeesite.modules.app.dao.AppLableDao;

/**
 * 标签表Service
 * @author zcq
 * @version 2019-08-19
 */
@Service
@Transactional(readOnly=true)
public class AppLableService extends CrudService<AppLableDao, AppLable> {
	
	/**
	 * 获取单条数据
	 * @param appLable
	 * @return
	 */
	@Override
	public AppLable get(AppLable appLable) {
		return super.get(appLable);
	}
	
	/**
	 * 查询分页数据
	 * @param appLable 查询条件
	 * @param appLable.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppLable> findPage(AppLable appLable) {
		return super.findPage(appLable);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppLable appLable) {
		super.save(appLable);
	}
	
	/**
	 * 更新状态
	 * @param appLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppLable appLable) {
		super.updateStatus(appLable);
	}
	
	/**
	 * 删除数据
	 * @param appLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppLable appLable) {
		super.delete(appLable);
	}
	
}