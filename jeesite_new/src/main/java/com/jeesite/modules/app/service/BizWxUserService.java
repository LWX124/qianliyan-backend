/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.BizWxUser;
import com.jeesite.modules.app.dao.BizWxUserDao;

import javax.annotation.Resource;

/**
 * 微信用户信息表Service
 * @author zcq
 * @version 2019-09-24
 */
@Service
@Transactional(readOnly=true)
public class BizWxUserService extends CrudService<BizWxUserDao, BizWxUser> {

	@Resource
	private BizWxUserDao bizWxUserDao;

	/**
	 * 获取单条数据
	 * @param bizWxUser
	 * @return
	 */
	@Override
	public BizWxUser get(BizWxUser bizWxUser) {
		return super.get(bizWxUser);
	}
	
	/**
	 * 查询分页数据
	 * @param bizWxUser 查询条件
	 * @param bizWxUser.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizWxUser> findPage(BizWxUser bizWxUser) {
		return super.findPage(bizWxUser);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizWxUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizWxUser bizWxUser) {
		super.save(bizWxUser);
	}
	
	/**
	 * 更新状态
	 * @param bizWxUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizWxUser bizWxUser) {
		super.updateStatus(bizWxUser);
	}
	
	/**
	 * 删除数据
	 * @param bizWxUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizWxUser bizWxUser) {
		super.delete(bizWxUser);
	}

	public BizWxUser findByOpenid(String openid) {
		return bizWxUserDao.findByOpenid(openid);
	}
}