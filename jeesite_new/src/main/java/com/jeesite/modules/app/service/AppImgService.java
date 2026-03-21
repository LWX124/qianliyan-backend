/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppImg;
import com.jeesite.modules.app.dao.AppImgDao;

/**
 * 图片表Service
 * @author zcq
 * @version 2019-08-06
 */
@Service
@Transactional(readOnly=true)
public class AppImgService extends CrudService<AppImgDao, AppImg> {

	@Autowired
	private AppImgDao appImgDao;


	public List<AppImg> getImg(Integer type,String  id){
		return appImgDao.findImg(type, id);
	}
	
	/**
	 * 获取单条数据
	 * @param appImg
	 * @return
	 */
	@Override
	public AppImg get(AppImg appImg) {
		return super.get(appImg);
	}
	
	/**
	 * 查询分页数据
	 * @param appImg 查询条件
	 * @return
	 */
	@Override
	public Page<AppImg> findPage(AppImg appImg) {
		return super.findPage(appImg);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppImg appImg) {
		super.save(appImg);
	}
	
	/**
	 * 更新状态
	 * @param appImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppImg appImg) {
		super.updateStatus(appImg);
	}
	
	/**
	 * 删除数据
	 * @param appImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppImg appImg) {
		super.delete(appImg);
	}

	@Transactional(readOnly=false)
	public List<Integer> findAllImg(String id) {
		return appImgDao.findAllImg(id);
	}

	@Transactional(readOnly=false)
	public void insertNew(AppImg appImg) {
		appImgDao.insertNew(appImg);
	}
}