/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppBUserContract;
import com.jeesite.modules.app.dao.AppBUserContractDao;

import javax.annotation.Resource;

/**
 * 商户合同图片表Service
 * @author zcq
 * @version 2020-08-10
 */
@Service
@Transactional(readOnly=true)
public class AppBUserContractService extends CrudService<AppBUserContractDao, AppBUserContract> {

	@Resource
	private AppBUserContractDao appBUserContractDao;
	
	/**
	 * 获取单条数据
	 * @param appBUserContract
	 * @return
	 */
	@Override
	public AppBUserContract get(AppBUserContract appBUserContract) {
		return super.get(appBUserContract);
	}
	
	/**
	 * 查询分页数据
	 * @param appBUserContract 查询条件
	 * @param appBUserContract.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppBUserContract> findPage(AppBUserContract appBUserContract) {
		return super.findPage(appBUserContract);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appBUserContract
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppBUserContract appBUserContract) {
		super.save(appBUserContract);
	}
	
	/**
	 * 更新状态
	 * @param appBUserContract
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppBUserContract appBUserContract) {
		super.updateStatus(appBUserContract);
	}
	
	/**
	 * 删除数据
	 * @param appBUserContract
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppBUserContract appBUserContract) {
		super.delete(appBUserContract);
	}

	@Transactional(readOnly=false)
	public void insertNewCon(AppBUserContract appBUserContract) {
		appBUserContractDao.insertNewCon(appBUserContract);
	}

	public Integer selectUrl(String newUrl) {
		return appBUserContractDao.selectUrl(newUrl);
	}

	public List<AppBUserContract> selectAll(String id) {
		return appBUserContractDao.selectAll(id);
	}
}