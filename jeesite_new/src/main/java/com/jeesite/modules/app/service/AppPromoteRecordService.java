/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppPromoteRecord;
import com.jeesite.modules.app.dao.AppPromoteRecordDao;

/**
 * 推广金额记录表Service
 * @author zcq
 * @version 2021-04-08
 */
@Service
@Transactional(readOnly=true)
public class AppPromoteRecordService extends CrudService<AppPromoteRecordDao, AppPromoteRecord> {
	
	/**
	 * 获取单条数据
	 * @param appPromoteRecord
	 * @return
	 */
	@Override
	public AppPromoteRecord get(AppPromoteRecord appPromoteRecord) {
		return super.get(appPromoteRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param appPromoteRecord 查询条件
	 * @param appPromoteRecord.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppPromoteRecord> findPage(AppPromoteRecord appPromoteRecord) {
		return super.findPage(appPromoteRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appPromoteRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppPromoteRecord appPromoteRecord) {
		super.save(appPromoteRecord);
	}
	
	/**
	 * 更新状态
	 * @param appPromoteRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppPromoteRecord appPromoteRecord) {
		super.updateStatus(appPromoteRecord);
	}
	
	/**
	 * 删除数据
	 * @param appPromoteRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppPromoteRecord appPromoteRecord) {
		super.delete(appPromoteRecord);
	}
	
}