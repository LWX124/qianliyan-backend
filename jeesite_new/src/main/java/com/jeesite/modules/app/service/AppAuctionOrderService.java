/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.constant2.AppAuctionConstant;
import com.jeesite.modules.constant2.AuctionConstant;
import com.jeesite.modules.util2.COrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppAuctionOrderDao;

/**
 * 拍卖订单表Service
 *
 * @author y
 * @version 2023-01-06
 */
@Service
@Transactional(readOnly = true)
public class AppAuctionOrderService extends CrudService<AppAuctionOrderDao, AppAuctionOrder> {

	@Autowired
	private AppAuctionOrderService appAuctionOrderService;

	@Autowired
	private AppAuctionService appAuctionService;

	@Autowired
	private AppAuctionUpService appAuctionUpService;

	@Autowired
	private AppAuctionWarnCarService appAuctionWarnCarService;

	@Autowired
	private AppAuctionHotService appAuctionHotService;

	@Autowired
	private AppAuctionCollectService collectService;

	@Autowired
	private AppAuctionImgService appAuctionImgService;

	/**
	 * 获取单条数据
	 *
	 * @param appAuctionOrder
	 * @return
	 */
	@Override
	public AppAuctionOrder get(AppAuctionOrder appAuctionOrder) {
		return super.get(appAuctionOrder);
	}

	/**
	 * 查询分页数据
	 *
	 * @param appAuctionOrder 查询条件
	 * @return
	 */
	@Override
	public Page<AppAuctionOrder> findPage(AppAuctionOrder appAuctionOrder) {
		return super.findPage(appAuctionOrder);
	}

