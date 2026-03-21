/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionVipControl;
import com.jeesite.modules.app.dao.AppAuctionVipControlDao;

import javax.annotation.Resource;

/**
 * 竞拍账号联系表Service
 * @author y
 * @version 2022-12-12
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionVipControlService extends CrudService<AppAuctionVipControlDao, AppAuctionVipControl> {
	@Resource
	private AppAuctionVipControlDao vipControlDao;
	/**
	 * 获取单条数据
	 * @param appAuctionVipControl
	 * @return
	 */
	@Override
	public AppAuctionVipControl get(AppAuctionVipControl appAuctionVipControl) {
		return super.get(appAuctionVipControl);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionVipControl 查询条件
	 * @param appAuctionVipControl.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionVipControl> findPage(AppAuctionVipControl appAuctionVipControl) {
		return super.findPage(appAuctionVipControl);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionVipControl
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionVipControl appAuctionVipControl) {
		super.save(appAuctionVipControl);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionVipControl
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionVipControl appAuctionVipControl) {
		super.updateStatus(appAuctionVipControl);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionVipControl
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionVipControl appAuctionVipControl) {
		super.delete(appAuctionVipControl);
	}

	public AppAuctionVipControl getByUserId(String userId) {
		return vipControlDao.getByUserId(userId);
	}

	public List<AppAuctionVipControl> getVips(){
		return vipControlDao.getVips();
	}
}