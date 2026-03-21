/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppSendOutSheetDao;

import javax.annotation.Resource;

/**
 * web派单记录表Service
 * @author zcq
 * @version 2020-09-24
 */
@Service
@Transactional(readOnly=true)
public class AppSendOutSheetService extends CrudService<AppSendOutSheetDao, AppSendOutSheet> {

	@Resource
	private AppSendOutSheetDao appSendOutSheetDao;

	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 获取单条数据
	 * @param appSendOutSheet
	 * @return
	 */
	@Override
	public AppSendOutSheet get(AppSendOutSheet appSendOutSheet) {
		return super.get(appSendOutSheet);
	}
	
	/**
	 * 查询分页数据
	 * @param appSendOutSheet 查询条件
	 * @param appSendOutSheet.page 分页对象
	 * @return
	 */
	@Override
	public Page<AppSendOutSheet> findPage(AppSendOutSheet appSendOutSheet) {
		return super.findPage(appSendOutSheet);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appSendOutSheet
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppSendOutSheet appSendOutSheet) {
		super.save(appSendOutSheet);
	}
	
	/**
	 * 更新状态
	 * @param appSendOutSheet
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppSendOutSheet appSendOutSheet) {
		super.updateStatus(appSendOutSheet);
	}
	
	/**
	 * 删除数据
	 * @param appSendOutSheet
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppSendOutSheet appSendOutSheet) {
		super.delete(appSendOutSheet);
	}

	public List<AppUser> findNowClaimAdjusters() {
		return appSendOutSheetDao.findNowClaimAdjusters();
	}


	@Transactional(readOnly=false)
	public Integer addSendSheet(AppSendOutSheet appSendOutSheet) {
		return appSendOutSheetDao.addSendSheet(appSendOutSheet);
	}

	public Integer findSendUserbid(Integer id) {
		return appSendOutSheetDao.findSendUserbid(id);
	}

	public String findByLngLat(String lat, String lng) {
		return appSendOutSheetDao.findByLngLat(lat,lng);
	}

	public List<String> findCheckImg(String indentId) {
		return appSendOutSheetDao.findCheckImg(indentId);
	}


	public BigDecimal getdistance(String lat, String lng, AppBUser bByPhone) {

		if (bByPhone==null){
			return BigDecimal.ZERO;
		}
		Circle circle = new Circle(Double.parseDouble(lng), Double.parseDouble(lat), 50.0 * 1000);
		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
		GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_GEO + bByPhone.getId(), circle, args);
		if (radius!=null){
			List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
			for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
				Distance distance = geoLocationGeoResult.getDistance();
				double value = distance.getValue();
				BigDecimal bigDecimal = new BigDecimal(value);
				return bigDecimal.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
			}
		}
		return BigDecimal.ZERO;
	}
}