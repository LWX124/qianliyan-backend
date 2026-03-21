/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppWxBankInterface;
import com.jeesite.modules.app.dao.AppWxBankInterfaceDao;

/**
 * app_wx_bank_interfaceService
 * @author dh
 * @version 2019-08-30
 */
@Service
@Transactional(readOnly=true)
public class AppWxBankInterfaceService extends CrudService<AppWxBankInterfaceDao, AppWxBankInterface> {
	
	/**
	 * 获取单条数据
	 * @param appWxBankInterface
	 * @return
	 */
	@Override
	public AppWxBankInterface get(AppWxBankInterface appWxBankInterface) {
		return super.get(appWxBankInterface);
	}
	
	/**
	 * 查询分页数据
	 * @param appWxBankInterface 查询条件
	 * @param appWxBankInterface.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppWxBankInterface> findPage(AppWxBankInterface appWxBankInterface) {
		return super.findPage(appWxBankInterface);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appWxBankInterface
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppWxBankInterface appWxBankInterface) {
		super.save(appWxBankInterface);
	}
	
	/**
	 * 更新状态
	 * @param appWxBankInterface
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppWxBankInterface appWxBankInterface) {
		super.updateStatus(appWxBankInterface);
	}
	
	/**
	 * 删除数据
	 * @param appWxBankInterface
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppWxBankInterface appWxBankInterface) {
		super.delete(appWxBankInterface);
	}
	
}