package com.cheji.web.modular.listener;


import com.cheji.web.modular.lock.RedisLock;
import com.cheji.web.modular.service.AppAuctionBailLogService;
import com.cheji.web.modular.service.AppAuctionBidService;
import com.cheji.web.modular.service.AppAuctionUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AppAuctionUpService appAuctionUpService;
    @Autowired
    private AppAuctionBidService appAuctionBidService;
    @Autowired
    private AppAuctionBailLogService bailLogService;

    @Autowired
    private RedisLock redisLock;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
//        String expiredKey = message.toString();
//        log.info("======================redis time out========================" + expiredKey);
//
//        if (expiredKey.startsWith(AppAuctionConstant.END_BID_ORDER_CARID.substring(0, 12))) {
//            String carId = expiredKey.split(":")[1];
//            if (StringUtils.isEmpty(carId)) {
//                log.info("##redis失效KEY获取ID失败");
//            }
//            List<AppAuctionBidEntity> bidList = appAuctionBidService.selectList(new EntityWrapper<AppAuctionBidEntity>()
//                    .eq("car_id", carId).eq("valid", AppAuctionConstant.BID_ON));
//            AppAuctionUpEntity appUp = appAuctionUpService.selectOne(new EntityWrapper<AppAuctionUpEntity>()
//                    .eq("car_id", carId));
//            String tokenKey = String.format(AuctionConstant.KEY_AUCTION_BID_TOKEN, carId);
//            String s = (String) redisTemplate.opsForValue().get(tokenKey);
//            if (!StringUtils.isEmpty(s)) {
//                s = s.replace("\"", "");
//                String[] split = s.split(":");
//                String userId = split[0];
//                String bid2 = split[1];
//                log.info("##获取redis出价信息:" + bid2);
//                BigDecimal bigDecimal = new BigDecimal(bid2);
//                //验证最高价,是否在拍
//                List<AppAuctionBidEntity> collect1 = bidList.stream().sorted(Comparator.comparing(AppAuctionBidEntity::getBid).reversed()).limit(1).collect(Collectors.toList());
//                if (collect1.size() < 1) {
//                    log.error("##最高出价验证失效!");
//                    throw new RuntimeException("##无信息!");
//                }
//                BigDecimal topPrice = collect1.get(0).getBid();
//                if (topPrice == null || bigDecimal == null || bigDecimal.compareTo(topPrice) != 0) {
//                    log.error("##redis中价格与bidlog中最高价不同!");
//                    throw new RuntimeException("##价格错误,请验证!");
//                }
//                List<Long> collect = bidList.stream().filter(a -> !userId.equals(a.getUserId().toString()))
//                        .collect(Collectors.collectingAndThen(
//                                Collectors.toCollection(() -> new TreeSet<>(
//                                        Comparator.comparing(
//                                                AppAuctionBidEntity::getUserId))), ArrayList::new))
//                        .stream().map(AppAuctionBidEntity::getUserId).collect(Collectors.toList());
//                log.info("##获取其他参拍用户信息id:" + collect);
//
//                if (collect.size() > 0) {
//                    List<AppAuctionBailLogEntity> appAuctionBailLogEntities = bailLogService.selectBatchIds(collect);
//                    for (AppAuctionBailLogEntity appAuctionBailLogEntity : appAuctionBailLogEntities) {
//                        if(carId.equals(appAuctionBailLogEntity.getCarId().toString())
//                                && appAuctionBailLogEntity.getStatus() == AppAuctionBailConstant.AUCTION_BAIL_SUCCESS){
//                            //设置退款订单号
//                            String outRefundNo = COrderNoUtil.getUniqueOrder();
//                            JSONObject object = new JSONObject();
//                            object.put("refund",appAuctionBailLogEntity.getAmount());
//                            object.put("total",appAuctionBailLogEntity.getAmount());
//                            object.put("currency","CNY");
//                            WxPayRefundUtil.bailRrfund(outRefundNo,appAuctionBailLogEntity.getOutTradeNo(),object);
//                        }
//                    }
//                }
//            }
//        }
//    }



}
