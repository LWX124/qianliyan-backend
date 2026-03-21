/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.jeesite.modules.app.entity.AppMerchants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppMerchantsInfoBanner;
import com.jeesite.modules.app.dao.AppMerchantsInfoBannerDao;

/**
 * 商户详情banner图Service
 * @author zcq
 * @version 2019-07-30
 */
@Service
@Transactional(readOnly=true)
public class AppMerchantsInfoBannerService extends CrudService<AppMerchantsInfoBannerDao, AppMerchantsInfoBanner> {

	@Autowired
	private AppMerchantsInfoBannerDao bannerDao;

	public List<AppMerchantsInfoBanner> findId(String id){
		return bannerDao.findImgByMerchantsId(id);
	}

    /**
	 * 获取单条数据
	 * @param appMerchantsInfoBanner
	 * @return
	 */
	@Override
	public AppMerchantsInfoBanner get(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		return super.get(appMerchantsInfoBanner);
	}
	
	/**
	 * 查询分页数据
	 * @param appMerchantsInfoBanner 查询条件
	 * @param appMerchantsInfoBanner.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppMerchantsInfoBanner> findPage(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		return super.findPage(appMerchantsInfoBanner);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMerchantsInfoBanner
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		super.save(appMerchantsInfoBanner);
	}
	
	/**
	 * 更新状态
	 * @param appMerchantsInfoBanner
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		super.updateStatus(appMerchantsInfoBanner);
	}
	
	/**
	 * 删除数据
	 * @param appMerchantsInfoBanner
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		super.delete(appMerchantsInfoBanner);
	}

	public AppMerchantsInfoBanner findIndex1Banner(String id) {
		return bannerDao.findIndex1Banner(id);
	}


	public List<AppMerchantsInfoBanner> findAllList(String id) {
		return bannerDao.findAllList(id);
	}

    @Transactional(readOnly=false)
	public void insertNew(AppMerchantsInfoBanner infoBanner) {
		bannerDao.insertNew(infoBanner);
	}
}