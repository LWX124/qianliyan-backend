/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppCleanIndet;
import com.jeesite.modules.app.dao.AppCleanIndetDao;

/**
 * 洗车订单表Service
 * @author dh
 * @version 2019-12-17
 */
@Service
@Transactional(readOnly=true)
public class AppCleanIndetService extends CrudService<AppCleanIndetDao, AppCleanIndet> {
	
	/**
	 * 获取单条数据
	 * @param appCleanIndet
	 * @return
	 */
	@Override
	public AppCleanIndet get(AppCleanIndet appCleanIndet) {
		return super.get(appCleanIndet);
	}
	
	/**
	 * 查询分页数据
	 * @param appCleanIndet 查询条件
	 * @param appCleanIndet.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppCleanIndet> findPage(AppCleanIndet appCleanIndet) {
		return super.findPage(appCleanIndet);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appCleanIndet
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppCleanIndet appCleanIndet) {
		super.save(appCleanIndet);
	}
	
	/**
	 * 更新状态
	 * @param appCleanIndet
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppCleanIndet appCleanIndet) {
		super.updateStatus(appCleanIndet);
	}
	
	/**
	 * 删除数据
	 * @param appCleanIndet
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppCleanIndet appCleanIndet) {
		super.delete(appCleanIndet);
	}
	
}