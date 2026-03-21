/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.CarInfor;
import com.jeesite.modules.app.dao.CarInforDao;

/**
 * car_inforService
 * @author y
 * @version 2022-10-09
 */
@Service
@Transactional(readOnly=true)
public class CarInforService extends CrudService<CarInforDao, CarInfor> {
	
	/**
	 * 获取单条数据
	 * @param carInfor
	 * @return
	 */
	@Override
	public CarInfor get(CarInfor carInfor) {
		return super.get(carInfor);
	}
	
	/**
	 * 查询分页数据
	 * @param carInfor 查询条件
	 * @param carInfor.page 分页对象
	 * @return
	 */
	@Override
	public Page<CarInfor> findPage(CarInfor carInfor) {
		return super.findPage(carInfor);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param carInfor
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CarInfor carInfor) {
		super.save(carInfor);
	}
	
	/**
	 * 更新状态
	 * @param carInfor
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CarInfor carInfor) {
		super.updateStatus(carInfor);
	}
	
	/**
	 * 删除数据
	 * @param carInfor
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CarInfor carInfor) {
		super.delete(carInfor);
	}
	
}