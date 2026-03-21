/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.jeesite.modules.app.entity.AppMerchantsInfoBanner;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppMerchants;
import com.jeesite.modules.app.dao.AppMerchantsDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

import javax.validation.constraints.NotBlank;

/**
 * 商户信息表Service
 * @author zcq
 * @version 2019-08-05
 */
@Service
@Transactional(readOnly=true)
public class AppMerchantsService extends CrudService<AppMerchantsDao, AppMerchants> {

	@Autowired
	private AppMerchantsDao appMerchantsDao;
	/**
	 * 获取单条数据
	 * @param appMerchants
	 * @return
	 */

	public AppMerchants findName(String name){
		return appMerchantsDao.findByName(name);
	}

	@Override
	public AppMerchants get(AppMerchants appMerchants) {
		return super.get(appMerchants);
	}
	
	/**
	 * 查询分页数据
	 * @param appMerchants 查询条件
	 * @param appMerchants.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppMerchants> findPage(AppMerchants appMerchants) {
		return super.findPage(appMerchants);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMerchants appMerchants) {
		Double rebates = appMerchants.getRebates();
		String address = appMerchants.getAddress();
		String phoneNumber = appMerchants.getPhoneNumber();
		String state = appMerchants.getState();
		String reason = appMerchants.getReason();
		String name = appMerchants.getName();
		AppMerchants byName = appMerchantsDao.findByName(name);
		byName.setRebates(rebates);
		byName.setPhoneNumber(phoneNumber);
		byName.setAddress(address);
		byName.setState(state);
		byName.setReason(reason);
		super.save(byName);

		// 保存上传图片
		FileUploadUtils.saveFileUpload(appMerchants.getId(), "appMerchants_image");
	}
	
	/**
	 * 更新状态
	 * @param appMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMerchants appMerchants) {
		super.updateStatus(appMerchants);
	}
	
	/**
	 * 删除数据
	 * @param appMerchants
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMerchants appMerchants) {
		super.delete(appMerchants);
	}
	
}