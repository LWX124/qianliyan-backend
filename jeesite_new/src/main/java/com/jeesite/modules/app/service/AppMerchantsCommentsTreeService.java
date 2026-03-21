/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.jeesite.modules.app.dao.AppMerchantsCommentsTreeDao;
import com.jeesite.modules.app.entity.AppMerchantsCommentsTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 商户评论树表Service
 * @author zcq
 * @version 2019-08-12
 */
@Service
@Transactional(readOnly=true)
public class AppMerchantsCommentsTreeService extends TreeService<AppMerchantsCommentsTreeDao, AppMerchantsCommentsTree> {
	
	/**
	 * 获取单条数据
	 * @param appMerchantsCommentsTree
	 * @return
	 */
	@Override
	public AppMerchantsCommentsTree get(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		return super.get(appMerchantsCommentsTree);
	}
	
	/**
	 * 查询列表数据
	 * @param appMerchantsCommentsTree
	 * @return
	 */
	@Override
	public List<AppMerchantsCommentsTree> findList(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		return super.findList(appMerchantsCommentsTree);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appMerchantsCommentsTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		super.save(appMerchantsCommentsTree);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(appMerchantsCommentsTree.getId(), "appMerchantsCommentsTree_image");
	}
	
	/**
	 * 更新状态
	 * @param appMerchantsCommentsTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		super.updateStatus(appMerchantsCommentsTree);
	}
	
	/**
	 * 删除数据
	 * @param appMerchantsCommentsTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		super.delete(appMerchantsCommentsTree);
	}
	
}