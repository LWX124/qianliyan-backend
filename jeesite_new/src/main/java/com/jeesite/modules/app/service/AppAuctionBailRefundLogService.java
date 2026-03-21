/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.*;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.constant2.AppAuctionRefundConstans;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.COrderNoUtil;
import com.jeesite.modules.util2.WxPayV2Util;
import jodd.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 保证金退款记录Service
 *
 * @author y
 * @version 2023-03-19
 */
@Service
@Transactional(readOnly = true)
public class AppAuctionBailRefundLogService extends CrudService<AppAuctionBailRefundLogDao, AppAuctionBailRefundLog> {

    @Resource
    private AppAuctionBailRefundLogDao bailRefundLogDao;
    @Resource
    private AppAuctionPayLogDao appAuctionPayLogDao;
    @Resource
    private AppAuctionWithdrawcashDao appAuctionWithdrawcashDao;
    @Resource
    private AppAuctionBidLogDao appAuctionBidLogDao;
    @Resource
    private AppAuctionDao appAuctionDao;
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
     * @param appAuctionBailRefundLog
     * @return
     */
    @Override
    public AppAuctionBailRefundLog get(AppAuctionBailRefundLog appAuctionBailRefundLog) {
        return super.get(appAuctionBailRefundLog);
    }

    /**
     * 查询分页数据
     *
     * @param appAuctionBailRefundLog 查询条件
     *                                appAuctionBailRefundLog.page 分页对象
     * @return
     */
    @Override
    public Page<AppAuctionBailRefundLog> findPage(AppAuctionBailRefundLog appAuctionBailRefundLog) {
        return super.findPage(appAuctionBailRefundLog);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appAuctionBailRefundLog
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppAuctionBailRefundLog appAuctionBailRefundLog) {
        super.save(appAuctionBailRefundLog);
    }

