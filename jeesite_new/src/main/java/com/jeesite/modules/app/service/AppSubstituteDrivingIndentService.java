/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.util.List;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.entity.AppWxpayOrder;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppSubstituteDrivingIndent;
import com.jeesite.modules.app.dao.AppSubstituteDrivingIndentDao;

import javax.annotation.Resource;

/**
 * 代驾订单表Service
 * @author zcq
 * @version 2020-06-23
 */
@Service
@Transactional(readOnly=true)
public class AppSubstituteDrivingIndentService extends CrudService<AppSubstituteDrivingIndentDao, AppSubstituteDrivingIndent> {

	@Resource
	private AppSubstituteDrivingIndentDao appSubstituteDrivingIndentDao;

	@Resource
	private AppWxpayOrderDao appWxpayOrderDao;

	@Resource
	private RedisLock redisLock;

	@Resource
	private AppWxpayOrderService appWxpayOrderService;
	
	/**
	 * 获取单条数据
	 * @param appSubstituteDrivingIndent
	 * @return
	 */
	@Override
	public AppSubstituteDrivingIndent get(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		return super.get(appSubstituteDrivingIndent);
	}
	
	/**
	 * 查询分页数据
	 * @param appSubstituteDrivingIndent 查询条件
	 * @param appSubstituteDrivingIndent.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppSubstituteDrivingIndent> findPage(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		return super.findPage(appSubstituteDrivingIndent);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appSubstituteDrivingIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		super.save(appSubstituteDrivingIndent);
	}
	
	/**
	 * 更新状态
	 * @param appSubstituteDrivingIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		super.updateStatus(appSubstituteDrivingIndent);
	}
	
	/**
	 * 删除数据
	 * @param appSubstituteDrivingIndent
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		super.delete(appSubstituteDrivingIndent);
	}

	public AppSubstituteDrivingIndent findBySubDri(String subDriNumber) {
		return appSubstituteDrivingIndentDao.findBySubDri(subDriNumber);
	}

	public List<AppSubstituteDrivingIndent> findNoCondirmIndent() {
		return appSubstituteDrivingIndentDao.findNoCondirmIndent();
	}

	@Transactional(readOnly = false)
	public void backMoney(String id)throws CusException {
		AppSubstituteDrivingIndent appSubstituteDrivingIndent = this.get(id);
		if (StringUtils.isEmpty(appSubstituteDrivingIndent.getPayNumber())) {
			logger.error("### 代驾订单发起退款  支付订单编号不能为空 appSubstituteDrivingIndent={}", appSubstituteDrivingIndent);
			throw new CusException(500, "支付订单编号不能为空");
		}
		AppWxpayOrder appWxpayOrder = appWxpayOrderDao.findByPayNumber(appSubstituteDrivingIndent.getPayNumber());

		if (appWxpayOrder == null) {
			logger.error("### 代驾订单发起退款  支付订单为空 appSubstituteDrivingIndent={}", appSubstituteDrivingIndent);
			throw new CusException(500, "支付订单为空");
		}
		String redisKey = RedisKeyUtils.CANCEL_SUBDRIVE_INDENT + appSubstituteDrivingIndent.getPayNumber();
		try {
			redisLock.lock(redisKey);

			if (appSubstituteDrivingIndent.getPayState() != 1) {
				logger.error("### 年检订单发起退款 初始退款状态下的订单才可以退款 appSubstituteDrivingIndent={}  ", appSubstituteDrivingIndent);
				throw new CusException(500, "初始退款状态下的订单才可以退款");
			}

			try {
				appWxpayOrderService.doBack(appWxpayOrder, 5, id);
			} catch (WxPayException e) {
				e.printStackTrace();
			}

			appSubstituteDrivingIndent.setPayState(3);
			appSubstituteDrivingIndent.setIndentState(9);

			this.update(appSubstituteDrivingIndent);
		} finally {
			redisLock.unlock(redisKey);
		}


	}
}