/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.jeesite.modules.app.entity.AppAuctionBailRefundLog;
import com.jeesite.modules.app.entity.AppAuctionTransactionLog;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.constant2.AppAuctionRefundConstans;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.WxPayV2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppAuctionOnePriceCarLog;
import com.jeesite.modules.app.dao.AppAuctionOnePriceCarLogDao;

import javax.annotation.Resource;

import static com.jeesite.modules.util2.COrderNoUtil.getOnePriceRefundNo;

/**
 * 一口价车辆支付记录Service
 *
 * @author dh
 * @version 2023-04-16
 */
@Service
@Transactional(readOnly = true)
public class AppAuctionOnePriceCarLogService extends CrudService<AppAuctionOnePriceCarLogDao, AppAuctionOnePriceCarLog> {

    @Autowired
    private AppAuctionOnePriceCarLogDao appAuctionOnePriceCarLogDao;
    @Resource
    private AppAuctionTransactionLogService transactionLogService;
    @Resource
    private RedisLock redisLock;

    @Resource
    private WxPayV2Util wxPayV2Util;

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.mchId}")
    private String mchId;
    @Value("${wx.mchKey}")
    private String mchKey;
    @Value("${wx.certPath}")
    private String certPath;


    /**
     * 获取单条数据
     *
     * @param appAuctionOnePriceCarLog
     * @return
     */
    @Override
    public AppAuctionOnePriceCarLog get(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        return super.get(appAuctionOnePriceCarLog);
    }

    /**
     * 查询分页数据
     *
     * @param appAuctionOnePriceCarLog      查询条件
     * @param appAuctionOnePriceCarLog.page 分页对象
     * @return
     */
    @Override
    public Page<AppAuctionOnePriceCarLog> findPage(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        return super.findPage(appAuctionOnePriceCarLog);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appAuctionOnePriceCarLog
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        super.save(appAuctionOnePriceCarLog);
    }

    /**
     * 更新状态
     *
     * @param appAuctionOnePriceCarLog
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        super.updateStatus(appAuctionOnePriceCarLog);
    }

    /**
     * 删除数据
     *
     * @param appAuctionOnePriceCarLog
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        super.delete(appAuctionOnePriceCarLog);
    }

    @Transactional(readOnly = false)
    public void dealOnerPriceRefund() {
        //分布式锁锁住整个方法，同一时间只能有一个任务在执行
        String redisKey = RedisKeyUtils.ONE_PRICE_REFUND;
        redisLock.lock(redisKey);

        //订单状态为12已过户完成的，并且不是vip订单用户支付了一口价订金的，需要退还这笔钱
        List<AppAuctionOnePriceCarLog> list = appAuctionOnePriceCarLogDao.queryOnePriceRefund();
        logger.info("### 一口价订单待退款 list.size={}", list.size());

        byte[] bytes = ResourceUtil.readBytes(certPath);

        try {
            for (AppAuctionOnePriceCarLog appAuctionOnePriceCarLog : list) {
                Double amount = appAuctionOnePriceCarLog.getAmount() / 100;
                String onePriceUniqueOrder = getOnePriceRefundNo();

                WxPayRefundResult refund = wxPayV2Util.refund(appId, mchId, mchKey, bytes, appAuctionOnePriceCarLog.getOutTradeNo(), onePriceUniqueOrder, new BigDecimal(amount), new BigDecimal(amount));
                logger.info("### 一口价订单退款调用结果 ### refund={}", refund.toString());
                appAuctionOnePriceCarLog.setRefundId(onePriceUniqueOrder);
                appAuctionOnePriceCarLog.setRefundStatus(2);
                appAuctionOnePriceCarLog.setRefundResultCode(refund.getResultCode());
                appAuctionOnePriceCarLog.setUpdateTime(new Date());
                appAuctionOnePriceCarLog.setApplyRefundTime(new Date());
                appAuctionOnePriceCarLog.setRefundFailReason(refund.getErrCodeDes());
                appAuctionOnePriceCarLog.setErrCode(refund.getErrCode());
                appAuctionOnePriceCarLog.setErrCodeDes(refund.getErrCodeDes());

                logger.info("### 一口价订单申请退款成功 ### refund={}", appAuctionOnePriceCarLog.toString());

                appAuctionOnePriceCarLogDao.update(appAuctionOnePriceCarLog);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            redisLock.unlock(redisKey);
        }

    }

    @Transactional(readOnly = false)
    public void queryOnerPriceRefund() {
        AppAuctionOnePriceCarLog appAuctionOnePriceCarLogParamer = new AppAuctionOnePriceCarLog();
        appAuctionOnePriceCarLogParamer.setRefundStatus(2);// 1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败
        List<AppAuctionOnePriceCarLog> list = appAuctionOnePriceCarLogDao.findList(appAuctionOnePriceCarLogParamer);
        logger.info("### 查询已退款的一口价订单的退款结果 list.size={}", list.size());
        list.stream().forEach(one -> {
            //查询结果
            WxPayRefundQueryResult refund = null;
            try {
                refund = wxPayV2Util.refundQuery(appId, mchId, mchKey, one.getRefundId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (refund.getReturnCode().equals("SUCCESS")) {
                //查询成功 不代表退款结果成功
                List<WxPayRefundQueryResult.RefundRecord> refundRecords = refund.getRefundRecords();
                //不支持分批次退款，每次退款都是全额退款
                WxPayRefundQueryResult.RefundRecord refundRecord = refundRecords.get(0);
                String refundStatus = refundRecord.getRefundStatus();
                one.setRefundResultCode(refundStatus);
                if (refundStatus.equals("SUCCESS")) { // 代表业务退款是否成功
                    one.setRefundStatus(3); // 1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败
                    one.setRefundSuccessTime(new Date());
                    AppAuctionTransactionLog appAuctionTransactionLog = new AppAuctionTransactionLog();
                    appAuctionTransactionLog.setOrderId(one.getOutTradeNo());
                    AppAuctionTransactionLog appAuctionTransactionLog1 = transactionLogService.get(appAuctionTransactionLog);
                    if (Objects.isNull(appAuctionTransactionLog1)) {
                        AppAuctionTransactionLog appAuctionTransactionLog2 = transactionLog2DTO(one);
                        logger.info("## 查询一口价订金退还 成功    插入用户记录 appAuctionTransactionLog2={}", appAuctionTransactionLog2);
                        transactionLogService.insert(appAuctionTransactionLog2);
                    }
                } else {
                    one.setRefundStatus(4); // 1:初始状态 2：已申请退款（退款中）  3：退款成功 4：失败
                    one.setRefundFailReason(refundRecord.getRefundStatus());
                }
                appAuctionOnePriceCarLogDao.update(one);

            }

        });
    }


    private AppAuctionTransactionLog transactionLog2DTO(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
        AppAuctionTransactionLog appAuctionTransactionLog = new AppAuctionTransactionLog();
        appAuctionTransactionLog.setType(1);
        appAuctionTransactionLog.setUserId(appAuctionOnePriceCarLog.getUserId());
        appAuctionTransactionLog.setAmount(appAuctionOnePriceCarLog.getAmount());
        appAuctionTransactionLog.setOrderId(appAuctionOnePriceCarLog.getOutTradeNo());
        appAuctionTransactionLog.setPayType(1);
        appAuctionTransactionLog.setOrderState(6);
        appAuctionTransactionLog.setCreateTime(new Date());
        appAuctionTransactionLog.setDesc("一口价订金退还");
        appAuctionTransactionLog.setCarId(appAuctionOnePriceCarLog.getCarId());
        return appAuctionTransactionLog;
    }
}