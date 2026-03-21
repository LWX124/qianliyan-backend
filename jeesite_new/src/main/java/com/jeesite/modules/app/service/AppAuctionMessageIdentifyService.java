/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionMessageIdentify;
import com.jeesite.modules.app.dao.AppAuctionMessageIdentifyDao;

/**
 * 认证信息Service
 * @author dh
 * @version 2023-04-22
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionMessageIdentifyService extends CrudService<AppAuctionMessageIdentifyDao, AppAuctionMessageIdentify> {
	
	/**
	 * 获取单条数据
	 * @param appAuctionMessageIdentify
	 * @return
	 */
	@Override
	public AppAuctionMessageIdentify get(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		return super.get(appAuctionMessageIdentify);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionMessageIdentify 查询条件
	 * @param appAuctionMessageIdentify.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionMessageIdentify> findPage(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		return super.findPage(appAuctionMessageIdentify);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionMessageIdentify
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		super.save(appAuctionMessageIdentify);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionMessageIdentify
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		super.updateStatus(appAuctionMessageIdentify);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionMessageIdentify
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		super.delete(appAuctionMessageIdentify);
	}
	
}