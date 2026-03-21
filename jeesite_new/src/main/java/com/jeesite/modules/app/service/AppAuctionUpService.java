/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppAuctionOrderDao;
import com.jeesite.modules.app.dao.AppAuctionUpDao;
import com.jeesite.modules.app.entity.AppAuction;
import com.jeesite.modules.app.entity.AppAuctionImg;
import com.jeesite.modules.app.entity.AppAuctionOrder;
import com.jeesite.modules.app.entity.AppAuctionUp;
import com.jeesite.modules.constant2.AppAuctionConstant;
import com.jeesite.modules.constant2.AppAuctionRedisConstans;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 拍卖车上架信息表Service
 *
 * @author y
 * @version 2022-10-27
 */
@Service
@Transactional(readOnly = true)
public class AppAuctionUpService extends CrudService<AppAuctionUpDao, AppAuctionUp> {
    @Resource
    private AppAuctionImgService appAuctionImgService;
    @Resource
    private AppAuctionHotService appAuctionHotService;
    @Resource
    private AppAuctionService appAuctionService;
    @Resource
    private AppAuctionUpDao appAuctionUpDao;
    @Resource
    private AppAuctionOrderDao appAuctionOrderDao;
    @Resource
    private RedisTemplate redisTemplate;

    private final String suffix = "?x-image-process=style/style-c2b2";


    /**
     * 获取单条数据
     *
     * @param appAuctionUp
     * @return
     */
    @Override
    public AppAuctionUp get(AppAuctionUp appAuctionUp) {
        return super.get(appAuctionUp);
    }

