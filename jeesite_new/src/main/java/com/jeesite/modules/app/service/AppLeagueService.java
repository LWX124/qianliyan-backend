/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.QueryService;
import com.jeesite.modules.app.entity.AppLeague;
import com.jeesite.modules.app.dao.AppLeagueDao;

/**
 * 加盟信息表Service
 * @author zcq
 * @version 2019-08-01
 */
@Service
public class AppLeagueService extends QueryService<AppLeagueDao, AppLeague> {
	
	/**
	 * 获取单条数据
	 * @param appLeague
	 * @return
	 */
	@Override
	public AppLeague get(AppLeague appLeague) {
		return super.get(appLeague);
	}
	
	/**
	 * 查询分页数据
	 * @param appLeague 查询条件
	 * @param appLeague.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppLeague> findPage(AppLeague appLeague) {
		return super.findPage(appLeague);
	}
	
}