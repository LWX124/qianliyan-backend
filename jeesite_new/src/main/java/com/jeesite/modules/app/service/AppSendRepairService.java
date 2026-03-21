/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppSendRepair;
import com.jeesite.modules.app.dao.AppSendRepairDao;

/**
 * 事故车送修现场业务表Service
 * @author y
 * @version 2022-07-18
 */
@Service
@Transactional(readOnly=true)
public class AppSendRepairService extends CrudService<AppSendRepairDao, AppSendRepair> {
	
	/**
	 * 获取单条数据
	 * @param appSendRepair
	 * @return
	 */
	@Override
	public AppSendRepair get(AppSendRepair appSendRepair) {
		return super.get(appSendRepair);
	}
	
	/**
	 * 查询分页数据
	 * @param appSendRepair 查询条件
	 * @param appSendRepair.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppSendRepair> findPage(AppSendRepair appSendRepair) {
		return super.findPage(appSendRepair);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appSendRepair
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppSendRepair appSendRepair) {
		super.save(appSendRepair);
	}
	
	/**
	 * 更新状态
	 * @param appSendRepair
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppSendRepair appSendRepair) {
		super.updateStatus(appSendRepair);
	}
	
	/**
	 * 删除数据
	 * @param appSendRepair
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppSendRepair appSendRepair) {
		super.delete(appSendRepair);
	}
	
}