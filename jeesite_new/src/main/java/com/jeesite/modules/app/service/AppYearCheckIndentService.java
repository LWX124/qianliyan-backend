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
import com.jeesite.modules.app.entity.AppYearCheckIndent;
import com.jeesite.modules.app.dao.AppYearCheckIndentDao;

import javax.annotation.Resource;

/**
 * 年检订单表Service
 *
 * @author zcq
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class AppYearCheckIndentService extends CrudService<AppYearCheckIndentDao, AppYearCheckIndent> {

    @Resource
    private AppYearCheckIndentDao appYearCheckIndentDao;

    @Resource
    private RedisLock redisLock;

    @Resource
    private AppWxpayOrderService appWxpayOrderService;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    /**
     * 获取单条数据
     *
     * @param appYearCheckIndent
     * @return
     */
    @Override
    public AppYearCheckIndent get(AppYearCheckIndent appYearCheckIndent) {
        return super.get(appYearCheckIndent);
    }

    /**
     * 查询分页数据
     *
     * @param appYearCheckIndent      查询条件
     * @param appYearCheckIndent.page 分页对象
     * @return
     */
    @Override
    public Page<AppYearCheckIndent> findPage(AppYearCheckIndent appYearCheckIndent) {
        return super.findPage(appYearCheckIndent);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appYearCheckIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppYearCheckIndent appYearCheckIndent) {
        super.save(appYearCheckIndent);
    }

    /**
     * 更新状态
     *
     * @param appYearCheckIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppYearCheckIndent appYearCheckIndent) {
        super.updateStatus(appYearCheckIndent);
    }

    /**
     * 删除数据
     *
     * @param appYearCheckIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppYearCheckIndent appYearCheckIndent) {
        super.delete(appYearCheckIndent);
    }

    public List<String> findImgs(String sprayId) {
        return appYearCheckIndentDao.findImgs(sprayId);
    }

    public AppYearCheckIndent findByCheckNumber(String yearCheckNumber) {
        return appYearCheckIndentDao.findByCheckNumber(yearCheckNumber);
    }

    @Transactional(readOnly = false)
    public void backMoney(String id) throws CusException {
        AppYearCheckIndent appYearCheckIndent = this.get(id);
        if (StringUtils.isEmpty(appYearCheckIndent.getPayNumber())) {
            logger.error("### 年检订单发起退款  支付订单编号不能为空 appYearCheckIndent={}", appYearCheckIndent);
            throw new CusException(500, "支付订单编号不能为空");
        }
        AppWxpayOrder appWxpayOrder = appWxpayOrderDao.findByPayNumber(appYearCheckIndent.getPayNumber());

        if (appWxpayOrder == null) {
            logger.error("### 年检订单发起退款  支付订单为空 appYearCheckIndent={}", appYearCheckIndent);
            throw new CusException(500, "支付订单为空");
        }

        String redisKey = RedisKeyUtils.CANCEL_YEARCHECK_INDENT + appYearCheckIndent.getPayNumber();

        try {
            redisLock.lock(redisKey);

            if (appYearCheckIndent.getPayState() != 1) {
                logger.error("### 年检订单发起退款 初始退款状态下的订单才可以退款 appRescueIndent={}  ", appYearCheckIndent);
                throw new CusException(500, "初始退款状态下的订单才可以退款");
            }

            try {
                appWxpayOrderService.doBack(appWxpayOrder, 4, id);
            } catch (WxPayException e) {
                e.printStackTrace();
            }

            appYearCheckIndent.setPayState(3);
            appYearCheckIndent.setBackState(2);

            this.update(appYearCheckIndent);
        } finally {
            redisLock.unlock(redisKey);
        }
    }
}