/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppBusinessConfirm;
import com.jeesite.modules.app.dao.AppBusinessConfirmDao;

import javax.annotation.Resource;

/**
 * 商户业务确认表Service
 * @author zcq
 * @version 2020-07-09
 */
@Service
@Transactional(readOnly=true)
public class AppBusinessConfirmService extends CrudService<AppBusinessConfirmDao, AppBusinessConfirm> {

	@Resource
	private AppBusinessConfirmDao appBusinessConfirmDao;

	
	/**
	 * 获取单条数据
	 * @param appBusinessConfirm
	 * @return
	 */
	@Override
	public AppBusinessConfirm get(AppBusinessConfirm appBusinessConfirm) {
		return super.get(appBusinessConfirm);
	}
	
	/**
	 * 查询分页数据
	 * @param appBusinessConfirm 查询条件
	 * @param appBusinessConfirm.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppBusinessConfirm> findPage(AppBusinessConfirm appBusinessConfirm) {
		return super.findPage(appBusinessConfirm);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appBusinessConfirm
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppBusinessConfirm appBusinessConfirm) {
		super.save(appBusinessConfirm);
	}
	
	/**
	 * 更新状态
	 * @param appBusinessConfirm
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppBusinessConfirm appBusinessConfirm) {
		super.updateStatus(appBusinessConfirm);
	}
	
	/**
	 * 删除数据
	 * @param appBusinessConfirm
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppBusinessConfirm appBusinessConfirm) {
		super.delete(appBusinessConfirm);
	}

	@Transactional(readOnly=false)
	public void insertCus(AppBusinessConfirm appBusinessConfirm) {
		 appBusinessConfirmDao.insertCus(appBusinessConfirm);
	}


	@Transactional(readOnly=false)
	public List<AppBusinessConfirm> selectByUserBId(String userBId) {
		return appBusinessConfirmDao.selectByUserBId(userBId);
	}
}