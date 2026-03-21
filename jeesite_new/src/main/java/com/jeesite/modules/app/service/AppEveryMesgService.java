/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.List;

import com.jeesite.modules.app.entity.AppAccidentRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppEveryMesg;
import com.jeesite.modules.app.dao.AppEveryMesgDao;

import javax.annotation.Resource;

/**
 * 每日数据Service
 * @author zcq
 * @version 2019-12-02
 */
@Service
@Transactional(readOnly=true)
public class AppEveryMesgService extends CrudService<AppEveryMesgDao, AppEveryMesg> {

	@Resource
	private AppEveryMesgDao appEveryMesgDao;
	/**
	 * 获取单条数据
	 * @param appEveryMesg
	 * @return
	 */
	@Override
	public AppEveryMesg get(AppEveryMesg appEveryMesg) {
		return super.get(appEveryMesg);
	}
	
	/**
	 * 查询分页数据
	 * @param appEveryMesg 查询条件
	 * @param appEveryMesg.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppEveryMesg> findPage(AppEveryMesg appEveryMesg) {
		return super.findPage(appEveryMesg);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appEveryMesg
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppEveryMesg appEveryMesg) {
		super.save(appEveryMesg);
	}
	
	/**
	 * 更新状态
	 * @param appEveryMesg
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppEveryMesg appEveryMesg) {
		super.updateStatus(appEveryMesg);
	}
	
	/**
	 * 删除数据
	 * @param appEveryMesg
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppEveryMesg appEveryMesg) {
		super.delete(appEveryMesg);
	}

	public AppEveryMesg selectTwoAgo(String format) {
		return appEveryMesgDao.selectTwoAgo(format);
	}

	public AppAccidentRecord selectPayAmountAndCount(String format) {
		return appEveryMesgDao.selectPayAmountAndCount(format);
	}

	public AppAccidentRecord selecTodayAccident(String format) {
		return appEveryMesgDao.selectTodatAccident(format);
	}

	public AppAccidentRecord selectPlusPayAmountCount(String format) {
		return appEveryMesgDao.selectPlusPayAmountCount(format);
	}

	public AppAccidentRecord selectPassAccidnt(String format) {
		return appEveryMesgDao.selectPassAccident(format);
	}

	public AppAccidentRecord selectWxUp(String format) {
		return appEveryMesgDao.selectWxUp(format);
	}

	public BigDecimal selectWxAmount(String format) {
		return appEveryMesgDao.selectWxAomunt(format);
	}

	public String selectWxCount(String format) {
		return appEveryMesgDao.selectWxcount(format);
	}
}