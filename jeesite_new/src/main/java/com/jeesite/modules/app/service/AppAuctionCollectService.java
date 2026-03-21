/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionCollect;
import com.jeesite.modules.app.dao.AppAuctionCollectDao;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * 收藏表Service
 * @author y
 * @version 2023-02-16
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionCollectService extends CrudService<AppAuctionCollectDao, AppAuctionCollect> {

	@Resource
	private AppAuctionCollectDao collectDao;
	/**
	 * 获取单条数据
	 * @param appAuctionCollect
	 * @return
	 */
	@Override
	public AppAuctionCollect get(AppAuctionCollect appAuctionCollect) {
		return super.get(appAuctionCollect);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionCollect 查询条件
	 * @param appAuctionCollect.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionCollect> findPage(AppAuctionCollect appAuctionCollect) {
		return super.findPage(appAuctionCollect);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionCollect
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionCollect appAuctionCollect) {
		super.save(appAuctionCollect);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionCollect
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionCollect appAuctionCollect) {
		super.updateStatus(appAuctionCollect);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionCollect
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionCollect appAuctionCollect) {
		super.delete(appAuctionCollect);
	}

	public List<AppAuctionCollect> findCollectByCarId(Long carId) {
		return collectDao.findCollectByCarId(carId);
	}


	
}