    /**
     * 查询分页数据
     *
     * @param appAuctionUp 查询条件
     * @return
     */
    @Override
    public Page<AppAuctionUp> findPage(AppAuctionUp appAuctionUp) {
        return super.findPage(appAuctionUp);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param paramerAppAuctionUp
     */
    @Override
    @Transactional(readOnly = false)
    @CacheEvict(cacheNames = AppAuctionRedisConstans.KEY_RECOMMEND_HOT, key = "'All'")
    public void save(AppAuctionUp paramerAppAuctionUp) throws RuntimeException {
        AppAuction appAuction = appAuctionService.get(paramerAppAuctionUp.getCarId().toString());

        AppAuction paramerAppAuction = paramerAppAuctionUp.getAppAuction();

        if (paramerAppAuctionUp.getId() != null) {

            if (paramerAppAuction.getCarState() == AppAuctionConstant.THREE && paramerAppAuction.getUpState() == AppAuctionConstant.ZERO) {
                //审核通过并且未上架就上架
                appAuction.setUpState(AppAuctionConstant.UP_STATE_UP);
            }

            if (appAuction.getCarState() == AppAuctionConstant.TEN) {   //过户审核
                if (paramerAppAuction.getCarState() < 10) {
                    throw new RuntimeException("过户审核阶段不能选择之前的状态!");
                }
            }

            if (paramerAppAuction.getCarState() == 12) {//过户完成状态 修改订单状态为 交易完成
                AppAuctionOrder appAuctionOrderParamer = new AppAuctionOrder();
                appAuctionOrderParamer.setCarId(paramerAppAuctionUp.getCarId());

                AppAuctionOrder appAuctionOrder = appAuctionOrderDao.getByEntity(appAuctionOrderParamer);
                appAuctionOrder.setState(1);
                appAuctionOrderDao.update(appAuctionOrder);
            }

            //获取操作人员信息并记录
            String counselorId = UserUtils.getUser().getId();
            paramerAppAuctionUp.setCounselorId(counselorId);
            paramerAppAuctionUp.setUpdateTime(new Date());


            if (paramerAppAuctionUp.getServiceFee() != null && (paramerAppAuctionUp.getServiceFee() > 50 || paramerAppAuctionUp.getServiceFee() < 1)) {
                throw new RuntimeException("服务费率必须小50并且大于等于1!");
            }
            if (Objects.isNull(paramerAppAuctionUp.getServiceFee())) {
                paramerAppAuctionUp.setServiceFee(3);
            }
            //除了审核和过户未通过,其他状态失败说明置空
            if (AppAuctionConstant.TWO != paramerAppAuction.getCarState() && AppAuctionConstant.ELEVEN != paramerAppAuction.getCarState() && StringUtils.isNotBlank(paramerAppAuctionUp.getExplain())) {
                paramerAppAuctionUp.setExplain("");
            }

            appAuction.setUpState(paramerAppAuction.getUpState());
            appAuction.setCarState(paramerAppAuction.getCarState());
            appAuction.setBrand(paramerAppAuction.getBrand());
            appAuction.setCarBondAmt(paramerAppAuction.getCarBondAmt());
            appAuction.setCarServiceAmt(paramerAppAuction.getCarServiceAmt());
            appAuction.setLuxuryCarPrice(paramerAppAuction.getLuxuryCarPrice());
            appAuction.setFirstAmount(paramerAppAuction.getFirstAmount());
            appAuctionService.update(appAuction);

            this.update(paramerAppAuctionUp);
        } else {
            this.insert(paramerAppAuctionUp);
        }
    }

    /**
     * 更新状态
     *
     * @param appAuctionUp
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppAuctionUp appAuctionUp) {
        super.updateStatus(appAuctionUp);
    }

    /**
     * 删除数据
     *
     * @param appAuctionUp
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppAuctionUp appAuctionUp) {
        super.delete(appAuctionUp);
    }

    //图片类型,1车辆图片,2保险资料,3,4,5过户资料,6驾驶证,7登记证书,8责任书
    public JSONObject showImgs(Long carId) {
        JSONObject object = new JSONObject();
        JSONArray carImg = new JSONArray();     //1车辆图片
        JSONArray insImg = new JSONArray();     //2理赔资料
        JSONArray newDriveImg = new JSONArray();     //3新的行驶证
        JSONArray carInvoiceImg = new JSONArray();     //4二手车发票
        JSONArray registrationImg = new JSONArray();     //5登记证书变更照片
        JSONArray driveImg = new JSONArray();   //6驾驶证
        JSONArray regImg = new JSONArray();   //7登记证书
        JSONArray beforeRepair = new JSONArray();   //修理前
        JSONArray afterRepair = new JSONArray();   //修理后

        ArrayList<AppAuctionImg> imgs = appAuctionImgService.findAllImgs(String.valueOf(carId));
        List<AppAuctionImg> allImgs = imgs.stream().map(img -> {
            if (StringUtils.isNotBlank(img.getUrl()) && img.getUrl().contains(suffix)) {
                img.setUrl(img.getUrl().replace(suffix, ""));
            }
            return img;
        }).collect(Collectors.toList());
        if (!allImgs.isEmpty()) {
            allImgs.stream().forEach(a -> {
                if (AppAuctionConstant.ONE.toString().equals(a.getState())) {
                    carImg.add(a.getUrl());
                } else if (AppAuctionConstant.THREE.toString().equals(a.getState())) {
                    newDriveImg.add(a.getUrl());
                } else if (AppAuctionConstant.FOUR.toString().equals(a.getState())) {
                    carInvoiceImg.add(a.getUrl());
                } else if (AppAuctionConstant.FIVE.toString().equals(a.getState())) {
                    registrationImg.add(a.getUrl());
                } else if (AppAuctionConstant.TWO.toString().equals(a.getState())) {
                    insImg.add(a.getUrl());
                } else if (AppAuctionConstant.SIX.toString().equals(a.getState())) {
                    driveImg.add(a.getUrl());
                } else if (AppAuctionConstant.SEVEN.toString().equals(a.getState())) {
                    regImg.add(a.getUrl());
                } else if (AppAuctionConstant.NINE.toString().equals(a.getState())) {
                    beforeRepair.add(a.getUrl());
                } else if (AppAuctionConstant.TEN.toString().equals(a.getState())) {
                    afterRepair.add(a.getUrl());
                }
            });
            object.put("carImg", carImg);
            object.put("newDriveImg", newDriveImg);
            object.put("carInvoiceImg", carInvoiceImg);
            object.put("registrationImg", registrationImg);
            object.put("insImg", insImg);
            object.put("driveImg", driveImg);
            object.put("regImg", regImg);
            object.put("beforeRepair", beforeRepair);
            object.put("afterRepair", afterRepair);
            return object;
        }
        return null;
    }

    public List<AppAuctionUp> findTodayUpCar() {
        return appAuctionUpDao.findTodayUpCar();
    }

    public List<AppAuctionUp> findTodayEndCar() {
        return appAuctionUpDao.findTodayEndCar();
    }

    public AppAuctionUp findAuctionUpByCarId(Long carId) {
        return appAuctionUpDao.findAuctionUpByCarId(carId);
    }

    public AppAuctionUp findAuctionUp(Long carId) {
        return appAuctionUpDao.findAuctionUp(carId);
    }


    @Transactional(readOnly = false)
    public void updateImgOne(String imgSrc, String newSrc) {
        appAuctionImgService.updateImgOne(imgSrc, newSrc);
    }
}