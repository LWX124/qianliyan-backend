/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionHot;
import com.jeesite.modules.app.dao.AppAuctionHotDao;

import javax.annotation.Resource;

/**
 * 热点表Service
 * @author y
 * @version 2023-01-04
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionHotService extends CrudService<AppAuctionHotDao, AppAuctionHot> {
	@Resource
	private AppAuctionHotDao auctionHotDao;
	
	/**
	 * 获取单条数据
	 * @param appAuctionHot
	 * @return
	 */
	@Override
	public AppAuctionHot get(AppAuctionHot appAuctionHot) {
		return super.get(appAuctionHot);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionHot 查询条件
	 * @param appAuctionHot.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionHot> findPage(AppAuctionHot appAuctionHot) {
		return super.findPage(appAuctionHot);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionHot
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionHot appAuctionHot) {
		super.save(appAuctionHot);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionHot
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionHot appAuctionHot) {
		super.updateStatus(appAuctionHot);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionHot
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionHot appAuctionHot) {
		super.delete(appAuctionHot);
	}

	public AppAuctionHot findHotByCarId(Long carId) {
		return auctionHotDao.findHotByCarId(carId);
	}

}