//package com.jeesite.modules.job;
//
//import com.jeesite.modules.app.entity.AppBUser;
//import com.jeesite.modules.app.entity.AppJgPush;
//import com.jeesite.modules.app.entity.AppUser;
//import com.jeesite.modules.app.service.AppBUserService;
//import com.jeesite.modules.app.service.AppJgPushService;
//import com.jeesite.modules.app.service.AppUserService;
//import com.jeesite.modules.app.service.JPushService;
//import com.jeesite.modules.constant2.JgTokenEnum;
//import org.hibernate.validator.constraints.Length;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Service
//@Component
//public class JPushJob {
//
//    private final static Logger logger = LoggerFactory.getLogger(JPushJob.class);
//
//    @Resource
//    private AppJgPushService appJgPushService;
//
//    @Resource
//    private JPushService jPushService;
//
//    @Resource
//    private AppUserService appUserService;
//
//    @Resource
//    private AppBUserService appBUserService;
//
//    //推送给C端用户
//    @Scheduled(cron = "0 */2 * * * ?")
//    public void jobB(){
//        //查询用户id
//        //定时推送，
////       C端， 红包，订单变化
//        logger.info("### 推送C端定时任务###   开始");
//        //根据推送和来源查询到推送消息
//        List<AppJgPush> pushList = appJgPushService.selectCPush();
//        if (pushList.isEmpty()){
//            logger.info("### 推送C端无数据###   结束");
//            return;
//        }
//        String str = "您有新的通知";
//        for (AppJgPush appJgPush : pushList) {
//            //type为1
//            if (appJgPush.getType().equals("2")){
//                str = "您有订单完成结算,佣金已经到账,点击查看";
//            }
//            if (appJgPush.getType().equals("3")){
//                str = "您提交的事故已经审核成功,红包已经到账,点击查看";
//            }
//            if (appJgPush.getType().equals("5")){
//                str = "您有订单的状态发生改变,点击查看";
//            }
//            String userId = appJgPush.getUserId();
//            AppUser appUser = appUserService.get(userId);
//            jPushService.jiguangPush(appUser.getUsername(),str ,JgTokenEnum.C,appJgPush.getType());
//            //修改状态
//            appJgPush.setIspush("1");
//            appJgPushService.update(appJgPush);
//        }
//    }
//
//
//    //给b端商户推送消息
//    @Scheduled(cron = "0 */2 * * * ?")
//    public void jobC() {
//        //查询用户id
//        //定时推送，
////       C端， 红包，订单变化
//        logger.info("### 推送B端定时任务###   开始");
//        //根据推送和来源查询到推送消息
//        List<AppJgPush> pushList = appJgPushService.selectBPush();
//        if (pushList.isEmpty()){
//            logger.info("### 推送B端无数据###   结束");
//            return;
//        }
//        String str = "您有新的通知";
//        for (AppJgPush appJgPush : pushList) {
//            //type为1
//            if (appJgPush.getType().equals("1")) {
//                str = "您有新的事故视频,点击查看";
//            }
//            if (appJgPush.getType().equals("2")) {
//                str = "您有订单完成并通过,点击查看";
//            }
//            if (appJgPush.getType().equals("4")) {
//                str = "您有新的订单,请注意查收,点击查看";
//            }
//            String userBId = appJgPush.getUserBId();
//            AppBUser appBUser = appBUserService.get(userBId);
//            jPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, appJgPush.getType());
//            appJgPush.setIspush("1");
//            appJgPushService.update(appJgPush);
//        }
//    }
//}