	/**
	 * 保存数据（插入或更新）
	 *
	 * @param appAuctionOrder
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(AppAuctionOrder appAuctionOrder) {
//		if (appAuctionOrder.getState().equals(AppAuctionConstant.ONE)){
//			Long userId = appAuctionOrder.getUserId();
//			AppAuctionVipControl vipControl1 = vipControlService.getByUserId(String.valueOf(userId));
//			if(Objects.nonNull(vipControl1) && vipControl1.getFreezeCount() > 0){
//				vipControl1.setCarCount(vipControl1.getCarCount() + 1);
//				vipControl1.setFreezeCount(vipControl1.getFreezeCount() - 1);
//				vipControlService.update(vipControl1);
//			}
//			Long auctionCarId = appAuctionOrder.getAuctionCarId();
//			AppAuction appAuction = appAuctionService.get(String.valueOf(auctionCarId));
//			appAuction.setTransferState(AppAuctionConstant.THREE);
//			appAuctionService.update(appAuction);
//
//			AppAuctionUp appAuctionUp = appAuctionUpService.findAuctionUp(auctionCarId);
//			appAuctionUp.setTransferState(AppAuctionConstant.THREE);
//			appAuctionUpService.update(appAuctionUp);
//
//		}

		super.save(appAuctionOrder);
	}

	/**
	 * 更新状态
	 *
	 * @param appAuctionOrder
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(AppAuctionOrder appAuctionOrder) {
		super.updateStatus(appAuctionOrder);
	}

	/**
	 * 删除数据
	 *
	 * @param appAuctionOrder
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(AppAuctionOrder appAuctionOrder) {
		super.delete(appAuctionOrder);
	}

	/**
	 * @param carId
	 * @param userId 交易失败返还手续费@todo
	 * @return
	 */
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public JSONObject readyOrder(AppAuctionBidLog bidLog, String carId, Long userId) {
		JSONObject result = new JSONObject();
		// 验证重复录入
		AppAuctionOrder appAuctionOrder = new AppAuctionOrder();
		appAuctionOrder.setCarId(Long.valueOf(carId));
		appAuctionOrder.setUserId(userId);
		AppAuctionOrder appAuctionOrder1 = appAuctionOrderService.get(appAuctionOrder);
		if (Objects.nonNull(appAuctionOrder1)) {
			result.put("code", 512);
			result.put("msg", "请勿重复录入");
			return result;
		}
		//修改订单状态,生成订单
		AppAuction auction = appAuctionService.findAuctionByCarId(Long.valueOf(carId));
		if (Objects.isNull(auction)) {
			result.put("code", 513);
			result.put("msg", "车辆表状态错误");
			return result;
		}
		//修改auction状态
		auction.setCarState(AppAuctionConstant.EIGHT);
		auction.setUpdateTime(new Date());
		appAuctionService.update(auction);
		logger.info("##修改auction状态");

		//修改auctionUp状态
		AppAuctionUp appAuctionUpEntity = appAuctionUpService.findAuctionUp(Long.valueOf(carId));
//		appAuctionUpEntity.setCarState(AppAuctionConstant.EIGHT);
//		appAuctionUpService.update(appAuctionUpEntity);
//		logger.info("##修改auctionUp状态");

		//修改热门推荐状态
		AppAuctionHot hotByCarId = appAuctionHotService.findHotByCarId(Long.valueOf(carId));
		if(Objects.nonNull(hotByCarId) && AppAuctionConstant.CAR_ALL_IS_ENABLED == hotByCarId.getIsEnabled()){
			hotByCarId.setIsEnabled(AppAuctionConstant.CAR_ALL_DEACTIVATE);
			appAuctionHotService.update(hotByCarId);
		}
		logger.info("##修改AppAuctionHot状态");

		//修改提醒车状态
        AppAuctionWarnCar appAuctionWarnCar = new AppAuctionWarnCar();
		if(Objects.nonNull(appAuctionWarnCar) && AppAuctionConstant.CAR_ALL_IS_ENABLED == appAuctionWarnCar.getIsEnabled()){
			appAuctionWarnCar.setIsEnabled(AppAuctionConstant.CAR_ALL_DEACTIVATE);
			appAuctionWarnCarService.update(appAuctionWarnCar);
		}
		logger.info("##修改AppAuctionWarnCar状态");

		//修改收藏状态
        List<AppAuctionCollect> collectByCarId = collectService.findCollectByCarId(Long.valueOf(carId));
        if (collectByCarId.size() > 0) {
            for (AppAuctionCollect appAuctionCollect : collectByCarId) {
                if (appAuctionCollect.getIsEnabled() == AppAuctionConstant.CAR_ALL_IS_ENABLED) {
					appAuctionCollect.setIsEnabled(AppAuctionConstant.CAR_ALL_DEACTIVATE);
                    collectService.update(appAuctionCollect);
                }
            }
        }
        logger.info("##修改AppAuctionCollect状态");

		//创建订单信息
		AppAuctionOrder appAuctionOrderEntity = new AppAuctionOrder();
		appAuctionOrderEntity.setOrderAmount(Double.valueOf(bidLog.getBid()));
		appAuctionOrderEntity.setUserId(bidLog.getUserId());
		appAuctionOrderEntity.setCarId(Long.valueOf(carId));
		appAuctionOrderEntity.setCreateTime(new Date());
		appAuctionOrderEntity.setState(AppAuctionConstant.ZERO);


		//生成订单号
		String orderNo = COrderNoUtil.getUniqueOrder();
		appAuctionOrderEntity.setOrderNo(orderNo);


		//获取服务费率
//		Integer rate = appAuctionUpEntity.getServiceFee();
//		if (rate != null && rate != 0) {
//			BigDecimal bigDecimal = new BigDecimal(rate);
//			BigDecimal serviceFee = bigDecimal.divide(new BigDecimal("100")).multiply(new BigDecimal(bidLog.getBid()));
//			appAuctionOrderEntity.setServiceFee(Double.valueOf(String.valueOf(serviceFee)));
//		} else {
//			BigDecimal bigDecimal = new BigDecimal(bidLog.getBid());
//			BigDecimal serviceFee = bigDecimal.multiply(new BigDecimal("0.03"));
//			appAuctionOrderEntity.setServiceFee(Double.valueOf(String.valueOf(serviceFee)));
//		}
		appAuctionOrderEntity.setServiceFee(auction.getCarServiceAmt().doubleValue());
		logger.info("##创建订单信息:"+appAuctionOrderEntity);

		//录入信息
		appAuctionOrderService.insert(appAuctionOrderEntity);
		logger.info("录入订单成功!");


		result.put("code", 200);
		result.put("msg", "成功");
		result.put("data", appAuctionOrderEntity);
		return result;

	}

}