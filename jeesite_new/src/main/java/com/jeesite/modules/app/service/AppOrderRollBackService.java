/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppOrderRollBack;
import com.jeesite.modules.app.dao.AppOrderRollBackDao;

/**
 * 库存回滚辅助表Service
 * @author dh
 * @version 2019-12-31
 */
@Service
@Transactional(readOnly=true)
public class AppOrderRollBackService extends CrudService<AppOrderRollBackDao, AppOrderRollBack> {
	
	/**
	 * 获取单条数据
	 * @param appOrderRollBack
	 * @return
	 */
	@Override
	public AppOrderRollBack get(AppOrderRollBack appOrderRollBack) {
		return super.get(appOrderRollBack);
	}
	
	/**
	 * 查询分页数据
	 * @param appOrderRollBack 查询条件
	 * @param appOrderRollBack.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppOrderRollBack> findPage(AppOrderRollBack appOrderRollBack) {
		return super.findPage(appOrderRollBack);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appOrderRollBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppOrderRollBack appOrderRollBack) {
		super.save(appOrderRollBack);
	}
	
	/**
	 * 更新状态
	 * @param appOrderRollBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppOrderRollBack appOrderRollBack) {
		super.updateStatus(appOrderRollBack);
	}
	
	/**
	 * 删除数据
	 * @param appOrderRollBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppOrderRollBack appOrderRollBack) {
		super.delete(appOrderRollBack);
	}
	
}