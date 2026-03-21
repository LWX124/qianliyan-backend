/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppPushBill;
import com.jeesite.modules.app.dao.AppPushBillDao;

import javax.annotation.Resource;

/**
 * 用户扣费记录表Service
 * @author zcq
 * @version 2019-10-24
 */
@Service
@Transactional(readOnly=true)
public class AppPushBillService extends CrudService<AppPushBillDao, AppPushBill> {

	@Resource
	private AppPushBillDao appPushBillDao;
	
	/**
	 * 获取单条数据
	 * @param appPushBill
	 * @return
	 */
	@Override
	public AppPushBill get(AppPushBill appPushBill) {
		return super.get(appPushBill);
	}
	
	/**
	 * 查询分页数据
	 * @param appPushBill 查询条件
	 * @param appPushBill.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppPushBill> findPage(AppPushBill appPushBill) {
		return super.findPage(appPushBill);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appPushBill
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppPushBill appPushBill) {
		super.save(appPushBill);
	}
	
	/**
	 * 更新状态
	 * @param appPushBill
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppPushBill appPushBill) {
		super.updateStatus(appPushBill);
	}
	
	/**
	 * 删除数据
	 * @param appPushBill
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppPushBill appPushBill) {
		super.delete(appPushBill);
	}

	public List<AppPushBill> findisPush(String s, String accId) {
		return appPushBillDao.findisPush(s,accId);
	}

	public List<String> selectNoPush() {
		return appPushBillDao.selectNoPush();
	}

	public List<Integer> selectPushMerchants(String id, Integer type) {
		return appPushBillDao.selectPushMerchants(id,type);
	}

	public List<AppPushBill> selectMerchantsPush(String id) {
		return appPushBillDao.selectMerchantsPush(id);
	}

	public List<AppPushBill> findpushBill(String accId, String type) {
		return appPushBillDao.findpushBill(accId,type);
	}

	public List<String> findPbImg(String accId,Integer type) {
		return appPushBillDao.findPbImg(accId,type);
	}

	public List<AppPushBill> findBySourceAccid(String id, Integer type) {
		return appPushBillDao.findBySourceAccid(id,type);
	}
}