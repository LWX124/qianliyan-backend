//package com.jeesite.modules.job.service;
//
//import com.jeesite.modules.app.entity.AppBUser;
//import com.jeesite.modules.app.entity.AppUser;
//import com.jeesite.modules.app.service.AppBUserService;
//import com.jeesite.modules.app.service.AppUserService;
//import com.jeesite.modules.app.service.JPushService;
//import com.jeesite.modules.constant2.JgTokenEnum;
//import net.sf.json.JSONObject;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.List;
//
//@Component
//public class Consumer {
//    @Resource
//    private AppUserService appUserService;
//
//    @Resource
//    private AppBUserService appBUserService;
//
//    @Resource
//    private JPushService jPushService;
//
//    private final static Logger logger = LoggerFactory.getLogger(Consumer.class);
//    private static final String ADDR = "127.0.0.1:9876";
//
//    @PostConstruct
//    public void consumer() throws MQClientException {
//        logger.info("init defaultMQPushConsumer");
//        //设置消费者组
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_LRW_DEV_SUBS");
//
//        consumer.setVipChannelEnabled(false);
//        consumer.setNamesrvAddr(ADDR);
//        //设置消费者端消息拉取策略，表示从哪里开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//
//        //设置消费者拉取消息的策略，*表示消费该topic下的所有消息，也可以指定tag进行消息过滤
//        consumer.subscribe("TopicTest", "*");
//
//        //消费者端启动消息监听，一旦生产者发送消息被监听到，就打印消息，和rabbitmq中的handlerDelivery类似
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                for (MessageExt messageExt : msgs) {
//                    String topic = messageExt.getTopic();
//                    String tag = messageExt.getTags();
//                    String msg = new String(messageExt.getBody());
//                    logger.error("###消费响应：当前时间### msgId={};msgBody={};tag={};topic={}", messageExt.getMsgId(), msg, tag, topic);
//                    JSONObject jsonObject = JSONObject.fromObject(msg);
//                    String source = jsonObject.getString("source");
//                    String type = jsonObject.getString("type");
//                    String userBId = jsonObject.getString("userBId");
//                    String userId = jsonObject.getString("userId");
//
//                    String str = "您有新的通知";
//                    if (type.equals("1")) {
//                        str = "您有新的事故视频,点击查看";
//                    }
//                    if (type.equals("2")) {
//                        str = "您有订单完成并通过,点击查看";
//                    }
//                    if (type.equals("3")) {
//                        str = "您提交的事故已经审核成功,红包已经到账,点击查看";
//                    }
//                    if (type.equals("4")) {
//                        str = "您有新的订单,请注意查收,点击查看";
//                    }
//                    if (type.equals("5")) {
//                        str = "您有订单的状态发生改变,点击查看";
//                    }
//
//                    if (source.equals("C")) {
//                        AppUser appUser = appUserService.get(userId);
//                        jPushService.jiguangPush(appUser.getUsername(), str, JgTokenEnum.C, type);
//                    } else {
//                        AppBUser appBUser = appBUserService.get(userBId);
//                        jPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, type);
//                    }
//                }
//
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//
//        //调用start()方法启动consumer
//        consumer.start();
//        System.out.println("Consumer Started....");
//    }
//}
