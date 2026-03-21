/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppClaimTeacher;
import com.jeesite.modules.app.dao.AppClaimTeacherDao;

import javax.annotation.Resource;

/**
 * 理赔老师表Service
 * @author zcq
 * @version 2021-05-17
 */
@Service
@Transactional(readOnly=true)
public class AppClaimTeacherService extends CrudService<AppClaimTeacherDao, AppClaimTeacher> {

	@Resource
	private AppClaimTeacherDao appClaimTeacherDao;
	
	/**
	 * 获取单条数据
	 * @param appClaimTeacher
	 * @return
	 */
	@Override
	public AppClaimTeacher get(AppClaimTeacher appClaimTeacher) {
		return super.get(appClaimTeacher);
	}
	
	/**
	 * 查询分页数据
	 * @param appClaimTeacher 查询条件
	 * @param appClaimTeacher.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppClaimTeacher> findPage(AppClaimTeacher appClaimTeacher) {
		return super.findPage(appClaimTeacher);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appClaimTeacher
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppClaimTeacher appClaimTeacher) {
		super.save(appClaimTeacher);
	}
	
	/**
	 * 更新状态
	 * @param appClaimTeacher
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppClaimTeacher appClaimTeacher) {
		super.updateStatus(appClaimTeacher);
	}
	
	/**
	 * 删除数据
	 * @param appClaimTeacher
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppClaimTeacher appClaimTeacher) {
		super.delete(appClaimTeacher);
	}

	@Transactional(readOnly=false)
	public List<AppClaimTeacher> findMerchants() {
		return appClaimTeacherDao.findMerchants();
	}

	public List<AppClaimTeacher> findClaTeacher() {
		return appClaimTeacherDao.findClaTeacher();
	}

	public List<AppClaimTeacher> findAllClaimTeacher() {
		return appClaimTeacherDao.findAllClaimTeacher();
	}

	public Integer findGoMess(Integer userId) {
		return appClaimTeacherDao.findGoMess(userId);
	}

	public Integer findAllMessCount(Integer userId) {
		return appClaimTeacherDao.findAllMessCount(userId);
	}

	public Integer findCheckMessage(Integer userId) {
		return appClaimTeacherDao.findCheckMessage(userId);
	}

	public Integer findFinshMess(Integer userId) {
		return appClaimTeacherDao.findFinshMess(userId);
	}

	public Integer findDealCount(Integer userId) {
		return appClaimTeacherDao.findDealCount(userId);
	}

	public BigDecimal findDealOutput(Integer userId) {
		return appClaimTeacherDao.findDealOutPut(userId);
	}



	public Integer findGoMessMonth(Integer userId) {
		return appClaimTeacherDao.findGoMessMonth(userId);
	}

	public Integer findAllMessCountMonth(Integer userId) {
		return appClaimTeacherDao.findAllMessCountMonth(userId);
	}

	public Integer findCheckMessageMonth(Integer userId) {
		return appClaimTeacherDao.findCheckMessageMonth(userId);
	}

	public Integer findFinshMessMonth(Integer userId) {
		return appClaimTeacherDao.findFinshMessMonth(userId);
	}

	public Integer findDealCountMonth(Integer userId) {
		return appClaimTeacherDao.findDealCountMonth(userId);
	}

	public AppClaimTeacher findClaimByUserId(Integer userId) {
		return appClaimTeacherDao.findClaimByUserId(userId);
	}
}