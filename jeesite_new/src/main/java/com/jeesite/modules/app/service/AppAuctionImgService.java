/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionImg;
import com.jeesite.modules.app.dao.AppAuctionImgDao;

import javax.annotation.Resource;

/**
 * 拍卖车详情图片Service
 * @author y
 * @version 2022-10-10
 */
@Service
@Transactional(readOnly=true)
public class AppAuctionImgService extends CrudService<AppAuctionImgDao, AppAuctionImg> {
	@Resource
	private AppAuctionImgDao appAuctionImgDao;
	
	/**
	 * 获取单条数据
	 * @param appAuctionImg
	 * @return
	 */
	@Override
	public AppAuctionImg get(AppAuctionImg appAuctionImg) {
		return super.get(appAuctionImg);
	}
	
	/**
	 * 查询分页数据
	 * @param appAuctionImg 查询条件
	 * @param appAuctionImg.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppAuctionImg> findPage(AppAuctionImg appAuctionImg) {
		return super.findPage(appAuctionImg);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appAuctionImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppAuctionImg appAuctionImg) {
		super.save(appAuctionImg);
	}
	
	/**
	 * 更新状态
	 * @param appAuctionImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppAuctionImg appAuctionImg) {
		super.updateStatus(appAuctionImg);
	}
	
	/**
	 * 删除数据
	 * @param appAuctionImg
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppAuctionImg appAuctionImg) {
		super.delete(appAuctionImg);
	}

	public ArrayList<AppAuctionImg> findAllImgs(String userId) {
		return appAuctionImgDao.findAllImgs(userId);
	}


	@Transactional(readOnly=false)
	public void updateImgOne(String imgSrc, String newSrc) {
		 appAuctionImgDao.updateImgOne(imgSrc,newSrc);
	}
}