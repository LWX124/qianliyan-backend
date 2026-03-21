/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppRepeatAccident;
import com.jeesite.modules.app.dao.AppRepeatAccidentDao;

import javax.annotation.Resource;

/**
 * 重复事故记录表Service
 * @author zcq
 * @version 2019-11-29
 */
@Service
@Transactional(readOnly=true)
public class AppRepeatAccidentService extends CrudService<AppRepeatAccidentDao, AppRepeatAccident> {

	@Resource
	private AppRepeatAccidentDao appRepeatAccidentDao;
	
	/**
	 * 获取单条数据
	 * @param appRepeatAccident
	 * @return
	 */
	@Override
	public AppRepeatAccident get(AppRepeatAccident appRepeatAccident) {
		return super.get(appRepeatAccident);
	}
	
	/**
	 * 查询分页数据
	 * @param appRepeatAccident 查询条件
	 * @param appRepeatAccident.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppRepeatAccident> findPage(AppRepeatAccident appRepeatAccident) {
		return super.findPage(appRepeatAccident);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appRepeatAccident
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppRepeatAccident appRepeatAccident) {
		super.save(appRepeatAccident);
	}
	
	/**
	 * 更新状态
	 * @param appRepeatAccident
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppRepeatAccident appRepeatAccident) {
		super.updateStatus(appRepeatAccident);
	}
	
	/**
	 * 删除数据
	 * @param appRepeatAccident
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppRepeatAccident appRepeatAccident) {
		super.delete(appRepeatAccident);
	}

    public AppRepeatAccident updateRepear(String reapetAccId, String source,String accId) {
		return appRepeatAccidentDao.updateRepaer(reapetAccId,source,accId);
    }

	public List<AppRepeatAccident> selectAllrepeat(String accId, String type) {
		return appRepeatAccidentDao.selectAllrepeat(accId,type);
	}
}