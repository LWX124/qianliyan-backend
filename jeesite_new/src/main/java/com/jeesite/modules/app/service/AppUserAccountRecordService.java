/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUserAccountRecord;
import com.jeesite.modules.app.dao.AppUserAccountRecordDao;

/**
 * 用户金额记录表Service
 * @author dh
 * @version 2019-10-31
 */
@Service
@Transactional(readOnly=true)
public class AppUserAccountRecordService extends CrudService<AppUserAccountRecordDao, AppUserAccountRecord> {
	
	/**
	 * 获取单条数据
	 * @param appUserAccountRecord
	 * @return
	 */
	@Override
	public AppUserAccountRecord get(AppUserAccountRecord appUserAccountRecord) {
		return super.get(appUserAccountRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param appUserAccountRecord 查询条件
	 * @param appUserAccountRecord.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppUserAccountRecord> findPage(AppUserAccountRecord appUserAccountRecord) {
		return super.findPage(appUserAccountRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appUserAccountRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppUserAccountRecord appUserAccountRecord) {
		super.save(appUserAccountRecord);
	}
	
	/**
	 * 更新状态
	 * @param appUserAccountRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppUserAccountRecord appUserAccountRecord) {
		super.updateStatus(appUserAccountRecord);
	}
	
	/**
	 * 删除数据
	 * @param appUserAccountRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppUserAccountRecord appUserAccountRecord) {
		super.delete(appUserAccountRecord);
	}
	
}