/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionBidLog;
import com.jeesite.modules.app.dao.AppAuctionBidLogDao;

/**
 * 出价记录Service
 * @author y
 * @version 2023-01-05
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionBidLogService extends CrudService<AppAuctionBidLogDao, AppAuctionBidLog> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionBidLog
	 * @return
	 */
	@Override
	public AppAuctionBidLog get(AppAuctionBidLog appAuctionBidLog) {
		return super.get(appAuctionBidLog);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionBidLog 查询条件
	 * @return
	 */
	@Override
	public Page<AppAuctionBidLog> findPage(AppAuctionBidLog appAuctionBidLog) {
		return super.findPage(appAuctionBidLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionBidLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionBidLog appAuctionBidLog) {
		super.save(appAuctionBidLog);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionBidLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionBidLog appAuctionBidLog) {
		super.updateStatus(appAuctionBidLog);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionBidLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionBidLog appAuctionBidLog) {
		super.delete(appAuctionBidLog);
	}
	
}