/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppOurIndentAmount;
import com.jeesite.modules.app.dao.AppOurIndentAmountDao;

/**
 * 订单抽成记录表Service
 * @author zcq
 * @version 2019-10-21
 */
@Service
@Transactional(readOnly=true)
public class AppOurIndentAmountService extends CrudService<AppOurIndentAmountDao, AppOurIndentAmount> {
	
	/**
	 * 获取单条数据
	 * @param appOurIndentAmount
	 * @return
	 */
	@Override
	public AppOurIndentAmount get(AppOurIndentAmount appOurIndentAmount) {
		return super.get(appOurIndentAmount);
	}
	
	/**
	 * 查询分页数据
	 * @param appOurIndentAmount 查询条件
	 * @param appOurIndentAmount.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppOurIndentAmount> findPage(AppOurIndentAmount appOurIndentAmount) {
		return super.findPage(appOurIndentAmount);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appOurIndentAmount
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppOurIndentAmount appOurIndentAmount) {
		super.save(appOurIndentAmount);
	}
	
	/**
	 * 更新状态
	 * @param appOurIndentAmount
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppOurIndentAmount appOurIndentAmount) {
		super.updateStatus(appOurIndentAmount);
	}
	
	/**
	 * 删除数据
	 * @param appOurIndentAmount
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppOurIndentAmount appOurIndentAmount) {
		super.delete(appOurIndentAmount);
	}
	
}