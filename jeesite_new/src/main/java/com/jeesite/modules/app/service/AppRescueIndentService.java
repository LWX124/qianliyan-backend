/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppRescueIndentDao;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.entity.AppRescueIndent;
import com.jeesite.modules.app.entity.AppWxpayOrder;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 救援表Service
 *
 * @author dh
 * @version 2020-03-03
 */
@Service
@Transactional(readOnly = true)
public class AppRescueIndentService extends CrudService<AppRescueIndentDao, AppRescueIndent> {

    /**
     * 获取单条数据
     *
     * @param appRescueIndent
     * @return
     */
    @Override
    public AppRescueIndent get(AppRescueIndent appRescueIndent) {
        return super.get(appRescueIndent);
    }

    /**
     * 查询分页数据
     *
     * @param appRescueIndent      查询条件
     * @param appRescueIndent.page 分页对象
     * @return
     */
    @Override
    public Page<AppRescueIndent> findPage(AppRescueIndent appRescueIndent) {
        return super.findPage(appRescueIndent);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appRescueIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppRescueIndent appRescueIndent) {
        super.save(appRescueIndent);
    }

    /**
     * 更新状态
     *
     * @param appRescueIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppRescueIndent appRescueIndent) {
        super.updateStatus(appRescueIndent);
    }

    /**
     * 删除数据
     *
     * @param appRescueIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppRescueIndent appRescueIndent) {
        super.delete(appRescueIndent);
    }

    @Resource
    private RedisLock redisLock;

    @Resource
    private AppWxpayOrderService appWxpayOrderService;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppRescueIndentDao appRescueIndentDao;

    /**
     * 发起退款
     *
     * @param ids
     */
    @Transactional(readOnly = false)
    public void backMoney(String ids) throws CusException {
        AppRescueIndent appRescueIndent = this.get(ids);
        if (StringUtils.isEmpty(appRescueIndent.getMerchantsPayNumber())) {
            logger.error("### 救援订单发起退款  支付订单编号不能为空 appRescueIndent={}", appRescueIndent);
            throw new CusException(500, "支付订单编号不能为空");
        }
        AppWxpayOrder appWxpayOrder = appWxpayOrderDao.findByPayNumber(appRescueIndent.getMerchantsPayNumber());

        if (appWxpayOrder == null) {
            logger.error("### 救援订单发起退款  支付订单为空 appRescueIndent={}", appRescueIndent);
            throw new CusException(500, "支付订单为空");
        }

        String redisKey = RedisKeyUtils.CANCEL_RESCUE_INDENT + appRescueIndent.getMerchantsPayNumber();
        try {
            redisLock.lock(redisKey);

            if (appRescueIndent.getPayState() != 1) {
                logger.error("### 救援订单发起退款 初始退款状态下的订单才可以退款 appRescueIndent={}  ", appRescueIndent);
                throw new CusException(500, "初始退款状态下的订单才可以退款");
            }

            try {
                appWxpayOrderService.doBack(appWxpayOrder, 3, ids);
            } catch (WxPayException e) {
                e.printStackTrace();
            }
            appRescueIndent.setPayState(3);//3=退款
            appRescueIndent.setBackState(2);//2=申请退款

            this.update(appRescueIndent);//修改救援订单状态

        } finally {
            redisLock.unlock(redisKey);
        }

    }

    public AppRescueIndent findRescueByOrder(String rescueNumber) {
        return appRescueIndentDao.findRescueByOrder(rescueNumber);
    }

    public List<AppRescueIndent> findOverThreeIndent() {
        return appRescueIndentDao.findOverThreeIndent();
    }
}
