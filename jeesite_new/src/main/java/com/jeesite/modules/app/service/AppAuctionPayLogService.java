/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionPayLog;
import com.jeesite.modules.app.dao.AppAuctionPayLogDao;

/**
 * 支付记录 vip充值Service
 * @author dh
 * @version 2023-04-13
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionPayLogService extends CrudService<AppAuctionPayLogDao, AppAuctionPayLog> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionPayLog
	 * @return
	 */
	@Override
	public AppAuctionPayLog get(AppAuctionPayLog appAuctionPayLog) {
		return super.get(appAuctionPayLog);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionPayLog 查询条件
	 * @param appAuctionPayLog.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionPayLog> findPage(AppAuctionPayLog appAuctionPayLog) {
		return super.findPage(appAuctionPayLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionPayLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionPayLog appAuctionPayLog) {
		super.save(appAuctionPayLog);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionPayLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionPayLog appAuctionPayLog) {
		super.updateStatus(appAuctionPayLog);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionPayLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionPayLog appAuctionPayLog) {
		super.delete(appAuctionPayLog);
	}
	
}