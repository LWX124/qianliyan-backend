/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppMerchantsComments;
import com.jeesite.modules.app.dao.AppMerchantsCommentsDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商户评论表Service
 * @author zcq
 * @version 2019-08-07
 */
@Service
@Transactional(readOnly=true)
public class AppMerchantsCommentsService extends CrudService<AppMerchantsCommentsDao, AppMerchantsComments> {



	@Autowired
	private AppMerchantsCommentsDao commentsDao;

	public List<AppMerchantsComments> findByMerchanstId(String id){
		return commentsDao.findAll(id);
	}

	public AppMerchantsComments findContent(String content){
		return commentsDao.findByCount(content);
	}
	/**
	 * 获取单条数据
	 * @param appMerchantsComments
	 * @return
	 */
	@Override
	public AppMerchantsComments get(AppMerchantsComments appMerchantsComments) {
		return super.get(appMerchantsComments);
	}
	
	/**
	 * 查询分页数据
	 * @param appMerchantsComments 查询条件
	 * @return
	 */
	@Override
	public Page<AppMerchantsComments> findPage(AppMerchantsComments appMerchantsComments) {

		Page<AppMerchantsComments> page = super.findPage(appMerchantsComments);
		return page;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMerchantsComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMerchantsComments appMerchantsComments) {
		//根据数据查询到信息再修改
		String content = appMerchantsComments.getContent();
		AppMerchantsComments byCount = commentsDao.findByCount(content);
		//拿到原本修改后的数据
		Integer efficiencyScore = appMerchantsComments.getEfficiencyScore();
		Integer serviceScore = appMerchantsComments.getServiceScore();
		String state = appMerchantsComments.getState();
		byCount.setEfficiencyScore(efficiencyScore);
		byCount.setServiceScore(serviceScore);
		byCount.setState(state);
		super.save(byCount);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(appMerchantsComments.getId(), "appMerchantsComments_image");
	}
	
	/**
	 * 更新状态
	 * @param appMerchantsComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMerchantsComments appMerchantsComments) {
		super.updateStatus(appMerchantsComments);
	}
	
	/**
	 * 删除数据
	 * @param appMerchantsComments
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMerchantsComments appMerchantsComments) {
		super.delete(appMerchantsComments);
	}
	
}