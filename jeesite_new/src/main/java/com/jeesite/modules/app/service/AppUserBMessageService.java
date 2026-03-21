/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUserBMessage;
import com.jeesite.modules.app.dao.AppUserBMessageDao;

import javax.annotation.Resource;

/**
 * 用户服务信息表Service
 * @author zcq
 * @version 2020-06-22
 */
@Service
@Transactional(readOnly=true)
public class AppUserBMessageService extends CrudService<AppUserBMessageDao, AppUserBMessage> {

	@Resource
	private AppUserBMessageDao appUserBMessageDao;
	
	/**
	 * 获取单条数据
	 * @param appUserBMessage
	 * @return
	 */
	@Override
	public AppUserBMessage get(AppUserBMessage appUserBMessage) {
		return super.get(appUserBMessage);
	}
	
	/**
	 * 查询分页数据
	 * @param appUserBMessage 查询条件
	 * @param appUserBMessage.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppUserBMessage> findPage(AppUserBMessage appUserBMessage) {
		return super.findPage(appUserBMessage);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appUserBMessage
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppUserBMessage appUserBMessage) {
		super.save(appUserBMessage);
	}
	
	/**
	 * 更新状态
	 * @param appUserBMessage
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppUserBMessage appUserBMessage) {
		super.updateStatus(appUserBMessage);
	}
	
	/**
	 * 删除数据
	 * @param appUserBMessage
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppUserBMessage appUserBMessage) {
		super.delete(appUserBMessage);
	}


	public AppUserBMessage findEmployee(String userBid, int type) {
		return appUserBMessageDao.findEmployee(userBid,type);
	}
}