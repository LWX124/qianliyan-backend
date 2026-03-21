/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppWxpayBack;
import com.jeesite.modules.app.dao.AppWxpayBackDao;

/**
 * 退款Service
 * @author dh
 * @version 2019-12-16
 */
@Service
@Transactional(readOnly=true)
public class AppWxpayBackService extends CrudService<AppWxpayBackDao, AppWxpayBack> {
	
	/**
	 * 获取单条数据
	 * @param appWxpayBack
	 * @return
	 */
	@Override
	public AppWxpayBack get(AppWxpayBack appWxpayBack) {
		return super.get(appWxpayBack);
	}
	
	/**
	 * 查询分页数据
	 * @param appWxpayBack 查询条件
	 * @param appWxpayBack.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppWxpayBack> findPage(AppWxpayBack appWxpayBack) {
		return super.findPage(appWxpayBack);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appWxpayBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppWxpayBack appWxpayBack) {
		super.save(appWxpayBack);
	}
	
	/**
	 * 更新状态
	 * @param appWxpayBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppWxpayBack appWxpayBack) {
		super.updateStatus(appWxpayBack);
	}
	
	/**
	 * 删除数据
	 * @param appWxpayBack
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppWxpayBack appWxpayBack) {
		super.delete(appWxpayBack);
	}
	
}