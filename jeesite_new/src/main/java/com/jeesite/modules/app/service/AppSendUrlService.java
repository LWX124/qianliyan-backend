/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.jeesite.modules.app.entity.AppSendOutSheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppSendUrl;
import com.jeesite.modules.app.dao.AppSendUrlDao;

import javax.annotation.Resource;

/**
 * 派单记录图片表Service
 * @author zcq
 * @version 2020-10-15
 */
@Service
@Transactional(readOnly=true)
public class AppSendUrlService extends CrudService<AppSendUrlDao, AppSendUrl> {

	@Resource
	private AppSendUrlDao appSendUrlDao;
	
	/**
	 * 获取单条数据
	 * @param appSendUrl
	 * @return
	 */
	@Override
	public AppSendUrl get(AppSendUrl appSendUrl) {
		return super.get(appSendUrl);
	}
	
	/**
	 * 查询分页数据
	 * @param appSendUrl 查询条件
	 * @param appSendUrl.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppSendUrl> findPage(AppSendUrl appSendUrl) {
		return super.findPage(appSendUrl);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appSendUrl
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppSendUrl appSendUrl) {
		super.save(appSendUrl);
	}
	
	/**
	 * 更新状态
	 * @param appSendUrl
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppSendUrl appSendUrl) {
		super.updateStatus(appSendUrl);
	}
	
	/**
	 * 删除数据
	 * @param appSendUrl
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppSendUrl appSendUrl) {
		super.delete(appSendUrl);
	}

	@Transactional(readOnly=false)
	public void insertSendSheet(AppSendUrl appSendUrl) {
		appSendUrlDao.insertSendSheet(appSendUrl);
	}

	public List<String> findImg(String pushBillId, Integer source, Integer type) {
		return appSendUrlDao.findImg(pushBillId,source,type);
	}

	public List<String> findcarImg(String pushBillId,Integer source, Integer type) {
		return appSendUrlDao.findcarImg(pushBillId,source,type);
	}
}