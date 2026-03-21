/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppWxBank;
import com.jeesite.modules.app.dao.AppWxBankDao;

/**
 * app_wx_bankService
 * @author dh
 * @version 2019-10-14
 */
@Service
@Transactional(readOnly=true)
public class AppWxBankService extends CrudService<AppWxBankDao, AppWxBank> {
	
	/**
	 * 获取单条数据
	 * @param appWxBank
	 * @return
	 */
	@Override
	public AppWxBank get(AppWxBank appWxBank) {
		return super.get(appWxBank);
	}
	
	/**
	 * 查询分页数据
	 * @param appWxBank 查询条件
	 * @param appWxBank.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppWxBank> findPage(AppWxBank appWxBank) {
		return super.findPage(appWxBank);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appWxBank
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppWxBank appWxBank) {
		super.save(appWxBank);
	}
	
	/**
	 * 更新状态
	 * @param appWxBank
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppWxBank appWxBank) {
		super.updateStatus(appWxBank);
	}
	
	/**
	 * 删除数据
	 * @param appWxBank
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppWxBank appWxBank) {
		super.delete(appWxBank);
	}
	
}