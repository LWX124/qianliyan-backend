/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionBailLog;
import com.jeesite.modules.app.dao.AppAuctionBailLogDao;

/**
 * 支付保证金记录Service
 * @author y
 * @version 2023-03-19
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionBailLogService extends CrudService<AppAuctionBailLogDao, AppAuctionBailLog> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionBailLog
	 * @return
	 */
	@Override
	public AppAuctionBailLog get(AppAuctionBailLog appAuctionBailLog) {
		return super.get(appAuctionBailLog);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionBailLog 查询条件
	 * @return
	 */
	@Override
	public Page<AppAuctionBailLog> findPage(AppAuctionBailLog appAuctionBailLog) {
		return super.findPage(appAuctionBailLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionBailLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionBailLog appAuctionBailLog) {
		super.save(appAuctionBailLog);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionBailLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionBailLog appAuctionBailLog) {
		super.updateStatus(appAuctionBailLog);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionBailLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionBailLog appAuctionBailLog) {
		super.delete(appAuctionBailLog);
	}
	
}