    /**
     * 更新状态
     *
     * @param appAuctionBailRefundLog
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppAuctionBailRefundLog appAuctionBailRefundLog) {
        super.updateStatus(appAuctionBailRefundLog);
    }

    /**
     * 删除数据
     *
     * @param appAuctionBailRefundLog
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppAuctionBailRefundLog appAuctionBailRefundLog) {
        super.delete(appAuctionBailRefundLog);
    }

    /**
     * 1,查询出需要退款的用户
     * 1.1车辆状态大于7且未中标
     * 1.2预退款订单里的订单状态为initial初始状态或关闭状态
     * 退款失败的订单也需要重复退款
     */
    @Transactional(readOnly = false)
    public List<AppAuctionBailRefundLog> findRefundUser() throws Exception {
        byte[] bytes = ResourceUtil.readBytes(certPath);

        //锁住整个方法
        String redisKey = RedisKeyUtils.REDIS_LOCK_REFUND_NOTIFY;
        redisLock.lock(redisKey);

        List<AppAuctionBailRefundLog> appAuctionBailRefundLog = bailRefundLogDao.findRefundUser();
        logger.info("### 单车保证金退款操作 ## 需要退款人的信息 appAuctionBailRefundLog={} ## ", appAuctionBailRefundLog);

        try {
            for (AppAuctionBailRefundLog refundUser : appAuctionBailRefundLog) {
                if (refundUser.getState() == AppAuctionRefundConstans.AUCTION_REFUND_FAIL || refundUser.getState() == AppAuctionRefundConstans.AUCTION_REFUND_INITIAL) {
                    //如果是中标用户  车辆状态必须是car_state=12过户完成 才能退款
                    Long carId = refundUser.getCarId();
                    //根据车辆id查询中标人ID
                    Long userId = appAuctionBidLogDao.queryUserIdByCarId(carId);
                    if (userId != null && refundUser.getUserId() == userId) {
                        // userId != null 表示没有流拍  并且当前退款操作是中标人的
                        AppAuction appAuction = appAuctionDao.findAuctionByCarId(carId);
                        logger.info("#######   退款车辆信息  appAuction ={}", appAuction);
                        if (appAuction.getCarState() != 12) {
                            //并且状态不是过户完成  跳过此次退款
                            logger.info("### 车辆状态不对，中标人不能退保证金 userId={};carState={}; carID={}; ####", userId, appAuction.getCarState(), carId);
                            continue;
                        }
                    }


                    logger.info("### 待退款数据 ### refund={}", refundUser.toString());
                    Double amount = refundUser.getAmount() / 100;
                    WxPayRefundResult refund = wxPayV2Util.refund(appId, mchId, mchKey, bytes, refundUser.getOutTradeNo(), refundUser.getOutRefundNo(), new BigDecimal(amount.toString()), new BigDecimal(amount.toString()));
                    logger.info("### 退款结果 ### reqInfo={}", refund.toString());
                    refundUser.setRefundId(refund.getRefundId());
                    refundUser.setBackStatus(refund.getResultCode());
                    refundUser.setNotifyTime(new Date());
                    refundUser.setState(AppAuctionRefundConstans.AUCTION_REFUND_RUNNING);
                    if (refund.getResultCode().equals("SUCCESS")) {
                        refundUser.setExplain("错误码：" + refund.getErrCode() + "|错误码翻译：" + refund.getErrCodeDes());
                    } else {
                        logger.error("### 退款失败  结果  reqInfo={}", refund);
                        refundUser.setExplain(refund.getReturnMsg());
                    }
                    logger.info("### 修改后的退款数据 ### refund={}", refundUser.toString());

                    bailRefundLogDao.update(refundUser);
                }
            }
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e);
        } finally {
            redisLock.unlock(redisKey);
        }
        return appAuctionBailRefundLog;
    }

    @Transactional(readOnly = false)
    public void dealBackResult() throws Exception {
        AppAuctionBailRefundLog paramer = new AppAuctionBailRefundLog();
        paramer.setState(AppAuctionRefundConstans.AUCTION_REFUND_RUNNING);

        List<AppAuctionBailRefundLog> appAuctionBailRefundLog = bailRefundLogDao.findList(paramer);
        for (AppAuctionBailRefundLog auctionBailRefundLog : appAuctionBailRefundLog) {
            //查询结果
            WxPayRefundQueryResult refund = wxPayV2Util.refundQuery(appId, mchId, mchKey, auctionBailRefundLog.getOutRefundNo());
            if (refund.getReturnCode().equals("SUCCESS")) {
                //查询成功 不代表退款结果成功
                List<WxPayRefundQueryResult.RefundRecord> refundRecords = refund.getRefundRecords();
                //不支持分批次退款，每次退款都是全额退款
                WxPayRefundQueryResult.RefundRecord refundRecord = refundRecords.get(0);
                String refundStatus = refundRecord.getRefundStatus();
                auctionBailRefundLog.setExplain(refundStatus);
                if (refundStatus.equals("SUCCESS")) { // 代表业务退款是否成功
                    auctionBailRefundLog.setState(AppAuctionRefundConstans.AUCTION_REFUND_SUCCESS);

                    AppAuctionTransactionLog appAuctionTransactionLog = new AppAuctionTransactionLog();
                    appAuctionTransactionLog.setOrderId(auctionBailRefundLog.getOutTradeNo());
                    appAuctionTransactionLog.setDesc(AppAuctionRefundConstans.AUCTION_REFUND_DESC);
                    AppAuctionTransactionLog appAuctionTransactionLog1 = transactionLogService.get(appAuctionTransactionLog);
                    if (Objects.isNull(appAuctionTransactionLog1)) {
                        AppAuctionTransactionLog appAuctionTransactionLog2 = transactionLog2DTO(auctionBailRefundLog);
                        logger.info("## 查询单车保证金退款  成功    插入用户记录 appAuctionTransactionLog2={}", appAuctionTransactionLog2);
                        transactionLogService.insert(appAuctionTransactionLog2);
                    }
                } else {
                    auctionBailRefundLog.setState(AppAuctionRefundConstans.AUCTION_REFUND_FAIL);
                }
                bailRefundLogDao.update(auctionBailRefundLog);
            }

        }
    }

    private AppAuctionTransactionLog transactionLog2DTO(AppAuctionBailRefundLog auctionBailRefundLog) {
        AppAuctionTransactionLog appAuctionTransactionLog = new AppAuctionTransactionLog();
        appAuctionTransactionLog.setType(1);
        appAuctionTransactionLog.setUserId(auctionBailRefundLog.getUserId());
        appAuctionTransactionLog.setAmount(auctionBailRefundLog.getAmount());
        appAuctionTransactionLog.setOrderId(auctionBailRefundLog.getOutTradeNo());
        appAuctionTransactionLog.setPayType(1);
        appAuctionTransactionLog.setOrderState(6);
        appAuctionTransactionLog.setCreateTime(new Date());
        appAuctionTransactionLog.setDesc("单车保证金退款");
        appAuctionTransactionLog.setCarId(auctionBailRefundLog.getCarId());
        return appAuctionTransactionLog;
    }

    //vip开始保证金退款
    @Transactional(readOnly = false)
    public void moneyBack() {
        byte[] bytes = ResourceUtil.readBytes(certPath);

        List<AppAuctionWithdrawcash> list = appAuctionWithdrawcashDao.findAllBack(0);
        //查询用户之前的支付记录，去原路退款
        for (AppAuctionWithdrawcash appAuctionWithdrawcash : list) {
            Integer userId = appAuctionWithdrawcash.getUserId();

            //查询该用户所有的vip充值记录
            List<AppAuctionPayLog> appAuctionPayLogDaoList = appAuctionPayLogDao.queryByUserId(userId);

            StringBuilder tmp = new StringBuilder();
            for (AppAuctionPayLog appAuctionPayLog : appAuctionPayLogDaoList) {
                //记录这次退款关联的所有支付订单id
                tmp.append(appAuctionPayLog.getOutTradeNo() + ",");

                String uniqueOrder = COrderNoUtil.getUniqueOrder();
                if (appAuctionPayLog.getBackStatus() != null && appAuctionPayLog.getBackStatus() == 2) {
                    logger.info("## 退款状态为2:成功的不用退款！appAuctionPayLog={}", appAuctionPayLog);
                    continue;
                }
                WxPayRefundResult refund = null;
                try {
                    logger.info("### vip待退款数据 ### appAuctionPayLog={}", appAuctionPayLog);
                    refund = wxPayV2Util.refund(appId, mchId, mchKey, bytes, appAuctionPayLog.getOutTradeNo(), uniqueOrder, new BigDecimal(appAuctionPayLog.getAmount()).divide(new BigDecimal(100)), new BigDecimal(appAuctionPayLog.getAmount()).divide(new BigDecimal(100)));
                    logger.info("### vip退款结果 ### refund={}", refund.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (refund != null && "INVALID_REQUEST".equals(refund.getErrCode()) && "订单已全额退款".equals(refund.getErrCodeDes())) {
                        //设置所有的支付订单退款id
                        appAuctionWithdrawcash.setPayOrderNoAll(tmp.deleteCharAt(tmp.length() - 1).toString());
                        appAuctionWithdrawcash.setState(3);//状态设置为已申请提现
                        appAuctionWithdrawcashDao.update(appAuctionWithdrawcash);
                    }
                } finally {
                    if (refund != null) {
                        appAuctionPayLog.setBackTradeNo(uniqueOrder);
                        appAuctionPayLog.setRefundId(refund.getRefundId());
                        appAuctionPayLog.setErrCode(refund.getErrCode());
                        appAuctionPayLog.setErrCodeDesc(refund.getErrCodeDes());
                        appAuctionPayLog.setReturnMsg(refund.getReturnMsg());
                        appAuctionPayLogDao.update(appAuctionPayLog);
                    }
                }
            }
            //设置所有的支付订单退款id
            appAuctionWithdrawcash.setPayOrderNoAll(tmp.deleteCharAt(tmp.length() - 1).toString());

            appAuctionWithdrawcash.setState(3);//状态设置为已申请提现
            appAuctionWithdrawcashDao.update(appAuctionWithdrawcash);
        }


    }

    //vip保证金退款结果查询
    @Transactional(readOnly = false)
    public void queryMoneyBack() {
        AppAuctionWithdrawcash appAuctionWithdrawcashParamer = new AppAuctionWithdrawcash();
        appAuctionWithdrawcashParamer.setState(3);//已申请提现
        List<AppAuctionWithdrawcash> list = appAuctionWithdrawcashDao.findList(appAuctionWithdrawcashParamer);
        logger.info("#####vip保证金退款结果查询 #### list={}", list);
        for (AppAuctionWithdrawcash appAuctionWithdrawcash : list) {
            String payOrderNoAll = appAuctionWithdrawcash.getPayOrderNoAll();
            String[] ids = payOrderNoAll.split(",");
            boolean flagAlltrue = true;//代表是否所有子订单都成功
            for (String outTradeNo : ids) {
                AppAuctionPayLog appAuctionPayLogParamer = new AppAuctionPayLog();
                appAuctionPayLogParamer.setOutTradeNo(outTradeNo);
                AppAuctionPayLog appAuctionPayLog = appAuctionPayLogDao.get(appAuctionPayLogParamer);
                logger.info("#####查询vip支付金额记录 #### appAuctionPayLog={}；outTradeNo={}", appAuctionPayLog, outTradeNo);
                if (appAuctionPayLog.getBackStatus() != null && appAuctionPayLog.getBackStatus() == 2) { //状态为成功的不用再查询结果
                    continue;
                }
                WxPayRefundQueryResult refund = null;
                try {
                    refund = wxPayV2Util.refundQuery(appId, mchId, mchKey, appAuctionPayLog.getBackTradeNo());
                    logger.info("#####查询vip支付金额记录结果 #### refund={}；BackTradeNo={}", refund, appAuctionPayLog.getBackTradeNo());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (refund.getReturnCode().equals("SUCCESS")) {
                    //查询成功 不代表退款结果成功
                    List<WxPayRefundQueryResult.RefundRecord> refundRecords = refund.getRefundRecords();
                    //不支持分批次退款，每次退款都是全额退款
                    WxPayRefundQueryResult.RefundRecord refundRecord = refundRecords.get(0);
                    String refundStatus = refundRecord.getRefundStatus();
                    if (refundStatus.equals("SUCCESS")) { // 代表业务退款是否成功
                        appAuctionPayLog.setBackStatus(2);
                    } else {
                        logger.info("#####vip保证金退款失败 状态={}", refundStatus);
                        flagAlltrue = false;//有退款失败了
                        appAuctionPayLog.setBackStatus(3);
                    }
                    appAuctionPayLogDao.update(appAuctionPayLog);
                }
            }
            if (flagAlltrue) {//只要有一次失败的，那么就不改变状态
                appAuctionWithdrawcash.setState(1);//提现成功
                appAuctionWithdrawcashDao.update(appAuctionWithdrawcash);
            }

        }
    }
}