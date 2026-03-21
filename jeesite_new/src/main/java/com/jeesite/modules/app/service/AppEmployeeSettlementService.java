/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppEmployeeSettlement;
import com.jeesite.modules.app.dao.AppEmployeeSettlementDao;

/**
 * 理赔老师结算表Service
 * @author zcq
 * @version 2020-07-02
 */
@Service
@Transactional(readOnly=true)
public class AppEmployeeSettlementService extends CrudService<AppEmployeeSettlementDao, AppEmployeeSettlement> {
	
	/**
	 * 获取单条数据
	 * @param appEmployeeSettlement
	 * @return
	 */
	@Override
	public AppEmployeeSettlement get(AppEmployeeSettlement appEmployeeSettlement) {
		return super.get(appEmployeeSettlement);
	}
	
	/**
	 * 查询分页数据
	 * @param appEmployeeSettlement 查询条件
	 * @param appEmployeeSettlement.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppEmployeeSettlement> findPage(AppEmployeeSettlement appEmployeeSettlement) {
		return super.findPage(appEmployeeSettlement);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appEmployeeSettlement
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppEmployeeSettlement appEmployeeSettlement) {
		super.save(appEmployeeSettlement);
	}
	
	/**
	 * 更新状态
	 * @param appEmployeeSettlement
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppEmployeeSettlement appEmployeeSettlement) {
		super.updateStatus(appEmployeeSettlement);
	}
	
	/**
	 * 删除数据
	 * @param appEmployeeSettlement
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppEmployeeSettlement appEmployeeSettlement) {
		super.delete(appEmployeeSettlement);
	}
	
}