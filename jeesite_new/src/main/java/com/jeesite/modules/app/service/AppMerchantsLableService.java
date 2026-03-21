/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.Date;
import java.util.List;

import com.jeesite.modules.app.dao.AppMerchantsDao;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppMerchantsLable;
import com.jeesite.modules.app.dao.AppMerchantsLableDao;

import javax.validation.constraints.NotNull;

/**
 * 商户和标签关系表Service
 * @author zcq
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly=true)
public class AppMerchantsLableService extends CrudService<AppMerchantsLableDao, AppMerchantsLable> {

	@Autowired
	private AppMerchantsLableDao lableDao;
	
	/**
	 * 获取单条数据
	 * @param appMerchantsLable
	 * @return
	 */
	@Override
	public AppMerchantsLable get(AppMerchantsLable appMerchantsLable) {
		return super.get(appMerchantsLable);
	}
	
	/**
	 * 查询分页数据
	 * @param appMerchantsLable 查询条件
	 * @return
	 */
	@Override
	public Page<AppMerchantsLable> findPage(AppMerchantsLable appMerchantsLable) {
		return super.findPage(appMerchantsLable);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMerchantsLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMerchantsLable appMerchantsLable) {
		appMerchantsLable.setCreateTime(new Date());
		appMerchantsLable.setUpdateTime(new Date());
		super.save(appMerchantsLable);
	}

	
	/**
	 * 更新状态
	 * @param appMerchantsLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMerchantsLable appMerchantsLable) {
		super.updateStatus(appMerchantsLable);
	}
	
	/**
	 * 删除数据
	 * @param appMerchantsLable
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMerchantsLable appMerchantsLable) {
		super.delete(appMerchantsLable);
	}

	//查询是否有相同的保存条件
	//	AppMerchantsLable shuju = selectBylable(appMerchantsLable.getMerchantsId(),appMerchantsLable.getLableId());


	@Transactional(readOnly=false)
	public AppMerchantsLable selectByLable(Long merchantsId,Long lableId){
		AppMerchantsLable appMerchantsLable = lableDao.selectBylable(merchantsId,lableId);
		return appMerchantsLable;
	}

}