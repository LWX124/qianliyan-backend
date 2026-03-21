/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppPhotoMer;
import com.jeesite.modules.app.dao.AppPhotoMerDao;

import javax.annotation.Resource;

/**
 * 图片上传商户图片表Service
 * @author zcq
 * @version 2021-03-08
 */
@Service
@Transactional(readOnly=true)
public class AppPhotoMerService extends CrudService<AppPhotoMerDao, AppPhotoMer> {

	@Resource
	private AppPhotoMerDao appPhotoMerDao;
	/**
	 * 获取单条数据
	 * @param appPhotoMer
	 * @return
	 */
	@Override
	public AppPhotoMer get(AppPhotoMer appPhotoMer) {
		return super.get(appPhotoMer);
	}
	
	/**
	 * 查询分页数据
	 * @param appPhotoMer 查询条件
	 * @param appPhotoMer.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppPhotoMer> findPage(AppPhotoMer appPhotoMer) {
		return super.findPage(appPhotoMer);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appPhotoMer
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppPhotoMer appPhotoMer) {
		super.save(appPhotoMer);
	}
	
	/**
	 * 更新状态
	 * @param appPhotoMer
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppPhotoMer appPhotoMer) {
		super.updateStatus(appPhotoMer);
	}
	
	/**
	 * 删除数据
	 * @param appPhotoMer
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppPhotoMer appPhotoMer) {
		super.delete(appPhotoMer);
	}

	@Transactional(readOnly=false)
	public ArrayList<Integer> findAllImgs(String id) {
		return appPhotoMerDao.findAllImgs(id);
	}


	@Transactional(readOnly=false)
	public void insertNew(AppPhotoMer appPhotoMer) {
		appPhotoMerDao.insertNew(appPhotoMer);
	}
}