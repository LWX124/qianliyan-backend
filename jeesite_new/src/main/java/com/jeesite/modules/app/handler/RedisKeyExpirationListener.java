package com.jeesite.modules.app.handler;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.dao.AppAuctionBidLogDao;
import com.jeesite.modules.app.entity.AppAuction;
import com.jeesite.modules.app.entity.AppAuctionBidLog;
import com.jeesite.modules.app.entity.AppAuctionUp;
import com.jeesite.modules.app.entity.AppAuctionVipControl;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.AppAuctionConstant;
import com.jeesite.modules.constant2.AuctionConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private AppAuctionBidLogService appAuctionBidLogService;
    @Resource
    private AppAuctionBidLogDao appAuctionBidLogDao;
    @Resource
    private AppAuctionOrderService appAuctionOrderService;
    @Resource
    private AppAuctionVipControlService appAuctionVipControlService;
    @Resource
    private AppAuctionService appAuctionService;
    @Resource
    private AppAuctionUpService appAuctionUpService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisLock redisLock;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        log.info("======================redis time out========================" + expiredKey);
        expiredKeyUse(expiredKey);
    }

    //    @CacheEvict(cacheNames = AppAuctionRedisConstans.KEY_RECOMMEND_HOT, key = "'All'")
    public void expiredKeyUse(String expiredKey) {
        if (expiredKey.startsWith(AppAuctionConstant.CAR_AUCTION_OVERDUE_KEY)) {
            //拍卖结束 结算拍卖
            String carId = expiredKey.split(":")[1];
            if (StringUtils.isEmpty(carId)) {
                log.info("##未能获取carId:");
                throw new RuntimeException("未能获取carId");
            }
            log.info("##Key失效的ID:" + expiredKey);
            String lockKey = AppAuctionConstant.AUCTION_BID_LOCK + carId;
            redisLock.lock(lockKey);
            try {

                //找到去重过后所有人出价信息
                List<AppAuctionBidLog> bidList = appAuctionBidLogDao.findDistinctUserListByCarId(Long.valueOf(carId));

                if (bidList.size() < 1) {//出价记录小于1  表示流拍
                    AppAuction appAuction = appAuctionService.get(carId);
                    appAuction.setCarState(AppAuctionConstant.NINE);
                    appAuction.setUpState(AppAuctionConstant.UP_STATE_DOWN);
                    appAuction.setUpdateTime(new Date());
                    appAuctionService.update(appAuction);
//                    appUp.setCarState(AppAuctionConstant.NINE);
//                    appUp.setUpState(AppAuctionConstant.UP_STATE_DOWN);
//                    appUp.setUpdateTime(new Date());
//                    appAuctionUpService.update(appUp);
                } else {//出价记录大于一 ，表示有人中标
                    JSONObject object = endAuction(bidList, carId);
                    String code = object.getString("code");
                    if (!AppAuctionConstant.SUCCESS.equals(code)) {
                        log.error("##" + object.getString("code"));
                        log.error("##" + object.getString("msg"));
                        throw new RuntimeException("录入失败");
                    }
                    log.info("##有拍卖车辆结算");
                }
            } finally {
                redisLock.unlock(lockKey);
            }
        }
//        if (expiredKey.startsWith(AppAuctionConstant.BEGIN_BID_ORDER_CARID.substring(0, 12))) {
//            log.info("##获取开始时间!");
//            String carId = expiredKey.split(":")[1];
//            if (StringUtils.isEmpty(carId)) {
//                log.info("##获取carId失败!");
//                throw new RuntimeException("获取carId失败");
//            }
//
//            AppAuction appAuction = appAuctionService.get(carId);
//            if (appAuction.getCarState() == AppAuctionConstant.THREE) {
//                appAuction.setCarState(AppAuctionConstant.SEVEN);
//                appAuctionService.update(appAuction);
//
//                AppAuctionUp auctionUp = appAuctionUpService.findAuctionUp(Long.valueOf(carId));
//                auctionUp.setCarState(AppAuctionConstant.SEVEN);
//                appAuctionUpService.update(auctionUp);
//                log.info("##开始时间修改状态成功!");
//            }
//        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public JSONObject endAuction(List<AppAuctionBidLog> bidList, String carId) {

        //根据出价记录找到出价最高的人
        Long userId = appAuctionBidLogDao.queryUserIdByCarId(Long.valueOf(carId));
        log.info("### carId={} 车的最高出价人id={}", carId, userId);
        log.info("### 最高出价信息={}", bidList.get(0));

        log.info("##获取其他参拍用户信息:{}", bidList);
        if (bidList.size() > 0) {
            //恢复其他参拍客户冻结台次等
            for (AppAuctionBidLog appAuctionBidLog : bidList) {
                Long bidUserId = appAuctionBidLog.getUserId();
                if (String.valueOf(bidUserId).equals(String.valueOf(userId))) {
                    continue;
                }
                AppAuctionVipControl vipControl1 = appAuctionVipControlService.getByUserId(String.valueOf(bidUserId));
                if (Objects.nonNull(vipControl1) && vipControl1.getFreezeCount() >= 0) {
                    vipControl1.setCarCount(vipControl1.getCarCount() + 1);
                    vipControl1.setFreezeCount(vipControl1.getFreezeCount() - 1);
                    appAuctionVipControlService.update(vipControl1);
                }
            }

        }


        // 录入订单信息
        JSONObject object = appAuctionOrderService.readyOrder(bidList.get(0), carId, userId);
        String code = object.getString("code");
        if (!AppAuctionConstant.SUCCESS.equals(code)) {
            log.error("##" + object.getString("code"));
            log.error("##" + object.getString("msg"));
            throw new RuntimeException("录入失败");
        }
        log.info("录入订单data信息:", object.getString("data"));

        //将所有出价信息设置为过时
        bidList.stream().filter(bid -> AppAuctionConstant.BID_ON.equals(bid.getValid())).peek(bid -> bid.setValid(AppAuctionConstant.BID_OFF)).forEach(a -> {
            appAuctionBidLogService.update(a);
        });

        return object;
//        } else {
//            result.put("code", 413);
//            result.put("msg", "redis无出价数据");
//            return result;
//        }
    }

}
