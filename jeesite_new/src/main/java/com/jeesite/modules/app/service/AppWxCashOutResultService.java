/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppWxCashOutResult;
import com.jeesite.modules.app.dao.AppWxCashOutResultDao;

/**
 * 查找微信 订单结果Service
 * @author dh
 * @version 2019-09-04
 */
@Service
@Transactional(readOnly=true)
public class AppWxCashOutResultService extends CrudService<AppWxCashOutResultDao, AppWxCashOutResult> {
	
	/**
	 * 获取单条数据
	 * @param appWxCashOutResult
	 * @return
	 */
	@Override
	public AppWxCashOutResult get(AppWxCashOutResult appWxCashOutResult) {
		return super.get(appWxCashOutResult);
	}
	
	/**
	 * 查询分页数据
	 * @param appWxCashOutResult 查询条件
	 * @param appWxCashOutResult.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppWxCashOutResult> findPage(AppWxCashOutResult appWxCashOutResult) {
		return super.findPage(appWxCashOutResult);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appWxCashOutResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppWxCashOutResult appWxCashOutResult) {
		super.save(appWxCashOutResult);
	}
	
	/**
	 * 更新状态
	 * @param appWxCashOutResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppWxCashOutResult appWxCashOutResult) {
		super.updateStatus(appWxCashOutResult);
	}
	
	/**
	 * 删除数据
	 * @param appWxCashOutResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppWxCashOutResult appWxCashOutResult) {
		super.delete(appWxCashOutResult);
	}
	
}