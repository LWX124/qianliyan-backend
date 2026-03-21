/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppAuctionDao;
import com.jeesite.modules.app.entity.AppAuction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 拍卖业务表Service
 * @author y
 * @version 2022-11-02
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionService extends CrudService<AppAuctionDao, AppAuction> {
	@Resource
	private AppAuctionDao appAuctionDao;

	/**
	 * 获取单条数据
	 * @param appAuction
	 * @return
	 */
	@Override
	public AppAuction get(AppAuction appAuction) {
		return super.get(appAuction);
	}

	/**
	 * 查询分页数据
	 * @param appAuction 查询条件
	 * @return
	 */
	@Override
	public Page<AppAuction> findPage(AppAuction appAuction) {
		return super.findPage(appAuction);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param appAuction
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuction appAuction) {
		super.save(appAuction);
	}

	/**
	 * 更新状态
	 * @param appAuction
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuction appAuction) {
		super.updateStatus(appAuction);
	}

	/**
	 * 删除数据
	 * @param appAuction
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuction appAuction) {
		super.delete(appAuction);
	}

	public AppAuction findAuctionByCarId(Long carId) {
		return appAuctionDao.findAuctionByCarId(carId);
	}

}