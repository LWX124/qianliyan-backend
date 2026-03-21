//package com.jeesite.modules.job;
//
//import com.jeesite.modules.app.dao.AppUserDao;
//import com.jeesite.modules.app.dao.AppUserPlusDao;
//import com.jeesite.modules.app.dao.AppWxpayOrderDao;
//import com.jeesite.modules.app.entity.AppUserPlus;
//import com.jeesite.modules.app.service.WebSocketService;
//import com.jeesite.modules.constant2.AppConstants;
//import com.jeesite.modules.job.service.PlusUserService;
//import com.jeesite.modules.util2.ShareCodeUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * plus会员定时任务
// */
//@Component
//@Configuration
//@EnableScheduling
//public class PlusUserJob {
//
//    private final static Logger logger = LoggerFactory.getLogger(PlusUserJob.class);
//
//    @Resource
//    private AppWxpayOrderDao appWxpayOrderDao;
//
//    @Resource
//    private AppUserDao appUserDao;
//
//    @Resource
//    private PlusUserService plusUserService;
//
//    @Resource
//    private AppUserPlusDao appUserPlusDao;
//
//    @Value("${proFileName}")
//    private String proFileName;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Resource
//    private WebSocketService webSocketService;
//
//    /**
//     * 所有人免费开通plus会员
//     */
//    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
//    public void getPlusAll() {
//        //找开关
//        Object s = redisTemplate.opsForValue().get(AppConstants.PLUS_MEMBER_FREE_FLAG);
//        Date currentDate = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.YEAR, 1);
//        Date time = calendar.getTime();
//        logger.info("###  所有人免费开通plus会员###");
//        if ("true".equals(s)) {
//            List<Map> maps = appUserDao.select4PlusAll();
//            for (Map map : maps) {
//                long id = (long) map.get("id");
//                logger.info("### 用户 id={}；正在开通plus会员", id);
//                AppUserPlus t = new AppUserPlus();
//                t.setUserId(id);
//                t.setCreateTime(currentDate);
//                t.setInviteCode(ShareCodeUtil.toSerialCode(id));
//                t.setInvalidTimeStart(currentDate);
//                t.setInvalidTimeEnd(time);
//                appUserPlusDao.insertSelf(t);
//                logger.info("### 用户 id={}；开通plus会员成功", id);
//            }
//        }
//    }
//
//    @Scheduled(fixedDelay = 1000 * 60 * 5)
//    public void start() {
////            webSocketService.sendMessageToAllUsers(new TextMessage("1"));
//
//        logger.info("###plus会员定时任务  当前时间###{}", LocalDateTime.now());
//
////        if (!proFileName.equals("pro")) {
////            logger.info("###不是生产环境  不需要执行 plus会员定时任务###{}", new Date());
////            return;
////        }
//        Object s = redisTemplate.opsForValue().get(AppConstants.PLUS_MEMBER_AMOUNT);
//        if (s == null) {
//            logger.error("### plus会员定时任务###  plus会员提成金额为空  请检查");
//            return;
//        }
//        logger.info("### plus会员定时任务###  plus会员提成金额为 ={}", s);
//
//        //查找未处理的订单 为邀请人增加分成
//        List<Map> maps = appWxpayOrderDao.selectForPlusMem();
//        for (Map map : maps) {
//
//            //为用户开通plus会员
////                AppUserPlus appUserPlus = new AppUserPlus();
////                appUserPlus.setCreateTime(new Date());
////                appUserPlus.setUserId((Long) map.get("user_id"));
////                appUserPlus.setInvalidTimeEnd(new Date());
////
////                //计算一年后的今天
////                Calendar calendar = Calendar.getInstance();
////                calendar.add(Calendar.YEAR,1);
////                Date time = calendar.getTime();
////                appUserPlus.setInvalidTimeEnd(time);
////
////                appUserPlus.setInviteCode(ShareCodeUtil.toSerialCode((Long) map.get("user_id")));
//
////                if (parent_id == null) {
////                    appWxpayOrderDao.updateBusinessStatus(String.valueOf(map.get("out_trade_no")), ConsEnum.WX_PAY_ORDER_BUSINESS_STATUS_OPS.getCode());
////                    logger.info("### plus会员定时任务###  parent_id为空,不需要分钱  map={}", map);
////                    continue;
////                }
//
//            plusUserService.addPlusAmount(map);
//        }
//
//    }
//}
