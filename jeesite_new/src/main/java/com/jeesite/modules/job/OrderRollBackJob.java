package com.jeesite.modules.job;

import com.jeesite.modules.app.dao.AppOrderRollBackDao;
import com.jeesite.modules.app.entity.AppOrderRollBack;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.app.service.AppBUserService;
import com.jeesite.modules.app.service.AppUpMerchantsService;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.job.service.OrderRollBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 此表为商品库存回滚辅助表，如果用户在点击支付之后，系统会给用户下单，
 * 如果支付失败（包含用户主动取消支付），对于合约商品的订单，
 * \r\n我们需要通过定时任务在半小时候之后把未付款的商品库存加回去
 */
@Component
@Configuration
@EnableScheduling
public class OrderRollBackJob {

    private final static Logger logger = LoggerFactory.getLogger(MerchantsJob.class);

    @Resource
    private AppOrderRollBackDao appOrderRollBackDao;

    @Resource
    private OrderRollBackService orderRollBackService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;

    //三分钟一次
//    @Scheduled(cron = "0 0/3 * * * ?")
    public void putBuserInblack() {
        List<AppOrderRollBack> list = appOrderRollBackDao.selectUnOpsList();
        for (AppOrderRollBack appOrderRollBack : list) {
            //回滚合约商品的库存
            if (appOrderRollBack.getType() == 1) {
                orderRollBackService.rollBack(appOrderRollBack);
            }
        }
    }

//    @Scheduled(cron = "0 0/30 9-18 * * ?")
//    @Scheduled(cron = "0 */1 * * * ?")
    public void freeToRedis(){
        // 删除所有关联上架的keys
        Set<String> keys = redisTemplate.keys(RedisKeyUtils.MERCHANTS_UP_GEO + "*");
        redisTemplate.delete(keys);
        // 查询所有关联的上架商铺
        List<String> upMerchants = appBUserService.findUpMerchants();
        List<AppUpMerchants> collect = appUpMerchantsService.findUpMerchantsByBrands(upMerchants);

        collect.forEach(mer ->
                redisTemplate.opsForGeo()
                        .add(RedisKeyUtils.MERCHANTS_UP_GEO + mer.getBrand()
                                ,new Point(Double.valueOf(mer.getLng().toString()),Double.valueOf(mer.getLat().toString()))
                                , mer.toString()));
    }

}
