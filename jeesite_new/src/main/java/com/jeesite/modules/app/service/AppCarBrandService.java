/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppCarBrand;
import com.jeesite.modules.app.dao.AppCarBrandDao;

/**
 * app_car_brandService
 * @author zcq
 * @version 2019-10-15
 */
@Service
@Transactional(readOnly=true)
public class AppCarBrandService extends CrudService<AppCarBrandDao, AppCarBrand> {
	
	/**
	 * 获取单条数据
	 * @param appCarBrand
	 * @return
	 */
	@Override
	public AppCarBrand get(AppCarBrand appCarBrand) {
		return super.get(appCarBrand);
	}
	
	/**
	 * 查询分页数据
	 * @param appCarBrand 查询条件
	 * @param appCarBrand.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppCarBrand> findPage(AppCarBrand appCarBrand) {
		return super.findPage(appCarBrand);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appCarBrand
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppCarBrand appCarBrand) {
		super.save(appCarBrand);
	}
	
	/**
	 * 更新状态
	 * @param appCarBrand
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppCarBrand appCarBrand) {
		super.updateStatus(appCarBrand);
	}
	
	/**
	 * 删除数据
	 * @param appCarBrand
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppCarBrand appCarBrand) {
		super.delete(appCarBrand);
	}
	
}