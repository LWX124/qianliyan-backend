/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppWxCashOutRecord;
import com.jeesite.modules.app.dao.AppWxCashOutRecordDao;

/**
 * 提现记录表Service
 * @author dh
 * @version 2019-10-08
 */
@Service
@Transactional(readOnly=true)
public class AppWxCashOutRecordService extends CrudService<AppWxCashOutRecordDao, AppWxCashOutRecord> {
	
	/**
	 * 获取单条数据
	 * @param appWxCashOutRecord
	 * @return
	 */
	@Override
	public AppWxCashOutRecord get(AppWxCashOutRecord appWxCashOutRecord) {
		return super.get(appWxCashOutRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param appWxCashOutRecord 查询条件
	 * @param appWxCashOutRecord.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppWxCashOutRecord> findPage(AppWxCashOutRecord appWxCashOutRecord) {
		return super.findPage(appWxCashOutRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appWxCashOutRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppWxCashOutRecord appWxCashOutRecord) {
		super.save(appWxCashOutRecord);
	}
	
	/**
	 * 更新状态
	 * @param appWxCashOutRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppWxCashOutRecord appWxCashOutRecord) {
		super.updateStatus(appWxCashOutRecord);
	}
	
	/**
	 * 删除数据
	 * @param appWxCashOutRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppWxCashOutRecord appWxCashOutRecord) {
		super.delete(appWxCashOutRecord);
	}
	
}