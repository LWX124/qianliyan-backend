/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUserPlus;
import com.jeesite.modules.app.dao.AppUserPlusDao;

import javax.annotation.Resource;

/**
 * plus会员Service
 * @author dh
 * @version 2019-09-10
 */
@Service
@Transactional(readOnly=true)
public class AppUserPlusService extends CrudService<AppUserPlusDao, AppUserPlus> {


	@Resource
	private AppUserPlusDao appUserPlusDao;
	/**
	 * 获取单条数据
	 * @param appUserPlus
	 * @return
	 */
	@Override
	public AppUserPlus get(AppUserPlus appUserPlus) {
		return super.get(appUserPlus);
	}
	
	/**
	 * 查询分页数据
	 * @param appUserPlus 查询条件
	 * @param appUserPlus.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppUserPlus> findPage(AppUserPlus appUserPlus) {
		return super.findPage(appUserPlus);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appUserPlus
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppUserPlus appUserPlus) {
		super.save(appUserPlus);
	}
	
	/**
	 * 更新状态
	 * @param appUserPlus
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppUserPlus appUserPlus) {
		super.updateStatus(appUserPlus);
	}
	
	/**
	 * 删除数据
	 * @param appUserPlus
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppUserPlus appUserPlus) {
		super.delete(appUserPlus);
	}

	public AppUserPlus findPlusUserByUserId(Integer parentId) {
		return appUserPlusDao.findPlusUserByUserId(parentId);

	}
}