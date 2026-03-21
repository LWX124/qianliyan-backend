/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionWarnCar;
import com.jeesite.modules.app.dao.AppAuctionWarnCarDao;

/**
 * 提醒车Service
 * @author y
 * @version 2023-02-15
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionWarnCarService extends CrudService<AppAuctionWarnCarDao, AppAuctionWarnCar> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionWarnCar
	 * @return
	 */
	@Override
	public AppAuctionWarnCar get(AppAuctionWarnCar appAuctionWarnCar) {
		return super.get(appAuctionWarnCar);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionWarnCar 查询条件
	 * @param appAuctionWarnCar.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionWarnCar> findPage(AppAuctionWarnCar appAuctionWarnCar) {
		return super.findPage(appAuctionWarnCar);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionWarnCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionWarnCar appAuctionWarnCar) {
		super.save(appAuctionWarnCar);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionWarnCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionWarnCar appAuctionWarnCar) {
		super.updateStatus(appAuctionWarnCar);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionWarnCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionWarnCar appAuctionWarnCar) {
		super.delete(appAuctionWarnCar);
	}
	
}