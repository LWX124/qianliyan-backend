/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppInsuranceMerchants;
import com.jeesite.modules.app.dao.AppInsuranceMerchantsDao;

import javax.annotation.Resource;

/**
 * 保险和商户关联表Service
 * @author zcq
 * @version 2020-08-11
 */
@Service
@Transactional(readOnly=true)
public class AppInsuranceMerchantsService extends CrudService<AppInsuranceMerchantsDao, AppInsuranceMerchants> {

	@Resource
	private AppInsuranceMerchantsDao appInsuranceMerchantsDao;

	/**
	 * 获取单条数据
	 * @param appInsuranceMerchants
	 * @return
	 */
	@Override
	public AppInsuranceMerchants get(AppInsuranceMerchants appInsuranceMerchants) {
		return super.get(appInsuranceMerchants);
	}
	
	/**
	 * 查询分页数据
	 * @param appInsuranceMerchants 查询条件
	 * @param appInsuranceMerchants.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppInsuranceMerchants> findPage(AppInsuranceMerchants appInsuranceMerchants) {
		return super.findPage(appInsuranceMerchants);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appInsuranceMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppInsuranceMerchants appInsuranceMerchants) {
		super.save(appInsuranceMerchants);
	}
	
	/**
	 * 更新状态
	 * @param appInsuranceMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppInsuranceMerchants appInsuranceMerchants) {
		super.updateStatus(appInsuranceMerchants);
	}
	
	/**
	 * 删除数据
	 * @param appInsuranceMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppInsuranceMerchants appInsuranceMerchants) {
		super.delete(appInsuranceMerchants);
	}


	public Integer findAgoInsurance(String insId, String userBId) {
		return appInsuranceMerchantsDao.findAgoInsurance(insId,userBId);
	}

	@Transactional(readOnly=false)
	public void insertInsurance(AppInsuranceMerchants insuranceMerchants) {
		appInsuranceMerchantsDao.insertInsurance(insuranceMerchants);
	}
}