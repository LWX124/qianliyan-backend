/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionSubscription;
import com.jeesite.modules.app.dao.AppAuctionSubscriptionDao;

/**
 * 订阅表Service
 * @author y
 * @version 2022-12-09
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionSubscriptionService extends CrudService<AppAuctionSubscriptionDao, AppAuctionSubscription> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionSubscription
	 * @return
	 */
	@Override
	public AppAuctionSubscription get(AppAuctionSubscription appAuctionSubscription) {
		return super.get(appAuctionSubscription);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionSubscription 查询条件
	 * @param appAuctionSubscription.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionSubscription> findPage(AppAuctionSubscription appAuctionSubscription) {
		return super.findPage(appAuctionSubscription);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionSubscription
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionSubscription appAuctionSubscription) {
		super.save(appAuctionSubscription);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionSubscription
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionSubscription appAuctionSubscription) {
		super.updateStatus(appAuctionSubscription);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionSubscription
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionSubscription appAuctionSubscription) {
		super.delete(appAuctionSubscription);
	}
	
}