package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.AppAuctionCashOutConstant;
import com.cheji.web.constant.AppAuctionConstant;
import com.cheji.web.constant.AppAuctionVipSet;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.*;
import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.mapper.*;
import com.cheji.web.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 退vip申请
 * </p>
 *
 * @author yang
 */
@Service
@Slf4j
public class AppAuctionWithdrawCashService extends ServiceImpl<AppAuctionWithdrawCashMapper, AppAuctionWithdrawCashEntity> implements IService<AppAuctionWithdrawCashEntity> {

    @Autowired
    private AppAuctionWithdrawCashService withdrawCashService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppAuctionVipControlService vipControlService;

    @Autowired
    private AppAuctionOrderMapper appAuctionOrderMapper;

    @Autowired
    private AppAuctionBidMapper appAuctionBidMapper;

    @Resource
    private RedisLock redisLock;

    @Autowired
    private AppAuctionTransactionLogService transactionLogService;

    @Resource
    private AppUserMapper appUserMapper;

    /**
     * @param result
     * @param withdrawCash 申请提现并取消vip资格,添加到交易记录
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject addApplyFor(JSONObject result, AppAuctionWithdrawCashEntity withdrawCash) {
        UserEntity userEntity = userService.selectById(withdrawCash.getUserId());
        String lockKey = RedisConstant.ADD_CASH_OUT_LOCK + withdrawCash.getUserId();
        redisLock.lock(lockKey);//防止因为网络原因导致用户连点两次提现
        try {
            AppAuctionVipControlEntity appAuctionVipControl = vipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("user_id", withdrawCash.getUserId()).eq("state", AppAuctionConstant.ONE));

            try {
                AssertUtil.isNotNull(withdrawCash, "withdrawCash无数据");
            } catch (Exception e) {
                result.put("code", 533);
                result.put("msg", "参数错误!");
                return result;
            }

            if (Objects.isNull(appAuctionVipControl) || Objects.isNull(userEntity.getVipLv()) || Integer.valueOf(userEntity.getVipLv()) < 1) {
                result.put("code", 534);
                result.put("msg", "非vip不能提现!");
                return result;
            }


//            if (StringUtils.isEmpty(withdrawCash.getUserBankId())) {
//                result.put("code", 535);
//                result.put("msg", "请添加银行卡!");
//                return result;
//            }
            log.info("## 查询是否有重复的提交提现操作  start  ###");
            Integer withDraws = withdrawCashService.selectCount(new EntityWrapper<AppAuctionWithdrawCashEntity>().
                    where("state=0").
                    or("state=2").
                    andNew().eq("user_id", withdrawCash.getUserId()));
            log.info("## 查询是否有重复的提交提现操作  end ###");

            if (withDraws > 0) {
                result.put("code", 536);
                result.put("msg", "请勿重复提交提现操作!");
                return result;
            }


            //查询 非单车保证金的用户出价记录
            Integer count = appAuctionBidMapper.selectCountBidByUser(withdrawCash.getUserId(), AppAuctionConstant.ZERO);

            if (count > 0) {
                result.put("code", 539);
                result.put("msg", "参与拍卖中,请勿提现!");
                return result;
            }

            //查询 非单车保证金的订单交易记录
            Integer orderCount = appAuctionOrderMapper.countOrderByUser(withdrawCash.getUserId(), 0);
            if (orderCount > 0) {
                result.put("code", 540);
                result.put("msg", "请完成所有交易后提现!");
                return result;
            }

            if (appAuctionVipControl.getAmount().divide(new BigDecimal("100.00")).compareTo(withdrawCash.getAmount()) != 0) {
                result.put("code", 541);
                result.put("msg", "金额不对!");
                return result;
            } else {
                //金额正确后将保证金乘以100
                withdrawCash.setAmount(withdrawCash.getAmount().multiply(new BigDecimal("100")));
            }


            //设置提现表VIP等级
            withdrawCash.setVipLv(userEntity.getVipLv());
            //取消用户vip等级
            userEntity.setVipLv(AppAuctionConstant.ZERO);
            userService.updateById(userEntity);

            appAuctionVipControl.setState(AppAuctionConstant.TWO);
            vipControlService.updateStateByUser(appAuctionVipControl.getUserId(), appAuctionVipControl.getState());


            //设置提现状态
            if (withdrawCash.getState() == null) {
                withdrawCash.setState(AppAuctionCashOutConstant.CASHOUT_STATE_DRAWING);
            }
            withdrawCashService.insert(withdrawCash);

            AppAuctionTransactionLogEntity appAuctionTransactionLogEntity = new AppAuctionTransactionLogEntity();
            appAuctionTransactionLogEntity.setPayType(AppAuctionConstant.THREE);
            appAuctionTransactionLogEntity.setDesc("VIP保证金提现");
            appAuctionTransactionLogEntity.setOrderState(AppAuctionConstant.THREE);
            appAuctionTransactionLogEntity.setAmount(appAuctionVipControl.getAmount());
            appAuctionTransactionLogEntity.setCreateTime(new Date());
            appAuctionTransactionLogEntity.setType(AppAuctionConstant.ONE);
            appAuctionTransactionLogEntity.setUserId(withdrawCash.getUserId());
            appAuctionTransactionLogEntity.setOrderId(String.valueOf(withdrawCash.getId()));
            transactionLogService.insert(appAuctionTransactionLogEntity);

            result.put("code", 200);
            result.put("msg", "申请成功!");
            result.put("data", "一般申请在5-7个工作日内到账!");
            return result;
        } finally {
            redisLock.unlock(lockKey);
        }


    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JSONObject cancelApplyFor(JSONObject result, AppUserEntity user) {
        AppAuctionWithdrawCashEntity withdrawCash = withdrawCashService.selectOne(new EntityWrapper<AppAuctionWithdrawCashEntity>().eq("state", AppAuctionCashOutConstant.CASHOUT_STATE_DRAWING).eq("user_id", user.getId()));
        if (Objects.isNull(withdrawCash)) {
            result.put("code", 450);
            result.put("msg", "没有提现需要取消!");
            return result;
        } else {
            AppAuctionVipControlEntity appAuctionVipControl = vipControlService.selectOne(new EntityWrapper<AppAuctionVipControlEntity>().eq("state", AppAuctionVipSet.VIP_STATE_CASHOUT));
            if (Objects.isNull(appAuctionVipControl)) {
                result.put("code", 451);
                result.put("msg", "无vip提现可取消!");
                return result;
            } else {
                appAuctionVipControl.setState(AppAuctionVipSet.VIP_STATE_USE);
                vipControlService.updateById(appAuctionVipControl);
            }

            withdrawCash.setState(AppAuctionCashOutConstant.CASHOUT_STATE_CANCEL);
            withdrawCashService.updateById(withdrawCash);

            user.setVipLv(withdrawCash.getVipLv());
            appUserMapper.updateById(user);

            result.put("code", 200);
            result.put("msg", "取消成功!");
            return result;

        }
    }
}
