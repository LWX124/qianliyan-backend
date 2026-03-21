/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppSprayPaintIndent;
import com.jeesite.modules.app.dao.AppSprayPaintIndentDao;

import javax.annotation.Resource;

/**
 * 喷漆订单表Service
 * @author zcq
 * @version 2020-03-24
 */
@Service
@Transactional(readOnly=true)
public class AppSprayPaintIndentService extends CrudService<AppSprayPaintIndentDao, AppSprayPaintIndent> {

	@Resource
	private AppSprayPaintIndentDao appSprayPaintIndentDao;
	
	/**
	 * 获取单条数据
	 * @param appSprayPaintIndent
	 * @return
	 */
	@Override
	public AppSprayPaintIndent get(AppSprayPaintIndent appSprayPaintIndent) {
		return super.get(appSprayPaintIndent);
	}
	
	/**
	 * 查询分页数据
	 * @param appSprayPaintIndent 查询条件
	 * @param appSprayPaintIndent.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppSprayPaintIndent> findPage(AppSprayPaintIndent appSprayPaintIndent) {
		return super.findPage(appSprayPaintIndent);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appSprayPaintIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppSprayPaintIndent appSprayPaintIndent) {
		super.save(appSprayPaintIndent);
	}
	
	/**
	 * 更新状态
	 * @param appSprayPaintIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppSprayPaintIndent appSprayPaintIndent) {
		super.updateStatus(appSprayPaintIndent);
	}
	
	/**
	 * 删除数据
	 * @param appSprayPaintIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppSprayPaintIndent appSprayPaintIndent) {
		super.delete(appSprayPaintIndent);
	}

	public List<String> findImgs(String sprayId, Integer type) {
		return appSprayPaintIndentDao.findImgs(sprayId,type);
	}

	public AppSprayPaintIndent findBySprayNumber(String rescueNumber) {
		return appSprayPaintIndentDao.findByNumber(rescueNumber);
	}
}