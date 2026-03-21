package com.jeesite.modules.job;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jeesite.modules.app.dao.AppAuctionDao;
import com.jeesite.modules.app.dao.AppAuctionOnePriceCarLogDao;
import com.jeesite.modules.app.entity.AppAuction;
import com.jeesite.modules.app.entity.AppAuctionUp;
import com.jeesite.modules.app.entity.AppAuctionVipControl;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.AppAuctionConstant;
import com.jeesite.modules.constant2.AppAuctionRedisConstans;
import com.jeesite.modules.constant2.AppAuctionVipConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 拍卖定时任务
 */
@Component
@Configuration
@EnableScheduling
@Slf4j
public class AppAuctionJob {

    @Autowired
    private AppAuctionBailRefundLogService bailRefundLogService;

    @Autowired
    private AppAuctionOnePriceCarLogService appAuctionOnePriceCarLogService;

    @Autowired
    private AppAuctionUpService appAuctionUpService;

    @Autowired
    private AppAuctionService appAuctionService;
    @Autowired
    private AppAuctionDao appAuctionDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private AppAuctionVipControlService vipControlService;


    /**
     * 设置默认车辆的开始时间
     */
    @Scheduled(fixedDelay = 1000 * 60 * 3) //3分钟执行一次
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void beginAuction() {
        //开始当天拍卖任务
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
//        //获取当前小时，24小时制
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if (hour < 12) {
//            log.info("每天12点之前不用上架车辆");
//            return;
//        }
        List<AppAuctionUp> list = appAuctionUpService.findTodayUpCar();

        if (list.size() > 0) {
            log.info("##开始当天拍卖定时任务");
            for (AppAuctionUp appAuctionUp : list) {
                calendar.setTime(date);
                //今天审核通过的车 明天上午十点上架
                calendar.add(Calendar.HOUR, +24);

                //设置时分秒为 10:00:00
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                appAuctionUp.setBeginTime(calendar.getTime());
                appAuctionUp.setUpdateTime(new Date());
                //上架天数  如果数据库没设置，那么使用代码里的默认值
                Integer numDate = appAuctionUp.getUpDateNum() == null ? AppAuctionConstant.FIFTEEN : appAuctionUp.getUpDateNum();
                calendar.add(Calendar.DAY_OF_MONTH, numDate);
                appAuctionUp.setEndTime(calendar.getTime());

                try {

                    AppAuction appAuction = appAuctionDao.findAuctionByCarId(appAuctionUp.getCarId());
                    appAuctionUp.setAppAuction(appAuction);

                    if ("1".equals(appAuction.getFixedPrice())) {
                        //一口价车辆不需要设置redis过期时间
                    } else {
                        //在redis设置拍卖结束过期时间
                        setEndTime(appAuctionUp);
                    }

                    appAuctionUpService.update(appAuctionUp);
                    appAuction.setCarState(AppAuctionConstant.SEVEN);
                    appAuction.setUpState(1);
                    appAuction.setUpdateTime(new Date());
                    appAuctionService.update(appAuction);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    log.error("##车辆id={}；设置redis失效key失败###", appAuctionUp.getCarId());
                }

            }
        }
    }


    /**
     * 由于测试需要  每隔5分钟 把正在拍卖中的车子的结束时间  重新刷一次到redis， 这样redis失效监控才能进行退款
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5分钟执行一次
    public void setEndTimeForRedis() {
        AppAuctionUp paramer = new AppAuctionUp();

        List<AppAuctionUp> list = appAuctionUpService.findList(paramer);
        for (AppAuctionUp appAuctionUp : list) {
            if (appAuctionUp.getEndTime() != null) {
                if ((appAuctionUp.getEndTime().getTime() - new Date().getTime()) / 1000 / 60 <= 1) {
                    //结束时间小于当前时间1分钟数据不再处理
                    continue;
                }

                AppAuction appAuction = appAuctionDao.findAuctionByCarId(appAuctionUp.getCarId());
                if ("1".equals(appAuction.getFixedPrice())) {
                    //一口价车辆不需要设置redis过期时间监听
                    continue;
                }
                if (appAuction.getSourceType() != 1) {
                    //二手车不需要设置redis过期时间监听
                    continue;
                }

                appAuctionUp.setAppAuction(appAuction);
                //设置拍卖结束过期时间
                setEndTime(appAuctionUp);
            }
        }
    }


    /**
     * 设置拍卖结束时间到redis，方便退款（退款功能是监听redis过期key实现的）
     */
    private void setEndTime(AppAuctionUp appAuctionUp) {
        String endbidOrderKey = AppAuctionConstant.CAR_AUCTION_OVERDUE_KEY + appAuctionUp.getCarId();
        //修改结束时间重置rediskey
        long endTimes = appAuctionUp.getEndTime().getTime() - new Date().getTime();
        if (endTimes > 0) {
            log.info("## 设置拍卖结束时间 ###   endbidOrderKey={},CarState={};endTimes={}", endbidOrderKey, appAuctionUp.getAppAuction().getCarState().toString(), endTimes);
            redisTemplate.opsForValue().set(endbidOrderKey, appAuctionUp.getAppAuction().getCarState().toString(), endTimes, TimeUnit.MILLISECONDS);
        } else {
            throw new RuntimeException("拍卖结束时间设置错误!");
        }
    }


    /**
     * 30分钟一次退款拍卖保证金
     * 1,查询出需要退款的用户
     * 1.1车辆状态大于7
     * 1.2预退款订单里的订单状态为initial初始状态
     * 退款失败的订单也需要重复退款
     */
//    @Scheduled(cron = "0 0 10 * * ?")
//    @Scheduled(cron = "0 */30 * * * ?") // 30分钟执行一次
    @Scheduled(fixedDelay = 1000 * 60 * 30) //30分钟执行一次
    public void bailRefund() throws Exception {
        log.info("### 处理单车保证金退款操作################");
        //处理退款 根据返回值并不能得到退款是否成功的结果
        bailRefundLogService.findRefundUser();
    }

    //查询退款结果
//    @Scheduled(cron = "0 */40 * * * ?") // 40分钟执行一次
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5 分钟执行一次
    public void dealBackResult() throws Exception {
        log.info("### 处理单车保证金 查询退款结果################");
        bailRefundLogService.dealBackResult();
    }

    /**
     * 处理一口价退款
     * 订单状态为12已过户完成的，并且不是vip订单用户支付了一口价订金的，需要退还这笔钱
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5分钟执行一次
    public void onePriceRefund() throws Exception {
        log.info("### 一口价订单退款 ################");
        appAuctionOnePriceCarLogService.dealOnerPriceRefund();
    }

    /**
     * 查询一口价退款结果
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5分钟执行一次
    public void queryOnePriceRefund() throws Exception {
        log.info("### 查询一口价退款结果 ################");
        appAuctionOnePriceCarLogService.queryOnerPriceRefund();
    }

//    //每月一号重置
//    @Scheduled(cron = "0 5 0 1 * ?")
//    public void resetOffer() {
//        List<AppAuctionVipControl> list = vipControlService.getVips();
//        for (AppAuctionVipControl vip : list) {
//            if (AppAuctionVipConstant.vip_lv_1.equals(vip.getVipLv())) {
//                vip.setOffer(AppAuctionVipConstant.vip_1_offer);
//            } else if (AppAuctionVipConstant.vip_lv_2.equals(vip.getVipLv())) {
//                vip.setOffer(AppAuctionVipConstant.vip_2_offer);
//            } else {
//                vip.setOffer(AppAuctionVipConstant.vip_else_offer);
//            }
//            vipControlService.update(vip);
//        }
//    }

}
