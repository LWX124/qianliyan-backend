/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppMessageCar;
import com.jeesite.modules.app.dao.AppMessageCarDao;

import javax.annotation.Resource;

/**
 * app_message_carService
 * @author zcq
 * @version 2021-09-13
 */
@Service
@Transactional(readOnly=true)
public class AppMessageCarService extends CrudService<AppMessageCarDao, AppMessageCar> {


	@Resource
	private AppMessageCarDao appMessageCarDao;
	
	/**
	 * 获取单条数据
	 * @param appMessageCar
	 * @return
	 */
	@Override
	public AppMessageCar get(AppMessageCar appMessageCar) {
		return super.get(appMessageCar);
	}
	
	/**
	 * 查询分页数据
	 * @param appMessageCar 查询条件
	 * @param appMessageCar.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppMessageCar> findPage(AppMessageCar appMessageCar) {
		return super.findPage(appMessageCar);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMessageCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMessageCar appMessageCar) {
		super.save(appMessageCar);
	}
	
	/**
	 * 更新状态
	 * @param appMessageCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMessageCar appMessageCar) {
		super.updateStatus(appMessageCar);
	}
	
	/**
	 * 删除数据
	 * @param appMessageCar
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMessageCar appMessageCar) {
		super.delete(appMessageCar);
	}

	public AppMessageCar findPushBill(String id) {
		return appMessageCarDao.findPushBill(id);
	}
}