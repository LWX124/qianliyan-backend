/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.Date;
import java.util.List;

import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppCleanPriceDetail;
import com.jeesite.modules.app.dao.AppCleanPriceDetailDao;

import javax.annotation.Resource;

/**
 * 商户清洗价格明细表Service
 * @author zcq
 * @version 2019-12-11
 */
@Service
@Transactional(readOnly=true)
public class AppCleanPriceDetailService extends CrudService<AppCleanPriceDetailDao, AppCleanPriceDetail> {

	@Resource
	private AppCleanPriceDetailDao appCleanPriceDetailDao;


	@Resource
	private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;

	/**
	 * 获取单条数据
	 * @param appCleanPriceDetail
	 * @return
	 */
	@Override
	public AppCleanPriceDetail get(AppCleanPriceDetail appCleanPriceDetail) {
		return super.get(appCleanPriceDetail);
	}

	/**
	 * 查询分页数据
	 * @param appCleanPriceDetail 查询条件
	 * @param appCleanPriceDetail.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppCleanPriceDetail> findPage(AppCleanPriceDetail appCleanPriceDetail) {
		return super.findPage(appCleanPriceDetail);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param appCleanPriceDetail
	 */
	@Transactional(readOnly=false)
	public void saveprice(AppCleanPriceDetail appCleanPriceDetail) {
		//判断是否已经添加过服务项目
		Integer carType = appCleanPriceDetail.getCarType();
		Integer cleanType = appCleanPriceDetail.getCleanType();
		Integer userBId = appCleanPriceDetail.getUserBId();
		appLableDetailsReviewTreeService.addFirst(userBId);
		AppLableDetailsReviewTree appLableDetails = appLableDetailsReviewTreeService.selectLable(userBId,4,1);
		if (appLableDetails==null){
			appLableDetailsReviewTreeService.addCleanSecond(userBId,4,null);
		}
		AppCleanPriceDetail priceDetail =  appCleanPriceDetailDao.findProject(userBId,carType,cleanType);
		if (priceDetail==null){
			//菜单为空
			save(appCleanPriceDetail);
		}else {
			priceDetail.setUserBId(appCleanPriceDetail.getUserBId());
			priceDetail.setCleanType(appCleanPriceDetail.getCleanType());
			priceDetail.setCarType(appCleanPriceDetail.getCarType());
			priceDetail.setOriginalPrice(appCleanPriceDetail.getOriginalPrice());
			priceDetail.setThriePrice(appCleanPriceDetail.getThriePrice());
			priceDetail.setContractProject(appCleanPriceDetail.getContractProject());
			priceDetail.setResidueDegree(appCleanPriceDetail.getResidueDegree());
			priceDetail.setCreateTime(new Date());
			priceDetail.setUpdateTime(new Date());
			update(priceDetail);
		}

	}


	/**
	 * 更新状态
	 * @param appCleanPriceDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppCleanPriceDetail appCleanPriceDetail) {
		super.updateStatus(appCleanPriceDetail);
	}

	/**
	 * 删除数据
	 * @param appCleanPriceDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppCleanPriceDetail appCleanPriceDetail) {
		super.delete(appCleanPriceDetail);
	}

	@Transactional(readOnly=false)
	public List<AppCleanPriceDetail> finduserB(String userBId) {
		return appCleanPriceDetailDao.finduserB(userBId);
	}



}
