package com.jeesite.modules.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.dao.AppCleanIndetDao;
import com.jeesite.modules.app.dao.AppRescueIndentDao;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.dao.AppYearCheckIndentDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.app.lock.RedisLock;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.JgTokenEnum;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);

    @Resource
    private AppWxpayOrderService appWxpayOrderService;

    @Resource
    private AppCleanIndetDao appCleanIndetDao;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppRescueIndentDao appRescueIndentDao;

    @Resource
    private RedisLock redisLock;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private CallPhoneService callPhoneService;

    @Resource
    private AppSprayPaintIndentService appSprayPaintIndentService;

    @Resource
    private AppYearCheckIndentService appYearCheckIndentService;

    @Resource
    private AppYearCheckIndentDao appYearCheckIndentDao;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppUserBMessageService appUserBMessageService;


    /**
     * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(msgs)) {
            logger.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        logger.info("接受到的消息为：" + messageExt.toString());
        if (messageExt.getTopic().equals("all")) {
            if (messageExt.getTags().equals("clearCancelOrder")) {//洗车取消订单
                //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
                String cleanIndentNumber = new String(messageExt.getBody());
                logger.info("#### 收到洗车退款订单号cleanIndentNumber={}", cleanIndentNumber);
                String redisKey = RedisKeyUtils.CANCEL_CLEAN_INDENT + cleanIndentNumber;
                try {
                    redisLock.lock(redisKey);
                    AppCleanIndet appCleanIndetParamer = new AppCleanIndet();
                    appCleanIndetParamer.setCleanIndentNumber(cleanIndentNumber);
                    AppCleanIndet appCleanIndet = appCleanIndetDao.getByEntity(appCleanIndetParamer);

                    if (appCleanIndet.getPayState().equals("3")) {
                        logger.error("### 洗车订单已退款，重复消费！ appCleanIndet={} ", appCleanIndet);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    AppWxpayOrder appWxpayOrderParamer = new AppWxpayOrder();
                    appWxpayOrderParamer.setOutTradeNo(appCleanIndet.getMerchantsPayNumber());
                    AppWxpayOrder byEntity = appWxpayOrderDao.get(appWxpayOrderParamer);
                    appWxpayOrderService.doBack(byEntity, 1, cleanIndentNumber);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    logger.error("mq消费报错", e);
                } finally {
                    redisLock.unlock(redisKey);
                }
                //TODO 处理对应的业务逻辑
            } else if (messageExt.getTags().equals("rescueCancelOrder")) {
                String rescueIndentNumber = new String(messageExt.getBody());
                logger.info("#### 收到救援退款订单号rescueIndentNumber={}", rescueIndentNumber);
                //查询到订单信息
                AppRescueIndent appRescueIndentparamer = new AppRescueIndent();
                appRescueIndentparamer.setRescueNumber(rescueIndentNumber);
                AppRescueIndent appRescueIndent = appRescueIndentDao.getByEntity(appRescueIndentparamer);

                try {
                    appRescueIndentService.backMoney(appRescueIndent.getId());
                } catch (CusException e) {
                    logger.error("消费报错", e);
                }
            } else if (messageExt.getTags().equals("yearCheckCancelOrder")) {
                String yearCheckIndentNumber = new String(messageExt.getBody());
                logger.info("#### 收到年检退款订单号yearCheckIndentNumber={}", yearCheckIndentNumber);
                //查询到订单信息
                AppYearCheckIndent appYearCheckIndentparamer = new AppYearCheckIndent();
                appYearCheckIndentparamer.setYearCheckNumber(yearCheckIndentNumber);
                AppYearCheckIndent appYearCheckIndent = appYearCheckIndentDao.getByEntity(appYearCheckIndentparamer);

                try {
                    appYearCheckIndentService.backMoney(appYearCheckIndent.getId());
                } catch (CusException e) {
                    logger.error("消费报错", e);
                }

            } else if (messageExt.getTags().equals("jgts_xc")) {
                String clearIndetId = new String(messageExt.getBody());
                AppCleanIndet cleanIndet = appCleanIndetDao.selectByIndentNum(clearIndetId);
                String userBId = cleanIndet.getUserBId();
                AppBUser appBUser = appBUserService.get(userBId);
                String userId = cleanIndet.getUserId();
                AppUser appUser = appUserService.get(userId);
                String username = appUser.getUsername();
                String substring = username.substring(username.length() - 4);
                if (cleanIndet.getIndentState().equals("3")) {
                    String str = "收到尾号" + substring + "用户的洗车订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                } else {
                    String str = "您有新的洗车订单，请注意查收";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (messageExt.getTags().equals("jgts_mr")) {
                String clearIndetId = new String(messageExt.getBody());
                AppCleanIndet cleanIndet = appCleanIndetDao.selectByIndentNum(clearIndetId);
                String userBId = cleanIndet.getUserBId();
                AppBUser appBUser = appBUserService.get(userBId);
                String userId = cleanIndet.getUserId();
                AppUser appUser = appUserService.get(userId);
                String username = appUser.getUsername();
                String substring = username.substring(username.length() - 4);
                if (cleanIndet.getIndentState().equals("3")) {
                    String str = "收到尾号" + substring + "用户的美容订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                } else {
                    String str = "您有新的美容订单，请注意查收";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (messageExt.getTags().equals("jgts_jy")) {
                String rescueNumber = new String(messageExt.getBody());
                AppRescueIndent appRescueIndent = appRescueIndentService.findRescueByOrder(rescueNumber);
                Long userBId = appRescueIndent.getUserBId();
                AppBUser appBUser = appBUserService.get(String.valueOf(userBId));
                Long userId = appRescueIndent.getUserId();
                AppUser appUser = appUserService.get(String.valueOf(userId));
                String username = appUser.getUsername();
                String substring = username.substring(username.length() - 4);
                if (appRescueIndent.getState() == 1) {
                    //支付救援订单
                    String str = "您有新的救援订单,请注意查收";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                    callPhoneService.callPhone(appBUser.getMerchantsName() + "的", appBUser.getMerchantsPhone());
                } else if (appRescueIndent.getState() == 3) {
                    String str = "收到尾号" + substring + "用户的救援订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (messageExt.getTags().equals("jgts_PQ")) {
                String rescueNumber = new String(messageExt.getBody());
                AppSprayPaintIndent appSprayPaintIndent = appSprayPaintIndentService.findBySprayNumber(rescueNumber);

                Long userId = appSprayPaintIndent.getUserId();
                AppUser appUser = appUserService.get(String.valueOf(userId));
                String username = appUser.getUsername();
                String substring = username.substring(username.length() - 4);

                Integer id = appBUserService.selectBUserMessage(appSprayPaintIndent.getTechnicianId());
                AppBUser appBUser = appBUserService.get(String.valueOf(id));
                //等于1就是新订单
                if (appSprayPaintIndent.getState() == 1) {
                    String str = "您有新的喷漆订单,请注意查收";
                    //查询技师
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                } else if (appSprayPaintIndent.getState() == 2) {
                    //状态为2，支付状态不为1，商户报价
                    String str = "您的喷漆订单已收到商户报价,请及时查看";
                    JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "100");
                } else if (appSprayPaintIndent.getState() == 3) {
                    //状态为1，支付为1，商户支付成功
                    String str = "尾号" + substring + "的用户同意喷漆报价,请及时联系";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "100");
                    if (StringUtils.isNotEmpty(appBUser.getMerchantsPhone())) {
                        callPhoneService.callPhone(appBUser.getMerchantsName() + "的", appBUser.getMerchantsPhone());
                    }
                } else if (appSprayPaintIndent.getState() == 5) {
                    String str = "您的喷漆订单尾号" + substring + "的用户已经确认";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "100");
                } else if (appSprayPaintIndent.getState() == 6) {
                    String str = "您的爱车喷漆已经完成";
                    JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "100");
                } else if (appSprayPaintIndent.getState() == 8) {
                    String str = "收到尾号" + substring + "的喷漆订单已结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (messageExt.getTags().equals("jgts_NJ")) {
                String yearCheckNumber = new String(messageExt.getBody());
                AppYearCheckIndent appYearCheckIndent = appYearCheckIndentService.findByCheckNumber(yearCheckNumber);
                Integer userBId = appYearCheckIndent.getUserBId();
                AppBUser appBUser = appBUserService.get(userBId.toString());
                Long userId = appYearCheckIndent.getUserId();
                AppUser appUser = appUserService.get(userId.toString());
                String username = appUser.getUsername();
                String substring = username.substring(username.length() - 4);
                if (appYearCheckIndent.getState() == 2) {
                    String str = "您有年检订单用户已支付，请及时处理";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                    logger.error("### B端新年检订单 appBuser={}", appBUser.getUsername());
                    callPhoneService.callPhone(appBUser.getMerchantsName() + "的", appBUser.getMerchantsPhone());
                }
                if (appYearCheckIndent.getState() == 6) {
                    String str = "收到尾号" + substring + "用户的年检订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (messageExt.getTags().equals("jgts_SC")) {
                String subDriNumber = new String(messageExt.getBody());
                AppSubstituteDrivingIndent appSubDriIndent = appSubstituteDrivingIndentService.findBySubDri(subDriNumber);
                Integer bId = appSubDriIndent.getUserBId();
                AppUserBMessage appUserBMessage = appUserBMessageService.get(String.valueOf(bId));
                Integer userBId = appUserBMessage.getUserBId();
                AppBUser appBUser = appBUserService.get(userBId.toString());
                Long userId = appSubDriIndent.getUserId();
                AppUser appUser = appUserService.get(userId.toString());
                String username = appBUser.getUsername();
                String substring = username.substring(username.length() - 4);
                if (appSubDriIndent.getIndentState() == 1) {
                    String str = "您有新的代驾订单,请注意查收";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "6");
                    //查询到技师电话
                   // AppUserBMessage employee = appUserBMessageService.findEmployee(userBId.toString(), 4);
                    AppUserBMessage appUserBMessage1 = appUserBMessageService.get(appSubDriIndent.getUserBId().toString());
                    callPhoneService.callPhone(appUserBMessage1.getName() + "的", appUserBMessage1.getPhone());
                } else if (appSubDriIndent.getIndentState() == 2) {
                    //状态为2，支付状态不为1，商户报价
                    String str = "您的代驾订单已经接单啦";
                    JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "100");
                } else if (appSubDriIndent.getIndentState() == 3) {
                    //状态为1，支付为1，商户支付成功
                    String str = "您的订单已经开始，请系好安全带";
                    JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "100");
                } else if (appSubDriIndent.getIndentState() == 5) {
                    String str = "您已到达目的地，请及时确认订单";
                    JPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, "100");
                } else if (appSubDriIndent.getIndentState() == 6) {
                    String str = "收到尾号" + substring + "用户的代驾订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                }else if (appSubDriIndent.getIndentState()==8){

                    try {
                        appSubstituteDrivingIndentService.backMoney(appSubDriIndent.getId());
                    } catch (CusException e) {
                        logger.error("消费报错", e);
                    }
                }
            }
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


}
