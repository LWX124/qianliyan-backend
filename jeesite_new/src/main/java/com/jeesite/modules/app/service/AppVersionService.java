/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppVersion;
import com.jeesite.modules.app.dao.AppVersionDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * app_versionService
 * @author dh
 * @version 2019-11-18
 */
@Service
@Transactional(readOnly=true)
public class AppVersionService extends CrudService<AppVersionDao, AppVersion> {
	
	/**
	 * 获取单条数据
	 * @param appVersion
	 * @return
	 */
	@Override
	public AppVersion get(AppVersion appVersion) {
		return super.get(appVersion);
	}
	
	/**
	 * 查询分页数据
	 * @param appVersion 查询条件
	 * @param appVersion.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppVersion> findPage(AppVersion appVersion) {
		return super.findPage(appVersion);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appVersion
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppVersion appVersion) {
		super.save(appVersion);
		// 保存上传附件
		FileUploadUtils.saveFileUpload(appVersion.getId(), "appVersion_file");
	}
	
	/**
	 * 更新状态
	 * @param appVersion
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppVersion appVersion) {
		super.updateStatus(appVersion);
	}
	
	/**
	 * 删除数据
	 * @param appVersion
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppVersion appVersion) {
		super.delete(appVersion);
	}
	
}